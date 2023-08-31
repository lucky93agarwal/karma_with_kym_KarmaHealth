package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;


public class AppsDevicesActivity extends AppCompatActivity {
    private TextView mTextViewConnect;
    private ImageView mImageBack;
    private SwitchCompat heartRateSwitchCompat, waterSwitchCompat, weightSwitchCompat,
            sleepSwitchCompat, mRestingHeartRateSwitchCompat;
    private LinearLayout fitBitSwitchesLayout, mParentLayout;
    private ProgressDialogSetup mProgressDialogSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_devices);

        AppUtils.changeStatusBarColor(AppsDevicesActivity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeIds();
        showProgress();
        setOnClickListeners();
        checkFitBitConnected();
        AppUtils.logCleverTapEvent(AppsDevicesActivity.this,
                Constants.APPS_AND_DEVICES_SCREEN_OPENED, null);
    }

    private void showProgress() {
        if(mProgressDialogSetup != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> mProgressDialogSetup.dismiss(), 2000);
        }
    }

    private void checkFitBitConnected() {
        if (getIntent().getExtras() != null) {
            String fitBitConnected = ApplicationPreferences.get().getStringValue("FitBit-Connected");
            if (fitBitConnected != null && fitBitConnected.equalsIgnoreCase("true")) {
                mTextViewConnect.setText(getString(R.string.disconnect));
                fitBitSwitchesLayout.setVisibility(View.VISIBLE);
                String isDialogShown = ApplicationPreferences.get()
                        .getStringValue(Constants.FITBIT_CONNECTED_DIALOG_SHOWN);
                if(isDialogShown != null && !isDialogShown.matches("") && isDialogShown.
                        matches("true")) {
                    showConnectedDialog();
                }
            } else {
                mTextViewConnect.setText(getString(R.string.connect));
            }
        }

        String isWaterSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WATER_SWITCH);
        if (isWaterSwitchOn != null && !isWaterSwitchOn.equalsIgnoreCase("")) {
            waterSwitchCompat.setChecked(isWaterSwitchOn.equalsIgnoreCase("true"));
        } else {
            waterSwitchCompat.setChecked(true);
        }

        String isWeightSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WEIGHT_SWITCH);
        if (isWeightSwitchOn != null && !isWeightSwitchOn.equalsIgnoreCase("")) {
            weightSwitchCompat.setChecked(isWeightSwitchOn.equalsIgnoreCase("true"));
        } else {
            weightSwitchCompat.setChecked(true);
        }

        String isHeartRateSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_HEART_RATE_SWITCH);
        if (isHeartRateSwitchOn != null && !isHeartRateSwitchOn.equalsIgnoreCase("")) {
            heartRateSwitchCompat.setChecked(isHeartRateSwitchOn.equalsIgnoreCase("true"));
        } else {
            heartRateSwitchCompat.setChecked(true);
        }

        String isRestingHeartRateSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_RESTING_HEART_SWITCH);
        if (isRestingHeartRateSwitchOn != null && !isRestingHeartRateSwitchOn.equalsIgnoreCase("")) {
            mRestingHeartRateSwitchCompat.setChecked(isRestingHeartRateSwitchOn.equalsIgnoreCase("true"));
        } else {
            mRestingHeartRateSwitchCompat.setChecked(false);
        }

        String isSleepSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_SLEEP_SWITCH);
        if (isSleepSwitchOn != null && !isSleepSwitchOn.equalsIgnoreCase("")) {
            sleepSwitchCompat.setChecked(isSleepSwitchOn.equalsIgnoreCase("true"));
        } else {
            sleepSwitchCompat.setChecked(true);
        }
    }

    private void initializeIds() {
        mProgressDialogSetup = ProgressDialogSetup.getProgressDialog(AppsDevicesActivity.this);
        mProgressDialogSetup.setCancelable(false);
        mProgressDialogSetup.show();
        mTextViewConnect = findViewById(R.id.text_view_connect);
        mImageBack = findViewById(R.id.image_back);
        heartRateSwitchCompat = findViewById(R.id.heart_switch_compat);
        waterSwitchCompat = findViewById(R.id.water_switch_compat);
        weightSwitchCompat = findViewById(R.id.weight_switch_compat);
        sleepSwitchCompat = findViewById(R.id.sleep_switch_compat);
        mRestingHeartRateSwitchCompat = findViewById(R.id.resting_heart_switch_compat);
        fitBitSwitchesLayout = findViewById(R.id.fitbit_switches_layout);
        mParentLayout = findViewById(R.id.parent_layout);
    }

    private void setOnClickListeners() {
        mImageBack.setOnClickListener(v -> onBackPressed());

        mTextViewConnect.setOnClickListener(v -> {
            if (mTextViewConnect.getText().toString().equalsIgnoreCase(getString(R.string.connect))) {
                if (AppUtils.isNetworkAvailable(AppsDevicesActivity.this)) {
                    startActivity(new Intent(AppsDevicesActivity.this, FitBitLoginActivity.class));
                    finish();
                } else {
                    AppUtils.openSnackBar(mParentLayout, getResources()
                            .getString(R.string.no_internet_msg_gr));
                }
            } else {
                showDialogBox();
            }
        });

        heartRateSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_HEART_RATE_SWITCH,
                        isChecked + "");
                if(isChecked){
                    AppUtils.addHeartRateParameterDtoValue(AppsDevicesActivity.this);
                    AppUtils.syncFitBitData(AppsDevicesActivity.this,
                            false,  true);
                }
            }
        });

        mRestingHeartRateSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_RESTING_HEART_SWITCH,
                        isChecked + "");
                if(isChecked){
                    AppUtils.addRestingHeartRateParameterDtoValue(AppsDevicesActivity.this);
                    AppUtils.syncFitBitData(AppsDevicesActivity.this,
                            false, true);
                }
            }
        });

        waterSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_WATER_SWITCH,
                        isChecked + "");
                if(isChecked){
                    AppUtils.addWaterParameterDtoValue(AppsDevicesActivity.this);
                    AppUtils.syncFitBitData(AppsDevicesActivity.this,
                            false, true);
                }
            }
        });

        sleepSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_SLEEP_SWITCH,
                        isChecked + "");
                if(isChecked){
                    AppUtils.addSleepParameterDtoValue(AppsDevicesActivity.this);
                    AppUtils.syncFitBitData(AppsDevicesActivity.this,
                            false, true);
                }
            }
        });

        weightSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_WEIGHT_SWITCH,
                        isChecked + "");
                if(isChecked){
                    AppUtils.addWeightParameterDtoValue(AppsDevicesActivity.this);
                    AppUtils.syncFitBitData(AppsDevicesActivity.this,
                            false, true);
                }
            }
        });
    }

    private void showDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppsDevicesActivity.this, R.style.AlertDialogTheme);
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_disconnect_fit_bit))
                .setTitle(getString(R.string.alert));

        builder.setPositiveButton(getString(R.string.yes), (dialog, id) -> {
            dialog.dismiss();
            AppUtils.logCleverTapEvent(AppsDevicesActivity.this,
                    Constants.FITBIT_DISCONNECTED, null);
            mTextViewConnect.setText(getString(R.string.connect));
            ApplicationPreferences.get().saveStringValue("FitBit-Connected", "false");
            fitBitSwitchesLayout.setVisibility(View.GONE);
        });

        builder.setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConnectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppsDevicesActivity.this,
                R.style.AlertDialogTheme);
        builder.setMessage(getString(R.string.you_can_see))
                .setTitle(getString(R.string.connected));
        builder.setCancelable(true);

        builder.setPositiveButton(getString(R.string.ok), (dialog, id) -> {
            ApplicationPreferences.get().saveStringValue(Constants.FITBIT_CONNECTED_DIALOG_SHOWN, false + "");
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}