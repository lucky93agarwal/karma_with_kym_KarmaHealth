package com.devkraft.karmahealth.Adapter;

import static com.devkraft.karmahealth.Screen.MyTrackActivity.userDto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Model.MyTrackModel;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.Values;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.TrackDetailsActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class MyTrackAdapter extends RecyclerView.Adapter<MyTrackAdapter.ViewHolder> {
    public ParameterDto mModel;
    public static List<ParameterDto> mProductList;
    public static Context mcontext;

    public MyTrackAdapter(List<ParameterDto> productList, Context context) {
        super();
        mcontext = context;
        mProductList = productList;
    }
    @NonNull
    @Override
    public MyTrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recyclerview_my_track_layout, parent, false);
        MyTrackAdapter.ViewHolder viewHolder = new MyTrackAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrackAdapter.ViewHolder holder, int position) {
        mModel = mProductList.get(position);
       /*holder.iviconiv.setImageResource(Integer.parseInt(mModel.icon));
        holder.tvdrNametv.setText(mModel.getName());
        holder.tvratingtv.setText(mModel.rate);
        holder.tvratingnametv.setText(mModel.rateName);
        holder.tvtimetv.setText(mModel.time);
        holder.mdrperchill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mcontext, TrackDetailsActivity.class);
                intent.putExtra("title", mProductList.get(position).title);
                mcontext.startActivity(intent);
            }
        });*/


        if (mModel.getActive() != null) {
            if (mModel.getActive()) {
                holder.mLayoutMain.setVisibility(View.VISIBLE);
                holder.mTextViewTrackerName.setText("" + mModel.getName());
                holder.mTextViewTrackerUnit.setText("" + mModel.getMeasurementUnit());

                if (mModel.getName().equalsIgnoreCase("Respiratory Rate") ||
                        mModel.getName().equalsIgnoreCase("Blood Glucose") ||
                        mModel.getName().equalsIgnoreCase("Resting Heart Rate")) {
                    holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_rate_green_icon);
                } else {
                    holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_heart_green_icon);
                }

                List<Values> valuesList = mModel.getValues();

                if (valuesList != null && valuesList.size() > 0) {
                    holder.mTextViewTrackerValue.setVisibility(View.VISIBLE);
                    holder.mTextViewTrackerUnit.setVisibility(View.VISIBLE);

                    if (valuesList.size() == 1) {
                        holder.mTextViewTrackerValue.setText("" + valuesList.get(0).getValue());
                        holder.mTextViewTrackerUnit.setText("" + valuesList.get(0).getUnit());
                    } else {
                        for (int i = 0; i <= valuesList.size(); i++) {
                            //for blood pressure only
                            holder.mTextViewTrackerValue.setText("" + valuesList.get(1).getValue() + "/" + valuesList.get(0).getValue());
                            holder.mTextViewTrackerUnit.setText("" + valuesList.get(0).getUnit());
                        }
                    }
                } else {
                    holder.mTextViewTrackerUnit.setVisibility(View.GONE);
                    holder.mTextViewTrackerValue.setVisibility(View.VISIBLE);

                    holder.mTextViewTrackerValue.setText(mcontext.getString(R.string.empty_msg_disease));
                }

                if (mModel.getLastRecordedDate() != null) {
                    holder.mTextViewTrackerDate.setText("" + AppUtils.getDateOnlyFromDateTime(mModel.getLastRecordedDate()));
                }

                holder.mLayoutMain.setOnClickListener(v -> {
                    if (mcontext != null) {

                        HashMap<String, Object> healthMatricsMap = new HashMap<>();
                        healthMatricsMap.put(Constants.HEALTH_MATRICS_NAME, mModel.getName());

                        AppUtils.logCleverTapEvent(mcontext,
                                Constants.CLICKED_ON_HEALTH_METRICS_ON_CONDITION_SCREEN, healthMatricsMap);
                        ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");

                        Intent intent = new Intent(mcontext, TrackDetailsActivity.class);
                        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                        intent.putExtra("title",mProductList.get(position).getName());
                        intent.putExtra(Constants.WHICH_DISEASE, mModel.getName());
                        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(mModel));
                        intent.putExtra(Constants.USER_PARA_TRACKING_ID, mModel.getUserParameterId());
                        intent.putExtra(Constants.POSITION, position);
                        mcontext.startActivity(intent);
                    }
                });
            } else {
                holder.mLayoutMain.setVisibility(View.GONE);
            }
        } else {
            holder.mLayoutMain.setVisibility(View.VISIBLE);
            holder.mTextViewTrackerName.setText("" + mModel.getName());
            holder.mTextViewTrackerUnit.setText("" + mModel.getMeasurementUnit());

            if (mModel.getName().equalsIgnoreCase("Respiratory Rate") ||
                    mModel.getName().equalsIgnoreCase("Blood Glucose") ||
                    mModel.getName().equalsIgnoreCase("Resting Heart Rate")) {
                holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_rate_green_icon);
            } else {
                holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_heart_green_icon);
            }

            List<Values> valuesList = mModel.getValues();

            if (valuesList != null && valuesList.size() > 0) {
                holder.mTextViewTrackerValue.setVisibility(View.VISIBLE);
                holder.mTextViewTrackerUnit.setVisibility(View.VISIBLE);

                if (valuesList.size() == 1) {
                    holder.mTextViewTrackerValue.setText("" + valuesList.get(0).getValue());
                    holder.mTextViewTrackerUnit.setText("" + valuesList.get(0).getUnit());
                } else {
                    for (int i = 0; i <= valuesList.size(); i++) {
                        //for blood pressure only
                        holder.mTextViewTrackerValue.setText("" + valuesList.get(1).getValue() + "/" + valuesList.get(0).getValue());
                        holder.mTextViewTrackerUnit.setText("" + valuesList.get(0).getUnit());
                    }
                }
            } else {
                holder.mTextViewTrackerUnit.setVisibility(View.GONE);
                holder.mTextViewTrackerValue.setVisibility(View.VISIBLE);

                holder.mTextViewTrackerValue.setText(mcontext.getString(R.string.empty_msg_disease));
            }

            if (mModel.getLastRecordedDate() != null) {
                holder.mTextViewTrackerDate.setText("" + AppUtils.getDateOnlyFromDateTime(mModel.getLastRecordedDate()));
            }

            holder.mLayoutMain.setOnClickListener(v -> {
                if (mcontext != null) {
                    ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");

                    Intent intent = new Intent(mcontext, TrackDetailsActivity.class);
                    intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                    intent.putExtra("title",mProductList.get(position).getName());
                    intent.putExtra(Constants.WHICH_DISEASE, mModel.getName());
                    intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(mModel));
                    intent.putExtra(Constants.USER_PARA_TRACKING_ID, mModel.getUserParameterId());
                    intent.putExtra(Constants.POSITION, position);
                    mcontext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewTrackerName;
        public TextView mTextViewTrackerValue;
        public TextView mTextViewTrackerUnit;
        public TextView mTextViewTrackerDate;
        public ImageView mImageViewIcon;
        public LinearLayout mLayoutMain;

        public ViewHolder(View itemView) {
            super(itemView);
            mLayoutMain = (LinearLayout)itemView.findViewById(R.id.drperchill);
            mTextViewTrackerUnit = (TextView)itemView.findViewById(R.id.ratingnametv);
            this.mImageViewIcon = (ImageView)itemView.findViewById(R.id.iconiv);
            this.mTextViewTrackerName = (TextView) itemView.findViewById(R.id.drNametv);
            this.mTextViewTrackerValue = (TextView) itemView.findViewById(R.id.ratingtv);
            this.mTextViewTrackerDate = (TextView) itemView.findViewById(R.id.timetv);
        }
    }
}

