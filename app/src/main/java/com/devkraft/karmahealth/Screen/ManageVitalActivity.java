package com.devkraft.karmahealth.Screen;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.FindParameterAdapter;
import com.devkraft.karmahealth.Adapter.MonthlySpinnerCustomDrugAdapter;
import com.devkraft.karmahealth.Adapter.ParameterListAPIResponse;
import com.devkraft.karmahealth.Adapter.ParameterSpinnerAdapter;
import com.devkraft.karmahealth.Adapter.UserSpinnerAdapter;
import com.devkraft.karmahealth.BuildConfig;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddParameterAPIResponse;
import com.devkraft.karmahealth.Model.AddParameterNewAPI;
import com.devkraft.karmahealth.Model.CustomParamDto;
import com.devkraft.karmahealth.Model.CustomparamterDto;
import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.Model.DrugListAPIResponse;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Model.ParamDto;
import com.devkraft.karmahealth.Model.ParameterSearchResultDto;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.VersionControllModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageVitalActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;



    private ProgressDialogSetup progress;
    private TextView userNameTv;
    private Spinner userSpinner, type_spinner, vital_spinner, vital_frequnecy_spinner, test_spinner, vaccine_spinner, vaccinefrequencySp, checkupFrequencySp, vitalcustomDayOfWeekSpinner, vitalcustomDayOfMonthSpinner;
    private UserDto userDto;
    private LinearLayout parentLayout, vitalbasedLLayout, testbasedLLayout, vaccinebasedLLayout, condition_typeLL, vital_typeLL, vital_frequencyLL, choose_testLL, test_frequencyLL, vitalFreqLL, vitalParametersLL,
            customCheckupNameLL, customVaccineNameLL, vitalCustomSelectDaysLL, vitalCustomDayOfWeekOrMonthLL, checkupDueDateLL, checkupFrequencyLL, vaccineDueDateLL, vaccineFrequencyLL, choose_vaccineLL, vaccine_frequencyLL;
    private String strCustomString, checkupDateString;
    private MenuItem menuItem;
    CustomparamterDto customparamterDto;
    ParameterSpinnerAdapter parameterSpinnerAdapter;
    String isCustom = "";
    private ParamDto paramDto;
    private EditText unitNameEt, checkupFrequencyEt, vaccineFrequencyEt, paramterNameEt;
    private TextView unitNameTv, unitNameTv1, paramaterNameTv, paramaterNameTv1, selectDateCheckupTv, vaccineselectDateTv, customParamMeasureTv;
    private ImageView selectDateCheckup, vaccineselectDate, vital_searchIv, checkup_searchIv, vaccine_searchIv;
    private MonthlySpinnerCustomDrugAdapter monthlySpinnerCustomDrugAdapter;
    AutoCompleteTextView vital_searchEt, checkup_searchEt, vaccine_searchEt;
    String searchTextString;
    private FindParameterAdapter findParameterAdapter;
    List<ParamDto> paramDtoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vital);

        iniIds();
        getIntentValue(getIntent());

        setTypeSpValues();
        handleClickEvent();
        setUpToolbar();
        AppUtils.hideKeyboard(ManageVitalActivity.this);
        setEmptyView();
        AppUtils.logEvent(Constants.CNDTN_SCR_TEST_CLK);
    }

    private void setEmptyView() {
        if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
            customParamMeasureTv.setVisibility(View.GONE);
            vitalcustomDayOfWeekSpinner.setVisibility(View.GONE);
            vitalcustomDayOfMonthSpinner.setVisibility(View.GONE);
            vitalFreqLL.setVisibility(View.GONE);
            vitalParametersLL.setVisibility(View.GONE);
            vital_frequnecy_spinner.setSelection(0);
            paramterNameEt.setText("");
            unitNameEt.setText("");
            vitalCustomSelectDaysLL.setVisibility(View.GONE);
        } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
            AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                    Constants.ADD_CHECKUP_SCREEN_OPENED, null);
            checkupDueDateLL.setVisibility(View.GONE);
            checkupFrequencyLL.setVisibility(View.GONE);
            customCheckupNameLL.setVisibility(View.GONE);
            checkupFrequencySp.setSelection(0);
            checkupFrequencyEt.setText("");
        } else {
            AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                    Constants.ADD_VACCINE_SCREEN_OPENED, null);
            vaccineDueDateLL.setVisibility(View.GONE);
            vaccineFrequencyLL.setVisibility(View.GONE);
            customVaccineNameLL.setVisibility(View.GONE);
            vaccineFrequencyEt.setText("");
            vaccinefrequencySp.setSelection(0);
        }
    }

    private void getIntentValue(Intent intent) {
        if (intent != null) {
            Gson gson = new Gson();
            strCustomString = intent.getStringExtra(Constants.Custom_condition);
            Log.i("checkitemonclick","onclick data 3 =  "+strCustomString);
            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);

            if (userDtoStr != null) {
                UserDto userDto = gson.fromJson(userDtoStr, UserDto.class);
                generateUserList(userDto);
            } else {
                generateUserList(null);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
            Log.i("checkitemonclick","onclick data 4 =  "+strCustomString);
            getSupportActionBar().setTitle(getString(R.string.add_vital));
            vitalbasedLLayout.setVisibility(View.VISIBLE);
            testbasedLLayout.setVisibility(View.GONE);
            vaccinebasedLLayout.setVisibility(View.GONE);
        } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
            getSupportActionBar().setTitle(getString(R.string.add_checkup));
            vitalbasedLLayout.setVisibility(View.GONE);
            testbasedLLayout.setVisibility(View.VISIBLE);
            vaccinebasedLLayout.setVisibility(View.GONE);
        } else {
            getSupportActionBar().setTitle(getString(R.string.add_vaccines));
            vitalbasedLLayout.setVisibility(View.GONE);
            testbasedLLayout.setVisibility(View.GONE);
            vaccinebasedLLayout.setVisibility(View.VISIBLE);
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));
    }

    private void setTypeSpValues() {
        if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital)))
            type_spinner.setSelection(0);
        else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup)))
            type_spinner.setSelection(1);
        else
            type_spinner.setSelection(2);
    }

    public void handleClickEvent() {
        condition_typeLL.setOnClickListener(view -> type_spinner.performClick());

        selectDateCheckup.setOnClickListener(view -> openStartDateDialogPicker());

        selectDateCheckupTv.setText(AppUtils.getTodayDate());
        checkupDateString = AppUtils.getTodayDateForSchedule();
        selectDateCheckupTv.setOnClickListener(view -> openStartDateDialogPicker());

        choose_vaccineLL.setOnClickListener(view -> vaccine_spinner.performClick());

        vaccine_frequencyLL.setOnClickListener(view -> vaccinefrequencySp.performClick());

        vaccineselectDate.setOnClickListener(view -> openStartDateDialogPicker());

        vaccineselectDateTv.setText(AppUtils.getTodayDate());

        vaccineselectDateTv.setOnClickListener(view -> openStartDateDialogPicker());

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        vitalbasedLLayout.setVisibility(View.VISIBLE);
                        testbasedLLayout.setVisibility(View.GONE);
                        vaccinebasedLLayout.setVisibility(View.GONE);
                        strCustomString = getString(R.string.add_vital);
                        setUpToolbar();
                        break;
                    case 1:
                        vitalbasedLLayout.setVisibility(View.GONE);
                        vaccinebasedLLayout.setVisibility(View.GONE);
                        testbasedLLayout.setVisibility(View.VISIBLE);
                        strCustomString = getString(R.string.add_checkup);
                        setUpToolbar();
                        break;
                    case 2:
                        vitalbasedLLayout.setVisibility(View.GONE);
                        testbasedLLayout.setVisibility(View.GONE);
                        vaccinebasedLLayout.setVisibility(View.VISIBLE);
                        strCustomString = getString(R.string.add_vaccines);
                        setUpToolbar();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        vital_typeLL.setOnClickListener(view -> vital_searchEt.performClick());

        vital_frequencyLL.setOnClickListener(view -> vital_frequnecy_spinner.performClick());

        vital_searchEt.setThreshold(1);
        vital_searchEt.setAdapter(findParameterAdapter);
        vital_searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppUtils.cancelAPICalls();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchTextString = vital_searchEt.getText().toString();
                if (searchTextString.length() > 0) {
                    vital_searchIv.setImageDrawable(ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.ic_cancel_black_24dp));
                    Log.i("checkitemonclick","onclick data 5 =  "+searchTextString);
                  searchAPI(Constants.VITALS, vital_searchEt.getText().toString());
                    //callParameterSearchAPI(Constants.VITALS, vital_searchEt.getText().toString());
                } else {
                    vital_searchIv.setImageDrawable(ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.search));
                    menuItem.setVisible(false);
                    paramDto = null;
                    setEmptyView();
                }
            }
        });

        vital_searchIv.setOnClickListener(view -> {
            if (vital_searchIv.getDrawable().getConstantState().equals
                    (ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.ic_cancel_black_24dp).getConstantState())) {
                vital_searchEt.setText("");
                searchTextString = "";
                menuItem.setVisible(false);
                paramDto = null;
                setEmptyView();
            }
        });

        checkup_searchEt.setThreshold(1);
        checkup_searchEt.setAdapter(findParameterAdapter);
        checkup_searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppUtils.cancelAPICalls();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchTextString = checkup_searchEt.getText().toString();
                if (searchTextString.length() > 0) {
                    checkup_searchIv.setImageDrawable(ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.ic_cancel_black_24dp));
                   searchAPI(getString(R.string.test), checkup_searchEt.getText().toString());
                    //callParameterSearchAPI(Constants.VITALS, vital_searchEt.getText().toString());
                } else {
                    vital_searchIv.setImageDrawable(ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.search));
                    menuItem.setVisible(false);
                    setEmptyView();
                    paramDto = null;
                }
            }
        });

        checkup_searchIv.setOnClickListener(view -> {
            if (checkup_searchIv.getDrawable().getConstantState().equals
                    (ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.ic_cancel_black_24dp).getConstantState())) {
                checkup_searchEt.setText("");
                searchTextString = "";
                menuItem.setVisible(false);
                paramDto = null;
                setEmptyView();
            }
        });

        vaccine_searchEt.setThreshold(1);
        vaccine_searchEt.setAdapter(findParameterAdapter);
        vaccine_searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppUtils.cancelAPICalls();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    searchTextString = vaccine_searchEt.getText().toString();
                    if (searchTextString.length() > 0) {
                        vaccine_searchIv.setImageDrawable(ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.ic_cancel_black_24dp));
                        searchAPI(getString(R.string.vaccine), vaccine_searchEt.getText().toString());
                    } else {
                        vaccine_searchIv.setImageDrawable(ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.search));
                        menuItem.setVisible(false);
                        setEmptyView();
                        paramDto = null;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        vaccine_searchIv.setOnClickListener(view -> {
            if (vaccine_searchIv.getDrawable().getConstantState().equals
                    (ContextCompat.getDrawable(ManageVitalActivity.this, R.drawable.ic_cancel_black_24dp).getConstantState())) {
                vaccine_searchEt.setText("");
                searchTextString = "";
                menuItem.setVisible(false);
                paramDto = null;
                setEmptyView();
            }
        });

        vital_frequnecy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                    case 1:
                        vitalCustomSelectDaysLL.setVisibility(View.GONE);
                        vitalcustomDayOfWeekSpinner.setVisibility(View.GONE);
                        vitalcustomDayOfMonthSpinner.setVisibility(View.GONE);
                        break;
                    case 2:
                        vitalCustomSelectDaysLL.setVisibility(View.VISIBLE);
                        vitalcustomDayOfWeekSpinner.setVisibility(View.VISIBLE);
                        vitalcustomDayOfMonthSpinner.setVisibility(View.GONE);
                        break;
                    case 3:
                        vitalCustomSelectDaysLL.setVisibility(View.VISIBLE);
                        vitalcustomDayOfMonthSpinner.setVisibility(View.VISIBLE);
                        vitalcustomDayOfWeekSpinner.setVisibility(View.GONE);
                        break;
                    case 4:
                        vitalCustomSelectDaysLL.setVisibility(View.GONE);
                        vitalcustomDayOfWeekSpinner.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        vitalCustomDayOfWeekOrMonthLL.setOnClickListener(view -> {
            if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(Constants.WEEKLY)) {
                vitalcustomDayOfWeekSpinner.performClick();
            } else if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(Constants.MONTHLY)) {
                vitalcustomDayOfMonthSpinner.performClick();
            }
        });

        choose_testLL.setOnClickListener(view -> test_spinner.performClick());

        test_frequencyLL.setOnClickListener(view -> checkupFrequencySp.performClick());
        checkupFrequencySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthlySpinnerCustomDrugAdapter = new
                MonthlySpinnerCustomDrugAdapter(ManageVitalActivity.this, AppUtils.getMonthlyList(null));

        vitalcustomDayOfMonthSpinner.setAdapter(monthlySpinnerCustomDrugAdapter);

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
                    AppUtils.openSnackBar(parentLayout, "Try Again");
                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(ManageVitalActivity.this, LoginActivity.class);
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
    private void searchAPI(String strParameterTypeString,String searchParameterString){
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        Log.i("checkitemonclick","onclick data searchParameterString =  "+searchParameterString);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.parameterlist(sharedPreferences.getString("kymPid", "134388"),searchParameterString,token).enqueue(new Callback<List<ParameterListAPIResponse>>() {
            @Override
            public void onResponse(Call<List<ParameterListAPIResponse>> call, retrofit2.Response<List<ParameterListAPIResponse>> response) {
                Log.i("checkitemonclick", "api login response 0 code = " + response.code());
                Log.i("checkitemonclick", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.i("checkitemonclick", "api login LoginNewResponse response = " + new Gson().toJson(response.body()));


                    ArrayList<ParameterSearchResultDto> list2 = new ArrayList<>();
                    for(int i=0;i<response.body().size();i++){
                        ParameterSearchResultDto diseasesDto = new ParameterSearchResultDto();
                        diseasesDto.setId(Long.parseLong(response.body().get(i).id));
                        diseasesDto.setName(response.body().get(i).parameterName);
                        diseasesDto.setMedicalParameterType(response.body().get(i).unit);
                        list2.add(diseasesDto);
                    }
                    updateSearchParameterAdapter(list2);

                    if(strParameterTypeString.matches(Constants.VITALS)){
                        Log.i("checkitemonclick","onclick data =  3");
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.SEARCHED_HEALTH_METRIC_NAME, null);
                    }
/*
                    adddrug.addAll(response.body());

                    drugDto = null;
                    drugInfoLl.setVisibility(View.GONE);
                    showHideMenuOption(false);
                    if (response.body() != null)
                        updateDrugAdapter(response.body());*/
                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    AppUtils.openSnackBar(parentLayout, "Parameter not found");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkitemonclick", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ParameterListAPIResponse>> call, Throwable t) {
                AppUtils.openSnackBar(parentLayout, t.getMessage());
                Log.i("checkitemonclick", "api error message response  = " + t.getMessage());
            }
        });
    }
    private void callParameterSearchAPI(String strParameterTypeString, String searchParameterString) {
        Log.i("checkitemonclick","onclick data strParameterTypeString =  "+strParameterTypeString);
        Log.i("checkitemonclick","onclick data searchParameterString =  "+searchParameterString);
        Log.i("checkitemonclick","onclick data url =  "+APIUrls.get().parameterSearch(userDto.getId(), strParameterTypeString, searchParameterString));
        AppUtils.logEvent(Constants.CONDITION_SEARCHED);
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(ManageVitalActivity.this);
        GenericRequest<ParameterSearchResultDto.ParameterSearchResultList> getDiseaseRequest = new GenericRequest<>
                (Request.Method.GET, APIUrls.get().parameterSearch(userDto.getId(), strParameterTypeString, searchParameterString),
                        ParameterSearchResultDto.ParameterSearchResultList.class, null,
                        diseaseDto -> {
                            authExpiredCallback.hideProgressBar();
                            Log.i("checkitemonclick","onclick data response =  "+new Gson().toJson(diseaseDto));
                            updateSearchParameterAdapter(diseaseDto);
                            if(strParameterTypeString.matches(getResources().getString(R.string.tests))){
                                Log.i("checkitemonclick","onclick data =  1");
                                AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                        Constants.SEARCHED_CHECKUP_NAME, null);
                            }
                            if(strParameterTypeString.matches(getResources().getString(R.string.vaccine))){
                                Log.i("checkitemonclick","onclick data =  2");
                                AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                        Constants.SEARCHED_VACCINE_NAME, null);
                            }
                            if(strParameterTypeString.matches(Constants.VITALS)){
                                Log.i("checkitemonclick","onclick data =  3");
                                AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                        Constants.SEARCHED_HEALTH_METRIC_NAME, null);
                            }
                        },
                        error -> {
                            AppUtils.hideKeyboard(ManageVitalActivity.this);
                            authExpiredCallback.hideProgressBar();
                        });
        authExpiredCallback.setRequest(getDiseaseRequest);
        ApiService.get().addToRequestQueue(getDiseaseRequest);
    }

    private void updateSearchParameterAdapter(ArrayList<ParameterSearchResultDto> parameterSearchDtoList) {
        ParameterSearchResultDto diseasesDropDownDto = new ParameterSearchResultDto();
       /* if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
            diseasesDropDownDto.setName(AppUtils.getEncodedString(getString(R.string.add_this_health_metric_to_your_profile)));
        } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
            diseasesDropDownDto.setName(AppUtils.getEncodedString(getString(R.string.add_this_checkup_to_your_profile)));
        } else {
            diseasesDropDownDto.setName(AppUtils.getEncodedString(getString(R.string.add_this_vaccine_to_your_profile)));
        }
        parameterSearchDtoList.add(0, diseasesDropDownDto);*/
        Log.i("addParamAPIaddParamAPI","Search 11");
        if (getContext() != null) {
            Log.i("checkitemonclick","onclick data response = 11 =11 = "+strCustomString);
            findParameterAdapter = new FindParameterAdapter(this, R.layout.view_find_drug,
                    parameterSearchDtoList, parameterSearchResultDto -> {
                Log.i("checkitemonclick","onclick data response = 11 =181 = "+strCustomString+" id = "+parameterSearchResultDto.getId());
                menuItem.setVisible(true);
                Log.i("checkitemonclick","onclick data response = 11 =18 = "+strCustomString);
                if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
                    Log.i("checkitemonclick","onclick data response = 11++++ = "+parameterSearchResultDto.getName());
                    vitalFreqLL.setVisibility(View.VISIBLE);
                    vitalParametersLL.setVisibility(View.VISIBLE);
                    //                        vitalCustomSelectDaysLL.setVisibility(View.GONE);
                    if (parameterSearchResultDto.getName().equalsIgnoreCase(AppUtils.getEncodedString(getString(R.string.add_this_health_metric_to_your_profile)))) {
                        Log.i("checkitemonclick","onclick data response = 11211 ");
                        isCustom = "true";
                        AppUtils.logEvent(Constants.PARAMETER_SCR_CUS_PARAMETER_SLCT);
                        String strSearched = searchTextString.substring(0, 1).toUpperCase() + searchTextString.substring(1);
                        String inForamtString = strSearched.trim();
                        vital_searchEt.setText(inForamtString);
                        paramterNameEt.setVisibility(View.VISIBLE);
                        unitNameEt.setVisibility(View.VISIBLE);
                        paramaterNameTv.setVisibility(View.GONE);
                        unitNameTv.setVisibility(View.GONE);
                        customParamMeasureTv.setVisibility(View.VISIBLE);
                        paramaterNameTv.setText("");
                        unitNameTv.setText("");
                        paramaterNameTv1.setText("");
                        unitNameTv1.setText("");


                        vital_searchEt.setSelection(vital_searchEt.getText().length());
                        AppUtils.hideKeyboard(ManageVitalActivity.this);
                        vital_searchEt.dismissDropDown();

                    }
                    else {
                        Log.i("checkitemonclick","onclick data response = 112 ");
                        isCustom = "false";
                        vital_searchEt.setText(parameterSearchResultDto.getName());
                        vital_searchEt.setSelection(vital_searchEt.getText().length());
                        AppUtils.hideKeyboard(ManageVitalActivity.this);
                        callGetParameterDetailsAPI(parameterSearchResultDto);
                        vital_searchEt.dismissDropDown();
                        customParamMeasureTv.setVisibility(View.GONE);
                    }

                }
                else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                    if (parameterSearchResultDto.getName().equalsIgnoreCase(AppUtils.getEncodedString(getString(R.string.add_this_checkup_to_your_profile)))) {
                        isCustom = "true";
                        AppUtils.logEvent(Constants.PARAMETER_SCR_CUS_PARAMETER_SLCT);
                        String strSearched = searchTextString.substring(0, 1).toUpperCase() + searchTextString.substring(1);
                        String inForamtString = strSearched.trim();
                        checkup_searchEt.setText(inForamtString);
                        checkup_searchEt.setSelection(checkup_searchEt.getText().length());
                        AppUtils.hideKeyboard(ManageVitalActivity.this);
                        checkupDueDateLL.setVisibility(View.VISIBLE);
                        checkupFrequencyLL.setVisibility(View.VISIBLE);
                        checkup_searchEt.dismissDropDown();
                    } else {
                        isCustom = "false";
                        checkup_searchEt.setText(parameterSearchResultDto.getName());
                        checkup_searchEt.setSelection(checkup_searchEt.getText().length());
                        AppUtils.hideKeyboard(ManageVitalActivity.this);
                        callGetParameterDetailsAPI(parameterSearchResultDto);
                        checkup_searchEt.dismissDropDown();

                    }

                }
                else {
                    if (parameterSearchResultDto.getName().equalsIgnoreCase(AppUtils.getEncodedString(getString(R.string.add_this_vaccine_to_your_profile)))) {
                        isCustom = "true";
                        AppUtils.logEvent(Constants.PARAMETER_SCR_CUS_PARAMETER_SLCT);
                        String strSearched = searchTextString.substring(0, 1).toUpperCase() + searchTextString.substring(1);
                        String inForamtString = strSearched.trim();
                        vaccine_searchEt.setText(inForamtString);
                        vaccine_searchEt.setSelection(vaccine_searchEt.getText().length());
                        AppUtils.hideKeyboard(ManageVitalActivity.this);
                        vaccineDueDateLL.setVisibility(View.VISIBLE);
                        vaccineFrequencyLL.setVisibility(View.VISIBLE);
                        vaccine_searchEt.dismissDropDown();
                    } else {
                        isCustom = "false";
                        vaccine_searchEt.setText(parameterSearchResultDto.getName());
                        vaccine_searchEt.setSelection(vaccine_searchEt.getText().length());
                        AppUtils.hideKeyboard(ManageVitalActivity.this);
                        callGetParameterDetailsAPI(parameterSearchResultDto);
                        vaccine_searchEt.dismissDropDown();
                    }

                }
            });
            if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {

                if (vital_searchEt != null && findParameterAdapter != null) {
                    vital_searchEt.setAdapter(findParameterAdapter);
                    findParameterAdapter.notifyDataSetChanged();
                }
            } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                if (checkup_searchEt != null && findParameterAdapter != null) {
                    checkup_searchEt.setAdapter(findParameterAdapter);
                    findParameterAdapter.notifyDataSetChanged();
                }
            } else {
                if (vaccine_searchEt != null && findParameterAdapter != null) {
                    vaccine_searchEt.setAdapter(findParameterAdapter);
                    findParameterAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void callGetParameterDetailsAPI(ParameterSearchResultDto parameterSearchResultDto) {
        Log.i("addParamAPIaddParamAPI", "Search 12235 = " );
        if (parameterSearchResultDto != null) {
            progress.show();
            Log.i("addParamAPIaddParamAPI", "Search 1223256 = " );
            UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
            String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
            service.getParameterData(String.valueOf(parameterSearchResultDto.getId()), token).enqueue(new Callback<ParamDto>() {
                @Override
                public void onResponse(Call<ParamDto> call, Response<ParamDto> response) {
                    Log.i("version_data", "upload data  list response 0 code = " + response.code());
                    Log.i("version_data", "upload data  list body = " + new Gson().toJson(response.body()));

                    if (response.isSuccessful()) {
                        AppUtils.hideProgressBar(progress);
                        paramDto = response.body();
                        Log.i("addParamAPIaddParamAPI", "Search 123 = " + new Gson().toJson(response.body()));
                        Log.i("addParamAPIaddParamAPI", "onclick data response = 76876 =  " + new Gson().toJson(response.body()));
                        /*if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {*/
                            updateVitals(paramDto);
                      /*  } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                            updateCheckups(paramDto);
                        } else {
                            updateVaccines(paramDto);
                        }*/
                    }else if(response.code() ==401){
                        refreshToken();
                    }

                }

                @Override
                public void onFailure(Call<ParamDto> call, Throwable t) {
                    Log.i("addParamAPIaddParamAPI", "api error message response  = " + t.getMessage());
                }
            });
        }
    }

   /* private void callGetParameterDetailsAPI(ParameterSearchResultDto parameterSearchResultDto) {
        Log.i("addParamAPIaddParamAPI","Search 122");
        Log.i("checkitemonclick","onclick data response = url 786 =  "+APIUrls.get().getParameterDetails(parameterSearchResultDto.getId()));
        if (parameterSearchResultDto != null) {
            progress.show();
            GenericRequestWithoutAuth<ParamDto> getScheduleRequest = new GenericRequestWithoutAuth<>
                    (Request.Method.GET, "http://staging-karmahealth.knowyourmeds.com:8899/api/auth/parameters/parameter/"+parameterSearchResultDto.getId(),
                            ParamDto.class, null,
                            diseaseParameterResponseDTO -> {
                                AppUtils.hideProgressBar(progress);
                                paramDto = diseaseParameterResponseDTO;
                                Log.i("addParamAPIaddParamAPI","Search 123 = "+new Gson().toJson(diseaseParameterResponseDTO));
                                Log.i("checkitemonclick","onclick data response = 76876 =  "+new Gson().toJson(diseaseParameterResponseDTO));
                                if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
                                    updateVitals(paramDto);
                                } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                                    updateCheckups(paramDto);
                                } else {
                                    updateVaccines(paramDto);
                                }
                            },
                            error -> {
                                Log.i("addParamAPIaddParamAPI","Search error = ");
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(ManageVitalActivity.this, error);
                                AppUtils.openSnackBar(parentLayout, res);
                            });
            ApiService.get().addToRequestQueue(getScheduleRequest);
        }
    }*/

    private void updateCheckups(ParamDto paramDto) {
        if (!searchTextString.isEmpty()) {
            // mangeTv.setText(getString(R.string.select_this_disease));
            checkupDueDateLL.setVisibility(View.VISIBLE);
            checkupFrequencyLL.setVisibility(View.VISIBLE);
            if (paramDto != null) {
                checkupFrequencyEt.setText(AppUtils.getValueWithoutDecimal(paramDto.getFrequencyNumber()));
                checkupFrequencyEt.setSelection(checkupFrequencyEt.getText().length());
                checkupFrequencySp.setSelection(getIndex(checkupFrequencySp, paramDto.getFrequencyUnit()));
            }
        } else {
            checkupDueDateLL.setVisibility(View.GONE);
            checkupFrequencyLL.setVisibility(View.GONE);
            customCheckupNameLL.setVisibility(View.GONE);
        }
    }

    private void updateVaccines(ParamDto paramDto) {
        if (!searchTextString.isEmpty()) {
            vaccineDueDateLL.setVisibility(View.VISIBLE);
            vaccineFrequencyLL.setVisibility(View.VISIBLE);
            if (paramDto != null) {
                vaccineFrequencyEt.setText(AppUtils.getValueWithoutDecimal(paramDto.getFrequencyNumber()));
                vaccineFrequencyEt.setSelection(vaccineFrequencyEt.getText().length());
                vaccinefrequencySp.setSelection(getIndex(vaccinefrequencySp, paramDto.getFrequencyUnit()));
                customVaccineNameLL.setVisibility(View.GONE);
            }
        } else {
            vaccineDueDateLL.setVisibility(View.GONE);
            vaccineFrequencyLL.setVisibility(View.GONE);
            customVaccineNameLL.setVisibility(View.GONE);
        }
    }

    private void updateVitals(ParamDto paramDto) {
        Log.i("checkitemonclick","onclick data updateVitals =  ");
        Log.i("checkitemonclick","onclick data getMinBaselineDisplayName =  "+paramDto.getMinBaselineDisplayName());
        Log.i("checkitemonclick","onclick data getFrequencyUnit =  "+paramDto.getFrequencyUnit());
        paramaterNameTv.setVisibility(View.VISIBLE);
        unitNameTv.setVisibility(View.VISIBLE);
        customParamMeasureTv.setVisibility(View.GONE);
        paramterNameEt.setVisibility(View.GONE);
        paramterNameEt.setText("");
        unitNameEt.setVisibility(View.GONE);
        unitNameEt.setText("");
        paramaterNameTv.setText(paramDto.getMaxBaselineDisplayName());
        unitNameTv.setText(paramDto.getMeasurementUnit());

        if (paramDto.getMinBaselineDisplayName() != null && !paramDto.getMinBaselineDisplayName().isEmpty()) {
            paramaterNameTv1.setVisibility(View.VISIBLE);
            unitNameTv1.setVisibility(View.VISIBLE);
            paramaterNameTv1.setText(paramDto.getMinBaselineDisplayName());
            unitNameTv1.setText(paramDto.getMeasurementUnit());
        } else {
            paramaterNameTv1.setText("");
            unitNameTv1.setText("");
        }
        if (paramDto.getFrequencyUnit() != null) {
            vital_frequnecy_spinner.setSelection(getIndexForVital(vital_frequnecy_spinner, paramDto.getFrequencyUnit()));
            if (paramDto.getFrequencyUnit().equalsIgnoreCase(getString(R.string.week))) {
                vitalcustomDayOfWeekSpinner.setSelection(getIndex(vitalcustomDayOfWeekSpinner, paramDto.getFrequencyValue()));
            } else if (paramDto.getFrequencyUnit().equalsIgnoreCase(getString(R.string.month))) {
                vitalcustomDayOfMonthSpinner.setSelection(getIndex(vitalcustomDayOfMonthSpinner, paramDto.getFrequencyValue()));
            }
        }
    }

    public interface searchParameterCallback {
        void callParameterDto(ParameterSearchResultDto parameterSearchResultDto);
    }

    private void openStartDateDialogPicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String startDateStr = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                    //startDateTv.setText(startDateStr);
                    if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup)))
                        selectDateCheckupTv.setText(startDateStr);
                    else if (strCustomString.equalsIgnoreCase(getString(R.string.add_vaccines)))
                        vaccineselectDateTv.setText(startDateStr);
                    checkupDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                }, mYear, mMonth, mDay);

        c.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        AppUtils.setDialogBoxButton(ManageVitalActivity.this, datePickerDialog);

        AppUtils.setSelectedDate(datePickerDialog, selectDateCheckupTv.getText().toString().trim());

        datePickerDialog.show();
    }

    private void generateUserList(UserDto selectedUserDto) {

        userNameTv.setVisibility(View.GONE);
        userSpinner.setVisibility(View.VISIBLE);
        final List<UserDto> userDtoList = new ArrayList<>();

        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto uDto = loginResponse.getUserDTO();
            if (uDto != null) {
                userDtoList.add(uDto);
                userDto = uDto;
            }
        }
        UserDto uDto;
        List<DependentDto> dependentDtoList = ApplicationDB.get().getDependents();
        if (dependentDtoList != null && dependentDtoList.size() > 0) {
            for (int i = 0; i < dependentDtoList.size(); i++) {
                DependentDto dependentDto = dependentDtoList.get(i);
                uDto = new UserDto();

                uDto.setName(dependentDto.getName());
                uDto.setId(dependentDto.getId());

                userDtoList.add(uDto);
            }
        }

        UserSpinnerAdapter userSpinnerAdapter = new UserSpinnerAdapter(ManageVitalActivity.this, userDtoList,sharedPreferences.getString("Pname",""));
        userSpinner.setAdapter(userSpinnerAdapter);
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userDto = userDtoList.get(position);
                AppUtils.logEvent(Constants.PARAMETER_SCR_PROFILE_SELECTION_CLK);
//                AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_PROFILE_SLCT);
                //callGetDiseasesAPI();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectedUserDto != null) {
            for (int i = 0; i < userDtoList.size(); i++) {
                UserDto localUserDto = userDtoList.get(i);
                if (localUserDto.getId().equals(selectedUserDto.getId())) {
                    userSpinner.setSelection(i);
                    userDto = selectedUserDto;
                }
            }
        }
    }

    private void callCustomParameterAPI() {
        if (ManageVitalActivity.this != null && userDto != null) {
            progress.show();

            GenericRequestWithoutAuth<CustomparamterDto> getCustomparamterRequest = new GenericRequestWithoutAuth<>
                    (Request.Method.GET, APIUrls.get().getAllParameters(),
                            CustomparamterDto.class, null,
                            diseaseDto -> {
                                AppUtils.hideProgressBar(progress);
                                customparamterDto = diseaseDto;
                                if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
                                    updateParameterDropDownData(diseaseDto.getVitals());
                                } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                                    updateParameterDropDownData(diseaseDto.getTests());
                                } else {
                                    updateParameterDropDownData(diseaseDto.getVaccines());
                                }
                            },
                            error -> {
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(ManageVitalActivity.this, error);
                                AppUtils.openSnackBar(parentLayout, res);
                            });
            ApiService.get().addToRequestQueue(getCustomparamterRequest);
        }
    }

    private void updateParameterDropDownData(final List<ParamDto> parameterDropDownDtoList) {
        Log.i("addParamAPIaddParamAPI","Search 10");
        Log.i("checkitemonclick","onclick data response = 11 =11 =parameterDropDownDtoList = "+parameterDropDownDtoList);
        if (parameterDropDownDtoList != null && parameterDropDownDtoList.size() > 0) {

            final ParamDto paramDropDown = new ParamDto();

            if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
                paramDropDown.setName(AppUtils.getEncodedString(getString(R.string.add_this_health_metric_to_your_profile)));
            } else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                paramDropDown.setName(AppUtils.getEncodedString(getString(R.string.add_this_checkup_to_your_profile)));
            } else {
                paramDropDown.setName(AppUtils.getEncodedString(getString(R.string.add_this_vaccine_to_your_profile)));
            }
            parameterDropDownDtoList.add(0, paramDropDown);

            paramDtoList = parameterDropDownDtoList;
            parameterSpinnerAdapter = new ParameterSpinnerAdapter(ManageVitalActivity.this, parameterDropDownDtoList);
            if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {

                vital_spinner.setAdapter(parameterSpinnerAdapter);
                vital_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("addParamAPIaddParamAPI","Search 7");
                        if (position != 0) {
                            // mangeTv.setText(getString(R.string.select_this_disease));
                            vitalFreqLL.setVisibility(View.VISIBLE);
                            vitalParametersLL.setVisibility(View.VISIBLE);
                            paramDto = parameterDropDownDtoList.get(position);
                            Log.i("addParamAPIaddParamAPI","Search 0");
                            if (paramDto != null) {
                                String dropDownName = paramDto.getName();
                                if (dropDownName.equalsIgnoreCase(getString(R.string.add_this_health_metric_to_your_profile))) {
                                    /// setEmptyView();
                                    /// setCustomView(View.VISIBLE);
                                    // customVitalNameLL.setVisibility(View.VISIBLE);
                                    paramterNameEt.setVisibility(View.VISIBLE);
                                    unitNameEt.setVisibility(View.VISIBLE);
                                    paramaterNameTv.setVisibility(View.GONE);
                                    unitNameTv.setVisibility(View.GONE);
                                    customParamMeasureTv.setVisibility(View.VISIBLE);
                                    paramaterNameTv.setText("");
                                    unitNameTv.setText("");
                                    paramaterNameTv1.setText("");
                                    unitNameTv1.setText("");
                                } else {
                                    ///  setCustomView(View.GONE);
                                    //   setMenuItemVisibility(true);
                                    //    callGetDiseaseScheduleAPI(diseasesDropDownDto);
                                    //customVitalNameLL.setVisibility(View.GONE);
                                    paramaterNameTv.setVisibility(View.VISIBLE);
                                    unitNameTv.setVisibility(View.VISIBLE);
                                    customParamMeasureTv.setVisibility(View.GONE);
                                    paramterNameEt.setVisibility(View.GONE);
                                    paramterNameEt.setText("");
                                    unitNameEt.setVisibility(View.GONE);
                                    unitNameEt.setText("");
                                    paramaterNameTv.setText(paramDto.getMaxBaselineDisplayName());
                                    unitNameTv.setText(paramDto.getMeasurementUnit());
                                    if (paramDto.getMinBaselineDisplayName() != null && !paramDto.getMinBaselineDisplayName().isEmpty()) {
                                        paramaterNameTv1.setVisibility(View.VISIBLE);
                                        unitNameTv1.setVisibility(View.VISIBLE);
                                        paramaterNameTv1.setText(paramDto.getMinBaselineDisplayName());
                                        unitNameTv1.setText(paramDto.getMeasurementUnit());
                                    } else {
                                        paramaterNameTv1.setText("");
                                        unitNameTv1.setText("");
                                    }
                                    if (paramDto.getFrequencyUnit() != null) {
                                        vital_frequnecy_spinner.setSelection(getIndexForVital(vital_frequnecy_spinner, paramDto.getFrequencyUnit()));
                                        if (paramDto.getFrequencyUnit().equalsIgnoreCase(getString(R.string.week))) {
                                            vitalcustomDayOfWeekSpinner.setSelection(getIndex(vitalcustomDayOfWeekSpinner, paramDto.getFrequencyValue()));
                                        } else if (paramDto.getFrequencyUnit().equalsIgnoreCase(getString(R.string.week))) {
                                            vitalcustomDayOfMonthSpinner.setSelection(getIndex(vitalcustomDayOfMonthSpinner, paramDto.getFrequencyValue()));
                                        }
                                    }
                                }
                            }
                        } else {
                            AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_DISEASE_SELECTION_CLK);
                            vitalFreqLL.setVisibility(View.GONE);
                            vitalParametersLL.setVisibility(View.GONE);
                            // customVitalNameLL.setVisibility(View.GONE);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if (paramDto != null) {
                    for (int i = 0; i < parameterDropDownDtoList.size(); i++) {
                        if (paramDto.getId() == parameterDropDownDtoList.get(i).getId()) {
                            vital_spinner.setSelection(i);
                        }
                    }
                }
            } else
                if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
                test_spinner.setAdapter(parameterSpinnerAdapter);
                test_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            // mangeTv.setText(getString(R.string.select_this_disease));
                            checkupDueDateLL.setVisibility(View.VISIBLE);
                            checkupFrequencyLL.setVisibility(View.VISIBLE);
                            paramDto = parameterDropDownDtoList.get(position);
                            Log.i("addParamAPIaddParamAPI","Search 1");
                            if (paramDto != null) {
                                String dropDownName = paramDto.getName();
                                if (dropDownName.equalsIgnoreCase(getString(R.string.custom))) {
                                    /// setEmptyView();
                                    /// setCustomView(View.VISIBLE);
                                    customCheckupNameLL.setVisibility(View.VISIBLE);
                                    //
                                } else {
                                    ///  setCustomView(View.GONE);
                                    //   setMenuItemVisibility(true);
                                    //    callGetDiseaseScheduleAPI(diseasesDropDownDto);
                                    checkupFrequencyEt.setText(AppUtils.getValueWithoutDecimal(paramDto.getFrequencyNumber()));
                                    checkupFrequencyEt.setSelection(checkupFrequencyEt.getText().length());
                                    checkupFrequencySp.setSelection(getIndex(checkupFrequencySp, paramDto.getFrequencyUnit()));
                                    customCheckupNameLL.setVisibility(View.GONE);
                                    //
                                }
                            }
                        } else {
                            checkupDueDateLL.setVisibility(View.GONE);
                            checkupFrequencyLL.setVisibility(View.GONE);
                            customCheckupNameLL.setVisibility(View.GONE);
                            //AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_DISEASE_SELECTION_CLK);
                            //setEmptyView();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if (paramDto != null) {
                    for (int i = 0; i < parameterDropDownDtoList.size(); i++) {
                        if (paramDto.getId() == parameterDropDownDtoList.get(i).getId()) {
                            test_spinner.setSelection(i);
                        }
                    }
                }
            } else
            {
                Log.i("addParamAPIaddParamAPI","Search 2");
                vaccine_spinner.setAdapter(parameterSpinnerAdapter);
                vaccine_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            // mangeTv.setText(getString(R.string.select_this_disease));
                            vaccineDueDateLL.setVisibility(View.VISIBLE);
                            vaccineFrequencyLL.setVisibility(View.VISIBLE);
                            paramDto = parameterDropDownDtoList.get(position);
                            Log.i("addParamAPIaddParamAPI","Search 4");
                            if (paramDto != null) {
                                String dropDownName = paramDto.getName();
                                if (dropDownName.equalsIgnoreCase(getString(R.string.custom))) {
                                    /// setEmptyView();
                                    /// setCustomView(View.VISIBLE);
                                    customVaccineNameLL.setVisibility(View.VISIBLE);
                                } else {
                                    ///  setCustomView(View.GONE);
                                    //   setMenuItemVisibility(true);
                                    //    callGetDiseaseScheduleAPI(diseasesDropDownDto);
                                    vaccineFrequencyEt.setText(AppUtils.getValueWithoutDecimal(paramDto.getFrequencyNumber()));
                                    vaccineFrequencyEt.setSelection(vaccineFrequencyEt.getText().length());
                                    vaccinefrequencySp.setSelection(getIndex(vaccinefrequencySp, paramDto.getFrequencyUnit()));
                                    customVaccineNameLL.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            vaccineDueDateLL.setVisibility(View.GONE);
                            vaccineFrequencyLL.setVisibility(View.GONE);
                            customVaccineNameLL.setVisibility(View.GONE);
                            //AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_DISEASE_SELECTION_CLK);
                            //setEmptyView();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Log.i("addParamAPIaddParamAPI","Search 5");

                if (paramDto != null) {
                    for (int i = 0; i < parameterDropDownDtoList.size(); i++) {
                        if (paramDto.getId() == parameterDropDownDtoList.get(i).getId()) {
                            vaccine_spinner.setSelection(i);
                        }
                    }
                }
            }
        }
    }

    private int getIndexForVital(Spinner spinner, String frequencyUnit) {

        if (frequencyUnit.equalsIgnoreCase("DAY")) {
            frequencyUnit = "Daily";
        } else if (frequencyUnit.equalsIgnoreCase("WEEK")) {
            frequencyUnit = "Weekly";
        } else if (frequencyUnit.equalsIgnoreCase("MONTH")) {
            frequencyUnit = "Monthly";
        }
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(frequencyUnit)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndex(Spinner spinner, String frequencyUnit) {

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(frequencyUnit)) {
                return i;
            }
        }
        return 0;
    }

    private void iniIds() {
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();


        progress = ProgressDialogSetup.getProgressDialog(ManageVitalActivity.this);
        parentLayout = findViewById(R.id.parentLayout);
        userNameTv = findViewById(R.id.userNameTv);
        userSpinner = findViewById(R.id.userSpinner);
        vitalbasedLLayout = (LinearLayout) findViewById(R.id.vitalbasedLLayout);
        condition_typeLL = (LinearLayout) findViewById(R.id.condition_typeLL);
        vaccinebasedLLayout = (LinearLayout) findViewById(R.id.vaccinebasedLLayout);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        //vital_spinner = (Spinner)findViewById(R.id.vital_spinner);
        vital_frequnecy_spinner = (Spinner) findViewById(R.id.vital_frequency_spinner);
        vital_typeLL = (LinearLayout) findViewById(R.id.vital_typeLL);
        vital_frequencyLL = (LinearLayout) findViewById(R.id.vital_frequencyLL);
        choose_testLL = (LinearLayout) findViewById(R.id.choose_testLL);
        //choose_test_spinner=(Spinner) findViewById(R.id.test_spinner);
        test_frequencyLL = (LinearLayout) findViewById(R.id.test_frequencyLL);
        testbasedLLayout = (LinearLayout) findViewById(R.id.testbasedLLayout);
        test_spinner = (Spinner) findViewById(R.id.test_spinner);
        customCheckupNameLL = (LinearLayout) findViewById(R.id.customCheckupNameLL);
        customVaccineNameLL = (LinearLayout) findViewById(R.id.customVaccineNameLL);
        // ` = (LinearLayout)findViewById(R.id.customVitalNameLL);
        vaccine_spinner = (Spinner) findViewById(R.id.vaccine_spinner);
        unitNameEt = (EditText) findViewById(R.id.unitNameEt);
        paramterNameEt = (EditText) findViewById(R.id.parameterNameEt);
        paramaterNameTv = (TextView) findViewById(R.id.paramterNameTv);
        paramaterNameTv1 = (TextView) findViewById(R.id.paramterNameTv1);
        unitNameTv = (TextView) findViewById(R.id.unitNameTv);
        unitNameTv1 = (TextView) findViewById(R.id.unitNameTv1);
        vaccineFrequencyEt = (EditText) findViewById(R.id.vaccineFrequencyEt);
        checkupFrequencyEt = (EditText) findViewById(R.id.checkupFrequencyEt);
        vaccinefrequencySp = (Spinner) findViewById(R.id.vaccinefrequencySp);
        checkupFrequencySp = (Spinner) findViewById(R.id.checkupFrequencySp);
        vitalcustomDayOfWeekSpinner = (Spinner) findViewById(R.id.spVitalCustomDayOfWeek);
        vitalcustomDayOfMonthSpinner = (Spinner) findViewById(R.id.spVitalCustomDayOfMonth);
        vitalCustomSelectDaysLL = (LinearLayout) findViewById(R.id.vitalCustomSelectDaysLL);
        vitalCustomDayOfWeekOrMonthLL = (LinearLayout) findViewById(R.id.vitalCustomDayOfWeekOrMonthLL);
        selectDateCheckupTv = (TextView) findViewById(R.id.selectDateCheckupTv);
        selectDateCheckup = (ImageView) findViewById(R.id.selectDateCheckup);
        vaccineselectDateTv = (TextView) findViewById(R.id.vaccineselectDateTv);
        vaccineselectDate = (ImageView) findViewById(R.id.vaccineselectDate);
        vitalFreqLL = findViewById(R.id.vitalFreqLL);
        vitalParametersLL = findViewById(R.id.vitalParametersLL);
        checkupFrequencyLL = findViewById(R.id.checkupFrequencyLL);
        checkupDueDateLL = findViewById(R.id.checkupDueDateLL);
        vaccineDueDateLL = findViewById(R.id.vaccineDueDateLL);
        vaccineFrequencyLL = findViewById(R.id.vaccineFrequencyLL);
        customParamMeasureTv = findViewById(R.id.customParamMeasureTv);
        choose_vaccineLL = findViewById(R.id.choose_vaccineLL);
        vaccine_frequencyLL = findViewById(R.id.vaccine_frequencyLL);
        vital_searchEt = findViewById(R.id.vital_searchEt);
        vital_searchIv = findViewById(R.id.vital_searchIv);
        checkup_searchEt = findViewById(R.id.checkup_searchEt);
        vaccine_searchEt = findViewById(R.id.vaccine_searchEt);
        checkup_searchIv = findViewById(R.id.checkup_searchIv);
        vaccine_searchIv = findViewById(R.id.vaccine_searchIv);
        vital_searchEt.setFilters(new InputFilter[]{AppUtils.setInputFilterToRestrictEmoticons()});
        checkup_searchEt.setFilters(new InputFilter[]{AppUtils.setInputFilterToRestrictEmoticons()});
        vaccine_searchEt.setFilters(new InputFilter[]{AppUtils.setInputFilterToRestrictEmoticons()});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);

        menuItem = menu.findItem(R.id.done);
        menuItem.setTitle(getString(R.string.done));
        menuItem.setVisible(false);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AppUtils.logEvent(Constants.PARAM_ADD_PARAM_SCR_BACK_BTN_CLK);
            finish();
        } else if (item.getItemId() == R.id.done) {
            AppUtils.logEvent(Constants.PARAMETER_SCR_DONE_BTN_CLK);
            AppUtils.hideKeyboard(ManageVitalActivity.this);
            checkfields();
        }
        return super.onOptionsItemSelected(item);
    }


    public void checkfields() {
        ParamDto param;

        if (strCustomString.equalsIgnoreCase(getString(R.string.add_vital))) {
            HashMap<String , Object> healthMetricParametersMap = new HashMap<>();
            AppUtils.logEvent(Constants.PARAMETER_SCR_VITAL_FORM_SUBMITTED);
            //if(!strvital_spinner.equalsIgnoreCase(getString(R.string.select))) {
            if (!vital_searchEt.getText().toString().trim().equalsIgnoreCase("") && vital_searchEt.getText().toString().trim() != null) {
                Log.i("checkitemonclickCustom","is Custom = "+String.valueOf(isCustom));
                if (isCustom.equalsIgnoreCase("true")) {
                    if (!vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select)) && !paramterNameEt.getText().toString().trim().isEmpty() && paramterNameEt.getText().toString().trim() != null && !unitNameEt.getText().toString().trim().isEmpty() && unitNameEt.getText().toString().trim() != null) {
                        CustomParamDto customParam = new CustomParamDto();
                        customParam.setMedicalParameterType("VITALS");
                        customParam.setFrequencyNumber(1.0);
                        //AppUtils.capitalize(etVitalName.getText().toString());
                        customParam.setName(AppUtils.capitalize(vital_searchEt.getText().toString().trim()));
                        if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase("Daily")) {
                            customParam.setFrequencyUnit(getString(R.string.sp_day));
                            //param.setFrequencyUnit(vital_frequnecy_spinner.getSelectedItem().toString());
                            customParam.setFrequencyValue("");
                            customParam.setFrequencyNumber(1.0);
                        } else if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase("Weekly")) {
                            customParam.setFrequencyUnit(getString(R.string.week));
                            customParam.setFrequencyValue(vitalcustomDayOfWeekSpinner.getSelectedItem().toString().toUpperCase());
                        } else {
                            customParam.setFrequencyUnit(getString(R.string.month));
                            int pos = vitalcustomDayOfMonthSpinner.getSelectedItemPosition();
                            MonthlyDays monthlyDays = (MonthlyDays) monthlySpinnerCustomDrugAdapter.getItem(pos);
                            String dateofMonth = String.valueOf(monthlyDays.getDay());
                            customParam.setFrequencyValue(dateofMonth);
                        }
                        customParam.setMeasurementUnit(unitNameEt.getText().toString().trim());
                        customParam.setMaxBaselineDisplayName(paramterNameEt.getText().toString().trim());
                        customParam.setMaxBaselineValue(0.0);
                        customParam.setMinBaselineValue(0.0);

                        healthMetricParametersMap.put("Health Metric Name", customParam.getName());
                        healthMetricParametersMap.put("Frequency Unit", customParam.getFrequencyUnit());
                        healthMetricParametersMap.put("Parameter Name", customParam.getMedicalParameterType());
                        healthMetricParametersMap.put("Unit", customParam.getMeasurementUnit());
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.ADD_HEALTH_METRIC_FORM_SUBMITTED, healthMetricParametersMap);

                        addCustomParameterAPI(customParam);
                        //param.
                    } else {
                        if (vital_searchEt.getText().toString().equalsIgnoreCase("") || vital_searchEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_type_customvital));
                        } else if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency));

                        } else if (paramterNameEt.getText().toString().trim().isEmpty() || paramterNameEt.getText().toString().trim() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_parameter_name));
                        } else if (unitNameEt.getText().toString().trim().isEmpty() || unitNameEt.getText().toString().trim() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_unit_name));
                        }
                        //AppUtils.openSnackBar(parentLayout, "Check the fields");
                    }


                }
                else if (isCustom.equalsIgnoreCase("false")) {


                    param = paramDto;
//                    param.setMedicalParameterType("VITALS");
                    param.setFrequencyNumber(1.0);
                    if (!vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                        if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.daily)/*"Daily"*/)) {
                            param.setFrequencyUnit("DAY");   ///vitalcustomDayOfWeekSpinner.getSelectedItem().toString()

                        } else if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.weekly)/*"Weekly"*/)) {
                            param.setFrequencyUnit("WEEK");
                          //  param.setFrequencyValue(vitalcustomDayOfWeekSpinner.getSelectedItem().toString().toUpperCase());
                            param.setFrequencyValue(String.valueOf(vitalcustomDayOfWeekSpinner.getSelectedItemPosition()+1));
                        } else {
                            param.setFrequencyUnit("MONTH");
                            int pos = vitalcustomDayOfMonthSpinner.getSelectedItemPosition();
                            MonthlyDays monthlyDays = (MonthlyDays) monthlySpinnerCustomDrugAdapter.getItem(pos);
                            String dateofMonth = String.valueOf(monthlyDays.getDay());
                            param.setFrequencyValue(dateofMonth);
                        }

                        healthMetricParametersMap.put("Health Metric Name", param.getName());
                        healthMetricParametersMap.put("Frequency Unit", param.getFrequencyUnit());
                        healthMetricParametersMap.put("Parameter Name", param.getMedicalParameterType());
                        healthMetricParametersMap.put("Unit", param.getFrequencyUnit());
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.ADD_HEALTH_METRIC_FORM_SUBMITTED, healthMetricParametersMap);
                       addParamAPI(param);
                        //addParameterAPI(param);
                    } else {
                        if (vital_frequnecy_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency));

                        }
                    }
                }
                else {
                    AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_vital));
                }

            } else {
                AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_vital));
            }
        }
        else if (strCustomString.equalsIgnoreCase(getString(R.string.add_checkup))) {
            HashMap<String , Object> checkupParametersMap = new HashMap<>();
            AppUtils.logEvent(Constants.PARAMETER_SCR_TEST_FORM_SUBMITTED);
            if (!checkup_searchEt.getText().toString().equalsIgnoreCase("") && checkup_searchEt.getText().toString() != null) {
                if (isCustom.equalsIgnoreCase("true")) {
                    CustomParamDto customParamDto = new CustomParamDto();
                    customParamDto.setMedicalParameterType("TESTS");
                    if (!checkupFrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select)) && !checkupFrequencyEt.getText().toString().isEmpty() && checkupFrequencyEt.getText().toString() != null && !checkupFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                        customParamDto.setName(AppUtils.capitalize(checkup_searchEt.getText().toString().trim()));
                        customParamDto.setFrequencyNumber(Double.parseDouble(checkupFrequencyEt.getText().toString()));
                        customParamDto.setFrequencyUnit(checkupFrequencySp.getSelectedItem().toString().toUpperCase(Locale.ENGLISH));
                        customParamDto.setNextDueDate(checkupDateString);
                        //customParamDto.setMaxBaselineDisplayName(paramterNameEt.getText().toString());
                        customParamDto.setMaxBaselineValue(0.0);
                        customParamDto.setMinBaselineValue(0.0);

                        checkupParametersMap.put("Health Metric Name", customParamDto.getName());
                        checkupParametersMap.put("Frequency Unit", customParamDto.getFrequencyUnit());
                        checkupParametersMap.put("Parameter Name", customParamDto.getMedicalParameterType());
                        checkupParametersMap.put("Unit", customParamDto.getMeasurementUnit());
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.ADD_CHECK_UP_FORM_SUBMITTED, checkupParametersMap);

                        addCustomParameterAPI(customParamDto);

                    } else {
                        if (checkup_searchEt.getText().toString().equalsIgnoreCase("") || checkup_searchEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_check_name));
                        } else if (checkupFrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency));
                        } else if (checkupFrequencyEt.getText().toString().isEmpty() || checkupFrequencyEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value));
                        } else if (checkupFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value_should_be_greater_than_zero));
                        }
                    }

                } else if (isCustom.equalsIgnoreCase("false")) {
                    if (!checkupFrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select)) && !checkupFrequencyEt.getText().toString().isEmpty() && checkupFrequencyEt.getText().toString() != null && !checkupFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                        param = paramDto;
                        param.setMedicalParameterType("TESTS");
                        param.setName(param.getName());
                        param.setFrequencyNumber(Double.parseDouble(checkupFrequencyEt.getText().toString()));
                        param.setFrequencyUnit(checkupFrequencySp.getSelectedItem().toString().toUpperCase());
                        param.setNextDueDate(checkupDateString);

                        checkupParametersMap.put("Health Metric Name", param.getName());
                        checkupParametersMap.put("Frequency Unit", param.getFrequencyUnit());
                        checkupParametersMap.put("Parameter Name", param.getMedicalParameterType());
                        checkupParametersMap.put("Unit", param.getMeasurementUnit());
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.ADD_CHECK_UP_FORM_SUBMITTED, checkupParametersMap);

                        addParameterAPI(param);
                    } else {
                        if (checkupFrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency));
                        } else if (checkupFrequencyEt.getText().toString().isEmpty() || checkupFrequencyEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value));
                        } else if (checkupFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value_should_be_greater_than_zero));
                        }
                    }
                } else {
                    AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_option_from_the_list));
                }

            } else {
                AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_checkup_from_the_list));
            }


        }
        else {
            HashMap<String , Object> vaccineParametersMap = new HashMap<>();
            AppUtils.logEvent(Constants.PARAMETER_SCR_VACCN_FORM_SUBMITTED);
            if (!vaccine_searchEt.getText().toString().equalsIgnoreCase("") && vaccine_searchEt.getText().toString() != null) {
                if (isCustom.equalsIgnoreCase("true")) {
                    CustomParamDto customParamDto = new CustomParamDto();
                    customParamDto.setMedicalParameterType("VACCINES");
                    if (!vaccinefrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select)) && !vaccineFrequencyEt.getText().toString().isEmpty() && vaccineFrequencyEt.getText().toString() != null && !vaccineFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                        customParamDto.setName(AppUtils.capitalize(vaccine_searchEt.getText().toString().trim()));
                        customParamDto.setFrequencyNumber(Double.parseDouble(vaccineFrequencyEt.getText().toString()));
                        customParamDto.setFrequencyUnit(vaccinefrequencySp.getSelectedItem().toString().toUpperCase(Locale.ENGLISH));
                        customParamDto.setNextDueDate(checkupDateString);
                        customParamDto.setMaxBaselineValue(0.0);
                        customParamDto.setMinBaselineValue(0.0);

                        vaccineParametersMap.put("Vaccine Name", customParamDto.getName());
                        vaccineParametersMap.put("Frequency Number", customParamDto.getFrequencyNumber());
                        vaccineParametersMap.put("Frequency Unit", customParamDto.getFrequencyUnit());
                        vaccineParametersMap.put("Next Due Date", customParamDto.getNextDueDate());
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.ADD_VACCINE_FORM_SUBMITTED, vaccineParametersMap);

                        addCustomParameterAPI(customParamDto);
                    } else {
                        if (vaccine_searchEt.getText().toString().equalsIgnoreCase("") || vaccine_searchEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_vaccine_name));
                        } else if (vaccinefrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency));
                        } else if (vaccineFrequencyEt.getText().toString().isEmpty() || vaccineFrequencyEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value));
                        } else if (vaccineFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value_should_be_greater_than_zero));
                        }
                    }
                } else if (isCustom.equalsIgnoreCase("false")) {
                    if (!vaccinefrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select)) && !vaccineFrequencyEt.getText().toString().isEmpty() && vaccineFrequencyEt.getText().toString() != null && !vaccineFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                        param = paramDto;
                        param.setMedicalParameterType("VACCINES");
                        param.setName(param.getName());
                        param.setFrequencyNumber(Double.parseDouble(vaccineFrequencyEt.getText().toString()));
                        param.setFrequencyUnit(vaccinefrequencySp.getSelectedItem().toString().toUpperCase());
                        param.setNextDueDate(checkupDateString);

                        vaccineParametersMap.put("Vaccine Name", param.getName());
                        vaccineParametersMap.put("Frequency Number", param.getFrequencyNumber());
                        vaccineParametersMap.put("Frequency Unit", param.getFrequencyUnit());
                        vaccineParametersMap.put("Next Due Date", param.getNextDueDate());
                        AppUtils.logCleverTapEvent(ManageVitalActivity.this,
                                Constants.ADD_VACCINE_FORM_SUBMITTED, vaccineParametersMap);

                        addParameterAPI(param);
                    } else {
                        if (vaccinefrequencySp.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select))) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency));
                        } else if (vaccineFrequencyEt.getText().toString().isEmpty() || vaccineFrequencyEt.getText().toString() == null) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value));
                        } else if (vaccineFrequencyEt.getText().toString().equalsIgnoreCase("0")) {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value_should_be_greater_than_zero));
                        }
                    }
                } else {
                    AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_option_from_the_list));
                }
            } else {
                AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_vaccine_from_the_list));
            }
        }
    }

    public void addParamAPI(ParamDto param){
        AddParameterNewAPI addparam = new AddParameterNewAPI();
        addparam.parameterId = String.valueOf(param.getId());
        addparam.frequencyUnit = param.getFrequencyUnit();
        addparam.unit = param.getMeasurementUnit();
        addparam.maxValue = String.valueOf(param.getMaxBaselineValue());
        addparam.minValue = String.valueOf(param.getMinBaselineValue());
        addparam.frequencyNumber = String.valueOf(param.getFrequencyValue());
        Log.i("addParamAPIaddParamAPI","is Custom add parameter api = "+new Gson().toJson(param));
        Log.i("addParamAPIaddParamAPI","is Custom add parameter api api = "+new Gson().toJson(addparam));
        Log.i("addParamAPIaddParamAPI","is Custom id = "+sharedPreferences.getString("kymPid", "134388"));



        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.addParameterData(sharedPreferences.getString("kymPid", "134388"),addparam,token).enqueue(new Callback<AddParameterAPIResponse>() {
            @Override
            public void onResponse(Call<AddParameterAPIResponse> call, retrofit2.Response<AddParameterAPIResponse> response) {
                Log.i("addParamAPIaddParamAPI", "api login response 0 code = " + response.code());
                Log.i("addParamAPIaddParamAPI", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.i("addParamAPIaddParamAPI", "api login LoginNewResponse response = " + new Gson().toJson(response.body()));

                    AppUtils.openSnackBar(parentLayout, response.body().message);
                    Intent intent = new Intent(ManageVitalActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
/*
                    adddrug.addAll(response.body());

                    drugDto = null;
                    drugInfoLl.setVisibility(View.GONE);
                    showHideMenuOption(false);
                    if (response.body() != null)
                        updateDrugAdapter(response.body());*/
                } else if(response.code() ==401){
                    refreshToken();
                }else {
                    AppUtils.openSnackBar(parentLayout, "Same think was wrong.");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkitemonclick", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<AddParameterAPIResponse> call, Throwable t) {
                AppUtils.openSnackBar(parentLayout, t.getMessage());
                Log.i("checkitemonclick", "api error message response  = " + t.getMessage());
            }
        });




    }
    public void addParameterAPI(ParamDto param) {
        progress.show();

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getAddParameter(userDto.getId()),
                        APIMessageResponse.class, param,
                        dosageDropDownDtoList -> {
                            AppUtils.hideProgressBar(progress);

                            Log.e("addParameterAPI_log"," : "+new Gson().toJson(dosageDropDownDtoList));

                            Intent intent = new Intent(ManageVitalActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            AppUtils.isDataChanged = true;
                            finish();
                        },
                        error -> {
                            AppUtils.hideProgressBar(progress);
                            authExpiredCallback.hideProgressBar();
                            String res = AppUtils.getVolleyError(ManageVitalActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(parentLayout, res);
                        });
        authExpiredCallback.setRequest(getDiseasRequest);
        ApiService.get().addToRequestQueue(getDiseasRequest);
    }

    public void addCustomParameterAPI(CustomParamDto param) {
        progress.show();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getAddParameter(userDto.getId()),
                        APIMessageResponse.class, param,
                        dosageDropDownDtoList -> {
                            AppUtils.hideProgressBar(progress);
                            Log.e("addCustomParameterAPI_log"," : "+new Gson().toJson(dosageDropDownDtoList));
                            Intent intent = new Intent(ManageVitalActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            AppUtils.isDataChanged = true;
                            finish();
                        },
                        error -> {
                            AppUtils.hideProgressBar(progress);
                            authExpiredCallback.hideProgressBar();
                            String res = AppUtils.getVolleyError(ManageVitalActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(parentLayout, res);
                        });
        authExpiredCallback.setRequest(getDiseasRequest);
        ApiService.get().addToRequestQueue(getDiseasRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progress != null)
            progress.dismiss();
    }
}