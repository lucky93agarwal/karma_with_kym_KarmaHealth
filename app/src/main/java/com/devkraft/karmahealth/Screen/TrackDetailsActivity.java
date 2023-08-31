package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.DoctorPrescriptionAdapter;
import com.devkraft.karmahealth.Adapter.ReportVitalAdapter;
import com.devkraft.karmahealth.Adapter.TrackDetailsAdapter;
import com.devkraft.karmahealth.Model.AvailableDoctorListModel;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.ReportDto;
import com.devkraft.karmahealth.Model.TrackDetailsModel;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ExpandableHeightListView;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TrackDetailsActivity extends AppCompatActivity {

    public TextView tvapptitletv;
    public ImageView ivBack;
    public FloatingActionButton mfab;

    RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mTrackDetailsRV;


    private List<TrackDetailsModel> mProductList = new ArrayList<>();
    TrackDetailsAdapter adapter;



    private String endDate;
    private String startDate;
    private String userDtoStr;
    private String vitalDtoStr;
    private boolean isFromReport;
    private String fromWhichDisease;
    private int position;
    private ParameterDto parameterDto;

    private UserDto userDto;

    private TextView startDateTv, endDateTv;

    private Context context = TrackDetailsActivity.this;

    private ProgressDialogSetup progress;

    private Boolean isGraphView = true;

    private ReportDto rDto;


    private ReportVitalAdapter reportVitalAdapter;

    private ExpandableHeightListView vitalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);

        init();
     //   setData();
        onClick();



        getValueFromArguments(getIntent());
    }

    public void init(){
        mfab =(FloatingActionButton)findViewById(R.id.fab);
        ivBack = (ImageView)findViewById(R.id.imageView3);
        tvapptitletv = (TextView) findViewById(R.id.apptitletv);
        mTrackDetailsRV = (RecyclerView)findViewById(R.id.mytrackrv);

        startDateTv = (TextView) findViewById(R.id.startDateTv);
        endDateTv = (TextView) findViewById(R.id.endingdatetv);


        progress = ProgressDialogSetup.getProgressDialog(this);



        vitalListView = findViewById(R.id.vitalListView);

        vitalListView.setExpanded(true);
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
         //       mTextViewDeleteParameter.setText("Delete ");
         //       mTextViewAboutParameter.setText("About " + fromWhichDisease);
            }

            /*if (fromWhichDisease.equalsIgnoreCase(Constants.Weight) ||
                    fromWhichDisease.equalsIgnoreCase(Constants.Temperature)) {
                weightLl.setVisibility(View.VISIBLE);

                Context context = TrackDetailActivity.this;
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
            }*/

            if (userDtoStr != null && vitalDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
                parameterDto = gson.fromJson(vitalDtoStr, ParameterDto.class);

                if (isFromReport) {
                    startDateTv.setText(startDate);
                    endDateTv.setText(endDate);
                } else {
                    setDates();
                }
                Log.i("Login_response","callGetReportAPI  isFromReport = ");
                callGetReportAPI();
            }

            /*if (parameterDto.getInformationLink() != null) {
                viewAbout.setVisibility(View.VISIBLE);
                mLayoutAboutParameter.setVisibility(View.VISIBLE);
            } else {
                viewAbout.setVisibility(View.GONE);
                mLayoutAboutParameter.setVisibility(View.GONE);
            }*/
        }
    }
    private boolean isEndDateIsGrater() {
        Long startDateLong = AppUtils.getDateInMillis(startDateTv.getText().toString());
        Long endDateLong = AppUtils.getDateInMillis(endDateTv.getText().toString());
        return (endDateLong >= startDateLong);
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

                        String weight ;
                        if (isWeight) {
                            weight = "&unit=" + Constants.KGS;
                        } else {
                            weight = "&tempUnit=" + Constants.degree_c;
                        }
                     /*   if (weightSwitchCompat.isChecked()) {
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
                        }*/

                        url = APIUrls.get().getReport(userDto.getId(), parameterDto.getUserParameterId(),
                                AppUtils.getBackendFormattedDate(startDateTv.getText().toString()),
                                AppUtils.getBackendFormattedDate(endDateTv.getText().toString()), weight, orderString);
                    } else {
                        url = APIUrls.get().getReport(userDto.getId(), parameterDto.getUserParameterId(),
                                AppUtils.getBackendFormattedDate(startDateTv.getText().toString()),
                                AppUtils.getBackendFormattedDate(endDateTv.getText().toString()), orderString);
                    }
                    Log.i("Login_response","callGetReportAPI  url = "+url.toString());
                    final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(context);
                    GenericRequest<ReportDto> updateDiseaseParameter = new GenericRequest<ReportDto>
                            (Request.Method.GET, url,
                                    ReportDto.class, null,
                                    reportDto -> {
                                        AppUtils.hideProgressBar(progress);
                                        rDto = reportDto;

                                            performListView();

                                    },
                                    error -> {
                                        authExpiredCallback.hideProgressBar();
                                        AppUtils.hideProgressBar(progress);
                                        String res = AppUtils.getVolleyError(context, error, authExpiredCallback);
                                     /*   AppUtils.openSnackBar(mParentView, res);*/
                                    });
                    authExpiredCallback.setRequest(updateDiseaseParameter);
                    ApiService.get().addToRequestQueue(updateDiseaseParameter);
                }
            } else {
                Toast.makeText(context, R.string.end_date_grater_start_date, Toast.LENGTH_SHORT).show();
            //    AppUtils.openSnackBar(mParentView, getString(R.string.end_date_grater_start_date));
            }
        }
    }

    private void performListView() {
        if (context != null) {
            updateAdapter();

        }
    }


    private void updateAdapter(){
//        reportVitalAdapter = new ReportVitalAdapter(context, R.layout.recyclerview_track_details,
//                rDto.getDates(), parameterDto.getId(), parameterDto.getName(), userDto,
//                TrackDetailsActivity .this, startDateTv.getText().toString(),
//                endDateTv.getText().toString(), parameterDto.getUserParameterId());


        vitalListView.setAdapter(reportVitalAdapter);
    }
    public void callAPI() {
        callGetReportAPI();
    }
    private void setDates() {
        Log.i("Login_response","start date = "+AppUtils.getSevenDaysBackDate());
        Log.i("Login_response","end date = "+AppUtils.getTodayDate());
        startDateTv.setText(AppUtils.getSevenDaysBackDate());
        endDateTv.setText(AppUtils.getTodayDate());
    }



    public void onClick(){
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new  Intent(TrackDetailsActivity.this, TaskdetalsEditActivity.class);
                intent.putExtra("title",getIntent().getStringExtra("title"));
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void setData(){
        tvapptitletv.setText(getIntent().getStringExtra("title")+" को ट्रैक करें");


        TrackDetailsModel model = new TrackDetailsModel();

        model.setId("121");
        model.setMedicenOne("सिस्टोलिक (मिमी एचजी)");
        model.setMedicenTwo("डायस्टोलिक (मिमी एचजी)");

        model.setRateOne("0.0");
        model.setRateTwo("mmHg");

        model.setRateNameOne("75.0");
        model.setRateNameTwo("mmHg");

        model.setTime("बुधवार, 4 अगस्त 2022 . शाम 05:48");
        mProductList.add(model);

        TrackDetailsModel model1 = new TrackDetailsModel();
        model1.setId("121");
        model1.setMedicenOne("सिस्टोलिक (मिमी एचजी)");
        model1.setMedicenTwo("डायस्टोलिक (मिमी एचजी)");

        model1.setRateOne("0.0");
        model1.setRateTwo("mmHg");

        model1.setRateNameOne("75.0");
        model1.setRateNameTwo("mmHg");

        model1.setTime("बुधवार, 4 अगस्त 2022 . शाम 05:48");
        mProductList.add(model1);


        adapter = new TrackDetailsAdapter(mProductList,this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrackDetailsRV.setLayoutManager(mLayoutManager);
        mTrackDetailsRV.setAdapter(adapter);
    }
}