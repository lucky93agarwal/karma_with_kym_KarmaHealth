package com.devkraft.karmahealth.Screen;

import static java.security.AccessController.getContext;

import com.devkraft.karmahealth.Adapter.AddChekupListAdapter;
import com.devkraft.karmahealth.Adapter.AddConditionSymptomsListAdapter;
import com.devkraft.karmahealth.Adapter.AddHealthMatricsListAdapter;
import com.devkraft.karmahealth.Adapter.AddVaccinesListAdapter;
import com.devkraft.karmahealth.Adapter.UserSpinnerAdapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddParameterRequest;
import com.devkraft.karmahealth.Model.CustomDieaseaseDto;
import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.FindDiseaseAdapter;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.Model.DiseaseParameterResponseDTO;
import com.devkraft.karmahealth.Model.DiseasesDropDownDto;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.SymptomsDTO;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
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
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddConditionsActivity extends AppCompatActivity  implements RvClickListener {
    private int addPosition;
    private UserDto userDto;
    private String searchTextString;
    private boolean userIsInteracting;
    private boolean isUserTryAddCondition;
    private DiseaseDto conditionResponseList;
    private FindDiseaseAdapter findDiseaseAdapter;
    private DiseasesDropDownDto diseasesDropDownDto;
    private DiseaseParameterResponseDTO diseaseScheduleDto;
    private DiseasesDropDownDto.DiseasesDropDownDtoList totalDiseaseDropDownDtoList;

    private Spinner mSpinnerUser;
    private TextView mTextViewNote;
    private RelativeLayout mParentLayout;
    private ProgressDialogSetup progress;
    private Button mButtonAddCustomCondition;
    private ImageView mImageViewConditionSearch;
    private RelativeLayout mLayoutStartManaging;
    private AutoCompleteTextView mAutoCompleteConditionName;
    private LinearLayout mLayoutChekups;
    private LinearLayout mLayoutVaccines;
    private LinearLayout mLayoutSymptoms;
    private LinearLayout mLayoutHealthMatrics;

    private RecyclerView mRecyclerViewCheckups;
    private RecyclerView mRecyclerViewVaccines;
    private RecyclerView mRecyclerViewSymptoms;
    private RecyclerView mRecyclerViewHealthMatrics;

    private ImageView mImageViewUserAvtar;

    private TextView mTextViewCustomConditionNote;
    private List<SymptomsDTO> unSelctedSymptoms = new ArrayList<>();
    private List<DiseaseParameterDTO> unSelctedCheckups =  new ArrayList<>();
    private List<DiseaseParameterDTO> unSelctedHealthMatrics = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conditions);

        setupToolbar();
        initializeIds();
        checkPremiumView();
        handleClickEvent();
        setAllViewsEmpty();
        getConditionResponseList();
        AppUtils.logCleverTapEvent(this, Constants.ADD_CONDITION_SCREEN_OPENED, null);
    }

    private void getConditionResponseList() {
        String conditionList = ApplicationPreferences.get().getStringValue(Constants.CONDITION_LIST);
        if (conditionList != null && !conditionList.matches("")) {
            Gson conditionListGson = new Gson();
            Type typeWebinar = new TypeToken<DiseaseDto>() {
            }.getType();
            conditionResponseList = conditionListGson.fromJson(conditionList, typeWebinar);
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void setAllViewsEmpty() {
        diseaseScheduleDto = null;

        mTextViewNote.setVisibility(View.GONE);
        mLayoutChekups.setVisibility(View.GONE);
        mLayoutVaccines.setVisibility(View.GONE);
        mLayoutSymptoms.setVisibility(View.GONE);
        mLayoutHealthMatrics.setVisibility(View.GONE);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.add_condition_header));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }

    private void initializeIds() {
        mParentLayout = findViewById(R.id.parentLayout);
        mTextViewCustomConditionNote = findViewById(R.id.textview_custom_condition);
        mButtonAddCustomCondition = findViewById(R.id.button_add_custom_condition);

        mImageViewUserAvtar = findViewById(R.id.imageview_user_avtar);

        mSpinnerUser = findViewById(R.id.userSpinner);
        mTextViewNote = findViewById(R.id.textview_note);
        mAutoCompleteConditionName = findViewById(R.id.cndtn_search_text);
        mImageViewConditionSearch = findViewById(R.id.imageview_condition_search);
        mAutoCompleteConditionName.setSelection(mAutoCompleteConditionName.getText().length());

        mLayoutChekups = findViewById(R.id.layout_add_checkups);
        mLayoutVaccines = findViewById(R.id.layout_add_vaccines);
        mLayoutSymptoms = findViewById(R.id.layout_add_symptoms);
        mLayoutStartManaging = findViewById(R.id.layout_start_managing);
        mLayoutHealthMatrics = findViewById(R.id.layout_add_health_matrics);

        mRecyclerViewCheckups = findViewById(R.id.recyclerview_add_checkups);
        mRecyclerViewVaccines = findViewById(R.id.recyclerview_add_vaccines);
        mRecyclerViewSymptoms = findViewById(R.id.recyclerview_add_symptoms);
        mRecyclerViewHealthMatrics = findViewById(R.id.recyclerview_add_healthmartics);

        progress = ProgressDialogSetup.getProgressDialog(AddConditionsActivity.this);
        mAutoCompleteConditionName.setFilters(new InputFilter[]{AppUtils.setInputFilterToRestrictEmoticons()});
    }

    private void checkPremiumView() {
        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto userDTO = loginResponse.getUserDTO();
            if (userDTO != null) {
                if (userDTO.isPremium()) {
                    // show premium view
                 /*   premiumLl.setVisibility(View.VISIBLE);
                    premiumMessageView.setVisibility(View.GONE);*/

                    getIntentValue(getIntent());
                } else {
                    // show premium message
                   /* premiumLl.setVisibility(View.GONE);
                    premiumMessageView.setVisibility(View.VISIBLE);*/
                }
            }
        }
    }

    private void getIntentValue(Intent intent) {
        if (intent != null) {
            Gson gson = new Gson();
            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            String diseaseDropDtoStr = intent.getStringExtra(Constants.DISEASES_DTO);
            if (userDtoStr != null) {
                UserDto userDto = gson.fromJson(userDtoStr, UserDto.class);
                generateUserList(userDto);
            } else {
                generateUserList(null);
            }

            if (diseaseDropDtoStr != null) {
                diseasesDropDownDto = gson.fromJson(diseaseDropDtoStr, DiseasesDropDownDto.class);
                if (diseasesDropDownDto != null) {
                    String dropDownDtoName = diseasesDropDownDto.getName();
                    if (dropDownDtoName != null) {
                        mAutoCompleteConditionName.setText(dropDownDtoName);
                        mAutoCompleteConditionName.setSelection(dropDownDtoName.length());
                        searchTextString = diseasesDropDownDto.getName();
                        mImageViewConditionSearch.setImageDrawable(ContextCompat.getDrawable(AddConditionsActivity.this, R.drawable.ic_cancel_black_24dp));
                        AppUtils.hideSoftKeyboard(this);
                        callGetConditionScheduleAPI(diseasesDropDownDto);

                    }
                }
            }
        } else {
            generateUserList(null);
        }

    }

    private void callGetConditionScheduleAPI(DiseasesDropDownDto diseasesDropDownDto) {
        AppUtils.logCleverTapEvent(AddConditionsActivity.this,
                Constants.CONDITION_SEARCHED, null);
        if (diseasesDropDownDto != null) {
            progress.show();
            GenericRequestWithoutAuth<DiseaseParameterResponseDTO> getScheduleRequest = new GenericRequestWithoutAuth<>
                    (Request.Method.GET, APIUrls.get().getDiseaseSchedule(diseasesDropDownDto.getId()),
                            DiseaseParameterResponseDTO.class, null,
                            diseaseParameterResponseDTO -> {
                                AppUtils.hideProgressBar(progress);
                                diseaseScheduleDto = diseaseParameterResponseDTO;
                                Log.e("Health_Matrics_size", "" + diseaseScheduleDto.getVitals());
                                Log.e("Checkups_size", "" + diseaseScheduleDto.getTests());
                                Log.e("Vaccines_size", "" + diseaseScheduleDto.getVaccinations());
                                generateCheckupsList(diseaseScheduleDto.getTests());
                                generateHealthMatricsList(diseaseScheduleDto.getVitals());
                                generateVaccinesList(diseaseScheduleDto.getVaccinations());
                                generateSymptomsList(diseaseScheduleDto.getSymptoms());
                            },
                            error -> {
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(AddConditionsActivity.this, error);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            ApiService.get().addToRequestQueue(getScheduleRequest);
        }
    }

    private void generateCheckupsList(List<DiseaseParameterDTO> checkupList) {
        if (checkupList != null && checkupList.size() > 0) {
            mTextViewNote.setVisibility(View.VISIBLE);
            mLayoutChekups.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerViewCheckups.setLayoutManager(linearLayoutManager);
            AddChekupListAdapter adapter = new AddChekupListAdapter(this, checkupList);
            mRecyclerViewCheckups.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            mLayoutChekups.setVisibility(View.GONE);
        }
    }

    private void generateHealthMatricsList(List<DiseaseParameterDTO> healthList) {
        if (healthList != null && healthList.size() > 0) {
            mTextViewNote.setVisibility(View.VISIBLE);
            mLayoutHealthMatrics.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerViewHealthMatrics.setLayoutManager(linearLayoutManager);
            AddHealthMatricsListAdapter adapter = new AddHealthMatricsListAdapter(this, healthList);
            mRecyclerViewHealthMatrics.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            mLayoutHealthMatrics.setVisibility(View.GONE);
        }
    }

    private void generateVaccinesList(List<DiseaseParameterDTO> vaccinationsList) {
        if (vaccinationsList != null && vaccinationsList.size() > 0) {
            mTextViewNote.setVisibility(View.VISIBLE);
            mLayoutVaccines.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerViewVaccines.setLayoutManager(linearLayoutManager);
            AddVaccinesListAdapter adapter = new AddVaccinesListAdapter(this, vaccinationsList);
            mRecyclerViewVaccines.setAdapter(adapter);
        } else {
            mLayoutVaccines.setVisibility(View.GONE);
        }
    }


    private void generateUserList(UserDto selectedUserDto) {
        mSpinnerUser.setVisibility(View.VISIBLE);
        final List<UserDto> userDtoList = new ArrayList<>();

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
        if (dependentDtoList != null && dependentDtoList.size() > 0) {
            for (int i = 0; i < dependentDtoList.size(); i++) {
                DependentDto dependentDto = dependentDtoList.get(i);
                uDto = new UserDto();

                uDto.setName(dependentDto.getName());
                uDto.setId(dependentDto.getId());
                uDto.setAvatarName(dependentDto.getAvatarName());

                userDtoList.add(uDto);
            }
        }

        UserSpinnerAdapter userSpinnerAdapter = new UserSpinnerAdapter(AddConditionsActivity.this, userDtoList,"");
        mSpinnerUser.setAdapter(userSpinnerAdapter);
        mSpinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userDto = userDtoList.get(position);
                AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_PROFILE_SELECTION_CLK);
                AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_PROFILE_SLCT);
                // callGetDiseasesAPI();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectedUserDto != null) {
            for (int i = 0; i < userDtoList.size(); i++) {
                UserDto localUserDto = userDtoList.get(i);
                if (localUserDto.getId().equals(selectedUserDto.getId())) {
                    mSpinnerUser.setSelection(i);
                    userDto = selectedUserDto;
                    setUserAvtar(userDto);
                }
            }
        }
    }

    private void setUserAvtar(UserDto userDto) {
        Integer resourceImage = AppUtils.getUserResourcedId(this, userDto.getAvatarName());
        Log.e("resourceImageAvtar", ":" + resourceImage);
        if (resourceImage != null) {
            mImageViewUserAvtar.setImageResource(resourceImage);
        } else {
            mImageViewUserAvtar.setImageResource(R.drawable.avatars_generic);
        }
    }

    private void handleClickEvent() {

        mLayoutStartManaging.setOnClickListener(v -> {
            proceed();
        });

        mImageViewConditionSearch.setOnClickListener(view -> {
            if (getContext() != null) {
                if (mImageViewConditionSearch.getDrawable().getConstantState().equals
                        (ContextCompat.getDrawable(AddConditionsActivity.this, R.drawable.ic_cancel_black_24dp).getConstantState())) {
                    mAutoCompleteConditionName.setText("");
                    mLayoutStartManaging.setVisibility(View.VISIBLE);
                    mButtonAddCustomCondition.setVisibility(View.GONE);
                    mTextViewCustomConditionNote.setVisibility(View.GONE);
                    setAllViewsEmpty();
                }
            }
        });

        mAutoCompleteConditionName.setThreshold(1);
        mAutoCompleteConditionName.setAdapter(findDiseaseAdapter);
        mAutoCompleteConditionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppUtils.cancelAPICalls();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (userIsInteracting) {
                    searchTextString = mAutoCompleteConditionName.getText().toString();
                    if (searchTextString.length() > 0) {
                        setSearchViewIcon(R.drawable.ic_cancel_black_24dp);
                        callConditionSearchAPI(mAutoCompleteConditionName.getText().toString());

                    } else {
                        setSearchViewIcon(R.drawable.search);
                        mLayoutHealthMatrics.setVisibility(View.GONE);
                        mLayoutChekups.setVisibility(View.GONE);
                        mLayoutVaccines.setVisibility(View.GONE);
                        mLayoutStartManaging.setVisibility(View.VISIBLE);
                        mButtonAddCustomCondition.setVisibility(View.GONE);
                        mTextViewCustomConditionNote.setVisibility(View.GONE);
                        setAllViewsEmpty();
                    }
                }
            }
        });

        mButtonAddCustomCondition.setOnClickListener(view -> proceed());

        mAutoCompleteConditionName.setOnClickListener(view -> {
            String customCondition = "";//customConditionEt.getText().toString();
            if (customCondition.length() > 0) {
                // call api call
                int pos = -1;
                for (int i = 0; i < totalDiseaseDropDownDtoList.size(); i++) {
                    if (customCondition.equalsIgnoreCase(totalDiseaseDropDownDtoList.get(i).getName())) {
                        pos = i;
                        break;
                    }
                }
                if (pos != -1) {
                    callGetDiseaseScheduleAPI(totalDiseaseDropDownDtoList.get(pos));// normal api call
                    proceed();
                }
            } /*else {
                AppUtils.openSnackBar(mParentLayout, getString(R.string.please_enter_valid_condition));
            }*/
        });

//        goPremiumBtn.setOnClickListener(view -> AppUtils.openMyAccountPage(AddConditionsActivity.this));
    }

    private void proceed() {
        searchTextString = mAutoCompleteConditionName.getText().toString();
        if (searchTextString != null && !searchTextString.isEmpty()) {
            if (userDto != null && diseaseScheduleDto != null) {
                callAddDiseaseAPI();
            } else {
                AppUtils.logEvent(Constants.CNDTN_ADD_CUS_CNDTN_SCR_ACTIVATE_BTN_CLK);
                callAddCustomDiseaseAPI();
                //AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_valid_disease));
            }
        } else {
            AppUtils.openSnackBar(mParentLayout, getString(R.string.please_select_valid_condition));
        }
    }

    private void callAddDiseaseAPI() {

        try {
            progress.show();

            diseaseScheduleDto.setVitals(getSelectedHealthData());
            diseaseScheduleDto.setTests(getSelectedCheckupList());
            diseaseScheduleDto.setSymptoms(getSelectedSymptomsList());

            Log.e("sendinParams_log", " = " + new Gson().toJson(diseaseScheduleDto));

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
            GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                    (Request.Method.POST, APIUrls.get().getAddDisease(userDto.getId()),
                            APIMessageResponse.class, diseaseScheduleDto,
                            dosageDropDownDtoList -> {
                                AppUtils.syncFitBitData(this, false, false);
                                AppUtils.hideProgressBar(progress);
                                Intent intent = new Intent(AddConditionsActivity.this, HomeActivity.class);
                                /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                startActivity(intent);
                                AppUtils.isDataChanged = true;

                                HashMap<String, Object> addConditionMap = new HashMap<>();
                                addConditionMap.put("Condition Name", searchTextString);
                                AppUtils.logCleverTapEvent(AddConditionsActivity.this,
                                        Constants.CONDITION_FORM_SUBMITTED, addConditionMap);

                                ApplicationPreferences.get().saveStringValue(Constants.CONDITION_NAME, searchTextString);
                                ApplicationPreferences.get().saveStringValue(Constants.CONDITION_ID_INTERCOM, String.valueOf(diseaseScheduleDto.getDiseaseId()));
                                finish();
                            },
                            error -> {
                                AppUtils.hideProgressBar(progress);
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyError(AddConditionsActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            authExpiredCallback.setRequest(getDiseasRequest);
            ApiService.get().addToRequestQueue(getDiseasRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DiseaseParameterDTO> getSelectedHealthData() {
        List<DiseaseParameterDTO> selectedHealthMatrics = new ArrayList<>();
        if(unSelctedHealthMatrics !=  null && !unSelctedHealthMatrics.isEmpty()) {
            for(int i = 0; i < diseaseScheduleDto.getVitals().size(); i++) {
                for(int j = 0; j < unSelctedHealthMatrics.size(); j++) {
                    if (unSelctedHealthMatrics.get(j).equals(diseaseScheduleDto.getVitals().get(i))) {
                        diseaseScheduleDto.getVitals().remove(i);
                    }
                }
            }
        }

        return diseaseScheduleDto.getVitals();
    }

    private List<DiseaseParameterDTO> getSelectedCheckupList() {
        try {
            if (unSelctedCheckups != null && !unSelctedCheckups.isEmpty()) {
                for (int i = 0; i < diseaseScheduleDto.getTests().size(); i++) {
                    for (int j = 0; j < unSelctedCheckups.size(); j++) {
                        if (unSelctedCheckups.get(j).equals(diseaseScheduleDto.getTests().get(i))) {
                            diseaseScheduleDto.getTests().remove(i);
                        }
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return diseaseScheduleDto.getTests();
    }

    private List<SymptomsDTO> getSelectedSymptomsList() {
        if(unSelctedSymptoms !=  null && !unSelctedSymptoms.isEmpty()) {
            for(int i = 0; i < diseaseScheduleDto.getSymptoms().size(); i++) {
                for(int j = 0; j < unSelctedSymptoms.size(); j++) {
                    if (unSelctedSymptoms.get(j).equals(diseaseScheduleDto.getSymptoms().get(i))) {
                        diseaseScheduleDto.getSymptoms().remove(i);
                    }
                }
            }
        }

        return diseaseScheduleDto.getSymptoms();
    }

    private void callAddCustomDiseaseAPI() {
        progress.show();
        CustomDieaseaseDto customDieaseaseDtostr = new CustomDieaseaseDto();
        String strSearched = searchTextString.substring(0, 1).toUpperCase() + searchTextString.substring(1);
        customDieaseaseDtostr.setcustomDiseaseName(strSearched.trim());
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getAddCustomDisease(userDto.getId()),
                        APIMessageResponse.class, customDieaseaseDtostr,
                        dosageDropDownDtoList -> {
                            AppUtils.syncFitBitData(this, false, false);
                            AppUtils.hideProgressBar(progress);
                            Intent intent = new Intent(AddConditionsActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            AppUtils.isDataChanged = true;

                            ApplicationPreferences.get().saveStringValue(Constants.CONDITION_NAME,searchTextString);
                            ApplicationPreferences.get().saveStringValue(Constants.CONDITION_ID_INTERCOM, String.valueOf(0));
                            finish();
                        },
                        error -> {
                            AppUtils.hideProgressBar(progress);
                            authExpiredCallback.hideProgressBar();
                            String res = AppUtils.getVolleyError(AddConditionsActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(mParentLayout, res);
                        });
        authExpiredCallback.setRequest(getDiseasRequest);
        ApiService.get().addToRequestQueue(getDiseasRequest);
    }

    private void callConditionSearchAPI(String conditionSearchStr) {
        if (getContext() != null) {
            AppUtils.logEvent(Constants.CONDITION_SEARCHED);
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddConditionsActivity.this);
            GenericRequest<DiseasesDropDownDto.DiseasesDropDownDtoList> searchDrugRequest = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().CustomConditionSearch(userDto.getId(), conditionSearchStr),
                            DiseasesDropDownDto.DiseasesDropDownDtoList.class, null,
                            dosageDropDownDtoList -> {
                                authExpiredCallback.hideProgressBar();
                                mLayoutHealthMatrics.setVisibility(View.GONE);
                                mLayoutChekups.setVisibility(View.GONE);
                                mLayoutVaccines.setVisibility(View.GONE);
                                updateSearchDrugAdapter(dosageDropDownDtoList);
                            },
                            error -> {
                                AppUtils.hideKeyboard(AddConditionsActivity.this);
                                authExpiredCallback.hideProgressBar();
                            });
            authExpiredCallback.setRequest(searchDrugRequest);
            ApiService.get().addToRequestQueue(searchDrugRequest);
        }
    }

    private void setSearchViewIcon(int imageViewId) {
        mImageViewConditionSearch.setImageDrawable(ContextCompat.getDrawable(this, imageViewId));
    }

    private void updateSearchDrugAdapter(DiseasesDropDownDto.DiseasesDropDownDtoList dosageDropDownDtoList) {
        DiseasesDropDownDto diseasesDropDownDto = new DiseasesDropDownDto();
        diseasesDropDownDto.setName(AppUtils.getHtmlString(AppUtils.getEncodedString(getString(R.string.add_custom_condition))).toString());
        dosageDropDownDtoList.add(0, diseasesDropDownDto);

        if (getContext() != null) {

            findDiseaseAdapter = new FindDiseaseAdapter(AddConditionsActivity.this, R.layout.view_find_drug, dosageDropDownDtoList, diseasesDropDownDto1 -> {
                if (diseasesDropDownDto1.getName().equalsIgnoreCase(AppUtils.getEncodedString(getString(R.string.add_custom_condition)))) {
                    String strSearched = searchTextString.substring(0, 1).toUpperCase() + searchTextString.substring(1);
                    String inFormatString = strSearched.trim();
                    userIsInteracting = false;
                    mAutoCompleteConditionName.setText(inFormatString);
                    mLayoutStartManaging.setVisibility(View.GONE);
                    mButtonAddCustomCondition.setVisibility(View.VISIBLE);
                    mTextViewNote.setVisibility(View.GONE);
                    mTextViewCustomConditionNote.setVisibility(View.VISIBLE);
                    AppUtils.logEvent(Constants.CNDTN_ADD_CNDTN_SCR_CUS_CNDTN_SLCT);
                    if (inFormatString.isEmpty()) {
                        setAllViewsEmpty();
                    }
                    diseaseScheduleDto = null;
                    mLayoutHealthMatrics.setVisibility(View.GONE);
                    mLayoutChekups.setVisibility(View.GONE);
                    mLayoutVaccines.setVisibility(View.GONE);

                } else {
                    mButtonAddCustomCondition.setVisibility(View.GONE);
                    mTextViewCustomConditionNote.setVisibility(View.GONE);
                    mLayoutStartManaging.setVisibility(View.VISIBLE);
                    userIsInteracting = false;
                    mAutoCompleteConditionName.setText(diseasesDropDownDto1.getName());
                    callGetDiseaseScheduleAPI(diseasesDropDownDto1);
                    HashMap<String, Object> addConditionMap = new HashMap<>();
                    addConditionMap.put("Condition Name", diseasesDropDownDto1.getName());
                    AppUtils.logCleverTapEvent(this, Constants.CONDITION_SEARCHED_CLICK, addConditionMap);
                }
                isUserTryAddCondition = true;
                mAutoCompleteConditionName.dismissDropDown();
                userIsInteracting = false;
                mAutoCompleteConditionName.setSelection(mAutoCompleteConditionName.getText().length());
                AppUtils.hideKeyboard(AddConditionsActivity.this);
            });

        }

        if (mAutoCompleteConditionName != null && findDiseaseAdapter != null) {
            mAutoCompleteConditionName.setAdapter(findDiseaseAdapter);
            findDiseaseAdapter.notifyDataSetChanged();
        }
    }

    private void callGetDiseaseScheduleAPI(DiseasesDropDownDto diseasesDropDownDto) {
        if (diseasesDropDownDto != null) {
            progress.show();
            GenericRequestWithoutAuth<DiseaseParameterResponseDTO> getScheduleRequest = new GenericRequestWithoutAuth<>
                    (Request.Method.GET, APIUrls.get().getDiseaseSchedule(diseasesDropDownDto.getId()),
                            DiseaseParameterResponseDTO.class, null,
                            diseaseParameterResponseDTO -> {
                                AppUtils.hideProgressBar(progress);
                                diseaseScheduleDto = diseaseParameterResponseDTO;
                                Log.e("diseaseScheduleDto_log", " = " + new Gson().toJson(diseaseScheduleDto));
                                mTextViewNote.setText(getString(R.string.we_recommend_add_condition) + " "+ diseasesDropDownDto.getName()+ " " + getString(R.string.we_recommend_description_add_condition));
                                generateHealthMatricsList(diseaseScheduleDto.getVitals());
                                generateCheckupsList(diseaseScheduleDto.getTests());
                                generateVaccinesList(diseaseScheduleDto.getVaccinations());
                                generateSymptomsList(diseaseScheduleDto.getSymptoms());
                            },
                            error -> {
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(AddConditionsActivity.this, error);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            ApiService.get().addToRequestQueue(getScheduleRequest);
        }
    }

    private void generateSymptomsList(List<SymptomsDTO> symptomsList) {
        if (symptomsList != null && !symptomsList.isEmpty()) {
            mTextViewNote.setVisibility(View.VISIBLE);
            mLayoutSymptoms.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerViewSymptoms.setLayoutManager(linearLayoutManager);
            AddConditionSymptomsListAdapter adapter = new AddConditionSymptomsListAdapter(this, symptomsList);
            mRecyclerViewSymptoms.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            mLayoutSymptoms.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            performBackButtonClickEvent();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void performBackButtonClickEvent() {
        AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_BACK_BTN_CLK);
        checkIfNeedToShowDialogBoxForVideo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            performBackButtonClickEvent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkIfNeedToShowDialogBoxForVideo() {
        AppUtils.logEvent(Constants.CNDTN_SHOW_ADD_CONDITION_VIDEO);
        if (isUserTryAddCondition && userDto != null) {
            DiseaseDto diseaseDto = ApplicationDB.get().getDiseases(userDto.getId());
            if (diseaseDto != null && diseaseDto.getDiseases() != null && diseaseDto.getDiseases().size() > 0) {
                finish();
            } else {
                showDialogBoxForVideo();
            }
        } else {
            finish();
        }
    }

    private void showDialogBoxForVideo() {
        LayoutInflater layoutInflater = LayoutInflater.from(AddConditionsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.video_alert_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddConditionsActivity.this, R.style.AlertDialogTheme);
        alertDialogBuilder.setView(promptView);

        TextView msgTv = promptView.findViewById(R.id.msg);
        msgTv.setText(getString(R.string.need_help_adding_condition));

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getString(R.string.watch_video), (dialog, id) -> {
                    AppUtils.logEvent(Constants.VIDEO_POPUP_WATCHVIDEO_BTN_CLK);
                    Intent intent = new Intent(AddConditionsActivity.this, FullScreenActivity.class);
                    intent.putExtra(Constants.VIDEO_URL, getString(R.string.add_condition_video));
                    startActivity(intent);
                    finish();
                });

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void rv_click(int position, int value, String key) {
        if (key.equalsIgnoreCase("healthParameterAdd")) {
            addPosition = position;
           /* if (validate()) {
//               checkForHealthMatricsParameter(position);
            }*/
            removeHealthMatrics(position);
        } else if (key.equalsIgnoreCase("healthParameterDelete")) {
//           checkForHealthMatricsParameter(position);
            addToUnselectedList(position);
        } else if (key.equalsIgnoreCase("checkupAdd")) {
//            checkForCheckupUserParameterIdAndCallAPI(position);
            removeCheckupFromUnselctedList(position);
        } else if (key.equalsIgnoreCase("checkupDelete")) {
//            checkForCheckupUserParameterIdAndCallAPI(position);

            addToUnselectedCheckup(position);
        } else if (key.equalsIgnoreCase(Constants.ADD_SYMPTOMS)) {
            removeSymptomFromList(position);

        } else if (key.equalsIgnoreCase(Constants.REMOVE_SYMPTOMS)) {
            addSymptomToList(position);
        }
    }

    private void removeCheckupFromUnselctedList(int position) {
        try {
            if (unSelctedCheckups != null && !unSelctedCheckups.isEmpty()) {
                for (int i = 0; i < unSelctedCheckups.size(); i++) {
                    if (unSelctedCheckups.get(i).equals(diseaseScheduleDto.getTests().get(position))) {
                        unSelctedCheckups.remove(i);
                        i--;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToUnselectedCheckup(int position) {
        unSelctedCheckups.add(diseaseScheduleDto.getTests().get(position));
    }

    private void removeHealthMatrics(int position) {
        try {
            if (unSelctedHealthMatrics != null && !unSelctedHealthMatrics.isEmpty()) {
                for (int i = 0; i < unSelctedHealthMatrics.size(); i++) {
                    if (unSelctedHealthMatrics.get(i).equals(diseaseScheduleDto.getVitals().get(position))) {
                        unSelctedHealthMatrics.remove(i);
                        i--;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToUnselectedList(int position) {
        unSelctedHealthMatrics.add(diseaseScheduleDto.getVitals().get(position));
    }

    private void removeSymptomFromList(int position) {
        if(unSelctedSymptoms != null && !unSelctedSymptoms.isEmpty()) {
            for(int i = 0; i < unSelctedSymptoms.size(); i++) {
                if(unSelctedSymptoms.get(i).equals(diseaseScheduleDto.getSymptoms().get(position))) {
                    unSelctedSymptoms.remove(i);
                    i--;
                }
            }
        }
    }

    private void addSymptomToList(int position) {
        unSelctedSymptoms.add(diseaseScheduleDto.getSymptoms().get(position));
    }

    private void checkForHealthMatricsParameter(int position) {
        for (int i = 0; i < conditionResponseList.getVitals().size(); i++) {
            if (diseaseScheduleDto.getVitals().get(position).getParameterName().equals(conditionResponseList.getVitals().get(i).getName())) {
                toggleParameterAddOrRemove(userDto, conditionResponseList.getVitals().get(i).getUserParameterId());
            }
        }
    }

    private void checkForCheckupUserParameterIdAndCallAPI(int position) {
        for (int i = 0; i < conditionResponseList.getTests().size(); i++) {
            if (diseaseScheduleDto.getTests().get(position).getParameterName().equals(conditionResponseList.getTests().get(i).getName())) {
                toggleParameterAddOrRemove(userDto, conditionResponseList.getTests().get(i).getUserParameterId());
            }
        }
    }

    private boolean validate() {
        if (diseaseScheduleDto.getVitals().get(addPosition).getMedicalParameterType() == null) {
            return false;
        } else if (diseaseScheduleDto.getVitals().get(addPosition).getParameterName() == null) {
            return false;
        } else if (diseaseScheduleDto.getVitals().get(addPosition).getFrequencyUnit() == null) {
            return false;
        } else if (diseaseScheduleDto.getVitals().get(addPosition).getParameterId() == null) {
            return false;
        }
        return true;
    }

    private void callAddHealthMatricsAPI(int position) {
        progress.show();
        AddParameterRequest addParameterRequest = new AddParameterRequest();
        addParameterRequest.setMedicalParameterType(diseaseScheduleDto.getVitals().get(position)
                .getMedicalParameterType());
        addParameterRequest.setName(diseaseScheduleDto.getVitals().get(position).getParameterName());
        addParameterRequest.setFrequencyUnit(diseaseScheduleDto.getVitals().get(position).getFrequencyUnit());
        addParameterRequest.setUiText("");
        addParameterRequest.setMeasurementUnit("");
        addParameterRequest.setId(diseaseScheduleDto.getVitals().get(position).getParameterId());

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(
                this);
        GenericRequest<APIMessageResponse> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getToggleAPIUrl(userDto.getId()),
                        APIMessageResponse.class, addParameterRequest,
                        response -> {
                            progress.dismiss();
                            Toast.makeText(this, "Added new Parameter..!!", Toast.LENGTH_SHORT).show();
                        },
                        error -> {
                            progress.dismiss();
                            String reason = AppUtils.getVolleyError
                                    (AddConditionsActivity.this, error);
                            AppUtils.openSnackBar(mParentLayout, reason);
                        });
        authExpiredCallback.setRequest(addQuestionRequest);
        ApiService.get().addToRequestQueue(addQuestionRequest);
    }

    private void toggleParameterAddOrRemove(UserDto userDto, Long userParameterId) {
        if (getContext() != null) {

            Log.e("userParemeterId"," = "+userParameterId);

            if (progress != null) {
                progress.show();
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddConditionsActivity.this);
            GenericRequest<APIMessageResponse> getDeleteRequest = new GenericRequest<>
                    (Request.Method.PUT, APIUrls.get().setToggleState(userDto.getId(), userParameterId),
                            APIMessageResponse.class, null,
                            apiMessageResponse -> {
                                AppUtils.hideProgressBar(progress);
                                AppUtils.openSnackBar(mParentLayout, "Parameter Updated Sucessfully");
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(AddConditionsActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            authExpiredCallback.setRequest(getDeleteRequest);
            ApiService.get().addToRequestQueue(getDeleteRequest);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progress != null)
            progress.dismiss();
    }

}