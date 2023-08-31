package com.devkraft.karmahealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
import android.telephony.SmsMessage;
public class MySMSBroadcastReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    Boolean b=true;
    String abcd, xyz;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        try{
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String sender = smsMessage.getDisplayOriginatingAddress();
                // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
                String messageBody = smsMessage.getMessageBody();
                abcd = messageBody.replaceAll("[^0-9]", ""); // here abcd contains otp which is in number format
                //Pass on the text to our listener.
                if(abcd !=null){
                    if(!abcd.isEmpty()){
                        if (b == true) {
                            mListener.messageReceived(abcd); // attach value to interface object
                        } else {}
                    }
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}