package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Model.GetKymUserDetailsRequest;
import com.devkraft.karmahealth.Model.GetUserDetailsRequest;
import com.devkraft.karmahealth.Model.GetUserDetailsResponse;
import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.SignInRequestModel;
import com.devkraft.karmahealth.Model.SignInResponseModel;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.VerifyOTPRequest;
import com.devkraft.karmahealth.Model.VerifyOTPResponse;
import com.devkraft.karmahealth.MySMSBroadcastReceiver;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.SmsListener;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    private UserDto userDto;
    private String mMobile = "";
    private String serviceSid = "";
    private TextView mtvMobile, tvresendotp, tvotpcount;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private CountDownTimer youcoun;

    private ProgressBar pb;

    private EditText etOne, etTwo, etThree, etFour, etFive, etSix;
    private boolean checkfirst = false;
    int check =1;
    public MySMSBroadcastReceiver mySMSBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        getData();
        init();
        setData();

        textwatcher();
        setVisible(true);

        startCoundown();
        onClick();



            MySMSBroadcastReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    if (messageText.length()>=6){
                        etOne.setText(String.valueOf(messageText.charAt(0)));
                        etTwo.setText(String.valueOf(messageText.charAt(1)));
                        etThree.setText(String.valueOf(messageText.charAt(2)));
                        etFour.setText(String.valueOf(messageText.charAt(3)));
                        etFive.setText(String.valueOf(messageText.charAt(4)));
                        etSix.setText(String.valueOf(messageText.charAt(5)));

                    }

                    // ed.setText(messageText);
                }
            });


    }
    public boolean showPhoneStatePermission(){
        int permissionCheckSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED && permissionCheckSMS != PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            return false;
        }

    }



    public void onClick(){
        tvresendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                tvresendotp.setVisibility(View.GONE);

                resentOTPapi(mMobile);
            }
        });
    }
    public void resentOTPapi(String mobile){
        Log.i("checkmodeldataCheck", "api LoginNewResponse mobile = " + mobile);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,true);
        service.genrateotp("+91" + mobile).enqueue(new Callback<LoginNewResponse>() {
            @Override
            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                Log.i("checkmodeldata", "api response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata", "api LoginNewResponse response = " + response.body().Details);
                    serviceSid = response.body().Details;
                    pb.setVisibility(View.GONE);

                    setVisible(true);

                    startCoundown();

                    if(check==3){
                        checkfirst = true;
                    }

                } else {
                    checkfirst = false;
                    pb.setVisibility(View.GONE);
                    setVisible(false);
                    //Toast.makeText(OTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    messVoid("गलत OTP।");
                  //  snack("गलत OTP");

                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }

    public void startCoundown(){
        youcoun = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                long tim = l / 1000;
                tvotpcount.setText("00:"+String.valueOf(tim));
               // Log.e("seconds remaining : ", "seconds remaining : " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                check = check+1;
                setVisible(false);
            }
        }.start();
    }

    public void setVisible(boolean check){
        if(check){
            tvresendotp.setVisibility(View.GONE);
            tvotpcount.setVisibility(View.VISIBLE);
        }else {
            tvresendotp.setVisibility(View.VISIBLE);
            tvotpcount.setVisibility(View.GONE);
        }

        if(checkfirst){
            tvresendotp.setVisibility(View.GONE);
            tvotpcount.setVisibility(View.GONE);
        }

    }

    public void getData() {
        Intent intent = getIntent();
        mMobile = intent.getStringExtra("mobile");
        serviceSid = intent.getStringExtra("serviceSid");
    }

    public void init() {
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        tvresendotp = (TextView)findViewById(R.id.textView7);
        tvotpcount = (TextView)findViewById(R.id.textView8);
        pb = (ProgressBar) findViewById(R.id.progresspb);
        mtvMobile = (TextView) findViewById(R.id.textView5);

        etOne = (EditText) findViewById(R.id.editTextPhone2);
        etTwo = (EditText) findViewById(R.id.editTextPhone3);
        etThree = (EditText) findViewById(R.id.editTextPhone4);
        etFour = (EditText) findViewById(R.id.editTextPhone5);
        etFive = (EditText) findViewById(R.id.editTextPhone6);
        etSix = (EditText) findViewById(R.id.editTextPhone7);
    }

    public void setData() {
        mtvMobile.setText(getResources().getString(R.string.hmne) + " " + mMobile + getResources().getString(R.string.fmno));
    }

    public void messVoid(String message){
        //handle menu2 click
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OTPActivity.this, R.style.AlertDialogTheme);
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
    public void textwatcher() {
        etTwo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace

                    if (etSix.getText().length() != 0) {
                        etSix.requestFocus();
                    } else if (etFive.getText().length() != 0) {
                        etFive.requestFocus();
                    } else if (etFour.getText().length() != 0) {
                        etFour.requestFocus();
                    } else if (etThree.getText().length() != 0) {
                        etThree.requestFocus();
                    } else if (etTwo.getText().length() != 0) {
                        etTwo.requestFocus();
                    } else if (etOne.getText().length() != 0) {
                        etOne.requestFocus();
                    } else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etThree.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (etSix.getText().length() != 0) {
                        etSix.requestFocus();
                    } else if (etFive.getText().length() != 0) {
                        etFive.requestFocus();
                    } else if (etFour.getText().length() != 0) {
                        etFour.requestFocus();
                    } else if (etThree.getText().length() != 0) {
                        etThree.requestFocus();
                    } else if (etTwo.getText().length() != 0) {
                        etTwo.requestFocus();
                    } else if (etOne.getText().length() != 0) {
                        etOne.requestFocus();
                    } else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etFour.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (etSix.getText().length() != 0) {
                        etSix.requestFocus();
                    } else if (etFive.getText().length() != 0) {
                        etFive.requestFocus();
                    } else if (etFour.getText().length() != 0) {
                        etFour.requestFocus();
                    } else if (etThree.getText().length() != 0) {

                        etThree.requestFocus();
                    } else if (etTwo.getText().length() != 0) {
                        etTwo.requestFocus();
                    } else if (etOne.getText().length() != 0) {
                        etOne.requestFocus();
                    } else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etFive.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (etSix.getText().length() != 0) {
                        etSix.requestFocus();
                    } else if (etFive.getText().length() != 0) {
                        etFive.requestFocus();
                    } else if (etFour.getText().length() != 0) {
                        etFour.requestFocus();
                    } else if (etThree.getText().length() != 0) {

                        etThree.requestFocus();
                    } else if (etTwo.getText().length() != 0) {
                        etTwo.requestFocus();
                    } else if (etOne.getText().length() != 0) {
                        etOne.requestFocus();
                    } else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etSix.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (etSix.getText().length() != 0) {
                        etSix.requestFocus();
                    } else if (etFive.getText().length() != 0) {
                        etFive.requestFocus();
                    } else if (etFour.getText().length() != 0) {
                        etFour.requestFocus();
                    } else if (etThree.getText().length() != 0) {
                        etThree.requestFocus();
                    } else if (etTwo.getText().length() != 0) {
                        etTwo.requestFocus();
                    } else if (etOne.getText().length() != 0) {
                        etOne.requestFocus();
                    } else {
                        etOne.requestFocus();
                    }

                }
                return false;
            }
        });


        etOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etOne.getText().length() > 0) {
                    etTwo.requestFocus();
                }
            }
        });
        etTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etTwo.getText().length() > 0) {
                    etThree.requestFocus();
                } else {
                    etOne.requestFocus();
                }
            }
        });

        etThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etThree.getText().length() > 0) {
                    etFour.requestFocus();
                } else {
                    etTwo.requestFocus();
                }
            }
        });

        etFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etFour.getText().length() > 0) {
                    etFive.requestFocus();
                } else {
                    etThree.requestFocus();
                }
            }
        });

        etFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etFive.getText().length() > 0) {
                    etSix.requestFocus();
                } else {
                    etFour.requestFocus();
                }
            }
        });

        etSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etSix.getText().length() > 0) {
                    etOne.setEnabled(false);
                    etTwo.setEnabled(false);
                    etThree.setEnabled(false);
                    etFour.setEnabled(false);
                    etFive.setEnabled(false);
                    etSix.setEnabled(false);

                    pb.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String code = etOne.getText().toString()+etTwo.getText().toString()+etThree.getText().toString()+etFour.getText().toString()+
                                    etFive.getText().toString()+etSix.getText().toString();
                            api(mMobile,code);
                        }
                    }, 3000);


                } else {
                    etFive.requestFocus();
                }
            }
        });
    }

    public void onClickNew() {
//        Intent intent = new Intent(this, CodeActivity.class);
//        intent.putExtra("mobile", mMobile);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void getUserDetailsAPI(String mobile, String token,String refreshToken){
        GetUserDetailsRequest request = new GetUserDetailsRequest();
        request.phone = mobile;
        Log.i("checkmodeldata", "get user api LoginNewResponse mobile = " + mobile);
        Log.i("checkmodeldata", "get user api LoginNewResponse token = " + token);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.getuserDetails(request,"Bearer " + token).enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {
                Log.i("checkmodeldata", "get user api response code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata", "get user api response body = " +new Gson().toJson(response.body()));
                    edit.putString("Ptoken", token);
                    edit.putString("refreshToken", refreshToken);
                    edit.putString("karmaPid", response.body().patientId);
                    edit.putString("kymPid", response.body().id);
                    edit.putString("Pname", response.body().patientName);
                    edit.putString("Page", response.body().age);
                    edit.putString("Pgender", response.body().gender);
                    edit.putString("Pphone", response.body().phone);
                    edit.putBoolean("Login",true);
                    edit.apply();
                  /*  Log.i("checkmodeldata", "api LoginNewResponse response = " + response.body().status);
                    if(response.body().valid == true){*/
                        onClickNew();
                    /*}*/

//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    messVoid("गलत OTP।");
                   // Toast.makeText(OTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());
                    apinot();

                }
            }

            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
                apinot();
            }
        });
    }
    public void apinot(){
        etOne.setEnabled(true);
        etTwo.setEnabled(true);
        etThree.setEnabled(true);
        etFour.setEnabled(true);
        etFive.setEnabled(true);
        etSix.setEnabled(true);

        pb.setVisibility(View.GONE);
    }


    public void signin(String mobile){
        SignInRequestModel request = new SignInRequestModel();
        request.phone = "+91" + mobile;

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.signin(request).enqueue(new Callback<SignInResponseModel>() {
            @Override
            public void onResponse(Call<SignInResponseModel> call, Response<SignInResponseModel> response) {
                Log.i("signinsignin", "sigin api response otp 0 code = " + response.code());

                if (response.isSuccessful()) {
                    Log.i("signinsignin", "sigin api response otp 0 body = " + new Gson().toJson(response.body()));
                  //  Log.i("checkmodeldata", "api LoginNewResponse otp response = " + String.valueOf(response.body().verificationCheck.valid));
                    Log.i("signinsignin", "sigin api LoginNewResponse otp token = " + String.valueOf(response.body().accessToken));
                    if(response.body().accessToken !=null){
                        getUserDetailsAPI("+91" + mobile,response.body().accessToken,response.body().refreshToken);
                    }else {
                        messVoid("गलत OTP।");
                       // Toast.makeText(OTPActivity.this, "गलत ओटीपी।", Toast.LENGTH_SHORT).show();
                        apinot();
                    }
                   // signin(mobile);

//                    mProductList.addAll(response.body().data);
//                    setData();
                }
                else {
                    messVoid("गलत OTP।");
                   // Toast.makeText(OTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "sigin api response 1 code = " + response.code());
                    apinot();
                }
            }

            @Override
            public void onFailure(Call<SignInResponseModel> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
                apinot();
            }
        });
    }
    public void api(String mobile, String code) {
        VerifyOTPRequest request = new VerifyOTPRequest();
        request.to = "+91" + mobile;
        request.code = code;
        Intent intent = getIntent();
        request.pathServiceSid = serviceSid;
        Log.i("checkmodeldataCheck", "api LoginNewResponse otp mobile = " + mobile);
        Log.i("checkmodeldataCheck", "api LoginNewResponse otp code = " + code);
        Log.i("checkmodeldataCheck", "api LoginNewResponse otp serviceSid = " + intent.getStringExtra("serviceSid"));
        Log.i("checkmodeldataCheck", "api LoginNewResponse otp request = " + new Gson().toJson(request));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,true);
        service.verifyOTPs(serviceSid,code).enqueue(new Callback<VerifyOTPResponse>() {
            @Override
            public void onResponse(Call<VerifyOTPResponse> call, Response<VerifyOTPResponse> response) {
                Log.i("checkmodeldataCheck", "verifyOTPs response otp 0 code = " + response.code());

                if (response.isSuccessful()) {
                   /* Log.i("checkmodeldata", "api response otp 0 body = " + new Gson().toJson(response.body()));
                    Log.i("checkmodeldata", "api LoginNewResponse otp response = " + String.valueOf(response.body().verificationCheck.valid));
                    Log.i("checkmodeldata", "api LoginNewResponse otp token = " + String.valueOf(response.body().jwtResponse.token));
                    if(response.body().Status.equalsIgnoreCase("Success")){
                        getUserDetailsAPI("+91" + mobile,"Bearer " + response.body().jwtResponse.token);
                    }else {
                        Toast.makeText(OTPActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                        apinot();
                    }*/
                    signin(mobile);

//                    mProductList.addAll(response.body().data);
//                    setData();
                }
                else {
                    messVoid("गलत OTP।");
                  //  Toast.makeText(OTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "verifyOTPs api response 1 code = " + response.code());
                    apinot();
                }
            }

            @Override
            public void onFailure(Call<VerifyOTPResponse> call, Throwable t) {
                Log.i("checkmodeldata", "verifyOTPs error message response  = " + t.getMessage());
                apinot();
            }
        });
    }

    public void onClick(View view) {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }
}