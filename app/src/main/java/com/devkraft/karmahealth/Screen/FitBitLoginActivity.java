package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

public class FitBitLoginActivity extends AppCompatActivity {
    private WebView webViewHomePage;
    private ProgressBar progressBar;
    private ImageView mImageBack;
    private boolean isAccessTokenGet = false, isAccessDenied = false;
    private String fitBitScopSelectionScreen = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_bit_login);
        AppUtils.changeStatusBarColor(FitBitLoginActivity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeIds();
        setOnClickListener();
        initializeFitBitUrl();
    }

    private void initializeIds() {
        webViewHomePage = findViewById(R.id.web_view_home_page);
        progressBar = findViewById(R.id.progress_bar_home_web_view);
        progressBar.setScaleY(2.5f);
        mImageBack = findViewById(R.id.image_back);
    }

    private void setOnClickListener() {
        webViewHomePage.setWebChromeClient(new WebChromeClient());
        mImageBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
        startActivity(new Intent(this, AppsDevicesActivity.class));
        FitBitLoginActivity.this.finish();
    }

    private void initializeFitBitUrl() {
        String clientID = "22C82H";
        String redirectUrl = "https://www.google.com/";
        String scope = "heartrate%20nutrition%20profile%20sleep%20weight";
        String getTokenURL = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id="
                + clientID + "&redirect_uri=" + redirectUrl + "&scope=" + scope + "&expires_in=31536000";
        fitBitScopSelectionScreen = getTokenURL;
        startWebView(getTokenURL);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startWebView(String url) {
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

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().contains("#access_token") && !isAccessTokenGet) {
                    parseAccessTokenFromUrl(request.getUrl().toString());
                } else if (request.getUrl().toString().contains("access_denied") && !isAccessDenied) {
                    isAccessDenied = true;
                    ApplicationPreferences.get().saveStringValue("FitBit-Connected", "false");
                    startActivity(new Intent(FitBitLoginActivity.this, AppsDevicesActivity.class));
                    FitBitLoginActivity.this.finish();
                } else if (request.getUrl().toString().contains("https://www.knowyourmeds-no.com")) {
                    isAccessDenied = true;
                    ApplicationPreferences.get().saveStringValue("FitBit-Connected", "false");
                    startActivity(new Intent(FitBitLoginActivity.this, AppsDevicesActivity.class));
                    FitBitLoginActivity.this.finish();
                } else if (request.getUrl().toString().contains("https://www.knowyourmeds-yes.com")) {
                    webViewHomePage.loadUrl(fitBitScopSelectionScreen);
                }
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }
        });
        // Javascript enabled on webview
        webViewHomePage.getSettings().setJavaScriptEnabled(true);
        webViewHomePage.getSettings().setLoadWithOverviewMode(true);
        webViewHomePage.getSettings().setAllowContentAccess(true);
        webViewHomePage.getSettings().setLoadsImagesAutomatically(true);
        webViewHomePage.getSettings().setLoadWithOverviewMode(true);
        webViewHomePage.getSettings().setDisplayZoomControls(false);
        webViewHomePage.getSettings().setSupportMultipleWindows(true);
        webViewHomePage.getSettings().setUseWideViewPort(true);
        webViewHomePage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webViewHomePage.setScrollbarFadingEnabled(false);
        webViewHomePage.getSettings().setBuiltInZoomControls(true);
        webViewHomePage.clearHistory();
        webViewHomePage.clearFormData();
        webViewHomePage.clearCache(true);
        webViewHomePage.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //Load url in webView
        webViewHomePage.loadUrl(url);
    }

    private void parseAccessTokenFromUrl(String fromUrl) {
        String param = fromUrl.split("#")[1];
        String token = param.split("&")[0];
        String scope = param.split("&scope")[1];
        if (scope.contains("heartrate") && scope.contains("nutrition") && scope.contains("profile") && scope.contains("sleep")
                && scope.contains("weight")) {
            String accessToken = token.split("=")[1];
            if (accessToken != null && !accessToken.equalsIgnoreCase("")) {
                isAccessTokenGet = true;
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_ACCESS_TOKEN_NAME, accessToken);
                callFitBitUserProfileApi();
                ApplicationPreferences.get().saveStringValue("FitBit-Connected", "true");
                webViewHomePage.clearHistory();
                webViewHomePage.clearFormData();
                webViewHomePage.clearCache(true);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookies(null);
                cookieManager.flush();
                String isHeartRateSwitchOn = ApplicationPreferences.get()
                        .getStringValue(Constants.FITBIT_HEART_RATE_SWITCH);
                String isRestingHeartRateSwitchOn = ApplicationPreferences.get()
                        .getStringValue(Constants.FITBIT_RESTING_HEART_SWITCH);
                if(isHeartRateSwitchOn == null || isHeartRateSwitchOn.matches("")) {
                    ApplicationPreferences.get().saveStringValue
                            (Constants.FITBIT_HEART_RATE_SWITCH, true + "");
                }
                if(isRestingHeartRateSwitchOn == null || isRestingHeartRateSwitchOn.matches("")) {
                    ApplicationPreferences.get().saveStringValue
                            (Constants.FITBIT_RESTING_HEART_SWITCH, false + "");
                }
                ApplicationPreferences.get().saveStringValue(Constants.FITBIT_CONNECTED_DIALOG_SHOWN, true + "");
                startActivity(new Intent(this, AppsDevicesActivity.class));
                FitBitLoginActivity.this.finish();
                AppUtils.logCleverTapEvent(FitBitLoginActivity.this,
                        Constants.FITBIT_CONNECTED, null);
            }
        } else {
            String allowAllWebUrl = "https://mobile.knowyourmeds.com/fitbit/fitbit_allowall.html";
            webViewHomePage.loadUrl(allowAllWebUrl);
        }
    }

    private void callFitBitUserProfileApi() {
        String mAccessToken = ApplicationPreferences.get().getStringValue(Constants.FITBIT_ACCESS_TOKEN_NAME);
        if (mAccessToken != null && !mAccessToken.equalsIgnoreCase("")) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Authorization", "Bearer " + mAccessToken);
            GenericRequestWithoutAuth<Object> profileRequest = new GenericRequestWithoutAuth<>(
                    Request.Method.GET, "https://api.fitbit.com/1/user/-/profile.json", Object.class, null,
                    response -> {
                        Gson gson = new Gson();
                        String userProfileString = gson.toJson(response);
                        ApplicationPreferences.get().saveStringValue(Constants.FITBIT_USER_PROFILE, userProfileString);
                        AppUtils.isFitBitApiRunning = false;
                        AppUtils.syncFitBitData(this, false, false);
                    }, error -> {
            }, headers);
            ApiService.get().addToRequestQueue(profileRequest);
        }
    }

}