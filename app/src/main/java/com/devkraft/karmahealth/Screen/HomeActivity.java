package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Adapter.AfternoonMedicenAdapter;
import com.devkraft.karmahealth.Adapter.EveningMedicenAdapter;
import com.devkraft.karmahealth.Adapter.MedicenAdapter;
import com.devkraft.karmahealth.Adapter.NightMedicenAdapter;
import com.devkraft.karmahealth.BuildConfig;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddDrugDto;
import com.devkraft.karmahealth.Model.AuthModel;
import com.devkraft.karmahealth.Model.DashboardResponse;
import com.devkraft.karmahealth.Model.DrugDosageAdherenceDTO;
import com.devkraft.karmahealth.Model.DrugDto;
import com.devkraft.karmahealth.Model.DrugListAPIResponse;
import com.devkraft.karmahealth.Model.FCMDataRequest;
import com.devkraft.karmahealth.Model.GetKymUserDetailsRequest;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.RetrfoitUploadModel;
import com.devkraft.karmahealth.Model.LoginRequest;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.MedicenListModel;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.RetrofitPrescriptionsModel;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.VersionControllModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.Pref;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.inter.OnItemClick;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.retrofit.ServiceGenerator;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements OnItemClick {
    public Pref mPref;
    private LinearLayout mlMedicen, mAvalaDoctor, mDrPrescriptionll, mMyTrackll, mMorningll, mAfternoonll, mEveingll, mNightll;
    private RecyclerView mMorningMedicenRecyclerView;
    private RecyclerView mAfternoonMorningMedicenRecyclerView;
    private RecyclerView mNightMedicenRecyclerView;
    private RecyclerView mEveningMedicenRecyclerView;
    private List<MedicenListModel> mProductList = new ArrayList<>();
    private List<MedicenListModel> mProductAfternoonList = new ArrayList<>();
    private List<MedicenListModel> mProductEveningList = new ArrayList<>();
    private List<MedicenListModel> mProductNightList = new ArrayList<>();


    private ShimmerFrameLayout mShirmmer;
    MedicenAdapter adapter;
    NightMedicenAdapter adapterN;
    AfternoonMedicenAdapter adapterAN;
    EveningMedicenAdapter adapterEV;

    private TextView tvidaddmedicintv;

    RecyclerView.LayoutManager mMorningLayoutManager;
    RecyclerView.LayoutManager mAfterLayoutManager;
    RecyclerView.LayoutManager mEvingLayoutManager;
    RecyclerView.LayoutManager mNightLayoutManager;
    private static final String YOUR_APP_ID = "42aab10e-22db-4ba4-bb4f-ff71ac7d4386";
    private static final String YOUR_APP_KEY = "37a46e18-7137-4cd5-8f98-37a002af3a84";

    private ProgressDialogSetup mProgressDialogSetup;
    private UserDto userDto;
    private ImageView ivnotiiv, ivlogoutiv;
    public TextView tvusernametv,tvdatetv;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    public String ftoken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FreshchatConfig config = new FreshchatConfig(YOUR_APP_ID, YOUR_APP_KEY);
        config.setCameraCaptureEnabled(true);


        config.setGallerySelectionEnabled(true);

        config.setResponseExpectationEnabled(true);
        config.setDomain("msdk.in.freshchat.com");
        Freshchat.getInstance(getApplicationContext()).init(config);


        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Freshchat.showConversations(getApplicationContext());
            }
        });


        setUpAppLanguage();

        init();
        getFirebaseToken();
        setData();


        onCick();

        getUserKYMDetails();
        //loginApi();
    //    authkey();
    }

    @Override
    protected void onResume() {
        super.onResume();
        version_data();
        authkey();

    }

    public interface SearchDrugCallback {
        public void callDrugDto(DrugListAPIResponse drugDto);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
      //  finish();
        finishAffinity();
        System.exit(0);
        //handle menu2 click
       /* AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("Closing App");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);

            }
        });
        builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();*/
    }

    public void onCick() {
        ivlogoutiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //handle menu2 click
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                builder.setTitle("Logout");
                builder.setMessage("क्या आप logout करना चाहते है?");
                builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit.clear();
                        edit.apply();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();



            }
        });
        ivnotiiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                startActivity(intent);
            }
        });
        tvidaddmedicintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddDrugActivity.class);
                intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                startActivity(intent);
            }
        });
        mMyTrackll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, MyTrackActivity.class);
//                startActivity(intent);.
                Log.i("symptomesurl","url - 2");

                Intent intent = new Intent(HomeActivity.this, MyConditionsNewActivity.class);
                startActivity(intent);
            }
        });
        mAvalaDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do something after 100ms
                Intent intent = new Intent(HomeActivity.this, AvailableDoctorActivity.class);
                startActivity(intent);

            }
        });
        mDrPrescriptionll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DoctorPrescriptionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setData() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM");
        String formattedDate = df.format(c.getTime());

        tvusernametv.setText("नमस्ते, "+sharedPreferences.getString("Pname",""));
        String currentDateTimeString = java.text.DateFormat.getDateInstance().format(new Date());
        tvdatetv.setText("आज- "+formattedDate);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialogSetup != null)
            mProgressDialogSetup.dismiss();
    }

    public void init() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        mProgressDialogSetup = ProgressDialogSetup.getProgressDialog(HomeActivity.this);
        mProgressDialogSetup.setCancelable(false);

        mMorningll = (LinearLayout) findViewById(R.id.morningll);

        mAfternoonll = (LinearLayout) findViewById(R.id.afternoonll);
        mEveingll = (LinearLayout) findViewById(R.id.eveingll);
        mNightll = (LinearLayout) findViewById(R.id.nightll);

        tvusernametv = (TextView) findViewById(R.id.usernametv);
        tvdatetv = (TextView)findViewById(R.id.datetv);
        ivlogoutiv = (ImageView) findViewById(R.id.logoutiv);
        ivnotiiv = (ImageView) findViewById(R.id.notiiv);
        tvidaddmedicintv = (TextView) findViewById(R.id.idaddmedicintv);
        mMyTrackll = (LinearLayout) findViewById(R.id.mytrackll);
        mDrPrescriptionll = (LinearLayout) findViewById(R.id.drprescriptionll);
        mAvalaDoctor = (LinearLayout) findViewById(R.id.avdoctorll);
        mShirmmer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mMorningMedicenRecyclerView = (RecyclerView) findViewById(R.id.morningMedicenRecyclerView);
        mAfternoonMorningMedicenRecyclerView = (RecyclerView) findViewById(R.id.afternoonMedicenRecyclerView);
        mNightMedicenRecyclerView = (RecyclerView) findViewById(R.id.nightMedicenRecyclerView);
        mEveningMedicenRecyclerView = (RecyclerView) findViewById(R.id.evningMedicenRecyclerView);
        mlMedicen = (LinearLayout) findViewById(R.id.medicenll);
    }


    public void setUpAppLanguage() {
        mPref = new Pref(this);


        String appLang = "hi";
    /*    if (appLang == "en") {
            String deviceLang = Resources.getSystem().getConfiguration().locale.getLanguage();
            Locale locale = new Locale(deviceLang);
            Locale.setDefault(locale);

            Resources resources = this.getResources();
            Configuration configuration = resources.getConfiguration();

            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }else {*/
        Locale locale = new Locale(appLang);
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        /*}*/
    }

    public void dashboardAPI() {
        mMorningMedicenRecyclerView.setVisibility(View.GONE);
        mShirmmer.setVisibility(View.VISIBLE);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");

        Log.i("checkmodeldashboard", "upload data  api request kym id = " + sharedPreferences.getString("kymPid", "134388"));
        Log.i("checkmodeldashboard", "token = " + token);
        service.dashbordAPI(sharedPreferences.getString("kymPid", "134388"),token).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                Log.i("checkmodeldashboard", "upload data  api response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldashboard", "upload data  api response = " + response.body());
                    Log.i("checkmodeldashboard", "upload data api response 111 = " + new Gson().toJson(response.body()));
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String startDateStr = df.format(c);
                    /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat dfhouse = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    //yyyy-MM-dd HH:mm:ss
                    String formattedDate = df.format(c);
                    String formattedHours = dfhouse.format(c);

                    String[] timeSplit = formattedHours.split(":");
                    String hourStr = timeSplit[0];*/



                        mlMedicen.setVisibility(View.VISIBLE);
                        if (response.body().morningDrug.size() > 0) {
                            if(mProductList.size()>0){
                                mProductList.clear();
                            }

                            // Log.i("checkmodeldashboard","upload data api formattedDate = "+ " hours : "+time);
                            for (int i = 0; i < response.body().morningDrug.size(); i++) {
                                MedicenListModel model1 = new MedicenListModel();
                                model1.setCheck(false);
                                model1.setId(response.body().morningDrug.get(i).id);
                                model1.setmName(response.body().morningDrug.get(i).shortName);
                                model1.setWhenToTake(response.body().morningDrug.get(i).whenToTake);
                                model1.setDrugForm(response.body().morningDrug.get(i).dosageFrom);
                                model1.setUrl(response.body().morningDrug.get(i).url);
                                model1.setDelete(response.body().morningDrug.get(i).isDelete);
                                model1.setTime("सुबह के 9 बजे");
                                if(response.body().morningDrug.get(i).dosageFrom.equalsIgnoreCase("Tablet")){
                                    model1.setNoofpills("1 टैबलेट");
                                }else if(response.body().morningDrug.get(i).dosageFrom.equalsIgnoreCase("Liquid")){
                                    model1.setNoofpills("1 चमच सिरप");
                                }else if(response.body().morningDrug.get(i).dosageFrom.equalsIgnoreCase("Injection")){
                                    model1.setNoofpills("1 इंजेक्शन");
                                }else if(response.body().morningDrug.get(i).dosageFrom.equalsIgnoreCase("Other")){
                                    model1.setNoofpills("1 अन्य");
                                }

                                mProductList.add(model1);

                            }
                            Log.i("checkmodeldashboard", "Morning size = " + String.valueOf(mProductList.size()));
                            adapter = new MedicenAdapter(mProductList, HomeActivity.this);
                            mMorningLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            mMorningMedicenRecyclerView.setLayoutManager(mMorningLayoutManager);
                            mMorningMedicenRecyclerView.setAdapter(adapter);

                            mMorningll.setVisibility(View.VISIBLE);
                            mMorningMedicenRecyclerView.setVisibility(View.VISIBLE);
                            mShirmmer.setVisibility(View.GONE);

                            String time = "09:00:00";

                            Log.i("checkmodeldashb", "upload data api formattedDate = " + " hours : " + time);
                            Log.i("checkmodeldashb", " endDate  : " + response.body().morningDrugEndDate);

                            try {
                                String dateTime = startDateStr + " " + time;
                                Log.i("checkmodeldashb", " dateTime  : " + dateTime);
                                SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formattedDate = sDFormat.parse(dateTime);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(formattedDate);
                                if (AppUtils.isTimePassed(time)) {
                                    cal.add(Calendar.DATE, 1);
                                }

                                Log.i("checkmodeldashb", "upload " + " cal.getTimeInMillis() : " + cal.getTimeInMillis());
                                AppUtils.setAlarmForTime(getApplicationContext(), time, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, Constants.DAILY, null,response.body().morningDrugEndDate);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            mMorningll.setVisibility(View.GONE);
                            mMorningMedicenRecyclerView.setVisibility(View.GONE);
                            mShirmmer.setVisibility(View.GONE);
                        }

                        if (response.body().afternoonDrug.size() > 0) {
                            if(mProductAfternoonList.size()>0){
                                mProductAfternoonList.clear();
                            }
                            for (int i = 0; i < response.body().afternoonDrug.size(); i++) {
                                MedicenListModel model = new MedicenListModel();
                                model.setCheck(false);
                                model.setId(response.body().afternoonDrug.get(i).id);
                                model.setmName(response.body().afternoonDrug.get(i).shortName);
                                model.setWhenToTake(response.body().afternoonDrug.get(i).whenToTake);
                                model.setDrugForm(response.body().afternoonDrug.get(i).dosageFrom);
                                model.setUrl(response.body().afternoonDrug.get(i).url);
                                model.setDelete(response.body().afternoonDrug.get(i).isDelete);
                                model.setTime("दोपहर के 12 बजे");
                                if(response.body().afternoonDrug.get(i).dosageFrom.equalsIgnoreCase("Tablet")){
                                    model.setNoofpills("1 टैबलेट");
                                }else if(response.body().afternoonDrug.get(i).dosageFrom.equalsIgnoreCase("Liquid")){
                                    model.setNoofpills("1 चमच सिरप");
                                }else if(response.body().afternoonDrug.get(i).dosageFrom.equalsIgnoreCase("Injection")){
                                    model.setNoofpills("1 इंजेक्शन");
                                }else if(response.body().afternoonDrug.get(i).dosageFrom.equalsIgnoreCase("Other")){
                                    model.setNoofpills("1 अन्य");
                                }
                                mProductAfternoonList.add(model);

                            }
                            adapterAN = new AfternoonMedicenAdapter(mProductAfternoonList, HomeActivity.this);
                            mAfterLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            mAfternoonMorningMedicenRecyclerView.setLayoutManager(mAfterLayoutManager);
                            mAfternoonMorningMedicenRecyclerView.setAdapter(adapterAN);

                            mAfternoonMorningMedicenRecyclerView.setVisibility(View.VISIBLE);
                            mShirmmer.setVisibility(View.GONE);
                            mAfternoonll.setVisibility(View.VISIBLE);
                            String time = "12:00:00";

                            try {
                                String dateTime = startDateStr + " " + time;
                                SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formattedDate = sDFormat.parse(dateTime);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(formattedDate);
                                if (AppUtils.isTimePassed(time)) {
                                    cal.add(Calendar.DATE, 1);
                                }

                                Log.i("checkmodeldashboardnew", "upload " + " cal.getTimeInMillis() lucky : " + cal.getTimeInMillis());
                                AppUtils.setAlarmForTime(getApplicationContext(), time, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, Constants.DAILY, null,response.body().afternoonDrugEndDate);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                        else {
                            mAfternoonll.setVisibility(View.GONE);
                            mAfternoonMorningMedicenRecyclerView.setVisibility(View.GONE);
                            mShirmmer.setVisibility(View.GONE);
                        }

                        if (response.body().nightDrug.size() > 0) {
                            if(mProductNightList.size()>0){
                                mProductNightList.clear();
                            }
                            for (int i = 0; i < response.body().nightDrug.size(); i++) {
                                MedicenListModel modele = new MedicenListModel();
                                modele.setCheck(false);
                                modele.setId(response.body().nightDrug.get(i).id);
                                modele.setmName(response.body().nightDrug.get(i).shortName);
                                modele.setWhenToTake(response.body().nightDrug.get(i).whenToTake);
                                modele.setDrugForm(response.body().nightDrug.get(i).dosageFrom);
                                modele.setUrl(response.body().nightDrug.get(i).url);
                                modele.setDelete(response.body().nightDrug.get(i).isDelete);
                                modele.setTime("रात के 9 बजे");
                                if(response.body().nightDrug.get(i).dosageFrom.equalsIgnoreCase("Tablet")){
                                    modele.setNoofpills("1 टैबलेट");
                                }else if(response.body().nightDrug.get(i).dosageFrom.equalsIgnoreCase("Liquid")){
                                    modele.setNoofpills("1 चमच सिरप");
                                }else if(response.body().nightDrug.get(i).dosageFrom.equalsIgnoreCase("Injection")){
                                    modele.setNoofpills("1 इंजेक्शन");
                                }else if(response.body().nightDrug.get(i).dosageFrom.equalsIgnoreCase("Other")){
                                    modele.setNoofpills("1 अन्य");
                                }
                                mProductNightList.add(modele);

                            }
                            adapterN = new NightMedicenAdapter(mProductNightList, HomeActivity.this);
                            mNightLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            mNightMedicenRecyclerView.setLayoutManager(mNightLayoutManager);
                            mNightMedicenRecyclerView.setAdapter(adapterN);


                            mNightll.setVisibility(View.VISIBLE);
                            mNightMedicenRecyclerView.setVisibility(View.VISIBLE);
                            mShirmmer.setVisibility(View.GONE);
                            /*String time = "22:00:00";*/
                            String time = "01:45:00";

                            try {
                                String dateTime = startDateStr + " " + time;
                                SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formattedDate = sDFormat.parse(dateTime);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(formattedDate);
                                if (AppUtils.isTimePassed(time)) {
                                    cal.add(Calendar.DATE, 1);
                                }

                                Log.i("checkmodeldashb", "lucky upload " + " cal.getTimeInMillis() : " + dateTime);
                                AppUtils.setAlarmForTime(getApplicationContext(), time, cal.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, Constants.DAILY, null,response.body().nightDrugEndDate);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            mNightll.setVisibility(View.GONE);
                            mNightMedicenRecyclerView.setVisibility(View.GONE);
                            mShirmmer.setVisibility(View.GONE);
                        }
                        // mEveningMedicenRecyclerView

                        if (response.body().eveningDrug.size() > 0) {
                            if(mProductEveningList.size()>0){
                                mProductEveningList.clear();
                            }
                            for (int i = 0; i < response.body().eveningDrug.size(); i++) {
                                MedicenListModel modele = new MedicenListModel();
                                modele.setCheck(false);
                                modele.setId(response.body().eveningDrug.get(i).id);
                                modele.setmName(response.body().eveningDrug.get(i).shortName);
                                modele.setWhenToTake(response.body().eveningDrug.get(i).whenToTake);
                                modele.setDrugForm(response.body().eveningDrug.get(i).dosageFrom);
                                modele.setUrl(response.body().eveningDrug.get(i).url);
                                modele.setDelete(response.body().eveningDrug.get(i).isDelete);
                                modele.setTime("शाम के 4 बजे");
                                if(response.body().eveningDrug.get(i).dosageFrom.equalsIgnoreCase("Tablet")){
                                    modele.setNoofpills("1 टैबलेट");
                                }else if(response.body().eveningDrug.get(i).dosageFrom.equalsIgnoreCase("Liquid")){
                                    modele.setNoofpills("1 चमच सिरप");
                                }else if(response.body().eveningDrug.get(i).dosageFrom.equalsIgnoreCase("Injection")){
                                    modele.setNoofpills("1 इंजेक्शन");
                                }else if(response.body().eveningDrug.get(i).dosageFrom.equalsIgnoreCase("Other")){
                                    modele.setNoofpills("1 अन्य");
                                }
                                mProductEveningList.add(modele);

                            }
                            adapterEV = new EveningMedicenAdapter(mProductEveningList, HomeActivity.this);
                            mEvingLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            mEveningMedicenRecyclerView.setLayoutManager(mEvingLayoutManager);
                            mEveningMedicenRecyclerView.setAdapter(adapterEV);

                            mEveingll.setVisibility(View.VISIBLE);
                            mEveningMedicenRecyclerView.setVisibility(View.VISIBLE);
                            mShirmmer.setVisibility(View.GONE);
                            String time = "16:00:00";

                            try {
                                String dateTime = startDateStr + " " + time;
                                SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date formattedDate = sDFormat.parse(dateTime);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(formattedDate);

                                if (AppUtils.isTimePassed(time)) {
                                    cal.add(Calendar.DATE, 1);
                                }

                                Log.i("checkmodeldashboardnew", "upload " + " cal.getTimeInMillis() : " + cal.getTimeInMillis());
                                AppUtils.setAlarmForTime(getApplicationContext(), time, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, Constants.DAILY, null,response.body().eveningDrugEndDate);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            mEveingll.setVisibility(View.GONE);
                            mEveningMedicenRecyclerView.setVisibility(View.GONE);
                            mShirmmer.setVisibility(View.GONE);
                        }


                } else if(response.code() == 401){

                    refreshToken();
                }else {
                    mlMedicen.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Log.i("checkmodeldatathirthi", "api error message response  = " + t.getMessage());
            }
        });
    }
    @Override
    public void onClickAdapter(String type) {
        dashboardAPI();

    }
    public void getFirebaseToken() {

        FirebaseApp.initializeApp(getApplicationContext());
        ftoken = sharedPreferences.getString("ftoken","");
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            /* if (!TextUtils.isEmpty(token)) {*/
            Log.i("upload_fcm", "retrieve token successful : " + token);

            edit.putString("ftoken", token);
            edit.apply();
            upload_FCM();
            Log.i("upload_fcm", "response history firebase token 87  = " + ftoken);
            /*LoadData loadData = new LoadData(ftoken);
            loadData.execute();*/
           /* } else{
                Log.i("luresponsehistoryyt", "token should not be null...");
            }*/
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task ->   Log.i("upload_fcm", "This is the token : " + task.getResult()));



    }
    public void upload_FCM() {


        FCMDataRequest fcmDataRequest = new FCMDataRequest();
        fcmDataRequest.fcmToken = sharedPreferences.getString("ftoken","");
        fcmDataRequest.source = "android";
        fcmDataRequest.userId = sharedPreferences.getString("kymPid","");
        Log.i("upload_fcm", "upload data api request = " + new Gson().toJson(fcmDataRequest));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.fcmData(fcmDataRequest).enqueue(new Callback<APIMessageResponse>() {
            @Override
            public void onResponse(Call<APIMessageResponse> call, Response<APIMessageResponse> response) {
                Log.i("upload_fcm", "upload data  list response 01 code = " + response.code());
                Log.i("upload_fcm", "upload data  list error body 1  = " + new Gson().toJson(response.errorBody()));
                /*if (response.isSuccessful()) {*/
                    Log.i("upload_fcm", "upload data  list response = " + response.body().getMessage());
                    Log.i("upload_fcm", "upload data list response = " + new Gson().toJson(response.body()));

                    if(response.code() == 401){

                    refreshToken();
                }

            }

            @Override
            public void onFailure(Call<APIMessageResponse> call, Throwable t) {
                Log.i("checkmodeldatathirthi", "api error message response  = " + t.getMessage());
            }
        });
    }



    public void version_data() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        Log.i("version_data", "upload versionName = " + versionName);
        Log.i("version_data", "versionCode id = " + String.valueOf(versionCode));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.versionControll(String.valueOf(versionName)).enqueue(new Callback<VersionControllModel>() {
            @Override
            public void onResponse(Call<VersionControllModel> call, Response<VersionControllModel> response) {
                Log.i("version_data", "upload data  list response 02 code = " + response.code());
                Log.i("version_data", "upload data  list body = " + new Gson().toJson(response.body()));

                if(response.isSuccessful()){
                    if(!response.body().needToUpdate){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("एक नया अपडेट उपलब्ध है।");

                        builder.setMessage("नवीन संस्करण उपलब्ध है। अपडेट करने के लिए हाँ पर क्लिक करें।");
                        builder.setCancelable(false);
                        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //   onDelete(mProductList.get(position).id);
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                        ("market://details?id=yourAppPackageName")));
                                dialogInterface.dismiss();

                            }
                        });
                        builder.show();
                    }
                }else if(response.code() == 401){

                    refreshToken();
                }

            }

            @Override
            public void onFailure(Call<VersionControllModel> call, Throwable t) {
                Log.i("checkmodeldatathirthi", "api error message response  = " + t.getMessage());
            }
        });
    }



    public void upload_data(RetrofitPrescriptionsModel param) {
        Log.i("upload_dataADdAlllsjfljsfl", "upload data api request = " + new Gson().toJson(param));
        Log.i("upload_dataADdAlllsjfljsfl", "karma id = " + sharedPreferences.getString("karmaPid", "134388"));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        Log.i("upload_dataADdAlllsjfljsfl", "upload api token  =" + token);
        service.uploadData(sharedPreferences.getString("karmaPid", "134388"),token, param).enqueue(new Callback<RetrfoitUploadModel>() {
            @Override
            public void onResponse(Call<RetrfoitUploadModel> call, Response<RetrfoitUploadModel> response) {
                Log.i("upload_dataADdAlllsjfljsfl", "717 upload data  list response 03 code = " + response.code());
               // Log.i("upload_dataADdAll", "upload data  list error body 2 = " + new Gson().toJson(response.errorBody()));
                if (response.isSuccessful()) {
                    Log.i("upload_dataADdAlllsjfljsfl", "upload data  list response = " + response.body().message);
                    Log.i("upload_dataADdAlllsjfljsfl", "upload data list response = " + new Gson().toJson(response.body()));
                    dashboardAPI();

                }else if(response.code() == 401){

                    refreshToken();
                } else {
                    dashboardAPI();
                }
            }

            @Override
            public void onFailure(Call<RetrfoitUploadModel> call, Throwable t) {
                Log.i("checkmodeldatathirthi", "api error message response  = " + t.getMessage());
            }
        });
    }
    public void  authkey(){
        HashMap<String,String> header =new HashMap<String, String>();
        String token = sharedPreferences.getString("Ptoken", "134388");
        header.put("seckey","znob1zj8z9zgdebrobd8y3apdtnj9zgo7lv55f5uh9mpx724gl6900ssdinuht9kwxanr1zf1sgjn848o1m1cnkcxbamwfzulo45w34npt4k3ubude60ipslql1e8fs1lv6yx0f551nydq9r2d0qjq1fzxtu1ir73wjbc");

        Log.i("upload_dataADdAll", "karma id = " + sharedPreferences.getString("karmaPid", "134388"));

        Log.i("upload_dataADdAlllsjfljsfl", "auth api token = " + token);

        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        Log.i("upload_dataADdAll", "token = " + service.authenticate(sharedPreferences.getString("karmaPid", "134388"),token,header).request().url().toString());
        service.authenticate(sharedPreferences.getString("karmaPid", "134388"),token,header).enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                Log.i("upload_dataADdAll", "prescription api response 0121111 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("upload_dataADdAll", "prescription api response = " + response.body().message);

                    prescriptionapi();
                } else {
                    dashboardAPI();
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }
    public void refreshToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(sharedPreferences.getString("refreshToken",""));
        Log.i("refreshTokenLog", "refreshToken api request 278 = " + new Gson().toJson(request));

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                Log.i("refreshTokenLog", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("refreshTokenLog", "prescription api response = " + new Gson().toJson(response.body()));

                    edit.putString("Ptoken", response.body().accessToken);
                    edit.apply();
                    authkey();
                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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

    public void prescriptionapi() {


        RetrofitPatientIDRequest request = new RetrofitPatientIDRequest();
        request.PatientId = sharedPreferences.getString("karmaPid", "134388");
        Log.i("upload_dataADdAll", "prescription api request 278 = " + new Gson().toJson(request));
        HashMap<String,String> header =new HashMap<String, String>();
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        header.put("seckey","znob1zj8z9zgdebrobd8y3apdtnj9zgo7lv55f5uh9mpx724gl6900ssdinuht9kwxanr1zf1sgjn848o1m1cnkcxbamwfzulo45w34npt4k3ubude60ipslql1e8fs1lv6yx0f551nydq9r2d0qjq1fzxtu1ir73wjbc");
        header.put("Authorization",token);

        AppUtils.logg("upload_dataADdAllnew","prescription api header = " + String.valueOf(header));
        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.prescriptions(request,header).enqueue(new Callback<RetrofitPrescriptionsModel>() {
            @Override
            public void onResponse(Call<RetrofitPrescriptionsModel> call, Response<RetrofitPrescriptionsModel> response) {
                Log.i("upload_dataADdAllnew", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("upload_dataADdAllnew", "prescription api response = " + response.body().message);
                    Log.i("upload_dataADdAllnew", "prescription api response = " + new Gson().toJson(response.body()));

                    if (response.body().data.size() > 0) {
                        TextView tv =findViewById(R.id.idnextdatetv);
                        int size = response.body().data.size()-1;
                        Log.i("upload_dataADdAllnew", "prescription api response size = " + String.valueOf(size));
                        Log.i("upload_dataADdAllnew", "prescription api response date = " + String.valueOf(new Gson().toJson(response.body().data.get(size).ReviewDate)));
                        if(response.body().data.get(size).ReviewDate.equalsIgnoreCase("0000-00-00")){
                            findViewById(R.id.onefindattll).setVisibility(View.GONE);
                        }else   if(response.body().data.get(size).ReviewDate.isEmpty()){
                            findViewById(R.id.onefindattll).setVisibility(View.GONE);
                        }else {

                            try {
                                // today
//                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                                String startDateStr = df.format(c);
                                String date = response.body().data.get(size).ReviewDate;

                                SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date formattedDate = sDFormat.parse(date);

                                Calendar cal = Calendar.getInstance();
                                Calendar cal1 = Calendar.getInstance();
                                cal.setTime(formattedDate);
                                Log.i("upload_dataADdAllnew", "prescription api response time = " + String.valueOf(cal.getTime()));
                                Log.i("upload_dataADdAllnew", "prescription api response time 1  = " + String.valueOf(cal1.getTime()));
                                if(cal1.after(cal)){
                                    Log.i("upload_dataADdAllnew", "true");
                                    findViewById(R.id.onefindattll).setVisibility(View.GONE);
                                }else {
                                    Log.i("upload_dataADdAllnew", "false");
                                    findViewById(R.id.onefindattll).setVisibility(View.VISIBLE);
                                    tv.setText(response.body().data.get(size).ReviewDate);

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                                findViewById(R.id.onefindattll).setVisibility(View.GONE);
                            }
                        }
                        upload_data(response.body());
                    } else {
                        //   upload_data(response.body());
                        findViewById(R.id.onefindattll).setVisibility(View.GONE);
                        dashboardAPI();

                    }

                } else {
                    dashboardAPI();
                }
            }

            @Override
            public void onFailure(Call<RetrofitPrescriptionsModel> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }
    public void getUserKYMDetails(){
        GetKymUserDetailsRequest request = new GetKymUserDetailsRequest();
        request.phone = sharedPreferences.getString("Pphone","");
      /*  request.otp = sharedPreferences.getString("otp","");*/

        Log.i("checkmodeldatalogin", "get user api LoginNewResponse token = " + String.valueOf(new Gson().toJson(request)));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.getkymuserDetails(request,"Bearer " + sharedPreferences.getString("Ptoken","")).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i("checkmodeldatalogin", "get user api response code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldatalogin", "get user api response body = " +new Gson().toJson(response.body()));
                    ApplicationPreferences.get().saveStringValue(Constants.IS_NEW_USER, "true");
                    ApplicationPreferences.get().setUserDetails(response.body());
                    userDto = response.body().getUserDTO();
                    AppUtils.logCleverTapEvent(HomeActivity.this, Constants.USER_LOGGED_IN, null);

                } else {
                    // Toast.makeText(OTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldatalogin", "api response 1 code = " + response.code());


                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i("checkmodeldatalogin", "api error message response  = " + t.getMessage());

            }
        });

    }
    public void loginApi() {
        Log.i("Login_response", "0 = ");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ashwini.mehendale+01@yopmail.com");
        loginRequest.setPassword("ashwiniM@7");
        loginRequest.setPlatform(Constants.ANDROID_PLATFORM);
        Log.i("Login_response", "1 = " + new Gson().toJson(loginRequest));


        if (mProgressDialogSetup != null)
            mProgressDialogSetup.show();

        AppUtils.logEvent(Constants.LOGIN_API_CALLED);
        AppUtils.logCleverTapEvent(this, Constants.LOGIN_API_CALLED, null);

        GenericRequest<LoginResponse> logincRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().SignIn(),
                        LoginResponse.class, loginRequest,
                        loginResponse -> updateUIAsLoginSuccess(loginResponse, false),
                        this::updateUIAsLoginFailed);

        ApiService.get().addToRequestQueue(logincRequest);
    }


    private void updateUIAsLoginSuccess(LoginResponse loginResponse, boolean isGoogleLogIn) {
        Log.i("Login_response", "2 = " + new Gson().toJson(loginResponse));
        ApplicationPreferences.get().saveStringValue(Constants.IS_NEW_USER, "true");
        AppUtils.hideProgressBar(mProgressDialogSetup);
        ApplicationPreferences.get().setUserDetails(loginResponse);
        userDto = loginResponse.getUserDTO();
        if (isGoogleLogIn) {
            AppUtils.logCleverTapEvent(this, Constants.GOOGLE_USER_LOGGED_IN, null);
        } else {
            AppUtils.logCleverTapEvent(this, Constants.USER_LOGGED_IN, null);
        }
       /* if (userDTO.getTenantId() != 1 && !userDTO.isEmailConfirm()) {
            callUserVerifiedEmailAPI();
        } else if (userDTO.getTrialExpiryDate() == null) {
            AppUtils.showLandingPageAccordingToUserProfile(this);
        } else {
            AppUtils.showLandingPageAccordingToUserProfile(HomeActivity.this);
            AppUtils.callTimeZoneAPI(HomeActivity.this);
        }*/
    }

    private void updateUIAsLoginFailed(VolleyError volleyError) {
        Log.i("Login_response", "3 = " + volleyError.getMessage());
        AppUtils.hideProgressBar(mProgressDialogSetup);
        String res = AppUtils.getVolleyError(HomeActivity.this, volleyError);
        AppUtils.showAlert(HomeActivity.this, res);
    }
}