package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.devkraft.karmahealth.R;
import androidx.core.content.ContextCompat;

import com.devkraft.karmahealth.Model.ParameterSearchResultDto;
import com.devkraft.karmahealth.Screen.ManageVitalActivity;
import com.devkraft.karmahealth.Utils.AppUtils;

import java.util.List;

public class FindParameterAdapter extends ArrayAdapter<ParameterSearchResultDto> {

    private List<ParameterSearchResultDto> list;
    private Context context;
    private LayoutInflater inflater;
    private ManageVitalActivity.searchParameterCallback searchDieasaseCallback;

    public FindParameterAdapter(Context context, int resource, List<ParameterSearchResultDto> list, ManageVitalActivity.searchParameterCallback searchDieasaseCallback) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.searchDieasaseCallback = searchDieasaseCallback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FindParameterAdapter.FindDomainViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_find_drug, null);
            holder = new FindParameterAdapter.FindDomainViewHolder();

            holder.parameterName = convertView.findViewById(R.id.drugName);
            holder.mainLayout = convertView.findViewById(R.id.mainLayout);
            convertView.setTag(holder);
        } else {
            holder = (FindParameterAdapter.FindDomainViewHolder) convertView.getTag();
        }

        final ParameterSearchResultDto item = getItem(position);
        String drugName = item.getName();
        holder.parameterName.setText(AppUtils.getEncodedString(drugName));

        if (context != null) {
            if (drugName.equalsIgnoreCase(context.getString(R.string.no_result_found))) {
                holder.parameterName.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.parameterName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
            } else {
                holder.parameterName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.parameterName.setTextColor(ContextCompat.getColor(context, R.color.gray));
            }
        }


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDieasaseCallback.callParameterDto(item);
            }
        });


        return convertView;
    }

    @Override
    public ParameterSearchResultDto getItem(int position) {
        return list.get(position);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    public void updateList(List<ParameterSearchResultDto> drugDtoList) {
        list.clear();
        list.addAll(drugDtoList);
        notifyDataSetChanged();
    }


    private class FindDomainViewHolder {
        public TextView parameterName;
        public LinearLayout mainLayout;
    }

}

