package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.DiseasesDropDownDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AddDiseaseActivity;
import com.devkraft.karmahealth.Utils.AppUtils;

import androidx.core.content.ContextCompat;

import java.util.List;

public class FindDiseaseAdapter extends ArrayAdapter<DiseasesDropDownDto> {

    private List<DiseasesDropDownDto> list;
    private Context context;
    private LayoutInflater inflater;
    private AddDiseaseActivity.searchDieasaseCallback searchDieasaseCallback;

    public FindDiseaseAdapter(Context context, int resource, List<DiseasesDropDownDto> list, AddDiseaseActivity.searchDieasaseCallback searchDieasaseCallback) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.searchDieasaseCallback = searchDieasaseCallback;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FindDiseaseAdapter.FindDomainViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_find_drug, null);
            holder = new FindDiseaseAdapter.FindDomainViewHolder();

            holder.diseaseName = convertView.findViewById(R.id.drugName);
            holder.mainLayout = convertView.findViewById(R.id.mainLayout);
            convertView.setTag(holder);
        } else {
            holder = (FindDiseaseAdapter.FindDomainViewHolder) convertView.getTag();
        }

        final DiseasesDropDownDto  item = getItem(position);
        String drugName = item.getName();
        holder.diseaseName.setText(AppUtils.getEncodedString(drugName));

        if (context != null) {
            if (drugName.equalsIgnoreCase(context.getString(R.string.no_result_found))) {
                holder.diseaseName.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.diseaseName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
            } else {
                holder.diseaseName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.diseaseName.setTextColor(ContextCompat.getColor(context, R.color.gray));
            }
        }


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDieasaseCallback.callDieasaseDto(item);
            }
        });


        return convertView;
    }

    @Override
    public DiseasesDropDownDto getItem(int position) {
        return list.get(position);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    public void updateList(List<DiseasesDropDownDto> drugDtoList) {
        list.clear();
        list.addAll(drugDtoList);
        notifyDataSetChanged();
    }


    private class FindDomainViewHolder {
        public TextView diseaseName;
        public LinearLayout mainLayout;
    }

}

