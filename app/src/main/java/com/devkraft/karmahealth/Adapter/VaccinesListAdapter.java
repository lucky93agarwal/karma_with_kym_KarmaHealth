package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.R;
import androidx.recyclerview.widget.RecyclerView;
import static com.devkraft.karmahealth.Screen.MyConditionsNewActivity.userDto;

import com.devkraft.karmahealth.Screen.TestConfigureActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.google.gson.Gson;

import java.util.List;

public class VaccinesListAdapter extends RecyclerView.Adapter<VaccinesListAdapter.ViewHolder> {

    private Context mContext;
    private List<ParameterDto> mList;
    private RvClickListener rvClickListener;

    public VaccinesListAdapter(Context context, List<ParameterDto> vaccineList) {
        this.mContext = context;
        this.mList = vaccineList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vaccines_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ParameterDto item = mList.get(position);
        holder.mTextViewVaccineName.setText(""+mList.get(position).getName());
        Log.e("vaccines_date_log"," = "+mList.get(position).getNextDueDate());
        holder.mTextViewVaccineDate.setText(""+ AppUtils.getSimpleFormatDate(mList.get(position).getNextDueDate()));

        holder.itemView.setOnClickListener(v -> {
            ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");

            Intent intent = new Intent(mContext, TestConfigureActivity.class);
            intent.putExtra(Constants.USER_DTO,new Gson().toJson(userDto));
            intent.putExtra(Constants.PARAMETER_DTO,new Gson().toJson(item));
            intent.putExtra(Constants.WHICH_TEST,item.getName());
            intent.putExtra(Constants.FROM_WHERE,Constants.VACCINES);
            mContext.startActivity(intent);
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

        private TextView mTextViewVaccineName, mTextViewVaccineDate;

        public ViewHolder(View view) {
            super(view);

            mTextViewVaccineName = view.findViewById(R.id.textview_vaccine_name);
            mTextViewVaccineDate = view.findViewById(R.id.textview_vaccine_date);
        }
    }
}
