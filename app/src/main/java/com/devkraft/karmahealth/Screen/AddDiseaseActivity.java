package com.devkraft.karmahealth.Screen;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.DiseaseAdapter;
import com.devkraft.karmahealth.Adapter.FindDiseaseAdapter;
import com.devkraft.karmahealth.Adapter.UserSpinnerAdapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.CustomDieaseaseDto;
import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.Model.DiseaseParameterResponseDTO;
import com.devkraft.karmahealth.Model.DiseasesDropDownDto;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.ExpandableHeightListView;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.android.volley.Request;
import com.devkraft.karmahealth.Adapter.AddSymptomsListAdapter;
import com.devkraft.karmahealth.Model.AddCustomSymptomRequest;
import com.devkraft.karmahealth.Model.AddSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.GetSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.SymptomRequest;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.google.gson.Gson;

public class AddDiseaseActivity extends BaseActivity {
    private ProgressDialogSetup progress;
    private TextView userNameTv;
    private Spinner userSpinner;
    private UserDto userDto;
    private LinearLayout parentLayout;
    private ExpandableHeightListView diseaseList;
    private DiseaseAdapter diseaseAdapter;
    private DiseasesDropDownDto diseasesDropDownDto;
    private DiseaseParameterResponseDTO diseaseScheduleDto;
    private TextView cantFindYourDiseaseTv;
    private LinearLayout selectDiseaseLl, vitalLl;
    private MenuItem menuItem;
    private LinearLayout testLl, vaccinationLl;
    private ExpandableHeightListView testList, vaccinationList;
    private LinearLayout premiumMessageView;
    private LinearLayout premiumLl;
    private Button goPremiumBtn, addCustomConditionBtn, addasCustomBtn;
    private AutoCompleteTextView conditionSearchEt;
    private ImageView condition_searchIv;
    private FindDiseaseAdapter findDiseaseAdapter;
    String searchTextString;
    DiseasesDropDownDto.DiseasesDropDownDtoList totalDiseaseDropDownDtoList;
    private boolean isUserTryAddCondition;
    private boolean userIsInteracting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disease);
        setUpToolbar();
        iniIds();
        checkPremiumView();
        handleClickEvent();
        setEmptyView();
        AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_OPENED);
        AppUtils.logCleverTapEvent(this,Constants.ADD_CONDITION_SCREEN_OPENED, null);
    }

    private void checkPremiumView() {
        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto userDTO = loginResponse.getUserDTO();
            if (userDTO != null) {
                if (userDTO.isPremium()) {
                    // show premium view
                    premiumLl.setVisibility(View.VISIBLE);
                    premiumMessageView.setVisibility(View.GONE);

                    getIntentValue(getIntent());
                } else {
                    // show premium message
                    premiumLl.setVisibility(View.GONE);
                    premiumMessageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void handleClickEvent() {

        condition_searchIv.setOnClickListener(view -> {
            if (getContext() != null) {
                if (condition_searchIv.getDrawable().getConstantState().equals
                        (ContextCompat.getDrawable(AddDiseaseActivity.this, R.drawable.ic_cancel_black_24dp).getConstantState())) {
                    conditionSearchEt.setText("");
                    addasCustomBtn.setVisibility(View.GONE);
                    setEmptyView();
                }
            }
        });

        conditionSearchEt.setThreshold(1);
        conditionSearchEt.setAdapter(findDiseaseAdapter);
        conditionSearchEt.addTextChangedListener(new TextWatcher() {
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
                    searchTextString = conditionSearchEt.getText().toString();
                    if (searchTextString.length() > 0) {
                        setSearchViewIcon(R.drawable.ic_cancel_black_24dp);
                        callConditionSearchAPI(conditionSearchEt.getText().toString());

                    } else {
                        setSearchViewIcon(R.drawable.search);
                        vitalLl.setVisibility(View.GONE);
                        testLl.setVisibility(View.GONE);
                        vaccinationLl.setVisibility(View.GONE);
                        addasCustomBtn.setVisibility(View.GONE);
                        setEmptyView();
                    }
                }
            }
        });

        addasCustomBtn.setOnClickListener(view -> proceed());

        addCustomConditionBtn.setOnClickListener(view -> {
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
            } else {
                AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_valid_condition));
            }
        });

        goPremiumBtn.setOnClickListener(view -> AppUtils.openMyAccountPage(AddDiseaseActivity.this));

        cantFindYourDiseaseTv.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_REQ_DIS_BTN_CLK);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            AppUtils.openDialogBox(AddDiseaseActivity.this, userDto, ft, prev, false);
        });
    }

    private void setSearchViewIcon(int imageViewId) {
        condition_searchIv.setImageDrawable(ContextCompat.getDrawable(this, imageViewId));
    }

    private void callConditionSearchAPI(String conditionSearchStr) {
        if (getContext() != null) {
            AppUtils.logEvent(Constants.CONDITION_SEARCHED);
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddDiseaseActivity.this);
            GenericRequest<DiseasesDropDownDto.DiseasesDropDownDtoList> searchDrugRequest = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().CustomConditionSearch(userDto.getId(), conditionSearchStr),
                            DiseasesDropDownDto.DiseasesDropDownDtoList.class, null,
                            dosageDropDownDtoList -> {
                                authExpiredCallback.hideProgressBar();
                                vitalLl.setVisibility(View.GONE);
                                testLl.setVisibility(View.GONE);
                                vaccinationLl.setVisibility(View.GONE);
                                updateSearchDrugAdapter(dosageDropDownDtoList);
                            },
                            error -> {
                                AppUtils.hideKeyboard(AddDiseaseActivity.this);
                                authExpiredCallback.hideProgressBar();
                            });
            authExpiredCallback.setRequest(searchDrugRequest);
            ApiService.get().addToRequestQueue(searchDrugRequest);
        }
    }

    private void updateSearchDrugAdapter(DiseasesDropDownDto.DiseasesDropDownDtoList dosageDropDownDtoList) {

        DiseasesDropDownDto diseasesDropDownDto = new DiseasesDropDownDto();
        diseasesDropDownDto.setName(AppUtils.getHtmlString(AppUtils.getEncodedString(getString(R.string.add_custom_condition))).toString());
        dosageDropDownDtoList.add(0, diseasesDropDownDto);

        if (getContext() != null) {

            findDiseaseAdapter = new FindDiseaseAdapter(AddDiseaseActivity.this, R.layout.view_find_drug, dosageDropDownDtoList, diseasesDropDownDto1 -> {
                if (diseasesDropDownDto1.getName().equalsIgnoreCase(AppUtils.getEncodedString(getString(R.string.add_custom_condition)))) {
                    String strSearched = searchTextString.substring(0, 1).toUpperCase() + searchTextString.substring(1);
                    String inFormatString = strSearched.trim();
                    userIsInteracting = false;
                    conditionSearchEt.setText(inFormatString);
                    setMenuItemVisibility(false);
                    addasCustomBtn.setVisibility(View.VISIBLE);
                    AppUtils.logEvent(Constants.CNDTN_ADD_CNDTN_SCR_CUS_CNDTN_SLCT);
                    if (inFormatString.isEmpty()) {
                        setEmptyView();
                    }
                    diseaseScheduleDto = null;
                    selectDiseaseLl.setVisibility(View.GONE);
                    vitalLl.setVisibility(View.GONE);
                    testLl.setVisibility(View.GONE);
                    vaccinationLl.setVisibility(View.GONE);

                } else {
                    addasCustomBtn.setVisibility(View.GONE);
                    userIsInteracting = false;
                    conditionSearchEt.setText(diseasesDropDownDto1.getName());
                    callGetDiseaseScheduleAPI(diseasesDropDownDto1);
                    setMenuItemVisibility(true);
                    HashMap<String, Object> addConditionMap = new HashMap<>();
                    addConditionMap.put("Condition Name", diseasesDropDownDto1.getName());
                    AppUtils.logCleverTapEvent(this,Constants.CONDITION_SEARCHED_CLICK, addConditionMap);
                }
                isUserTryAddCondition = true;
                conditionSearchEt.dismissDropDown();
                userIsInteracting = false;
                conditionSearchEt.setSelection(conditionSearchEt.getText().length());
                AppUtils.hideKeyboard(AddDiseaseActivity.this);
            });

        }

        if (conditionSearchEt != null && findDiseaseAdapter != null) {
            conditionSearchEt.setAdapter(findDiseaseAdapter);
            findDiseaseAdapter.notifyDataSetChanged();
        }

    }

    public interface searchDieasaseCallback {
        void callDieasaseDto(DiseasesDropDownDto diseasesDropDownDto);
    }

    private void iniIds() {
        addCustomConditionBtn = findViewById(R.id.addCustomConditionBtn);
        conditionSearchEt = findViewById(R.id.cndtn_search_text);
        conditionSearchEt.setSelection(conditionSearchEt.getText().length());
        condition_searchIv = findViewById(R.id.condition_searchIv);
        goPremiumBtn = findViewById(R.id.goPremiumBtn);
        premiumLl = findViewById(R.id.premiumLl);
        premiumMessageView = findViewById(R.id.premiumMessageView);
        progress = ProgressDialogSetup.getProgressDialog(AddDiseaseActivity.this);
        userNameTv = findViewById(R.id.userNameTv);
        userSpinner = findViewById(R.id.userSpinner);
        parentLayout = findViewById(R.id.parentLayout);
        diseaseList = findViewById(R.id.diseaseList);
        diseaseList.setExpanded(true);
        cantFindYourDiseaseTv = findViewById(R.id.cantFindYourDisease);
        selectDiseaseLl = findViewById(R.id.selectDiseaseLl);
        vitalLl = findViewById(R.id.vitalLl);
        testLl = findViewById(R.id.testLl);
        testList = findViewById(R.id.testList);
        testList.setExpanded(true);
        vaccinationLl = findViewById(R.id.vaccinationLl);
        vaccinationList = findViewById(R.id.vaccinationList);
        vaccinationList.setExpanded(true);
        addasCustomBtn = findViewById(R.id.addasCustomBtn);
        conditionSearchEt.setFilters(new InputFilter[]{AppUtils.setInputFilterToRestrictEmoticons()});
    }

    private void generateVitalList(List<DiseaseParameterDTO> termParametersDtoList) {
        if (termParametersDtoList != null && termParametersDtoList.size() > 0) {
            vitalLl.setVisibility(View.VISIBLE);
            diseaseAdapter = new DiseaseAdapter(this, R.layout.disease_list, termParametersDtoList, userDto, true);
            diseaseList.setAdapter(diseaseAdapter);
        } else {
            vitalLl.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);

        menuItem = menu.findItem(R.id.done);
        menuItem.setTitle(getString(R.string.activate));
        menuItem.setVisible(diseasesDropDownDto != null);
        return true;
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

        if (item.getItemId() == R.id.done) {
            AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_SCR_ACTIVATE_BTN_CLK);
            proceed();
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
        LayoutInflater layoutInflater = LayoutInflater.from(AddDiseaseActivity.this);
        View promptView = layoutInflater.inflate(R.layout.video_alert_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddDiseaseActivity.this, R.style.AlertDialogTheme);
        alertDialogBuilder.setView(promptView);

        TextView msgTv = promptView.findViewById(R.id.msg);
        msgTv.setText(getString(R.string.need_help_adding_condition));

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getString(R.string.watch_video), (dialog, id) -> {
                    AppUtils.logEvent(Constants.VIDEO_POPUP_WATCHVIDEO_BTN_CLK);
                    Intent intent = new Intent(AddDiseaseActivity.this, FullScreenActivity.class);
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

    private void proceed() {
        searchTextString = conditionSearchEt.getText().toString();
        if (searchTextString != null && !searchTextString.isEmpty()) {
            if (userDto != null && diseaseScheduleDto != null) {
                HashMap<String, Object> addConditionMap = new HashMap<>();
                addConditionMap.put("Condition Name", searchTextString);
                AppUtils.logCleverTapEvent(AddDiseaseActivity.this,
                        Constants.CONDITION_FORM_SUBMITTED, addConditionMap);
                callAddDiseaseAPI();
            } else {
                AppUtils.logEvent(Constants.CNDTN_ADD_CUS_CNDTN_SCR_ACTIVATE_BTN_CLK);
                callAddCustomDiseaseAPI();
                //AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_valid_disease));
            }
        } else {
            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_valid_disease));
        }
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
                            Intent intent = new Intent(AddDiseaseActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            AppUtils.isDataChanged = true;
                            finish();
                        },
                        error -> {
                            AppUtils.hideProgressBar(progress);
                            authExpiredCallback.hideProgressBar();
                            String res = AppUtils.getVolleyError(AddDiseaseActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(parentLayout, res);
                        });
        authExpiredCallback.setRequest(getDiseasRequest);
        ApiService.get().addToRequestQueue(getDiseasRequest);
    }

    private void callAddDiseaseAPI() {
        progress.show();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getAddDisease(userDto.getId()),
                        APIMessageResponse.class, diseaseScheduleDto,
                        dosageDropDownDtoList -> {
                            AppUtils.syncFitBitData(this, false, false);
                            AppUtils.hideProgressBar(progress);
                            Intent intent = new Intent(AddDiseaseActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            AppUtils.isDataChanged = true;
                            finish();
                        },
                        error -> {
                            AppUtils.hideProgressBar(progress);
                            authExpiredCallback.hideProgressBar();
                            String res = AppUtils.getVolleyError(AddDiseaseActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(parentLayout, res);
                        });
        authExpiredCallback.setRequest(getDiseasRequest);
        ApiService.get().addToRequestQueue(getDiseasRequest);
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
                                generateVitalList(diseaseScheduleDto.getVitals());
                                generateTestList(diseaseScheduleDto.getTests());
                                generateVaccinesList(diseaseScheduleDto.getVaccinations());
                            },
                            error -> {
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(AddDiseaseActivity.this, error);
                                AppUtils.openSnackBar(parentLayout, res);
                            });
            ApiService.get().addToRequestQueue(getScheduleRequest);
        }
    }

    private void generateVaccinesList(List<DiseaseParameterDTO> termParametersDtoList) {
        if (termParametersDtoList != null && termParametersDtoList.size() > 0) {
            vaccinationLl.setVisibility(View.VISIBLE);
            diseaseAdapter = new DiseaseAdapter(this, R.layout.disease_list, termParametersDtoList, userDto, true);
            vaccinationList.setAdapter(diseaseAdapter);
        } else {
            vaccinationLl.setVisibility(View.GONE);
        }
    }

    private void generateTestList(List<DiseaseParameterDTO> termParametersDtoList) {
        if (termParametersDtoList != null && termParametersDtoList.size() > 0) {
            testLl.setVisibility(View.VISIBLE);
            diseaseAdapter = new DiseaseAdapter(this, R.layout.disease_list, termParametersDtoList, userDto, true);
            testList.setAdapter(diseaseAdapter);
        } else {
            testLl.setVisibility(View.GONE);
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
                        conditionSearchEt.setText(dropDownDtoName);
                        conditionSearchEt.setSelection(dropDownDtoName.length());
                        searchTextString = diseasesDropDownDto.getName();
                        condition_searchIv.setImageDrawable(ContextCompat.getDrawable(AddDiseaseActivity.this, R.drawable.ic_cancel_black_24dp));
                        AppUtils.hideSoftKeyboard(this);
                        callGetDiseaseScheduleAPI(diseasesDropDownDto);

                    }
                }
            }
        } else {
            generateUserList(null);
        }

    }

    private void generateUserList(UserDto selectedUserDto) {

        userNameTv.setVisibility(View.GONE);
        userSpinner.setVisibility(View.VISIBLE);
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

                userDtoList.add(uDto);
            }
        }

        UserSpinnerAdapter userSpinnerAdapter = new UserSpinnerAdapter(AddDiseaseActivity.this, userDtoList,"");
        userSpinner.setAdapter(userSpinnerAdapter);
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    userSpinner.setSelection(i);
                    userDto = selectedUserDto;
                }
            }
        }
    }

    private void setEmptyView() {
        diseaseScheduleDto = null;
        setMenuItemVisibility(false);

        vitalLl.setVisibility(View.GONE);
        testLl.setVisibility(View.GONE);
        vaccinationLl.setVisibility(View.GONE);
    }

    private void setMenuItemVisibility(boolean isNeedToVisible) {
        if (menuItem != null) {
            menuItem.setVisible(isNeedToVisible);
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.my_condition));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));
    }
}