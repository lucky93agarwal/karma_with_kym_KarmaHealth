package com.devkraft.karmahealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.os.Bundle;
import android.util.Log;

import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateStr = df.format(c);

        String time = "00:43:00";


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
            AppUtils.setAlarmForTime(getApplicationContext(), time, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, Constants.DAILY, null,"response.body().nightDrugEndDate");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
}