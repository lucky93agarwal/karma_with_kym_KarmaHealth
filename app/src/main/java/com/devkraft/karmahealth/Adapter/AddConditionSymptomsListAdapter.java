package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Model.SymptomsDTO;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;

import java.util.List;

public class AddConditionSymptomsListAdapter extends RecyclerView.Adapter<AddConditionSymptomsListAdapter.ViewHolder> {

    private Context mContext;
    private List<SymptomsDTO> mList;
    private RvClickListener rvClickListener;

    public AddConditionSymptomsListAdapter(Context context, List<SymptomsDTO> checkupList) {
        this.mContext = context;
        this.mList = checkupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_symptoms, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(mList != null) {
            holder.mTextViewSymptomName.setText(mList.get(position).getParameterName());
            holder.mToggleButtonSymptoms.setChecked(true);
        }

        holder.mToggleButtonSymptoms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                rvClickListener.rv_click(position,position, Constants.ADD_SYMPTOMS);
            } else {
                rvClickListener.rv_click(position,position,Constants.REMOVE_SYMPTOMS);
            }
        });
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewSymptomName;
        private ToggleButton mToggleButtonSymptoms;

        public ViewHolder(View view) {
            super(view);

            mToggleButtonSymptoms = view.findViewById(R.id.toggle_symptoms);
            mTextViewSymptomName = view.findViewById(R.id.textview_symptom_name);
        }
    }
}
