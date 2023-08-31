package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.devkraft.karmahealth.Model.ConfigureDto;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.R;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devkraft.karmahealth.Adapter.ConditionsListAdapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddParameterRequest;
import com.devkraft.karmahealth.Model.AddSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.Disease;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterResponseDTO;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.LoginRequest;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.SymptomRequest;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AddConditionsActivity;
import com.devkraft.karmahealth.Screen.AddSymptomsActivity;
import com.devkraft.karmahealth.Screen.ManageVitalActivity;
import com.devkraft.karmahealth.Screen.MyConditionsNewActivity;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class TestConfigureActivity extends BaseActivity {

    private UserDto userDto;
    private Spinner frequencySp;
    private RelativeLayout dateRl;
    private LinearLayout parentLl;
    private LinearLayout frequencyLl;

    private Button saveBtn;
    private TextView headerMsgTv;
    private EditText frequencyEt;
    private TextView selectDateTv;
    private ConfigureDto configureDto;
    private ParameterDto parameterDto;
    private ProgressDialogSetup progress;

    private String backendDate;
    private String fromWhereStr;
    private String fromHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_configure);
        setUpToolbar();
        iniIds();
        getIntentValue(getIntent());
        handleClickEvent();
        AppUtils.logCleverTapEvent(TestConfigureActivity.this,
                Constants.CHECKUP_VACCINE_SCREEN_OPENED, null);
    }

    private void handleClickEvent() {
        frequencyLl.setOnClickListener(view -> frequencySp.performClick());
        dateRl.setOnClickListener(view -> openDateDialogPicker());
        saveBtn.setOnClickListener(view -> handleSaveButtonClick());
    }

    private void handleSaveButtonClick() {
        AppUtils.hideKeyboard(TestConfigureActivity.this);
        String frequencyStr = frequencyEt.getText().toString();
        if (frequencyStr.length() > 0) {
            if(!frequencyStr.equalsIgnoreCase("0")){
                if (!selectDateTv.getText().toString().equalsIgnoreCase(getString(R.string.select_date_track))) {
                    int position =  frequencySp.getSelectedItemPosition();
                    if(position != 0){
                        configureDto.setFrequencyUnit(frequencySp.getSelectedItem().toString().toUpperCase());
                        configureDto.setFrequencyNumber(Double.parseDouble(frequencyStr));
                        configureDto.setNextDueDate(backendDate);
                        callUpdateAPI(configureDto);
                    }else{
                        AppUtils.openSnackBar(parentLl,getString(R.string.please_select_frequency));
                    }
                } else {
                    AppUtils.openSnackBar(parentLl, getString(R.string.plese_select_valid_date));
                }
            }else {
                AppUtils.openSnackBar(parentLl, getString(R.string.please_enter_valid_freuency));
            }
        } else {
            AppUtils.openSnackBar(parentLl, getString(R.string.please_enter_valid_freuency));
        }
    }

    private void callUpdateAPI(ConfigureDto configureDto) {
        if (progress != null) {
            progress.show();
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> updateDiseaseParameter = new GenericRequest<>
                (Request.Method.PUT, APIUrls.get().getConfigurationParameter(userDto.getId(), parameterDto.getUserParameterId()),
                        APIMessageResponse.class, configureDto,
                        apiMessageResponse -> {
                            AppUtils.logEvent(Constants.CNDTN_TEST_SCR_CONFIG_SAVED);
                            AppUtils.logEvent(Constants.CNDTN_VACCINE_SCR_CONFIG_SAVED);
                            AppUtils.hideProgressBar(progress);
                            AppUtils.isDataChanged = true;
                            setActivityResult();
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(TestConfigureActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(parentLl, res);
                        });
        authExpiredCallback.setRequest(updateDiseaseParameter);
        ApiService.get().addToRequestQueue(updateDiseaseParameter);
    }

    private void setActivityResult() {
        Intent intent = new Intent(TestConfigureActivity.this, HomeActivity.class);
        intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE,true);
        intent.putExtra(Constants.FROM_WHERE, fromWhereStr);
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
        intent.putExtra(Constants.FROM_HOME_FRAGMENT,fromHomeFragment);
        startActivity(intent);
        finish();
    }

    private void openDateDialogPicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                (view, year, monthOfYear, dayOfMonth) -> {
                    int month = monthOfYear + 1;
                    String monthStr = month + "";
                    if (monthStr.trim().length() == 1) {
                        monthStr = "0" + monthStr;
                    }

                    String dayStr = dayOfMonth + "";
                    if (dayStr.trim().length() == 1) {
                        dayStr = "0" + dayStr;
                    }

                    selectDateTv.setText(monthStr + "/" + dayStr + "/" + year);
                    backendDate = year + "-" + monthStr + "-" + dayStr;

                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        if (selectDateTv.getText() != null && !selectDateTv.getText().toString().equalsIgnoreCase(getString(R.string.select_date_track))) {
            AppUtils.setSelectedDate(datePickerDialog, selectDateTv.getText().toString().trim());
        }
        datePickerDialog.show();
    }

    private void iniIds() {
        frequencyLl = findViewById(R.id.frequencyLl);
        frequencySp = findViewById(R.id.frequencySp);
        dateRl = findViewById(R.id.dateRl);
        selectDateTv = findViewById(R.id.selectDateTv);
        headerMsgTv = findViewById(R.id.headerMsgTv);
        saveBtn = findViewById(R.id.saveBtn);
        progress = ProgressDialogSetup.getProgressDialog(this);
        parentLl = findViewById(R.id.parentLl);
        frequencyEt = findViewById(R.id.frequencyEt);
    }


    private void getIntentValue(Intent intent) {
        if (intent != null) {

            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            String fromWhichDisease = intent.getStringExtra(Constants.WHICH_TEST);
            String parameterDtoStr = intent.getStringExtra(Constants.PARAMETER_DTO);
            fromWhereStr = intent.getStringExtra(Constants.FROM_WHERE);
            fromHomeFragment = intent.getStringExtra(Constants.FROM_HOME_FRAGMENT);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(fromWhichDisease);
            }


            String msg = getString(R.string.schedule);

            if (fromWhichDisease != null) {
                msg = msg.concat(" " + fromWhichDisease + " ");
                headerMsgTv.setText(msg);
            }

            if (userDtoStr != null && parameterDtoStr != null) {
                Gson gson = new Gson();
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(parameterDtoStr, ParameterDto.class);

                callGetParameterAPI();
            }
        }
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
                                updateUI();
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(TestConfigureActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(parentLl, res);
                            });
            authExpiredCallback.setRequest(getDiseaseParameter);
            ApiService.get().addToRequestQueue(getDiseaseParameter);
        }
    }

    private void updateUI() {
        if (configureDto != null) {
            Double frequencyNumber = configureDto.getFrequencyNumber();
            if(frequencyNumber != null){
                DecimalFormat df = new DecimalFormat("#");
                String number = df.format(frequencyNumber);
                frequencyEt.setText(number);
                frequencyEt.setSelection(number.length());
            }


            String frequencyUnit = configureDto.getFrequencyUnit();
            if (frequencyUnit == null) {
                frequencySp.setSelection(0);
            } else if (frequencyUnit.equalsIgnoreCase("month")) {
                frequencySp.setSelection(2);
            } else if (frequencyUnit.equalsIgnoreCase("year")) {
                frequencySp.setSelection(3);
            }else if(frequencyUnit.equalsIgnoreCase("week")){
                frequencySp.setSelection(1);
            }

            String nextDueDate = configureDto.getNextDueDate();
            if (nextDueDate != null) {
                selectDateTv.setText(AppUtils.getFormattedDate(nextDueDate));
                backendDate = nextDueDate;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setActivityResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(parameterDto != null && parameterDto.getMedicalParameterType() != null && parameterDto.getMedicalParameterType().equalsIgnoreCase(Constants.VACCINES)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.delete_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AppUtils.logEvent(Constants.CNDTN_TEST_SCR_BACK_BTN_CLK);
            AppUtils.logEvent(Constants.CNDTN_VACCINE_SCR_BACK_BTN_CLK);
            setActivityResult();
        } else if (item.getItemId() == R.id.delete) {
            showDialogBox(parameterDto);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogBox(final ParameterDto item) {
        if (userDto != null && item != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.layout_delete_vaccine, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            alertDialogBuilder.setView(promptView);

            TextView title = promptView.findViewById(R.id.title);
            TextView msg = promptView.findViewById(R.id.msg);

            title.setText(this.getString(R.string.you_want_to_remove) + "" + item.getName() + " ?");
/*
            String msgStr = this.getString(R.string.do_you_want_to_delete) + this.getString(R.string.space)
                    + item.getName()  + this.getString(R.string.space) + this.getString(R.string.form_your_profile);
            msg.setText(msgStr);*/

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton(this.getString(R.string.ok), (dialog, id) -> callDeleteParameterAPI(userDto.getId(),item.getUserParameterId(),item));

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton(this.getString(R.string.cancel), (dialog, id) -> dialog.dismiss());

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void callDeleteParameterAPI(Long userId, Long userParameterId,ParameterDto item) {
        if(progress != null){
            progress.show();
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> getDeleteRequest = new GenericRequest<>
                (Request.Method.DELETE, APIUrls.get().getDeleteDiseaseParamaneter(userId, userParameterId),
                        APIMessageResponse.class, null,
                        response ->  {

                            AppUtils.hideProgressBar(progress);
                            AppUtils.isDataChanged = true;
                            ApplicationDB.get().deleteVaccines(userId,item);
                            Intent intent = new Intent(TestConfigureActivity.this, HomeActivity.class);
                            intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
                            intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            finish();
                        },
                        error ->  {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(TestConfigureActivity.this, error, authExpiredCallback);
                            Toast.makeText(TestConfigureActivity.this,res,Toast.LENGTH_SHORT).show();
                        });
        authExpiredCallback.setRequest(getDeleteRequest);
        ApiService.get().addToRequestQueue(getDeleteRequest);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
    }
}