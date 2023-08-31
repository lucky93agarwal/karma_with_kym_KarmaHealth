package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.Constants;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class NotificationDetailActivity extends BaseActivity {
    private TextView mNotificationTitleTv, mNotificationDetailsTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        setUpToolbar();
        intIds();
        getIntentValue();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.inbox));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));
    }

    private void intIds() {
        mNotificationTitleTv = findViewById(R.id.notification_title_tv);
        mNotificationDetailsTv = findViewById(R.id.notification_details_tv);
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        if (intent != null) {
            mNotificationTitleTv.setText(intent.getStringExtra(Constants.TITLE));
            mNotificationDetailsTv.setText(intent.getStringExtra(Constants.SUB_TITLE));
        }
    }
}