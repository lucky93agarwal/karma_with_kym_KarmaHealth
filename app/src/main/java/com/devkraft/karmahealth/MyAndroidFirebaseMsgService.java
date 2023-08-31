package com.devkraft.karmahealth;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.devkraft.karmahealth.Screen.HomeActivity;
import com.devkraft.karmahealth.Screen.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final String CHANNEL_NAME = "Main Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notification description";
    static  String patient;
    public static  int IS_NOTIFICATION = 0  ;
    public static  int IS_NOTIFICATION_CHAT = 0  ;
    String arraydata, shownotification, HistoryID;
    PendingIntent contentIntent;
    SharedPreferences prefs;
    Intent intent;
    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        prefs = getSharedPreferences("UserDatasss", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("ftoken", refreshedToken);
        edit.apply();
        Log.i("luresponsehistoryyt", "response history firebase token 8  = " + refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.i("luresponsehistoryyt", "response history firebase token 8  = " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Gson g = new Gson();
        Map<String, String> data = remoteMessage.getData();


        if(remoteMessage.getNotification().getImageUrl()!=null){
            Log.i("luresponsehistoryyt","image");
            new generatePictureStyleNotification(this,remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),
                    String.valueOf(remoteMessage.getNotification().getImageUrl())).execute();
        }else {
            Log.i("luresponsehistoryyt","just test");
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Log.i("luresponsehistoryyt","upload data api S requestCode =");
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            } else {
                Log.i("luresponsehistoryyt","upload data api not S requestCode =");
                pendingIntent = PendingIntent.getActivity(this, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
            }
            String channelId = "default_channel_id";


            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(new long[]{500, 500, 500})
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);








            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i("luresponsehistoryyt", "firebase notificcation LOLLIPOP =");
                builder.setSmallIcon(R.mipmap.newlogo);
                builder.setColor(getApplicationContext().getColor(R.color.colorAccent));
                builder.setColorized(true);
            } else {
                Log.i("luresponsehistoryyt", "firebase notificcation less =");
                builder.setSmallIcon(R.mipmap.logoicon);
                builder.setColor(getApplicationContext().getColor(R.color.colorAccent));
                builder.setColorized(true);
            }
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
        }
        Log.i("luresponsehistoryyt", "firebase notificcation response = " + String.valueOf(data));


    }



    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            String channelId = "default_channel_id";


            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Log.i("luresponsehistoryyt","upload data api S requestCode =");
                pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_MUTABLE);
            } else {
                Log.i("luresponsehistoryyt","upload data api not S requestCode =");
                pendingIntent = PendingIntent.getActivity(mContext, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationCompat.Builder builder = new  NotificationCompat.Builder(mContext, channelId)
                    .setVibrate(new long[]{500, 500, 500})
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(title)
                    .setLargeIcon(result)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result).bigLargeIcon(null))
                    .setContentText(message).setAutoCancel(true).setContentIntent(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i("luresponsehistoryyt", "firebase notificcation LOLLIPOP -+9+ =");
                builder.setSmallIcon(R.mipmap.newlogo);
                builder.setColor(getApplicationContext().getColor(R.color.colorAccent));
                builder.setColorized(true);
            } else {
                Log.i("luresponsehistoryyt", "firebase notificcation less =");
                builder.setSmallIcon(R.mipmap.logoicon);
                builder.setColorized(true);
            }
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());

        }
    }
}
