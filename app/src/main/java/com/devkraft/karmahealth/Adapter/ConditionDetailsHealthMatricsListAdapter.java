package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.R;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.google.gson.Gson;

import java.util.List;

public class ConditionDetailsHealthMatricsListAdapter extends RecyclerView.Adapter<ConditionDetailsHealthMatricsListAdapter.ViewHolder> {

    private Context mContext;
    private Long paramerterId;
    private DiseaseDto mDiseaseDTO;
    private RvClickListener rvClickListener;
    private DiseaseDto conditionListResponse;
    private List<DiseaseParameterDTO> mHealthMatricsList;
    private boolean isWaterSwitchOnFitBit ;
    private boolean isWeightSwitchOnFitBit;

    public ConditionDetailsHealthMatricsListAdapter(Context context, List<DiseaseParameterDTO> healthMartricsList, DiseaseDto mDiseaseDTO) {
        this.mContext = context;
        this.mHealthMatricsList = healthMartricsList;
        this.mDiseaseDTO = mDiseaseDTO;
        Log.e("vitalsList_log"," = "+new Gson().toJson(healthMartricsList));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_conditions_health_matrics_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextViewPrameterName.setText("" + mHealthMatricsList.get(position).getParameterName());
        holder.mTextViewParameterDuration.setText("" + mHealthMatricsList.get(position).getParameterFrequency());

        for (int i = 0; i < mDiseaseDTO.getVitals().size(); i++) {
            if (mDiseaseDTO.getVitals().get(i).getName().equalsIgnoreCase(mHealthMatricsList.get(position).getParameterName())) {
                holder.mToggleButtonCondition.setChecked(true);
                break;
            } else {
                holder.mToggleButtonCondition.setChecked(false);
            }
        }

        holder.mToggleButtonCondition.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                paramerterId = mHealthMatricsList.get(position).getParameterId();
                ApplicationPreferences.get().saveStringValue(Constants.PARAMETER_ID, String.valueOf(paramerterId));
                rvClickListener.rv_click(position, position, Constants.ADD_HEALTH_MATRICS);
            } else {
                paramerterId = mHealthMatricsList.get(position).getParameterId();
                ApplicationPreferences.get().saveStringValue(Constants.PARAMETER_ID, String.valueOf(paramerterId));
                rvClickListener.rv_click(position, position, Constants.DELETE_HEALTH_MATRICS);
            }
        });
    }

    private void callAddParameterAPI() {

    }

    @Override
    public int getItemCount() {
        if (mHealthMatricsList != null)
            return mHealthMatricsList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ToggleButton mToggleButtonCondition;
        private TextView mTextViewPrameterName, mTextViewParameterDuration;

        public ViewHolder(View view) {
            super(view);

            mTextViewPrameterName = view.findViewById(R.id.textview_parameter_name);
            mTextViewParameterDuration = view.findViewById(R.id.textview_prameter_duration);
            mToggleButtonCondition = view.findViewById(R.id.toggle_condition_health_matrics);
        }
    }
}
