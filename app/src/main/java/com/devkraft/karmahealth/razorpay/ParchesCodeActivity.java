package com.devkraft.karmahealth.razorpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.devkraft.karmahealth.R;
import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class ParchesCodeActivity extends AppCompatActivity implements PaymentResultListener {
    private EditText etMobie;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    AppCompatButton button22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parches_code);

        init();

        change();
    }

    public void init(){
        Checkout.preload(getApplicationContext());
        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        button22 = (AppCompatButton)findViewById(R.id.button2);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etMobie = (EditText)  findViewById(R.id.editTextPhone);

        /**
         * Preload payment resources
         */

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
                etMobie.setTextColor(getResources().getColor(R.color.text_color));
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
        if(etMobie.getText().length() != 7){
            button22.setEnabled(true);
            snack("Please enter 7 digit Promo Code");
            etMobie.setBackgroundResource(R.drawable.rededitbac);
            etMobie.setTextColor(getResources().getColor(R.color.red));
        }else if(etMobie.getText().toString().equalsIgnoreCase("1234567")){
            etMobie.setText("");
            api();
        }else{

            button22.setEnabled(true);
            snack("Please enter valid Promo Code");
            etMobie.setBackgroundResource(R.drawable.rededitbac);
            etMobie.setTextColor(getResources().getColor(R.color.red));
        }
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        /**
         * Add your logic here for a successful payment response
         */
        button22.setEnabled(true);
        snack("Payment is successful : " + razorpayPaymentID);
    }

    @Override
    public void onPaymentError(int code, String response) {
        snack("Payment Failed due to error : " + response);
        Log.i("kdatanow", "Error in starting Razorpay Checkout = "+ response.toString());
        Log.i("kdatanow", "Error in starting Razorpay Checkout 2 = "+ String.valueOf(code));
        /**
         * Add your logic here for a failed payment response
         */
        button22.setEnabled(true);
    }

    public void api(){
     //   Checkout.preload(getApplicationContext());

        // ...


        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_1DP5mmOlF5G5ag");
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */


        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Lucky Agarwal");
            options.put("description", "Test Payment");
            options.put("currency", "INR");
            options.put("amount", "25000");
//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "agarwal.lucky93@gmail.com");
            preFill.put("contact", "8840149029");
            options.put("prefill", preFill);
            checkout.open(ParchesCodeActivity.this, options);

        } catch(Exception e) {
            Log.i("kdatanow", "Error in starting Razorpay Checkout", e);
        }

    }
}