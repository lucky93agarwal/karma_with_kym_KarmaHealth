package com.devkraft.karmahealth.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.UserRequestDisease;
import com.devkraft.karmahealth.R;
import androidx.fragment.app.DialogFragment;

import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.google.gson.Gson;

public class DialogFragmentDiseases extends DialogFragment {

    private EditText diseaseNameEt;
    private TextView submitTv, cancelTv, titleTv,firstLineTv,secondLineTv;
    private UserDto userDto;
    private ProgressDialogSetup progress;
    private boolean isAddDrug;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container, false);

        //AppUtils.logEvent(Constants.REQUEST_DRUG_POPUP_SHOWN);
        AppUtils.logEvent(Constants.CNDTN_ADD_DIS_SCR_REQ_DIS_POPUP_SHOWN);

        if(getContext() != null){
            progress = ProgressDialogSetup.getProgressDialog(getContext());
        }
        diseaseNameEt = view.findViewById(R.id.diseaseNameEt);
        submitTv = view.findViewById(R.id.submitTv);
        cancelTv = view.findViewById(R.id.cancelTv);
        titleTv = view.findViewById(R.id.title);
        firstLineTv = view.findViewById(R.id.firstLine);
        secondLineTv = view.findViewById(R.id.secondLine);


        final Bundle bundle =  getArguments();
        if (bundle != null) {
            String userDtoStr = bundle.getString(Constants.USER_DTO);
            if (userDtoStr != null) {
                Gson gson = new Gson();
                userDto = gson.fromJson(userDtoStr, UserDto.class);
            }

            isAddDrug = bundle.getBoolean(Constants.ADD_DRUG);

            if(isAddDrug){
                titleTv.setText(getString(R.string.cant_find_drug));
                firstLineTv.setText(getString(R.string.cant_find_drug_line));
                secondLineTv.setText(getString(R.string.cant_find_drug_line1));
                diseaseNameEt.setHint(getString(R.string.drug_name));
            }else {
                titleTv.setText(getString(R.string.cant_find_disease));
                firstLineTv.setText(getString(R.string.cant_find_disease_line));
                secondLineTv.setText(getString(R.string.cant_find_disease_line1));
                diseaseNameEt.setHint(getString(R.string.condition_name));
            }
        }


        submitTv.setOnClickListener(view1 -> {
            AppUtils.logEvent(Constants.CNDN_ADD_DIS_SCR_REQ_DIS_POP_SBT_BTN_CLK);
            String diseaseName = diseaseNameEt.getText().toString().trim();
            if (diseaseName.length() > 0) {
                if (AppUtils.isValidString(diseaseName)) {
                    UserRequestDisease userRequestDisease = new UserRequestDisease();
                    userRequestDisease.setName(diseaseName);
                    callAddUserDiseaseAPI(userRequestDisease);
                }else {
                    if(isAddDrug){
                        Toast.makeText(getContext(), getString(R.string.please_enter_valid_drug), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), getString(R.string.please_enter_valid_condition), Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                if(isAddDrug){
                    Toast.makeText(getContext(), getString(R.string.please_enter_valid_drug), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), getString(R.string.please_enter_valid_condition), Toast.LENGTH_LONG).show();
                }

            }
        });

        cancelTv.setOnClickListener(view12 -> {
            AppUtils.logEvent(Constants.CNDN_ADD_DIS_SCR_REQ_DIS_POP_CNL_BTN_CLK);
            dismiss();
        });

        return view;
    }

    private void showAlert(String message) {

        if(getContext() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setMessage(message)
                    .setTitle("");

            builder.setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                dialog.dismiss();
                dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private void callAddUserDiseaseAPI(UserRequestDisease userRequestDisease) {
        if(getContext() != null){

            if(progress != null){
                progress.show();
            }

            String url;
            if(isAddDrug){
                url = APIUrls.get().getRequestedDrug(userDto.getId());
            }else {
                url = APIUrls.get().getRequestedDisease(userDto.getId());
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
            GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                    (Request.Method.POST, url,
                            APIMessageResponse.class, userRequestDisease,
                            dosageDropDownDtoList -> {
                                AppUtils.hideProgressBar(progress);
                                if (isAddDrug) {
                                    showAlert(getString(R.string.request_drug_success));
                                } else {
                                    AppUtils.logEvent(Constants.CNDTN_ADD_DISEASE_FORM_SUBMITTED);
                                    showAlert(getString(R.string.request_disease_success));
                                }
                            },
                            error -> {
                                AppUtils.hideProgressBar(progress);
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                showAlert(res);
                            });
            authExpiredCallback.setRequest(getDiseasRequest);
            ApiService.get().addToRequestQueue(getDiseasRequest);
        }

    }


}

