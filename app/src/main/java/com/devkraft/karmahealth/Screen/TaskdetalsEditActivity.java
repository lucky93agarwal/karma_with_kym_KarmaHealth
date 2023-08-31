package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskdetalsEditActivity extends AppCompatActivity {
    public TextView tvapptitletv;
    public LinearLayout lldatell, lltimell;
    public TextView tvdatetv, tvtimetv;
    public ImageView ivBack;

    // date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetals_edit);

        init();
        onClick();
        setData();
    }

    public void onClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lldatell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lltimell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void init() {


        tvtimetv = (TextView) findViewById(R.id.timetv);
        tvdatetv = (TextView) findViewById(R.id.datetv);

        lltimell = (LinearLayout) findViewById(R.id.timell);
        lldatell = (LinearLayout) findViewById(R.id.datell);
        ivBack = (ImageView) findViewById(R.id.imageView3);
        tvapptitletv = (TextView) findViewById(R.id.apptitletv);
    }

    public void setData() {
        tvapptitletv.setText(getIntent().getStringExtra("title") + " को ट्रैक करें");
    }
}