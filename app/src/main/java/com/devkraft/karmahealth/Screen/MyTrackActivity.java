package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.DoctorPrescriptionAdapter;
import com.devkraft.karmahealth.Adapter.MyTrackAdapter;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.MyTrackModel;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyTrackActivity extends AppCompatActivity {
    private RecyclerView mAvaDrRecyclerView;
    private List<MyTrackModel> mProductList = new ArrayList<>();

    private List<ParameterDto> healthList;
    MyTrackAdapter adapter;
    private ShimmerFrameLayout mShirmmer;
    RecyclerView.LayoutManager mLayoutManager;
    private DiseaseDto conditionListResponse;
    public static UserDto userDto;
    ImageView ivback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_track);



        init();
        setData();
        onClick();


    }
    public void onClick(){
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void  init(){
        ivback = (ImageView)findViewById(R.id.imageView3);
        /* mAvalaDoctor = (LinearLayout)findViewById(R.id.avdoctorll);*/
        mShirmmer = (ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);
        mAvaDrRecyclerView = (RecyclerView)findViewById(R.id.avadrRecyclerView);
        /*mAfternoonMorningMedicenRecyclerView = (RecyclerView)findViewById(R.id.afternoonMedicenRecyclerView);
        mNightMedicenRecyclerView = (RecyclerView)findViewById(R.id.nightMedicenRecyclerView);
        mlMedicen = (LinearLayout) findViewById(R.id.medicenll);*/
    }

    public void setData(){

        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto uDto = loginResponse.getUserDTO();
            if (uDto != null) {
                userDto = uDto;
                callGetDiseaseAPI(userDto);
            }
        }

/*
        MyTrackModel model = new MyTrackModel();

        model.setId("121");
        model.setIcon(String.valueOf(R.drawable.heart_icon));
        model.setTitle("रक्त-दाब");
        model.setRate("120/80");
        model.setRateName("mmHg");
        model.setTime("25 अगस्त 2022 - शाम 05:48");
        mProductList.add(model);

        MyTrackModel model1 = new MyTrackModel();
        model1.setId("123");
        model1.setIcon(String.valueOf(R.drawable.rate_green_icon));
        model1.setTitle("श्र्वसन दर");
        model1.setRate("10");
        model1.setRateName("श्र्वसन/मिनट");
        model1.setTime("15 अगस्त 2022 - शाम 05:48");
        mProductList.add(model1);

        MyTrackModel model2 = new MyTrackModel();

        model2.setId("121");
        model2.setIcon(String.valueOf(R.drawable.heart_icon));
        model2.setTitle("ह्रदय दर");
        model2.setRate("50");
        model2.setRateName("BPM");
        model2.setTime("21 अगस्त 2022 - शाम 05:48");
        mProductList.add(model2);

        MyTrackModel model3 = new MyTrackModel();

        model3.setId("121");
        model3.setIcon(String.valueOf(R.drawable.blood_pressure_icon));
        model3.setTitle("रक्त शर्करा");
        model3.setRate("90");
        model3.setRateName("mg/dL");
        model3.setTime("20 अगस्त 2022 - शाम 05:48");
        mProductList.add(model3);

        Log.i("checkmodeldata","model 0 = "+String.valueOf(mProductList.get(0).time));
        Log.i("checkmodeldata","model 1 = "+String.valueOf(mProductList.get(1).time));


        mAvaDrRecyclerView.setVisibility(View.GONE);
        mShirmmer.setVisibility(View.VISIBLE);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAvaDrRecyclerView.setVisibility(View.VISIBLE);
                mShirmmer.setVisibility(View.GONE);

            }
        }, 3000);*/

    }

    private void callGetDiseaseAPI(UserDto userDto) {
        if (userDto != null) {
            mAvaDrRecyclerView.setVisibility(View.GONE);
            mShirmmer.setVisibility(View.VISIBLE);

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<DiseaseDto> getDiseaseRequest = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().getAllDisease(userDto.getId()),
                            DiseaseDto.class, null,
                            diseaseDto -> {
                                Log.e("response_getDisease", " = " + new Gson().toJson(diseaseDto));
                                updateNoDataCount();
                                ApplicationDB.get().upsertDisease(userDto.getId(), diseaseDto);

                                if (diseaseDto != null) {
                                    Gson gson = new Gson();
                                    String conditionListing = gson.toJson(diseaseDto);
                                    ApplicationPreferences.get().saveStringValue(Constants.CONDITION_LIST, conditionListing);



                                    showTrackersList();
                                }

                            },
                            error -> {
                                updateNoDataCount();
                                String res = AppUtils.getVolleyError(this, error, authExpiredCallback);

                            });
            authExpiredCallback.setRequest(getDiseaseRequest);
            ApiService.get().addToRequestQueue(getDiseaseRequest);
        }
    }
    //region user Defined methodsD
    private void showTrackersList() {
        String conditionListString = ApplicationPreferences.get().getStringValue(Constants.CONDITION_LIST);
        Log.e("conditionListString_track"," = "+conditionListString);
        if (conditionListString != null && !conditionListString.matches("")) {
            Gson conditionList = new Gson();
            Type typeWebinar = new TypeToken<DiseaseDto>() {
            }.getType();
            conditionListResponse = conditionList.fromJson(conditionListString, typeWebinar);
            if (conditionListResponse != null) {
                if (!conditionListResponse.getTests().isEmpty() ||
                        !conditionListResponse.getVitals().isEmpty()) {
                    healthList = conditionListResponse.getVitals();

                    setAvailableDataCount();
                    show(healthList);

                } else {
                    updateNoDataCount();
                }
            }
        } else {
            updateNoDataCount();
        }
    }


    private void show(List<ParameterDto> mProductList){
        adapter = new MyTrackAdapter(mProductList,this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAvaDrRecyclerView.setLayoutManager(mLayoutManager);
        mAvaDrRecyclerView.setAdapter(adapter);
    }

    private void updateNoDataCount(){

        mAvaDrRecyclerView.setVisibility(View.GONE);
        mShirmmer.setVisibility(View.GONE);
    }

    private void setAvailableDataCount(){
        mAvaDrRecyclerView.setVisibility(View.VISIBLE);
        mShirmmer.setVisibility(View.GONE);
    }
}