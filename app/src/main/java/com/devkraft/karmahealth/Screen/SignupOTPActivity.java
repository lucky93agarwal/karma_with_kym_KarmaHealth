package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.SignUpRequestModel;
import com.devkraft.karmahealth.Model.SignUpResponseModel;
import com.devkraft.karmahealth.Model.VerifyOTPRequest;
import com.devkraft.karmahealth.Model.VerifyOTPResponse;
import com.devkraft.karmahealth.MySMSBroadcastReceiver;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.SmsListener;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SignupOTPActivity extends AppCompatActivity {
    private String mMobile = "";
    private String serviceSid = "";
    private TextView mtvMobile, tvresendotp, tvotpcount;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private CountDownTimer youcoun;

    private ProgressBar pb;

    private EditText etOne, etTwo, etThree, etFour, etFive, etSix;
    private boolean checkfirst = false;
    int check = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_otpactivity);


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
                if (messageText.length() >= 6) {
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


    public void onClick() {
        tvresendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                tvresendotp.setVisibility(View.GONE);

                resentOTPapi(mMobile);
            }
        });
    }

    public void resentOTPapi(String mobile) {
        Log.i("checkmodeldata", "api LoginNewResponse mobile = " + mobile);
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
                    if (check == 3) {
                        checkfirst = true;
                    }
                } else {
                    checkfirst = false;
                    pb.setVisibility(View.GONE);
                    setVisible(false);
                    messVoid("गलत OTP।");
                  //  Toast.makeText(SignupOTPActivity.this, "गलत OTP", Toast.LENGTH_SHORT).show();
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

    public void startCoundown() {
        youcoun = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                long tim = l / 1000;
                tvotpcount.setText("00:" + String.valueOf(tim));
                // Log.e("seconds remaining : ", "seconds remaining : " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                check = check + 1;
                setVisible(false);
            }
        }.start();
    }

    public void setVisible(boolean check) {
        if (check) {
            tvresendotp.setVisibility(View.GONE);
            tvotpcount.setVisibility(View.VISIBLE);
        } else {
            tvresendotp.setVisibility(View.VISIBLE);
            tvotpcount.setVisibility(View.GONE);
        }

        if (checkfirst) {
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
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        tvresendotp = (TextView) findViewById(R.id.textView7);
        tvotpcount = (TextView) findViewById(R.id.textView8);
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
//

                            String code = etOne.getText().toString() + etTwo.getText().toString() + etThree.getText().toString() + etFour.getText().toString() +
                                    etFive.getText().toString() + etSix.getText().toString();
                            api(mMobile, code);
                        }
                    }, 3000);


                } else {
                    etFive.requestFocus();
                }
            }
        });
    }

    public void onClickNew(String token,String refreshToken) {

        edit.putString("Ptoken", token);
        edit.putString("refreshToken", refreshToken);
        edit.putBoolean("Login", true);
        edit.apply();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void api(String mobile, String code) {
//        VerifyOTPRequest request = new VerifyOTPRequest();
//        request.to = "+91" + mobile;
//        request.code = code;
//        request.pathServiceSid = serviceSid;
        Log.i("checkmodeldataCheck", "api sign up top mobile = " + mobile);
        Log.i("checkmodeldataCheck", "api  sign up top code = " + code);
        Log.i("checkmodeldataCheck", "api  sign up top serviceSid = " + serviceSid);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,true);
        service.verifyOTPs(serviceSid,code).enqueue(new Callback<VerifyOTPResponse>() {
            @Override
            public void onResponse(Call<VerifyOTPResponse> call, Response<VerifyOTPResponse> response) {
                Log.i("checkmodeldataCheck", " sign up top response 0 code = " + response.code());
                Log.i("checkmodeldataCheck", " sign up otp verify response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.i("checkmodeldatasignup", " sign up top LoginNewResponse response = " + String.valueOf(response.body()));

                    signupapi();
                } else {
                    messVoid("गलत OTP।");
                  //  Toast.makeText(SignupOTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldatasignup", " sign up top response 1 code = " + response.code());
                    etOne.setEnabled(true);
                    etTwo.setEnabled(true);
                    etThree.setEnabled(true);
                    etFour.setEnabled(true);
                    etFive.setEnabled(true);
                    etSix.setEnabled(true);

                    pb.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<VerifyOTPResponse> call, Throwable t) {
                Log.i("checkmodeldatasignup", " sign up top error message response  = " + t.getMessage());
            }
        });
    }
    public void messVoid(String message){
        //handle menu2 click
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupOTPActivity.this, R.style.AlertDialogTheme);
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
    public void signupapi() {
        SignUpRequestModel request = new SignUpRequestModel();
        request.patient_phone = "+91" + mMobile;
        request.patient_age = sharedPreferences.getString("Page", "");
        request.patient_gender = sharedPreferences.getString("Pgender", "");
        request.patient_id = sharedPreferences.getString("karmaPid", "");
        request.patient_name = sharedPreferences.getString("Pname", "");


        request.patient_createdts = "null";
        request.patient_updatedts = "null";
        request.patient_dob = "null";
        request.patient_location = "null";
        request.patient_email = "null";
        request.patient_village = "null";
        request.password = sharedPreferences.getString("karmaPid", "");


        Log.i("checkmodeldatasignup", "api signup mobile = " + mMobile);
        Log.i("checkmodeldatasignup", "api signup age = " + sharedPreferences.getString("Page", ""));
        Log.i("checkmodeldatasignup", "api signup serviceSid = " + serviceSid);
        Log.i("checkmodeldatasignup", "api signup request = " + new Gson().toJson(request));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.signup(request).enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call, Response<SignUpResponseModel> response) {
                Log.i("checkmodeldatasignup", "api signup 0 code = " + response.code());

                if (response.isSuccessful()) {
                    edit.putString("kymPid", response.body().id);
                    edit.apply();
                    //      Log.i("checkmodeldata", "api LoginNewResponse response = " + response.body().status);
                    onClickNew(response.body().token,response.body().refreshToken);

                } else {
                    etOne.setEnabled(true);
                    etTwo.setEnabled(true);
                    etThree.setEnabled(true);
                    etFour.setEnabled(true);
                    etFive.setEnabled(true);
                    etSix.setEnabled(true);

                    pb.setVisibility(View.GONE);
                    messVoid("गलत OTP कृपया पुन: प्रयास करें।");
                   // Toast.makeText(SignupOTPActivity.this, "गलत OTP कृपया पुन: प्रयास करें।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldatasignup", "api signup 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call, Throwable t) {
                Log.i("checkmodeldatasignup", "api signup error message response  = " + t.getMessage());
            }
        });
    }

    public void onClick(View view) {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }
}