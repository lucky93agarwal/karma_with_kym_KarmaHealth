package com.devkraft.karmahealth;

import android.app.Application;
import android.app.NotificationManager;

import com.clevertap.android.sdk.CleverTapAPI;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
//import com.zoho.commons.Fonts;
//import com.zoho.commons.InitConfig;
//import com.zoho.livechat.android.listeners.InitListener;
//import com.zoho.salesiqembed.ZohoSalesIQ;

public class KymApplication  extends Application {
    private static final String APP_NAME = "KymApplication";
    private static final String YOUR_APP_ID = "42aab10e-22db-4ba4-bb4f-ff71ac7d4386";
    private static final String YOUR_APP_KEY = "37a46e18-7137-4cd5-8f98-37a002af3a84";
    private static final String ZOHO_APP_KEY = "0w27IRIbmGUH%2ByUN%2FFfexJn6izg1Y%2BdHl9L39dkTxDPeh3%2F0hvSx8CyTm%2BwHDCPz_in";
    private static final String ZOHO_APP_ACCESS_KEY = "1N9nVtv6RPVpntS63x9xl%2BWJgZUk3ILz6P2rUSIJZBc3C3n0Qz84YvctU5Ny2MpZLx95jq9Xm5zyZvE3suMgUCUvg1vEmylngDtaZG%2Fcmg7jvCwYF9ckguaMGHjmPyF892S3W%2BVgUZwGkBVkpBmcjw9%2FPHFfL%2BwhI%2FBU1uDt6Mo%3D";
    @Override
    public void onCreate() {
     //   Freshchat.init();

 //       InitConfig initConfig = new InitConfig();
//        initConfig.setFont(Fonts.REGULAR,"");

//        ZohoSalesIQ.init(this, ZOHO_APP_KEY, ZOHO_APP_ACCESS_KEY, initConfig, new InitListener() {
//            @Override
//            public void onInitSuccess() {
//                ZohoSalesIQ.showLauncher(true);
//            }
//
//            @Override
//            public void onInitError(int errorCode, String errorMessage) {
//                //your code
//            }
//        });
        // Initialize Application Preferences. Use ApplicationPreferences singleton
        // to access preferences from any activity or fragment. PersistentPreferences
        ApplicationPreferences.init(getApplicationContext());

        // Initialize Database object. Use a singleton object to provide access to
        // the database helper
        ApplicationDB.init(getApplicationContext(), APP_NAME);

        // Initialize Volley for network calls
        ApiService.init(getApplicationContext());

        // Initialize API URLs to point to correct server location (prod/test)
        APIUrls.init(getURLPrefix(), getURLPrefixV2());


        CleverTapAPI.createNotificationChannel(getApplicationContext(),"kym",
                "Kym_Android","Android Push Notifications Channel",
                NotificationManager.IMPORTANCE_HIGH,false);
    }
    private String getURLPrefixV2() {
        return BuildConfig.URL_PREFIX_V2;
    }
    private String getURLPrefix() {
        return BuildConfig.URL_PREFIX;
    }
}
