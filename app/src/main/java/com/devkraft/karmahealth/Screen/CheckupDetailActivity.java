package com.devkraft.karmahealth.Screen;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.TrackCheckupDetailsListAdapter;
import com.devkraft.karmahealth.Model.ReportDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import com.devkraft.karmahealth.Adapter.CheckupsListAdapter;
import com.devkraft.karmahealth.Adapter.HealthMatricsListAdapter;
import com.devkraft.karmahealth.Adapter.VaccinesListAdapter;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AppsDevicesActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;



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



public class CheckupDetailActivity extends AppCompatActivity implements RvClickListener {

    private View mParentView;
    private TextView mTextViewTrackNow;
    private ProgressDialogSetup progress;
    private LinearLayout mLayoutEmptyView;
    private TextView mTextViewAboutCheckup;
    private TextView mTextViewDeleteCheckup;
    private LinearLayout mLayoutAboutCheckup;
    private LinearLayout mLayoutExportReport;
    private LinearLayout mLayoutDeleteCheckup;
    private RecyclerView mRecyclerviewCheckups;
    private LinearLayout mLayoutConfigureCheckup;

    private TextView endDateTv;
    private TextView startDateTv;
    private LinearLayout endDateLl;
    private LinearLayout startDateLl;

    private View viewAbout;
    private UserDto userDto;
    private MenuItem menuItem;
    private String fromWhereStr;
    private ReportDto mReportDto;
    private String fromWhichDisease;
    private ParameterDto parameterDto;
    private TrackCheckupDetailsListAdapter adapter;
    private String TAG = "CheckupDetailActivity";
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String fileName;

    private DownloadManager downloadManager;
    private int PERMISSION_REQUEST_CODE = 200;
    private boolean isFirstTimePermissionAsk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup_detail);
        setupToolbar();
        initializeIds();
        setOnclickEvents();
        getIntentValues(getIntent());
        initializeDownloadManager();
//        Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
        isFirstTimePermissionAsk = Boolean.parseBoolean(ApplicationPreferences.get().getStringValue(Constants.IS_PERMISSION_GRANTED));
    }

    private void initializeDownloadManager() {
        downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        fileName="Report.pdf";
    }

    private void getIntentValues(Intent intent) {
        if (intent != null) {

            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            fromWhichDisease = intent.getStringExtra(Constants.WHICH_TEST);
            String parameterDtoStr = intent.getStringExtra(Constants.PARAMETER_DTO);
            fromWhereStr = intent.getStringExtra(Constants.FROM_WHERE);
            String fromHomeFragment = intent.getStringExtra(Constants.FROM_HOME_FRAGMENT);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(fromWhichDisease);
            }

            if (userDtoStr != null && parameterDtoStr != null) {
                Gson gson = new Gson();
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(parameterDtoStr, ParameterDto.class);
            }

            if (parameterDto.getInformationLink() != null) {
                viewAbout.setVisibility(View.VISIBLE);
                mLayoutAboutCheckup.setVisibility(View.VISIBLE);
            } else {
                viewAbout.setVisibility(View.GONE);
                mLayoutAboutCheckup.setVisibility(View.GONE);
            }

            setTexts();
            callGetReportAPI();
        }
    }

    private void setTexts() {
        endDateTv.setText(AppUtils.getTodayDate());
        mTextViewDeleteCheckup.setText("Delete ");
        startDateTv.setText(AppUtils.getSevenDaysBackDate());
        mTextViewAboutCheckup.setText("About " + fromWhichDisease);
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

    private void initializeIds() {
        viewAbout = findViewById(R.id.view_about);
        mLayoutEmptyView = findViewById(R.id.emptyView);
        mTextViewAboutCheckup = findViewById(R.id.about_checkup);
        mTextViewTrackNow = findViewById(R.id.textview_track_now);
        mTextViewDeleteCheckup = findViewById(R.id.delete_checkup);

        endDateLl = findViewById(R.id.endDateLl);
        endDateTv = findViewById(R.id.endDateTv);
        startDateLl = findViewById(R.id.startDateLl);
        startDateTv = findViewById(R.id.startDateTv);

        progress = ProgressDialogSetup.getProgressDialog(this);
        mParentView = findViewById(R.id.parent_view_checkup_detail);
        mLayoutAboutCheckup = findViewById(R.id.layout_about_checkup);
        mLayoutExportReport = findViewById(R.id.layout_export_report);
        mLayoutDeleteCheckup = findViewById(R.id.layout_delete_checkup);
        mLayoutConfigureCheckup = findViewById(R.id.layout_configure_checkup);
        mRecyclerviewCheckups = findViewById(R.id.recyclerview_checkup_details);
    }

    private void setOnclickEvents() {
        mLayoutDeleteCheckup.setOnClickListener(v -> deleteCheckup());
        mLayoutAboutCheckup.setOnClickListener(v -> showAboutCheckup());
        endDateLl.setOnClickListener(view -> openEndDateDialogPicker());
        mLayoutExportReport.setOnClickListener(v -> startDownloadingFile());
        startDateLl.setOnClickListener(view -> openStartDateDialogPicker());
        mTextViewTrackNow.setOnClickListener(v -> openTrackCheckupActivity());
        mLayoutConfigureCheckup.setOnClickListener(v -> openConfigureCheckupActivity());
    }

    private void openStartDateDialogPicker() {
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

                    startDateTv.setText(monthStr + "/" + dayStr + "/" + year);
                    AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_START_DATE_SLCT);
                    callGetReportAPI();
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        AppUtils.setDialogBoxButton(this, datePickerDialog);
        if (startDateTv.getText() != null && !startDateTv.getText().toString().equalsIgnoreCase(getString(R.string.start_date))) {
            AppUtils.setSelectedDate(datePickerDialog, startDateTv.getText().toString().trim());
        }
        datePickerDialog.show();
    }

    private void openEndDateDialogPicker() {
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

                    endDateTv.setText(monthStr + "/" + dayStr + "/" + year);
                    AppUtils.logEvent(Constants.CNDTN_VITAL_SCR_REPORT_END_DATE_SLCT);
                    callGetReportAPI();
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        AppUtils.setDialogBoxButton(this, datePickerDialog);
        if (endDateTv.getText() != null && !endDateTv.getText().toString().equalsIgnoreCase(getString(R.string.end_date))) {
            AppUtils.setSelectedDate(datePickerDialog, endDateTv.getText().toString().trim());
        }
        datePickerDialog.show();
    }

    private void showAboutCheckup() {
        if (parameterDto != null) {
            Intent intent = new Intent(CheckupDetailActivity.this, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, parameterDto.getName());
            intent.putExtra(Constants.URL, parameterDto.getInformationLink());
            startActivity(intent);
        }
    }

    private void deleteCheckup() {
        showDeletCheckupDialogBox(parameterDto);
    }

    private void showDeletCheckupDialogBox(final ParameterDto item) {
        if (userDto != null && item != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.delete_drug_dialog_box, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            alertDialogBuilder.setView(promptView);

            TextView title = promptView.findViewById(R.id.title);
            TextView msg = promptView.findViewById(R.id.msg);

            title.setText(this.getString(R.string.remove_checkup) + " " + item.getName() + " ?");

            String msgStr = this.getString(R.string.deleting_this_checkup_text);
            msg.setText(msgStr);

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton(this.getString(R.string.delete), (dialog, id) -> callDeleteParameterAPI(userDto.getId(),item.getUserParameterId(),item));

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton(this.getString(R.string.cancel), (dialog, id) -> dialog.dismiss());

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void callDeleteParameterAPI(Long userId, Long userParameterId, ParameterDto item) {
        if(progress != null){
            progress.show();
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> getDeleteRequest = new GenericRequest<>
                (Request.Method.DELETE, APIUrls.get().getDeleteDiseaseParamaneter(userId, userParameterId),
                        APIMessageResponse.class, null,
                        apiMessageResponse -> {
                            AppUtils.hideProgressBar(progress);
                            ApplicationDB.get().deleteTest(userId, item);
                            AppUtils.isDataChanged = true;
                            AppUtils.logEvent(Constants.CNDTN_SCR_TEST_DELETED);
                            Intent intent = new Intent(CheckupDetailActivity.this, HomeActivity.class);
                            intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
                            intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            finish();
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(CheckupDetailActivity.this, error, authExpiredCallback);
                            Toast.makeText(CheckupDetailActivity.this, res, Toast.LENGTH_SHORT).show();
                        });
        authExpiredCallback.setRequest(getDeleteRequest);
        ApiService.get().addToRequestQueue(getDeleteRequest);
    }

    private void openConfigureCheckupActivity() {
        Intent intent = new Intent(CheckupDetailActivity.this, TestConfigureActivity.class);
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
        intent.putExtra(Constants.WHICH_TEST, fromWhichDisease);
        intent.putExtra(Constants.FROM_WHERE, Constants.TEST);
        startActivity(intent);
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
            openTrackCheckupActivity();
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra(Constants.IS_FROM_TRACK_DETAIL,true);
            intent.putExtra(Constants.USER_DTO,new Gson().toJson(userDto));
            startActivity(intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTrackCheckupActivity() {
        Intent intent = new Intent(CheckupDetailActivity.this, TrackCheckupActivity.class);
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(parameterDto));
        intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callGetReportAPI();
    }

    private boolean isEndDateIsGrater() {
        Long startDateLong = AppUtils.getDateInMillis(startDateTv.getText().toString());
        Long endDateLong = AppUtils.getDateInMillis(endDateTv.getText().toString());
        return (endDateLong >= startDateLong);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progress != null)
            progress.dismiss();
    }

    private void callGetReportAPI() {
        if (parameterDto.getUserParameterId() == null) {
            parameterDto.setUserParameterId(parameterDto.getId());
        }
        if(isEndDateIsGrater()) {
            if (userDto != null && parameterDto != null) {

                if (progress != null) {
                    progress.show();
                }


                String orderString = Constants.DESCENDING;

                String url = APIUrls.get().getReport(userDto.getId(), parameterDto.getUserParameterId(),
                        AppUtils.getBackendFormattedDate(startDateTv.getText().toString()),
                        AppUtils.getBackendFormattedDate(endDateTv.getText().toString()), orderString);

                Log.e("url", url);

                final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
                GenericRequest<ReportDto> updateDiseaseParameter = new GenericRequest<>
                        (Request.Method.GET, url,
                                ReportDto.class, null,
                                reportDto -> {
                                    AppUtils.hideProgressBar(progress);
                                    mReportDto = reportDto;
                                    setCheckUpListView();
                                    Log.e("data", new Gson().toJson(reportDto));
                                },
                                error -> {
                                    authExpiredCallback.hideProgressBar();
                                    AppUtils.hideProgressBar(progress);
                                    String res = AppUtils.getVolleyError(CheckupDetailActivity.this, error, authExpiredCallback);
                                    AppUtils.openSnackBar(mParentView, res);
                                });
                authExpiredCallback.setRequest(updateDiseaseParameter);
                ApiService.get().addToRequestQueue(updateDiseaseParameter);
            } else {
                AppUtils.openSnackBar(mParentView, getString(R.string.end_date_grater_start_date));
            }
        }
    }

    private void setCheckUpListView() {
        if (!mReportDto.getDates().isEmpty()) {
            mLayoutEmptyView.setVisibility(View.GONE);
            mLayoutExportReport.setVisibility(View.VISIBLE);
            mRecyclerviewCheckups.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerviewCheckups.setLayoutManager(linearLayoutManager);
            adapter = new TrackCheckupDetailsListAdapter(this, mReportDto.getDates());
            mRecyclerviewCheckups.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            mLayoutEmptyView.setVisibility(View.VISIBLE);
            mLayoutExportReport.setVisibility(View.GONE);
            mRecyclerviewCheckups.setVisibility(View.GONE);
        }
    }

    @Override
    public void rv_click(int position, int value, String key) {
        if(key.equalsIgnoreCase(Constants.EDIT_TRACK_CHECK_UP)) {
            openEditTrackCheckup(position);
        } else if (key.equalsIgnoreCase(Constants.DELETE_CHECKUP_TRACK)) {
            deleteTrackCheckupEntry(position);
        }
    }

    private void deleteTrackCheckupEntry(int position) {
        showDialogForDeletingEntry(position);
    }

    private void showDialogForDeletingEntry(int position) {
        if (userDto != null && mReportDto.getDates().get(position) != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.delete_drug_dialog_box, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            alertDialogBuilder.setView(promptView);

            TextView title = promptView.findViewById(R.id.title);
            TextView msg = promptView.findViewById(R.id.msg);

            title.setText(this.getString(R.string.are_you_sure));

            String msgStr = this.getString(R.string.this_str) + this.getString(R.string.space)
                    + parameterDto.getName() + this.getString(R.string.space) + this.getString(R.string.entry_will_be_deleted);

            msg.setText(msgStr);

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton(this.getString(R.string.ok), (dialog, id) -> callDeleteCheckUpFrommListAPI(mReportDto.getDates().get(position).getUserParameterTrackingId(),
                            position));

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton(this.getString(R.string.cancel), (dialog, id) -> dialog.dismiss());

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void callDeleteCheckUpFrommListAPI(Long userParameterTrackingId, int position) {
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> drugTakenRequest = new GenericRequest<>
                (Request.Method.DELETE, APIUrls.get().deleteVital(userDto.getId(), userParameterTrackingId),
                        APIMessageResponse.class, null,
                        apiMessageResponse -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.isDataChanged = true;
                            updateListItems(position);
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            String res = AppUtils.getVolleyErrorForNoti(CheckupDetailActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(mParentView, res);
                        });
        authExpiredCallback.setRequest(drugTakenRequest);
        ApiService.get().addToRequestQueue(drugTakenRequest);
    }

    private void updateListItems(int position) {
        mReportDto.getDates().remove(position);
        adapter.notifyDataSetChanged();

        if (mReportDto.getDates().isEmpty()) {
            mLayoutEmptyView.setVisibility(View.VISIBLE);
            mLayoutExportReport.setVisibility(View.GONE);
            mRecyclerviewCheckups.setVisibility(View.GONE);
        }
    }

    private void openEditTrackCheckup(int position) {
        Intent intent = new Intent(CheckupDetailActivity.this, TrackCheckupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.USER_DTO, Constants.GSON.toJson(userDto));
        intent.putExtra(Constants.PARAMETER_DTO, Constants.GSON.toJson(parameterDto));
        intent.putExtra(Constants.WHICH_DISEASE, fromWhichDisease);
        intent.putExtra(Constants.TRACK_CONFIG_DTO, Constants.GSON.toJson(mReportDto.getDates().get(position)));
        intent.putExtra(Constants.IS_VITAL_EDIT, true);
        intent.putExtra(Constants.USER_PARA_TRACKING_ID, mReportDto.getDates().get(position).getUserParameterTrackingId());
        intent.putExtra(Constants.IS_FROM_REPORT, true);
        startActivity(intent);
    }

    private void callExportReportAPI() {
        if (parameterDto.getUserParameterId() == null) {
            parameterDto.setUserParameterId(parameterDto.getId());
        }
        if (userDto != null ) {

          /*  if (progress != null) {
                progress.show();
            }
*/
            String url;

            String orderString = Constants.DESCENDING;

            url = APIUrls.get().getReportDownload(userDto.getId(), parameterDto.getUserParameterId(),
                    AppUtils.getBackendFormattedDate(AppUtils.getSevenDaysBackDate()),
                    AppUtils.getBackendFormattedDate(AppUtils.getTodayDate()), orderString);

            Log.e("url", url);

//            callDownloadReport(url);
            downloadUsingManager(url);

        } else {
            AppUtils.openSnackBar(mParentView, getString(R.string.end_date_grater_start_date));
        }
    }

    private void callDownloadReport(String url) {
        Log.v(TAG, "download() Method invoked ");

        if (!hasPermissions(CheckupDetailActivity.this, PERMISSIONS)) {

            Log.e(TAG, "download() Method DON'T HAVE PERMISSIONS ");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 112);
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

        } else {
            Log.e(TAG, "download() Method HAVE PERMISSIONS ");

            downloadUsingManager(url);
        }
        Log.e(TAG, "download() Method completed ");
    }

    private void downloadUsingManager(String url) {
//        AppUtils.hideProgressBar(progress);
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Report")
                .setDescription("File is downloading...")
//                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.addRequestHeader("Authorization", "Bearer " + ApplicationPreferences.get().getUserDetails().getAccessToken());
        //Enqueue the download.The download will start automatically once the download manager is ready
        // to execute it and connectivity is available.
        long downLoadId = downloadManager.enqueue(request);
        Toast.makeText(CheckupDetailActivity.this, "Downloading Started..", Toast.LENGTH_SHORT).show();
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