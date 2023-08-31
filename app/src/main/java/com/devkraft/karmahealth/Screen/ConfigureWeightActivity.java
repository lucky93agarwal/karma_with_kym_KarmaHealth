package com.devkraft.karmahealth.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.MonthlySpinnerAdapter;
import com.devkraft.karmahealth.Adapter.WeeklySpinnerAdapter;
import com.devkraft.karmahealth.Model.ConfigureDto;
import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.WeeklyDays;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.jetbrains.annotations.NotNull;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ConfigureWeightActivity extends AppCompatActivity {
    private List<WeeklyDays> weeklyDaysList;
    private List<MonthlyDays> monthlyDaysList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private UserDto userDto;
    private Button mButoonSubmit;
    private Spinner mSpinnerGoal;
    private Spinner mSpinnerFrequency;
    private ParameterDto parameterDto;
    private ConfigureDto configureDto;
    private RelativeLayout mParentView;
    private ProgressDialogSetup progress;
    private EditText mEditTextTargetWeight;
    private EditText mEditextCurrentWeight;
    private ToggleButton mToggleButtonWeighUnit;

    private ImageView mImageViewFrequency;
    private ImageView mImageViewWeightGoal;

    private Spinner daySp;
    private Spinner monthlySp;
    private LinearLayout daySpLl;
    private LinearLayout weeklyLl;
    private LinearLayout monthlyLl;

    final double change = 2.2046226218; //this is the change between kg and lbs, 1kg = this amount of lbs
    private boolean isClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_weight);
        setupToolbar();
        initializeIds();
        getIntentValues(getIntent());
        setOnClickListeners();
    }

    private void getIntentValues(Intent arguments) {
        if (arguments != null) {
            Gson gson = new Gson();
            String userDtoStr = arguments.getStringExtra(Constants.USER_DTO);
            String vitalDtoStr = arguments.getStringExtra(Constants.PARAMETER_DTO);

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(vitalDtoStr, ParameterDto.class);
            }
           // callGetParameterAPI();
            Log.i("getParameterAPI", "drug apiGetParameterAPI hit ");
            apiGetParameterAPI();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.configure_weight));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }

    private void initializeIds() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        daySpLl = findViewById(R.id.daySpLl);
        weeklyLl = findViewById(R.id.weeklyLl);
        monthlyLl = findViewById(R.id.monthlyLl);

        daySp = findViewById(R.id.daySp);
        monthlySp = findViewById(R.id.monthlySp);
        mSpinnerGoal = findViewById(R.id.spinner_weight_goal);

        mParentView = findViewById(R.id.parent_view_configure);
        progress = ProgressDialogSetup.getProgressDialog(this);
        mSpinnerFrequency = findViewById(R.id.spinner_frequency);
        mToggleButtonWeighUnit = findViewById(R.id.toggle_weight_unit);
        mEditTextTargetWeight = findViewById(R.id.editext_target_weight);
        mButoonSubmit = findViewById(R.id.button_submit_weight_configure);
        mEditextCurrentWeight = findViewById(R.id.editext_current_weight);

        mImageViewFrequency = findViewById(R.id.imageview_frequency);
        mImageViewWeightGoal = findViewById(R.id.imageview_weight_goal);
    }

    private void setOnClickListeners() {

        daySpLl.setOnClickListener(view -> daySp.performClick());

        monthlyLl.setOnClickListener(view -> monthlySp.performClick());

        mImageViewWeightGoal.setOnClickListener(v -> mSpinnerGoal.performClick());

        mImageViewFrequency.setOnClickListener(v -> mSpinnerFrequency.performClick());

        mButoonSubmit.setOnClickListener(v -> {
            configureDto = new ConfigureDto();
            int frequencyPos = mSpinnerFrequency.getSelectedItemPosition();
            if (frequencyPos != 0) {
                if (frequencyPos == 1) {
                    configureDto.setFrequencyUnit(Constants.DAY);
                } else if (frequencyPos == 2) {
                    configureDto.setFrequencyUnit(Constants.WEEK);
                    int pos = daySp.getSelectedItemPosition();
                    configureDto.setFrequencyValue(weeklyDaysList.get(pos).getFullName().toUpperCase());
                } else if (frequencyPos == 3) {
                    configureDto.setFrequencyUnit(Constants.MONTH);
                    int pos = monthlySp.getSelectedItemPosition();
                    configureDto.setFrequencyValue(monthlyDaysList.get(pos).getDay() + "");
                }

                // Checking if user entered values are in the given ranges
                AppUtils.hideKeyboard(this);
                checkIfUserEnteredValues(configureDto);
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.please_select_frequency));
            }
        });

        mSpinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    // hide all the options
                    performSelectClickEvent();
                } else if (position == 1) {
                    performSelectClickEvent();
                } else if (position == 2) {
                    performWeeklyClickEvent();
                } else if (position == 3) {
                    performMonthlyClickEvent();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        mToggleButtonWeighUnit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (configureDto != null && isChecked) {
                Log.i("getParameterAPI", "03" );
                calculateWeightAccordingToSwitch(isChecked);
            } else {
                Log.i("getParameterAPI", "02" );
                calculateWeightAccordingToSwitch(false);
            }
        });

    }

    private void calculateWeightAccordingToSwitch(boolean isChecked) {
        Log.i("getParameterAPI", "01" );
        double currentWeight = Double.parseDouble(mEditextCurrentWeight.getText().toString());
        double targetWeight = Double.parseDouble(mEditTextTargetWeight.getText().toString());

        Log.e("current_weight", " = "+currentWeight);
        Log.e("target_weight", " = "+targetWeight);
        if (isChecked) {
            Log.e("if_kgs", " = "+configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.KGS));
            if(!configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.KGS) ||
                    configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.KGS)) {
                mEditextCurrentWeight.setText(AppUtils.getValueWithOneDecimal(currentWeight / change));
                mEditTextTargetWeight.setText(AppUtils.getValueWithOneDecimal(targetWeight / change));
            } else {
                mEditextCurrentWeight.setText(""+configureDto.getCurrentWeight());
                mEditTextTargetWeight.setText(""+configureDto.getTargetWeight());
            }
        } else {
//            Log.e("else_lbs", " = "+configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.LBS));
           /* if(!configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.LBS) ||
                    configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.LBS)) {
                Log.e("else_lbs", " = "+configureDto.getMeasurementUnit().equalsIgnoreCase(Constants.LBS));
                mEditextCurrentWeight.setText(AppUtils.getValueWithOneDecimal(currentWeight * change));
                mEditTextTargetWeight.setText(AppUtils.getValueWithOneDecimal(targetWeight * change));
            } else {*/
            if(configureDto.getCurrentWeight() != null){
                mEditextCurrentWeight.setText(""+configureDto.getCurrentWeight());
            }else {
                mEditextCurrentWeight.setText("0");
            }
            if(configureDto.getTargetWeight() != null){
                mEditTextTargetWeight.setText(""+configureDto.getTargetWeight());
            }else {
                mEditextCurrentWeight.setText("0");
            }

            /*}*/
        }
    }

    private void checkIfUserEnteredValues(ConfigureDto reuestConfigureDto) {
        int weightGoalPos = mSpinnerGoal.getSelectedItemPosition();

        if (weightGoalPos == 1) {
            reuestConfigureDto.setWeightGoal(Constants.LOSS_WEIGHT);
        } else if (weightGoalPos == 2) {
            reuestConfigureDto.setWeightGoal(Constants.MAINTAIN_WEIGHT);
        } else if (weightGoalPos == 3) {
            reuestConfigureDto.setWeightGoal(Constants.GAIN_WEIGHT);
        }

        if (mToggleButtonWeighUnit.isChecked())
            reuestConfigureDto.setMeasurementUnit(Constants.KGS);
        else
            reuestConfigureDto.setMeasurementUnit(Constants.LBS);

        if (!mEditextCurrentWeight.getText().toString().equalsIgnoreCase(""))
            reuestConfigureDto.setCurrentWeight(Double.valueOf(mEditextCurrentWeight.getText().toString()));

        if (!mEditTextTargetWeight.getText().toString().equalsIgnoreCase(""))
            reuestConfigureDto.setTargetWeight(Double.valueOf(mEditTextTargetWeight.getText().toString()));

        reuestConfigureDto.setRemindFlag(true);
        reuestConfigureDto.setUserId(Long.parseLong(sharedPreferences.getString("kymPid", "134388")));
        reuestConfigureDto.setId(parameterDto.getId());
        reuestConfigureDto.setParameterReminderText("");
        reuestConfigureDto.setMaxBaselineDisplayName(parameterDto.getName());
        reuestConfigureDto.setParameterId(parameterDto.getId());
        reuestConfigureDto.setMaxBaselineValue(parameterDto.getMaxBaselineValue());
        reuestConfigureDto.setMedicalparameterType(parameterDto.getMedicalParameterType());
        proceedToUpdateData(reuestConfigureDto);
    }

    private void proceedToUpdateData(ConfigureDto reuestConfigureDto) {
       // callAPIForWeightConfiguration(reuestConfigureDto);
        apiUpdateParameterAPI(reuestConfigureDto);
    }

    private void apiUpdateParameterAPI(ConfigureDto configureDto){
        Log.i("updateParameterAPI", "updateParameterAPI request user id =  " + sharedPreferences.getString("kymPid", "134388"));
        Log.i("updateParameterAPI", "updateParameterAPI request parameterDto id =  " + String.valueOf(parameterDto.getId()));
        Log.i("updateParameterAPI", "updateParameterAPI request user model =  " + new Gson().toJson(configureDto));
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.updateParameterAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(parameterDto.getId()),configureDto,token).enqueue(new Callback<APIMessageResponse>() {
            @Override
            public void onResponse(Call<APIMessageResponse> call, retrofit2.Response<APIMessageResponse> response) {
                Log.i("updateParameterAPI", "api login response 0 code = " + response.code());
                Log.i("updateParameterAPI", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.hideProgressBar(progress);
                    AppUtils.isDataChanged = true;
                    AppUtils.openSnackBar(mParentView, "Configuration sucessfull");
                    finish();
                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    AppUtils.openSnackBar(mParentView, "Please try after some time.");
                    Log.i("updateParameterAPI", "api response 1 code = " + response.code());
                    AppUtils.hideProgressBar(progress);
                }
            }

            @Override
            public void onFailure(Call<APIMessageResponse> call, Throwable t) {
                AppUtils.openSnackBar(mParentView, t.getMessage());
                Log.i("updateParameterAPI", "api error message response  = " + t.getMessage());
                AppUtils.hideProgressBar(progress);
            }
        });
    }
    private void callAPIForWeightConfiguration(ConfigureDto configureDto) {

        Gson gson = new Gson();
        Log.e("requestObject_log", " = " + gson.toJson(configureDto));

        if (progress != null) {
            progress.show();
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> updateDiseaseParameter = new GenericRequest<>
                (Request.Method.PUT, APIUrls.get().getConfigurationParameter(userDto.getId(), parameterDto.getUserParameterId()),
                        APIMessageResponse.class, configureDto,
                        apiMessageResponse -> {
                            AppUtils.hideProgressBar(progress);
                            AppUtils.isDataChanged = true;
                            AppUtils.openSnackBar(mParentView, "Configuration sucessfull");
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
                    Intent intent = new Intent(ConfigureWeightActivity.this, LoginActivity.class);
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

    private void apiGetParameterAPI(){
        Log.i("getParameterAPI", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.getParameterAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(parameterDto.getId()),token).enqueue(new Callback<ConfigureDto>() {
            @Override
            public void onResponse(Call<ConfigureDto> call, retrofit2.Response<ConfigureDto> response) {
                Log.i("getParameterAPI", "api login response 0 code = " + response.code());
                Log.i("getParameterAPI", "api login response  = " + new Gson().toJson(response.body()));
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
                AppUtils.hideProgressBar(progress);
                AppUtils.openSnackBar(mParentView, t.getMessage());
                Log.i("checkmodrug", "api error message response  = " + t.getMessage());
            }
        });
    }
    private void callGetParameterAPI() {
        if (userDto != null && parameterDto != null) {

            if (progress != null) {
                progress.show();
            }
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<ConfigureDto> getDiseaseParameter = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().getConfigurationParameter(userDto.getId(), parameterDto.getUserParameterId()),
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

    @SuppressLint("SetTextI18n")
    private void updateUI(ConfigureDto cDto) {
        Log.e("response_log", " = " + new Gson().toJson(cDto));
        if (cDto != null) {
            if (cDto.getCurrentWeight() == null && cDto.getTargetWeight() == null) {
                mEditextCurrentWeight.setText("0");
                mEditTextTargetWeight.setText("0");
            } else {
                mEditextCurrentWeight.setText("" + cDto.getCurrentWeight());
                mEditTextTargetWeight.setText("" + cDto.getTargetWeight());
            }

            setToggleButton(cDto);
            updateWeightGoal(cDto);
            updateFrequencyValues(cDto);

        }
    }

    private void setToggleButton(ConfigureDto cDto) {
        if (cDto.getMeasurementUnit() != null) {
            if (cDto.getMeasurementUnit().equalsIgnoreCase(Constants.KGS)) {
                mToggleButtonWeighUnit.setChecked(true);
            } else {
                mToggleButtonWeighUnit.setChecked(false);
                Log.i("getParameterAPI", "05" );
                calculateWeightAccordingToSwitch(false);
            }
        } else {
            mToggleButtonWeighUnit.setChecked(false);
            Log.i("getParameterAPI", "04" );
            calculateWeightAccordingToSwitch(false);
        }
    }

    private void updateWeightGoal(ConfigureDto cDto) {
        if (cDto.getWeightGoal() != null) {
            if (cDto.getWeightGoal().equalsIgnoreCase(Constants.LOSS_WEIGHT)) {
                mSpinnerGoal.setSelection(1);
            } else if (cDto.getWeightGoal().equalsIgnoreCase(Constants.MAINTAIN_WEIGHT)) {
                mSpinnerGoal.setSelection(2);
            } else if (cDto.getWeightGoal().equalsIgnoreCase(Constants.GAIN_WEIGHT)) {
                mSpinnerGoal.setSelection(3);
            } else {
                mSpinnerGoal.setSelection(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSelectClickEvent() {
        weeklyLl.setVisibility(View.GONE);
        monthlyLl.setVisibility(View.GONE);
    }

    private void performMonthlyClickEvent() {
        weeklyLl.setVisibility(View.GONE);
        monthlyLl.setVisibility(View.VISIBLE);

        monthlyDaysList = AppUtils.getMonthlyList(null);
        MonthlySpinnerAdapter monthlySpinnerAdapter = new MonthlySpinnerAdapter(ConfigureWeightActivity.this, R.layout.text_view, R.id.header, monthlyDaysList);
        monthlySp.setAdapter(monthlySpinnerAdapter);
    }

    private void performWeeklyClickEvent() {
        weeklyLl.setVisibility(View.VISIBLE);
        monthlyLl.setVisibility(View.GONE);

        weeklyDaysList = AppUtils.getWeeklyList(null);
        WeeklySpinnerAdapter weeklySpinnerAdapter = new WeeklySpinnerAdapter(this, R.layout.text_view, R.id.header, weeklyDaysList);
        daySp.setAdapter(weeklySpinnerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getIntentValues(getIntent());
    }

    private void updateFrequencyValues(ConfigureDto cDto) {
        if (cDto != null) {
            String frequencyUnit = cDto.getFrequencyUnit();
            String frequencyValue = cDto.getFrequencyValue();
            if (frequencyUnit.equalsIgnoreCase(Constants.DAY)) {
                mSpinnerFrequency.setSelection(1);
            } else if (frequencyUnit.equalsIgnoreCase(Constants.WEEK)) {
                mSpinnerFrequency.setSelection(2);
                performWeeklyClickEvent();

                if (weeklyDaysList != null && !weeklyDaysList.isEmpty()) {
                    for (int i = 0; i < weeklyDaysList.size(); i++) {
                        WeeklyDays weeklyDays = weeklyDaysList.get(i);
                        if (weeklyDays.getFullName().equalsIgnoreCase(frequencyValue)) {
                            daySp.setSelection(i);
                            break;
                        }
                    }
                }
            } else if (frequencyUnit.equalsIgnoreCase(Constants.MONTH)) {
                mSpinnerFrequency.setSelection(3);
                performMonthlyClickEvent();

                if (monthlyDaysList != null && !monthlyDaysList.isEmpty()) {
                    for (int i = 0; i < monthlyDaysList.size(); i++) {
                        Log.e("spinnerFor_log", "="+i);
                        MonthlyDays monthlyDays = monthlyDaysList.get(i);
                        String day = monthlyDays.getDay() + "";
                        if (day.equalsIgnoreCase(frequencyValue)) {
                            monthlySp.setSelection(i);
                            Log.e("spinner_log", "="+monthlySp.getSelectedItem().toString());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progress != null)
            progress.dismiss();
    }

}