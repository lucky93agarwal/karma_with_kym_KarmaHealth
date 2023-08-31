package com.devkraft.karmahealth.fragment;

import static com.devkraft.karmahealth.fragment.SymptomTrackersFragment.getUserAddedSymptomsResponseDTO;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConditionNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConditionNewFragment extends Fragment implements RvClickListener {
    public static UserDto userDto;
    private Button mButtonAddCondition;
    private ScrollView mEmptyScrollView;
    private RelativeLayout mParentLayout;
    private ProgressDialogSetup progress;
    private RecyclerView mRecyclerViewConditions;
//    private ProgressDialogSetup mProgressDialogSetup;
    private int removePosition;
    private Long userParameterID;
    private SymptomRequest symptomRequest;
    private DiseaseDto conditionListResponse;
    private ConditionsListAdapter conditionsListAdapter;
    private DiseaseParameterResponseDTO diseaseParameterResponse;
    public ArrayList<SymptomRequest.Symptom> symptomArrayList = new ArrayList<>();

    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton menuItemAddTest;
    private com.github.clans.fab.FloatingActionButton menuItemAddVitals;
    private com.github.clans.fab.FloatingActionButton menuItemAddVaccines;
    private com.github.clans.fab.FloatingActionButton menuItemAddSymptoms;
    private com.github.clans.fab.FloatingActionButton menuItemAddCondition;

    ArrayList<String> conditionsListTemp = new ArrayList<>();

    public ConditionNewFragment() {
    }
    private View view;

    public static ConditionNewFragment newInstance() {
        ConditionNewFragment fragment = new ConditionNewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_condition_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View viewx, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewx, savedInstanceState);
        view = viewx;
        loginApi();

    }
    public void loginApi(){
        Log.i("Login_response", "0 = " );
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ashwini.mehendale+01@yopmail.com");
        loginRequest.setPassword("ashwiniM@7");
        loginRequest.setPlatform(Constants.ANDROID_PLATFORM);
        Log.i("Login_response", "1 = " + new Gson().toJson(loginRequest));
//        mProgressDialogSetup = ProgressDialogSetup.getProgressDialog(getActivity());
//        mProgressDialogSetup.setCancelable(false);
//        if (mProgressDialogSetup != null)
//            mProgressDialogSetup.show();
        AppUtils.logEvent(Constants.LOGIN_API_CALLED);
        AppUtils.logCleverTapEvent(getActivity(), Constants.LOGIN_API_CALLED, null);
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
        String res = AppUtils.getVolleyError(getContext(), volleyError);
        AppUtils.showAlert(getContext(), res);
    }

    private void updateUIAsLoginSuccess(LoginResponse loginResponse, boolean isGoogleLogIn) {
        Log.i("Login_response", "2 = " + new Gson().toJson(loginResponse));
        ApplicationPreferences.get().saveStringValue(Constants.IS_NEW_USER, "true");
//        AppUtils.hideProgressBar(mProgressDialogSetup);
        ApplicationPreferences.get().setUserDetails(loginResponse);
        userDto = loginResponse.getUserDTO();

        if (isGoogleLogIn) {
            AppUtils.logCleverTapEvent(getActivity(), Constants.GOOGLE_USER_LOGGED_IN, null);
        } else {
            AppUtils.logCleverTapEvent(getActivity(), Constants.USER_LOGGED_IN, null);
        }
       /* if (userDTO.getTenantId() != 1 && !userDTO.isEmailConfirm()) {
            callUserVerifiedEmailAPI();
        } else if (userDTO.getTrialExpiryDate() == null) {
            AppUtils.showLandingPageAccordingToUserProfile(this);
        } else {
            AppUtils.showLandingPageAccordingToUserProfile(HomeActivity.this);
            AppUtils.callTimeZoneAPI(HomeActivity.this);
        }*/

        initializeIds(view);
        setupClickEvents();
        showConditionList();
        hideInterComChatBot();

    }

    private void hideInterComChatBot() {
     //   Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
    }

    private void setupClickEvents() {
        mButtonAddCondition.setOnClickListener(v -> openAddConditionActivity());

        menuItemAddCondition.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_CNDTN_BTN_CLK);
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.MY_CONDITION_SCREEN_ADD_CONDITION_FLOAT_BUTTON_CLICKED, null);
            openAddConditionActivity();
        });

        menuItemAddVitals.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_VITAL_BTN_CLK);
            AppUtils.logCleverTapEvent(getActivity(),
                    Constants.CONDITIONS_SCREEN_ADD_HEALTH_METRIC_FLOATING_BUTTON_CLICKED, null);
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
            AppUtils.logEvent(Constants.CNDTN_SCR_ADD_CNDTN_BTN_CLK);
                Intent intent = new Intent(getContext(), ManageVitalActivity.class);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
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
                    Constants.CLICKED_ON_ADD_NOW_BUTTON_ON_CONDITIONS_SCREEN, null);

            Intent intent = new Intent(getContext(), AddConditionsActivity.class);
            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
            startActivity(intent);
        }
    }

    private void initializeIds(View view) {
        if (getContext() != null) {
            progress = ProgressDialogSetup.getProgressDialog(getContext());
        }

        mEmptyScrollView = view.findViewById(R.id.emptyView);
        mParentLayout = view.findViewById(R.id.parentLayout);
        mButtonAddCondition = view.findViewById(R.id.button_add_condition);
        mRecyclerViewConditions = view.findViewById(R.id.recycler_view_conditions);

        floatingActionMenu = view.findViewById(R.id.menu);
        menuItemAddTest = view.findViewById(R.id.menu_item_add_test);
        menuItemAddVitals = view.findViewById(R.id.menu_item_add_vital);
        menuItemAddSymptoms = view.findViewById(R.id.menu_item_add_symptoms);
        menuItemAddVaccines = view.findViewById(R.id.menu_item_add_vaccines);
        menuItemAddCondition = view.findViewById(R.id.menu_item_add_condition);
    }

    private void setRecyclerViewAdapter(List<Disease> conditionList, DiseaseDto diseaseDto) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewConditions.setLayoutManager(linearLayoutManager);
        conditionsListAdapter = new ConditionsListAdapter(getContext(), conditionList, diseaseDto);
        mRecyclerViewConditions.setAdapter(conditionsListAdapter);
        conditionsListAdapter.setRvClickListener(this);
    }

    private void showConditionList() {
        try {
            String conditionListString = ApplicationPreferences.get().getStringValue(Constants.CONDITION_LIST);
            DiseaseDto diseaseDto = ApplicationDB.get().getDiseases(userDto.getId());
            if (conditionListString != null && !conditionListString.matches("")) {
                Gson conditionList = new Gson();
                Type typeWebinar = new TypeToken<DiseaseDto>() {
                }.getType();
                conditionListResponse = conditionList.fromJson(conditionListString, typeWebinar);
                if (conditionListResponse != null) {
                    if (conditionListResponse.getDiseases() != null &&
                            !conditionListResponse.getDiseases().isEmpty()) {
                        mEmptyScrollView.setVisibility(View.GONE);
                        Log.e("response_condition_log", " = " + conditionListString);
                        mRecyclerViewConditions.setVisibility(View.VISIBLE);
                        setRecyclerViewAdapter(conditionListResponse.getDiseases(), diseaseDto);
                    } else {
                        mEmptyScrollView.setVisibility(View.VISIBLE);
                        mRecyclerViewConditions.setVisibility(View.GONE);
                    }
                }
            } else {
                mEmptyScrollView.setVisibility(View.VISIBLE);
                mRecyclerViewConditions.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getListOfDiseaseParameterDTO() {
        String diseaseParameterDTO = ApplicationPreferences.get().getStringValue(Constants.DISEASE_PARAMETER_RESPONSE);
        if (diseaseParameterDTO != null && !diseaseParameterDTO.matches("")) {
            Gson conditionList = new Gson();
            Type typeWebinar = new TypeToken<DiseaseParameterResponseDTO>() {
            }.getType();
            diseaseParameterResponse = conditionList.fromJson(diseaseParameterDTO, typeWebinar);
        }
    }

    public void showDialogBoxForDeleteCondition(int position) {
        if (userDto != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            final View deletePopupView = getLayoutInflater().inflate(R.layout.layout_delete_condition, null);

            Button mButtonDelete = deletePopupView.findViewById(R.id.button_delete);
            TextView mTextViewTitle = deletePopupView.findViewById(R.id.textview_title);
            Button mButtonNoCancel = deletePopupView.findViewById(R.id.button_no_cancel);
            TextView mTextViewDeleteMessage = deletePopupView.findViewById(R.id.textview_delete_message);

            dialogBuilder.setView(deletePopupView);
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
            dialog.setCancelable(false);

            String msgStr = getString(R.string.are_you_sure_deleting_condition) + getString(R.string.space)
                    + conditionListResponse.getDiseases().get(position).getName() + "?";
            mTextViewTitle.setText(msgStr);

            String deleteMsg = "Deleting " + conditionListResponse.getDiseases().get(position).getName() + " " + getString(R.string.delete_condition_msg);
            mTextViewDeleteMessage.setText(deleteMsg);

            mButtonDelete.setOnClickListener(v -> {
                dialog.dismiss();
                callDeleteAPI(userDto, conditionListResponse.getDiseases().get(position).getUserDiseaseId());
            });

            mButtonNoCancel.setOnClickListener(v -> dialog.dismiss());
        }
    }

    private void callDeleteAPI(UserDto userDto, Long userDiseaseId) {
        if (getContext() != null) {

            if (progress != null) {
                progress.show();
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
            GenericRequest<APIMessageResponse> getDeleteRequest = new GenericRequest<>
                    (Request.Method.DELETE, APIUrls.get().getDeleteDisease(userDto.getId(), userDiseaseId),
                            APIMessageResponse.class, null,
                            apiMessageResponse -> {
                                AppUtils.hideProgressBar(progress);
                                ApplicationDB.get().deleteDisease(userDto.getId());
                                updateListItems();
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            authExpiredCallback.setRequest(getDeleteRequest);
            ApiService.get().addToRequestQueue(getDeleteRequest);
        }
    }

    private void updateListItems() {
        conditionListResponse.getDiseases().remove(removePosition);
        conditionsListAdapter.notifyDataSetChanged();

        if (conditionListResponse.getDiseases().isEmpty()) {
            mEmptyScrollView.setVisibility(View.VISIBLE);
            mRecyclerViewConditions.setVisibility(View.GONE);
        }
    }

    private Long getUserParameterID(String fromList) {
        if (fromList.equalsIgnoreCase(Constants.HEALTH)) {
            for (int i = 0; i < conditionListResponse.getVitals().size(); i++) {
                if (conditionListResponse.getVitals().get(i).getId().equals(Long.valueOf(ApplicationPreferences.get().getStringValue(Constants.PARAMETER_ID)))) {
                    userParameterID = conditionListResponse.getVitals().get(i).getUserParameterId();
                    break;
                }
            }
        } else if (fromList.equalsIgnoreCase(Constants.CHECKUP)) {
            for (int i = 0; i < conditionListResponse.getTests().size(); i++) {
                if (conditionListResponse.getTests().get(i).getId().equals(Long.valueOf(ApplicationPreferences.get().getStringValue(Constants.PARAMETER_ID)))) {
                    userParameterID = conditionListResponse.getTests().get(i).getUserParameterId();
                    break;
                }
            }
        }

        return userParameterID;
    }

    @Override
    public void rv_click(int position, int value, String key) {
        getListOfDiseaseParameterDTO();
        if (key.equals(Constants.DELETE)) {
            removePosition = position;
            showDialogBoxForDeleteCondition(position);
        } else if (key.equals(Constants.DELETE_HEALTH_MATRICS)) {
            toggleParameterAddOrRemove(userDto,
                    getUserParameterID(Constants.HEALTH));
        } else if (key.equals(Constants.ADD_HEALTH_MATRICS)) {
            addHealthMatrics(position);
        } else if (key.equals(Constants.ADD_CHECKUPS)) {
            addCheckups(position);
        } else if (key.equals(Constants.DELETE_CHECKUPS)) {
            toggleParameterAddOrRemove(userDto,
                    getUserParameterID(Constants.CHECKUP));
        } else if (key.equalsIgnoreCase(Constants.ADD_SYMPTOMS)) {
            addSymptomsAPI(position);
        } else if (key.equalsIgnoreCase(Constants.REMOVE_SYMPTOMS)) {
            removeSymptomAPI(position);
        }
    }

    private void addSymptomsAPI(int position) {
        symptomArrayList = new ArrayList<>();
        SymptomRequest.Symptom symptoms = new SymptomRequest.Symptom();
        symptoms.setSymptomId(diseaseParameterResponse.getSymptoms().get(position).getParameterId());

        symptomArrayList.add(symptoms);

        symptomRequest = new SymptomRequest();
        symptomRequest.setSymptoms(symptomArrayList);

        if (progress != null) {
            progress.show();
        }

        Log.e("request_obj_toggle", " = " + new Gson().toJson(symptomRequest));
        Log.e("url_toggle", " = " + APIUrls.get().addSymptomsAPI(userDto.getId()));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getActivity());
        GenericRequest<AddSymptomsResponseDTO.AddSymptomsListResponseDTO> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().addSymptomsAPI(userDto.getId()),
                        AddSymptomsResponseDTO.AddSymptomsListResponseDTO.class, symptomRequest,
                        response -> {
                            progress.dismiss();
                            Log.e("response_log", " = " + new Gson().toJson(response));
//                            AppUtils.openSnackBar(mParentLayout, "Symptom added");
                            callGetUserAddedSymptoms(userDto, AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));

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

    private void callGetUserAddedSymptoms(UserDto userDto, String longFormatDate) {
        if (progress != null) {
            progress.show();
        }

        Log.e("API_URL_GET_SYMPTOM", " = " + APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getActivity());
        GenericRequest<GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO> getSymptomRequest = new GenericRequest<>
                (Request.Method.GET, APIUrls.get().getUserSelectedSymptoms(userDto.getId(), longFormatDate),
                        GetUserAddedSymptomsResponseDTO.GetUserAddedSymptomsListResponseDTO.class, null,
                        symptomsResponseDTO -> {
                            AppUtils.hideProgressBar(progress);
                            getUserAddedSymptomsResponseDTO = symptomsResponseDTO;
                            Log.e("getSymptomsList_log", " = " + new Gson().toJson(getUserAddedSymptomsResponseDTO));
                            saveTOString(getUserAddedSymptomsResponseDTO);

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

    private void saveTOString(List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO) {
        if (getUserAddedSymptomsResponseDTO != null) {
            Gson gson = new Gson();
            String symptomList = gson.toJson(getUserAddedSymptomsResponseDTO);
            ApplicationPreferences.get().saveStringValue(Constants.SYMPTOMS_LIST, symptomList);
        }
    }

    private void removeSymptomAPI(int position) {
        try {
            int symptomPosition = 0;

            for(int i = 0; i < diseaseParameterResponse.getSymptoms().size(); i++) {
                for(int j = 0 ;  j < getUserAddedSymptomsResponseDTO.size(); j++) {
                    if(diseaseParameterResponse.getSymptoms().get(i).getParameterId().equals(getUserAddedSymptomsResponseDTO.get(j).getSymptomId())) {
                        symptomPosition = j;
                        break;
                    }
                }
            }

            if (progress != null) {
                progress.show();
            }

            StringRequest request = new StringRequest(Request.Method.PUT, APIUrls.get().getSymptomDelete(userDto.getId(), getUserAddedSymptomsResponseDTO.get(symptomPosition).getUserSymptomId()), response -> {
                if (!response.isEmpty()) {
                    Log.e("Your Array Response", response);
                    AppUtils.hideProgressBar(progress);
                    callGetUserAddedSymptoms(userDto, AppUtils.getBackendFormattedDateForSymptoms(AppUtils.getTodayDate()));
                }
            }, error -> {
                AppUtils.hideProgressBar(progress);
                Log.e("error is ", "" + error);
            }) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + ApplicationPreferences.get().getUserDetails().getAccessToken());
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCheckups(int position) {
        progress.show();
        AddParameterRequest addParameterRequest = new AddParameterRequest();
        addParameterRequest.setUiText("");
        addParameterRequest.setMeasurementUnit("");
        addParameterRequest.setId(diseaseParameterResponse.getTests().get(position).getParameterId());
        addParameterRequest.setName(diseaseParameterResponse.getTests().get(position).getParameterName());
        addParameterRequest.setFrequencyUnit(diseaseParameterResponse.getTests().get(position).getFrequencyUnit());
        addParameterRequest.setMedicalParameterType(diseaseParameterResponse.getTests().get(position).getMedicalParameterType());

        Log.e("addCheckupMatrics_log", " = " + new Gson().toJson(addParameterRequest));
        Log.e("checkUpList_log", " = " + new Gson().toJson(diseaseParameterResponse.getTests()));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(
                getContext());
        GenericRequest<APIMessageResponse> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getToggleAPIUrl(userDto.getId()),
                        APIMessageResponse.class, addParameterRequest,
                        response -> {
                            progress.dismiss();
                            callGetDiseaseAPI(userDto);
                        },
                        error -> {
                            progress.dismiss();
                            String reason = AppUtils.getVolleyError
                                    (getContext(), error);
                            AppUtils.openSnackBar(mParentLayout, reason);
                        });
        authExpiredCallback.setRequest(addQuestionRequest);
        ApiService.get().addToRequestQueue(addQuestionRequest);
    }

    private void addHealthMatrics(int position) {
        progress.show();
        AddParameterRequest addParameterRequest = new AddParameterRequest();
        addParameterRequest.setMedicalParameterType(diseaseParameterResponse.getVitals().get(position)
                .getMedicalParameterType());
        addParameterRequest.setName(diseaseParameterResponse.getVitals().get(position).getParameterName());
        addParameterRequest.setFrequencyUnit(diseaseParameterResponse.getVitals().get(position).getFrequencyUnit());
        addParameterRequest.setUiText("");
        addParameterRequest.setMeasurementUnit("");
        addParameterRequest.setId(diseaseParameterResponse.getVitals().get(position).getParameterId());

        Log.e("addHealthMatrics_log", " = " + new Gson().toJson(addParameterRequest));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(
                getContext());
        GenericRequest<APIMessageResponse> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getToggleAPIUrl(userDto.getId()),
                        APIMessageResponse.class, addParameterRequest,
                        response -> {
                            progress.dismiss();
                            callGetDiseaseAPI(userDto);
                        },
                        error -> {
                            progress.dismiss();
                            String reason = AppUtils.getVolleyError
                                    (getContext(), error);
                            AppUtils.openSnackBar(mParentLayout, reason);
                        });
        authExpiredCallback.setRequest(addQuestionRequest);
        ApiService.get().addToRequestQueue(addQuestionRequest);
    }

    private void toggleParameterAddOrRemove(UserDto userDto, Long userParameterId) {
        if (userParameterId != null && getContext() != null) {
            if (progress != null) {
                progress.show();
            }

            Log.e("API_URL", " = " + APIUrls.get().setToggleState(userDto.getId(), userParameterId));
            Log.e("API_URL", " = " + APIUrls.get().setToggleState(userDto.getId(), userParameterId));

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
            GenericRequest<APIMessageResponse> getDeleteRequest = new GenericRequest<>
                    (Request.Method.PUT, APIUrls.get().setToggleState(userDto.getId(), userParameterId),
                            APIMessageResponse.class, null,
                            apiMessageResponse -> {
                                AppUtils.hideProgressBar(progress);
                                AppUtils.openSnackBar(mParentLayout, "Parameter Updated Sucessfully");
                                callGetDiseaseAPI(userDto);
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                AppUtils.openSnackBar(mParentLayout, res);
                            });
            authExpiredCallback.setRequest(getDeleteRequest);
            ApiService.get().addToRequestQueue(getDeleteRequest);
        }
    }


    private void callGetDiseaseAPI(UserDto userDto) {
        if (getContext() != null && userDto != null) {
            progress.show();

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
            GenericRequest<DiseaseDto> getDiseaseRequest = new GenericRequest<>
                    (Request.Method.GET, APIUrls.get().getAllDisease(userDto.getId()),
                            DiseaseDto.class, null,
                            diseaseDto -> {
                                Log.e("response_getDisease", " = " + new Gson().toJson(diseaseDto));
                                AppUtils.hideProgressBar(progress);
                                ApplicationDB.get().upsertDisease(userDto.getId(), diseaseDto);
                                storeAndProceed(diseaseDto);
                                sendBroadcast(Constants.UPDATE_CONDITIONS_AND_TRACKERS_LISTING);
                                setRecyclerViewAdapter(diseaseDto.getDiseases(), diseaseDto);
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
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
        }
    }

    private void sendBroadcast(String message) {
        Intent intent = new Intent();
        intent.setAction(message);
        getContext().sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progress != null)
            progress.dismiss();
    }

}