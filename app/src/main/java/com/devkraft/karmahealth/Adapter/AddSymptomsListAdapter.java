package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Model.GetSymptomsResponseDTO;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;

import java.util.List;

public class AddSymptomsListAdapter extends RecyclerView.Adapter<AddSymptomsListAdapter.ViewHolder> {
    private Context mContext;
    private RvClickListener rvClickListener;
    private List<GetSymptomsResponseDTO> mList;
    private List<GetSymptomsResponseDTO> mFilteredList;
    private boolean isSelected = false;

    public AddSymptomsListAdapter(Context context, List<GetSymptomsResponseDTO> mSymptomsList) {
        this.mContext = context;
        this.mList = mSymptomsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_symptoms_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(!mList.isEmpty()) {
            holder.mTextViewSymptomName.setText(mList.get(position).getParameterName());
        }

        if(mList.get(position).getSelected() != null && mList.get(position).getSelected()) {
            holder.mImageViewCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewCheck.setVisibility(View.GONE);
        }

       /* holder.itemView.setOnClickListener(v -> {
            if(!isSelected) {
                isSelected = true;
                holder.mImageViewCheck.setVisibility(View.VISIBLE);
                rvClickListener.rv_click(position, position, Constants.ADD_SYMPTOMS);
            } else  {
                isSelected = false;
                holder.mImageViewCheck.setVisibility(View.GONE);
                rvClickListener.rv_click(position, position, Constants.REMOVE_SYMPTOMS);
            }
        });*/

        holder.itemView.setOnClickListener(v -> {

            if(mList.get(position).getSelected() != null && mList.get(position).getSelected()) {
                rvClickListener.rv_click(position,position, Constants.REMOVE_SYMPTOMS);
            } else {
                rvClickListener.rv_click(position,position,Constants.ADD_SYMPTOMS);
            }

            notifyDataSetChanged();
        });
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filteredList(List<GetSymptomsResponseDTO> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewCheck;
        private TextView mTextViewSymptomName;

        public ViewHolder(View view) {
            super(view);

            mImageViewCheck = view.findViewById(R.id.imageview_check);
            mTextViewSymptomName = view.findViewById(R.id.textview_symptom_name);
        }
    }
}
