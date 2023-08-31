package com.devkraft.karmahealth.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Model.AvailableDoctorListModel;
import com.devkraft.karmahealth.Model.RetrofitDataPrescriptionsModel;
import com.devkraft.karmahealth.Model.RetrofitPatientModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.DrPrescriptionDownloadActivity;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorPrescriptionAdapter extends RecyclerView.Adapter<DoctorPrescriptionAdapter.ViewHolder> {
    public RetrofitDataPrescriptionsModel mModel;
    public static List<RetrofitDataPrescriptionsModel> mProductList;
    public static Context mcontext;

    public DoctorPrescriptionAdapter(List<RetrofitDataPrescriptionsModel> productList, Context context) {
        super();
        mcontext = context;
        mProductList = productList;
    }

    @NonNull
    @Override
    public DoctorPrescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recyclerview_dr_prescription_item, parent, false);
        DoctorPrescriptionAdapter.ViewHolder viewHolder = new DoctorPrescriptionAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorPrescriptionAdapter.ViewHolder holder, int position) {
        mModel = mProductList.get(position);
        holder.tvdrNametv.setText(mModel.karma_doctor.Doctor);
        holder.tvdepartmenttv.setText(mModel.diagnosis);
        holder.tvtimetv.setText(maink(mModel.ConsultationDate));

        holder.lldrperchill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, DrPrescriptionDownloadActivity.class);
                intent.putExtra("url", mProductList.get(position).URL);
                mcontext.startActivity(intent);
                /*((Activity)mcontext).finish();*/

            }
        });
    }

    public void filterList(ArrayList<RetrofitDataPrescriptionsModel> filteredList) {
        mProductList = filteredList;
        notifyDataSetChanged();
    }

    public String maink(String date) {/*
        String date = "2019-07-14T18:30:00.000Z";*/
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date parsedDate = inputFormat.parse(date);
            String formattedDate = outputFormat.format(parsedDate);
            System.out.println(parsedDate);
            System.out.println(formattedDate);
            return formattedDate;
        } catch (Exception e) {
            return "";
        }

    }

    public String parseDateToddMMyyyy(String time) {
        Log.i("checkmodeldatatime", "api time = " + time);
        String inputPattern = "yyyy-MM-dd'T'HH:mm.SSS'Z'";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        maink(time);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
            Log.i("checkmodeldatatime", "result time = " + str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("checkmodeldatatime", "error message = " + e.getMessage());
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvdrNametv;
        public TextView tvdepartmenttv;
        public TextView tvtimetv;
        public LinearLayout lldrperchill;

        public ViewHolder(View itemView) {
            super(itemView);
            this.lldrperchill = (LinearLayout) itemView.findViewById(R.id.drperchill);
            this.tvdrNametv = (TextView) itemView.findViewById(R.id.drNametv);
            this.tvdepartmenttv = (TextView) itemView.findViewById(R.id.departmenttv);
            this.tvtimetv = (TextView) itemView.findViewById(R.id.timetv);
        }
    }
}
