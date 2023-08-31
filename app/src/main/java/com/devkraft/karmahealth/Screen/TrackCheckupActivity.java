package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.ReportDto;
import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.R;
import com.google.gson.Gson;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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




public class TrackCheckupActivity extends AppCompatActivity {
    private Button mButtonSubmit;
    private ProgressDialogSetup progress;
    private RelativeLayout mParentView;
    private TimePickerDialog mTimePicker;
    private TextView mTextViewDate, mTextViewTime;
    private EditText mEditTextNotes, mEditextAlbuminLeve;
    private LinearLayout mLayoutEditAlbuminLevel, mLayoutMarkAsDone;
    private ImageView mImageViewDate, mImageViewTime, mImageViewMarkAsDone;

    private UserDto userDto;
    private String backendDate;
    private String fromWhichDisease;
    private ParameterDto parameterDto;
    private TrackConfigurationDto trackConfigurationDto;

    private boolean isVitalEdit;
    private boolean isFromReport;
    private ReportDto mReportDto;
    private boolean setTaken = false;
    private boolean isSelected = false;
    private long userParameterTrackingId;

    private TextView mTextViewMeasurementUnit;
    private boolean isFromHome = false;
    private boolean receivedTaken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_checkup);
        setupToolbar();
        initializeIds();
        setOnClickEvents();
        getIntentValues(getIntent());
  //      Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getIntentValues(Intent intent) {
        if (intent != null) {
            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            fromWhichDisease = intent.getStringExtra(Constants.WHICH_DISEASE);
            String parameterDtoStr = intent.getStringExtra(Constants.PARAMETER_DTO);
            String trackDtoStr = intent.getStringExtra(Constants.TRACK_CONFIG_DTO);
            isVitalEdit = intent.getBooleanExtra(Constants.IS_VITAL_EDIT, false);
            userParameterTrackingId = intent.getLongExtra(Constants.USER_PARA_TRACKING_ID, 0);
            isFromReport = intent.getBooleanExtra(Constants.IS_FROM_REPORT, false);
            isFromHome = intent.getBooleanExtra(Constants.IS_FROM_HOME, false);

            Log.e("isFromHome", " = " + isFromHome);
            Log.e("isVitalEdit", " = " + isVitalEdit);
            Log.e("userParameterTrackingId", " = " + userParameterTrackingId);
            Log.e("parameterDto_log", " = " + new Gson().toJson(parameterDto));

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(fromWhichDisease);
            }

            if (userDtoStr != null && parameterDtoStr != null) {
                Gson gson = new Gson();
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(parameterDtoStr, ParameterDto.class);
                trackConfigurationDto = gson.fromJson(trackDtoStr, TrackConfigurationDto.class);

                Log.e("trackConfigure_log", " = " + new Gson().toJson(trackConfigurationDto));
                Log.e("parameterDtotrackConfigure_log", " = " + new Gson().toJson(parameterDto));

                if (isVitalEdit && trackConfigurationDto != null) {

                    if (parameterDto != null && parameterDto.getShowAlbuminLevelValue()) {
                        mLayoutEditAlbuminLevel.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutEditAlbuminLevel.setVisibility(View.GONE);
                    }

                    if (trackConfigurationDto.getMaxBaselineValue() != null) {
                        mEditextAlbuminLeve.setText(AppUtils.getValueWithoutDecimal(trackConfigurationDto.getMaxBaselineValue()));
                    }

                    mEditTextNotes.setText(trackConfigurationDto.getNotes());

                    if (trackConfigurationDto.getMaxBaselineValue() != null && trackConfigurationDto.getMaxBaselineValue() > 0) {
                        mEditextAlbuminLeve.setText("" + trackConfigurationDto.getMaxBaselineValue());
                    } else {
                        mEditextAlbuminLeve.setHint("00");
                    }

                    if (trackConfigurationDto.getRecordedDate() != null) {
                        mTextViewDate.setText(AppUtils.getOnlyDateFromDateTime(trackConfigurationDto.getRecordedDate()));
                        mTextViewTime.setText(AppUtils.get12HoursTimeFormat(AppUtils.getOnlyTimeFromDate(trackConfigurationDto.getRecordedDate())));
                    }

                    if (trackConfigurationDto.getTaken() != null && trackConfigurationDto.getTaken())
                        mImageViewMarkAsDone.setImageResource(R.drawable.ic_tick_selected_icon);
                    else
                        mImageViewMarkAsDone.setImageResource(R.drawable.ic_untick_icon);
                } else if (parameterDto != null) {
                    Log.e("parameterDto_condition", " = " + new Gson().toJson(parameterDto));
                    if (parameterDto.getShowAlbuminLevelValue()) {
                        mLayoutEditAlbuminLevel.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutEditAlbuminLevel.setVisibility(View.GONE);
                    }

                    if (parameterDto.getMeasurementUnit() != null && !parameterDto.getMeasurementUnit().equalsIgnoreCase(""))
                        mTextViewMeasurementUnit.setText("Value (" + parameterDto.getMeasurementUnit() + ")");
                    else
                        mTextViewMeasurementUnit.setText("Value");
                }
            }

            if (isFromHome) {
                setVisibility();
                callGetCheckupDataAPI();
            }
        }
    }

    private void callGetCheckupDataAPI() {
        if (userDto != null && parameterDto != null) {

            if (progress != null) {
                progress.show();
            }


            String orderString = Constants.DESCENDING;

            String url = APIUrls.get().getReport(userDto.getId(), parameterDto.getUserParameterId(),
                    AppUtils.getBackendFormattedDate(AppUtils.getTodayDate()),
                    AppUtils.getBackendFormattedDate(AppUtils.getTodayDate()), orderString);

            Log.e("url", url);

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<ReportDto> updateDiseaseParameter = new GenericRequest<>
                    (Request.Method.GET, url,
                            ReportDto.class, null,
                            reportDto -> {
                                AppUtils.hideProgressBar(progress);
                                Log.e("reportData_log", " = " + new Gson().toJson(reportDto));
                                mReportDto = reportDto;
//                                setCheckUpListView();
                                setupCheckupData(reportDto);
                                Log.e("data", new Gson().toJson(reportDto));
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(TrackCheckupActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentView, res);
                            });
            authExpiredCallback.setRequest(updateDiseaseParameter);
            ApiService.get().addToRequestQueue(updateDiseaseParameter);
        }
    }

    private void setupCheckupData(ReportDto reportDto) {
        if (reportDto != null && !reportDto.getDates().isEmpty()) {
            if (parameterDto.getShowAlbuminLevelValue()) {
                mLayoutEditAlbuminLevel.setVisibility(View.VISIBLE);

                if (parameterDto.getMeasurementUnit() != null && !parameterDto.getMeasurementUnit().equalsIgnoreCase("")) {
                    mTextViewMeasurementUnit.setText("Value (" + reportDto.getDates().get(0).getMeasurementUnit() + ")");
                } else {
                    mTextViewMeasurementUnit.setText("Value");
                }
            }

            if (reportDto.getDates().get(0).getNotes() != null)
                mEditTextNotes.setText(reportDto.getDates().get(0).getNotes());

            if (reportDto.getDates().get(0).getRecordedDate() != null)
                mTextViewTime.setText(AppUtils.get12HoursTimeFormat(AppUtils.getOnlyTimeFromDate(reportDto.getDates().get(0).getRecordedDate())));
            if (reportDto.getDates().get(0).getTaken()) {
                setTaken = true;
                mImageViewMarkAsDone.setImageResource(R.drawable.ic_tick_selected_icon);
            } else {
                setTaken = false;
                mImageViewMarkAsDone.setImageResource(R.drawable.ic_untick_icon);
            }

            if (reportDto.getDates().get(0).getMaxBaselineValue() != null) {
                mEditextAlbuminLeve.setText("" + reportDto.getDates().get(0).getMaxBaselineValue());
            } else {
                mEditextAlbuminLeve.setHint("0");
            }
        }
    }

    private void setVisibility() {
        Log.e("parameterVisibility", " = " + new Gson().toJson(parameterDto));
        if (parameterDto.getShowAlbuminLevelValue()) {
            mLayoutEditAlbuminLevel.setVisibility(View.VISIBLE);
        } else {
            mLayoutEditAlbuminLevel.setVisibility(View.GONE);
        }

        if (parameterDto.getMeasurementUnit() != null && !parameterDto.getMeasurementUnit().equalsIgnoreCase(""))
            mTextViewMeasurementUnit.setText("Value (" + parameterDto.getMeasurementUnit() + ")");
        else
            mTextViewMeasurementUnit.setText("Value");

        if (parameterDto.getMaxBaselineValue() != null && parameterDto.getMaxBaselineValue() > 0) {
            mEditextAlbuminLeve.setText("" + parameterDto.getMaxBaselineValue());
        } else {
            mEditextAlbuminLeve.setHint("0");
        }

        if (isVitalEdit)
            mButtonSubmit.setText(this.getString(R.string.update));
        else
            mButtonSubmit.setText(this.getString(R.string.submit));
    }

    private void setOnClickEvents() {
        mTextViewTime.setOnClickListener(view -> showTimePicker());
        mImageViewTime.setOnClickListener(view -> showTimePicker());

        mImageViewDate.setOnClickListener(view -> openDateDialogPicker());
        mTextViewDate.setOnClickListener(view -> openDateDialogPicker());

        mImageViewMarkAsDone.setOnClickListener(v -> {
            if (isSelected) {
                isSelected = false;
                setTaken = false;
                mImageViewMarkAsDone.setImageResource(R.drawable.ic_untick_icon);
            } else {
                isSelected = true;
                setTaken = true;
                mImageViewMarkAsDone.setImageResource(R.drawable.ic_tick_selected_icon);
            }
        });

        mButtonSubmit.setOnClickListener(v -> {
            if (validate()) {
                trackConfigurationDto = new TrackConfigurationDto();
                trackConfigurationDto.setNotes(mEditTextNotes.getText().toString());
                trackConfigurationDto.setMaxBaselineDisplayName(parameterDto.getName());
                trackConfigurationDto.setRecordedDate(AppUtils.getBackendFormattedDate(mTextViewDate.getText().toString()) + " " + mTextViewTime.getText().toString());

                if (parameterDto.getShowAlbuminLevelValue()) {
                    trackConfigurationDto.setMaxBaselineValue(Double.valueOf(mEditextAlbuminLeve.getText().toString()));
                } else {
                    trackConfigurationDto.setMaxBaselineValue(0.0);
                }

                if (parameterDto.getShowAlbuminLevelValue()) {
                    trackConfigurationDto.setMeasurementUnit(parameterDto.getMeasurementUnit());
                } else {
                    trackConfigurationDto.setMeasurementUnit("");
                }

                trackConfigurationDto.setTaken(setTaken);

                if (isVitalEdit)
                    callEditCheckupAPI(trackConfigurationDto);
                else
                    callTrackCheckupAPI(trackConfigurationDto);
            }
        });
    }

    private void callEditCheckupAPI(TrackConfigurationDto trackConfigurationDto) {
        if (userDto != null && parameterDto != null) {

            Log.e("Edit_checkup_log", " = " + new Gson().toJson(trackConfigurationDto));

            if (progress != null) {
                progress.show();
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<APIMessageResponse> updateDiseaseParameter = new GenericRequest<APIMessageResponse>
                    (Request.Method.PUT, APIUrls.get().getEditParameterTrack(userDto.getId(), userParameterTrackingId),
                            APIMessageResponse.class, trackConfigurationDto,
                            apiMessageResponse -> {
                                AppUtils.hideProgressBar(progress);
                                if (isVitalEdit) {
                                    if (isFromReport) {
                                        Intent intent = new Intent(TrackCheckupActivity.this, CheckupDetailActivity.class);
                                        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                        intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
                                        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
                                        intent.putExtra(Constants.IS_VITAL_EDIT, true);
                                        intent.putExtra(Constants.USER_PARA_TRACKING_ID, userParameterTrackingId);
                                        intent.putExtra(Constants.IS_FROM_REPORT, isFromReport);
                                        finish();
                                    } else {
                                        AppUtils.isDataChanged = true;
                                        Intent intent = new Intent(TrackCheckupActivity.this, HomeActivity.class);
                                        intent.putExtra(Constants.DATE, backendDate);
                                        intent.putExtra(Constants.USER_DTO_TRACK, Constants.GSON.toJson(userDto));
                                        intent.putExtra(Constants.IS_VITAL_EDIT, true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(TrackCheckupActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentView, res);
                            });
            authExpiredCallback.setRequest(updateDiseaseParameter);
            ApiService.get().addToRequestQueue(updateDiseaseParameter);
        }
    }

    private boolean validate() {
        if (parameterDto.getShowAlbuminLevelValue()) {
            if (mTextViewDate.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please select Date");
                return false;
            } else if (mTextViewTime.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please select Time");
                return false;
            } else if (mEditTextNotes.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please Enter Notes");
                return false;
            } else if (mEditextAlbuminLeve.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please Enter Value");
                return false;
            }
        } else {
            if (mTextViewDate.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please select Date");
                return false;
            } else if (mTextViewTime.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please select Time");
                return false;
            } else if (mEditTextNotes.getText().toString().equalsIgnoreCase("")) {
                AppUtils.openSnackBar(mParentView, "Please Enter Notes");
                return false;
            }
        }
        return true;
    }

    private void callTrackCheckupAPI(TrackConfigurationDto trackConfigurationDto) {

        Gson gson = new Gson();
        Log.e("request_Object", " = " + gson.toJson(trackConfigurationDto));

        if (userDto != null && parameterDto != null) {

            if (progress != null) {
                progress.show();
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<APIMessageResponse> editTrackParameter = new GenericRequest<APIMessageResponse>
                    (Request.Method.POST, APIUrls.get().getConfigurationParameterTrack(userDto.getId(), parameterDto.getUserParameterId()),
                            APIMessageResponse.class, trackConfigurationDto,
                            apiMessageResponse -> {
                                AppUtils.hideProgressBar(progress);
                                AppUtils.isDataChanged = true;
                                Intent intent = new Intent(TrackCheckupActivity.this, CheckupDetailActivity.class);
                                intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                intent.putExtra(Constants.WHICH_TEST, fromWhichDisease);
                                intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
                                startActivity(intent);
                                finish();
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(TrackCheckupActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentView, res);
                            });
            authExpiredCallback.setRequest(editTrackParameter);
            ApiService.get().addToRequestQueue(editTrackParameter);
        }

    }

    private void initializeIds() {
        progress = ProgressDialogSetup.getProgressDialog(this);

        mTextViewDate = findViewById(R.id.textview_date);
        mTextViewTime = findViewById(R.id.textview_time);
        mEditTextNotes = findViewById(R.id.editext_notes);
        mImageViewDate = findViewById(R.id.imageview_date);
        mImageViewTime = findViewById(R.id.imageview_time);
        mImageViewTime = findViewById(R.id.imageview_time);
        mParentView = findViewById(R.id.parent_view_checkup);
        mButtonSubmit = findViewById(R.id.button_submit_checkup);
        mLayoutMarkAsDone = findViewById(R.id.layout_mark_as_done);
        mEditextAlbuminLeve = findViewById(R.id.editext_albumin_level);
        mImageViewMarkAsDone = findViewById(R.id.imageview_mark_as_done);
        mLayoutEditAlbuminLevel = findViewById(R.id.layout_edit_albumin_level);
        mTextViewMeasurementUnit = findViewById(R.id.textview_measurement_unit);


        mTextViewDate.setText(AppUtils.getTodayDate());
        mTextViewTime.setText(AppUtils.get12HoursTimeFormat(AppUtils.getCurrentTimeInString()));
        backendDate = AppUtils.getTodayDateForSchedule();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.parameter));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }


    private void openDateDialogPicker() {
        if (!isFromHome) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                    (view, year, monthOfYear, dayOfMonth) -> {
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

                        mTextViewDate.setText(monthStr + "/" + dayStr + "/" + year);

                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            AppUtils.setDialogBoxButton(this, datePickerDialog);
            if (mTextViewDate.getText() != null && !mTextViewDate.getText().toString().equalsIgnoreCase(getString(R.string.select_date_track))) {
                AppUtils.setSelectedDate(datePickerDialog, mTextViewDate.getText().toString().trim());
            }
            datePickerDialog.show();
        }
    }

    private void showTimePicker() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                (timePicker, selectedHour, selectedMinute) ->
                        mTextViewTime.setText(AppUtils.get12HoursTimeFormat(String.format("%02d:%02d", selectedHour, selectedMinute))),
                hour, minute, true);
        Context context = TrackCheckupActivity.this;
        if (mTimePicker != null) {
            mTimePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, context.getString(R.string.ok), mTimePicker);
            mTimePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
            mTimePicker.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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