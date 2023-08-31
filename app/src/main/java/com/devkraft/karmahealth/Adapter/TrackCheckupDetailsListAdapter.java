package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrackCheckupDetailsListAdapter extends RecyclerView.Adapter<TrackCheckupDetailsListAdapter.ViewHolder> {

    private Context mContext;
    private List<TrackConfigurationDto> mList;
    private RvClickListener rvClickListener;

    public TrackCheckupDetailsListAdapter(Context context, List<TrackConfigurationDto> checkupList) {
        this.mContext = context;
        this.mList = checkupList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_track_checkup_details_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mTextViewHeader.setText("" + mList.get(position).getNotes());

        if (mList.get(position).getMaxBaselineValue() > 0)
            holder.mTextViewValue.setText("Value : " + mList.get(position).getMaxBaselineValue());
        else
            holder.mTextViewValue.setVisibility(View.GONE);

        holder.mTextViewDate.setText("" + AppUtils.getDateInReportFormatted(mList.get(position).getRecordedDate()));

        holder.mImageViewEdit.setOnClickListener(v -> {
            rvClickListener.rv_click(position, position, Constants.EDIT_TRACK_CHECK_UP);
        });

        holder.mImageViewDelete.setOnClickListener(v -> {
            rvClickListener.rv_click(position, position, Constants.DELETE_CHECKUP_TRACK);
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

        private RelativeLayout mLayoutMain;
        private ImageView mImageViewEdit, mImageViewDelete;
        private TextView mTextViewHeader, mTextViewValue, mTextViewDate;

        public ViewHolder(View view) {
            super(view);

            mLayoutMain = view.findViewById(R.id.layout_header);
            mTextViewDate = view.findViewById(R.id.textview_date);
            mTextViewValue = view.findViewById(R.id.textview_value);
            mTextViewHeader = view.findViewById(R.id.textview_header);

            mImageViewEdit = view.findViewById(R.id.imageview_edit);
            mImageViewDelete = view.findViewById(R.id.imageview_delete);

        }
    }
}
