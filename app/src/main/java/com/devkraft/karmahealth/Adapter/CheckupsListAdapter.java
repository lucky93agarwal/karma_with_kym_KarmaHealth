package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.devkraft.karmahealth.R;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Screen.CheckupDetailActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.google.gson.Gson;
import static com.devkraft.karmahealth.Screen.MyConditionsNewActivity.userDto;
import java.util.List;

public class CheckupsListAdapter extends RecyclerView.Adapter<CheckupsListAdapter.ViewHolder> {

    private Context mContext;
    private List<ParameterDto> mList;
    private RvClickListener rvClickListener;

    public CheckupsListAdapter(Context context, List<ParameterDto> checkupList) {
        this.mContext = context;
        this.mList = checkupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_checkup_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mList.get(position).getActive() != null) {
            if (mList.get(position).getActive()) {
                final ParameterDto item = mList.get(position);
                holder.mTextViewCheckupName.setText("" + mList.get(position).getName());
                holder.mTextViewCheckupDate.setText("" + AppUtils.getSimpleFormatDate(mList.get(position).getNextDueDate()));
                holder.itemView.setOnClickListener(v -> {
                    rvClickListener.rv_click(position, position, "Checkup");
                });

                holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(mContext, TestConfigureActivity.class);
                    Intent intent = new Intent(mContext, CheckupDetailActivity.class);
                    intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                    intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(item));
                    intent.putExtra(Constants.WHICH_TEST, item.getName());
                    intent.putExtra(Constants.FROM_WHERE, Constants.TEST);
                    mContext.startActivity(intent);
                });
            } else {
                holder.mLayoutMain.setVisibility(View.GONE);
            }
        } else {
            final ParameterDto item = mList.get(position);
            holder.mTextViewCheckupName.setText("" + mList.get(position).getName());
            holder.mTextViewCheckupDate.setText("" + AppUtils.getSimpleFormatDate(mList.get(position).getNextDueDate()));
            holder.itemView.setOnClickListener(v -> {
                rvClickListener.rv_click(position, position, "Checkup");
            });

            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(mContext, TestConfigureActivity.class);
                Intent intent = new Intent(mContext, CheckupDetailActivity.class);
                intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(item));
                intent.putExtra(Constants.WHICH_TEST, item.getName());
                intent.putExtra(Constants.FROM_WHERE, Constants.TEST);
                mContext.startActivity(intent);
            });
        }
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutMain;
        private TextView mTextViewCheckupName, mTextViewCheckupDate;

        public ViewHolder(View view) {
            super(view);

            mLayoutMain = view.findViewById(R.id.layout_header);
            mTextViewCheckupDate = view.findViewById(R.id.textview_date);
            mTextViewCheckupName = view.findViewById(R.id.textview_checkup_name);

        }
    }
}
