package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.inter.RvClickListener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddHealthMatricsListAdapter extends RecyclerView.Adapter<AddHealthMatricsListAdapter.ViewHolder> {

    private Context mContext;
    private List<DiseaseParameterDTO> mList;
    private RvClickListener rvClickListener;

    public AddHealthMatricsListAdapter(Context context, List<DiseaseParameterDTO> healthList) {
        this.mContext = context;
        this.mList = healthList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_healthmatrics_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mToggleButtonHealthMatrics.setChecked(true);
        holder.mTextViewHealthMatricsName.setText(""+mList.get(position).getParameterName());
        holder.mTextViewHealthMatricsDuration.setText(""+mList.get(position).getParameterFrequency());


        holder.mToggleButtonHealthMatrics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                rvClickListener.rv_click(position,position,"healthParameterAdd");
            } else {
                rvClickListener.rv_click(position,position,"healthParameterDelete");
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

        private ToggleButton mToggleButtonHealthMatrics;
        private TextView mTextViewHealthMatricsName, mTextViewHealthMatricsDuration;

        public ViewHolder(View view) {
            super(view);

            mToggleButtonHealthMatrics = view.findViewById(R.id.toggle_health_matrics);
            mTextViewHealthMatricsName = view.findViewById(R.id.textview_heath_matrics_name);
            mTextViewHealthMatricsDuration = view.findViewById(R.id.textview_health_matrics_duration);
        }
    }
}
