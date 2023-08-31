package com.devkraft.karmahealth.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Adapter.CheckupsListAdapter;
import com.devkraft.karmahealth.Adapter.HealthMatricsListAdapter;
import com.devkraft.karmahealth.Adapter.VaccinesListAdapter;
import com.devkraft.karmahealth.Model.AddParameterAPIResponse;
import com.devkraft.karmahealth.Model.GetParametersListResponse;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AppsDevicesActivity;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

public class TrackersNewFragment extends Fragment {
    int countHelath = 0;
    int countCheckup = 0;
    int countVaccines = 0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;


    private boolean isExpandedCheckups = false;
    private boolean isExpandedVaccines = false;
    private boolean isExpandedHealthMatics = false;

    private List<ParameterDto> healthList = new ArrayList<>();
    private List<ParameterDto> checkupList= new ArrayList<>();
    private List<ParameterDto> vaccinesList= new ArrayList<>();
    private DiseaseDto conditionListResponse;
    private FloatingActionMenu floatingActionMenu;

    private FloatingActionButton menuItemAddTest;
    private FloatingActionButton menuItemAddVitals;
    private FloatingActionButton menuItemAddVaccines;
    private FloatingActionButton menuItemAddSymptoms;
    private FloatingActionButton menuItemAddCondition;

    private ImageView mImageViewHealthUp;
    private ImageView mImageViewCheckupUp;
    private ImageView mImageViewVaccineUp;
    private ImageView mImageViewHealthDown;
    private ImageView mImageViewCheckupDown;
    private ImageView mImageViewVaccineDown;

    private TextView mTextViewCheckups;
    private TextView mTextViewVaccines;
    private TextView mTextViewHealthMatrics;

    private RecyclerView mRecyclerViewCheckups;
    private RecyclerView mRecyclerViewVaccines;
    private RecyclerView mRecyclerViewHealthMatrics;

    private RelativeLayout mLayoutCheckups;
    private RelativeLayout mLayoutVaccines;
    private RelativeLayout mLayoutHealthMatrics;
    private RelativeLayout mLayoutAppsAndDevices;

    private TextView datanofoundtvtv;
    public TrackersNewFragment() {
        // Required empty public constructor
    }

    public static TrackersNewFragment newInstance() {
        TrackersNewFragment fragment = new TrackersNewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trackers_new, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeIds(view);
        showTrackersList();
        getListData();
        setupClickEventsOnLayout();
        hideIntercomChatBot();


    }

    private void hideIntercomChatBot() {
//        Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
    }

    //endregion
    private void getListData(){
       /* UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.parameterDataList("12"*//*sharedPreferences.getString("kymPid", "134388")*//*).enqueue(new Callback<ArrayList<GetParametersListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetParametersListResponse>> call, retrofit2.Response<ArrayList<GetParametersListResponse>> response) {
             //   Log.i("checkitemonclickCustom", "api login response 0 code = " + response.code());
                Log.i("checkitemonclickCustom", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                  //  Log.i("checkitemonclickCustom", "api login LoginNewResponse response = " + new Gson().toJson(response.body()));

                 //   AppUtils.openSnackBar(parentLayout, response.body().message);


                } else {
                  //  AppUtils.openSnackBar(parentLayout, "Drug not found");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkitemonclick", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetParametersListResponse>> call, Throwable t) {
             //   AppUtils.openSnackBar(parentLayout, t.getMessage());
                Log.i("checkitemonclick", "api error message response  = " + t.getMessage());
            }
        });*/
    }

    //region user Defined methodsD
    private void showTrackersList() {
        Log.i("showTrackersList","step url = "+Constants.CONDITION_LIST);
        String conditionListString = ApplicationPreferences.get().getStringValue(Constants.CONDITION_LIST);
        Log.e("showTrackersList"," = "+conditionListString);
        Log.i("showTrackersList","step conditionListString = "+conditionListString);
        if (conditionListString != null && !conditionListString.matches("")) {
            Gson conditionList = new Gson();
            Type typeWebinar = new TypeToken<DiseaseDto>() {
            }.getType();
            conditionListResponse = conditionList.fromJson(conditionListString, typeWebinar);
            Log.i("showTrackersList","step conditionListResponse = "+conditionListResponse);
            if (conditionListResponse != null) {
                if (!conditionListResponse.getTests().isEmpty() ||
                        !conditionListResponse.getVitals().isEmpty()) {
                    healthList = conditionListResponse.getVitals();
                    checkupList = conditionListResponse.getTests();
                    vaccinesList = conditionListResponse.getVaccines();
                    if(healthList.size() >0){
                        datanofoundtvtv.setVisibility(View.GONE);
                    }else {
                        datanofoundtvtv.setVisibility(View.VISIBLE);
                    }
                    Log.i("showTrackersList","step healthList size = "+String.valueOf(healthList.size()));
                    Log.i("checkitemonclickCustom","step checkupList size = "+String.valueOf(checkupList.size()));
                    Log.i("checkitemonclickCustom","step vaccinesList size = "+String.valueOf(vaccinesList.size()));
                    setAvailableDataCount();

                } else {
                    datanofoundtvtv.setVisibility(View.VISIBLE);
                    updateNoDataCount();
                }
            }
        } else {
            datanofoundtvtv.setVisibility(View.VISIBLE);
            updateNoDataCount();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateNoDataCount() {
        mImageViewHealthUp.setVisibility(View.GONE);
        mImageViewCheckupUp.setVisibility(View.GONE);
        mImageViewVaccineUp.setVisibility(View.GONE);
        mImageViewHealthDown.setVisibility(View.GONE);
        mImageViewCheckupDown.setVisibility(View.GONE);
        mImageViewVaccineDown.setVisibility(View.GONE);

        mTextViewCheckups.setText("No " + getString(R.string.chekup_description));
        mTextViewVaccines.setText("No " + getString(R.string.vaccines_description));
        mTextViewHealthMatrics.setText("No " + getString(R.string.health_matrics_description));
    }

    @SuppressLint("SetTextI18n")
    private void setAvailableDataCount() {
        mImageViewHealthDown.setVisibility(View.VISIBLE);
        mImageViewCheckupDown.setVisibility(View.VISIBLE);
        mImageViewVaccineDown.setVisibility(View.VISIBLE);

        mImageViewHealthUp.setVisibility(View.GONE);
        mImageViewCheckupUp.setVisibility(View.GONE);
        mImageViewVaccineUp.setVisibility(View.GONE);

        setCheckUpCount();
        setVaccinesData();
        setHealthMatricsData();
    }

    private void setHealthMatricsData() {
        if (!conditionListResponse.getVitals().isEmpty()) {
            mTextViewHealthMatrics.setText("" + healthList.size() + " " + getString(R.string.health_matrics_description));
        } else  {
            mTextViewHealthMatrics.setText("No " + getString(R.string.health_matrics_description));
        }
    }

    private void setVaccinesData() {
        if (!conditionListResponse.getVaccines().isEmpty()) {
            mTextViewVaccines.setText("" + vaccinesList.size() + " " + getString(R.string.vaccines_description));
        } else  {
            mTextViewVaccines.setText("No " + getString(R.string.vaccines_description));
        }
    }

    private void setCheckUpCount() {
        if (!conditionListResponse.getTests().isEmpty()) {
            mTextViewCheckups.setText("" + checkupList.size() + " " + getString(R.string.chekup_description));
        } else  {
            mTextViewHealthMatrics.setText("No " + getString(R.string.chekup_description));
        }
    }

    private void setupClickEventsOnLayout() {
        mLayoutVaccines.setOnClickListener(v -> checkforVaccinesIsExpanded());
        mLayoutCheckups.setOnClickListener(v -> checkforCheckupsIsExpanded());
        mLayoutAppsAndDevices.setOnClickListener(v -> openAppsAndDevicesActivity());
        mLayoutHealthMatrics.setOnClickListener(v -> checkforHealthMarticsIsExpanded());
        setupRecyclerview(mRecyclerViewHealthMatrics, "Health Matrics", healthList);


        menuItemAddCondition.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_CNDTN_BTN_CLK);
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.MY_CONDITION_SCREEN_ADD_CONDITION_FLOAT_BUTTON_CLICKED, null);
            openAddConditionActivity();
        });

        menuItemAddVitals.setOnClickListener(view -> {
            Log.i("checkitemonclick","onclick data 1 ");
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_VITAL_BTN_CLK);
            AppUtils.logCleverTapEvent(getActivity(), Constants.CONDITIONS_SCREEN_ADD_HEALTH_METRIC_FLOATING_BUTTON_CLICKED, null);
            openManageActivity(getString(R.string.add_vital));
        });

        menuItemAddTest.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_TEST_BTN_CLK);
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

        menuItemAddSymptoms.setOnClickListener(v -> openAddSymptomsScreen());
    }

    private void openAddSymptomsScreen() {
        Intent intent = new Intent(getActivity(), AddSymptomsActivity.class);
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(MyConditionsNewActivity.userDto));
        startActivity(intent);
    }

    private void openManageActivity(String condition) {
        if (getContext() != null) {
            Log.i("checkitemonclick","onclick data 2 =  "+condition);
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

    private void openAppsAndDevicesActivity() {
        ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");
        startActivity(new Intent(getActivity(), AppsDevicesActivity.class));
    }

    private void openAddConditionActivity() {
        if (getContext() != null) {
            ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");
            Intent intent = new Intent(getContext(), AddConditionsActivity.class);
            startActivity(intent);
        }
    }

    private void checkforVaccinesIsExpanded() {
        if (!isExpandedVaccines && vaccinesList != null) {
            isExpandedVaccines = true;
            mImageViewVaccineDown.setVisibility(View.GONE);
            mImageViewVaccineUp.setVisibility(View.VISIBLE);
            setupRecyclerview(mRecyclerViewVaccines, "Vaccines", vaccinesList);
        } else {
            isExpandedVaccines = false;
            mImageViewVaccineUp.setVisibility(View.GONE);
            mRecyclerViewVaccines.setVisibility(View.GONE);
            mImageViewVaccineDown.setVisibility(View.VISIBLE);
        }
    }

    private void checkforCheckupsIsExpanded() {
        if (!isExpandedCheckups && checkupList != null) {
            isExpandedCheckups = true;
            mImageViewCheckupDown.setVisibility(View.GONE);
            mImageViewCheckupUp.setVisibility(View.VISIBLE);
            setupRecyclerview(mRecyclerViewCheckups, "Checkups", checkupList);
        } else {
            isExpandedCheckups = false;
            mImageViewCheckupUp.setVisibility(View.GONE);
            mRecyclerViewCheckups.setVisibility(View.GONE);
            mImageViewCheckupDown.setVisibility(View.VISIBLE);
        }
    }

    private void checkforHealthMarticsIsExpanded() {
        if (!isExpandedHealthMatics && healthList != null ) {
            isExpandedHealthMatics = true;
            mImageViewHealthDown.setVisibility(View.GONE);
            mImageViewHealthUp.setVisibility(View.VISIBLE);
            setupRecyclerview(mRecyclerViewHealthMatrics, "Health Matrics", healthList);
        } else {
            isExpandedHealthMatics = false;
            mImageViewHealthUp.setVisibility(View.GONE);
            mImageViewHealthDown.setVisibility(View.VISIBLE);
            mRecyclerViewHealthMatrics.setVisibility(View.GONE);
        }
    }

    private void initializeIds(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userData",MODE_PRIVATE);
        edit = sharedPreferences.edit();
        datanofoundtvtv = view.findViewById(R.id.datanofoundtv);
        mImageViewHealthUp = view.findViewById(R.id.imageview_health_up);
        mImageViewCheckupUp = view.findViewById(R.id.imageview_checkup_up);
        mImageViewVaccineUp = view.findViewById(R.id.imageview_vaccine_up);
        mImageViewHealthDown = view.findViewById(R.id.imageview_health_down);
        mImageViewCheckupDown = view.findViewById(R.id.imageview_checkup_down);
        mImageViewVaccineDown = view.findViewById(R.id.imageview_vaccine_down);

        mTextViewVaccines = view.findViewById(R.id.textview_vaccines);
        mTextViewCheckups = view.findViewById(R.id.textview_checkups);
        mTextViewHealthMatrics = view.findViewById(R.id.textview_health_matrics);

        mLayoutCheckups = view.findViewById(R.id.layout_checkups);
        mLayoutVaccines = view.findViewById(R.id.layout_vaccines);
        mLayoutHealthMatrics = view.findViewById(R.id.layout_health_matrics);
        mLayoutAppsAndDevices = view.findViewById(R.id.layout_main_apps_devices);

        floatingActionMenu = view.findViewById(R.id.menu);
        menuItemAddTest = view.findViewById(R.id.menu_item_add_test);
        menuItemAddVitals = view.findViewById(R.id.menu_item_add_vital);
        menuItemAddSymptoms = view.findViewById(R.id.menu_item_add_symptoms);
        menuItemAddVaccines = view.findViewById(R.id.menu_item_add_vaccines);
        menuItemAddCondition = view.findViewById(R.id.menu_item_add_condition);

        mRecyclerViewCheckups = view.findViewById(R.id.recycler_view_checkups);
        mRecyclerViewVaccines = view.findViewById(R.id.recycler_view_vaccines);
        mRecyclerViewHealthMatrics = view.findViewById(R.id.recycler_view_health_matrics);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTrackersList();
        AppUtils.syncFitBitData(getActivity(), true, false);
    }

    private void setupRecyclerview(RecyclerView mRecyclerView, String type, List<ParameterDto> dataList) {
        Log.i("checkitemonclickCustom", "api login type  = " + type);

        mRecyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (type.equalsIgnoreCase("Health Matrics")) {
            HealthMatricsListAdapter adapter = new HealthMatricsListAdapter(getActivity(), dataList);
            mRecyclerView.setAdapter(adapter);
        } else if (type.equalsIgnoreCase("Checkups")) {
            CheckupsListAdapter adapter = new CheckupsListAdapter(getActivity(), dataList);
            mRecyclerView.setAdapter(adapter);
        } else if (type.equalsIgnoreCase("Vaccines")) {
            VaccinesListAdapter adapter = new VaccinesListAdapter(getActivity(), dataList);
            mRecyclerView.setAdapter(adapter);
        }
    }
}