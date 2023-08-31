package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSymptomsActivity extends AppCompatActivity  implements RvClickListener {

    private RelativeLayout mLayoutAddSymptoms;
    private RecyclerView mRecyclerViewAddSymptoms;
    private AutoCompleteTextView mAutoCompleteSearch;
    private List<GetSymptomsResponseDTO> filteredList;

    private UserDto userDto;
    private ProgressDialogSetup progress;
    private RelativeLayout mParentLayout;
    public SymptomRequest symptomRequest;
    private List<String> symptomNames;
    private AddSymptomsListAdapter adapter;
    private TextView mTextViewCustomSymptomName;
    private RelativeLayout mAddCustomSymptomView;
    private LinearLayout mLayoutSymptomsRecyclerview;
    private List<GetSymptomsResponseDTO> getSymptomsResponseDTO;
    public ArrayList<SymptomRequest.Symptom> symptomArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptoms);

        setupToolbar();
        getIntentValue(getIntent());
        initializeIds();
        handleClickEvents();
        callGetSymptomsAPI();

        AppUtils.logCleverTapEvent(this, Constants.ADD_SYMPTOM_SCREEN_OPENED, null);
    }

    private void getIntentValue(Intent intent) {
        if (intent != null) {
            Gson gson = new Gson();
            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);

            if (userDtoStr != null) {
                userDto = gson.fromJson(userDtoStr, UserDto.class);
            }
        }
    }

    private void callGetSymptomsAPI() {
        if (progress != null) {
            progress.show();
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(this);
        GenericRequest<GetSymptomsResponseDTO.GetSymptomsListResponse> getDiseaseRequest = new GenericRequest<>
                (Request.Method.GET, APIUrls.get().getSymptomsList(userDto.getId()),
                        GetSymptomsResponseDTO.GetSymptomsListResponse.class, null,
                        symptomsResponseDTO -> {
                            AppUtils.hideProgressBar(progress);
                            getSymptomsResponseDTO = symptomsResponseDTO;
                            Log.e("getSymptomsResponseDTO_log", " = " + new Gson().toJson(getSymptomsResponseDTO));
                            filteredList = getSymptomsResponseDTO;
                            Log.e("symptomList_size", " = " + getSymptomsResponseDTO.size());
                            setAddSymptomsListAdapter(getSymptomsResponseDTO);
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

    private void setAddSymptomsListAdapter(List<GetSymptomsResponseDTO> mSymptomsListAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewAddSymptoms.setLayoutManager(linearLayoutManager);
        adapter = new AddSymptomsListAdapter(this, mSymptomsListAdapter);
        mRecyclerViewAddSymptoms.setAdapter(adapter);
        adapter.setRvClickListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.add_symptoms));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grayish_brown));
    }

    private void initializeIds() {
        symptomNames = new ArrayList<>();
        mParentLayout = findViewById(R.id.parentLayout);
        progress = ProgressDialogSetup.getProgressDialog(this);

        mLayoutAddSymptoms = findViewById(R.id.layout_add_symptoms);
        mAutoCompleteSearch = findViewById(R.id.autocomplete_search_text);
        mAddCustomSymptomView = findViewById(R.id.layout_add_custom_symptom);
        mRecyclerViewAddSymptoms = findViewById(R.id.recyclerview_add_symptoms);
        mTextViewCustomSymptomName = findViewById(R.id.textview_custom_symptom);
        mLayoutSymptomsRecyclerview = findViewById(R.id.layout_add_symptom_recyclerview);
    }

    private void handleClickEvents() {
        mLayoutAddSymptoms.setOnClickListener(v -> {
            callAddSymptomsAPI(userDto);
        });

        mAutoCompleteSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
//                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAddCustomSymptomView.setOnClickListener(v -> showDialogBoxForAddingSymptomType(mAutoCompleteSearch.getText().toString()));

        mTextViewCustomSymptomName.setOnClickListener(v -> {
            showDialogBoxForAddingSymptomType(mAutoCompleteSearch.getText().toString());
        });

    }

    private void showDialogBoxForAddingSymptomType(String symptomName) {
        if (userDto != null && symptomName != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            final View symptomsPopupView = getLayoutInflater().inflate(R.layout.layout_dialog_custom_symptom_type, null);

            Button mButtonDone = symptomsPopupView.findViewById(R.id.button_done);
            RadioGroup radioGroup = (RadioGroup) symptomsPopupView.findViewById(R.id.redio_group_symptom_type);

            dialogBuilder.setView(symptomsPopupView);
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
            dialog.setCancelable(true);

            mButtonDone.setOnClickListener(v -> {
                dialog.dismiss();
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButtonSymptomType = (RadioButton) radioGroup.findViewById(selectedId);

                if (radioButtonSymptomType != null) {
                    mAddCustomSymptomList(symptomName, radioButtonSymptomType.getText().toString());
                }

            });
        }
    }

    private void mAddCustomSymptomList(String symptomName, String measurementType) {
        AddCustomSymptomRequest.CustomSymptom customSymptomRequest = new AddCustomSymptomRequest.CustomSymptom();
        customSymptomRequest.setName(symptomName);
        customSymptomRequest.setMeasurementType(measurementType.toUpperCase());

        ArrayList<AddCustomSymptomRequest.CustomSymptom> customSymptomArrayList = new ArrayList<>();
        customSymptomArrayList.add(customSymptomRequest);

        AddCustomSymptomRequest addCustomSymptomRequest = new AddCustomSymptomRequest();
        addCustomSymptomRequest.setSymptoms(customSymptomArrayList);

        HashMap<String, Object> customMap = new HashMap<>();
        customMap.put("Name", symptomName);
        customMap.put("Severity", measurementType);

        callAddCustomSymptomAPI(addCustomSymptomRequest, userDto, customMap);
    }

    private void callAddCustomSymptomAPI(AddCustomSymptomRequest addCustomSymptomRequest, UserDto userDto, HashMap<String, Object> customMap) {
        if (progress != null) {
            progress.show();
        }

        Log.e("AddCustomrequest_obj", " = " + new Gson().toJson(addCustomSymptomRequest));
        Log.e("url", " = " + APIUrls.get().addSymptomsAPI(userDto.getId()));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(
                this);
        GenericRequest<AddSymptomsResponseDTO.AddSymptomsListResponseDTO> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().addSymptomsAPI(userDto.getId()),
                        AddSymptomsResponseDTO.AddSymptomsListResponseDTO.class, addCustomSymptomRequest,
                        response -> {
                            progress.dismiss();

                            AppUtils.logCleverTapEvent(this, Constants.ADD_SYMPTOM_FORM_SUBMITTED, customMap);
                            Log.e("CustomResponse_log", " = " + new Gson().toJson(response));
                            Intent intent = new Intent(AddSymptomsActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            startActivity(intent);
                            AppUtils.isDataChanged = true;
                            finish();
                        },
                        error -> {
                            progress.dismiss();
                            String reason = AppUtils.getVolleyError
                                    (AddSymptomsActivity.this, error);
                            AppUtils.openSnackBar(mParentLayout, reason);
                        });
        authExpiredCallback.setRequest(addQuestionRequest);
        ApiService.get().addToRequestQueue(addQuestionRequest);
    }

    private void filter(String searchText) {
        if (searchText != null) {
            filteredList = new ArrayList<>();
            if (getSymptomsResponseDTO != null) {
                for (GetSymptomsResponseDTO item : getSymptomsResponseDTO) {
                    if (item.getParameterName().toLowerCase().contains(searchText.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }

            if (filteredList.isEmpty()) {
                mAddCustomSymptomView.setVisibility(View.VISIBLE);
                mLayoutSymptomsRecyclerview.setVisibility(View.GONE);
                mTextViewCustomSymptomName.setText("Create " + searchText);
            } else {
                mAddCustomSymptomView.setVisibility(View.GONE);
                mLayoutSymptomsRecyclerview.setVisibility(View.VISIBLE);
            }

            if (filteredList != null)
                adapter.filteredList(filteredList);
        }
    }

    private void callAddSymptomsAPI(UserDto userDto) {
        if (progress != null) {
            progress.show();
        }

        Log.e("request_obj", " = " + new Gson().toJson(symptomRequest));
        Log.e("url", " = " + APIUrls.get().addSymptomsAPI(userDto.getId()));

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(
                this);
        GenericRequest<AddSymptomsResponseDTO.AddSymptomsListResponseDTO> addQuestionRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().addSymptomsAPI(userDto.getId()),
                        AddSymptomsResponseDTO.AddSymptomsListResponseDTO.class, symptomRequest,
                        response -> {
                            progress.dismiss();
                            HashMap<String, Object> symptomNamesMap = new HashMap<>();
                            String allSymptoms = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                allSymptoms = String.join(",", symptomNames);
                            } else {
                                allSymptoms = TextUtils.join(",", symptomNames);
                            }
                            symptomNamesMap.put("Symptom Name", allSymptoms);
                            AppUtils.logCleverTapEvent(this, Constants.ADD_SYMPTOM_FORM_SUBMITTED, symptomNamesMap);

                            Log.e("response_log", " = " + new Gson().toJson(response));
                            Intent intent = new Intent(AddSymptomsActivity.this, HomeActivity.class);
                            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                            intent.putExtra(Constants.IS_FROM_SYMPTOM, true);
                            startActivity(intent);
                            AppUtils.isDataChanged = true;
                            symptomArrayList = new ArrayList<>();
                            symptomNames = new ArrayList<>();
                            finish();
                        },
                        error -> {
                            progress.dismiss();
                            String reason = AppUtils.getVolleyError
                                    (AddSymptomsActivity.this, error);
                            AppUtils.openSnackBar(mParentLayout, reason);
                        });
        authExpiredCallback.setRequest(addQuestionRequest);
        ApiService.get().addToRequestQueue(addQuestionRequest);

    }

    private void addSymptoms(int position) {
        SymptomRequest.Symptom symptoms = new SymptomRequest.Symptom();
        symptoms.setSymptomId(filteredList.get(position).getParameterId());

        symptomArrayList.add(symptoms);
        symptomNames.add(filteredList.get(position).getParameterName());

        filteredList.get(position).setSelected(true);

        HashMap<String, Object> symptomMap = new HashMap<>();
        symptomMap.put("Symptom Name", filteredList.get(position).getParameterName());
        AppUtils.logCleverTapEvent(this, Constants.SYMPTOM_SEARCHED_ON_ADD_SYMPTOM_SCREEN, symptomMap);

        symptomRequest = new SymptomRequest();
        symptomRequest.setSymptoms(symptomArrayList);
    }

    @Override
    public void rv_click(int position, int value, String key) {
        if (key.equalsIgnoreCase(Constants.ADD_SYMPTOMS)) {
            addSymptoms(position);
        } else if (key.equalsIgnoreCase(Constants.REMOVE_SYMPTOMS)) {
            removeSymptoms(position);
        }
    }

    private void removeSymptoms(int position) {
        if (!symptomArrayList.isEmpty()) {
            for (int i = 0; i < symptomArrayList.size(); i++) {
                if (symptomArrayList.get(i).getSymptomId().equals(filteredList.get(position).getParameterId()) && symptomNames.get(i).equalsIgnoreCase(filteredList.get(position).getParameterName())) {
                    symptomArrayList.remove(i);
                    symptomNames.remove(i);
                    break;
                }
            }

            filteredList.get(position).setSelected(false);

            symptomRequest = new SymptomRequest();
            symptomRequest.setSymptoms(symptomArrayList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progress != null) {
            progress.dismiss();
        }
    }
}