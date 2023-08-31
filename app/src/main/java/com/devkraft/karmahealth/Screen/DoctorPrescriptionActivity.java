package com.devkraft.karmahealth.Screen;
import com.devkraft.karmahealth.Adapter.AvailableDoctorAdapter;
import com.devkraft.karmahealth.Adapter.DoctorPrescriptionAdapter;
import com.devkraft.karmahealth.Model.AvailableDoctorListModel;
import com.devkraft.karmahealth.Model.RetrofitDataPrescriptionsModel;
import com.devkraft.karmahealth.Model.RetrofitDrAvailabilityModel;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.RetrofitPatientModel;
import com.devkraft.karmahealth.Model.RetrofitPrescriptionsModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.retrofit.ServiceGenerator;
import com.devkraft.karmahealth.retrofit.UserService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorPrescriptionActivity extends AppCompatActivity {
    private RecyclerView mAvaDrRecyclerView;
    private List<RetrofitDataPrescriptionsModel> mProductList = new ArrayList<>();
    DoctorPrescriptionAdapter adapter;
    private ShimmerFrameLayout mShirmmer;
    RecyclerView.LayoutManager mLayoutManager;
    EditText eteditseet;
    ImageView ivback;

    private TextView tvnodatatv;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription);



        init();
     //   setData();
        onClick();


    }

   /* @Override
    protected void onStart() {
        super.onStart();
        api();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        api();
    }

    public void onClick(){
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        eteditseet.addTextChangedListener(new TextWatcher() {
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
    public void api(){


        mAvaDrRecyclerView.setVisibility(View.GONE);
        mShirmmer.setVisibility(View.VISIBLE);

        tvnodatatv.setVisibility(View.GONE);

        RetrofitPatientIDRequest request = new RetrofitPatientIDRequest();
        request.PatientId = sharedPreferences.getString("karmaPid","134388");
        Log.i("prescriptionsdata","api request = "+ new Gson().toJson(request));
        UserService service = ServiceGenerator.createService(UserService.class, null, null);

        HashMap<String,String> header =new HashMap<String, String>();
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        header.put("seckey","znob1zj8z9zgdebrobd8y3apdtnj9zgo7lv55f5uh9mpx724gl6900ssdinuht9kwxanr1zf1sgjn848o1m1cnkcxbamwfzulo45w34npt4k3ubude60ipslql1e8fs1lv6yx0f551nydq9r2d0qjq1fzxtu1ir73wjbc");
        header.put("Authorization",token);

        service.prescriptions(request,header).enqueue(new Callback<RetrofitPrescriptionsModel>() {
            @Override
            public void onResponse(Call<RetrofitPrescriptionsModel> call, Response<RetrofitPrescriptionsModel> response) {
                Log.i("checkmodeldata","api response 0 code = "+ response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata","api response = "+ response.body().message);
                    Log.i("prescriptionsdata","api response = "+ new Gson().toJson(response.body()));
                    if(response.body().data.size()>0){
                        if(mProductList.size()>0){
                            mProductList.clear();
                        }
                        mProductList.addAll(response.body().data);
                        tvnodatatv.setVisibility(View.GONE);
                        setData();
                    }else {
                        mAvaDrRecyclerView.setVisibility(View.GONE);
                        mShirmmer.setVisibility(View.GONE);
                        tvnodatatv.setText("डॉक्टर की पर्ची उपलब्ध नहीं है।");
                        tvnodatatv.setVisibility(View.VISIBLE);
                        Toast.makeText(DoctorPrescriptionActivity.this, "डॉक्टर की पर्ची उपलब्ध नहीं है।", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Log.i("checkmodeldata","api response 1 code = "+ response.code());

                }
            }

            @Override
            public void onFailure(Call<RetrofitPrescriptionsModel> call, Throwable t) {
                Log.i("checkmodeldata","api error message response  = "+ t.getMessage());
            }
        });
    }
    public void  init(){

        tvnodatatv = (TextView)findViewById(R.id.nodatatv);
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();

        eteditseet = (EditText)findViewById(R.id.editseet);
        ivback = (ImageView)findViewById(R.id.imageView3);
        /* mAvalaDoctor = (LinearLayout)findViewById(R.id.avdoctorll);*/
        mShirmmer = (ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);
        mAvaDrRecyclerView = (RecyclerView)findViewById(R.id.avadrRecyclerView);
        /*mAfternoonMorningMedicenRecyclerView = (RecyclerView)findViewById(R.id.afternoonMedicenRecyclerView);
        mNightMedicenRecyclerView = (RecyclerView)findViewById(R.id.nightMedicenRecyclerView);
        mlMedicen = (LinearLayout) findViewById(R.id.medicenll);*/
    }
    private void filter(String text) {
        ArrayList<RetrofitDataPrescriptionsModel> filteredList = new ArrayList<>();
        for (RetrofitDataPrescriptionsModel item : mProductList) {
            String Nickname = item.karma_doctor.Doctor;
            if (Nickname.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if(filteredList.size()==0){
            mAvaDrRecyclerView.setVisibility(View.GONE);
            mShirmmer.setVisibility(View.GONE);
            tvnodatatv.setText("डॉक्टर की पर्ची उपलब्ध नहीं है।");
            tvnodatatv.setVisibility(View.VISIBLE);
        }else {
            mAvaDrRecyclerView.setVisibility(View.VISIBLE);
            mShirmmer.setVisibility(View.GONE);
            tvnodatatv.setText("डॉक्टर की पर्ची उपलब्ध नहीं है।");
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
//        model.setTime("25 अगस्त 2022");
//        mProductList.add(model);
//
//        AvailableDoctorListModel model1 = new AvailableDoctorListModel();
//        model1.setId("123");
//        model1.setmName("डॉ प्रकाश नाले");
//        model1.setDepartment("बाल विशेषज्ञ");
//        model1.setTime("15 अगस्त 2022");
//        mProductList.add(model1);
//
//        AvailableDoctorListModel model2 = new AvailableDoctorListModel();
//
//        model2.setId("121");
//        model2.setmName("डॉ दीपक मिश्रा");
//        model2.setDepartment("त्वचा विशेषज्ञ");
//        model2.setTime("21 अगस्त 2022");
//        mProductList.add(model2);
//
//        AvailableDoctorListModel model3 = new AvailableDoctorListModel();
//
//        model3.setId("121");
//        model3.setmName("डॉ प्रिया राज");
//        model3.setDepartment("कान-नाक-गला विशेषज्ञ");
//        model3.setTime("20 अगस्त 2022");
//        mProductList.add(model2);
//
//        AvailableDoctorListModel model4 = new AvailableDoctorListModel();
//
//        model4.setId("121");
//        model4.setmName("डॉ नीरजा पाटिल");
//        model4.setDepartment("हर्दय रोग विशेषज्ञ");
//        model4.setTime("16 अगस्त 2022");
//        mProductList.add(model4);
//
//        AvailableDoctorListModel model5 = new AvailableDoctorListModel();
//
//        model5.setId("121");
//        model5.setmName("डॉ अक्षय वैध");
//        model5.setDepartment("जठर रोग विशेषज्ञ");
//        model5.setTime("15 जुलाई 2022");
//        mProductList.add(model5);
//        Log.i("checkmodeldata","model 0 = "+String.valueOf(mProductList.get(0).mName));
//        Log.i("checkmodeldata","model 1 = "+String.valueOf(mProductList.get(1).mName));
        adapter = new DoctorPrescriptionAdapter(mProductList,this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAvaDrRecyclerView.setLayoutManager(mLayoutManager);
        mAvaDrRecyclerView.setAdapter(adapter);

//        mAvaDrRecyclerView.setVisibility(View.GONE);
//        mShirmmer.setVisibility(View.VISIBLE);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAvaDrRecyclerView.setVisibility(View.VISIBLE);
                mShirmmer.setVisibility(View.GONE);
                tvnodatatv.setVisibility(View.GONE);
            }
        }, 3000);

    }
}