package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;

import com.devkraft.karmahealth.R;

public class InfoActivity extends AppCompatActivity {
    private WebView webView;
    public ProgressBar progressBar;
    private String url;
    private ImageView ivdownload,ivBack;
    private TextView textView8tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //
        setContentView(R.layout.activity_info);

        init();
        getData();
        onClick();
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    public void onClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
                        .setMimeType("application/pdf")
                        .setTitle("Dr Prescription")// Title of the Download Notification
                        .setDescription("Downloading")// Description of the Download Notification
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                        .setRequiresCharging(false)// Set if charging is required to begin the download
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
                request.allowScanningByMediaScanner();
                DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                long  lDownloadID = downloadManager.enqueue(request);
            }
        });
    }


    public void init(){
        textView8tv = (TextView)findViewById(R.id.textView8);
        ivdownload = (ImageView)findViewById(R.id.imageView4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivBack = (ImageView)findViewById(R.id.imageView3);
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
        webView.setWebViewClient(new WebViewClient());

    }
    public void getData(){
        url = getIntent().getStringExtra("url");
        textView8tv.setText(getIntent().getStringExtra("title"));
        Log.i("urlgetdata","get url data = "+url);
        webView.loadUrl(url);
    }
}