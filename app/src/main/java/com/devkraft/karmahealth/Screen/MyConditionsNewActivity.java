package com.devkraft.karmahealth.Screen;





import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Adapter.UserSpinnerAdapter;
import com.devkraft.karmahealth.Adapter.ViewPagerAdapter;
import com.devkraft.karmahealth.Model.AddDrugResponse;
import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.GetKymUserDetailsRequest;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.LoginRequest;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyConditionsNewActivity extends AppCompatActivity {
    public static UserDto userDto;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    public static List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO;
    private Spinner mUserSpinner;
    private LinearLayout mParentLayout;
    private ProgressDialogSetup progress;
    private ImageView mImageViewUserAvtar;
    private ArrayList<UserDto> userDtoList;
    private MyReceiver mUpdateViewReceiver;

    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public int selectedTab = 0;
//    private ProgressDialogSetup mProgressDialogSetup;
    private boolean isEventNeedToCall = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conditions_new);
//        mProgressDialogSetup = ProgressDialogSetup.getProgressDialog(MyConditionsNewActivity.this);
//        mProgressDialogSetup.setCancelable(false);
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        getUserKYMDetails();
        initializeIds();
        handleTabLayoutClickEvents();
        AppUtils.storeScreenName("Conditions");
        AppUtils.setTitle(this, getString(R.string.conditions));
    }
    public void getUserKYMDetails(){
        GetKymUserDetailsRequest request = new GetKymUserDetailsRequest();
        request.phone = sharedPreferences.getString("Pphone","");
        /*  request.otp = sharedPreferences.getString("otp","");*/

        Log.i("checkmodeldatalogin", "get user api LoginNewResponse token = " + String.valueOf(new Gson().toJson(request)));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.getkymuserDetails(request,"Bearer " + sharedPreferences.getString("Ptoken","")).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i("checkmodeldatalogin", "get user api response code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("checkmodeldatalogin", "get user api response body = " +new Gson().toJson(response.body()));
                    ApplicationPreferences.get().saveStringValue(Constants.IS_NEW_USER, "true");
                    ApplicationPreferences.get().setUserDetails(response.body());
                    userDto = response.body().getUserDTO();
                    generateUserList(userDto);

                        AppUtils.logCleverTapEvent(MyConditionsNewActivity.this, Constants.USER_LOGGED_IN, null);


                } else {
                    // Toast.makeText(OTPActivity.this, "गलत OTP।", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldatalogin", "api response 1 code = " + response.code());


                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i("checkmodeldatalogin", "api error message response  = " + t.getMessage());

            }
        });

    }
    public void loginApi(){
        Log.i("Login_response", "0 = " );
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ashwini.mehendale+01@yopmail.com");
        loginRequest.setPassword("ashwiniM@7");
        loginRequest.setPlatform(Constants.ANDROID_PLATFORM);
        Log.i("Login_response", "1 = " + new Gson().toJson(loginRequest));

//        if (mProgressDialogSetup != null)
//            mProgressDialogSetup.show();
        AppUtils.logEvent(Constants.LOGIN_API_CALLED);
        AppUtils.logCleverTapEvent(this, Constants.LOGIN_API_CALLED, null);
        GenericRequest<LoginResponse> logincRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().SignIn(),
                        LoginResponse.class, loginRequest,
                        loginResponse -> updateUIAsLoginSuccess(loginResponse, false),
                        this::updateUIAsLoginFailed);

        ApiService.get().addToRequestQueue(logincRequest);
    }
    private void updateUIAsLoginFailed(VolleyError volleyError) {
        Log.i("Login_response", "3 = " + volleyError.getMessage());
//        AppUtils.hideProgressBar(mProgressDialogSetup);
        String res = AppUtils.getVolleyError(MyConditionsNewActivity.this, volleyError);
        AppUtils.showAlert(MyConditionsNewActivity.this, res);
    }

    private void updateUIAsLoginSuccess(LoginResponse loginResponse, boolean isGoogleLogIn) {
        Log.i("Login_response", "2 = " + new Gson().toJson(loginResponse));
        ApplicationPreferences.get().saveStringValue(Constants.IS_NEW_USER, "true");
//        AppUtils.hideProgressBar(mProgressDialogSetup);
        ApplicationPreferences.get().setUserDetails(loginResponse);
        userDto = loginResponse.getUserDTO();
        generateUserList(userDto);
        if (isGoogleLogIn) {
            AppUtils.logCleverTapEvent(this, Constants.GOOGLE_USER_LOGGED_IN, null);
        } else {
            AppUtils.logCleverTapEvent(this, Constants.USER_LOGGED_IN, null);
        }
       /* if (userDTO.getTenantId() != 1 && !userDTO.isEmailConfirm()) {
            callUserVerifiedEmailAPI();
        } else if (userDTO.getTrialExpiryDate() == null) {
            AppUtils.showLandingPageAccordingToUserProfile(this);
        } else {
            AppUtils.showLandingPageAccordingToUserProfile(HomeActivity.this);
            AppUtils.callTimeZoneAPI(HomeActivity.this);
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();

            IntentFilter updateFilter = new IntentFilter();
            updateFilter.addAction(Constants.UPDATE_CONDITIONS_AND_TRACKERS_LISTING);
            registerReceiver(mUpdateViewReceiver, updateFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
            unregisterReceiver(mUpdateViewReceiver);

    }

    private void initializeIds() {
            progress = ProgressDialogSetup.getProgressDialog(this);


        mUpdateViewReceiver = new MyReceiver();
        mUserSpinner = findViewById(R.id.userSpinner);
        mParentLayout = findViewById(R.id.parent_layout);
        mTabLayout = findViewById(R.id.tab_layout_conditions);
        mViewPager = findViewById(R.id.view_pager_conditions);
        mImageViewUserAvtar = findViewById(R.id.userAvatarIv);
        mImageViewUserAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    //    Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
    }

    private void setupTabLayoutAndViewPager() {
        try {
//            mTabLayout.addTab(mTabLayout.newTab().setText("Tab1"));
            mTabLayout.addTab(mTabLayout.newTab().setText("Tab2"));
            mTabLayout.addTab(mTabLayout.newTab().setText("Tab3"));
            //   setTabLayoutBackground(R.drawable.tab_left_select, R.drawable.tab_center_unselect, R.drawable.tab_right_unselect);

            mTabLayout.setupWithViewPager(mViewPager);
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mTabLayout.getTabCount(), this);

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


            try {
                if (selectedTab != 0) {
                    isEventNeedToCall = false;
                    mViewPager.setCurrentItem(selectedTab);
                    callCleverTapEvent(selectedTab);
                } else {
                    isEventNeedToCall = false;
                    callCleverTapEvent(Integer.parseInt(ApplicationPreferences.get().getStringValue(Constants.SELECTED_CONDITION_SCREEN_TAB)));
                    mViewPager.setCurrentItem(Integer.parseInt(ApplicationPreferences.get().getStringValue(Constants.SELECTED_CONDITION_SCREEN_TAB)));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCleverTapEvent(int selectedTab) {
        if (selectedTab == 0) {
            AppUtils.logCleverTapEvent(this, Constants.CLICKED_ON_CONDITION_TAB, null);
        } else if (selectedTab == 1) {
            AppUtils.logCleverTapEvent(this, Constants.CLICKED_ON_TRACKERTS_TAB, null);
        } else if (selectedTab == 2) {
            AppUtils.logCleverTapEvent(this, Constants.CLICKED_ON_SYMPTOMS_TAB, null);
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void setTabLayoutBackground(int conditionTab, int trackerTab, int symptops) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewGroup tabStrip = (ViewGroup) mTabLayout.getChildAt(0);
            View tabView1 = tabStrip.getChildAt(0);
            View tabView2 = tabStrip.getChildAt(1);
            View tabView3 = tabStrip.getChildAt(2);
            if (tabView1 != null) {
                int paddingStart = tabView1.getPaddingStart();
                int paddingTop = tabView1.getPaddingTop();
                int paddingEnd = tabView1.getPaddingEnd();
                int paddingBottom = tabView1.getPaddingBottom();
                ViewCompat.setBackground(tabView1, AppCompatResources.getDrawable(tabView1.getContext(), conditionTab));
                ViewCompat.setPaddingRelative(tabView1, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }

            if (tabView2 != null) {
                int paddingStart = tabView2.getPaddingStart();
                int paddingTop = tabView2.getPaddingTop();
                int paddingEnd = tabView2.getPaddingEnd();
                int paddingBottom = tabView2.getPaddingBottom();
                ViewCompat.setBackground(tabView2, AppCompatResources.getDrawable(tabView2.getContext(), trackerTab));
                ViewCompat.setPaddingRelative(tabView2, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }

            if (tabView3 != null) {
                int paddingStart = tabView3.getPaddingStart();
                int paddingTop = tabView3.getPaddingTop();
                int paddingEnd = tabView3.getPaddingEnd();
                int paddingBottom = tabView3.getPaddingBottom();
                ViewCompat.setBackground(tabView3, AppCompatResources.getDrawable(tabView3.getContext(), symptops));
                ViewCompat.setPaddingRelative(tabView3, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }
        }
    }

    private void handleTabLayoutClickEvents() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isEventNeedToCall) {
                    if (tab.getText().toString().equalsIgnoreCase(getString(R.string.condition))) {
                        AppUtils.logCleverTapEvent(MyConditionsNewActivity.this, Constants.CLICKED_ON_CONDITION_TAB, null);
                    } else if (tab.getText().toString().equalsIgnoreCase(getString(R.string.trackers))) {
                        AppUtils.logCleverTapEvent(MyConditionsNewActivity.this, Constants.CLICKED_ON_TRACKERTS_TAB, null);
                    } else if (tab.getText().toString().equalsIgnoreCase(getString(R.string.symptom_trackers))) {
                        AppUtils.logCleverTapEvent(MyConditionsNewActivity.this, Constants.CLICKED_ON_SYMPTOMS_TAB, null);
                    }
                }

                if (mTabLayout.getSelectedTabPosition() == 0) {
//                    selectedTab = 0;
                    ApplicationPreferences.get().saveStringValue(Constants.SELECTED_CONDITION_SCREEN_TAB, String.valueOf(0));
//                    AppUtils.logCleverTapEvent(this,Constants.CLICKED_ON_CONDITION_TAB,null);
                    setTabLayoutBackground(R.drawable.tab_left_select, R.drawable.tab_center_unselect, R.drawable.tab_right_unselect);
                } else if (mTabLayout.getSelectedTabPosition() == 1) {
//                    selectedTab = 1;
                    ApplicationPreferences.get().saveStringValue(Constants.SELECTED_CONDITION_SCREEN_TAB, String.valueOf(1));
//                    AppUtils.logCleverTapEvent(this,Constants.CLICKED_ON_TRACKERTS_TAB,null);
                    setTabLayoutBackground(R.drawable.tab_left_unselect, R.drawable.tab_center_select, R.drawable.tab_right_unselect);
                } else {
//                    selectedTab = 2;
                    ApplicationPreferences.get().saveStringValue(Constants.SELECTED_CONDITION_SCREEN_TAB, String.valueOf(2));
//                    AppUtils.logCleverTapEvent(this,Constants.CLICKED_ON_SYMPTOMS_TAB,null);
                    setTabLayoutBackground(R.drawable.tab_left_unselect, R.drawable.tab_center_unselect, R.drawable.tab_right_select);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing because of empty view
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing because of empty view
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        Log.e("selectedTab_logResume", "=" + selectedTab);
        try {
            checkPremiumView();
          //  callGetUserAddedSymptoms(MyConditionsNewActivity.userDto, AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
            callSymptoms(AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
            AppUtils.syncFitBitData(this, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPremiumView() {
        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto userDTO = loginResponse.getUserDTO();
            if (userDTO != null) {
                if (userDTO.isPremium()) {
                    // show premium view
//                    Bundle bundle = this.getArguments();
//                    if (bundle != null) {
//                        selectedTab = bundle.getInt(Constants.POSITION);
//                        Log.e("selectedTab_log", "=" + selectedTab);
//                        String userDtoStr = bundle.getString(Constants.USER_DTO);
//                        if (userDtoStr != null) {
//                            Gson gson = new Gson();
//                            UserDto userDtoLocal = gson.fromJson(userDtoStr, UserDto.class);
//
//                            generateUserList(userDtoLocal);
//                        }
//                    } else {
//                        generateUserList(null);
//                    }
                }
            }
        }
    }

    private void generateUserList(UserDto selectedUserDto) {
//        if (isAdded()) {
            mUserSpinner.setVisibility(View.GONE);
            userDtoList = new ArrayList<>();

            LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
            if (loginResponse != null) {
                UserDto uDto = loginResponse.getUserDTO();
                if (uDto != null) {
                    userDtoList.add(uDto);
                    userDto = uDto;
                }
            }

            UserDto uDto;
            List<DependentDto> dependentDtoList = ApplicationDB.get().getDependents();
            if (dependentDtoList != null && !dependentDtoList.isEmpty()) {
                for (int i = 0; i < dependentDtoList.size(); i++) {
                    DependentDto dependentDto = dependentDtoList.get(i);
                    uDto = new UserDto();

                    uDto.setName(dependentDto.getName());
                    uDto.setId(dependentDto.getId());
                    uDto.setAvatarName(dependentDto.getAvatarName());

                    userDtoList.add(uDto);
                }
            }

            UserSpinnerAdapter userSpinnerAdapter = new UserSpinnerAdapter(this, userDtoList,sharedPreferences.getString("Pname",""));
            mUserSpinner.setAdapter(userSpinnerAdapter);

            String selectedUserName = ApplicationPreferences.get().getStringValue(Constants.SELECTED_USER_NAME);
            if (selectedUserName != null && !selectedUserName.equalsIgnoreCase("")) {
                for (UserDto userDto : userDtoList) {
                    if (userDto.getName().equalsIgnoreCase(selectedUserName)) {
                        int index = userDtoList.indexOf(userDto);
                        if (index != -1) {
                            mUserSpinner.setSelection(index);
                        }
                    }
                }
            }

            if (selectedUserDto != null) {
                for (int i = 0; i < userDtoList.size(); i++) {
                    UserDto localUserDto = userDtoList.get(i);
                    if (localUserDto.getId().equals(selectedUserDto.getId())) {
                        userDto = selectedUserDto;
                        mUserSpinner.setSelection(i);
                        Log.i("symptomesurl","url - 00");
                        apiGetContactList();
                      //  callGetUserAddedSymptoms(userDto, AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
                        callSymptoms(AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
                     //   callGetDiseaseAPI(userDto);
                        break;
                    }
                }
            }

            mUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    AppUtils.logEvent(Constants.CNDTN_SCR_PROFILE_SELECTION_CLK);
                    userDto = userDtoList.get(position);
                    Integer resourceImage = AppUtils.getUserResourcedId(MyConditionsNewActivity.this, userDto.getAvatarName());
                    if (resourceImage != null) {
  //                      mImageViewUserAvtar.setImageResource(resourceImage);
                    } else {
  //                      mImageViewUserAvtar.setImageResource(R.drawable.avatars_generic);
                    }
                    Log.i("symptomesurl","url - 0");
                    apiGetContactList();
                 //   callGetDiseaseAPI(userDto);
                    ApplicationPreferences.get().saveStringValue(Constants.SELECTED_USER_NAME, userDto.getName());
                    Log.i("symptomesurl","url - 1");
                   // callGetUserAddedSymptoms(userDto, AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
                    callSymptoms(AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing because of X and Y
                }
            });
//        }
    }
    private void callSymptoms(String longFormatDate){
        Log.i("symptomesurl", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        Log.i("symptomesurl", "drug request user url = http://staging-karmahealth.knowyourmeds.com:8899/api/auth/symptoms/user/" + sharedPreferences.getString("kymPid", "134388")+"?date="+longFormatDate);
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        Log.i("symptomesurl", "api parameterDataList user id  = " + sharedPreferences.getString("kymPid", "134388"));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.symptomsDataList(sharedPreferences.getString("kymPid", "134388"),longFormatDate,token).enqueue(new Callback<List<GetUserAddedSymptomsResponseDTO>>() {
            @Override
            public void onResponse(Call<List<GetUserAddedSymptomsResponseDTO>> call, retrofit2.Response<List<GetUserAddedSymptomsResponseDTO>> response) {
                Log.i("symptomesurl", "api parameterDataList response 0 code = " + response.code());
                Log.i("symptomesurl", "api parameterDataList response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.hideProgressBar(progress);
                    getUserAddedSymptomsResponseDTO = response.body();
                    //  ApplicationDB.get().upsertSymptoms(userDto.getId(), symptomsResponseDTO);
                    saveTOString(getUserAddedSymptomsResponseDTO);
                }else if(response.code() == 401){
                    refreshToken();
                }  else {
                    AppUtils.hideProgressBar(progress);
                    AppUtils.openSnackBar(mParentLayout, "Please try after some time.");
                    Log.i("checkmodrug", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<GetUserAddedSymptomsResponseDTO>> call, Throwable t) {
                AppUtils.hideProgressBar(progress);
                AppUtils.openSnackBar(mParentLayout, t.getMessage());
                Log.i("checkmodrug", "api error message response  = " + t.getMessage());
            }
        });
    }

    private void callGetUserAddedSymptoms(UserDto userDto, String longFormatDate) {
        if (progress != null) {
            progress.show();
        }
        Log.i("symptomesurl","url - "+APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO> getSymptomRequest = new GenericRequest<>
                (Request.Method.GET, APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate),
                        GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO.class, null,
                        symptomsResponseDTO -> {
                            AppUtils.hideProgressBar(progress);
                            getUserAddedSymptomsResponseDTO = symptomsResponseDTO;
                          //  ApplicationDB.get().upsertSymptoms(userDto.getId(), symptomsResponseDTO);
                            saveTOString(getUserAddedSymptomsResponseDTO);
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(this, error, authExpiredCallback);
                            AppUtils.openSnackBar(mParentLayout, res);
                        });
        authExpiredCallback.setRequest(getSymptomRequest);
        ApiService.get().addToRequestQueue(getSymptomRequest);
    }

    private void saveTOString(List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO) {
        if (getUserAddedSymptomsResponseDTO != null) {
            Gson gson = new Gson();
            String conditionListing = gson.toJson(getUserAddedSymptomsResponseDTO);
            ApplicationPreferences.get().saveStringValue(Constants.SYMPTOMS_LIST, conditionListing);
        }
    }
    public void refreshToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(sharedPreferences.getString("refreshToken",""));
        Log.i("refreshToken", "refreshToken api request 278 = " + new Gson().toJson(request));

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, retrofit2.Response<RefreshTokenResponse> response) {
                Log.i("refreshToken", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("refreshToken", "prescription api response = " + new Gson().toJson(response.body()));

                    edit.putString("Ptoken", response.body().accessToken);
                    edit.apply();

                    AppUtils.openSnackBar(mParentLayout, "Try Again");
                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(MyConditionsNewActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });

    }
    private void apiGetContactList(){
        Log.i("checkmodrulistg", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        if (progress != null) {
            progress.show();
        }
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        Log.i("checkmodrulistg", "api parameterDataList user id  = " + sharedPreferences.getString("kymPid", "134388"));
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.parameterDataList(sharedPreferences.getString("kymPid", "134388"),token).enqueue(new Callback<DiseaseDto>() {
            @Override
            public void onResponse(Call<DiseaseDto> call, retrofit2.Response<DiseaseDto> response) {
                Log.i("checkmodrulistg", "api parameterDataList response 0 code = " + response.code());
                Log.i("checkmodrulistg", "api parameterDataList response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                   // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.hideProgressBar(progress);
                    ApplicationDB.get().upsertDisease(userDto.getId(), response.body());
                    storeAndProceed(response.body());
                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    AppUtils.hideProgressBar(progress);
                    AppUtils.openSnackBar(mParentLayout, "Please try after some time.");
                    Log.i("checkmodrulistg", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<DiseaseDto> call, Throwable t) {
                AppUtils.hideProgressBar(progress);
                AppUtils.openSnackBar(mParentLayout, t.getMessage());
                Log.i("checkmodrulistg", "api error message response  = " + t.getMessage());
            }
        });
    }
    private void callGetDiseaseAPI(UserDto userDto) {
        if (userDto != null) {

            if (progress != null) {
                progress.show();
            }
            Log.i("checkitemonclick","Condition list url =  "+APIUrls.get().getAllDisease(userDto.getId()));
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<DiseaseDto> getDiseaseRequest = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().getAllDisease(userDto.getId()),
                            DiseaseDto.class, null,
                            diseaseDto -> {
                                Log.i("checkitemonclick", "Condition list response =  " + new Gson().toJson(diseaseDto));
                                AppUtils.hideProgressBar(progress);
                                ApplicationDB.get().upsertDisease(userDto.getId(), diseaseDto);
                                storeAndProceed(diseaseDto);
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            authExpiredCallback.setRequest(getDiseaseRequest);
            ApiService.get().addToRequestQueue(getDiseaseRequest);
        }
    }

    private void storeAndProceed(DiseaseDto diseaseDto) {
        if (diseaseDto != null) {
            Gson gson = new Gson();
            String conditionListing = gson.toJson(diseaseDto);
            ApplicationPreferences.get().saveStringValue(Constants.CONDITION_LIST, conditionListing);
            setupTabLayoutAndViewPager();
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkPremiumView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
    }
}