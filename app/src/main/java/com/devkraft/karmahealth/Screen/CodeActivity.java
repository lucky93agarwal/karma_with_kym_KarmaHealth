package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import com.devkraft.karmahealth.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CodeActivity extends AppCompatActivity {
    private String mMobile = "";
    private TextView mtvMobile;

    private ProgressBar pb;

    private EditText etOne, etTwo, etThree, etFour, etFive, etSix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        getData();
        init();
        setData();

        textwatcher();
    }

    public void getData(){
        Intent intent = getIntent();
        mMobile = intent.getStringExtra("mobile");
    }
    public void init(){
        pb = (ProgressBar)findViewById(R.id.progresspb);
        mtvMobile = (TextView) findViewById(R.id.textView5);

        etOne = (EditText) findViewById(R.id.editTextPhone2);
        etTwo = (EditText) findViewById(R.id.editTextPhone3);
        etThree = (EditText) findViewById(R.id.editTextPhone4);
        etFour = (EditText) findViewById(R.id.editTextPhone5);
        etFive = (EditText) findViewById(R.id.editTextPhone6);
        etSix = (EditText) findViewById(R.id.editTextPhone7);
    }
    public void setData(){
        mtvMobile.setText(getResources().getString(R.string.hmne)+" "+mMobile+getResources().getString(R.string.fmno));
    }


    public void textwatcher(){
        etTwo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace

                    if(etSix.getText().length() !=0){
                        etSix.requestFocus();
                    } else if(etFive.getText().length() !=0){
                        etFive.requestFocus();
                    } else if(etFour.getText().length() !=0){
                        etFour.requestFocus();
                    } else if(etThree.getText().length() !=0){
                        etThree.requestFocus();
                    } else if(etTwo.getText().length() !=0){
                        etTwo.requestFocus();
                    }else if(etOne.getText().length() !=0){
                        etOne.requestFocus();
                    }else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etThree.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(etSix.getText().length() !=0){
                        etSix.requestFocus();
                    } else if(etFive.getText().length() !=0){
                        etFive.requestFocus();
                    } else if(etFour.getText().length() !=0){
                        etFour.requestFocus();
                    } else if(etThree.getText().length() !=0){
                        etThree.requestFocus();
                    } else if(etTwo.getText().length() !=0){
                        etTwo.requestFocus();
                    }else if(etOne.getText().length() !=0){
                        etOne.requestFocus();
                    }else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etFour.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(etSix.getText().length() !=0){
                        etSix.requestFocus();
                    } else if(etFive.getText().length() !=0){
                        etFive.requestFocus();
                    } else if(etFour.getText().length() !=0){
                        etFour.requestFocus();
                    } else if(etThree.getText().length() !=0){

                        etThree.requestFocus();
                    } else if(etTwo.getText().length() !=0){
                        etTwo.requestFocus();
                    }else if(etOne.getText().length() !=0){
                        etOne.requestFocus();
                    }else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etFive.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(etSix.getText().length() !=0){
                        etSix.requestFocus();
                    } else if(etFive.getText().length() !=0){
                        etFive.requestFocus();
                    } else if(etFour.getText().length() !=0){
                        etFour.requestFocus();
                    } else if(etThree.getText().length() !=0){

                        etThree.requestFocus();
                    } else if(etTwo.getText().length() !=0){
                        etTwo.requestFocus();
                    }else if(etOne.getText().length() !=0){
                        etOne.requestFocus();
                    }else {
                        etOne.requestFocus();
                    }
                }
                return false;
            }
        });
        etSix.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(etSix.getText().length() !=0){
                        etSix.requestFocus();
                    } else if(etFive.getText().length() !=0){
                        etFive.requestFocus();
                    } else if(etFour.getText().length() !=0){
                        etFour.requestFocus();
                    } else if(etThree.getText().length() !=0){
                        etThree.requestFocus();
                    } else if(etTwo.getText().length() !=0){
                        etTwo.requestFocus();
                    }else if(etOne.getText().length() !=0){
                        etOne.requestFocus();
                    }else {
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
                if(etOne.getText().length() >0){
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
                if(etTwo.getText().length() >0){
                    etThree.requestFocus();
                }else {
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
                if(etThree.getText().length() >0){
                    etFour.requestFocus();
                }else {
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
                if(etFour.getText().length() >0){
                    etFive.requestFocus();
                }else {
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
                if(etFive.getText().length() >0){
                    etSix.requestFocus();
                }else {
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
                if(etSix.getText().length() >0){
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
                            onClickNew();

                        }
                    }, 3000);


                }else {
                    etFive.requestFocus();
                }
            }
        });
    }
    public void onClickNew() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }

}