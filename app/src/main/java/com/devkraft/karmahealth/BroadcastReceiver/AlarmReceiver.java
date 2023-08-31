package com.devkraft.karmahealth.BroadcastReceiver;

import static android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED;
import static android.content.Context.MODE_PRIVATE;

import com.devkraft.karmahealth.BuildConfig;
import com.devkraft.karmahealth.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Screen.HomeActivity;
import com.devkraft.karmahealth.Screen.SplashActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.db.AlarmTable;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.db.PrescriptionRefillTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String GROUP_KEY = "com.android.reminder";
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        //sharedPreferences = context.getSharedPreferences("userData",MODE_PRIVATE);
        Log.i("checkmodeldashb", "AlarmReceiver 786 = ");
        if (intent.getAction().equalsIgnoreCase(ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)) {
            Toast.makeText(context, "special access granted..!", Toast.LENGTH_SHORT).show();
        }
        Log.i("checkmodeldashb", "AlarmReceiver = ");
        Log.i("checkmodeldashb", " = " + intent);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        String time = intent.getStringExtra(Constants.TIME);
        String reminderType = intent.getStringExtra(Constants.REMINDER_TYPE);
        String dataToCompare = intent.getStringExtra(Constants.DATA_PARA);
        String prescriptionRefill = intent.getStringExtra(Constants.PRESCRIPTION_REFILL);
        String endDate = intent.getStringExtra(Constants.END_DATE);
        Log.i("checkmodeldashb", "AlarmReceiver time = " + time);
        Log.i("checkmodeldashb", "AlarmReceiver END_DATE = " + endDate);
        Log.i("checkmodeldashb", "AlarmReceiver reminderType = " + reminderType);
        Log.i("checkmodeldashb", "AlarmReceiver dataToCompare = " + dataToCompare);
        Log.i("checkmodeldashb", "AlarmReceiver prescriptionRefill = " + prescriptionRefill);
        //    Log.i("checkmodeldashboardnew", "AlarmReceiver time share = " +sharedPreferences.getString("MTime","09:00:00"));



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateStr = df.format(c);
        try {
            Date todayDate = sdf.parse(startDateStr);
            Date endDates = sdf.parse(endDate);
            if (todayDate.getTime() <= endDates.getTime()) {
                Log.i("checkmodeldashb", "step  = 0 ");
                processedToNotification(context, time, context.getString(R.string.take_daily_meds));
            } else {
                Log.i("checkmodeldashb", "step  = 1 ");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // processedToNotification(context, time, context.getString(R.string.take_daily_meds));


    }

    private void processedToPrescriptionNotification(Context context, Cursor cursor) {

        if (cursor != null && cursor.moveToFirst()) {
            Log.i("checkmodeldashboard", "AlarmReceiver processedToPrescriptionNotification = 1");
            String title1 = cursor.getString(cursor.getColumnIndex(PrescriptionRefillTable.TITLE));
            String subTitle = cursor.getString(cursor.getColumnIndex(PrescriptionRefillTable.SUB_TITLE));
            String time1 = cursor.getString(cursor.getColumnIndex(PrescriptionRefillTable.TIME));
            showNotification(context, title1, subTitle, time1, null, false, "");
        }
    }

    private void processedToNotification(Context context, String time1, String Message) {
        Log.i("checkmodeldashboard", "AlarmReceiver prescriptionRefill = 11");
        showNotification(context, "आप कि दवा खाने का समय हो गया है। ", "दवा खाने का समय", time1, null, false, "");
        /*if (cursor != null && cursor.moveToFirst()) {
            do {
                String title1 = cursor.getString(cursor.getColumnIndex(AlarmTable.TITLE));
                String subTitle1 = cursor.getString(cursor.getColumnIndex(AlarmTable.SUB_TITLE));
                String time1 = cursor.getString(cursor.getColumnIndex(AlarmTable.TIME));
                Long userId = cursor.getLong(cursor.getColumnIndex(AlarmTable.USER_ID));
                if (time1.split(":")[1].matches("03") ||
                        time1.split(":")[1].matches("05")) {
                    if (time1.split(":")[1].matches("03")) {
                        time1 = time1.replaceAll("03", "00");
                    }
                    if (time1.split(":")[1].matches("05")) {
                        time1 = time1.replaceAll("05", "00");
                    }
                }
                if (title1 != null) {
                    *//*title1 = context.getResources().getString(R.string.hey) +
                            context.getResources().getString(R.string.space) +
                            title1 + "!" + context.getResources().getString(R.string.space)
                            .concat(Message);*//*

                    boolean isRemindFlag = (cursor.getInt(cursor.getColumnIndex(AlarmTable.REMIND_FLAG)) == 1);
                    Log.e("isRemindFlag_log", " = " + isRemindFlag);
                    if (isRemindFlag) {
                        showNotification(context, title1, subTitle1, time1, userId + "", true, Message);
                    }
                }
            } while (cursor.moveToNext());
        }*/
    }

    private void showNotification(Context context, String title, String subTitle,
                                  String time, String userId, boolean isMedicine, String message) {

        subTitle = AppUtils.getEncodedString(subTitle);

        Intent notificationIntent = new Intent(context, SplashActivity.class);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (userId != null) {
            notificationIntent.putExtra(Constants.FROM_WHERE, Constants.FROM_WHERE);
            notificationIntent.putExtra(Constants.NOTIFICATION_USER_ID, userId);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(notificationIntent);


        int num = (int) System.currentTimeMillis();

        if (num < 0) {
            num = num * -1;
        }

//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(num, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = stackBuilder.getPendingIntent(num, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = stackBuilder.getPendingIntent(num, PendingIntent.FLAG_UPDATE_CURRENT);
        }


//        String id = "notification_channel_id";
        String id = "notification_channel_id_1";
        // The user-visible name of the channel.
        CharSequence name = "Message_Notification";
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Intent takenIntent = new Intent(context, NotificationReceiver.class);
        takenIntent.setAction(Constants.TAKEN);
        takenIntent.putExtra(Constants.TIME, time);
        takenIntent.putExtra(Constants.SCHEDULE_DATE, AppUtils.getTodayDateForSchedule());
        takenIntent.putExtra(Constants.NOTI_ID, num);

        PendingIntent takenPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            takenPendingIntent = PendingIntent.getBroadcast(context, num, takenIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            takenPendingIntent = PendingIntent.getBroadcast(context, num, takenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent notTakenIntent = new Intent(context, NotificationReceiver.class);
        notTakenIntent.setAction(Constants.NOT_TAKEN);
        notTakenIntent.putExtra(Constants.TIME, time);
        notTakenIntent.putExtra(Constants.SCHEDULE_DATE, AppUtils.getTodayDateForSchedule());
        notTakenIntent.putExtra(Constants.NOTI_ID, num);

        PendingIntent notTakenIntentPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notTakenIntentPendingIntent = PendingIntent.getBroadcast(context, num, notTakenIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            notTakenIntentPendingIntent = PendingIntent.getBroadcast(context, num, notTakenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, id)
                .setAutoCancel(true)
//                .setSound(uri)
                .setGroupSummary(true)
                .setGroup(GROUP_KEY)
//                .setSound(soundUri)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/raw/shking_tab_long"))
                .setContentIntent(pendingIntent)
                .setChannelId(id);


        if (isMedicine) {
            notificationBuilder.setContentTitle("It's time to take your Medicine!");
            notificationBuilder.setContentText(context.getResources().getString(R.string.its) + " " +
                    AppUtils.get12HrsDate(time) + " ," +
                    context.getResources().getString(R.string.just_sending_an_remainder_for)
                    + " " + title + " " + message);
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(context.getResources().getString(R.string.its) + " " +
                            AppUtils.get12HrsDate(time) + "," +
                            context.getResources().getString(R.string.just_sending_an_remainder_for)
                            + " " + title + " " + message));

        } else {
            notificationBuilder.setContentText(title + context.getResources()
                    .getString(R.string.space) + AppUtils.get12HrsDate(time));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(title + context.getResources()
                    .getString(R.string.space) + AppUtils.get12HrsDate(time)));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.newlogo);
            notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.logoicon);
        }

        Log.e("soundUri", " = " + Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/raw/shking_tab_long"));

        /*if (subTitle != null) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            inboxStyle.addLine(AppUtils.get12HrsDate(time));

            if (subTitle.contains("&&&")) {

                String[] splitStrings = subTitle.split("&&&");

                for (int i = 0; i < splitStrings.length; i++) {
                    String msg = splitStrings[i].replace("-", " - ");
                    //inboxStyle.addLine(splitStrings[i]);
                    inboxStyle.addLine(msg);
                }
            } else {
                inboxStyle.addLine(subTitle);
            }

            // Moves the expanded layout object into the notification object.
            notificationBuilder.setStyle(inboxStyle);
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            notificationBuilder.setLargeIcon(bm);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // The user.-visible description of the channel.
            String description = context.getString(R.string.messages_sent_by_kym_team);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);

            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);

            mChannel.setSound(
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/raw/shking_tab_long"),
                    Notification.AUDIO_ATTRIBUTES_DEFAULT
            ); // This is IMPORTANT


            Log.e("channel_log", " = " + mChannel.getSound());

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        // Sets an ID for the notification

        // Gets an instance of the NotificationManager service
        final NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        Log.e("mNotifyMgr", " = " + mNotifyMgr);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(num, notificationBuilder.build());
        }
    }
}

