package com.devkraft.karmahealth.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.devkraft.karmahealth.Utils.AppUtils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                // Set the alarm here.
                if (context != null)
                    AppUtils.setAlarmAfterBoot(context);
            }
        }
    }
}