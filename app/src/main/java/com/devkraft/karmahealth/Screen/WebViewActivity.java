package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Model.CustomProgressDialog;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.BuildConfig;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    WebView webViewHomePage;
    CustomProgressDialog customProgressDialog;
    ProgressBar progressBar;
    private boolean isFromTestVaccinesUpdate;
    String fromWhereStr, userDtoStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        setUpToolbar();

        webViewHomePage = findViewById(R.id.web_view_home_page);

        progressBar = findViewById(R.id.progress_bar_home_web_view);
        progressBar.setScaleY(2.5f);
        webViewHomePage.setWebChromeClient(new WebChromeClient());
        customProgressDialog = new CustomProgressDialog(this);

        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra(Constants.URL);
            String title = intent.getStringExtra(Constants.TITLE);
            isFromTestVaccinesUpdate = intent.getBooleanExtra(Constants.IS_TEST_VACCINES_UPDATE, false);
            fromWhereStr = intent.getStringExtra(Constants.FROM_WHERE);
            userDtoStr = intent.getStringExtra(Constants.USER_DTO);

            getSupportActionBar().setTitle(title);
            if (url != null) {
                if(url.matches(BuildConfig.FAQ_URL)){
                    AppUtils.logCleverTapEvent(WebViewActivity.this,
                            Constants.FAQS_PAGE_OPENED, null);
                }
                startWebView(url);
            }
        }


        setupClickEvents();

     //   Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
    }


    private void setupClickEvents() {
        webViewHomePage.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, String url) {
                try {
                    Log.e("onCheckout_click"," = "+url);
                    if (url.contains("paymentsuccess")) {
                        showPaymentSucessDialog();
                        webViewHomePage.goBack();
                        return true;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void showPaymentSucessDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View deletePopupView = getLayoutInflater().inflate(R.layout.payment_sucess_dialog_box, null);

        Button mButtonYes = deletePopupView.findViewById(R.id.button_ok);

        dialogBuilder.setView(deletePopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.setCancelable(false);

        mButtonYes.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle("Drug Details");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startWebView(String url) {
        Log.e("url_log"," = "+url);
        //Create new webview Client to show progress dialog
        progressBar.setVisibility(View.VISIBLE);
        webViewHomePage.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                try {
                    progressBar.setVisibility(View.GONE);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        // Javascript enabled on webview
        webViewHomePage.getSettings().setJavaScriptEnabled(true);
        webViewHomePage.getSettings().setLoadWithOverviewMode(true);
        webViewHomePage.getSettings().setAllowContentAccess(true);
        webViewHomePage.getSettings().setLoadsImagesAutomatically(true);
        webViewHomePage.getSettings().setLoadWithOverviewMode(true);
        webViewHomePage.getSettings().setDisplayZoomControls(false);
        webViewHomePage.getSettings().setAppCacheEnabled(true);
        webViewHomePage.getSettings().setSupportMultipleWindows(true);
        webViewHomePage.getSettings().setUseWideViewPort(true);
        webViewHomePage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webViewHomePage.setScrollbarFadingEnabled(false);
        webViewHomePage.getSettings().setBuiltInZoomControls(true);


        //webViewHomePage.loadData(Constants.HTML_STRING,"text/html",null);

        //webViewHomePage.loadDataWithBaseURL(null,Constants.HTML_STRING,"text/html","base64",null);

        //Load url in webView
        webViewHomePage.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        performBackAction();
    }

    private void performBackAction() {
        if (isFromTestVaccinesUpdate) {
            Intent intent = new Intent(WebViewActivity.this, HomeActivity.class);
            intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
            intent.putExtra(Constants.FROM_WHERE, fromWhereStr);
            intent.putExtra(Constants.USER_DTO, userDtoStr);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            performBackAction();
        }
        return super.onOptionsItemSelected(item);
    }
}