package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.PatientIdModel;
import com.devkraft.karmahealth.Model.ValidateRequest;
import com.devkraft.karmahealth.Model.ValidateResponse;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpMobileActivity extends AppCompatActivity {
    private EditText etMobie;
    CoordinatorLayout coordinatorLayout;
    private ProgressBar pb;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_mobile);
        init();

        getData();
        change();
    }

    public void getData(){
        if (sharedPreferences.getString("Pphone","").equalsIgnoreCase("0")){
            etMobie.setText("");
        }else{
            etMobie.setText(sharedPreferences.getString("Pphone",""));
        }

    }

    public void init() {
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        pb = (ProgressBar)findViewById(R.id.progress);
        pb.setVisibility(View.GONE);
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUpMobileActivity.this, R.style.AlertDialogTheme);
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
           // snack(getResources().getString(R.string.enterten));
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

        }

    }

    public void checkMobileNo(String mobile){
        Log.i("checkmodeldata", "api validateData mobile = " + mobile);
        ValidateRequest request = new ValidateRequest();
        request.phone = "+91" + mobile;
        request.patientId = sharedPreferences.getString("karmaPid","");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.validateData(request).enqueue(new Callback<ValidateResponse>() {
            @Override
            public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                Log.i("checkmodeldata", "validateData response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata", "validateData LoginNewResponse response = " + String.valueOf(response.body().validation));
//                    pb.setVisibility(View.GONE);
                    if (!response.body().validation){
//                        pb.setVisibility(View.GONE);

                        apicheck(etMobie.getText().toString());
                    }else {
                        pb.setVisibility(View.GONE);
                        messVoid("मोबाइल नंबर मौजूद है। कृपया साइन इन करें।");
                    }

//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    pb.setVisibility(View.GONE);
                    messVoid("मोबाइल नंबर मौजूद है। कृपया साइन इन करें। 2");
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

    public void apicheck(String mobile){
        Log.i("MobileNoCheck", "api LoginNewResponse request = " + etMobie.getText().toString());
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);

        service.mobileNoCheck("+91"+mobile).enqueue(new Callback<PatientIdModel>() {
            @Override
            public void onResponse(Call<PatientIdModel> call, Response<PatientIdModel> response) {
                Log.i("MobileNoCheck", "api response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("MobileNoCheck", "api PatientIdCheck response = " + new Gson().toJson(response.body()));

                    if(!response.body().validation){
                        api(etMobie.getText().toString());

                        /*finish();*/
                    }else {
                        pb.setVisibility(View.GONE);
                        messVoid("मोबाइल नंबर पहले ही ले लिया गया है। साइन इन करें।");
                      //  snack("मोबाइल नंबर पहले ही ले लिया गया है। साइन इन करें।");
                    }
//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    pb.setVisibility(View.GONE);
                    messVoid("मोबाइल नंबर पहले ही ले लिया गया है। साइन इन करें।");
                  //  snack("मोबाइल नंबर पहले ही ले लिया गया है। साइन इन करें।");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("MobileNoCheck", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<PatientIdModel> call, Throwable t) {
                messVoid(t.getMessage());
//                snack(t.getMessage());
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }
    public void api(String mobile) {
        Log.i("checkmodeldataCheck", "api Send OTP mobile = " + mobile);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,true);
        service.genrateotp("+91" + mobile).enqueue(new Callback<LoginNewResponse>() {
            @Override
            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                Log.i("checkmodeldataCheck", "api response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldataCheck", "api LoginNewResponse response = " + response.body().Details);
                    pb.setVisibility(View.GONE);
                    apidata(response.body().Details);
//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    pb.setVisibility(View.GONE);
                    messVoid("Same think was wrong.");
                   // snack("Same think was wrong.");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }
    public void apidata(String serviceSid){
        Intent intent = new Intent(this, SignupOTPActivity.class);
        edit.putString("Pphone",etMobie.getText().toString());
        intent.putExtra("mobile", etMobie.getText().toString());
        intent.putExtra("serviceSid", serviceSid);
        startActivity(intent);
    }
}