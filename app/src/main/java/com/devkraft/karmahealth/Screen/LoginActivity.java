package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.BuildConfig;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.RetrfoitUploadModel;
import com.devkraft.karmahealth.Model.RetrofitDrAvailabilityModel;
import com.devkraft.karmahealth.Model.RetrofitPrescriptionsModel;
import com.devkraft.karmahealth.Model.ValidateRequest;
import com.devkraft.karmahealth.Model.ValidateResponse;
import com.devkraft.karmahealth.Model.VersionControllModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.retrofit.ServiceGenerator;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etMobie;
    private AppCompatButton tvSignup;
    CoordinatorLayout coordinatorLayout;
    private ProgressBar pb;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private final int REQUEST_PERMISSION_PHONE_STATE=121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        init();
        showPhoneStatePermission();
       // getData();
        change();
        version_data();
        onClick();
    }
    public void version_data() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        Log.i("version_data", "upload versionName = " + versionName);
        Log.i("version_data", "versionCode id = " + String.valueOf(versionCode));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.versionControll(String.valueOf(versionCode)).enqueue(new Callback<VersionControllModel>() {
            @Override
            public void onResponse(Call<VersionControllModel> call, Response<VersionControllModel> response) {
                Log.i("version_data", "upload data  list response 0 code = " + response.code());
                Log.i("version_data", "upload data  list body = " + new Gson().toJson(response.body()));


                if(response.isSuccessful()){
                    if(!response.body().needToUpdate){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
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
                }


            }

            @Override
            public void onFailure(Call<VersionControllModel> call, Throwable t) {
                Log.i("checkmodeldatathirthi", "api error message response  = " + t.getMessage());
            }
        });
    }
    private void showPhoneStatePermission() {
        int permissionCheckSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED && permissionCheckSMS != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_SMS, REQUEST_PERMISSION_PHONE_STATE);
                showExplanation("Permission Needed", "Rationale", Manifest.permission.RECEIVE_SMS, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, REQUEST_PERMISSION_PHONE_STATE);
            }
        } else {
        //    Toast.makeText(LoginActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
    private void onClick(){
        tvSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
    public void getData(){
        etMobie.setText(sharedPreferences.getString("Pphone",""));
    }

    public void init() {
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        pb = (ProgressBar)findViewById(R.id.progress);
        pb.setVisibility(View.GONE);
        tvSignup = (AppCompatButton)findViewById(R.id.button8);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etMobie = (EditText) findViewById(R.id.editTextPhone);
    }


    public void change() {
        etMobie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                etMobie.setBackgroundResource(R.drawable.editbac);
                etMobie.setTextColor(getResources().getColor(R.color.header));
            }
        });
    }

    public void snack(String msg) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public void messVoid(String message){
        //handle menu2 click
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("Alert");
        builder.setMessage(message);
//        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void loginbtn(View view) {
        View views = this.getCurrentFocus();
        if (views != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService("input_method");
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (etMobie.getText().length() != 10) {
            messVoid(getResources().getString(R.string.enterten));
          //  snack(getResources().getString(R.string.enterten));
            etMobie.setBackgroundResource(R.drawable.rededitbac);
            etMobie.setTextColor(getResources().getColor(R.color.red));/*
            Toast.makeText(this, getResources().getString(R.string.enterten), Toast.LENGTH_SHORT).show();*/
        } else if (etMobie.getText().toString().substring(0, 1).toString().equalsIgnoreCase("0")) {
            messVoid(getResources().getString(R.string.entertenzero));
//            snack(getResources().getString(R.string.entertenzero));
            etMobie.setBackgroundResource(R.drawable.rededitbac);
            etMobie.setTextColor(getResources().getColor(R.color.red));
        } else {
//            Intent intent = new Intent(this,OTPActivity.class);
//            intent.putExtra("mobile",etMobie.getText().toString());
//            startActivity(intent);
            pb.setVisibility(View.VISIBLE);
            checkMobileNo(etMobie.getText().toString());
           // api(etMobie.getText().toString());

        }

    }
    public void checkMobileNo(String mobile){
        Log.i("checkmodeldata", "api validateData mobile = " + mobile);
        ValidateRequest request = new ValidateRequest();
        request.phone = "+91" + mobile;
        request.patientId ="null";
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.validateData(request).enqueue(new Callback<ValidateResponse>() {
            @Override
            public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                Log.i("checkmodeldata", "validateData response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata", "validateData LoginNewResponse response = " + String.valueOf(response.body().validation));
//                    pb.setVisibility(View.GONE);
                    if (response.body().validation){
//                        pb.setVisibility(View.GONE);
                        api(etMobie.getText().toString());
                    }else {
                        pb.setVisibility(View.GONE);
                        messVoid("मोबाइल नंबर नहीं मिला। कृपया रजिस्टर करें।");
                       // snack("मोबाइल नंबर नहीं मिला। कृपया रजिस्टर करें।");
                    }

//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    pb.setVisibility(View.GONE);
                    messVoid("मोबाइल नंबर नहीं मिला। कृपया रजिस्टर करें।");
//                    snack("मोबाइल नंबर नहीं मिला। कृपया रजिस्टर करें।");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<ValidateResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }


    public void api(String mobile) {
        Log.i("checkmodeldataCheck", "api login LoginNewResponse mobile = " + mobile);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,true);
        service.genrateotp("+91" + mobile).enqueue(new Callback<LoginNewResponse>() {
            @Override
            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                Log.i("checkmodeldataCheck", "genrateotp login response 0 code = " + response.code());
                Log.i("checkmodeldataCheck", "genrateotp login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata", "api login LoginNewResponse response = " + response.body().Details);

                    pb.setVisibility(View.GONE);
                    apidata(response.body().Details);
//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    pb.setVisibility(View.GONE);
                    messVoid("मोबाइल नंबर नहीं मिला। कृपया रजिस्टर करें।");
//                    snack("गलत प्रत्यक्ष पत्र।");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
                pb.setVisibility(View.GONE);
            }
        });
    }
    public void apidata(String serviceSid){
        Intent intent = new Intent(this, OTPActivity.class);
        intent.putExtra("mobile", etMobie.getText().toString());
        intent.putExtra("serviceSid", serviceSid);
        startActivity(intent);
    }
}