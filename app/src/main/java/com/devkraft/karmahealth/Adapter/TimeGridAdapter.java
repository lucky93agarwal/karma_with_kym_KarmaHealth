package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.Time;
import com.devkraft.karmahealth.R;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Screen.AddDrugActivity;
import com.devkraft.karmahealth.Utils.AppUtils;

import java.util.List;

public class TimeGridAdapter extends RecyclerView.Adapter<TimeGridAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Time> timeList;
    private final int limit;
    private AddDrugActivity.TimeCallback timeCallback;

    public TimeGridAdapter(Context context, List<Time> timeList, int limit, AddDrugActivity.TimeCallback timeCallback) {
        this.mContext = context;
        this.timeList = timeList;
        this.limit = limit;
        this.timeCallback = timeCallback;
    }

    @Override
    public TimeGridAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimeGridAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(timeList.get(i).getDisplayFormat().toLowerCase());

        final Time time = timeList.get(i);
        if(time.isSelected()){
            viewHolder.tv_android.setBackgroundResource(R.drawable.grid_border_selected);
            viewHolder.tv_android.setTextColor(ContextCompat.getColor(mContext,R.color.white));

            timeCallback.timeDto(timeList);
        }else {
            viewHolder.tv_android.setBackgroundResource(R.drawable.grid_border_normal);
            viewHolder.tv_android.setTextColor(ContextCompat.getColor(mContext,R.color.subText));
        }

        viewHolder.tv_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = getSelectedItemCount();

                if(limit == AppUtils.DAILY_LIMIT){
                    if(timeList.get(i).isSelected()){
                        if(count != 1){
                            timeList.get(i).setSelected(!time.isSelected());
                        }
                    }else {
                        if(count < limit){
                            timeList.get(i).setSelected(!time.isSelected());
                        }else {
                            String errorMsg =  mContext.getString(R.string.you_can_not_add_more_than_dosage)
                                    + mContext.getString(R.string.space) + limit +
                                    mContext.getString(R.string.space) +   mContext.getString(R.string.dosages);
                            AppUtils.openSnackBar(viewHolder.parentLayout,errorMsg);
                        }
                    }
                }else {
                    // weekly and monthly
                    updateList(i);
                }

                timeCallback.timeDto(timeList);
                notifyDataSetChanged();
            }
        });

    }

    private void updateList(int pos) {
        for(int j =0; j < timeList.size(); j++){
            if(j == pos){
                timeList.get(j).setSelected(true);
            }else {
                timeList.get(j).setSelected(false);
            }
        }
    }

    private int getSelectedItemCount() {
        int count = 0;
        for(int j =0; j < timeList.size(); j++){
            if(timeList.get(j).isSelected()){
                count ++;
            }
        }
        return count;
    }


    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public List<Time> getList() {
        return timeList;
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
