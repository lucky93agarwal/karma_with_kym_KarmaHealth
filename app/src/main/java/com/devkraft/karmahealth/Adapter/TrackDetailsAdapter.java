package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Model.TrackDetailsModel;
import com.devkraft.karmahealth.R;

import java.util.List;

public class TrackDetailsAdapter extends RecyclerView.Adapter<TrackDetailsAdapter.ViewHolder> {
    public TrackDetailsModel mModel;
    public static List<TrackDetailsModel> mProductList;
    public static Context mcontext;
    public TrackDetailsAdapter(List<TrackDetailsModel> productList, Context context) {
        super();
        mcontext = context;
        mProductList = productList;
    }

    @NonNull
    @Override
    public TrackDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recyclerview_track_details, parent, false);
        TrackDetailsAdapter.ViewHolder viewHolder = new TrackDetailsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackDetailsAdapter.ViewHolder holder, int position) {

//        mModel = mProductList.get(position);
//
//        holder.tvMedicenOneTv.setText(mModel.medicenOne);
//        holder.tvMedicenTwoTv.setText(mModel.medicenTwo);
//
//        holder.tvRateNameOnetv.setText(mModel.rateNameOne);
//        holder.tvRateNameTwotv.setText(mModel.rateNameTwo);
//
//
//        holder.tvRateOnetv.setText(mModel.rateOne);
//        holder.tvRateTwotv.setText(mModel.rateTwo);
//
//
//        holder.tvTime.setText(mModel.time);
//
//        holder.iveditiv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        holder.mdeleteiv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMedicenOneTv;
        public TextView tvMedicenTwoTv;

        public TextView tvRateNameOnetv;
        public TextView tvRateNameTwotv;

        public TextView tvRateOnetv;
        public TextView tvRateTwotv;


        public TextView tvTime;
        public ImageView iveditiv;
        public ImageView mdeleteiv;

        public ViewHolder(View itemView) {
            super(itemView);

//            this.tvMedicenOneTv = (TextView) itemView.findViewById(R.id.medicenonetv);
//            this.tvMedicenTwoTv = (TextView) itemView.findViewById(R.id.medicentwotv);
//
//            this.tvRateTwotv = (TextView) itemView.findViewById(R.id.ratetwotv);
//            this.tvRateNameTwotv = (TextView) itemView.findViewById(R.id.ratenametwotv);
//
//
//            this.tvRateOnetv = (TextView) itemView.findViewById(R.id.rateonetv);
//            this.tvRateNameOnetv = (TextView) itemView.findViewById(R.id.ratenameonetv);
//
//
//            this.tvTime = (TextView) itemView.findViewById(R.id.timetv);
//            this.iveditiv = (ImageView) itemView.findViewById(R.id.editiv);
//            this.mdeleteiv = (ImageView) itemView.findViewById(R.id.deleteiv);
        }
    }
}
