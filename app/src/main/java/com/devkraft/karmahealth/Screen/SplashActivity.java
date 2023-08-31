package com.devkraft.karmahealth.Screen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Pref;
import com.devkraft.karmahealth.retrofit.Log;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Window;
import android.widget.Toast;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    public Pref mPref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    AlertDialog.Builder builder;
    private final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setUpAppLanguage();
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        showDialogBoxForBatteryOptimization();
       /* final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean pid  = sharedPreferences.getBoolean("Login",false);
                if(pid == true){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                   // getApplicationContext().deleteDatabase("KymApplication.db");
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);*/
    }

    private void gotonextScreen(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean pid  = sharedPreferences.getBoolean("Login",false);
                if(pid == true){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    // getApplicationContext().deleteDatabase("KymApplication.db");
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }


    private void showDialogBoxForBatteryOptimization() {
        Log.i("checkbopup","data of bank of indian");
        String lastLogDate = ApplicationPreferences.get().getLastBatteryOptimizationDate();
        if (AppUtils.isNeedToShowBatteryOptimization(lastLogDate)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                String packageName = getPackageName();
                if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                    builder = new AlertDialog.Builder(this);

                    builder.setMessage(getString(R.string.drug_reminders_msg))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.proceed), (dialog, id) -> {
                                Log.i("checkbopup","data of bank of indian = 1");
                                ApplicationPreferences.get().logBatteryOptimizationDate(AppUtils.getTodayDate());
                                askPermissionForBatteryOptimization();
                                dialog.cancel();
                            })
                            .setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
                                Log.i("checkbopup","data of bank of indian = 2");
                                ApplicationPreferences.get().logBatteryOptimizationDate(AppUtils.getTodayDate());
                                dialog.cancel();
                                gotonextScreen();
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle(getString(R.string.drug_reminders));
                    alert.show();
                }else {
                    Log.i("checkbopup","data of bank of indian = 3");
                    gotonextScreen();
                }
            }
        }else{
            gotonextScreen();
        }
    }

    private void askPermissionForBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @SuppressLint("BatteryLife")
            Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (pm != null) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
                if (!wl.isHeld()) {
                    wl.acquire();
                }

                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                    gotonextScreen();
                    Log.i("checkbopup","data of bank of indian = 4");
                }else{

                    Log.i("checkbopup","data of bank of indian = 5");
                    gotonextScreen();
                }
            }else{
                Log.i("checkbopup","data of bank of indian = 6");
                gotonextScreen();
            }
        }
    }

    public void setUpAppLanguage(){
        mPref = new Pref(this);

        ApplicationPreferences.init(getApplicationContext());
        String appLang = "hi";
    /*    if (appLang == "en") {
            String deviceLang = Resources.getSystem().getConfiguration().locale.getLanguage();
            Locale locale = new Locale(deviceLang);
            Locale.setDefault(locale);

            Resources resources = this.getResources();
            Configuration configuration = resources.getConfiguration();

            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }else {*/
            Locale locale =new Locale(appLang);
            Locale.setDefault(locale);
            Resources resources = this.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
       /* }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("checkbopup","requestCode = "+String.valueOf(requestCode));
        Log.i("checkbopup","resultCode = "+String.valueOf(resultCode));
        Log.i("checkbopup","data = "+String.valueOf(data.getData()));
    }
}