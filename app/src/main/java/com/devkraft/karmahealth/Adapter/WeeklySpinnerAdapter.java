package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.WeeklyDays;
import com.devkraft.karmahealth.R;
import java.util.List;

public class WeeklySpinnerAdapter extends ArrayAdapter<WeeklyDays> {

    LayoutInflater flater;

    public WeeklySpinnerAdapter(Context context, int resouceId, int textviewId, List<WeeklyDays> list){
        super(context,resouceId,textviewId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        WeeklyDays rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.text_view, null, false);

            holder.txtTitle = rowview.findViewById(R.id.header);

            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }

        holder.txtTitle.setText(rowItem.getDisplayFormat());

        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;
    }
}
