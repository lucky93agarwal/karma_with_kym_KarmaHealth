package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.SymptomsDTO;
import com.devkraft.karmahealth.R;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.fragment.SymptomTrackersFragment;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.google.gson.Gson;

import java.util.List;

public class SymptomConditionDetailsListAdapter extends RecyclerView.Adapter<SymptomConditionDetailsListAdapter.ViewHolder> {

    private Long paramerterId;
    private DiseaseDto mDiseaseDTO;
    private RvClickListener rvClickListener;
    private List<SymptomsDTO> mSymptomList;
    private List<GetUserAddedSymptomsResponseDTO> getUserAddedSymptomsResponseDTO = SymptomTrackersFragment.getUserAddedSymptomsResponseDTO;

    public SymptomConditionDetailsListAdapter(List<SymptomsDTO> symptomList, DiseaseDto mDiseaseDTO) {
        this.mSymptomList = symptomList;
        this.mDiseaseDTO = mDiseaseDTO;
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_conditions_health_matrics_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //check if parameter present in user selected disease list then set toggle to true
        if(getUserAddedSymptomsResponseDTO != null) {
            Log.e("getUserAddedSymptomsResponseDTO_log", " = " + new Gson().toJson(getUserAddedSymptomsResponseDTO));
            Log.e("mSymptomListDTO_log", " = " + new Gson().toJson(mSymptomList));
            for (int i = 0; i < getUserAddedSymptomsResponseDTO.size(); i++) {
                if(getUserAddedSymptomsResponseDTO.get(i).getSymptomId() != null) {
                    if (getUserAddedSymptomsResponseDTO.get(i).getSymptomId().equals(mSymptomList.get(position).getParameterId())) {
                        holder.mToggleButtonSymptom.setChecked(true);
                        break;
                    } else {
                        holder.mToggleButtonSymptom.setChecked(false);
                    }
                }
            }
        }

        // set the parameter name and frequency
        holder.mTextViewPrameterName.setText(mSymptomList.get(position).getParameterName());


        //Set visibility of view
        if(position <  mSymptomList.size() -1) {
            holder.mViewConditionSymptom.setVisibility(View.VISIBLE);
        } else {
            holder.mViewConditionSymptom.setVisibility(View.GONE);
        }


        /**
         * handle the click event of toggle button is isChecked is true call add parameter
         * else handle delete parameter
         */
        holder.mToggleButtonSymptom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rvClickListener.rv_click(position, position, Constants.ADD_SYMPTOMS);
            } else {
                rvClickListener.rv_click(position, position, Constants.REMOVE_SYMPTOMS);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSymptomList != null)
            return mSymptomList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewPrameterName;
        private ToggleButton mToggleButtonSymptom;
        private TextView mTextViewParameterDuration;
        private View mViewConditionSymptom;

        public ViewHolder(View view) {
            super(view);

            mViewConditionSymptom = view.findViewById(R.id.view_condition_symptom);
            mTextViewPrameterName = view.findViewById(R.id.textview_parameter_name);
            mTextViewParameterDuration = view.findViewById(R.id.textview_prameter_duration);
            mToggleButtonSymptom = view.findViewById(R.id.toggle_condition_health_matrics);

        }
    }
}
