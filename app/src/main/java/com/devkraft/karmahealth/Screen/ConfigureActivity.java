package com.devkraft.karmahealth.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Adapter.MonthlySpinnerAdapter;
import com.devkraft.karmahealth.Adapter.WeeklySpinnerAdapter;
import com.devkraft.karmahealth.Model.ConfigureDto;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.Ranges;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.WeeklyDays;
import com.devkraft.karmahealth.Model.Weights;
import com.devkraft.karmahealth.R;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Adapter.ReportVitalAdapter;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.ReportDto;
import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.FindParameterAdapter;
import com.devkraft.karmahealth.Adapter.MonthlySpinnerCustomDrugAdapter;
import com.devkraft.karmahealth.Adapter.ParameterSpinnerAdapter;
import com.devkraft.karmahealth.Adapter.UserSpinnerAdapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.CustomParamDto;
import com.devkraft.karmahealth.Model.CustomparamterDto;
import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Model.ParamDto;
import com.devkraft.karmahealth.Model.ParameterSearchResultDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ExpandableHeightListView;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.fragment.ConfigureFragment;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HICondition;
import com.highsoft.highcharts.common.hichartsclasses.HICredits;
import com.highsoft.highcharts.common.hichartsclasses.HIEvents;
import com.highsoft.highcharts.common.hichartsclasses.HIExporting;
import com.highsoft.highcharts.common.hichartsclasses.HILabel;
import com.highsoft.highcharts.common.hichartsclasses.HIMarker;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPoint;
import com.highsoft.highcharts.common.hichartsclasses.HIResponsive;
import com.highsoft.highcharts.common.hichartsclasses.HIRules;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.highsoft.highcharts.core.HIFunction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;


public class ConfigureActivity extends AppCompatActivity {
    private View parentView;
    private UserDto userDto;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private String fromWhichDisease;
    private boolean fragmentResume = false,
            fragmentVisible = false, fragmentOnCreated = false;
    private EditText systolicValue, diastolicValue, mgDlValue,
            customConfiguredParameterEt, minConfiguredParameterEt;
    private TextView systolicMaxValue, systolicMinValue,
            diastolicMaxValue, diastolicMinValue, headerMsgTv, mgDlMaxValue, mgDlMinValue,
            diastolicHeader, systolicHeader, underTv, normalTv,
            overTv, obeseTv, extremelyTv, weighMinValue, weighMaxValue, customConfiguredParameterTv;
    private Spinner frequencySp, daySp, monthlySp, cmsSp, ftSp, incsSp;
    private LinearLayout frequencySpLl, daySpLl, weeklyLl, monthlyLl,
            bloodPressureLl, bloodPressurePpLl, customConfiguredParameterLL, weightLl,
            ftIncsLl, ftLl, inclSpLl, cmParentLl;

    private HashMap<String, Object> configurationMap = new HashMap<>();

    private Button saveBtn;
    private String[] ftArray;
    private String[] incArray;
    private RelativeLayout tempRl;
    private ParameterDto vitalDto;
    private List<String> cmsString;
    private ConfigureDto configureDto;
    private RelativeLayout mParentView;
    private ProgressDialogSetup progress;
    private List<WeeklyDays> weeklyDaysList;
    private List<MonthlyDays> monthlyDaysList;
    private boolean userIsInteracting = false;
    private ConfigureFragment.OnSaveListener onSaveListener;
    private UserInteractionListener userInteractionListener;
    private SwitchCompat heightSwitchCompat, weightSwitchCompat, tempSwitchCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        setupToolbar();
        initializeIds();
        getValueFromArguments(getIntent());
        handleClickEvent();
        AppUtils.hideKeyboard(this);

        this.setUserInteractionListener(() -> userIsInteracting = true);
    }

    public void setUserInteractionListener(UserInteractionListener userInteractionListener) {
        this.userInteractionListener = userInteractionListener;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (userInteractionListener != null)
            userInteractionListener.onUserInteraction();
    }

    public interface UserInteractionListener {
        void onUserInteraction();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.configure));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }

    private void initializeIds() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        progress = ProgressDialogSetup.getProgressDialog(this);

        tempRl = findViewById(R.id.tempRl);
        overTv = findViewById(R.id.overTv);
        obeseTv = findViewById(R.id.obeseTv);
        underTv = findViewById(R.id.underTv);
        normalTv = findViewById(R.id.normalTv);
        extremelyTv = findViewById(R.id.extremelyTv);
        weighMinValue = findViewById(R.id.weighMinValue);
        weighMaxValue = findViewById(R.id.weighMaxValue);
        mParentView = findViewById(R.id.parent_view_configure);
        tempSwitchCompat = findViewById(R.id.tempSwitchCompat);

        systolicValue = findViewById(R.id.systolicValue);
        systolicMaxValue = findViewById(R.id.systolicMaxValue);
        systolicMinValue = findViewById(R.id.systolicMinValue);

        daySp = findViewById(R.id.daySp);
        daySpLl = findViewById(R.id.daySpLl);
        monthlySp = findViewById(R.id.monthlySp);
        frequencySp = findViewById(R.id.frequencySp);
        diastolicValue = findViewById(R.id.diastolicValue);
        diastolicMaxValue = findViewById(R.id.diastolicMaxValue);
        diastolicMinValue = findViewById(R.id.diastolicMinValue);
        frequencySpLl = findViewById(R.id.frequencySpLl);

        saveBtn = findViewById(R.id.saveBtn);
        weeklyLl = findViewById(R.id.weeklyLl);
        monthlyLl = findViewById(R.id.monthlyLl);
        headerMsgTv = findViewById(R.id.headerMsgTv);
        bloodPressureLl = findViewById(R.id.bloodPressureLl);

        mgDlValue = findViewById(R.id.mgDlValue);
        bloodPressurePpLl = findViewById(R.id.bloodPressurePpLl);

        mgDlMaxValue = findViewById(R.id.mgDlMaxValue);
        mgDlMinValue = findViewById(R.id.mgDlMinValue);

        weightLl = findViewById(R.id.weightLl);
        diastolicHeader = findViewById(R.id.diastolicHeader);
        systolicHeader = findViewById(R.id.systolicHeader);

        ftLl = findViewById(R.id.ftLl);
        ftIncsLl = findViewById(R.id.ftIncsLl);
        inclSpLl = findViewById(R.id.inclSpLl);
        cmParentLl = findViewById(R.id.cmParentLl);

        ftSp = findViewById(R.id.ftSp);
        cmsSp = findViewById(R.id.cmsSp);
        incsSp = findViewById(R.id.incsSp);

        heightSwitchCompat = findViewById(R.id.heightSwitchCompat);
        weightSwitchCompat = findViewById(R.id.weightSwitchCompat);

        minConfiguredParameterEt = findViewById(R.id.minConfiguredParameterEt);
        customConfiguredParameterLL = findViewById(R.id.customConfiguredParameterLL);
        customConfiguredParameterTv = findViewById(R.id.customConfiguredParameterTv);
        customConfiguredParameterEt = findViewById(R.id.customConfiguredParameterEt);

        heightSwitchCompat.setChecked(false);
        weightSwitchCompat.setChecked(false);

        heightSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        weightSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        tempSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        setFtSpValues();
        setCmsSpValues();

    }

    private void setFtSpValues() {
        ftArray = getResources().getStringArray(R.array.ft_adapter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_disease, ftArray);
        ftSp.setAdapter(dataAdapter);
    }

    private void setCmsSpValues() {
        cmsString = new ArrayList<>();
        for (int i = 147; i <= 203; i++) {
            cmsString.add(i + "");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_disease, cmsString);
        cmsSp.setAdapter(adapter);
    }


    private void getValueFromArguments(Intent arguments) {
        if (arguments != null) {
            Gson gson = new Gson();
            String userDtoStr = arguments.getStringExtra(Constants.USER_DTO);
            fromWhichDisease = arguments.getStringExtra(Constants.WHICH_DISEASE);

            Log.e("fromWhichDisease", " = " + fromWhichDisease);

            String vitalDtoStr = arguments.getStringExtra(Constants.PARAMETER_DTO);

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                vitalDto = gson.fromJson(vitalDtoStr, ParameterDto.class);

               // callGetParameterAPI();
                apiGetParameterAPI();
            }
        }
    }

    private void apiGetParameterAPI(){
        Log.i("checkmodrug", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        Log.i("checkmodrug", "drug request user id = 589 " + String.valueOf(vitalDto.getId()));
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.getParameterAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(vitalDto.getId()),token).enqueue(new Callback<ConfigureDto>() {
            @Override
            public void onResponse(Call<ConfigureDto> call, retrofit2.Response<ConfigureDto> response) {
                Log.i("checkmodrug", "api login response 0 code = " + response.code());
                Log.i("checkmodrug", "api login response 1245  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.hideProgressBar(progress);
                    configureDto = response.body();
                    updateUI(response.body());
                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    AppUtils.openSnackBar(mParentView, "Please try after some time.");
                    Log.i("checkmodrug", "api response 1 code = " + response.code());
                    AppUtils.hideProgressBar(progress);
                }
            }

            @Override
            public void onFailure(Call<ConfigureDto> call, Throwable t) {
                AppUtils.openSnackBar(mParentView, t.getMessage());
                Log.i("checkmodrug", "api error message response  = " + t.getMessage());
                AppUtils.hideProgressBar(progress);
            }
        });
    }
    public void refreshToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(sharedPreferences.getString("refreshToken",""));
        Log.i("refreshToken", "refreshToken api request 278 = " + new Gson().toJson(request));

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, retrofit2.Response<RefreshTokenResponse> response) {
                Log.i("refreshToken", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("refreshToken", "prescription api response = " + new Gson().toJson(response.body()));

                    edit.putString("Ptoken", response.body().accessToken);
                    edit.apply();
                    AppUtils.openSnackBar(mParentView, "Try Again");
                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(ConfigureActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });

    }
    private void callGetParameterAPI() {
        if (userDto != null && vitalDto != null) {

            if (progress != null) {
                progress.show();
            }
            Log.i("chekcurlcheck","callGetParameterAPI 2  = "+ APIUrls.get().getConfigurationParameter(userDto.getId(), vitalDto.getUserParameterId()));
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<ConfigureDto> getDiseaseParameter = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().getConfigurationParameter(userDto.getId(), vitalDto.getUserParameterId()),
                            ConfigureDto.class, null,
                            cDto -> {
                                AppUtils.hideProgressBar(progress);
                                configureDto = cDto;
                                updateUI(cDto);
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentView, res);
                            });
            authExpiredCallback.setRequest(getDiseaseParameter);
            ApiService.get().addToRequestQueue(getDiseaseParameter);
        }
    }

    public static class DecimalDigitsInputFilter implements InputFilter {
        int digitsBeforeZero;
        int digitsAfterZero;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            this.digitsBeforeZero = digitsBeforeZero;
            this.digitsAfterZero = digitsAfterZero;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (dest != null && dest.toString().trim().length() < (digitsBeforeZero + digitsAfterZero)) {
                String value = dest.toString().trim();
                if (value.contains(".") && (value.substring(value.indexOf(".")).length() < (digitsAfterZero + 1))) {
                    return ((value.indexOf(".") + 1 + digitsAfterZero) > dstart) ? null : "";
                } else if (value.contains(".") && (value.indexOf(".") < dstart)) {
                    return "";
                } else if (source != null && source.equals(".") && ((value.length() - dstart) >= (digitsAfterZero + 1))) {
                    return "";
                }

            } else {
                return "";
            }
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(final ConfigureDto configureDto) {

        if (this != null) {
            ConfigureActivity.this.runOnUiThread(() -> {
                Context context = this;
                if (context != null) {
                    String msg = context.getString(R.string.lets_add_your_ideal);
                    String msg1 = context.getString(R.string.reading_recommended_by_doctor);
                    if (fromWhichDisease != null) {

                        if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {
                            msg = msg.concat(" " + getString(R.string.resting_heart_rate) + " ");
                        } else {
                            msg = msg.concat(" " + fromWhichDisease + " ");
                        }

                        msg = msg.concat(msg1);
                        headerMsgTv.setText(msg +" "+ getString(R.string.we_will_use_this));

                        // setting layout according to different vitals

                        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS) ||
                                fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate) ||
                                fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose) ||
                                fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate) ||
                                fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

                            systolicValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                            diastolicValue.setInputType(InputType.TYPE_CLASS_NUMBER);

                            bloodPressureLl.setVisibility(View.VISIBLE);
                            bloodPressurePpLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);


                            // changing seekBars headers value

                            if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {
                                systolicHeader.setText(getString(R.string.maximum));
                                diastolicHeader.setText(getString(R.string.minimum));

                                tempRl.setVisibility(View.VISIBLE);
                                systolicValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                diastolicValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                                systolicValue.setFilters(new InputFilter[]{new ConfigureFragment.DecimalDigitsInputFilter(5, 1)});
                                diastolicValue.setFilters(new InputFilter[]{new ConfigureFragment.DecimalDigitsInputFilter(5, 1)});

                                if (configureDto != null) {
                                    String measurementUnit = configureDto.getMeasurementUnit();
                                    if (measurementUnit != null) {
                                        tempSwitchCompat.setChecked(measurementUnit.equalsIgnoreCase(Constants.C));
                                    }
                                }
                            } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {
                                systolicHeader.setText(getString(R.string.systolic));
                                diastolicHeader.setText(getString(R.string.diastolic));
                                tempRl.setVisibility(View.GONE);


                                systolicMinValue.setText(AppUtils.SYSTOLIC_MIN_VALUE + "");
                                systolicMaxValue.setText(AppUtils.SYSTOLIC_MAX_VALUE + "");


                                diastolicMinValue.setText(AppUtils.MIN_VALUE + "");
                                diastolicMaxValue.setText(AppUtils.DIASTOLIC_MAX_VALUE + "");

                            } else if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {
                                systolicHeader.setText(getString(R.string.bpm_max));
                                diastolicHeader.setText(getString(R.string.bpm_min));

                                tempRl.setVisibility(View.GONE);


                                systolicMinValue.setText(AppUtils.MIN_VALUE + "");
                                systolicMaxValue.setText(AppUtils.HEART_RATE_MAX_VALUE + "");


                                diastolicMinValue.setText(AppUtils.MIN_VALUE + "");
                                diastolicMaxValue.setText(AppUtils.HEART_RATE_MAX_VALUE + "");


                            } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose)) {
                                systolicHeader.setText(getString(R.string.max_mgdl));
                                diastolicHeader.setText(getString(R.string.min_mgdl));

                                tempRl.setVisibility(View.GONE);


                                systolicMinValue.setText(AppUtils.MIN_VALUE + "");
                                systolicMaxValue.setText(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "");


                                diastolicMinValue.setText(AppUtils.MIN_VALUE + "");
                                diastolicMaxValue.setText(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "");

                            } else if (fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {

                                systolicHeader.setText(getString(R.string.breaths_max));
                                diastolicHeader.setText(getString(R.string.breaths_min));

                                tempRl.setVisibility(View.GONE);
                            }

                            if (configureDto != null) {

                                Double maxBaselineValue = configureDto.getMaxBaselineValue();
                                Double minBaselineValue = configureDto.getMinBaselineValue();
                                if (maxBaselineValue != null)
                                    systolicValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue));
                                if (minBaselineValue != null)
                                    diastolicValue.setText(AppUtils.getValueWithoutDecimal(minBaselineValue));
                                else
                                    diastolicValue.setText(AppUtils.getValueWithoutDecimal(0));
                            }

                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {
                            bloodPressureLl.setVisibility(View.GONE);
                            bloodPressurePpLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);

                            weightLl.setVisibility(View.VISIBLE);

                            if (configureDto != null) {
                                String measurementUnit = configureDto.getMeasurementUnit();
                                Double maxBaselineValue = configureDto.getMaxBaselineValue();
                                DecimalFormat df = new DecimalFormat("#");
                                if (measurementUnit != null) {
                                    if (measurementUnit.equalsIgnoreCase(Constants.CMS)) {
                                        heightSwitchCompat.setChecked(true);

                                        ftIncsLl.setVisibility(View.GONE);
                                        cmParentLl.setVisibility(View.VISIBLE);


                                        setCmsSpValues();
                                        if (cmsString != null && cmsString.size() > 0) {
                                            int pos = cmsString.indexOf(df.format(maxBaselineValue));
                                            cmsSp.setSelection(pos);
                                        }

                                    } else if (measurementUnit.equalsIgnoreCase(Constants.INCH)) {
                                        heightSwitchCompat.setChecked(false);

                                        ftIncsLl.setVisibility(View.VISIBLE);
                                        cmParentLl.setVisibility(View.GONE);


                                        int feet = AppUtils.getFeet(Integer.parseInt(df.format(maxBaselineValue)));
                                        int feetPos = -1;
                                        if (ftArray != null) {
                                            feetPos = Arrays.asList(ftArray).indexOf(feet + "");
                                            ftSp.setSelection(feetPos);
                                        }

                                        if (feetPos != -1) {
                                            updateIncArray(feetPos);
                                            int incs = AppUtils.getIncs(Integer.parseInt(df.format(maxBaselineValue)));
                                            if (incArray != null) {
                                                int pos = Arrays.asList(incArray).indexOf(incs + "");
                                                incsSp.setSelection(pos);
                                            }
                                        }

                                    }
                                }
                            }

                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_PP)) {
                            bloodPressureLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            bloodPressurePpLl.setVisibility(View.VISIBLE);
                            tempRl.setVisibility(View.GONE);

                            mgDlMinValue.setText(AppUtils.MIN_VALUE + "");
                            mgDlMaxValue.setText(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "");

                            if (configureDto != null) {
                                Double maxBaselineValue = configureDto.getMaxBaselineValue();
                                mgDlValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue));
                                //mgDlValue.setSelection(maxBaselineValue.toString().length());
                            }
                        } else {
                            bloodPressureLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);
                            bloodPressureLl.setVisibility(View.GONE);
                            bloodPressurePpLl.setVisibility(View.GONE);
                            if (configureDto.getMaxBaselineValue() != null) {
                                customConfiguredParameterEt.setText(AppUtils.getValueWithoutDecimal(configureDto.getMaxBaselineValue()));
                            }
                            customConfiguredParameterLL.setVisibility(View.VISIBLE);
                            customConfiguredParameterTv.setText(configureDto.getMaxBaselineDisplayName() + "(" + configureDto.getMeasurementUnit() + ")");
                            if (configureDto.getMinBaselineValue() != null) {
                                minConfiguredParameterEt.setVisibility(View.VISIBLE);
                                minConfiguredParameterEt.setText(AppUtils.getValueWithoutDecimal(configureDto.getMinBaselineValue()));
                            } else {
                                minConfiguredParameterEt.setVisibility(View.GONE);
                            }
                        }

                        updateFrequencyValues();
                    }
                }
            });
        }
    }

    private void updateFrequencyValues() {
        if (configureDto != null) {
            String frequencyUnit = configureDto.getFrequencyUnit();
            String frequencyValue = configureDto.getFrequencyValue();
            if (frequencyUnit.equalsIgnoreCase(Constants.DAY)) {
                frequencySp.setSelection(1);
            } else if (frequencyUnit.equalsIgnoreCase(Constants.WEEK)) {
                frequencySp.setSelection(2);
                performWeeklyClickEvent();

                if (weeklyDaysList != null && weeklyDaysList.size() > 0) {
                    for (int i = 0; i < weeklyDaysList.size(); i++) {
                        WeeklyDays weeklyDays = weeklyDaysList.get(i);
                        if (weeklyDays.getFullName().equalsIgnoreCase(frequencyValue)) {
                            daySp.setSelection(i);
                            break;
                        }
                    }
                }
            } else if (frequencyUnit.equalsIgnoreCase(Constants.MONTH)) {
                frequencySp.setSelection(3);
                performMonthlyClickEvent();

                if (monthlyDaysList != null && monthlyDaysList.size() > 0) {
                    for (int i = 0; i < monthlyDaysList.size(); i++) {
                        MonthlyDays monthlyDays = monthlyDaysList.get(i);
                        String day = monthlyDays.getDay() + "";
                        if (day.equalsIgnoreCase(frequencyValue)) {
                            monthlySp.setSelection(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void performDailyClickEvent() {

        weeklyLl.setVisibility(View.GONE);
        monthlyLl.setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    private void handleClickEvent() {

        monthlyLl.setOnClickListener(view -> monthlySp.performClick());

        daySpLl.setOnClickListener(view -> daySp.performClick());

        saveBtn.setOnClickListener(view -> {
            boolean fieldsOK = validate(getEditextArray());
            Log.i("chekcurlcheck","editTexts size 5  = "+ String.valueOf(validate(getEditextArray())));
           /* Log.e("fieldsOk", " = "+fieldsOK);

            if(fieldsOK) {*/
            if (this != null && userDto != null && vitalDto != null) {
                AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_CONFIG_TAB_SAVE_BTN_CLK);

                configurationMap.put(Constants.HEALTH_MATRICS_NAME, fromWhichDisease);
                ConfigureDto reuestConfigureDto = new ConfigureDto();
                int frequencyPos = frequencySp.getSelectedItemPosition();
                if (frequencyPos != 0) {
                    if (frequencyPos == 1) {
                        reuestConfigureDto.setFrequencyUnit(Constants.DAY);
                        configurationMap.put("frequency", "DAY");
                    } else if (frequencyPos == 2) {
                        reuestConfigureDto.setFrequencyUnit(Constants.WEEK);
                        configurationMap.put("frequency", "WEEK");
                        int pos = daySp.getSelectedItemPosition();
                        reuestConfigureDto.setFrequencyValue(weeklyDaysList.get(pos).getFullName().toUpperCase());
                    } else if (frequencyPos == 3) {
                        reuestConfigureDto.setFrequencyUnit(Constants.MONTH);
                        configurationMap.put("frequency", "MONTH");
                        int pos = monthlySp.getSelectedItemPosition();
                        reuestConfigureDto.setFrequencyValue(monthlyDaysList.get(pos).getDay() + "");
                    }
                    Log.i("chekcurlcheck","editTexts size 56  = ");
                    // Checking if user entered values are in the given ranges
                    AppUtils.hideKeyboard(this);
                    checkIfUserEnteredValues(reuestConfigureDto);
                } else {
                    AppUtils.openSnackBar(mParentView, getString(R.string.please_select_frequency));
                }
//                }
            }
        });


        ftSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (userIsInteracting) {
                    updateIncArray(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        incsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateWeightGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateWeightGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ftLl.setOnClickListener(view -> ftSp.performClick());

        inclSpLl.setOnClickListener(view -> incsSp.performClick());

        cmParentLl.setOnClickListener(view -> cmsSp.performClick());

        heightSwitchCompat.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (userIsInteracting) {
                if (isChecked) {
                    AppUtils.logEvent(Constants.CNDTN_HEIGHT_MODE_CM_SLCT);
                    cmParentLl.setVisibility(View.VISIBLE);
                    ftIncsLl.setVisibility(View.GONE);
                    setCmsSpValues();
                } else {
                    AppUtils.logEvent(Constants.CNDTN_HEIGHT_MODE_FEET_SLCT);
                    cmParentLl.setVisibility(View.GONE);
                    ftIncsLl.setVisibility(View.VISIBLE);
                }
                updateWeightGraph();
            }
        });

        tempSwitchCompat.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // celsius
                AppUtils.logEvent(Constants.CNDTN_TEMP_MODE_DEGREEC_SLCT);
                systolicValue.setText(AppUtils.MAX_VALUE_TEMP_CELSIUS + "");
                diastolicValue.setText(AppUtils.MIN_VALUE_TEMP_CELSIUS + "");
            } else {
                //  fern
                AppUtils.logEvent(Constants.CNDTN_TEMP_MODE_DEGREEF_SLCT);
                systolicValue.setText(AppUtils.MAX_VALUE_TEMP_FAHRENHEIT + "");
                diastolicValue.setText(AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + "");
            }
        });

        weightSwitchCompat.setOnCheckedChangeListener((compoundButton, isChecked) -> updateWeightGraph());

        systolicValue.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (event == null || !event.isShiftPressed()) {
                            //if (sysValueStr.length() > 0)
                            AppUtils.hideKeyboard(this);
                            return true;
                        }
                    }
                    return false;
                }
        );

        diastolicValue.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (event == null || !event.isShiftPressed()) {
                            AppUtils.hideKeyboard(this);
                            return true;
                        }
                    }
                    return false;
                }
        );

        mgDlValue.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (event == null || !event.isShiftPressed()) {
                            AppUtils.hideKeyboard(this);
                            return true;
                        }
                    }
                    return false;
                }
        );

        frequencySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (userIsInteracting) {
                    if (position == 0) {
                        // hide all the options
                        performSelectClickEvent();
                    } else if (position == 1) {
                        performDailyClickEvent();
                    } else if (position == 2) {
                        performWeeklyClickEvent();
                    } else if (position == 3) {
                        performMonthlyClickEvent();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        frequencySpLl.setOnClickListener(view -> frequencySp.performClick());
    }

    private ArrayList getEditextArray() {

        ArrayList<EditText> editTexts = new ArrayList<EditText>();
        if (systolicValue.getVisibility() == View.VISIBLE) {
            editTexts.add(systolicValue);
        } else if (diastolicValue.getVisibility() == View.VISIBLE) {
            editTexts.add(diastolicValue);
        } else if (mgDlValue.getVisibility() == View.VISIBLE) {
            editTexts.add(mgDlValue);
        } else if (customConfiguredParameterEt.getVisibility() == View.VISIBLE) {
            editTexts.add(customConfiguredParameterEt);
        } else if (minConfiguredParameterEt.getVisibility() == View.VISIBLE) {
            editTexts.add(minConfiguredParameterEt);
        }

        for (int i = 0; i < editTexts.size(); i++) {
            Log.e("editTexts_log", " = " + editTexts.size() + " editeTextName =" + editTexts.get(i).getText().toString());
        }
        Log.i("chekcurlcheck","editTexts size 2  = "+ String.valueOf(editTexts.size()));
        return editTexts;
    }

    private boolean validate() {
        Log.e("validate_log", " = clicked");
        Log.e("validate_log", " = clicked" + AppUtils.isEmpty(systolicValue.getText().toString()) + " value =" + systolicValue.getText().toString());
        Log.e("validate_log", " = clicked" + AppUtils.isEmpty(diastolicValue.getText().toString()) + " value =" + diastolicValue.getText().toString());
        Log.e("validate_log", " = clicked" + AppUtils.isEmpty(mgDlValue.getText().toString()) + " value =" + mgDlValue.getText().toString());
        Log.e("validate_log", " = clicked" + AppUtils.isEmpty(customConfiguredParameterEt.getText().toString()) + " value =" + customConfiguredParameterEt.getText().toString());
        Log.e("validate_log", " = clicked" + AppUtils.isEmpty(minConfiguredParameterEt.getText().toString()) + " value =" + minConfiguredParameterEt.getText().toString());

        if (!AppUtils.isEmpty(systolicValue.getText().toString())) {
            return false;
        } else if (!AppUtils.isEmpty(diastolicValue.getText().toString())) {
            return false;
        } else if (!AppUtils.isEmpty(mgDlValue.getText().toString())) {
            return false;
        } else if (!AppUtils.isEmpty(customConfiguredParameterEt.getText().toString())) {
            return false;
        } else if (!AppUtils.isEmpty(minConfiguredParameterEt.getText().toString())) {
            return false;
        }

        return true;
    }

    private boolean validate(ArrayList<EditText> fields) {
        for (int i = 0; i < fields.size(); i++) {
            EditText currentField = fields.get(i);
            if (currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        Log.i("chekcurlcheck","editTexts size 4  = "+ String.valueOf(fields.size()));
        return true;
    }

    private void checkIfUserEnteredValues(ConfigureDto reuestConfigureDto) {
        String systolicValueStr = systolicValue.getText().toString();
        String diastolicValueStr = diastolicValue.getText().toString();
        String mgDlValueStr = mgDlValue.getText().toString();
        String customConfiguredParameterStr = customConfiguredParameterEt.getText().toString();
        Context context = this;


        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {
            Log.i("chekcurlcheck","editTexts size 57  = ");
            String rangeSys = "";
            String rangeDia = "";
            if (context != null) {
                rangeSys = context.getString(R.string.enter_systolic_value) + context.getString(R.string.space) + context.getString(R.string.between) + context.getString(R.string.space)
                        + AppUtils.SYSTOLIC_MIN_VALUE + " - " + AppUtils.SYSTOLIC_MAX_VALUE;

                rangeDia = context.getString(R.string.enter_diastolic_value) + context.getString(R.string.space) + context.getString(R.string.between) + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.DIASTOLIC_MAX_VALUE;
            }


            if (systolicValueStr.length() > 0 && diastolicValueStr.length() > 0) {
                Log.i("chekcurlcheck","editTexts size 70  = ");
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.SYSTOLIC_MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.SYSTOLIC_MAX_VALUE + "")) {
                    Log.i("chekcurlcheck","editTexts size 71  = ");
                    if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                            && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.DIASTOLIC_MAX_VALUE + "")) {
                        Log.i("chekcurlcheck","editTexts size 72 = ");
                        if (Double.parseDouble(systolicValueStr) > Double.parseDouble(diastolicValueStr)) {
                            Log.i("chekcurlcheck","editTexts size 73  = ");
                            proceedToUpdateData(reuestConfigureDto);
                        } else {
                            Log.i("chekcurlcheck","editTexts size 74  systolicValueStr = "+systolicValueStr+", diastolicValueStr = "+diastolicValueStr);
                            AppUtils.openSnackBar(mParentView, getString(R.string.sys_dia_value_greater));
                        }
                    } else {
                        Log.i("chekcurlcheck","editTexts size 75  = ");
                        AppUtils.openSnackBar(mParentView, rangeDia);
                    }

                } else {
                    Log.i("chekcurlcheck","editTexts size 76  = ");
                    AppUtils.openSnackBar(mParentView, rangeSys);
                }
            } else {
                Log.i("chekcurlcheck","editTexts size 77  = ");
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_PP)) {
            Log.i("chekcurlcheck","editTexts size 58  = ");
            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_blood_sugar_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE;
            }

            if (mgDlValueStr.length() > 0) {
                if (Double.parseDouble(mgDlValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                        && Double.parseDouble(mgDlValueStr) <= Double.parseDouble(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "")) {

                    proceedToUpdateData(reuestConfigureDto);
                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose)) {
            Log.i("chekcurlcheck","editTexts size 59  = ");
            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_blood_glucose_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0 && diastolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "")) {

                    if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                            && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "")) {

                        if (Double.parseDouble(systolicValueStr) > Double.parseDouble(diastolicValueStr)) {
                            proceedToUpdateData(reuestConfigureDto);
                        } else {
                            AppUtils.openSnackBar(mParentView, getString(R.string.max_value_greater));
                        }

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }

                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {
            Log.i("chekcurlcheck","editTexts size 59  = ");
            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_heart_rate_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.HEART_RATE_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0 && diastolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.HEART_RATE_MAX_VALUE + "")) {

                    if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                            && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.HEART_RATE_MAX_VALUE + "")) {

                        proceedToUpdateData(reuestConfigureDto);

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }

                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {
            Log.i("chekcurlcheck","editTexts size 60  = ");
            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_respiratory_rate_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.RR_MIN_VALUE + " - " + AppUtils.RR_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0 && diastolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.RR_MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.RR_MAX_VALUE + "")) {

                    if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.RR_MIN_VALUE + "")
                            && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.RR_MAX_VALUE + "")) {

                        proceedToUpdateData(reuestConfigureDto);

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }

                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

            Log.i("chekcurlcheck","editTexts size 61  = ");
            if (systolicValueStr.length() > 0 && diastolicValueStr.length() > 0) {
                if (tempSwitchCompat.isChecked()) {
                    // celsius

                    String range = "";
                    if (context != null) {
                        range = context.getString(R.string.enter_temperature_value) + context.getString(R.string.space) + context.getString(R.string.between)
                                + context.getString(R.string.space)
                                + AppUtils.MIN_VALUE_TEMP_CELSIUS + " - " + AppUtils.MAX_VALUE_TEMP_CELSIUS;
                    }

                    if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE_TEMP_CELSIUS + "")
                            && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.MAX_VALUE_TEMP_CELSIUS + "")) {

                        if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE_TEMP_CELSIUS + "")
                                && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.MAX_VALUE_TEMP_CELSIUS + "")) {

                            reuestConfigureDto.setMeasurementUnit(Constants.C);
                            proceedToUpdateData(reuestConfigureDto);

                        } else {
                            AppUtils.openSnackBar(mParentView, range);
                        }

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }

                } else {
                    // fahrenheit

                    String range = "";
                    if (context != null) {
                        range = context.getString(R.string.enter_temperature_value) + context.getString(R.string.space) + context.getString(R.string.between)
                                + context.getString(R.string.space)
                                + AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + " - " + AppUtils.MAX_VALUE_TEMP_FAHRENHEIT;
                    }

                    if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + "")
                            && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.MAX_VALUE_TEMP_FAHRENHEIT + "")) {

                        if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + "")
                                && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.MAX_VALUE_TEMP_FAHRENHEIT + "")) {

                            reuestConfigureDto.setMeasurementUnit(Constants.F);
                            proceedToUpdateData(reuestConfigureDto);

                        } else {
                            AppUtils.openSnackBar(mParentView, range);
                        }

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }

                }
            } else {
                Log.i("chekcurlcheck","editTexts size 62  = ");
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {
            Log.i("chekcurlcheck","editTexts size 63  = ");
            proceedToUpdateData(reuestConfigureDto);
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Pulse_Oximeter)) {

            Log.i("chekcurlcheck","editTexts size 64  = ");
            double customConfiguredParameterd = Double.parseDouble(customConfiguredParameterEt.getText().toString());
            double minConfiguredParameterd = Double.parseDouble(minConfiguredParameterEt.getText().toString());
            if (customConfiguredParameterd >= 60 && minConfiguredParameterd <= 100) {
                proceedToUpdateData(reuestConfigureDto);
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.value_should_in_be_in_between_of_60_100));
            }
        } else {
            Log.i("chekcurlcheck","editTexts size 65  = ");
            if (!customConfiguredParameterEt.getText().toString().isEmpty()) {

                if (customConfiguredParameterStr.length() > 0 && !customConfiguredParameterStr.equalsIgnoreCase("0")) {

                    proceedToUpdateData(reuestConfigureDto);
                } else {
                    AppUtils.openSnackBar(mParentView, getString(R.string.enter_configure_value_greater_than_zero));
                }
            }
        }
    }

    private void proceedToUpdateData(ConfigureDto reuestConfigureDto) {
        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_PP)) {
            reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(mgDlValue.getText().toString()));
            reuestConfigureDto.setMinBaselineValue(configureDto.getMinBaselineValue());
            reuestConfigureDto.setMeasurementUnit(vitalDto.getMeasurementUnit());

            configurationMap.put(Constants.UNIT,vitalDto.getMeasurementUnit());
            configurationMap.put(Constants.VALUE,"Min = "+configureDto.getMinBaselineValue() + "Max = "+Double.parseDouble(mgDlValue.getText().toString()));

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {


            if (heightSwitchCompat.isChecked()) {
                // cms
                int cms = Integer.parseInt(cmsSp.getSelectedItem().toString());

                reuestConfigureDto.setMeasurementUnit(Constants.CMS);
                reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(cms + ""));
                configurationMap.put(Constants.UNIT,Constants.CMS);
                configurationMap.put(Constants.VALUE,"Min = 0 " + "Max = "+Double.parseDouble(cms + ""));


            } else {
                // ft
                int ft = Integer.parseInt(ftSp.getSelectedItem().toString());
                int incs = Integer.parseInt(incsSp.getSelectedItem().toString());

                reuestConfigureDto.setMeasurementUnit(Constants.INCH);

                long incsLong = AppUtils.getIncsFromFtIncs(ft, incs);
                reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(incsLong + ""));

                configurationMap.put(Constants.UNIT,Constants.INCH);
                configurationMap.put(Constants.VALUE,"Min = 0 " + "Max = "+Double.parseDouble(incsLong + ""));
            }



        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

            if (tempSwitchCompat.isChecked()) {
                // cel
                reuestConfigureDto.setMeasurementUnit(Constants.C);
                configurationMap.put(Constants.UNIT,Constants.C);

            } else {
                // fern
                reuestConfigureDto.setMeasurementUnit(Constants.F);
                configurationMap.put(Constants.UNIT,Constants.F);
            }

            reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(systolicValue.getText().toString()));
            reuestConfigureDto.setMinBaselineValue(Double.parseDouble(diastolicValue.getText().toString()));

            configurationMap.put(Constants.VALUE,"Min =  "+Double.parseDouble(systolicValue.getText().toString()) + "Max = "+Double.parseDouble(diastolicValue.getText().toString()));
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {
            reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(systolicValue.getText().toString()));
            reuestConfigureDto.setMinBaselineValue(Double.parseDouble(diastolicValue.getText().toString()));
            reuestConfigureDto.setMeasurementUnit(vitalDto.getMeasurementUnit());

            configurationMap.put(Constants.UNIT,vitalDto.getMeasurementUnit());
            configurationMap.put(Constants.VALUE,"Min =  "+Double.parseDouble(systolicValue.getText().toString()) + "Max = "+Double.parseDouble(diastolicValue.getText().toString()));

        } else {

            String strFreqUnit = reuestConfigureDto.getFrequencyUnit();
            String strFreqValue = reuestConfigureDto.getFrequencyValue();
            reuestConfigureDto = configureDto;
            Double customConfiguredParameterd = Double.parseDouble(customConfiguredParameterEt.getText().toString());
            reuestConfigureDto.setMaxBaselineValue(customConfiguredParameterd);
            reuestConfigureDto.setFrequencyUnit(strFreqUnit);
            reuestConfigureDto.setFrequencyValue(strFreqValue);


            if (configureDto.getMinBaselineValue() != null) {
                Double minConfiguredParameterd = Double.parseDouble(minConfiguredParameterEt.getText().toString());
                reuestConfigureDto.setMinBaselineValue(minConfiguredParameterd);
                configurationMap.put(Constants.UNIT,strFreqValue + " " +strFreqValue);
                configurationMap.put(Constants.VALUE,"Min =  "+minConfiguredParameterd + "Max = "+customConfiguredParameterd);
            }



            //reuestConfigureDto.setMeasurementUnit(configureDto.getMeasurementUnit());
        }
        apiUpdateParameterAPI(reuestConfigureDto);
       // callUpdateAPI(reuestConfigureDto);
    }

    private void updateIncArray(int position) {
        if (position == 0) {
            incArray = getResources().getStringArray(R.array.four_incs_adapter);
        } else if (position == 2) {
            incArray = getResources().getStringArray(R.array.six_incs_adapter);
        } else {
            incArray = getResources().getStringArray(R.array.incs_adapter);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown, incArray);
        incsSp.setAdapter(dataAdapter);
        updateWeightGraph();

    }
    private void apiUpdateParameterAPI(ConfigureDto configureDto){
        Log.i("checkmodrug", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        Log.i("checkmodrug", "drug request parameter id = 589 " + String.valueOf(vitalDto.getId()));
        Log.i("checkmodrug", "drug request json = 589 " + new Gson().toJson(configureDto));
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.updateParameterAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(vitalDto.getId()),configureDto,token).enqueue(new Callback<APIMessageResponse>() {
            @Override
            public void onResponse(Call<APIMessageResponse> call, retrofit2.Response<APIMessageResponse> response) {
                Log.i("checkmodrug", "api login response 0 code = " + response.code());
                Log.i("checkmodrug", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.hideProgressBar(progress);
                    AppUtils.isDataChanged = true;
//                            onSaveListener.onSaveClicked(getString(R.string.configure));
                    configurationMap.put(Constants.DATE,AppUtils.getTodayDate());
                    configurationMap.put(Constants.TIME,AppUtils.getCurrentTime());
                    AppUtils.logCleverTapEvent(ConfigureActivity.this, Constants.HEALTH_MATRICS_BASELINE_CONFIGURE_FORM_SUBMITTED, configurationMap);
                    finish();
                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    AppUtils.openSnackBar(mParentView, "Please try after some time.");
                    Log.i("checkmodrug", "api response 1 code = " + response.code());
                    AppUtils.hideProgressBar(progress);
                }
            }

            @Override
            public void onFailure(Call<APIMessageResponse> call, Throwable t) {
                AppUtils.openSnackBar(mParentView, t.getMessage());
                Log.i("checkmodrug", "api error message response  = " + t.getMessage());
                AppUtils.hideProgressBar(progress);
            }
        });
    }
    private void callUpdateAPI(ConfigureDto configureDto) {
        if (progress != null) {
            progress.show();
        }
        Log.i("chekcurlcheck","callUpdateAPI 2  = "+ APIUrls.get().getConfigurationParameter(userDto.getId(), vitalDto.getUserParameterId()));
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> updateDiseaseParameter = new GenericRequest<>
                (Request.Method.PUT, APIUrls.get().getConfigurationParameter(userDto.getId(), vitalDto.getUserParameterId()),
                        APIMessageResponse.class, configureDto,
                        apiMessageResponse -> {
                            AppUtils.hideProgressBar(progress);
                            AppUtils.isDataChanged = true;
//                            onSaveListener.onSaveClicked(getString(R.string.configure));
                            configurationMap.put(Constants.DATE,AppUtils.getTodayDate());
                            configurationMap.put(Constants.TIME,AppUtils.getCurrentTime());
                            AppUtils.logCleverTapEvent(ConfigureActivity.this, Constants.HEALTH_MATRICS_BASELINE_CONFIGURE_FORM_SUBMITTED, configurationMap);
                            finish();
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(this, error, authExpiredCallback);
                            AppUtils.openSnackBar(mParentView, res);
                        });
        authExpiredCallback.setRequest(updateDiseaseParameter);
        ApiService.get().addToRequestQueue(updateDiseaseParameter);
    }

    private void updateWeightGraph() {
        long incsLong;
        if (heightSwitchCompat.isChecked()) {
            // cms
            int cms = Integer.parseInt(cmsSp.getSelectedItem().toString());

            incsLong = AppUtils.getIncsFromCms(cms);
        } else {
            // ft
            int ft = Integer.parseInt(ftSp.getSelectedItem().toString());
            int incs = Integer.parseInt(incsSp.getSelectedItem().toString());

            incsLong = AppUtils.getIncsFromFtIncs(ft, incs);
        }

        Weights weights = ApplicationDB.get().getWeightRange(incsLong);

        if (weights != null) {
            List<Ranges> rangesList = weights.getRanges();
            for (int i = 0; i < rangesList.size(); i++) {
                Ranges ranges = rangesList.get(i);
                String category = ranges.getCategory();

                if (category.equalsIgnoreCase(Constants.UNDER)) {
                    String under = AppUtils.capitalize(category.toLowerCase()) + " <br/> " + "< " + getWeightAccToUnit(ranges.getMax());
                    underTv.setText(AppUtils.getHtmlString(under));
                } else if (category.equalsIgnoreCase(Constants.NORMAL)) {
                    String under = AppUtils.capitalize(category.toLowerCase()) + " <br/> " + getWeightAccToUnit(ranges.getMin()) + "-" + getWeightAccToUnit(ranges.getMax());
                    normalTv.setText(AppUtils.getHtmlString(under));

                    String minValue = getWeightAccToUnit(ranges.getMin());
                    String maxValue = getWeightAccToUnit(ranges.getMax());

                    weighMinValue.setText(minValue);
                    weighMaxValue.setText(maxValue);


                } else if (category.equalsIgnoreCase(Constants.OVER)) {
                    String under = AppUtils.capitalize(category.toLowerCase()) + " <br/> " + getWeightAccToUnit(ranges.getMin()) + "-" + getWeightAccToUnit(ranges.getMax());
                    overTv.setText(AppUtils.getHtmlString(under));
                } else if (category.equalsIgnoreCase(Constants.OBESE)) {
                    String under = AppUtils.capitalize(category.toLowerCase()) + " <br/> " + getWeightAccToUnit(ranges.getMin()) + "-" + getWeightAccToUnit(ranges.getMax());
                    obeseTv.setText(AppUtils.getHtmlString(under));
                } else if (category.equalsIgnoreCase(Constants.EXTREME)) {
                    String under = AppUtils.capitalize(category.toLowerCase()) + " <br/> " + getWeightAccToUnit(ranges.getMin()) + "+";
                    extremelyTv.setText(AppUtils.getHtmlString(under));
                }
            }
        }
    }

    private String getWeightAccToUnit(Double weight) {
        DecimalFormat df = new DecimalFormat("#");
        if (weightSwitchCompat.isChecked()) {
            // kgs
            double weightDouble = weight / 2.2046;
            return df.format(weightDouble);
        } else {
            return df.format(weight);
        }
    }

    private void performSelectClickEvent() {
        weeklyLl.setVisibility(View.GONE);
        monthlyLl.setVisibility(View.GONE);
    }

    private void performMonthlyClickEvent() {
        weeklyLl.setVisibility(View.GONE);
        monthlyLl.setVisibility(View.VISIBLE);

        monthlyDaysList = AppUtils.getMonthlyList(null);
        MonthlySpinnerAdapter monthlySpinnerAdapter = new MonthlySpinnerAdapter(this, R.layout.text_view, R.id.header, monthlyDaysList);
        monthlySp.setAdapter(monthlySpinnerAdapter);
    }

    private void performWeeklyClickEvent() {
        weeklyLl.setVisibility(View.VISIBLE);
        monthlyLl.setVisibility(View.GONE);

        weeklyDaysList = AppUtils.getWeeklyList(null);
        if (this != null) {
            WeeklySpinnerAdapter weeklySpinnerAdapter = new WeeklySpinnerAdapter(this, R.layout.text_view, R.id.header, weeklyDaysList);
            daySp.setAdapter(weeklySpinnerAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
    }

}