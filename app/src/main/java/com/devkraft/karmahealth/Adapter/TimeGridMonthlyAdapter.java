package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.devkraft.karmahealth.R;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Screen.AddDrugActivity;
import com.devkraft.karmahealth.Utils.AppUtils;

import java.util.List;

public class TimeGridMonthlyAdapter extends RecyclerView.Adapter<TimeGridMonthlyAdapter.ViewHolder> {

    private final Context mContext;
    private final List<MonthlyDays> monthlyDaysList;
    private final int limit;
    private AddDrugActivity.MonthlyTimeCallback monthlyTimeCallback;

    public TimeGridMonthlyAdapter(Context context, List<MonthlyDays> monthlyDaysList, int limit, AddDrugActivity.MonthlyTimeCallback monthlyTimeCallback) {
        this.mContext = context;
        this.monthlyDaysList = monthlyDaysList;
        this.limit = limit;
        this.monthlyTimeCallback = monthlyTimeCallback;
    }

    @Override
    public TimeGridMonthlyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_week_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimeGridMonthlyAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(monthlyDaysList.get(i).getDay() + "");

        final MonthlyDays time = monthlyDaysList.get(i);
        if(time.isSelected()){
            viewHolder.tv_android.setBackgroundResource(R.drawable.grid_border_selected);
            viewHolder.tv_android.setTextColor(ContextCompat.getColor(mContext,R.color.white));

            monthlyTimeCallback.timeDto(monthlyDaysList);
        }else {
            viewHolder.tv_android.setBackgroundResource(R.drawable.grid_border_normal);
            viewHolder.tv_android.setTextColor(ContextCompat.getColor(mContext,R.color.subText));
        }

        viewHolder.tv_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = getSelectedItemCount();

                if(monthlyDaysList.get(i).isSelected()){
                    if(count != 1){
                        monthlyDaysList.get(i).setSelected(!time.isSelected());
                    }
                }else {
                    if(count < limit){
                        monthlyDaysList.get(i).setSelected(!time.isSelected());
                    }else {
                        String errorMsg =  mContext.getString(R.string.you_can_not_add_more_than_dosage) +
                                mContext.getString(R.string.space) + limit +
                                mContext.getString(R.string.space) +   mContext.getString(R.string.days);
                        AppUtils.openSnackBar(viewHolder.parentLayout,errorMsg);
                    }
                }

                monthlyTimeCallback.timeDto(monthlyDaysList);
                notifyDataSetChanged();
            }
        });

    }

    private int getSelectedItemCount() {
        int count = 0;
        for(int j =0; j < monthlyDaysList.size(); j++){
            if(monthlyDaysList.get(j).isSelected()){
                count ++;
            }
        }
        return count;
    }


    @Override
    public int getItemCount() {
        return monthlyDaysList.size();
    }

    public List<MonthlyDays> getList() {
        return monthlyDaysList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private LinearLayout parentLayout;
        public ViewHolder(View view) {
            super(view);
            parentLayout = view.findViewById(R.id.parentLayout);
            tv_android = view.findViewById(R.id.timeTv);
        }
    }
}
