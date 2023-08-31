package com.devkraft.karmahealth.Screen;


import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.RetrfoitUploadModel;
import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.Model.TrackJSONForRequest;
import com.devkraft.karmahealth.R;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class TrackActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private Button saveBtn;
    private View mParentView;
    private UserDto userDto;
    private String backendDate;
    private Spinner measuredSp;
    private boolean isFromHome;
    private boolean isVitalEdit;
    private boolean isFromReport;
    private TextView headerMsgTv;
    private ImageView selectDateIv;
    private String fromWhichDisease;
    private ParameterDto parameterDto;
    private boolean isProgramatically;
    private TimePickerDialog mTimePicker;
    private long userParameterTrackingId;
    private ProgressDialogSetup progress;
    private TextView selectDateTv, selectTimeTv;
    private RelativeLayout dateRl, timeRl, tempRl;
    private TextView diastolicHeader, systolicHeader;
    private LinearLayout systolicLl, diastolicLl, weightLl;
    /*private ConfigureFragment.OnSaveListener onSaveListener;*/
    private TextView diastolicMaxValue, diastolicMinValue, customTrackParamterTv;
    private LinearLayout measuredLl, measuredSpLl, notesLl, customTrackParamterLL;
    private TextView systolicMaxValue, systolicMinValue, weightMaxValue, weightMinValue;
    private androidx.appcompat.widget.SwitchCompat weightSwitchCompat, tempSwitchCompat;
    private EditText systolicValue, diastolicValue, weightValue, addNoteEt, customTrackParameterEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        setupToolbar();
        initializeIds();
        getValueFromArguments(getIntent());
        handleClickEvent();
        AppUtils.hideKeyboard(this);
        /*Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);*/
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.track));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }

    private void initializeIds() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        progress = ProgressDialogSetup.getProgressDialog(this);

        dateRl = findViewById(R.id.dateRl);
        timeRl = findViewById(R.id.timeRl);
        saveBtn = findViewById(R.id.saveBtn);
        weightLl = findViewById(R.id.weightLl);
        weightValue = findViewById(R.id.weightValue);
        headerMsgTv = findViewById(R.id.headerMsgTv);
        selectDateIv = findViewById(R.id.selectDate);
        selectTimeTv = findViewById(R.id.selectTimeTv);
        selectDateTv = findViewById(R.id.selectDateTv);

        tempRl = findViewById(R.id.tempRl);
        notesLl = findViewById(R.id.notesLl);
        addNoteEt = findViewById(R.id.addNoteEt);
        measuredLl = findViewById(R.id.measuredLl);
        measuredSp = findViewById(R.id.measuredSp);
        measuredSpLl = findViewById(R.id.measuredSpLl);
        weightMinValue = findViewById(R.id.weightMinValue);
        weightMaxValue = findViewById(R.id.weightMaxValue);
        tempSwitchCompat = findViewById(R.id.tempSwitchCompat);
        weightSwitchCompat = findViewById(R.id.weightSwitchCompat);

        systolicValue = findViewById(R.id.systolicValue);
        systolicMaxValue = findViewById(R.id.systolicMaxValue);
        systolicMinValue = findViewById(R.id.systolicMinValue);

        systolicLl = findViewById(R.id.systolicLl);
        diastolicLl = findViewById(R.id.diastolicLl);
        systolicHeader = findViewById(R.id.systolicHeader);
        diastolicValue = findViewById(R.id.diastolicValue);
        diastolicHeader = findViewById(R.id.diastolicHeader);
        diastolicMaxValue = findViewById(R.id.diastolicMaxValue);
        diastolicMinValue = findViewById(R.id.diastolicMinValue);
        customTrackParamterLL = findViewById(R.id.customTrackParamterLL);
        customTrackParamterTv = findViewById(R.id.customTrackParameterTv);
        customTrackParameterEt = findViewById(R.id.customTrackParameterEt);

        mParentView = findViewById(R.id.parent_view);
        selectDateTv.setText(AppUtils.getTodayDate());
        backendDate = AppUtils.getTodayDateForSchedule();
        selectTimeTv.setText(AppUtils.getCurrentTimeInString());
        weightValue.setFilters(new InputFilter[]{new TrackActivity.DecimalDigitsInputFilter(5, 1)});

        weightSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        tempSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        weightMinValue.setText(AppUtils.MIN_VALUE + "");
        weightMaxValue.setText(AppUtils.WEIGHT_MAX_VALUE_LBS + "");
        //weightValue.setText(AppUtils.MIN_VALUE + "");

    }

    private void getValueFromArguments(Intent arguments) {
        if (arguments != null) {
            Gson gson = new Gson();
            String userDtoStr = arguments.getStringExtra(Constants.USER_DTO);
            fromWhichDisease = arguments.getStringExtra(Constants.WHICH_DISEASE);
            isVitalEdit = arguments.getBooleanExtra(Constants.IS_VITAL_EDIT, false);
            Log.i("checkincall","bools  =  "+String.valueOf(isVitalEdit));
            userParameterTrackingId = arguments.getLongExtra(Constants.USER_PARA_TRACKING_ID, 0);
            isFromReport = arguments.getBooleanExtra(Constants.IS_FROM_REPORT, false);
            isFromHome = arguments.getBooleanExtra(Constants.IS_FROM_HOME, false);

            String vitalDtoStr = arguments.getStringExtra(Constants.PARAMETER_DTO);

            if (isVitalEdit) {
                saveBtn.setText(getString(R.string.update));
                HashMap<String,Object> helthmatricsName = new HashMap<>();
                helthmatricsName.put(Constants.HEALTH_MATRICS_NAME,fromWhichDisease);
                AppUtils.logCleverTapEvent(this,
                        Constants.HEALTH_METRICS_EDIT_TRACK_SCREEN_OPENED, helthmatricsName);
            } else {
                saveBtn.setText(getString(R.string.save_label));
                HashMap<String,Object> helthmatricsName = new HashMap<>();
                helthmatricsName.put(Constants.HEALTH_MATRICS_NAME,fromWhichDisease);
                AppUtils.logCleverTapEvent(this,
                        Constants.HEALTH_METRICS_TRACK_SCREEN_OPENED, helthmatricsName);
            }

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(vitalDtoStr, ParameterDto.class);
                if (parameterDto != null) {
                    parameterDto.setMaxBaselineValue(parameterDto.getMaxBaselineValue());
                }

                Context context = TrackActivity.this;
                if (context != null) {
                    String msg = context.getString(R.string.track_title);
                    String msg1 = context.getString(R.string.reading);
                    if (fromWhichDisease != null) {

                        if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {
                            msg = msg.concat(" " + getString(R.string.resting_heart_rate) + " ");
                        } else {
                            msg = msg.concat(" " + fromWhichDisease + " ");
                        }
                        headerMsgTv.setText(msg);
                        Log.i("checkStringone","string = "+msg);

                        // setting layout according to different vitals
                        systolicValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {

                            systolicLl.setVisibility(View.VISIBLE);
                            diastolicLl.setVisibility(View.VISIBLE);
                            weightLl.setVisibility(View.GONE);
                            measuredLl.setVisibility(View.GONE);
                            notesLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);

                            systolicHeader.setText(getString(R.string.systolic));
                            diastolicHeader.setText(getString(R.string.diastolic));


                            systolicMinValue.setText(AppUtils.SYSTOLIC_MIN_VALUE + "");
                            systolicMaxValue.setText(AppUtils.SYSTOLIC_MAX_VALUE + "");

                            diastolicMinValue.setText(AppUtils.MIN_VALUE + "");
                            diastolicMaxValue.setText(AppUtils.DIASTOLIC_MAX_VALUE + "");

                            //systolicValue.setText(AppUtils.SYSTOLIC_MIN_VALUE + "");
                            //diastolicValue.setText(AppUtils.MIN_VALUE + "");

                            if (isVitalEdit && parameterDto != null) {
                                if (parameterDto.getMaxBaselineValue() != null &&
                                        parameterDto.getMinBaselineValue() != null) {
                                    systolicValue.setText(AppUtils.getValueWithoutDecimal(parameterDto.getMaxBaselineValue()));
                                    diastolicValue.setText(AppUtils.getValueWithoutDecimal(parameterDto.getMinBaselineValue()));
                                }
                            }
                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_Fasting) ||
                                fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose)) {

                            systolicLl.setVisibility(View.VISIBLE);
                            diastolicLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            measuredLl.setVisibility(View.VISIBLE);
                            notesLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);

                            systolicHeader.setText(getString(R.string.mgDl));


                            systolicMinValue.setText(AppUtils.MIN_VALUE + "");
                            systolicMaxValue.setText(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "");


                            //systolicValue.setText(AppUtils.MIN_VALUE + "");

                            if (isVitalEdit && parameterDto != null) {
                                if (parameterDto.getMaxBaselineValue() != null) {
                                    systolicValue.setText(AppUtils.getValueWithoutDecimal(parameterDto.getMaxBaselineValue()));
                                }
                                String measured = parameterDto.getMeasured();
                                String[] measuredAdapter = getResources().getStringArray(R.array.measured_adapter);
                                if (measured != null) {
                                    int pos = Arrays.asList(measuredAdapter).indexOf(measured);
                                    measuredSp.setSelection(pos);
                                }
                            }

                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {

                            systolicLl.setVisibility(View.VISIBLE);
                            diastolicLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            measuredLl.setVisibility(View.GONE);
                            notesLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);

                            systolicHeader.setText(getString(R.string.bpm));

                            systolicMinValue.setText(AppUtils.MIN_VALUE + "");
                            systolicMaxValue.setText(AppUtils.HEART_RATE_MAX_VALUE + "");

                            //systolicValue.setText(AppUtils.MIN_VALUE + "");

                            if (isVitalEdit && parameterDto != null) {
                                if (parameterDto.getMaxBaselineValue() != null)
                                    systolicValue.setText(AppUtils.getValueWithoutDecimal(parameterDto.getMaxBaselineValue()));
                            }

                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {

                            systolicLl.setVisibility(View.VISIBLE);
                            diastolicLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            measuredLl.setVisibility(View.GONE);
                            notesLl.setVisibility(View.VISIBLE);
                            tempRl.setVisibility(View.GONE);

                            systolicHeader.setText(getString(R.string.breaths_per_min));
                            //systolicValue.setText(AppUtils.MIN_VALUE + "");

                            if (isVitalEdit && parameterDto != null) {
                                if (parameterDto.getMaxBaselineValue() != null) {
                                    systolicValue.setText(AppUtils.getValueWithoutDecimal(parameterDto.getMaxBaselineValue()));
                                }

                                String notes = parameterDto.getNotes();
                                if (notes != null) {
                                    addNoteEt.setText(notes);
                                }

                            }

                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

                            systolicLl.setVisibility(View.VISIBLE);
                            diastolicLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            measuredLl.setVisibility(View.GONE);
                            notesLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.VISIBLE);

                            systolicHeader.setText(getString(R.string.temperature));
                            //systolicValue.setText(AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + "");
                            systolicValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            systolicValue.setFilters(new InputFilter[]{new TrackActivity.DecimalDigitsInputFilter(5, 1)});

                            if (isVitalEdit && parameterDto != null) {
                                if (parameterDto.getMaxBaselineValue() != null) {
                                    systolicValue.setText(AppUtils.getValueWithoutDecimal(parameterDto.getMaxBaselineValue()));
                                }

                                String measurementUnit = parameterDto.getMeasurementUnit();
                                if (measurementUnit != null) {
                                    if (measurementUnit.equalsIgnoreCase(Constants.C)) {
                                        tempSwitchCompat.setChecked(true);
                                    } else {
                                        tempSwitchCompat.setChecked(false);
                                    }
                                }
                            }


                        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {
                            systolicLl.setVisibility(View.GONE);
                            diastolicLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.VISIBLE);
                            measuredLl.setVisibility(View.GONE);
                            notesLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);

                            if (isVitalEdit && parameterDto != null) {
                                Double baselineValue = parameterDto.getMaxBaselineValue();
                                if (parameterDto.getMaxBaselineDisplayName() != null &&
                                        baselineValue != null)
                                    weightValue.setText(String.valueOf(baselineValue));

                                String measurementUnit = parameterDto.getMeasurementUnit();
                                if (measurementUnit != null) {
                                    if (measurementUnit.equalsIgnoreCase(getString(R.string.lbs))) {
                                        weightSwitchCompat.setChecked(false);
                                    } else {
                                        weightSwitchCompat.setChecked(true);
                                    }
                                }
                            }
                        } else {
                            systolicLl.setVisibility(View.GONE);
                            diastolicLl.setVisibility(View.GONE);
                            weightLl.setVisibility(View.GONE);
                            measuredLl.setVisibility(View.GONE);
                            notesLl.setVisibility(View.GONE);
                            tempRl.setVisibility(View.GONE);
                            customTrackParamterLL.setVisibility(View.VISIBLE);

                            String measurementUnit = parameterDto.getMeasurementUnit();

                            if (measurementUnit != null) {
                                customTrackParamterTv.setText(measurementUnit);
                            }

                            Double maxBaselineValue = parameterDto.getMaxBaselineValue();
                            if (maxBaselineValue != null) {
                                customTrackParameterEt.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue));
                            }
                        }
                    }

                    if (isVitalEdit && parameterDto != null) {

                        String lastRecordedDate = parameterDto.getLastRecordedDate();
                        if (lastRecordedDate != null) {
                            if (lastRecordedDate.contains(" ")) {
                                String[] dateTime = lastRecordedDate.split(" ");
                                String date = dateTime[0];
                                String time = dateTime[1];

                                selectDateTv.setText(AppUtils.change_dd_mm_yyyy_to_mm_dd_yyyy(date));
                                backendDate = AppUtils.getBackendFormattedDateForTrack(date);
                                selectTimeTv.setText(time);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleClickEvent() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_TRACK_TAB_SAVE_BTN_CLK);

                TrackConfigurationDto trackConfigurationDto = new TrackConfigurationDto();
                AppUtils.hideKeyboard(TrackActivity.this);
                if (!selectDateTv.getText().toString().equalsIgnoreCase(getString(R.string.select_date_track))
                        && selectDateTv.getText().toString().length() > 0) {
                    if (!selectTimeTv.getText().toString().equalsIgnoreCase(getString(R.string.select_time))
                            && selectTimeTv.getText().toString().length() > 0) {
                        Calendar c = Calendar.getInstance();
                        c.getTime();
                        if(selectTimeTv.getText().toString().equalsIgnoreCase(c.getTime().toString())) {
                            trackConfigurationDto.setRecordedDate(backendDate + " " + selectTimeTv.getText().toString());

                            // Checking if user entered values are in the given ranges

                            checkIfUserEnteredValues(trackConfigurationDto);
                        } else {
                            trackConfigurationDto.setRecordedDate(backendDate + " " + selectTimeTv.getText().toString());

                            // Checking if user entered values are in the given ranges

                            checkIfUserEnteredValues(trackConfigurationDto);
//                            AppUtils.openSnackBar(mParentView, getString(R.string.your_new_value_overwrite));
                        }

                    } else {
                        AppUtils.openSnackBar(mParentView, getString(R.string.plese_select_valid_time));
                    }
                } else {
                    AppUtils.openSnackBar(mParentView, getString(R.string.plese_select_valid_date));
                }
            }
        });


        tempSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // celsius
                    systolicValue.setText(AppUtils.MIN_VALUE_TEMP_CELSIUS + "");
                    //systolicValue.setText(AppUtils.getCelsiusValueFromFahrenheit(systolicValue.getText().toString()));
                } else {
                    //  fahrenheit
                    systolicValue.setText(AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + "");
                    //systolicValue.setText(AppUtils.getFahrenheitValueFromCelsius(systolicValue.getText().toString()));
                }
            }
        });


        weightSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // kgs
                    AppUtils.logEvent(Constants.CNDTN_WEIGHT_MODE_KGS_SLCT);
                    weightMinValue.setText(AppUtils.MIN_VALUE + "");
                    weightMaxValue.setText(AppUtils.WEIGHT_MAX_VALUE_KG + "");
                    weightValue.setText(AppUtils.MIN_VALUE + "");
                    //weightValue.setText(AppUtils.getKgsValueFromLbs(weightValue.getText().toString()));

                } else {
                    // lbs
                    AppUtils.logEvent(Constants.CNDTN_HEIGHT_MODE_LBS_SLCT);
                    weightMinValue.setText(AppUtils.MIN_VALUE + "");
                    weightMaxValue.setText(AppUtils.WEIGHT_MAX_VALUE_LBS + "");
                    weightValue.setText(AppUtils.MIN_VALUE + "");
                    //weightValue.setText(AppUtils.getLbsValueFromKgs(weightValue.getText().toString()));
                }
            }
        });

        weightValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    updateWeightValue();
                }
            }
        });


        weightValue.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (event == null || !event.isShiftPressed()) {
                                updateWeightValue();
                                AppUtils.hideKeyboard(TrackActivity.this);
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );


        systolicValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    updateSysValue();
                }
            }
        });

        systolicValue.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (event == null || !event.isShiftPressed()) {
                                updateSysValue();
                                AppUtils.hideKeyboard(TrackActivity.this);
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );


        diastolicValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    updateDiaValue();
                }
            }
        });

        diastolicValue.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (event == null || !event.isShiftPressed()) {
                                updateDiaValue();
                               /* Editable sysValue = diastolicValue.getText();
                                if (sysValue != null) {
                                    String sysValueStr = sysValue.toString();
                                    if(sysValueStr.length() > 0)
                                    diastolicSeekBar.setProgress(Float.parseFloat(sysValueStr));
                                }*/
                                AppUtils.hideKeyboard(TrackActivity.this);
                                return true;
                            }
                        }
                        return false;
                    }
                }
        );


        timeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        dateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialogPicker();
            }
        });

        selectDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialogPicker();
            }
        });

        selectDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialogPicker();
            }
        });

        measuredLl.setOnClickListener(v -> measuredSp.performClick());

    }

    public class DecimalDigitsInputFilter implements InputFilter {
        int digitsBeforeZero = 0;
        int digitsAfterZero = 0;

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

    private void updateWeightValue() {
        Editable sysValue = weightValue.getText();
        if (sysValue != null) {
            String sysValueStr = sysValue.toString();
            if (sysValueStr.length() > 0) {

            } else {
                weightValue.setText("");
                //AppUtils.openSnackBar(getView(), getString(R.string.enter_valid_value));
            }
        }
    }

    private void updateDiaValue() {
        Editable sysValue = diastolicValue.getText();
        if (sysValue != null) {
            String sysValueStr = sysValue.toString();
            if (sysValueStr.length() > 0) {

            } else {
                diastolicValue.setText("");
                //AppUtils.openSnackBar(getView(), getString(R.string.enter_valid_value));
            }
        }
    }

    private void updateSysValue() {
        Editable sysValue = systolicValue.getText();
        if (sysValue != null) {
            String sysValueStr = sysValue.toString();
            if (sysValueStr.length() > 0) {

            } else {
                //AppUtils.openSnackBar(getView(), getString(R.string.enter_valid_value));
            }
        }
    }

    private void checkIfUserEnteredValues(TrackConfigurationDto trackConfigurationDto) {
        String systolicValueStr = systolicValue.getText().toString();
        String diastolicValueStr = diastolicValue.getText().toString();
        String customTrackParameterStr = customTrackParameterEt.getText().toString();
        Context context = TrackActivity.this;

        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {

            String rangeSys = "";
            String rangeDia = "";
            if (context != null) {
                rangeSys = context.getString(R.string.enter_systolic_value) + context.getString(R.string.space) + context.getString(R.string.between) + context.getString(R.string.space)
                        + AppUtils.SYSTOLIC_MIN_VALUE + " - " + AppUtils.SYSTOLIC_MAX_VALUE;

                rangeDia = context.getString(R.string.enter_diastolic_value) + context.getString(R.string.space) + context.getString(R.string.between) + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.DIASTOLIC_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0 && diastolicValueStr.length() > 0) {

                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.SYSTOLIC_MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.SYSTOLIC_MAX_VALUE + "")) {

                    if (Double.parseDouble(diastolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                            && Double.parseDouble(diastolicValueStr) <= Double.parseDouble(AppUtils.DIASTOLIC_MAX_VALUE + "")) {

                        proceedToUpdateData(trackConfigurationDto);

                    } else {
                        AppUtils.openSnackBar(mParentView, rangeDia);
                    }

                } else {
                    AppUtils.openSnackBar(mParentView, rangeSys);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_PP)) {

            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_blood_sugar_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "")) {

                    proceedToUpdateData(trackConfigurationDto);
                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose)) {

            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_blood_glucose_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.BLOOD_SUGAR_FASTING_MAX_VALUE + "")) {
                    String measuredStr = measuredSp.getSelectedItem().toString();
                    if (!measuredStr.equalsIgnoreCase(getString(R.string.select))) {
                        trackConfigurationDto.setMeasured(measuredStr);
                        trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                        proceedToUpdateData(trackConfigurationDto);
                    } else {
                        AppUtils.openSnackBar(mParentView, getString(R.string.select_measured));
                    }
                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {

            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_heart_rate_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.MIN_VALUE + " - " + AppUtils.HEART_RATE_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.HEART_RATE_MAX_VALUE + "")) {
                    trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                    proceedToUpdateData(trackConfigurationDto);

                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {

            String range = "";
            if (context != null) {
                range = context.getString(R.string.enter_respiratory_rate_value) + context.getString(R.string.space) + context.getString(R.string.between)
                        + context.getString(R.string.space)
                        + AppUtils.RR_MIN_VALUE + " - " + AppUtils.RR_MAX_VALUE;
            }

            if (systolicValueStr.length() > 0) {
                if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.RR_MIN_VALUE + "")
                        && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.RR_MAX_VALUE + "")) {

                    String noteStr = addNoteEt.getText().toString();
                    if (noteStr.length() > 0) {
                        trackConfigurationDto.setNotes(noteStr);
                    }
                    trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                    proceedToUpdateData(trackConfigurationDto);
                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

            if (tempSwitchCompat.isChecked()) {
                // celsius

                String range = "";
                if (context != null) {
                    range = context.getString(R.string.enter_temperature_value) + context.getString(R.string.space) + context.getString(R.string.between)
                            + context.getString(R.string.space)
                            + AppUtils.MIN_VALUE_TEMP_CELSIUS + " - " + AppUtils.MAX_VALUE_TEMP_CELSIUS;
                }

                if (systolicValueStr.length() > 0) {
                    if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE_TEMP_CELSIUS + "")
                            && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.MAX_VALUE_TEMP_CELSIUS + "")) {


                        trackConfigurationDto.setMeasurementUnit(Constants.C);
                        trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                        proceedToUpdateData(trackConfigurationDto);
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

                if (systolicValueStr.length() > 0) {
                    if (Double.parseDouble(systolicValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE_TEMP_FAHRENHEIT + "")
                            && Double.parseDouble(systolicValueStr) <= Double.parseDouble(AppUtils.MAX_VALUE_TEMP_FAHRENHEIT + "")) {


                        trackConfigurationDto.setMeasurementUnit(Constants.F);
                        trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                        proceedToUpdateData(trackConfigurationDto);
                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }
                } else {
                    AppUtils.openSnackBar(mParentView, range);
                }

            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {

            String weightValueStr = weightValue.getText().toString();
            if (weightValueStr.length() > 0 && !weightValueStr.equalsIgnoreCase("0")) {
                if (weightSwitchCompat.isChecked()) {
                    // kgs

                    String range = "";
                    if (context != null) {
                        range = context.getString(R.string.enter_weight_value) + context.getString(R.string.space) + context.getString(R.string.between)
                                + context.getString(R.string.space)
                                + AppUtils.MIN_VALUE + " - " + AppUtils.WEIGHT_MAX_VALUE_KG;
                    }

                    if (Double.parseDouble(weightValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                            && Double.parseDouble(weightValueStr) <= Double.parseDouble(AppUtils.WEIGHT_MAX_VALUE_KG + "")) {

                        proceedToUpdateData(trackConfigurationDto);

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }

                } else {
                    // lbs

                    String range = "";
                    if (context != null) {
                        range = context.getString(R.string.enter_weight_value) + context.getString(R.string.space) + context.getString(R.string.between)
                                + context.getString(R.string.space)
                                + AppUtils.MIN_VALUE + " - " + AppUtils.WEIGHT_MAX_VALUE_LBS;
                    }

                    if (Double.parseDouble(weightValueStr) >= Double.parseDouble(AppUtils.MIN_VALUE + "")
                            && Double.parseDouble(weightValueStr) <= Double.parseDouble(AppUtils.WEIGHT_MAX_VALUE_LBS + "")) {

                        proceedToUpdateData(trackConfigurationDto);

                    } else {
                        AppUtils.openSnackBar(mParentView, range);
                    }
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Pulse_Oximeter)) {
            if (!customTrackParameterEt.getText().toString().isEmpty()) {
                Double oxygen_percent = Double.parseDouble(customTrackParameterStr);
                if (customTrackParameterStr.length() > 0 && !customTrackParameterStr.equalsIgnoreCase("0")) {
                    if (oxygen_percent <= 100) {
                        trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                        proceedToUpdateData(trackConfigurationDto);
                    } else {
                        AppUtils.openSnackBar(mParentView, getString(R.string.value_should_not_more_than_100));
                    }
                } else {
                    AppUtils.openSnackBar(mParentView, getString(R.string.enter_value_in_given_range));
                }
            }
        } else {
            if (!customTrackParameterEt.getText().toString().isEmpty()) {
                if (customTrackParameterStr.length() > 0 && !customTrackParameterStr.equalsIgnoreCase("0")) {
                    trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                    proceedToUpdateData(trackConfigurationDto);
                } else {
                    AppUtils.openSnackBar(mParentView, getString(R.string.enter_track_value_greater_than_zero));
                }
            }
        }
    }

    private void proceedToUpdateData(TrackConfigurationDto trackConfigurationDto) {
        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {

            trackConfigurationDto.setMaxBaselineValue(Double.parseDouble(systolicValue.getText().toString()));
            trackConfigurationDto.setMinBaselineValue(Double.parseDouble(diastolicValue.getText().toString()));

            trackConfigurationDto.setMeasurementUnit(Constants.MMHG);

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_Fasting) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

            if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {
                trackConfigurationDto.setMeasurementUnit(Constants.BPM);
            } else if (fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {
                trackConfigurationDto.setMeasurementUnit(Constants.Breaths_per_min);
            } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {
                if (tempSwitchCompat.isChecked()) {
                    // cel
                    trackConfigurationDto.setMeasurementUnit(Constants.C);
                } else {
                    // fern
                    trackConfigurationDto.setMeasurementUnit(Constants.F);
                }
            } else {
                trackConfigurationDto.setMeasurementUnit(Constants.MGDL);
            }


            trackConfigurationDto.setMaxBaselineValue(Double.parseDouble(systolicValue.getText().toString()));

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {

            if (weightSwitchCompat.isChecked()) {
                // kgs
                trackConfigurationDto.setMeasurementUnit(Constants.KGS);
            } else {
                // lbs
                trackConfigurationDto.setMeasurementUnit(Constants.LBS);
            }
            trackConfigurationDto.setMaxBaselineValue(Double.parseDouble(weightValue.getText().toString()));
            trackConfigurationDto.setMaxBaselineDisplayName(Constants.Weight);
        } else {
            trackConfigurationDto.setMaxBaselineValue(Double.parseDouble(customTrackParameterEt.getText().toString()));
            trackConfigurationDto.setMeasurementUnit(parameterDto.getMeasurementUnit());
//            trackConfigurationDto.setTaken(true);
        }

        trackConfigurationDto.setTaken(true);

       /* if (isVitalEdit) {
            callEditAPI(trackConfigurationDto);
        } else {*/
         //   callTrackAPI(trackConfigurationDto);
        callTrackAPINew(trackConfigurationDto);
        /*}*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progress != null)
            progress.dismiss();
    }

    private void callEditAPI(TrackConfigurationDto trackConfigurationDto) {
        if (userDto != null && parameterDto != null) {

            HashMap<String,Object> trackMap = new HashMap<>();
            trackMap.put(Constants.HEALTH_MATRICS_NAME,fromWhichDisease);
            trackMap.put(Constants.DATE,AppUtils.getTodayDate());
            trackMap.put(Constants.TIME,selectTimeTv.getText().toString());
            trackMap.put(Constants.UNIT,trackConfigurationDto.getMeasurementUnit());
            trackMap.put(Constants.VALUE,"Min = " +trackConfigurationDto.getMinBaselineValue() + " , Max = " +trackConfigurationDto.getMaxBaselineValue());

            AppUtils.logCleverTapEvent(TrackActivity.this,
                    Constants.HEALTH_METRICS_TRACK_READING_UPDATED_ON_TRACK_SCREEN, trackMap);

            if (progress != null) {
                progress.show();
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<APIMessageResponse> updateDiseaseParameter = new GenericRequest<APIMessageResponse>
                    (Request.Method.PUT, APIUrls.get().getEditParameterTrack(userDto.getId(), userParameterTrackingId),
                            APIMessageResponse.class, trackConfigurationDto,
                            new Response.Listener<APIMessageResponse>() {
                                @Override
                                public void onResponse(APIMessageResponse apiMessageResponse) {
                                    AppUtils.hideProgressBar(progress);
                                    if (isVitalEdit) {
                                        if (isFromReport) {
                                            Intent intent = new Intent(TrackActivity.this, TrackerDetailActivity.class);
                                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                            intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
                                            intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
                                            intent.putExtra(Constants.IS_VITAL_EDIT, true);
                                            intent.putExtra(Constants.USER_PARA_TRACKING_ID, userParameterTrackingId);
                                            intent.putExtra(Constants.IS_FROM_REPORT, isFromReport);
                                            finish();
                                        } else {
                                            AppUtils.isDataChanged = true;
                                            Intent intent = new Intent(TrackActivity.this, HomeActivity.class);
                                            /*intent.putExtra(Constants.DATE, backendDate);
                                            intent.putExtra(Constants.USER_DTO_TRACK, Constants.GSON.toJson(userDto));
                                            intent.putExtra(Constants.IS_VITAL_EDIT, true);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
                                            startActivity(intent);
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    authExpiredCallback.hideProgressBar();
                                    AppUtils.hideProgressBar(progress);
                                    String res = AppUtils.getVolleyError(TrackActivity.this, error, authExpiredCallback);
                                    AppUtils.openSnackBar(mParentView, res);
                                }
                            });
            authExpiredCallback.setRequest(updateDiseaseParameter);
            ApiService.get().addToRequestQueue(updateDiseaseParameter);
        }
    }

    private void callTrackAPINew(TrackConfigurationDto trackConfigurationDto){
        if (userDto != null && parameterDto != null) {
            AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_TRACK_SAVED);

            HashMap<String,Object> trackMap = new HashMap<>();
            trackMap.put(Constants.HEALTH_MATRICS_NAME,fromWhichDisease);
            trackMap.put(Constants.DATE,AppUtils.getTodayDate());
            trackMap.put(Constants.TIME,selectTimeTv.getText().toString());
            trackMap.put(Constants.UNIT,trackConfigurationDto.getMeasurementUnit());
            trackMap.put(Constants.VALUE,"Min = " +trackConfigurationDto.getMinBaselineValue() + " , Max = " +trackConfigurationDto.getMaxBaselineValue());

            AppUtils.logCleverTapEvent(TrackActivity.this,
                    Constants.HEALTH_METRICS_TRACK_SCREEN_FORM_SUBMITTED, trackMap);
            if (progress != null) {
                progress.show();
            }


            UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
            Log.i("checkincallcallTrackAPINew", "upload data  api request kym id = " + sharedPreferences.getString("kymPid", "134388"));
            Log.i("checkincallcallTrackAPINew", "upload data  api request model = " + new Gson().toJson(trackConfigurationDto));

            TrackJSONForRequest ndata = new TrackJSONForRequest();
            if(trackConfigurationDto.minBaselineValue !=null){
                ndata.minBaselineValue = trackConfigurationDto.minBaselineValue.toString();
            }else {
                ndata.minBaselineValue = "0.0";
            }
            if(trackConfigurationDto.maxBaselineValue !=null){
                ndata.maxBaselineValue = trackConfigurationDto.maxBaselineValue.toString();
            }else {
                ndata.maxBaselineValue ="0.0";
            }



            if(trackConfigurationDto.minBaselineDisplayName !=null){
                ndata.minBaselineDisplayName = trackConfigurationDto.minBaselineDisplayName;
            }else {
                ndata.minBaselineDisplayName = "";
            }
            if(trackConfigurationDto.maxBaselineDisplayName !=null){
                ndata.maxBaselineDisplayName = trackConfigurationDto.maxBaselineDisplayName;
            }else {
                ndata.maxBaselineDisplayName = "";
            }

            if(trackConfigurationDto.measurementUnit !=null){
                ndata.measurementUnit = trackConfigurationDto.measurementUnit;
            }else {
                ndata.measurementUnit = "";
            }
            if(trackConfigurationDto.recordedDate !=null){
                ndata.recordedDate = trackConfigurationDto.recordedDate;
            }else {
                ndata.recordedDate = "";
            }


            if(trackConfigurationDto.measured !=null){
                ndata.measured = trackConfigurationDto.measured;
            }else {
                ndata.measured = "";
            }

            if(trackConfigurationDto.userParameterTrackingId !=null){
                ndata.userParameterTrackingId = trackConfigurationDto.userParameterTrackingId.toString();
            }else {
                ndata.userParameterTrackingId = "0";
            }


            if(trackConfigurationDto.notes !=null){
                ndata.notes = trackConfigurationDto.notes;
            }else {
                ndata.notes = "";
            }

            if(trackConfigurationDto.source !=null){
                ndata.source = trackConfigurationDto.source;
            }else {
                ndata.source = "";
            }

            if(trackConfigurationDto.taken !=null){
                ndata.taken = trackConfigurationDto.taken.toString();
            }else {
                ndata.taken = "true";
            }

            if(trackConfigurationDto.sheduledDate !=null){
                ndata.sheduledDate = trackConfigurationDto.sheduledDate.toString();
            }else {
                ndata.sheduledDate = null;
            }
            String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
            String parameterid = parameterDto.getId().toString();
            Log.i("checkincallcallTrackAPINew", "upload data  api ndata model = " +new Gson().toJson(ndata));
            Log.i("checkincallcallTrackAPINew", "upload data  api request parameter id = " + Long.toString(parameterDto.getId()));
            Log.i("checkincallcallTrackAPINew", "upload data  api request parameter url = " + service.parameterTrack(sharedPreferences.getString("kymPid", "134388"),
                    parameterid,trackConfigurationDto,token).request().url().toString());

            service.parameterTrack(sharedPreferences.getString("kymPid", "134388"),
                    parameterid,
                    trackConfigurationDto,
                    token).enqueue(new Callback<APIMessageResponse>() {
                @Override
                public void onResponse(Call<APIMessageResponse> call, retrofit2.Response<APIMessageResponse> response) {
                    Log.i("checkincallcallTrackAPINew", "upload data  api response 0 code = " + response.code());
                    Log.i("checkincallcallTrackAPINew", "upload data  api response 0 body = " + new Gson().toJson(response.errorBody()));
                    if (response.isSuccessful()) {
                        Log.i("checkincallcallTrackAPINew", "upload data  api response = " + response.body().getMessage());
                        Log.i("checkincallcallTrackAPINew", "upload data api response = " + new Gson().toJson(response.body()));
                        AppUtils.hideProgressBar(progress);
                        AppUtils.isDataChanged = true;
                        Intent intent = new Intent(TrackActivity.this, MyConditionsNewActivity.class);
                        startActivity(intent);
                        finish();

                    } else if(response.code() == 401){
                        refreshToken();
                    }else {
                        AppUtils.hideProgressBar(progress);
                      //  AppUtils.openSnackBar(mParentView, "Same think was wrong.");
                    }
                }

                @Override
                public void onFailure(Call<APIMessageResponse> call, Throwable t) {
                    Log.i("checkincallcallTrackAPINew", "api error message response  = " + t.getMessage());

                }
            });
        }
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
                    Intent intent = new Intent(TrackActivity.this, LoginActivity.class);
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
    private void callTrackAPI(TrackConfigurationDto trackConfigurationDto) {

        if (userDto != null && parameterDto != null) {
            AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_TRACK_SAVED);

            HashMap<String,Object> trackMap = new HashMap<>();
            trackMap.put(Constants.HEALTH_MATRICS_NAME,fromWhichDisease);
            trackMap.put(Constants.DATE,AppUtils.getTodayDate());
            trackMap.put(Constants.TIME,selectTimeTv.getText().toString());
            trackMap.put(Constants.UNIT,trackConfigurationDto.getMeasurementUnit());
            trackMap.put(Constants.VALUE,"Min = " +trackConfigurationDto.getMinBaselineValue() + " , Max = " +trackConfigurationDto.getMaxBaselineValue());

            AppUtils.logCleverTapEvent(TrackActivity.this,
                    Constants.HEALTH_METRICS_TRACK_SCREEN_FORM_SUBMITTED, trackMap);

            if (progress != null) {
                progress.show();
            }
            Log.i("checkincall","url  =  "+String.valueOf(APIUrls.get().getConfigurationParameterTrack(userDto.getId(), parameterDto.getUserParameterId())));
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<APIMessageResponse> editTrackParameter = new GenericRequest<APIMessageResponse>
                    (Request.Method.POST, APIUrls.get().getConfigurationParameterTrack(userDto.getId(), parameterDto.getUserParameterId()),
                            APIMessageResponse.class, trackConfigurationDto,
                            new Response.Listener<APIMessageResponse>() {
                                @Override
                                public void onResponse(APIMessageResponse apiMessageResponse) {
                                    AppUtils.hideProgressBar(progress);
                                    AppUtils.isDataChanged = true;
                                    if(isFromHome) {
                                        Intent intent = new Intent(TrackActivity.this, HomeActivity.class);
                                        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                        intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
                                        intent.putExtra(Constants.FROM_HOME_FRAGMENT, Constants.FROM_HOME_FRAGMENT);
                                        startActivity(intent);
                                        AppUtils.isDataChanged = true;
                                        finish();
                                    } else {
                                        Intent intent = new Intent(TrackActivity.this, TrackerDetailActivity.class);
                                        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                        intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
                                        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
                                        intent.putExtra(Constants.IS_VITAL_EDIT, isVitalEdit);
                                        intent.putExtra(Constants.USER_PARA_TRACKING_ID, userParameterTrackingId);
                                        intent.putExtra(Constants.IS_FROM_REPORT, isFromReport);
                                        startActivity(intent);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    authExpiredCallback.hideProgressBar();
                                    AppUtils.hideProgressBar(progress);
                                    String res = AppUtils.getVolleyError(TrackActivity.this, error, authExpiredCallback);
                                    AppUtils.openSnackBar(mParentView, res);
                                }
                            });
            authExpiredCallback.setRequest(editTrackParameter);
            ApiService.get().addToRequestQueue(editTrackParameter);
        }
    }

    private boolean isNeedToNonEdit() {
        if (isVitalEdit) {
            if (parameterDto != null) {
                if (parameterDto.getLastRecordedDate() == null) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    private void showTimePicker() {

        if (!isNeedToNonEdit()) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            mTimePicker = new TimePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, (timePicker, selectedHour, selectedMinute) -> {
                Calendar c = Calendar.getInstance();
                if((selectedHour <= (c.get(Calendar.HOUR_OF_DAY)))&&
                        (minute <= (c.get(Calendar.MINUTE)))){
                    selectTimeTv.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
//                    AppUtils.openSnackBar(mParentView,"Your new value may be overwritten the old value");
                }else {
//                    AppUtils.openSnackBar(mParentView,"you can not select future time");
                }

            }, hour, minute, true);

            Context context = TrackActivity.this;
            if (mTimePicker != null) {
                mTimePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, context.getString(R.string.ok), mTimePicker);
                mTimePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
                mTimePicker.show();
            }
        }
    }

    private void openDateDialogPicker() {

        if (!isNeedToNonEdit()) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            //selectDateTv.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

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

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            AppUtils.setDialogBoxButton(this, datePickerDialog);
            if (selectDateTv.getText() != null && !selectDateTv.getText().toString().equalsIgnoreCase(getString(R.string.select_date_track))) {
                AppUtils.setSelectedDate(datePickerDialog, selectDateTv.getText().toString().trim());
            }
            datePickerDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

