package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devkraft.karmahealth.Model.AvailableDoctorListModel;
import com.devkraft.karmahealth.Model.RetrofitDrAvailabilityModel;
import com.devkraft.karmahealth.Model.RetrofitPatientModel;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AvailableDoctorActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class AvailableDoctorAdapter extends RecyclerView.Adapter<AvailableDoctorAdapter.ViewHolder> {
    public RetrofitPatientModel mModel;
    public static List<RetrofitPatientModel> mProductList;
    public static Context mcontext;

    public AvailableDoctorAdapter(List<RetrofitPatientModel> productList, Context context) {
        super();
        mcontext = context;
        mProductList = productList;
    }
    @NonNull
    @Override
    public AvailableDoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recyclerview_availabledoctor_item, parent, false);
        AvailableDoctorAdapter.ViewHolder viewHolder = new AvailableDoctorAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableDoctorAdapter.ViewHolder holder, int position) {
        mModel = mProductList.get(position);
        holder.tvdrNametv.setText(mModel.dname);
        holder.tvdepartmenttv.setText(mModel.Speciality);

        Glide.with(mcontext)
                .load(mModel.DoctorImage)
                .centerCrop()
                .skipMemoryCache(true)
                .placeholder(R.drawable.dr_round_circle)
                .into(holder.ivimg);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);




        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                stimg(mProductList.get(position).Timings,"Sunday",holder);
                break;
            case Calendar.MONDAY:
                stimg(mProductList.get(position).Timings,"Monday",holder);
                break;
            case Calendar.TUESDAY:
                stimg(mProductList.get(position).Timings,"Tuesday",holder);


                break;
            case Calendar.WEDNESDAY:

                stimg(mProductList.get(position).Timings,"Wednesday",holder);
                break;
            case Calendar.THURSDAY:

                stimg(mProductList.get(position).Timings,"Thursday",holder);
                break;
            case Calendar.FRIDAY:
                stimg(mProductList.get(position).Timings,"Friday",holder);

                break;
            case Calendar.SATURDAY:
                stimg(mProductList.get(position).Timings,"Saturday",holder);

                break;
        }

    }

    private void stimg(Object timeori,String day,@NonNull AvailableDoctorAdapter.ViewHolder holder)
    {
        try {


            JSONObject j = new JSONObject(new Gson().toJson(timeori));

            if(j.has(day)){
                JSONArray arrJson =  j.getJSONArray(day);
                String[] arr = new String[arrJson.length()];
                String time="";
                for(int i =0;i<arrJson.length();i++){
                    String timenew = arrJson.getString(i);
                    if(i== arrJson.length()-1){
                        String[] sepa = timenew.split("-");
                        String time1 = timecon(sepa[0]);
                        String time2 = timecon(sepa[1]);
                        time = time + time1+"-"+time2;
                    }else {
                        String[] sepa = timenew.split("-");
                        String time1 = timecon(sepa[0]);
                        String time2 = timecon(sepa[1]);
                        time = time + time1+"-"+time2+"\n";
                    }

                }

                holder.tvtimetv.setText(time);
            }else{
                holder.tvtimetv.setText("dr not available today");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
            holder.tvtimetv.setText(e.getMessage());
        }
    }
    private String timecon(String s){
        if(s.length()>0)
        {

           /* StringTokenizer tk = new StringTokenizer(s);
            String date = tk.nextToken();
            String time = tk.nextToken();*/

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            Date dt;
            try {
                dt = sdf.parse(s);
                return sdfs.format(dt);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvdrNametv;
        public TextView tvdepartmenttv;
        public TextView tvtimetv;
        public ImageView ivimg;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivimg= (ImageView)itemView.findViewById(R.id.imgiv);
            this.tvdrNametv = (TextView) itemView.findViewById(R.id.drNametv);
            this.tvdepartmenttv = (TextView) itemView.findViewById(R.id.departmenttv);
            this.tvtimetv = (TextView) itemView.findViewById(R.id.timetv);
        }
    }



    public void filterList(ArrayList<RetrofitPatientModel> filteredList) {
        mProductList = filteredList;
        notifyDataSetChanged();
    }
}
