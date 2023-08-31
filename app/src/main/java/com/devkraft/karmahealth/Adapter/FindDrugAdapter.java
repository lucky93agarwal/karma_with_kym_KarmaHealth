package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.DrugListAPIResponse;
import com.devkraft.karmahealth.R;

import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Screen.HomeActivity;
import com.devkraft.karmahealth.Utils.AppUtils;

import java.util.List;

public class FindDrugAdapter extends ArrayAdapter<DrugListAPIResponse> {
    private List<DrugListAPIResponse> list;
    private Context context;
    private LayoutInflater inflater;
    private HomeActivity.SearchDrugCallback searchDrugCallback;

    public FindDrugAdapter(Context context, int resource, List<DrugListAPIResponse> list, HomeActivity.SearchDrugCallback searchDrugCallback) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.searchDrugCallback = searchDrugCallback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FindDomainViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_find_drug, null);
            holder = new FindDomainViewHolder();

            holder.drugName = convertView.findViewById(R.id.drugName);
            holder.mainLayout = convertView.findViewById(R.id.mainLayout);
            convertView.setTag(holder);
        } else {
            holder = (FindDomainViewHolder) convertView.getTag();
        }

        final DrugListAPIResponse item = getItem(position);
//        String drugName = item.getName();
        String drugName = item.medicineName;

        Log.e("displayName"," = "+item.medicineName);
        holder.drugName.setText(AppUtils.getEncodedString(drugName));

        if (context != null && drugName != null) {
            if (drugName.equalsIgnoreCase(context.getString(R.string.no_result_found))) {
                holder.drugName.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.drugName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
            } else {
                holder.drugName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.drugName.setTextColor(ContextCompat.getColor(context, R.color.gray));
            }
        }


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDrugCallback.callDrugDto(item);
            }
        });


        return convertView;
    }

    @Override
    public DrugListAPIResponse getItem(int position) {
        return list.get(position);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    public void updateList(List<DrugListAPIResponse> drugDtoList) {
        list.clear();
        list.addAll(drugDtoList);
        notifyDataSetChanged();
    }


    private class FindDomainViewHolder {
        public TextView drugName;
        public LinearLayout mainLayout;
    }
}
