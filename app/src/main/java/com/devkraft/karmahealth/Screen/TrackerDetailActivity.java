package com.devkraft.karmahealth.Screen;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Adapter.ReportVitalAdapter;
import com.devkraft.karmahealth.Model.ConfigureDto;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.ReportDto;
import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

import retrofit2.Call;
import retrofit2.Callback;

public class TrackerDetailActivity extends AppCompatActivity  implements ConfigureFragment.OnSaveListener {
    private int position;
    private UserDto userDto;
    private MenuItem menuItem;
    private boolean isVitalEdit;
    private ParameterDto parameterDto;
    private Long userParameterTrackingId;
    private TextView mTextViewAboutParameter;
    private UserInteractionListener userInteractionListener;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;


    private LinearLayout mLayoutAboutParameter, mLayoutConfigureParameter,
            mLayoutDeleteParameter;

    private String fileName;
    private long downLoadId;
    private DownloadManager downloadManager;

    private String endDate;
    private String startDate;
    private String userDtoStr;
    private String vitalDtoStr;
    private boolean isFromReport;
    private String fromWhichDisease;

    private ReportVitalAdapter reportVitalAdapter;
    private Context context = TrackerDetailActivity.this;
    private TextView mTextViewDelete;
    private TextView selectedDateTv, maxHeader, minHeaderTv, maxValueTv,
            minValueTv, maxValueUnit, minValueUnit;

    private String chartOneColor = "2b41fa";
    private String chartTwoColor = "fea601";

    HIChartView chartView;
    private View viewAbout;
    private ReportDto rDto;
    private View line1, line2;
    private ScrollView scrollView;
    private RelativeLayout weightLl;
    private LinearLayout emptyView;
    private LinearLayout mParentView;
    private List<List<Object>> list1;
    private List<List<Object>> list2;
    private LinearLayout chartDataLl;
    private Boolean isGraphView = true;
    private ProgressDialogSetup progress;
    private ImageView graphView, listView;
    private TextView startDateTv, endDateTv;
    private ImageView maxDotView, minDotView;
    private LinearLayout mLayoutExportReport;
    private TextView mTextViewDeleteParameter;
    private LinearLayout startDateLl, endDateLl;
    private LinearLayout charViewLl, listViewLl;
    private HashMap<String, Double> stringDoubleMap;
    private HashMap<String, Double> stringDoubleMapSecond;
    private ConfigureFragment.OnSaveListener onSaveListener;
    private TextView mTextViewBloodGlucoseParameterMeasured;
    private androidx.appcompat.widget.SwitchCompat weightSwitchCompat;
    private TextView trackNowBtn, switchCompatFalseTv, switchCompatTrueTv;
    private ExpandableHeightListView vitalListView;
    private LinearLayout minHeaderLl, maxHeaderLl, maxValueLl, minValueLl, chartDataDisplayLL;
    private LinearLayout chartDataDisplayLLSingle, mLayoutMaxValueSingle;
    private TextView mTextViewMaxValueSingle, mTextViewMaxValueUnitSingle, mTextViewSelectedDateSingle;
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String TAG = "TrackerDetailsActivity";

    private HashMap<String, Object> map;
    private int PERMISSION_REQUEST_CODE = 200;
    private boolean isFirstTimePermissionAsk = false;

    public void setUserInteractionListener(UserInteractionListener userInteractionListener) {
        this.userInteractionListener = userInteractionListener;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (userInteractionListener != null)
            userInteractionListener.onUserInteraction();
    }

    @Override
    public void onSaveClicked(String tab) {

    }

    public interface UserInteractionListener {
        void onUserInteraction();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_detail);
        setupToolbar();
        initializeIds();
        getValueFromArguments(getIntent());
        handleClickEvent();
        initializeDownloadManager();
        map = new HashMap<>();
        map.put(Constants.HEALTH_MATRICS_NAME, fromWhichDisease);
        AppUtils.logCleverTapEvent(this, Constants.HEALTH_MARTICS_REPORT_SCREEN_OPENED, map);
     //   Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
        isFirstTimePermissionAsk = Boolean.parseBoolean(ApplicationPreferences.get().getStringValue(Constants.IS_PERMISSION_GRANTED));
    }

    private void initializeDownloadManager() {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        fileName = "Report.pdf";
    }

    private void getValueFromArguments(Intent arguments) {
        if (arguments != null) {
            Gson gson = new Gson();
            endDate = arguments.getStringExtra(Constants.END_DATE);
            startDate = arguments.getStringExtra(Constants.START_DATE);
            vitalDtoStr = arguments.getStringExtra(Constants.PARAMETER_DTO);
            String userDtoStr = arguments.getStringExtra(Constants.USER_DTO);
            position = arguments.getIntExtra(Constants.POSITION, -1);
            fromWhichDisease = arguments.getStringExtra(Constants.WHICH_DISEASE);
            isFromReport = arguments.getBooleanExtra(Constants.IS_FROM_REPORT, false);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(fromWhichDisease);
                mTextViewDeleteParameter.setText("Delete ");
                mTextViewAboutParameter.setText("About " + fromWhichDisease);
            }

            if (fromWhichDisease.equalsIgnoreCase(Constants.Weight) ||
                    fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {
                weightLl.setVisibility(View.VISIBLE);

                Context context = TrackerDetailActivity.this;
                if (context != null) {
                    if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {
                        switchCompatFalseTv.setText(context.getString(R.string.lbs));
                        switchCompatTrueTv.setText(context.getString(R.string.kgs));
                    } else if (fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {
                        switchCompatFalseTv.setText(context.getString(R.string.fahrenheit));
                        switchCompatTrueTv.setText(context.getString(R.string.celsius));
                    }
                }
            } else {
                weightLl.setVisibility(View.GONE);
            }

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(vitalDtoStr, ParameterDto.class);

                if (isFromReport) {
                    startDateTv.setText(startDate);
                    endDateTv.setText(endDate);
                    isGraphView = false;
                } else {
                    setDates();
                }
                callGetReportAPI();
            }

            if (parameterDto.getInformationLink() != null) {
                viewAbout.setVisibility(View.VISIBLE);
                mLayoutAboutParameter.setVisibility(View.GONE);
            } else {
                viewAbout.setVisibility(View.GONE);
                mLayoutAboutParameter.setVisibility(View.GONE);
            }
        }
    }

    private void initializeIds() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();

        viewAbout = findViewById(R.id.view_about);
        mParentView = findViewById(R.id.parent_view);
        mLayoutAboutParameter = findViewById(R.id.layout_about_parameter);
        mLayoutDeleteParameter = findViewById(R.id.layout_delete_paremeter);
        boolean heck = getIntent().getBooleanExtra("delete",false);
        Log.i("checkmodrulistg","activity = "+String.valueOf(heck));
        if(heck==true){
            mLayoutDeleteParameter.setVisibility(View.VISIBLE);
        }else {
            mLayoutDeleteParameter.setVisibility(View.GONE);
        }
        mLayoutConfigureParameter = findViewById(R.id.layout_configure_parameter);

        mTextViewAboutParameter = findViewById(R.id.about_parameter);
        mTextViewDeleteParameter = findViewById(R.id.delete_parameter);

        minDotView = findViewById(R.id.minDotView);
        maxDotView = findViewById(R.id.maxDotView);
        vitalListView = findViewById(R.id.vitalListView);

        vitalListView.setExpanded(true);
        progress = ProgressDialogSetup.getProgressDialog(this);

        switchCompatTrueTv = findViewById(R.id.switchCompatTrueTv);
        switchCompatFalseTv = findViewById(R.id.switchCompatFalseTv);

        listView = findViewById(R.id.listView);
        weightLl = findViewById(R.id.weightLl);
        graphView = findViewById(R.id.graphView);
        endDateLl = findViewById(R.id.endDateLl);
        emptyView = findViewById(R.id.emptyView);
        endDateTv = findViewById(R.id.endDateTv);
        chartView = findViewById(R.id.highChart);
        scrollView = findViewById(R.id.scrollView);
        maxValueLl = findViewById(R.id.maxValueLl);
        minValueLl = findViewById(R.id.minValueLl);
        trackNowBtn = findViewById(R.id.trackNowBtn);
        startDateLl = findViewById(R.id.startDateLl);
        startDateTv = findViewById(R.id.startDateTv);
        maxHeaderLl = findViewById(R.id.maxHeaderLl);
        minHeaderLl = findViewById(R.id.minHeaderLl);
        selectedDateTv = findViewById(R.id.selectedDateTv);

        line1 = findViewById(R.id.line1);
//        line2 = findViewById(R.id.line2);
        maxHeader = findViewById(R.id.maxHeader);
        charViewLl = findViewById(R.id.charViewLl);
        maxValueTv = findViewById(R.id.maxValueTv);
        minValueTv = findViewById(R.id.minValueTv);
        listViewLl = findViewById(R.id.listViewLl);
        minHeaderTv = findViewById(R.id.minHeaderTv);
        chartDataLl = findViewById(R.id.chartDataLl);
        minValueUnit = findViewById(R.id.minValueUnit);
        maxValueUnit = findViewById(R.id.maxValueUnit);
        chartDataDisplayLL = findViewById(R.id.chartDataDisplayLL);
        weightSwitchCompat = findViewById(R.id.weightSwitchCompat);
        mLayoutExportReport = findViewById(R.id.layout_export_report);
        chartDataDisplayLLSingle = findViewById(R.id.chartDataDisplayLLSingle);
        mTextViewBloodGlucoseParameterMeasured = findViewById(R.id.textview_blood_glucose_parameter_measured);

        mTextViewMaxValueSingle = findViewById(R.id.maxValueTv_single);
        mTextViewMaxValueUnitSingle = findViewById(R.id.maxValueUnit_single);
        mTextViewSelectedDateSingle = findViewById(R.id.selectedDateTv_single);

        ActivityCompat.requestPermissions(TrackerDetailActivity.this, PERMISSIONS, 112);
        weightSwitchCompat.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
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

    @SuppressLint("SetTextI18n")
    private void getIntentValue(Intent intent) {
        if (intent != null) {
            endDate = intent.getStringExtra(Constants.END_DATE);
            userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            startDate = intent.getStringExtra(Constants.START_DATE);
            vitalDtoStr = intent.getStringExtra(Constants.PARAMETER_DTO);

            isVitalEdit = intent.getBooleanExtra(Constants.IS_VITAL_EDIT, false);
            userParameterTrackingId = intent.getLongExtra(Constants.USER_PARA_TRACKING_ID, -1);
            fromWhichDisease = intent.getStringExtra(Constants.WHICH_DISEASE);
            isFromReport = intent.getBooleanExtra(Constants.IS_FROM_REPORT, false);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(fromWhichDisease);
                mTextViewDelete.setText(getString(R.string.delete) + fromWhichDisease);
                mTextViewAboutParameter.setText(getString(R.string.about) + fromWhichDisease);
            }

            Gson gson = new Gson();

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(vitalDtoStr, ParameterDto.class);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getValueFromArguments(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.track_menu, menu);

        menuItem = menu.findItem(R.id.track);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.track) {
            openTrackActivity();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
          /*  Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.IS_FROM_TRACK_DETAIL, true);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
            startActivity(intent);
            finish();*/ // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDates() {
        startDateTv.setText(AppUtils.getSevenDaysBackDate());
        endDateTv.setText(AppUtils.getTodayDate());
    }

    private void callGetReportAPI() {

        if (!startDateTv.getText().toString().equalsIgnoreCase("") && !endDateTv.getText().toString().equalsIgnoreCase("")) {
            if (parameterDto.getUserParameterId() == null) {
                parameterDto.setUserParameterId(parameterDto.getId());//parameterDto.getId();
            }
            if (isEndDateIsGrater()) {
                if (userDto != null && parameterDto != null) {

                    if (progress != null) {
                        progress.show();
                    }

                    String url, orderString;

                    if (isGraphView) {
                        orderString = Constants.ASCENDING;
                    } else {
                        orderString = Constants.DESCENDING;
                    }

                    if (fromWhichDisease.equalsIgnoreCase(Constants.Weight) ||
                            fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {

                        boolean isWeight = fromWhichDisease.equalsIgnoreCase(Constants.Weight);

                        String weight;
                        if (weightSwitchCompat.isChecked()) {
                            if (isWeight) {
                                weight = "&unit=" + Constants.KGS;
                            } else {
                                weight = "&tempUnit=" + Constants.degree_c;
                            }

                        } else {
                            if (isWeight) {
                                weight = "&unit=" + Constants.LBS;
                            } else {
                                weight = "&tempUnit=" + Constants.degree_f;
                            }
                        }

                        url = APIUrls.get().getReport(userDto.getId(), parameterDto.getUserParameterId(),
                                AppUtils.getBackendFormattedDate(startDateTv.getText().toString()),
                                AppUtils.getBackendFormattedDate(endDateTv.getText().toString()), weight, orderString);
                    } else {
                        Log.e("StartDate_log = ", startDateTv.getText().toString());
                        Log.e("endDate_log = ", endDateTv.getText().toString());
                        url = APIUrls.get().getReport(userDto.getId(), parameterDto.getUserParameterId(),
                                AppUtils.getBackendFormattedDate(startDateTv.getText().toString()),
                                AppUtils.getBackendFormattedDate(endDateTv.getText().toString()), orderString);
                    }

                    Log.i("chekcurlcheck", url);
                    chartDataDisplayLL.setVisibility(View.GONE);
                    chartDataDisplayLLSingle.setVisibility(View.GONE);
                    AppUtils.hideProgressBar(progress);
                    /*final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(context);
                    GenericRequest<ReportDto> updateDiseaseParameter = new GenericRequest<ReportDto>
                            (Request.Method.GET, url,
                                    ReportDto.class, null,
                                    new Response.Listener<ReportDto>() {
                                        @Override
                                        public void onResponse(ReportDto reportDto) {
                                            AppUtils.hideProgressBar(progress);
                                            rDto = reportDto;
                                            if (isGraphView) {
                                                performGraphView();
                                            } else {
                                                performListView();
                                            }
                                            Log.e("data", new Gson().toJson(reportDto));
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            authExpiredCallback.hideProgressBar();
                                            AppUtils.hideProgressBar(progress);
                                            String res = AppUtils.getVolleyError(context, error, authExpiredCallback);
                                            AppUtils.openSnackBar(mParentView, res);
                                        }
                                    });
                    authExpiredCallback.setRequest(updateDiseaseParameter);
                    ApiService.get().addToRequestQueue(updateDiseaseParameter);*/
                }
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.end_date_grater_start_date));
            }
        }
    }

    private void performListView() {
        if (context != null) {
            listView.setBackgroundResource(R.drawable.image_border_selected);
            graphView.setBackgroundResource(R.drawable.image_border_not_selected);
            listView.setPadding(15, 15, 15, 15);
            graphView.setPadding(15, 15, 15, 15);
            updateAdapter();

            listView.setColorFilter(ContextCompat.getColor(context, R.color.white));
            graphView.setColorFilter(ContextCompat.getColor(context, R.color.report_image_color));
        }
    }

    private void performGraphView() {
        if (context != null) {
            AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_CHART_UPDATED);
            graphView.setBackgroundResource(R.drawable.image_border_selected);
            listView.setBackgroundResource(R.drawable.image_border_not_selected);
            listView.setPadding(15, 15, 15, 15);
            graphView.setPadding(15, 15, 15, 15);

            graphView.setColorFilter(ContextCompat.getColor(context, R.color.white));
            listView.setColorFilter(ContextCompat.getColor(context, R.color.report_image_color));

            if (rDto != null && rDto.getDates().size() > 0) {
                charViewLl.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                listViewLl.setVisibility(View.GONE);
                mLayoutExportReport.setVisibility(View.VISIBLE);
                setChartData(rDto);
            } else {
                charViewLl.setVisibility(View.GONE);
                listViewLl.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                mLayoutExportReport.setVisibility(View.GONE);
            }
        }
    }

    private void handleClickEvent() {

        listView.setOnClickListener(view -> {
            isGraphView = false;
            callGetReportAPI();
        });

        graphView.setOnClickListener(view -> {
            isGraphView = true;
            callGetReportAPI();
        });

        weightSwitchCompat.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            chartDataDisplayLL.setVisibility(View.GONE);
            chartDataDisplayLLSingle.setVisibility(View.GONE);
            callGetReportAPI();
        });

        trackNowBtn.setOnClickListener(view -> openTrackActivity());
        endDateLl.setOnClickListener(view -> openEndDateDialogPicker());
        mLayoutExportReport.setOnClickListener(v -> startDownloadingFile());
        startDateLl.setOnClickListener(view -> openStartDateDialogPicker());
        mLayoutAboutParameter.setOnClickListener(v -> openAboutParameterScreen());
        mLayoutConfigureParameter.setOnClickListener(v -> openConfigureActivity());
        mLayoutDeleteParameter.setOnClickListener(v -> showDeleteParameterDialog(parameterDto, position));
    }

    private void openConfigureActivity() {
        AppUtils.logCleverTapEvent(this, Constants.CLICKED_ON_CONFIGURE_BASELINE_HEALTH_MATRICS_ON_REPORT_SCREEN, map);
        if (fromWhichDisease.equalsIgnoreCase(Constants.Weight)) {
            Intent intent = new Intent(TrackerDetailActivity.this, ConfigureWeightActivity.class);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
            intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
            intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
            startActivity(intent);
        } else {
            Intent intent = new Intent(TrackerDetailActivity.this, ConfigureActivity.class);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
            intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
            intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
            startActivity(intent);
        }
    }

    private void openAboutParameterScreen() {
        if (parameterDto != null) {
            Intent intent = new Intent(TrackerDetailActivity.this, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, parameterDto.getName());
            intent.putExtra(Constants.URL, parameterDto.getInformationLink());
            startActivity(intent);
        }
    }

    private void openTrackActivity() {
        HashMap<String, Object> healthMap = new HashMap<>();
        healthMap.put(Constants.HEALTH_MATRICS_NAME, fromWhichDisease);
        AppUtils.logCleverTapEvent(this,
                Constants.CLICKED_ON_TRACK_BUTTON_ON_HEALTH_METRICS_REPORT_SCREEN, healthMap);
        Intent intent = new Intent(TrackerDetailActivity.this, TrackActivity.class);
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
        intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
        intent.putExtra(Constants.IS_VITAL_EDIT, isVitalEdit);
        intent.putExtra(Constants.USER_PARA_TRACKING_ID, userParameterTrackingId);
        intent.putExtra(Constants.IS_FROM_REPORT, isFromReport);
        startActivity(intent);
    }

    private void updateAdapter() {
        if (rDto != null && rDto.getDates().size() > 0) {
            charViewLl.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            mLayoutExportReport.setVisibility(View.VISIBLE);
            listViewLl.setVisibility(View.VISIBLE);
            reportVitalAdapter = new ReportVitalAdapter(context, R.layout.report_vital_list,
                    rDto.getDates(), parameterDto.getId(), parameterDto.getName(), userDto,
                    TrackerDetailActivity.this, startDateTv.getText().toString(), endDateTv.getText().toString(), parameterDto.getUserParameterId());

            vitalListView.setAdapter(reportVitalAdapter);
        } else {
            charViewLl.setVisibility(View.GONE);
            listViewLl.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            mLayoutExportReport.setVisibility(View.GONE);
        }
    }

    private void openEndDateDialogPicker() {
        if (TrackerDetailActivity.this != null) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            int month = monthOfYear + 1;
                            String monthStr = month + "";
                            if (monthStr.trim().length() == 1) {
                                monthStr = "0" + monthStr;
                            }

                            String dayStr = dayOfMonth + "";
                            if (dayStr.trim().length() == 1) {
                                dayStr = "0" + dayStr;
                            }

                            endDateTv.setText(monthStr + "/" + dayStr + "/" + year);
                            AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_END_DATE_SLCT);
                            callGetReportAPI();
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            AppUtils.setDialogBoxButton(context, datePickerDialog);
            if (endDateTv.getText() != null && !endDateTv.getText().toString().equalsIgnoreCase(getString(R.string.end_date))) {
                AppUtils.setSelectedDate(datePickerDialog, endDateTv.getText().toString().trim());
            }
            datePickerDialog.show();
        }
    }

    private void openStartDateDialogPicker() {
        if (TrackerDetailActivity.this != null) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            int month = monthOfYear + 1;
                            String monthStr = month + "";
                            if (monthStr.trim().length() == 1) {
                                monthStr = "0" + monthStr;
                            }

                            String dayStr = dayOfMonth + "";
                            if (dayStr.trim().length() == 1) {
                                dayStr = "0" + dayStr;
                            }

                            startDateTv.setText(monthStr + "/" + dayStr + "/" + year);
                            AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_START_DATE_SLCT);
                            callGetReportAPI();
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            AppUtils.setDialogBoxButton(context, datePickerDialog);
            if (startDateTv.getText() != null && !startDateTv.getText().toString().equalsIgnoreCase(getString(R.string.start_date))) {
                AppUtils.setSelectedDate(datePickerDialog, startDateTv.getText().toString().trim());
            }
            datePickerDialog.show();
        }
    }


    private void setChartData(ReportDto reportDto) {

        try {
            if (reportDto != null && reportDto.getDates().size() > 0) {

                chartView.setVisibility(View.GONE);
                chartDataLl.setVisibility(View.VISIBLE);
                chartView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                mLayoutExportReport.setVisibility(View.VISIBLE);

                List<TrackConfigurationDto> reportDtoDatesList = reportDto.getDates();

                if (reportDtoDatesList != null && reportDtoDatesList.size() > 0) {

                    list1 = new ArrayList<>();
                    List<Object> subList1;

                    list2 = new ArrayList<>();
                    List<Object> subList2;


                    for (int i = 0; i < reportDtoDatesList.size(); i++) {

                        TrackConfigurationDto trackConfigurationDto = reportDtoDatesList.get(i);

                        subList1 = new ArrayList<>();
                        subList2 = new ArrayList<>();


                        if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {

                            subList1.add(trackConfigurationDto.getRecordedDate());
                            subList1.add(trackConfigurationDto.getMaxBaselineValue());
                            list1.add(subList1);

                            subList2.add(trackConfigurationDto.getRecordedDate());
                            subList2.add(trackConfigurationDto.getMinBaselineValue());
                            list2.add(subList2);

                           /* subList1.add(trackConfigurationDto.getRecordedDate());
                            subList1.add(trackConfigurationDto.getMinBaselineValue());
                            list1.add(subList1);

                            subList2.add(trackConfigurationDto.getRecordedDate());
                            subList2.add(trackConfigurationDto.getMaxBaselineValue());
                            list2.add(subList2);*/

                        } else {
                            subList2.add(trackConfigurationDto.getRecordedDate());
                            subList2.add(trackConfigurationDto.getMaxBaselineValue());
                            list2.add(subList2);
                        }
                    }

                    HIOptions options = new HIOptions();
                    HIChart chart = new HIChart();
                    chart.setType("line");
                    options.setChart(chart);

                    HITitle title = new HITitle();
                    title.setText("");
                    options.setTitle(title);

                    HISubtitle subtitle = new HISubtitle();
                    subtitle.setText("");
                    options.setSubtitle(subtitle);

                    HIXAxis xAxis = new HIXAxis();
                    xAxis.setType("category");
                    HITitle hiTitle = new HITitle();
                    hiTitle.setText("Date And Time");
                    xAxis.setTitle(hiTitle);
                    xAxis.setVisible(false);
                    options.setXAxis(new ArrayList<>(Collections.singletonList(xAxis)));

                    HIYAxis yaxis = new HIYAxis();
                    yaxis.setTitle(new HITitle());
                    yaxis.getTitle().setText(fromWhichDisease + " (" + reportDto.getDates().get(0).getMeasurementUnit() + ")");
                    options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));

                    HICredits hiCredits = new HICredits();
                    hiCredits.setEnabled(false);

                    options.setCredits(hiCredits);

                    HISeries hiSeries = new HISeries();
                    hiSeries.setName("");

                    HIExporting hiExporting = new HIExporting();
                    hiExporting.setEnabled(false);
                    options.setExporting(hiExporting);

                    HIPlotOptions plotoptions = new HIPlotOptions();
                    plotoptions.setSeries(new HISeries());
                    HISeries series = plotoptions.getSeries();

                    plotoptions.getSeries().setLabel(new HILabel());
                    plotoptions.getSeries().getLabel().setConnectorAllowed(false);
                    plotoptions.getSeries().setPoint(new HIPoint());
                    plotoptions.getSeries().getPoint().setEvents(new HIEvents());

                    plotoptions.getSeries().getPoint().getEvents().setClick(new HIFunction(
                            f -> {
                                setValue(f.getProperty("x"), f.getProperty("y"));
                            }, new String[]{"x", "y"}
                    ));


                    HITooltip hiTooltip = new HITooltip();
                    hiTooltip.setShared(true);
                    hiTooltip.setEnabled(false);
                    options.setTooltip(hiTooltip);

                    HIMarker hiMarker = new HIMarker();
                    hiMarker.setEnabled(true);
                    series.setMarker(hiMarker);

                    options.setPlotOptions(plotoptions);

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


                    if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {

                        HISeries line1 = new HISeries();
                        line1.setName(reportDto.getDates().get(0).getMaxBaselineDisplayName());
                        line1.setData(new ArrayList<>(list1));
                        line1.setColor(HIColor.initWithHexValue(chartOneColor));


                        HISeries line2 = new HISeries();

                        line2.setName(reportDto.getDates().get(0).getMinBaselineDisplayName());
                        line2.setData(new ArrayList<>(list2));
                        line2.setColor(HIColor.initWithHexValue(chartTwoColor));

                        options.setSeries(new ArrayList<>(Arrays.asList(line1, line2)));

                        chartView.setOptions(options);
                        chartView.reload();

                        /*HISeries line1 = new HISeries();
                        line1.setName(reportDto.getDates().get(0).getMinBaselineDisplayName());
                        line1.setData(new ArrayList<>(list1));
                        line1.setColor(HIColor.initWithHexValue(chartTwoColor));


                        HISeries line2 = new HISeries();

                        line2.setName(reportDto.getDates().get(0).getMaxBaselineDisplayName());
                        line2.setData(new ArrayList<>(list2));
                        line2.setColor(HIColor.initWithHexValue(chartOneColor));

                        options.setSeries(new ArrayList<>(Arrays.asList(line1, line2)));

                        chartView.setOptions(options);
                        chartView.reload();*/

                    } else {
                        HISeries line2 = new HISeries();
                        line2.setName(reportDto.getDates().get(0).getMeasurementUnit());
                        line2.setData(new ArrayList<>(list2));
                        line2.setColor(HIColor.initWithHexValue(chartOneColor));

                        options.setSeries(new ArrayList<>(Arrays.asList(line2)));

                        chartView.setOptions(options);
                        chartView.reload();
                    }
                }
            } else {
                chartDataLl.setVisibility(View.VISIBLE);
                chartView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                mLayoutExportReport.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setValue(Object pos, Object posy) {

        try {
            TrackerDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chartDataDisplayLL.setVisibility(View.VISIBLE);
                    chartDataDisplayLLSingle.setVisibility(View.GONE);

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                    double doublePos = Double.parseDouble(pos + "");
                    long longPos = Math.round(doublePos);

                    if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {

                        minHeaderLl.setVisibility(View.VISIBLE);
                        minValueLl.setVisibility(View.VISIBLE);
                        line1.setVisibility(View.VISIBLE);
//                        line2.setVisibility(View.VISIBLE);
                        maxHeader.setVisibility(View.VISIBLE);
                        maxDotView.setVisibility(View.VISIBLE);
                        minDotView.setVisibility(View.VISIBLE);

                        if (list2 != null && list2.size() > 0) {

                            List<Object> objectList = list2.get(Integer.parseInt(longPos + ""));
                            String xValue = objectList.get(0).toString();
                            String yValue = objectList.get(1).toString();

                            selectedDateTv.setText(AppUtils.getDateInReportFormatted(xValue));

                            TrackConfigurationDto trackConfigurationDto = rDto.getDates().get(Integer.parseInt(longPos + ""));

                            String maxHeaderStr = trackConfigurationDto.getMaxBaselineDisplayName();
                            Double maxBaselineValue = trackConfigurationDto.getMaxBaselineValue();
                            maxHeader.setText(maxHeaderStr);
                            maxValueTv.setText(maxBaselineValue + "");
                            maxValueUnit.setText(trackConfigurationDto.getMeasurementUnit());

                        }

                        if (list1 != null && list1.size() > 0) {

                            List<Object> objectList = list1.get(Integer.parseInt(longPos + ""));
                            String xValue = objectList.get(0).toString();
                            String yValue = objectList.get(1).toString();


                            TrackConfigurationDto trackConfigurationDto = rDto.getDates().get(Integer.parseInt(longPos + ""));

                            String minHeaderStr = trackConfigurationDto.getMinBaselineDisplayName();
                            minHeaderTv.setText(minHeaderStr);
                            Double minBaselineValue = trackConfigurationDto.getMinBaselineValue();
                            minValueTv.setText(minBaselineValue + "");
                            minValueUnit.setText(trackConfigurationDto.getMeasurementUnit());

                        }

                    } else {

                        minHeaderLl.setVisibility(View.GONE);
                        minValueLl.setVisibility(View.GONE);
                        line1.setVisibility(View.GONE);
                        chartDataDisplayLL.setVisibility(View.GONE);
                        chartDataDisplayLLSingle.setVisibility(View.VISIBLE);
//                        line2.setVisibility(View.GONE);

                        if (list2 != null && list2.size() > 0) {

                            List<Object> objectList = list2.get(Integer.parseInt(longPos + ""));
                            String xValue = objectList.get(0).toString();
                            String yValue = objectList.get(1).toString();

                            selectedDateTv.setText(AppUtils.getDateInReportFormatted(xValue));
                            mTextViewSelectedDateSingle.setText(AppUtils.getDateInReportFormatted(xValue));

                            TrackConfigurationDto trackConfigurationDto = rDto.getDates().get(Integer.parseInt(longPos + ""));

                            String maxHeaderStr = trackConfigurationDto.getMaxBaselineDisplayName();
                            maxHeader.setText(maxHeaderStr);
                            maxHeader.setVisibility(View.GONE);
                            maxDotView.setVisibility(View.GONE);

                            maxValueTv.setText(yValue);
                            mTextViewMaxValueSingle.setText(fromWhichDisease + " " + yValue);
                            mTextViewMaxValueSingle.setTextColor(getResources().getColor(R.color.black));
                            maxValueUnit.setText(trackConfigurationDto.getMeasurementUnit());
                            mTextViewMaxValueUnitSingle.setText(trackConfigurationDto.getMeasurementUnit());

                            if (fromWhichDisease.equalsIgnoreCase(Constants.Blood_Glucose)) {
                                mTextViewBloodGlucoseParameterMeasured.setVisibility(View.VISIBLE);
                                mTextViewBloodGlucoseParameterMeasured.setText(trackConfigurationDto.getMeasured());
                                mTextViewBloodGlucoseParameterMeasured.setTextColor(getResources().getColor(R.color.black));
                            } else {
                                mTextViewBloodGlucoseParameterMeasured.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEndDateIsGrater() {
        Long startDateLong = AppUtils.getDateInMillis(startDateTv.getText().toString());
        Long endDateLong = AppUtils.getDateInMillis(endDateTv.getText().toString());
        return (endDateLong >= startDateLong);
    }

    public void callAPI() {
        callGetReportAPI();
    }

    public void openTrackPage(ParameterDto parameterDto) {

    }

    private void showDeleteParameterDialog(final ParameterDto item, final int position) {
        if (userDto != null && context != null && item != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(TrackerDetailActivity.this);
            View promptView = layoutInflater.inflate(R.layout.delete_drug_dialog_box, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TrackerDetailActivity.this, R.style.AlertDialogTheme);
            alertDialogBuilder.setView(promptView);

            TextView title = promptView.findViewById(R.id.title);
            TextView msg = promptView.findViewById(R.id.msg);

            title.setText(context.getString(R.string.are_you_sure_you_want_to_remove_this) + " " + item.getName() + "?");
            userDto.getName();
            /*String msgStr = context.getString(R.string.this_action_will_delete) + context.getString(R.string.space)
                    + item.getName() + context.getString(R.string.space) + context.getString(R.string.and) +
                    context.getString(R.string.space) + context.getString(R.string.it_is_tracking_history_from) + context.getString(R.string.space) + userDto.getName() + context.getString(R.string.from_health_matrics);
            msg.setText(msgStr);*/

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(item != null) {
                              //  callDeleteParameterAPI(userDto.getId(), item.getUserParameterId(), position, item);
                                apiUpdateParameterAPI(userDto.getId(), item.getUserParameterId(), position, item);
                            }
                        }
                    });

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton(context.getString(R.string.cancel), (dialog, id) -> {
                        AppUtils.logCleverTapEvent(this,
                                Constants.CLICKED_ON_NO_BUTTON_ON_HEALTH_METRICS_DELETE_POPUP, null);
                        dialog.dismiss();
                    });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
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
                    Intent intent = new Intent(TrackerDetailActivity.this, LoginActivity.class);
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
    private void apiUpdateParameterAPI(Long userId,Long userParameterId, final int position, ParameterDto item){
        Log.i("checkmodrug", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.deleteParameterAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(parameterDto.getId()),token).enqueue(new Callback<APIMessageResponse>() {
            @Override
            public void onResponse(Call<APIMessageResponse> call, retrofit2.Response<APIMessageResponse> response) {
                Log.i("checkmodrug", "api login response 0 code = " + response.code());
                Log.i("checkmodrug", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.hideProgressBar(progress);

                    if (item.getId() == 414) {
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_HEART_RATE_START_DATE, "");
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_HEART_RATE_SWITCH, false + "");
                    } else if (item.getId() == 3) {
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_RESTING_HEART_SWITCH, "");
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_RESTING_HEART_SWITCH, false + "");
                    }
                    if (item.getId() == 225) {
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_WATER_START_DATE, "");
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_WATER_SWITCH, false + "");
                    } else if (item.getId() == 2) {
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_WEIGHT_START_DATE, "");
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_WEIGHT_SWITCH, false + "");
                    } else if (item.getId() == 272) {
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_SLEEP_START_DATE, "");
                        ApplicationPreferences.get().saveStringValue
                                (Constants.FITBIT_SLEEP_SWITCH, false + "");
                    }
                    ApplicationDB.get().deleteVitals(userId, item);
                    AppUtils.isDataChanged = true;
                    AppUtils.logEvent(Constants.CNDTN_SCR_VITAL_DELETED);
                    Intent intent = new Intent(TrackerDetailActivity.this, HomeActivity.class);
                    intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
                    intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
                    intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                    startActivity(intent);
                    finish();
                }else if(response.code() == 401){
                    refreshToken();
                }  else {
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
    private void callDeleteParameterAPI(Long userId, Long userParameterId, final int position, ParameterDto item) {
        if (progress != null) {
            progress.show();
        }
        Log.i("chekcurlcheck","url_log_track_export_report 2  = "+ APIUrls.get().getDeleteDiseaseParamaneter(userId, userParameterId));
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(TrackerDetailActivity.this);
        GenericRequest<APIMessageResponse> getDeleteRequest = new GenericRequest<>
                (Request.Method.DELETE, APIUrls.get().getDeleteDiseaseParamaneter(userId, userParameterId),
                        APIMessageResponse.class, null,
                        apiMessageResponse -> {
                            AppUtils.hideProgressBar(progress);

                            if (item.getId() == 414) {
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_HEART_RATE_START_DATE, "");
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_HEART_RATE_SWITCH, false + "");
                            } else if (item.getId() == 3) {
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_RESTING_HEART_SWITCH, "");
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_RESTING_HEART_SWITCH, false + "");
                            }
                            if (item.getId() == 225) {
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_WATER_START_DATE, "");
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_WATER_SWITCH, false + "");
                            } else if (item.getId() == 2) {
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_WEIGHT_START_DATE, "");
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_WEIGHT_SWITCH, false + "");
                            } else if (item.getId() == 272) {
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_SLEEP_START_DATE, "");
                                ApplicationPreferences.get().saveStringValue
                                        (Constants.FITBIT_SLEEP_SWITCH, false + "");
                            }
                            ApplicationDB.get().deleteVitals(userId, item);
                            AppUtils.isDataChanged = true;
                            AppUtils.logEvent(Constants.CNDTN_SCR_VITAL_DELETED);
                            Intent intent = new Intent(TrackerDetailActivity.this, HomeActivity.class);
                            intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
                            intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            finish();
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(TrackerDetailActivity.this, error, authExpiredCallback);
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                        });
        authExpiredCallback.setRequest(getDeleteRequest);
        ApiService.get().addToRequestQueue(getDeleteRequest);
    }

    private void callExportReportAPI() {
        HashMap<String, Object> healthMatricsName = new HashMap<>();
        healthMatricsName.put(Constants.HEALTH_MATRICS_NAME, fromWhichDisease);
        healthMatricsName.put("Start Date ", startDateTv.getText().toString());
        healthMatricsName.put("End Date ", endDateTv.getText().toString());
        AppUtils.logCleverTapEvent(this,
                Constants.CLICKED_ON_HEALTH_METRICS_EXPORT_REPORT_ON_REPORT_SCREEN, healthMatricsName);
        if (parameterDto.getUserParameterId() == null) {
            parameterDto.setUserParameterId(parameterDto.getId());//parameterDto.getId();
        }
        if (userDto != null) {

        /*    if (progress != null) {
                progress.show();
            }*/

            String url, orderString;

            orderString = Constants.DESCENDING;

            url = APIUrls.get().getReportDownload(userDto.getId(), parameterDto.getUserParameterId(),
                    AppUtils.getBackendFormattedDate(startDateTv.getText().toString()),
                    AppUtils.getBackendFormattedDate(endDateTv.getText().toString()), orderString);
            Log.i("chekcurlcheck","url_log_track_export_report 1  = "+ url);
            Log.e("url_log_track_export_report", url);

//            callDownloadReport(url);
            downloadFileUsingDownloadManager(url);
        } else {
            AppUtils.openSnackBar(mParentView, getString(R.string.end_date_grater_start_date));
        }
    }

    private void callDownloadReport(String url) {
        Log.v(TAG, "download() Method invoked ");
        if (!hasPermissions(TrackerDetailActivity.this, PERMISSIONS)) {
            Log.e(TAG, "download() Method DON'T HAVE PERMISSIONS ");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 112);
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "download() Method HAVE PERMISSIONS ");
            downloadFileUsingDownloadManager(url);
        }
        Log.e(TAG, "download() Method completed ");
    }

    private void downloadFileUsingDownloadManager(String url) {
//        AppUtils.hideProgressBar(progress);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Report")
                .setDescription("File is downloading...")
//                .setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS, fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.addRequestHeader("Authorization", "Bearer " + ApplicationPreferences.get().getUserDetails().getAccessToken());
        //Enqueue the download.The download will start automatically once the download manager is ready
        // to execute it and connectivity is available.
        downLoadId = downloadManager.enqueue(request);
        Toast.makeText(context, "Downloading Started..", Toast.LENGTH_SHORT).show();
        Log.e("report_location_log", " = " + Environment.DIRECTORY_DOWNLOADS);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
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
                callExportReportAPI();

                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED, "false");

            } else {
                // Permission request was denied.
                isFirstTimePermissionAsk = true;
                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED, "true");
                AppUtils.openSnackBar(mParentView, "Permission denied");
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
            Snackbar.make(mParentView, "Permissions required",
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                ActivityCompat.requestPermissions(this,
                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);

                isFirstTimePermissionAsk = true;

                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED, "true");
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