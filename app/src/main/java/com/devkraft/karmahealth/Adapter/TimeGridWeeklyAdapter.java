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

import com.devkraft.karmahealth.Model.WeeklyDays;
import com.devkraft.karmahealth.Screen.AddDrugActivity;
import com.devkraft.karmahealth.Utils.AppUtils;

import java.util.List;

public class TimeGridWeeklyAdapter extends RecyclerView.Adapter<TimeGridWeeklyAdapter.ViewHolder> {

    private final Context mContext;
    private final int limit;
    private final List<WeeklyDays> weeklyDaysList;
    private AddDrugActivity.WeekTimeCallback weekTimeCallback;

    public TimeGridWeeklyAdapter(Context context, List<WeeklyDays> weeklyDaysList, int limit, AddDrugActivity.WeekTimeCallback weekTimeCallback) {
        this.mContext = context;
        this.weeklyDaysList = weeklyDaysList;
        this.weekTimeCallback = weekTimeCallback;
        this.limit = limit;
    }

    @Override
    public TimeGridWeeklyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_week_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimeGridWeeklyAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(AppUtils.capitalize(weeklyDaysList.get(i).getDisplayFormat().toLowerCase()));

        final WeeklyDays time = weeklyDaysList.get(i);
        if(time.isSelected()){
            viewHolder.tv_android.setBackgroundResource(R.drawable.grid_border_selected);
            viewHolder.tv_android.setTextColor(ContextCompat.getColor(mContext,R.color.white));

            weekTimeCallback.timeDto(weeklyDaysList);
        }else {
            viewHolder.tv_android.setBackgroundResource(R.drawable.grid_border_normal);
            viewHolder.tv_android.setTextColor(ContextCompat.getColor(mContext,R.color.subText));
        }

        viewHolder.tv_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = getSelectedItemCount();

                if(weeklyDaysList.get(i).isSelected()){
                    if(count != 1){
                        weeklyDaysList.get(i).setSelected(!time.isSelected());
                    }
                }else {
                    if(count < limit){
                        weeklyDaysList.get(i).setSelected(!time.isSelected());
                    }else {
                        String errorMsg =  mContext.getString(R.string.you_can_not_add_more_than_dosage) +
                                mContext.getString(R.string.space) + limit +
                                mContext.getString(R.string.space) +   mContext.getString(R.string.days);
                        AppUtils.openSnackBar(viewHolder.parentLayout,errorMsg);
                    }
                }

                weekTimeCallback.timeDto(weeklyDaysList);
                notifyDataSetChanged();
            }
        });

    }

    private int getSelectedItemCount() {
        int count = 0;
        for(int j =0; j < weeklyDaysList.size(); j++){
            if(weeklyDaysList.get(j).isSelected()){
                count ++;
            }
        }
        return count;
    }


    @Override
    public int getItemCount() {
        return weeklyDaysList.size();
    }

    public List<WeeklyDays> getList() {
        return weeklyDaysList;
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
