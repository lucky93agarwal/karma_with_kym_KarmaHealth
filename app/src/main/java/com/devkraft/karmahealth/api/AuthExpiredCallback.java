package com.devkraft.karmahealth.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.R;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class AuthExpiredCallback {
    private static final String TAG = AuthExpiredCallback.class.getName();
    private GenericRequest request;
    private Context context;
    private ProgressDialogSetup progressDialog;

    public AuthExpiredCallback(Context context) {
        this.context = context;
    }

    public void setRequest(GenericRequest request) {
        this.request = request;
    }

    public GenericRequest getRequest() {
        return request;
    }

    public void onAuthExpired(boolean silent) {
        callAuthRefreshAPI(silent);
    }

    private void callAuthRefreshAPI(boolean silent) {
        if (null != context && !silent) {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = ProgressDialogSetup.getProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        Response.Listener<LoginResponse> response = new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                // adding try catch here for caching IllegalArgumentException
                // unable to reproduce and it was crashing in the production app
                // added by Harsh
                try{
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                if (response != null) {
                    if (context != null) {

                        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();

                        if(loginResponse != null){
                            LoginResponse lrResponse = new LoginResponse();
                            lrResponse.setRefreshToken(response.getRefreshToken());
                            lrResponse.setAccessToken(response.getAccessToken());
                            lrResponse.setUserDTO(loginResponse.getUserDTO());

                            ApplicationPreferences.get().setUserDetails(null);
                            ApplicationPreferences.get().setUserDetails(lrResponse);
                            if (null != request) {
                                if (request.getCallCount() <= GenericRequest.MAX_ALLOWED_AFTER_REFRESH_ATTEMPTS) {
                                    Log.w(TAG, "Refreshing user session");
                                    request.setCallCount(request.getCallCount() + 1);
                                    if(request.getBody() != null){
                                        String requestBody = new String(request.getBody(), StandardCharsets.UTF_8);
                                        request.setRequestBody(requestBody);
                                    }
                                    request.setHeader();
                                    ApiService.get().addToRequestQueue(request);
                                }
                            }
                        }else {
                            Log.e(TAG, "User details is not available");
                        }
                    } else {
                        Log.e(TAG, "Activity context is null");
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (null != error && null != error.networkResponse && error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(context, context.getString(R.string.session_expired_logging_out),
                            Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "User session has expired, force logging out");
                    Toast.makeText(context,"User will be logged out ",Toast.LENGTH_LONG).show();
                    AppUtils.performLogout(context,false);
                    return;
                }

                if (context != null) {
                    Toast.makeText(context, "Error occurred while calling refresh API.", Toast.LENGTH_SHORT).show();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(context,
                                context.getString(R.string.netwotk_error),
                                Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(context,
                                context.getString(R.string.can_not_connect),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context,
                                context.getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        RefreshTokenRequest requestObj = new RefreshTokenRequest();
        if( null != ApplicationPreferences.get().getUserDetails()) {
            requestObj.setRefreshToken(ApplicationPreferences.get().getUserDetails().getRefreshToken());
            GenericRequestWithoutAuth<LoginResponse> request =
                    new GenericRequestWithoutAuth<>(Request.Method.POST,
                            APIUrls.get().getAuthRefreshUrl(),
                            LoginResponse.class, requestObj, response,
                            errorListener, GenericRequest.getDefHeaders());
            ApiService.get().addToRequestQueue(request);
        } else {
            Toast.makeText(context, context.getString(R.string.session_expired_logging_out),
                    Toast.LENGTH_LONG).show();
            Log.w(TAG, "User session has expired, force logging out");
            Toast.makeText(context,"User will be logged out 2",Toast.LENGTH_LONG).show();
        }
    }

    public void hideProgressBar() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
