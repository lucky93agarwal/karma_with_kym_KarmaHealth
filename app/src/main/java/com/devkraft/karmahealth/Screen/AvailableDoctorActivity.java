package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Adapter.AvailableDoctorAdapter;
import com.devkraft.karmahealth.Adapter.MedicenAdapter;
import com.devkraft.karmahealth.Model.AvailableDoctorListModel;
import com.devkraft.karmahealth.Model.RetrofitDrAvailabilityModel;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.RetrofitPatientModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.retrofit.ServiceGenerator;
import com.devkraft.karmahealth.retrofit.UserService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AvailableDoctorActivity extends AppCompatActivity {
    private RecyclerView mAvaDrRecyclerView;
    private List<RetrofitPatientModel> mProductList = new ArrayList<>();
    AvailableDoctorAdapter adapter;
    private ShimmerFrameLayout mShirmmer;
    RecyclerView.LayoutManager mLayoutManager;
    private EditText etsearch;
    ImageView ivback;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private TextView tvnodatatv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_doctor);


        init();
       // setData();
        onClick();

        api();
    }

    public void api(){

        mAvaDrRecyclerView.setVisibility(View.GONE);
        mShirmmer.setVisibility(View.VISIBLE);

        tvnodatatv.setVisibility(View.GONE);
        RetrofitPatientIDRequest request = new RetrofitPatientIDRequest();
        request.PatientId = sharedPreferences.getString("karmaPid","1");
        Log.i("checkmodeldata","api request = "+ new Gson().toJson(request));

        HashMap<String,String> header =new HashMap<String, String>();
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        header.put("seckey","znob1zj8z9zgdebrobd8y3apdtnj9zgo7lv55f5uh9mpx724gl6900ssdinuht9kwxanr1zf1sgjn848o1m1cnkcxbamwfzulo45w34npt4k3ubude60ipslql1e8fs1lv6yx0f551nydq9r2d0qjq1fzxtu1ir73wjbc");
        header.put("Authorization",token);


        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.availability(request,header).enqueue(new Callback<RetrofitDrAvailabilityModel>() {
            @Override
            public void onResponse(Call<RetrofitDrAvailabilityModel> call, Response<RetrofitDrAvailabilityModel> response) {
                Log.i("checkmodeldata","api response 0 code = "+ response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata","api response = "+ response.body().message);
                    Log.i("checkmodeldata","api response 2 = "+ new Gson().toJson(response.body()));
                    if(response.body().data.size()>0){
                        mProductList.addAll(response.body().data);
                        tvnodatatv.setVisibility(View.GONE);
                        setData();
                    }else {
                        mAvaDrRecyclerView.setVisibility(View.GONE);
                        mShirmmer.setVisibility(View.GONE);
                        tvnodatatv.setText("डॉक्टर की पर्ची उपलब्ध नहीं है।");
                        tvnodatatv.setVisibility(View.VISIBLE);
                        Toast.makeText(AvailableDoctorActivity.this, "डॉक्टर उपलब्ध नहीं है", Toast.LENGTH_SHORT).show();
                    }

                }

                else
                {
                    Log.i("checkmodeldata","api response 1 code = "+ response.code());

                }
            }

            @Override
            public void onFailure(Call<RetrofitDrAvailabilityModel> call, Throwable t) {
                Log.i("checkmodeldata","api error message response  = "+ t.getMessage());
            }
        });
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
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();

        tvnodatatv = (TextView)findViewById(R.id.nodatatv);
        ivback = (ImageView)findViewById(R.id.imageView3);

        etsearch = (EditText)findViewById(R.id.searchet);
       /* mAvalaDoctor = (LinearLayout)findViewById(R.id.avdoctorll);*/
        mShirmmer = (ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);
        mAvaDrRecyclerView = (RecyclerView)findViewById(R.id.avadrRecyclerView);
        /*mAfternoonMorningMedicenRecyclerView = (RecyclerView)findViewById(R.id.afternoonMedicenRecyclerView);
        mNightMedicenRecyclerView = (RecyclerView)findViewById(R.id.nightMedicenRecyclerView);
        mlMedicen = (LinearLayout) findViewById(R.id.medicenll);*/

        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mProductList.size()>0){
                    filter(s.toString());
                }
            }
        });
    }
    private void filter(String text) {
        ArrayList<RetrofitPatientModel> filteredList = new ArrayList<>();
        for (RetrofitPatientModel item : mProductList) {
            String Nickname = item.dname;
            if (Nickname.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if(filteredList.size()==0){
            mAvaDrRecyclerView.setVisibility(View.GONE);
            mShirmmer.setVisibility(View.GONE);
            tvnodatatv.setText("डॉक्टर उपलब्ध नहीं है।");
            tvnodatatv.setVisibility(View.VISIBLE);
        }else {
            mAvaDrRecyclerView.setVisibility(View.VISIBLE);
            mShirmmer.setVisibility(View.GONE);
            tvnodatatv.setText("डॉक्टर उपलब्ध नहीं है।");
            tvnodatatv.setVisibility(View.GONE);
        }
        adapter.filterList(filteredList);
    }
    public void setData(){
//        AvailableDoctorListModel model = new AvailableDoctorListModel();
//
//        model.setId("121");
//        model.setmName("डॉ एकता कौर");
//        model.setDepartment("सामान्य चिकित्सक");
//        model.setTime("09:30 सुबह - 02:00 सुबह");
//        mProductList.add(model);
//
//        AvailableDoctorListModel model1 = new AvailableDoctorListModel();
//        model1.setId("123");
//        model1.setmName("डॉ प्रकाश नाले");
//        model1.setDepartment("सामान्य चिकित्सक");
//        model1.setTime("09:30 सुबह - 02:00 सुबह");
//        mProductList.add(model1);
//
//        AvailableDoctorListModel model2 = new AvailableDoctorListModel();
//
//        model2.setId("121");
//        model2.setmName("डॉ दीपक मिश्रा");
//        model2.setDepartment("सामान्य चिकित्सक");
//        model2.setTime("09:30 सुबह - 02:00 सुबह");
//        mProductList.add(model2);
//
//        AvailableDoctorListModel model3 = new AvailableDoctorListModel();
//
//        model3.setId("121");
//        model3.setmName("डॉ प्रिया राज");
//        model3.setDepartment("सामान्य चिकित्सक");
//        model3.setTime("09:30 सुबह - 02:00 सुबह");
//        mProductList.add(model2);
//
//        AvailableDoctorListModel model4 = new AvailableDoctorListModel();
//
//        model4.setId("121");
//        model4.setmName("डॉ नीरजा पाटिल");
//        model4.setDepartment("सामान्य चिकित्सक");
//        model4.setTime("09:30 सुबह - 02:00 सुबह");
//        mProductList.add(model4);
//
//        AvailableDoctorListModel model5 = new AvailableDoctorListModel();
//
//        model5.setId("121");
//        model5.setmName("डॉ अक्षय वैध");
//        model5.setDepartment("सामान्य चिकित्सक");
//        model5.setTime("09:30 सुबह - 02:00 सुबह");
//        mProductList.add(model5);
//        Log.i("checkmodeldata","model 0 = "+String.valueOf(mProductList.get(0).mName));
//        Log.i("checkmodeldata","model 1 = "+String.valueOf(mProductList.get(1).mName));
        Collections.sort(mProductList, new Comparator<RetrofitPatientModel>() {
            @Override
            public int compare(RetrofitPatientModel s1, RetrofitPatientModel s2) {
                return s1.dname.compareToIgnoreCase(s2.dname);
            }
        });
        adapter = new AvailableDoctorAdapter(mProductList,this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAvaDrRecyclerView.setLayoutManager(mLayoutManager);
        mAvaDrRecyclerView.setAdapter(adapter);



        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAvaDrRecyclerView.setVisibility(View.VISIBLE);
                mShirmmer.setVisibility(View.GONE);

            }
        }, 3000);

    }
}