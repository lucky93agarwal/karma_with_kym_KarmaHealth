package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.PatientIdModel;
import com.devkraft.karmahealth.Model.ValidationLoginResponse;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.razorpay.ParchesCodeActivity;
import com.devkraft.karmahealth.retrofit.ServiceGenerator;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText etMobie;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    private AppCompatButton tvSignin;
    AppCompatButton button22;
    private TextView privtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        change();
        onClick();
    }

    public void onClick(){
        tvSignin.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        privtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://karmahealthcare.in/privacy-policy.php"));
                startActivity(browserIntent);
            }
        });
    }
    public void init(){
        privtv = (TextView)findViewById(R.id.priv);
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        tvSignin = (AppCompatButton)findViewById(R.id.textView8);
        button22 = (AppCompatButton)findViewById(R.id.button2);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etMobie = (EditText)  findViewById(R.id.editTextPhone);
    }
    public void change(){
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

    public void snack(String msg){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout,msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    public void loginbtn(View view) {
        button22.setEnabled(false);
        View views = this.getCurrentFocus();
        if (views != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService("input_method");
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(etMobie.getText().length() <= 1){
            button22.setEnabled(true);
            messVoid(getResources().getString(R.string.entertene));
          //  snack(getResources().getString(R.string.entertene));
            etMobie.setBackgroundResource(R.drawable.rededitbac);
            etMobie.setTextColor(getResources().getColor(R.color.red));
        }else{
            api();

        }
    }
    public void messVoid(String message){
        //handle menu2 click
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignupActivity.this, R.style.AlertDialogTheme);
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
    public void api(){
        Log.i("checkmodeldata", "api LoginNewResponse request = " + etMobie.getText().toString());
        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        HashMap<String,String> header =new HashMap<String, String>();
        header.put("seckey","znob1zj8z9zgdebrobd8y3apdtnj9zgo7lv55f5uh9mpx724gl6900ssdinuht9kwxanr1zf1sgjn848o1m1cnkcxbamwfzulo45w34npt4k3ubude60ipslql1e8fs1lv6yx0f551nydq9r2d0qjq1fzxtu1ir73wjbc");

        service.karmaValidation(etMobie.getText().toString(),header).enqueue(new Callback<ValidationLoginResponse>() {
            @Override
            public void onResponse(Call<ValidationLoginResponse> call, Response<ValidationLoginResponse> response) {
                Log.i("checkmodeldata", "api response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldata", "api LoginNewResponse response = " + new Gson().toJson(response.body()));

                    if(response.body().success){
                        button22.setEnabled(true);
                        edit.putString("karmaPid", response.body().data.get(0).patient_id);
                        edit.putString("Pname", response.body().data.get(0).patient_name);
                        edit.putString("Page", response.body().data.get(0).patient_age);
                        edit.putString("Pgender", response.body().data.get(0).patient_gender);
                        edit.putString("Pphone", response.body().data.get(0).patient_phone);
                        edit.apply();
                        if(response.body().data.get(0).patient_phone.equalsIgnoreCase("0")){
                           // snack("आप का मोबाइल नंबर रजिस्टर नहीं है। कृपया क्लिनिक से संपर्क करें।");
                            messVoid("आप का मोबाइल नंबर रजिस्टर नहीं है। कृपया क्लिनिक से संपर्क करें।");
                        }else {
                            if(response.body().data.get(0).patient_phone.length()>4){
                                apicheck();
                            }else {
                                messVoid("आप का मोबाइल नंबर रजिस्टर नहीं है। कृपया क्लिनिक से संपर्क करें।");
                               // snack("आप का मोबाइल नंबर रजिस्टर नहीं है। कृपया क्लिनिक से संपर्क करें।");
                            }
                        }
                       // Intent intent = new Intent(SignupActivity.this, ParchesCodeActivity.class);

                        /*finish();*/
                    }else {
                        button22.setEnabled(true);
                        messVoid("मरीज नहीं मिला।");
                     //   snack("मरीज नहीं मिला।");
                    }
//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    button22.setEnabled(true);
                    messVoid("यूजर आईडी पहले से मौजूद है।");
                   // snack("यूजर आईडी पहले से मौजूद है।");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<ValidationLoginResponse> call, Throwable t) {
                button22.setEnabled(true);
                messVoid(t.getMessage());
//                snack(t.getMessage());
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }


    public void apicheck(){
        Log.i("PatientIdCheck", "api LoginNewResponse request = " + etMobie.getText().toString());
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);

        service.patentIdCheck(etMobie.getText().toString()).enqueue(new Callback<PatientIdModel>() {
            @Override
            public void onResponse(Call<PatientIdModel> call, Response<PatientIdModel> response) {
                Log.i("PatientIdCheck", "api response 0 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("PatientIdCheck", "api PatientIdCheck response = " + new Gson().toJson(response.body()));

                    if(!response.body().validation){
                        button22.setEnabled(true);

                        // Intent intent = new Intent(SignupActivity.this, ParchesCodeActivity.class);
                        Intent intent = new Intent(SignupActivity.this, SignUpMobileActivity.class);
                        startActivity(intent);
                        /*finish();*/
                    }else {
                        button22.setEnabled(true);
                        messVoid("यूजर आईडी पहले से मौजूद है।");
                       // snack("यूजर आईडी पहले से मौजूद है।");
                    }
//                    mProductList.addAll(response.body().data);
//                    setData();
                } else {
                    button22.setEnabled(true);
                    messVoid("यूजर आईडी पहले से मौजूद है।");
                   // snack("यूजर आईडी पहले से मौजूद है।");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<PatientIdModel> call, Throwable t) {
                button22.setEnabled(true);
                messVoid(t.getMessage());
//                snack(t.getMessage());
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }
}