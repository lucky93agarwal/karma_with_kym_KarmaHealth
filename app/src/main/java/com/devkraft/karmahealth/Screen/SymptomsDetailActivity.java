package com.devkraft.karmahealth.Screen;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Model.GetSymptomReportResponseDTO;
import com.devkraft.karmahealth.R;
import com.highsoft.highcharts.common.hichartsclasses.HICondition;
import com.highsoft.highcharts.common.hichartsclasses.HICredits;
import com.highsoft.highcharts.common.hichartsclasses.HIExporting;
import com.highsoft.highcharts.common.hichartsclasses.HILabel;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIResponsive;
import com.highsoft.highcharts.common.hichartsclasses.HIRules;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.SymptomsFilledTempListAdapter;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.SymptomCleverTap;
import com.devkraft.karmahealth.Model.UserSymptomTrackingRequest;
import com.devkraft.karmahealth.Model.UserSymptomTrackingResponseDTO;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SymptomsDetailActivity extends AppCompatActivity {
    private List<List<Object>> list1;
    private List<List<Object>> list2;
    private HIChartView symptomsCharView;
    private String chartOneColor = "2b41fa";
    private String chartTwoColor = "fea601";

    private String fileName;
    private long downLoadId;
    private DownloadManager downloadManager;

    private UserDto userDto;
    private String symptomName;
    private View mParentLayout;
    private CardView mCardDeleteSymptom;
    private ProgressDialogSetup progress;
    private GetUserAddedSymptomsResponseDTO symptomsDTO;
    private ArrayList<String> dateList = new ArrayList<>();
    private ArrayList<Integer> severityList = new ArrayList<>();
    private List<GetSymptomReportResponseDTO> getSymptomReportResponseDTO;

    private LinearLayout mEmptyView;
    private TextView mTextViewEndDate;
    private LinearLayout mLayoutEndDate;
    private TextView mTextViewStartDate;
    private LinearLayout mLayoutStarDate;
    private LinearLayout mLayoutExportReport;
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String TAG = "SymptomsDetailActivity";

    private int PERMISSION_REQUEST_CODE = 200;
    private boolean isFirstTimePermissionAsk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_detail);

        setupToolBar();
        initializeIds();
        setEmptyOptions();
        handleClickEvents();
        getIntentValues(getIntent());
        initializeDownloadManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntentValues(getIntent());
    }

    private void callGetSymptomChartData() {
        if (isEndDateIsGrater()) {

            if (progress != null) {
                progress.show();
            }

            Log.e("API_URL_GET_SYMPTOM", " = " +
                    APIUrls.get().getDetailsDataOfSymptom(symptomsDTO.getUserSymptomId(),
                            AppUtils.getBackendFormattedDateForSymptoms(mTextViewStartDate.getText().toString()),
                            AppUtils.getBackendFormattedDateForSymptoms(mTextViewEndDate.getText().toString())));

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<GetSymptomReportResponseDTO.GetSymptomReportListResponseDTO> getSymptomRequest = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().getDetailsDataOfSymptom(symptomsDTO.getUserSymptomId(),
                            AppUtils.getBackendFormattedDateForSymptoms(mTextViewStartDate.getText().toString()),
                            AppUtils.getBackendFormattedDateForSymptoms(mTextViewEndDate.getText().toString())),
                            GetSymptomReportResponseDTO.GetSymptomReportListResponseDTO.class, null,
                            symptomsResponseDTO -> {
                                AppUtils.hideProgressBar(progress);
                                getSymptomReportResponseDTO = symptomsResponseDTO;
                                Log.e("SymptomsReportList_log", " = " + new Gson().toJson(getSymptomReportResponseDTO));
                                if (getSymptomReportResponseDTO != null && !getSymptomReportResponseDTO.isEmpty()) {
                                    mEmptyView.setVisibility(View.GONE);
                                    symptomsCharView.setVisibility(View.VISIBLE);
                                    mLayoutExportReport.setVisibility(View.VISIBLE);
                                    showUpdatedChart(getSymptomReportResponseDTO);
                                } else {
                                    mEmptyView.setVisibility(View.VISIBLE);
                                    symptomsCharView.setVisibility(View.GONE);
                                    mLayoutExportReport.setVisibility(View.GONE);
                                }
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            authExpiredCallback.setRequest(getSymptomRequest);
            ApiService.get().addToRequestQueue(getSymptomRequest);
        } else {
            AppUtils.openSnackBar(mParentLayout, getString(R.string.plese_select_valid_date));
        }
    }

    private boolean isEndDateIsGrater() {
        Long startDateLong = AppUtils.getDateInMillis(mTextViewStartDate.getText().toString());
        Long endDateLong = AppUtils.getDateInMillis(mTextViewEndDate.getText().toString());
        return (endDateLong >= startDateLong);
    }

    private void setEmptyOptions() {
        HIOptions options = new HIOptions();
        symptomsCharView.setOptions(options);
    }

    private void showUpdatedChart(List<GetSymptomReportResponseDTO> getSymptomReportResponseDTO) {
        if (getSymptomReportResponseDTO != null && !getSymptomReportResponseDTO.isEmpty()) {
            severityList = getCalculatedSeverityList(getSymptomReportResponseDTO);
            dateList = getDateList(getSymptomReportResponseDTO);
            Log.e("severity_list_log", " = " + severityList.toString());
            Collections.reverse(dateList);
            chartDemoCode(severityList, dateList);
        }
    }

    private ArrayList<Integer> getCalculatedSeverityList(List<GetSymptomReportResponseDTO> getSymptomReportResponseDTO) {
        ArrayList<Integer> newSeverityList = new ArrayList<>();
        if (!getSymptomReportResponseDTO.isEmpty()) {
            for (int i = 0; i < getSymptomReportResponseDTO.size(); i++) {
                Log.e("severity_list_for_loop", " = " + newSeverityList.toString());
                if (getSymptomReportResponseDTO.get(i).getSeverityLevel().matches(Constants.NONE)
                        || getSymptomReportResponseDTO.get(i).getSeverityLevel().equalsIgnoreCase("0-2")) {
                    newSeverityList.add(0);
                } else if (getSymptomReportResponseDTO.get(i).getSeverityLevel().matches(Constants.MILD)
                        || getSymptomReportResponseDTO.get(i).getSeverityLevel().equalsIgnoreCase("3-5")) {
                    newSeverityList.add(1);
                } else if (getSymptomReportResponseDTO.get(i).getSeverityLevel().matches(Constants.MODERATE)
                        || getSymptomReportResponseDTO.get(i).getSeverityLevel().equalsIgnoreCase("6-8")) {
                    newSeverityList.add(2);
                } else if (getSymptomReportResponseDTO.get(i).getSeverityLevel().matches(Constants.SEVERE)
                        || getSymptomReportResponseDTO.get(i).getSeverityLevel().equalsIgnoreCase("9-10")) {
                    newSeverityList.add(3);
                } else if (getSymptomReportResponseDTO.get(i).getSeverityLevel().matches(Constants.VERY_SEVERE)
                        || getSymptomReportResponseDTO.get(i).getSeverityLevel().equalsIgnoreCase("10+")) {
                    newSeverityList.add(4);
                }
            }
        }
        return newSeverityList;
    }

    private ArrayList<String> getDateList(List<GetSymptomReportResponseDTO> getSymptomReportResponseDTO) {
        ArrayList<String> dateList = new ArrayList<>();
        if (!getSymptomReportResponseDTO.isEmpty()) {
            for (int i = 0; i < getSymptomReportResponseDTO.size(); i++) {
                Log.e("severity_list_for_loop", " = " + dateList.toString());
                if (getSymptomReportResponseDTO.get(i).getRecordedDate() != null) {
                    dateList.add(AppUtils.getDateInDDMMMYY(getSymptomReportResponseDTO.get(i).getRecordedDate()));
                }
            }
        }
        return dateList;
    }

    private void initializeDownloadManager() {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        fileName = "Report.pdf";
//        fileName = "Report.pdf";
    }

    private void getIntentValues(Intent intent) {
        Gson gson = new Gson();
        if (intent != null) {
            symptomName = intent.getStringExtra(Constants.SYMPTOM_NAME);
            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            getSupportActionBar().setTitle(symptomName);

            String symptomDtoString = intent.getStringExtra(Constants.SYMPTOMS_DTO);

            if (symptomDtoString != null && userDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                symptomsDTO = gson.fromJson(symptomDtoString, GetUserAddedSymptomsResponseDTO.class);

                setDates();
                callGetSymptomChartData();
            }
        }
    }

    private void setDates() {
        mTextViewStartDate.setText(AppUtils.getSevenDaysBackDate());
        mTextViewEndDate.setText(AppUtils.getTodayDate());
    }

    private void handleClickEvents() {
        mLayoutEndDate.setOnClickListener(v -> openEndDateDialogPicker());
        mLayoutExportReport.setOnClickListener(v -> startDownloadingFile());
        mLayoutStarDate.setOnClickListener(v -> openStartDateDialogPicker());
        mCardDeleteSymptom.setOnClickListener(v -> showDialogBoxForDeleteSymptom());
    }

    private void callExportReportAPI() {
        if (symptomsDTO.getUserSymptomId() == null) {
            symptomsDTO.setUserSymptomId(symptomsDTO.getUserSymptomId());//parameterDto.getId();
        }

        if (userDto != null && symptomsDTO != null) {
           /* if (progress != null) {
                progress.show();
            }*/

            String url = APIUrls.get().getReportForSymptoms(userDto.getId(), symptomsDTO.getUserSymptomId(),
                    AppUtils.getSevenDaysBackDate(),
                    AppUtils.getTodayDate());

            Log.e("url", url);
            HashMap<String,Object> valuesMap = new HashMap<>();
            valuesMap.put("Start Date",mTextViewStartDate.getText().toString());
            valuesMap.put("End Date",mTextViewEndDate.getText().toString());
            valuesMap.put("Symptom Name",symptomName);
            AppUtils.logCleverTapEvent(this,Constants.CLICKED_ON_EXPORT_REPORT_BUTTON_ON_SYMPTOM_REPORT_SCREEN,valuesMap);
//            callDownloadReport(url);
            downloadFileUsingDownloadManager(url);
        } else {
            AppUtils.openSnackBar(mParentLayout, getString(R.string.end_date_grater_start_date));
        }
    }

    //Old code for export report

    /* private void callDownloadReport(String url) {
        Log.v(TAG, "download() Method invoked ");
        if (!hasPermissions(SymptomsDetailActivity.this, PERMISSIONS)) {
            Log.e(TAG, "download() Method DON'T HAVE PERMISSIONS ");
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "download() Method HAVE PERMISSIONS ");
            downloadFileUsingDownloadManager(url);
        }
        Log.e(TAG, "download() Method completed ");
    }*/

    private void downloadFileUsingDownloadManager(String url) {
//        AppUtils.hideProgressBar(progress);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Report")
                .setDescription("File is downloading...")
//                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.addRequestHeader("Authorization", "Bearer " + ApplicationPreferences.get().getUserDetails().getAccessToken());
        //Enqueue the download.The download will start automatically once the download manager is ready
        // to execute it and connectivity is available.
        downLoadId = downloadManager.enqueue(request);
        Toast.makeText(this, "Downloading Started..", Toast.LENGTH_SHORT).show();
        Log.e("report_location_log", " = " + Environment.DIRECTORY_DOWNLOADS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
    }

    public void showDialogBoxForDeleteSymptom() {
        if (userDto != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            final View deletePopupView = getLayoutInflater().inflate(R.layout.layout_delete_symptom, null);

            Button mButtonDelete = deletePopupView.findViewById(R.id.button_delete);
            TextView mTextViewTitle = deletePopupView.findViewById(R.id.textview_title);
            Button mButtonNoCancel = deletePopupView.findViewById(R.id.button_no_cancel);
            TextView mTextViewDelete = deletePopupView.findViewById(R.id.textview_delete_message);

            dialogBuilder.setView(deletePopupView);
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
            dialog.setCancelable(false);

            String msgStr = getString(R.string.are_you_sure_deleting_symptom) + getString(R.string.space)
                    + symptomName + "?";
            mTextViewTitle.setText(msgStr);

            mButtonDelete.setOnClickListener(v -> {
                dialog.dismiss();
                callDeleteAPI(userDto, symptomsDTO);
            });

            mButtonNoCancel.setOnClickListener(v -> dialog.dismiss());
        }
    }

    private void callDeleteAPI(UserDto userDto, GetUserAddedSymptomsResponseDTO symptomsDTO) {
        if (userDto != null) {
            if (progress != null) {
                progress.show();
            }

            Log.e("API_URL", " = " + APIUrls.get().getSymptomDelete(userDto.getId(), symptomsDTO.getUserSymptomId()));
            callDeleteNEwAPI();
        }
    }

    private void callDeleteNEwAPI() {
        StringRequest request = new StringRequest(Request.Method.PUT, APIUrls.get().getSymptomDelete(userDto.getId(), symptomsDTO.getUserSymptomId()), response -> {
            if (!response.isEmpty()) {
                Log.e("Your Array Response", response);
                ApplicationDB.get().deleteSymptom(userDto.getId(),symptomsDTO.getUserSymptomId());
                AppUtils.hideProgressBar(progress);
                Intent intent = new Intent(SymptomsDetailActivity.this, HomeActivity.class);
                intent.putExtra(Constants.IS_FROM_SYMPTOM, true);
                intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
                intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                startActivity(intent);
                finish();
            }
        }, error -> {
            AppUtils.hideProgressBar(progress);
            Log.e("error is ", "" + error);
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + ApplicationPreferences.get().getUserDetails().getAccessToken());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void initializeIds() {
        symptomsCharView = findViewById(R.id.chart_symptoms);
        progress = ProgressDialogSetup.getProgressDialog(this);
        mCardDeleteSymptom = findViewById(R.id.card_delete_symptoms);
        mParentLayout = findViewById(R.id.parent_view_symptom_detail);

        mEmptyView = findViewById(R.id.emptyView);
        mLayoutEndDate = findViewById(R.id.endDateLl);
        mTextViewEndDate = findViewById(R.id.endDateTv);
        mLayoutStarDate = findViewById(R.id.startDateLl);
        mTextViewStartDate = findViewById(R.id.startDateTv);
        mLayoutExportReport = findViewById(R.id.layout_export_report);

        isFirstTimePermissionAsk = Boolean.parseBoolean(ApplicationPreferences.get().getStringValue(Constants.IS_PERMISSION_GRANTED));
        ActivityCompat.requestPermissions(SymptomsDetailActivity.this, PERMISSIONS, 112);
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.parameter));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SymptomsDetailActivity.this, HomeActivity.class);
            intent.putExtra(Constants.IS_FROM_SYMPTOM, true);
            intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
            startActivity(intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void openStartDateDialogPicker() {
        if (SymptomsDetailActivity.this != null) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
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

                        mTextViewStartDate.setText(monthStr + "/" + dayStr + "/" + year);
                        AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_START_DATE_SLCT);

                        callGetSymptomChartData();

                    }, mYear, mMonth, mDay);


            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            AppUtils.setDialogBoxButton(this, datePickerDialog);
            if (mTextViewStartDate.getText() != null && !mTextViewStartDate.getText().toString().equalsIgnoreCase(getString(R.string.start_date))) {
                AppUtils.setSelectedDate(datePickerDialog, mTextViewStartDate.getText().toString().trim());
            }
            datePickerDialog.show();
        }
    }

    private void openEndDateDialogPicker() {
        if (SymptomsDetailActivity.this != null) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
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

                        mTextViewEndDate.setText(monthStr + "/" + dayStr + "/" + year);
                        AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_END_DATE_SLCT);

                        callGetSymptomChartData();

                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            AppUtils.setDialogBoxButton(this, datePickerDialog);
            if (mTextViewEndDate.getText() != null && !mTextViewEndDate.getText().toString().equalsIgnoreCase(getString(R.string.end_date))) {
                AppUtils.setSelectedDate(datePickerDialog, mTextViewEndDate.getText().toString().trim());
            }
            datePickerDialog.show();
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void chartDemoCode(ArrayList<Integer> severityList, ArrayList<String> dateList) {

        try {
            if (getSymptomReportResponseDTO != null && !this.severityList.isEmpty()) {

                HIOptions options = new HIOptions();
                HITitle title = new HITitle();
                title.setText("");
                options.setTitle(title);

                HIYAxis yaxis = new HIYAxis();
                yaxis.setTitle(new HITitle());
                yaxis.getTitle().setText("Severity Level");
                ArrayList<String> nameList = new ArrayList<>();
                nameList.add("None");
                nameList.add("Mild");
                nameList.add("Moderate");
                nameList.add("Severe");
                nameList.add("Very Severe");
                yaxis.setCategories(nameList);
                options.setYAxis(new ArrayList() {{
                    add(yaxis);
                }});

                HIXAxis xAxis = new HIXAxis();
                xAxis.setCategories(new ArrayList<>(dateList));
                options.setXAxis(new ArrayList<HIXAxis>() {{
                    add(xAxis);
                }});

                HICredits hiCredits = new HICredits();
                hiCredits.setEnabled(false);
                options.setCredits(hiCredits);

                HIExporting hiExporting = new HIExporting();
                hiExporting.setEnabled(false);
                options.setExporting(hiExporting);

                HIPlotOptions plotoptions = new HIPlotOptions();
                plotoptions.setSeries(new HISeries());
                plotoptions.getSeries().setLabel(new HILabel());
                plotoptions.getSeries().getLabel().setConnectorAllowed(false);
                options.setPlotOptions(plotoptions);

                HILine line5 = new HILine();
                line5.setName(symptomsDTO.getName());
                line5.setData(new ArrayList<>(severityList));

                HIResponsive responsive = new HIResponsive();
                HIRules rules1 = new HIRules();
                rules1.setCondition(new HICondition());
                HashMap<String, HashMap> chartLegend = new HashMap<>();
                HashMap<String, String> legendOptions = new HashMap<>();
                legendOptions.put("layout", "horizontal");
                legendOptions.put("align", "left");
                legendOptions.put("verticalAlign", "top");
                chartLegend.put("legend", legendOptions);
                rules1.setChartOptions(chartLegend);
                responsive.setRules(new ArrayList<>(Collections.singletonList(rules1)));
                options.setResponsive(responsive);

                options.setSeries(new ArrayList<>(Arrays.asList(line5)));
                symptomsCharView.setOptions(options);
                symptomsCharView.reload();

            } else {
                AppUtils.openSnackBar(mParentLayout, "No data found");
            }
        } catch (Exception e) {

        }
    }



    //New Code for Export Report

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                //TODO:Enter download code
                callExportReportAPI();

                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED,"false");

            } else {
                // Permission request was denied.
                isFirstTimePermissionAsk = true;
                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED,"true");
                AppUtils.openSnackBar(mParentLayout, "Permission denied");
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void startDownloadingFile() {
        if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
//            Snackbar.make(mParentLayout, "Permission available", Snackbar.LENGTH_SHORT).show();
            callExportReportAPI();
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
    }

    /**
     * Requests the {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mParentLayout, "Permissions required",
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                ActivityCompat.requestPermissions(this,
                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);

                isFirstTimePermissionAsk = true;

                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED,"true");
            }).show();

        } else {

            Log.e("profile_log", " = " + isFirstTimePermissionAsk);
            if (isFirstTimePermissionAsk) {
                showEnablePermissionDialog();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void showEnablePermissionDialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        final View deletePopupView = getLayoutInflater().inflate(R.layout.layout_food_diary_popup, null);

        Button mButtonYes = deletePopupView.findViewById(R.id.button_ok);
        TextView mTextViewTitle = deletePopupView.findViewById(R.id.textview_title);

        dialogBuilder.setView(deletePopupView);
        androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.setCancelable(false);

        mTextViewTitle.setText(getString(R.string.enable_permission_msg));

        mButtonYes.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

}