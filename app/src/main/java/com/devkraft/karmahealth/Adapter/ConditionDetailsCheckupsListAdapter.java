package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConditionDetailsCheckupsListAdapter extends RecyclerView.Adapter<ConditionDetailsCheckupsListAdapter.ViewHolder> {

    private Long paramerterId;
    private DiseaseDto mDiseaseDTO;
    private RvClickListener rvClickListener;
    private List<DiseaseParameterDTO> mCheckupList;

    public ConditionDetailsCheckupsListAdapter(List<DiseaseParameterDTO> checkupList, DiseaseDto mDiseaseDTO) {
        this.mCheckupList = checkupList;
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
        for (int i = 0; i < mDiseaseDTO.getTests().size(); i++) {
            if (mDiseaseDTO.getTests().get(i).getName().equalsIgnoreCase(mCheckupList.get(position).getParameterName())) {
                holder.mToggleButtonCheckups.setChecked(true);
                break;
            } else {
                holder.mToggleButtonCheckups.setChecked(false);
            }
        }

        // set the parameter name and frequency
        holder.mTextViewPrameterName.setText(mCheckupList.get(position).getParameterName());
        holder.mTextViewParameterDuration.setText(mCheckupList.get(position).getParameterFrequency());

        /**
         * handle the click event of toggle button is isChecked is true call add parameter
         * else handle delete parameter
         */
        holder.mToggleButtonCheckups.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                paramerterId = mCheckupList.get(position).getParameterId();
                ApplicationPreferences.get().saveStringValue(Constants.PARAMETER_ID,
                        String.valueOf(paramerterId));
                rvClickListener.rv_click(position, position, Constants.ADD_CHECKUPS);
            } else {
                paramerterId = mCheckupList.get(position).getParameterId();
                ApplicationPreferences.get().saveStringValue(Constants.PARAMETER_ID,
                        String.valueOf(paramerterId));
                rvClickListener.rv_click(position, position, Constants.DELETE_CHECKUPS);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCheckupList != null)
            return mCheckupList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewPrameterName;
        private ToggleButton mToggleButtonCheckups;
        private TextView mTextViewParameterDuration;

        public ViewHolder(View view) {
            super(view);

            mTextViewPrameterName = view.findViewById(R.id.textview_parameter_name);
            mTextViewParameterDuration = view.findViewById(R.id.textview_prameter_duration);
            mToggleButtonCheckups = view.findViewById(R.id.toggle_condition_health_matrics);

        }
    }
}
