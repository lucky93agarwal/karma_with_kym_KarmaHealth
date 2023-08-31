package com.devkraft.karmahealth.fragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.SymptomsFilledTempListAdapter;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.SymptomCleverTap;
import com.devkraft.karmahealth.Model.UserSymptomTrackingRequest;
import com.devkraft.karmahealth.Model.UserSymptomTrackingResponseDTO;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.LoginActivity;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devkraft.karmahealth.Adapter.ConditionsListAdapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddParameterRequest;
import com.devkraft.karmahealth.Model.AddSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.Disease;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterResponseDTO;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.LoginRequest;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.SymptomRequest;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AddConditionsActivity;
import com.devkraft.karmahealth.Screen.AddSymptomsActivity;
import com.devkraft.karmahealth.Screen.ManageVitalActivity;
import com.devkraft.karmahealth.Screen.MyConditionsNewActivity;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;

public class SymptomTrackersFragment extends Fragment implements RvClickListener {

    private String fileName;
    private DownloadManager downloadManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private TextView mTextViewDate;
    private RelativeLayout mParentLayout;
    private LinearLayout mLayoutExportReport;
    private RecyclerView mRecyclerViewSymptoms;
    private FrameLayout mFloatingButtonLayout;
    private LinearLayout mLayoutSaveCancelButton;
    private String TAG = "SymptomTrackerFragment";
    public static UserSymptomTrackingRequest.UserTracking userTracking;

    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton menuItemAddTest;
    private com.github.clans.fab.FloatingActionButton menuItemAddVitals;
    private com.github.clans.fab.FloatingActionButton menuItemAddVaccines;
    private com.github.clans.fab.FloatingActionButton menuItemAddSymptoms;
    private com.github.clans.fab.FloatingActionButton menuItemAddCondition;

    private LinearLayout mButtonSave;
    private LinearLayout mButtonCancel;
    private Button mButtonAddSymptom;
    private ScrollView mEmptyScrollView;
    private View mViewSymptomSaveCancel;
    private ProgressDialogSetup progress;
    private LinearLayout mLayoutSymptomsList;
    private UserSymptomTrackingRequest userTrackingUpdateRequest;
    private List<SymptomCleverTap> updatedValues = new ArrayList<>();
    public static List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO;
    private ArrayList<UserSymptomTrackingRequest.UserTracking> symptomArrayList = new ArrayList<>();

    private SymptomCleverTap symptomCleverTapRequest;
    private SymptomsFilledTempListAdapter symptomAdapter;

    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int PERMISSION_REQUEST_CODE = 200;
    private boolean isFirstTimePermissionAsk = false;

    public static SymptomTrackersFragment newInstance() {
        SymptomTrackersFragment fragment = new SymptomTrackersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symptom_trackers, container, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeIds(view);
        handleClickEvents();
        showSymptomList();
        initializeDownloadManager();
    }

    private void showSymptomList() {
        Log.i("showsymp","step 1");
        try {
            Log.i("showsymp","step url = "+Constants.SYMPTOMS_LIST);
            String symptomListString = ApplicationPreferences.get().getStringValue(Constants.SYMPTOMS_LIST);
            if (symptomListString != null && !symptomListString.matches("")) {
                Gson symptomList = new Gson();
                Type typeWebinar = new TypeToken<GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO>() {
                }.getType();
                getUserAddedSymptomsResponseDTO = symptomList.fromJson(symptomListString, typeWebinar);
                if (getUserAddedSymptomsResponseDTO != null) {
                    if (!getUserAddedSymptomsResponseDTO.isEmpty()) {
                        mEmptyScrollView.setVisibility(View.GONE);
                        Log.e("response_symptoms_log", " = " + symptomListString);
                        mLayoutSymptomsList.setVisibility(View.VISIBLE);
                        setSymptomsAdapter(getUserAddedSymptomsResponseDTO);
                    } else {
                        mEmptyScrollView.setVisibility(View.VISIBLE);
                        mLayoutSymptomsList.setVisibility(View.GONE);
                    }
                }
            } else {
                mEmptyScrollView.setVisibility(View.VISIBLE);
                mLayoutSymptomsList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("showsymp","step error message = "+e.getMessage());
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
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });

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
                 //   ApplicationDB.get().upsertSymptoms(userDto.getId(),response.body());
                    mFloatingButtonLayout.setVisibility(View.VISIBLE);
                    Log.e("getSymptomsList_log", " = " + new Gson().toJson(getUserAddedSymptomsResponseDTO));
                    setSymptomsAdapter(getUserAddedSymptomsResponseDTO);
                    saveToString(getUserAddedSymptomsResponseDTO);
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
        Log.i("showsymp","step url 2 = "+APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate));
        Log.e("API_URL_GET_SYMPTOM", " = " + APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getActivity());
        GenericRequest<GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO> getSymptomRequest = new GenericRequest<>
                (Request.Method.GET, APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate),
                        GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO.class, null,
                        symptomsResponseDTO -> {
                            AppUtils.hideProgressBar(progress);
                            getUserAddedSymptomsResponseDTO = symptomsResponseDTO;
                            ApplicationDB.get().upsertSymptoms(userDto.getId(),symptomsResponseDTO);
                            mFloatingButtonLayout.setVisibility(View.VISIBLE);
                            Log.e("getSymptomsList_log", " = " + new Gson().toJson(getUserAddedSymptomsResponseDTO));
                            setSymptomsAdapter(getUserAddedSymptomsResponseDTO);
                            saveToString(getUserAddedSymptomsResponseDTO);
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(getActivity(), error, authExpiredCallback);
                            AppUtils.openSnackBar(mParentLayout, res);
                        });
        authExpiredCallback.setRequest(getSymptomRequest);
        ApiService.get().addToRequestQueue(getSymptomRequest);
    }

    private void saveToString(List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO) {
        if (getUserAddedSymptomsResponseDTO != null) {
            Gson gson = new Gson();
            String conditionListing = gson.toJson(getUserAddedSymptomsResponseDTO);
            ApplicationPreferences.get().saveStringValue(Constants.SYMPTOMS_LIST, conditionListing);
        }
    }

    private void setSymptomsAdapter(List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewSymptoms.setLayoutManager(linearLayoutManager);
//        SymptomsFilledListAdapter adapter = new SymptomsFilledListAdapter(getActivity(), getUserAddedSymptomsResponseDTO, mLayoutSaveCancelButton, mFloatingButtonLayout, mButtonCancel,mViewSymptomSaveCancel);
        symptomAdapter = new SymptomsFilledTempListAdapter(getActivity(), getUserAddedSymptomsResponseDTO, mLayoutSaveCancelButton, mFloatingButtonLayout);
        mRecyclerViewSymptoms.setAdapter(symptomAdapter);
        symptomAdapter.setRvClickListener(this);
    }

    private void openDateDialogPicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog,
                (view, year, monthOfYear, dayOfMonth) -> {

                    int month = monthOfYear + 1;
                    String monthStr = month + "";
                    if (monthStr.trim().length() == 1) {
                        monthStr = "0" + monthStr;
                    }

                    String dayStr = dayOfMonth + "";
                    if (dayStr.trim().length() == 1) {
                        dayStr = "0" + dayStr;
                    }

                    mTextViewDate.setText(monthStr + "/" + dayStr + "/" + year);

                 //   callGetUserAddedSymptoms(MyConditionsNewActivity.userDto, AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));
                    callSymptoms(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        AppUtils.setDialogBoxButton(getActivity(), datePickerDialog);
        if (mTextViewDate.getText() != null && !mTextViewDate.getText().toString().equalsIgnoreCase(getString(R.string.select_date_track))) {
            AppUtils.setSelectedDate(datePickerDialog, mTextViewDate.getText().toString().trim());
        }
        datePickerDialog.show();
    }

    private void initializeIds(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        floatingActionMenu = view.findViewById(R.id.menu);
        menuItemAddTest = view.findViewById(R.id.menu_item_add_test);
        menuItemAddVitals = view.findViewById(R.id.menu_item_add_vital);
        menuItemAddSymptoms = view.findViewById(R.id.menu_item_add_symptoms);
        menuItemAddVaccines = view.findViewById(R.id.menu_item_add_vaccines);
        menuItemAddCondition = view.findViewById(R.id.menu_item_add_condition);

        mTextViewDate = view.findViewById(R.id.textview_date);
        mFloatingButtonLayout = view.findViewById(R.id.floating_menu);
        mLayoutExportReport = view.findViewById(R.id.layout_export_report);
        mLayoutSaveCancelButton = view.findViewById(R.id.layout_save_cancel);
        mRecyclerViewSymptoms = view.findViewById(R.id.recyclerview_symtomps);

        mButtonSave = view.findViewById(R.id.button_save_symptom);
        mButtonCancel = view.findViewById(R.id.button_cancel_symptom);

        mEmptyScrollView = view.findViewById(R.id.emptyView);
        mLayoutSymptomsList = view.findViewById(R.id.layout_list_view);
        mButtonAddSymptom = view.findViewById(R.id.button_add_symptom);
        mViewSymptomSaveCancel = view.findViewById(R.id.view_symptom_save_cancel);

        mParentLayout = view.findViewById(R.id.parent_symptom_track);
        progress = ProgressDialogSetup.getProgressDialog(getActivity());

        mTextViewDate.setText(AppUtils.getTodayDate());
        mFloatingButtonLayout.setVisibility(View.VISIBLE);

        isFirstTimePermissionAsk = Boolean.parseBoolean(ApplicationPreferences.get().getStringValue(Constants.IS_PERMISSION_GRANTED));
    }

    private void initializeDownloadManager() {
        downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        fileName = "Report.pdf";
    }

    private void handleClickEvents() {
        mTextViewDate.setOnClickListener(v -> openDateDialogPicker());
        mLayoutExportReport.setOnClickListener(v -> startDownloadingFile());
        mButtonAddSymptom.setOnClickListener(v -> openAddSymptomsActivity());

        mButtonCancel.setOnClickListener(v -> clearData());
        mButtonSave.setOnClickListener(v -> callUpdateSymptomAPI(MyConditionsNewActivity.userDto));

        menuItemAddCondition.setOnClickListener(view -> {
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.MY_CONDITION_SCREEN_ADD_CONDITION_FLOAT_BUTTON_CLICKED, null);
            openAddConditionActivity();
        });

        menuItemAddVitals.setOnClickListener(view -> {
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.CONDITIONS_SCREEN_ADD_HEALTH_METRIC_FLOATING_BUTTON_CLICKED, null);
            openManageActivity(getString(R.string.add_vital));
        });

        menuItemAddTest.setOnClickListener(view -> {
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.CONDITIONS_SCREEN_ADD_CHECKUP_FLOATING_BUTTON_CLICKED, null);
            openManageActivity(getString(R.string.add_checkup));
        });

        menuItemAddVaccines.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_VACCINE_BTN_CLK);
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.CONDITION_SCREEN_ADD_VACCINE_FLOATING_BUTTON_CLICKED, null);
            openManageActivity(getString(R.string.add_vaccines));
        });

        menuItemAddSymptoms.setOnClickListener(v -> openAddSymptomsActivity());
    }

    private void openManageActivity(String condition) {
        if (getContext() != null) {
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_CNDTN_BTN_CLK);
            Intent intent = new Intent(getContext(), ManageVitalActivity.class);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(MyConditionsNewActivity.userDto));
            intent.putExtra(Constants.Custom_condition, condition);
            startActivity(intent);
            closeActionButton();
        }
    }

    private void closeActionButton() {
        if (floatingActionMenu != null) {
            AppUtils.logEvent(Constants.LOGGED_IN_DASHBOARD_SCREEN_ADD_DRUGS_TO_YOUR_PROFILE_PAGE_X_BUTTON_CLICKED);
            floatingActionMenu.close(true);
        }
    }

    private void openAddConditionActivity() {
        if (getContext() != null) {
            ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "0");
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.ADD_NOW_BUTTON_CLICKED_ON_CONDITIONS_SCREEN, null);

            Intent intent = new Intent(getContext(), AddConditionsActivity.class);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(MyConditionsNewActivity.userDto));
            startActivity(intent);
        }
    }

    private void clearData() {

        for (int i = 0; i < getUserAddedSymptomsResponseDTO.size(); i++) {
            getUserAddedSymptomsResponseDTO.get(i).setSelectedMeasurementValues("");
        }

        mLayoutSaveCancelButton.setVisibility(View.GONE);
        mFloatingButtonLayout.setVisibility(View.VISIBLE);
        symptomArrayList.clear();
        updatedValues.clear();
        showSymptomList();
    }

    private void callUpdateSymptomAPI(UserDto userDto) {
        if (progress != null) {
            progress.show();
        }

        Log.e("updateValues_log"," = "+new Gson().toJson(updatedValues));

        if(!updatedValues.isEmpty()) {
            for (int i = 0; i < updatedValues.size(); i++) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Symptom Name", updatedValues.get(i).getName());
                hashMap.put("Symptom Type", updatedValues.get(i).getSeverityType());
                hashMap.put("SeverityLevel", updatedValues.get(i).getSeverityLevel());

                Log.e("hasmap_log"," = "+ new Gson().toJson(hashMap));
                AppUtils.logCleverTapEvent(getActivity(),Constants.SYMPTOMS_VALUE_UPDATED_ON_CONDITION_SCREEN,hashMap);
            }
        }

        Log.e("request_obj", " = " + new Gson().toJson(userTrackingUpdateRequest));
        Log.e("url", " = " + APIUrls.get().getUpdateSymptomTracking(userDto.getId()));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getActivity());
        GenericRequest<UserSymptomTrackingResponseDTO.UserSymptomTrackingListResponseDTO> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getUpdateSymptomTracking(userDto.getId()),
                        UserSymptomTrackingResponseDTO.UserSymptomTrackingListResponseDTO.class, userTrackingUpdateRequest,
                        response -> {
                            progress.dismiss();
                            Log.e("response_log", " = " + new Gson().toJson(response));
                            mLayoutSaveCancelButton.setVisibility(View.GONE);
                            mFloatingButtonLayout.setVisibility(View.VISIBLE);
                         //   callGetUserAddedSymptoms(userDto,AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));
                            callSymptoms(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));
                        },
                        error -> {
                            progress.dismiss();
                            String reason = AppUtils.getVolleyError
                                    (getActivity(), error);
                            AppUtils.openSnackBar(mParentLayout, reason);
                        });
        authExpiredCallback.setRequest(addQuestionRequest);
        ApiService.get().addToRequestQueue(addQuestionRequest);
    }

    private void openAddSymptomsActivity() {
        AppUtils.logCleverTapEvent(getActivity(),Constants.CLICKED_ON_ADD_NOW_BUTTON_ON_SYMPTOM_TAB_ON_CONDITION_SCREEN,null);
        Intent intent = new Intent(getActivity(), AddSymptomsActivity.class);
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(MyConditionsNewActivity.userDto));
        startActivity(intent);
    }

    @Override
    public void rv_click(int position, int value, String key) {
        Log.e("key_log_symptom"," = "+key);
        if (key.equalsIgnoreCase(Constants.UPDATE_SYMPTOM_ADD)) {
            mLayoutSaveCancelButton.setVisibility(View.VISIBLE);
            mFloatingButtonLayout.setVisibility(View.GONE);
            addSymptomInUpdateList(position, value);
            symptomAdapter.notifyDataSetChanged();
        } else if (key.equalsIgnoreCase(Constants.UPDATE_SYMPTOM_REMOVE)) {
            removeItemFromList(position);
            symptomAdapter.notifyDataSetChanged();
        }
    }

    private void removeItemFromList(int position) {
        try {
            if (!symptomArrayList.isEmpty()) {
                for (int i = 0; i < symptomArrayList.size(); i++) {
                    if (symptomArrayList.get(i).getUserSymptomId().equals(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId())) {
                        symptomArrayList.remove(i);
                        updatedValues.remove(position);
                        break;
                    }
                }

                if(!updatedValues.isEmpty()) {
                    for(int i=0; i < updatedValues.size(); i++) {
                        if(updatedValues.get(i).getName().equalsIgnoreCase(getUserAddedSymptomsResponseDTO.get(position).getName())) {
                            updatedValues.remove(i);
                        }
                    }
                }
                userTrackingUpdateRequest = new UserSymptomTrackingRequest();
                userTrackingUpdateRequest.setUserSymptomTracking(symptomArrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSymptomInUpdateList(int position, int value) {

        Log.e("value_log", " = " + value);
        if(!getUserAddedSymptomsResponseDTO.isEmpty()) {

            if(!symptomArrayList.isEmpty())  {
                boolean isExist = false;
                for (UserSymptomTrackingRequest.UserTracking userTracking1 : symptomArrayList) {
                    if(userTracking1.getUserSymptomId().equals(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId())) {
                        isExist = true;
                        userTracking1.setRecordedDate(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString() + " " +AppUtils.getCurrentTime()));
                        switch (value) {
                            case 1 :
                                symptomCleverTapRequest = new SymptomCleverTap();
                                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(0));
                                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                                updatedValues.add(symptomCleverTapRequest);
                                userTracking1.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(0));
                                break;

                            case 2 :
                                symptomCleverTapRequest = new SymptomCleverTap();
                                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(1));
                                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                                updatedValues.add(symptomCleverTapRequest);
                                userTracking1.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(1));
                                break;

                            case 3 :
                                symptomCleverTapRequest = new SymptomCleverTap();
                                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(2));
                                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                                updatedValues.add(symptomCleverTapRequest);
                                userTracking1.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(2));
                                break;

                            case 4 :
                                symptomCleverTapRequest = new SymptomCleverTap();
                                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(3));
                                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                                updatedValues.add(symptomCleverTapRequest);
                                userTracking1.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(3));
                                break;

                            case 5 :
                                symptomCleverTapRequest = new SymptomCleverTap();
                                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(4));
                                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                                updatedValues.add(symptomCleverTapRequest);
                                userTracking1.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(4));
                                break;

                            default:
                                //Do Nothing
                                break;
                        }
                    }
                }

                if(!isExist) {
                    addSymptomRequestObjectInList(value,position);
                }
            } else {
                addSymptomRequestObjectInList(value,position);
            }

            userTrackingUpdateRequest = new UserSymptomTrackingRequest();
            userTrackingUpdateRequest.setUserSymptomTracking(symptomArrayList);
        }
    }

    private void addSymptomRequestObjectInList(int value, int position) {
        if (getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue() != null && !getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().isEmpty()) {
            if (value == 1) {
                userTracking = new UserSymptomTrackingRequest.UserTracking();
                userTracking.setUserSymptomId(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId());
                userTracking.setRecordedDate(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString() + " " + AppUtils.getCurrentTime()));
                userTracking.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(0));

                symptomCleverTapRequest = new SymptomCleverTap();
                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(0));
                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                updatedValues.add(symptomCleverTapRequest);
            } else if (value == 2) {
                userTracking = new UserSymptomTrackingRequest.UserTracking();
                userTracking.setUserSymptomId(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId());
                userTracking.setRecordedDate(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString() + " " +AppUtils.getCurrentTime()));
                userTracking.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(1));

                symptomCleverTapRequest = new SymptomCleverTap();
                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(1));
                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                updatedValues.add(symptomCleverTapRequest);
            } else if (value == 3) {
                userTracking = new UserSymptomTrackingRequest.UserTracking();
                userTracking.setUserSymptomId(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId());
                userTracking.setRecordedDate(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString() + " " +AppUtils.getCurrentTime()));
                userTracking.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(2));

                symptomCleverTapRequest = new SymptomCleverTap();
                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(2));
                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                updatedValues.add(symptomCleverTapRequest);
            } else if (value == 4) {
                userTracking = new UserSymptomTrackingRequest.UserTracking();
                userTracking.setUserSymptomId(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId());
                userTracking.setRecordedDate(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString() + " " +AppUtils.getCurrentTime()));
                userTracking.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(3));

                symptomCleverTapRequest = new SymptomCleverTap();
                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(3));
                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                updatedValues.add(symptomCleverTapRequest);
            } else if (value == 5) {
                userTracking = new UserSymptomTrackingRequest.UserTracking();
                userTracking.setUserSymptomId(getUserAddedSymptomsResponseDTO.get(position).getUserSymptomId());
                userTracking.setRecordedDate(AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString() + " " +AppUtils.getCurrentTime()));
                userTracking.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(4));

                symptomCleverTapRequest = new SymptomCleverTap();
                symptomCleverTapRequest.setName(getUserAddedSymptomsResponseDTO.get(position).getName());
                symptomCleverTapRequest.setSeverityLevel(getUserAddedSymptomsResponseDTO.get(position).getMeasurementValue().get(4));
                symptomCleverTapRequest.setSeverityType(getUserAddedSymptomsResponseDTO.get(position).getMeasurementType());

                updatedValues.add(symptomCleverTapRequest);
            }
            symptomArrayList.add(userTracking);
        }
    }

    /*private void callExportReportAPI() {
        if (MyConditionsNewFragment.userDto != null) {

            if (progress != null) {
                progress.show();
            }

            String url;

            url = APIUrls.get().getReportSymptomsList(MyConditionsNewFragment.userDto.getId(),
                    AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));

            Log.e("url", url);

            callDownloadReport(url);
        } else {
            AppUtils.openSnackBar(mParentLayout, getString(R.string.end_date_grater_start_date));
        }
    }

     private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void callDownloadReport(String url) {
        Log.v(TAG, "download() Method invoked");
        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            Log.e(TAG, "download() Method DON'T HAVE PERMISSIONS ");
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 112);
            Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "download() Method HAVE PERMISSIONS ");
            downloadFileUsingDownloadManager(url);
        }
        Log.e(TAG, "download() Method completed ");
    }

    */

    private void downloadFileUsingDownloadManager(String url) {
        AppUtils.hideProgressBar(progress);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Report")
                .setDescription("File is downloading...")
//                .setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .allowScanningByMediaScanner();

        request.addRequestHeader("Authorization", "Bearer " + ApplicationPreferences.get().getUserDetails().getAccessToken());
        //Enqueue the download.The download will start automatically once the download manager is ready
        // to execute it and connectivity is available.
        long downLoadId = downloadManager.enqueue(request);
        Toast.makeText(getActivity(), "Downloading Started..", Toast.LENGTH_SHORT).show();
        Log.e("report_location_log", " = " + Environment.DIRECTORY_DOWNLOADS);
    }


    //New code for permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                if (MyConditionsNewActivity.userDto != null) {

                    String url = APIUrls.get().getReportSymptomsList(MyConditionsNewActivity.userDto.getId(),
                            AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));

                    Log.e("url", url);
                    downloadFileUsingDownloadManager(url);

                    ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED,"false");
                }
            } else {
                // Permission request was denied.
                isFirstTimePermissionAsk = true;
                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED,"true");
                AppUtils.openSnackBar(mParentLayout, "Permission denied");
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void startDownloadingFile() {
        if (ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
//            Snackbar.make(mParentLayout, "Permission available", Snackbar.LENGTH_SHORT).show();
            if (MyConditionsNewActivity.userDto != null) {
                String url;

                url = APIUrls.get().getReportSymptomsList(MyConditionsNewActivity.userDto.getId(),
                        AppUtils.getBackendFormattedDateForSymptoms(mTextViewDate.getText().toString()));

                Log.e("url", url);
                downloadFileUsingDownloadManager(url);

            }
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
    }

    /**
     * Requests the {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mParentLayout, "Permissions required",
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);

                isFirstTimePermissionAsk = true;
                ApplicationPreferences.get().saveStringValue(Constants.IS_PERMISSION_GRANTED,"true");
            }).show();

        } else {

            Log.e("profile_log", " = " + isFirstTimePermissionAsk);
            if (isFirstTimePermissionAsk) {
                showEnablePermissionDialog();
            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void showEnablePermissionDialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        final View deletePopupView = getLayoutInflater().inflate(R.layout.layout_food_diary_popup, null);

        Button mButtonYes = deletePopupView.findViewById(R.id.button_ok);
        TextView mTextViewTitle = deletePopupView.findViewById(R.id.textview_title);

        dialogBuilder.setView(deletePopupView);
        androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.setCancelable(false);

        mTextViewTitle.setText(getString(R.string.enable_permission_msg));

        mButtonYes.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

}