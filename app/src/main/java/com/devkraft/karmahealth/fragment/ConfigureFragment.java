package com.devkraft.karmahealth.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.MonthlySpinnerAdapter;
import com.devkraft.karmahealth.Adapter.WeeklySpinnerAdapter;
import com.devkraft.karmahealth.Model.ConfigureDto;
import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.Ranges;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.WeeklyDays;
import com.devkraft.karmahealth.Model.Weights;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.TrackerDetailActivity;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.fragment.ConfigureFragment;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.google.gson.Gson;

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
public class ConfigureFragment extends Fragment {

    private boolean fragmentResume = false, fragmentVisible = false, fragmentOnCreated = false;
    private View parentView;
    private UserDto userDto;
    private String fromWhichDisease;
    private EditText systolicValue, diastolicValue, mgDlValue, customConfiguredParameterEt, minConfiguredParameterEt;
    private TextView systolicMaxValue, systolicMinValue, diastolicMaxValue, diastolicMinValue, headerMsgTv, mgDlMaxValue, mgDlMinValue,
            diastolicHeader, systolicHeader, underTv, normalTv, overTv, obeseTv, extremelyTv, weighMinValue, weighMaxValue, customConfiguredParameterTv;
    private Spinner frequencySp, daySp, monthlySp, cmsSp, ftSp, incsSp;
    private LinearLayout frequencySpLl, daySpLl, weeklyLl, monthlyLl, bloodPressureLl, bloodPressurePpLl,customConfiguredParameterLL, weightLl,
            ftIncsLl, ftLl, inclSpLl, cmParentLl;
    private Button saveBtn;
    private List<WeeklyDays> weeklyDaysList;
    private List<MonthlyDays> monthlyDaysList;
    private OnSaveListener onSaveListener;
    private SwitchCompat heightSwitchCompat, weightSwitchCompat, tempSwitchCompat;
    private ParameterDto vitalDto;
    private ProgressDialogSetup progress;
    private ConfigureDto configureDto;
    private List<String> cmsString;
    private String[] ftArray;
    private String[] incArray;
    private boolean userIsInteracting = false;
    private RelativeLayout tempRl;


    private void intIds() {

        if (getContext() != null) {
            progress = ProgressDialogSetup.getProgressDialog(getContext());
        }
        tempSwitchCompat = parentView.findViewById(R.id.tempSwitchCompat);
        tempRl = parentView.findViewById(R.id.tempRl);
        normalTv = parentView.findViewById(R.id.normalTv);
        underTv = parentView.findViewById(R.id.underTv);
        overTv = parentView.findViewById(R.id.overTv);
        obeseTv = parentView.findViewById(R.id.obeseTv);
        extremelyTv = parentView.findViewById(R.id.extremelyTv);
        weighMinValue = parentView.findViewById(R.id.weighMinValue);
        weighMaxValue = parentView.findViewById(R.id.weighMaxValue);

        systolicValue = parentView.findViewById(R.id.systolicValue);
        systolicMaxValue = parentView.findViewById(R.id.systolicMaxValue);
        systolicMinValue = parentView.findViewById(R.id.systolicMinValue);

        diastolicValue = parentView.findViewById(R.id.diastolicValue);
        diastolicMaxValue = parentView.findViewById(R.id.diastolicMaxValue);
        diastolicMinValue = parentView.findViewById(R.id.diastolicMinValue);
        frequencySp = parentView.findViewById(R.id.frequencySp);
        monthlySp = parentView.findViewById(R.id.monthlySp);
        daySp = parentView.findViewById(R.id.daySp);
        frequencySpLl = parentView.findViewById(R.id.frequencySpLl);
        daySpLl = parentView.findViewById(R.id.daySpLl);

        weeklyLl = parentView.findViewById(R.id.weeklyLl);
        monthlyLl = parentView.findViewById(R.id.monthlyLl);
        saveBtn = parentView.findViewById(R.id.saveBtn);
        headerMsgTv = parentView.findViewById(R.id.headerMsgTv);
        bloodPressureLl = parentView.findViewById(R.id.bloodPressureLl);

        bloodPressurePpLl = parentView.findViewById(R.id.bloodPressurePpLl);
        mgDlValue = parentView.findViewById(R.id.mgDlValue);

        mgDlMaxValue = parentView.findViewById(R.id.mgDlMaxValue);
        mgDlMinValue = parentView.findViewById(R.id.mgDlMinValue);

        diastolicHeader = parentView.findViewById(R.id.diastolicHeader);
        systolicHeader = parentView.findViewById(R.id.systolicHeader);
        weightLl = parentView.findViewById(R.id.weightLl);

        ftIncsLl = parentView.findViewById(R.id.ftIncsLl);
        ftLl = parentView.findViewById(R.id.ftLl);
        inclSpLl = parentView.findViewById(R.id.inclSpLl);
        cmParentLl = parentView.findViewById(R.id.cmParentLl);


        cmsSp = parentView.findViewById(R.id.cmsSp);
        ftSp = parentView.findViewById(R.id.ftSp);
        incsSp = parentView.findViewById(R.id.incsSp);

        heightSwitchCompat = parentView.findViewById(R.id.heightSwitchCompat);
        weightSwitchCompat = parentView.findViewById(R.id.weightSwitchCompat);

        customConfiguredParameterLL = parentView.findViewById(R.id.customConfiguredParameterLL);
        customConfiguredParameterTv = parentView.findViewById(R.id.customConfiguredParameterTv);
        customConfiguredParameterEt = parentView.findViewById(R.id.customConfiguredParameterEt);
        minConfiguredParameterEt = parentView.findViewById(R.id.minConfiguredParameterEt);

        heightSwitchCompat.setChecked(false);
        weightSwitchCompat.setChecked(false);


        if (getContext() != null) {
            heightSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            weightSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            tempSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);

            setFtSpValues();
            setCmsSpValues();
        }
    }

    private void setFtSpValues() {
        ftArray = getResources().getStringArray(R.array.ft_adapter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_dropdown_disease, ftArray);
        ftSp.setAdapter(dataAdapter);
    }

    private void setCmsSpValues() {
        cmsString = new ArrayList<>();
        for (int i = 147; i <= 203; i++) {
            cmsString.add(i + "");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_dropdown_disease, cmsString);
        cmsSp.setAdapter(adapter);
    }


    public interface OnSaveListener {
        void onSaveClicked(String tab);
    }
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.configure_layout, null);
        if (!fragmentResume && fragmentVisible) { //only when first time fragment is created
            setUp();
        }
        return parentView;
    }

    private void setUp() {
        intIds();
        getValueFromArguments(getArguments());
        handleClickEvent();
        AppUtils.hideKeyboard(getActivity());
        AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_CONFIG_TAB_OPENED);
        AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_CONFIGURE_TAB_CLK);
        AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_CONFIGURE_TAB_CLK);

        if (getActivity() != null) {
//            ((DiseaseConfigureActivity) getActivity()).setUserInteractionListener(() -> userIsInteracting = true);
            ((TrackerDetailActivity) getActivity()).setUserInteractionListener(() -> userIsInteracting = true);
        }
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            onSaveListener = (OnSaveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSaveListener");
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleClickEvent() {

        monthlyLl.setOnClickListener(view -> monthlySp.performClick());

        daySpLl.setOnClickListener(view -> daySp.performClick());

        saveBtn.setOnClickListener(view -> {
            if (getContext() != null && userDto != null && vitalDto != null) {
                AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_CONFIG_TAB_SAVE_BTN_CLK);

                ConfigureDto reuestConfigureDto = new ConfigureDto();
                int frequencyPos = frequencySp.getSelectedItemPosition();
                if (frequencyPos != 0) {

                    if (frequencyPos == 1) {
                        reuestConfigureDto.setFrequencyUnit(Constants.DAY);
                    } else if (frequencyPos == 2) {
                        reuestConfigureDto.setFrequencyUnit(Constants.WEEK);
                        int pos = daySp.getSelectedItemPosition();
                        reuestConfigureDto.setFrequencyValue(weeklyDaysList.get(pos).getFullName().toUpperCase());
                    } else if (frequencyPos == 3) {
                        reuestConfigureDto.setFrequencyUnit(Constants.MONTH);
                        int pos = monthlySp.getSelectedItemPosition();
                        reuestConfigureDto.setFrequencyValue(monthlyDaysList.get(pos).getDay() + "");
                    }

                    // Checking if user entered values are in the given ranges
                    AppUtils.hideKeyboard(getActivity());
                    checkIfUserEnteredValues(reuestConfigureDto);
                } else {
                    AppUtils.openSnackBar(getView(), getString(R.string.please_select_frequency));
                }
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
                            AppUtils.hideKeyboard(getActivity());
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
                            AppUtils.hideKeyboard(getActivity());
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
                            AppUtils.hideKeyboard(getActivity());
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

    private void checkIfUserEnteredValues(ConfigureDto reuestConfigureDto) {
        String systolicValueStr = systolicValue.getText().toString();
        String diastolicValueStr = diastolicValue.getText().toString();
        String mgDlValueStr = mgDlValue.getText().toString();
        String customConfiguredParameterStr = customConfiguredParameterEt.getText().toString();
        Context context = getContext();

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

                        if (Double.parseDouble(systolicValueStr) > Double.parseDouble(diastolicValueStr)) {
                            proceedToUpdateData(reuestConfigureDto);
                        } else {
                            AppUtils.openSnackBar(parentView, getString(R.string.sys_dia_value_greater));
                        }
                    } else {
                        AppUtils.openSnackBar(parentView, rangeDia);
                    }

                } else {
                    AppUtils.openSnackBar(parentView, rangeSys);
                }
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_PP)) {

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
                    AppUtils.openSnackBar(parentView, range);
                }
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose)) {

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
                            AppUtils.openSnackBar(parentView, getString(R.string.max_value_greater));
                        }

                    } else {
                        AppUtils.openSnackBar(parentView, range);
                    }

                } else {
                    AppUtils.openSnackBar(parentView, range);
                }
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.enter_value_in_given_range));
            }
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {

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
                        AppUtils.openSnackBar(parentView, range);
                    }

                } else {
                    AppUtils.openSnackBar(parentView, range);
                }
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.enter_value_in_given_range));
            }
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {

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
                        AppUtils.openSnackBar(parentView, range);
                    }

                } else {
                    AppUtils.openSnackBar(parentView, range);
                }
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.enter_value_in_given_range));
            }
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {


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
                            AppUtils.openSnackBar(parentView, range);
                        }

                    } else {
                        AppUtils.openSnackBar(parentView, range);
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
                            AppUtils.openSnackBar(parentView, range);
                        }

                    } else {
                        AppUtils.openSnackBar(parentView, range);
                    }

                }
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.enter_value_in_given_range));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {
            proceedToUpdateData(reuestConfigureDto);
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Pulse_Oximeter)) {


            double customConfiguredParameterd = Double.parseDouble(customConfiguredParameterEt.getText().toString());
            double minConfiguredParameterd = Double.parseDouble(minConfiguredParameterEt.getText().toString());
            if (customConfiguredParameterd >= 60 && minConfiguredParameterd <= 100) {
                proceedToUpdateData(reuestConfigureDto);
            } else {
                AppUtils.openSnackBar(parentView, getString(R.string.value_should_in_be_in_between_of_60_100));
            }
        } else {
            if (!customConfiguredParameterEt.getText().toString().isEmpty()) {

                if (customConfiguredParameterStr.length() > 0 && !customConfiguredParameterStr.equalsIgnoreCase("0")) {

                    proceedToUpdateData(reuestConfigureDto);
                } else {
                    AppUtils.openSnackBar(parentView, getString(R.string.enter_configure_value_greater_than_zero));
                }
            }

        }
    }

    private void proceedToUpdateData(ConfigureDto reuestConfigureDto) {
        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Sugar_PP)) {
            reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(mgDlValue.getText().toString()));
            reuestConfigureDto.setMinBaselineValue(configureDto.getMinBaselineValue());

            reuestConfigureDto.setMeasurementUnit(vitalDto.getMeasurementUnit());

        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {


            if (heightSwitchCompat.isChecked()) {
                // cms
                int cms = Integer.parseInt(cmsSp.getSelectedItem().toString());

                reuestConfigureDto.setMeasurementUnit(Constants.CMS);
                reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(cms + ""));

            } else {
                // ft
                int ft = Integer.parseInt(ftSp.getSelectedItem().toString());
                int incs = Integer.parseInt(incsSp.getSelectedItem().toString());

                reuestConfigureDto.setMeasurementUnit(Constants.INCH);

                long incsLong = AppUtils.getIncsFromFtIncs(ft, incs);
                reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(incsLong + ""));
            }


        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

            if (tempSwitchCompat.isChecked()) {
                // cel
                reuestConfigureDto.setMeasurementUnit(Constants.C);
            } else {
                // fern
                reuestConfigureDto.setMeasurementUnit(Constants.F);
            }

            reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(systolicValue.getText().toString()));
            reuestConfigureDto.setMinBaselineValue(Double.parseDouble(diastolicValue.getText().toString()));
        } else if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose) ||
                fromWhichDisease.equalsIgnoreCase(Constants.Respiratory_Rate)) {
            reuestConfigureDto.setMaxBaselineValue(Double.parseDouble(systolicValue.getText().toString()));
            reuestConfigureDto.setMinBaselineValue(Double.parseDouble(diastolicValue.getText().toString()));
            reuestConfigureDto.setMeasurementUnit(vitalDto.getMeasurementUnit());
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
            }
            //reuestConfigureDto.setMeasurementUnit(configureDto.getMeasurementUnit());
        }

        callUpdateAPI(reuestConfigureDto);
    }

    private void updateIncArray(int position) {
        if (getContext() != null) {
            if (position == 0) {
                incArray = getResources().getStringArray(R.array.four_incs_adapter);
            } else if (position == 2) {
                incArray = getResources().getStringArray(R.array.six_incs_adapter);
            } else {
                incArray = getResources().getStringArray(R.array.incs_adapter);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_dropdown, incArray);
            incsSp.setAdapter(dataAdapter);
            updateWeightGraph();
        }
    }

    private void callUpdateAPI(ConfigureDto configureDto) {
        if (progress != null) {
            progress.show();
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
        GenericRequest<APIMessageResponse> updateDiseaseParameter = new GenericRequest<>
                (Request.Method.PUT, APIUrls.get().getConfigurationParameter(userDto.getId(), vitalDto.getUserParameterId()),
                        APIMessageResponse.class, configureDto,
                        apiMessageResponse -> {
                            AppUtils.hideProgressBar(progress);
                            AppUtils.isDataChanged = true;
                            onSaveListener.onSaveClicked(getString(R.string.configure));
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                            AppUtils.openSnackBar(getView(), res);
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
        if (getContext() != null) {
            MonthlySpinnerAdapter monthlySpinnerAdapter = new MonthlySpinnerAdapter(getContext(), R.layout.text_view, R.id.header, monthlyDaysList);
            monthlySp.setAdapter(monthlySpinnerAdapter);
        }
    }

    private void performWeeklyClickEvent() {

        weeklyLl.setVisibility(View.VISIBLE);
        monthlyLl.setVisibility(View.GONE);


        weeklyDaysList = AppUtils.getWeeklyList(null);
        if (getContext() != null) {
            WeeklySpinnerAdapter weeklySpinnerAdapter = new WeeklySpinnerAdapter(getContext(), R.layout.text_view, R.id.header, weeklyDaysList);
            daySp.setAdapter(weeklySpinnerAdapter);
        }
    }

    private void performDailyClickEvent() {

        weeklyLl.setVisibility(View.GONE);
        monthlyLl.setVisibility(View.GONE);

    }


    private void getValueFromArguments(Bundle arguments) {
        if (arguments != null) {
            Gson gson = new Gson();
            String userDtoStr = arguments.getString(Constants.USER_DTO);
            fromWhichDisease = arguments.getString(Constants.WHICH_DISEASE);

            String vitalDtoStr = arguments.getString(Constants.PARAMETER_DTO);

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                vitalDto = gson.fromJson(vitalDtoStr, ParameterDto.class);

                callGetParameterAPI();
            }
        }
    }

    private void callGetParameterAPI() {
        if (getContext() != null && userDto != null && vitalDto != null) {

            if (progress != null) {
                progress.show();
            }
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
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
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                AppUtils.openSnackBar(getView(), res);
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

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Context context = getContext();
                if (context != null) {
                    String msg = context.getString(R.string.enter_the);
                    String msg1 = context.getString(R.string.recommended_by_doctor);
                    if (fromWhichDisease != null) {

                        if (fromWhichDisease.equalsIgnoreCase(Constants.Heart_Rate)) {
                            msg = msg.concat(" " + getString(R.string.resting_heart_rate) + " ");
                        } else {
                            msg = msg.concat(" " + fromWhichDisease + " ");
                        }
                        msg = msg.concat(msg1);
                        headerMsgTv.setText(msg);

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

                                systolicValue.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});
                                diastolicValue.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 1)});

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

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            setUp();
        } else if (visible) {        // only at fragment onCreated
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;
        } else if (fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }
}
