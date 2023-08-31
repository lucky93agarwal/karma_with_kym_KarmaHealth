package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.devkraft.karmahealth.DownloadPDF;
import com.devkraft.karmahealth.R;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import kotlin.random.Random;

public class DrPrescriptionDownloadActivity extends AppCompatActivity {
    private WebView webView;
    public ProgressBar progressBar;
    private String url;
    private ImageView ivdownload, ivBack;
    private final int REQUEST_PERMISSION_PHONE_STATE=121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //
        setContentView(R.layout.activity_dr_prescription_download);
        showPhoneStatePermission();
        init();
        getData();
        onClick();
    }

    public void onClick() {
        ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Data.Builder d = new Data.Builder();
//                d.putString("file_path", url);
//                WorkRequest workRequest = new OneTimeWorkRequest.Builder(DownloadPDF.class).setInputData(d.build()).build();
//                WorkManager.getInstance(DrPrescriptionDownloadActivity.this).enqueue(workRequest);
//                Log.i("checkurldownload", "url = " + url);

                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .mkdirs();
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
                        .setTitle("Dr Prescription")// Title of the Download Notification
                        .setDescription("Downloading")// Description of the Download Notification
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                "Prescription.pdf")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                        .setRequiresCharging(false)// Set if charging is required to begin the download
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(false);// Set if download is allowed on roaming network

                DownloadManager downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                long  lDownloadID = downloadManager.enqueue(request);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showPhoneStatePermission() {//WRITE_EXTERNAL_STORAGE
        int permissionCheckSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheckSMS != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_PHONE_STATE);
            }
        } else {
            //    Toast.makeText(LoginActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            Log.i("checkbackbtn", "onBack 1 ");
        } else {
            super.onBackPressed();
            Log.i("checkbackbtn", "onBack 2 ");
        }
    }

    public void init() {
        ivdownload = (ImageView) findViewById(R.id.imageView4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivBack = (ImageView) findViewById(R.id.imageView3);
//        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        webView = (WebView) findViewById(R.id.webview);


        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, String url) {
                url = getIntent().getStringExtra("url");
                Log.i("urlgetdata", "get url data = " + url);
                view.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
                return (false);
            }
        });
        webView.setWebViewClient(new Callback());

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }

    public void getData() {
        url = getIntent().getStringExtra("url");
        Log.i("urlgetdata", "get url data = " + url);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
    }
}