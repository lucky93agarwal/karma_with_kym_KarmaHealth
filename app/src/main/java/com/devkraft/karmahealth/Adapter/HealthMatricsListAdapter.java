package com.devkraft.karmahealth.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.Values;
import com.devkraft.karmahealth.R;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Screen.ConfigureActivity;
import com.devkraft.karmahealth.Screen.ConfigureWeightActivity;
import com.devkraft.karmahealth.Screen.HomeActivity;
import com.devkraft.karmahealth.Screen.InfoActivity;
import com.devkraft.karmahealth.Screen.LoginActivity;
import com.devkraft.karmahealth.Screen.MyConditionsNewActivity;
import com.devkraft.karmahealth.Screen.TrackActivity;
import com.devkraft.karmahealth.Screen.TrackerDetailActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.devkraft.karmahealth.Screen.MyConditionsNewActivity.userDto;

import retrofit2.Call;
import retrofit2.Callback;

public class HealthMatricsListAdapter extends RecyclerView.Adapter<HealthMatricsListAdapter.ViewHolder> {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private Context mContext;
    private List<ParameterDto> mList;
    private RvClickListener rvClickListener;

    public HealthMatricsListAdapter(Context context, List<ParameterDto> healthList) {
        this.mContext = context;
        this.mList = healthList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_health_matrics_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //TODO handle the fitbit on off functionality here

        if (mList.get(position).getActive() != null) {
            if (mList.get(position).getActive()) {
                holder.mLayoutMain.setVisibility(View.VISIBLE);
                holder.mTextViewTrackerName.setText("" + mList.get(position).getName());
                holder.mTextViewTrackerUnit.setText("" + mList.get(position).getMeasurementUnit());

                /*if (mList.get(position).getName().equalsIgnoreCase("Blood Pressure")) {
                    holder.mImageViewIcon.setBackgroundResource(R.drawable.blood_pressure_icon);
                } else  */if (mList.get(position).getName().equalsIgnoreCase("Respiratory Rate") ||
                        mList.get(position).getName().equalsIgnoreCase("Blood Glucose") ||
                        mList.get(position).getName().equalsIgnoreCase("Resting Heart Rate")) {
                    holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_rate_green_icon);
                } else {
                    holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_heart_green_icon);
                }

                List<Values> valuesList = mList.get(position).getValues();

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

                    holder.mTextViewTrackerValue.setText(mContext.getString(R.string.empty_msg_disease));
                }

                if (mList.get(position).getLastRecordedDate() != null) {
                    holder.mTextViewTrackerDate.setText("" + AppUtils.getDateOnlyFromDateTime(mList.get(position).getLastRecordedDate()));
                }
                if(mList.get(position).isDelete()){
                    holder.tvButtonViewOption.setVisibility(View.VISIBLE);
                }else {
                    holder.tvButtonViewOption.setVisibility(View.GONE);
                }
                holder.tvButtonViewOption.setOnClickListener(v -> {
                    if (mContext != null) {

                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(mContext, holder.tvButtonViewOption);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.vitals_option);
                        Menu menu = popup.getMenu();
                        menu.findItem(R.id.menu1).setVisible(false);
                        menu.findItem(R.id.menu3).setVisible(false);
                        if(mList.get(position).isDelete()){
                            menu.findItem(R.id.menu2).setVisible(true);
                        }else {
                            menu.findItem(R.id.menu2).setVisible(false);
                        }

                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu1:
                                        HashMap<String, Object> healthMap = new HashMap<>();
                                        healthMap.put(Constants.HEALTH_MATRICS_NAME, mList.get(position).getName());
                                        AppUtils.logCleverTapEvent(mContext,
                                                Constants.CLICKED_ON_TRACK_BUTTON_ON_HEALTH_METRICS_REPORT_SCREEN, healthMap);
                                        Intent intent = new Intent(mContext, TrackActivity.class);
                                        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                        intent.putExtra(Constants.PARAMETER_DTO,new Gson().toJson(mList.get(position)));
                                        intent.putExtra(Constants.WHICH_DISEASE,  mList.get(position).getName());
                                        intent.putExtra(Constants.IS_VITAL_EDIT, false);
                                        intent.putExtra(Constants.USER_PARA_TRACKING_ID, mList.get(position).getUserParameterId());
                                        intent.putExtra(Constants.IS_FROM_REPORT, false);
                                        mContext.startActivity(intent);
                                        return true;
                                    case R.id.menu2:
//handle menu2 click
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                                        builder.setTitle("क्या आप अवश्य हटाना चाहते हैं?");
                                        builder.setMessage("यह क्रिया आपकी प्रोफ़ाइल से "+mList.get(position).getName()+" को हटा देगी।");
                                        builder.setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                apiUpdateParameterAPI(mList.get(position).getId());
                                            }
                                        });
                                        builder.setNegativeButton("नहीं", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        builder.show();

                                        return true;
                                    case R.id.menu3:

                                        if (mList.get(position).getName().equalsIgnoreCase(Constants.Weight)) {
                                            Intent intents = new Intent(mContext, ConfigureWeightActivity.class);
                                            intents.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                            intents.putExtra(Constants.WHICH_DISEASE, mList.get(position).getName());
                                            intents.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(mList.get(position)));
                                            mContext.startActivity(intents);
                                        } else {
                                            Intent intentb = new Intent(mContext, ConfigureActivity.class);
                                            intentb.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                            intentb.putExtra(Constants.WHICH_DISEASE, mList.get(position).getName());
                                            intentb.putExtra(Constants.PARAMETER_DTO,new Gson().toJson(mList.get(position)));
                                            mContext.startActivity(intentb);
                                        }
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();

//                        HashMap<String, Object> healthMatricsMap = new HashMap<>();
//                        healthMatricsMap.put(Constants.HEALTH_MATRICS_NAME, mList.get(position).getName());
//
//                        AppUtils.logCleverTapEvent(mContext,
//                                Constants.CLICKED_ON_HEALTH_METRICS_ON_CONDITION_SCREEN, healthMatricsMap);
//                        ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");
//
//                        Intent intent = new Intent(mContext, TrackerDetailActivity.class);
//                        intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
//                        intent.putExtra(Constants.WHICH_DISEASE, mList.get(position).getName());
//                        intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(mList.get(position)));
//                        intent.putExtra(Constants.USER_PARA_TRACKING_ID, mList.get(position).getUserParameterId());
//                        intent.putExtra(Constants.POSITION, position);
//                        intent.putExtra("delete",mList.get(position).isDelete());
//                        Log.i("checkmodrulistg","adapter = "+String.valueOf(mList.get(position).isDelete()));
//                        mContext.startActivity(intent);
                    }
                });
            } else {
                holder.mLayoutMain.setVisibility(View.GONE);
            }
        } else {
            holder.mLayoutMain.setVisibility(View.VISIBLE);
            holder.mTextViewTrackerName.setText("" + mList.get(position).getName());
            holder.mTextViewTrackerUnit.setText("" + mList.get(position).getMeasurementUnit());

            /*if (mList.get(position).getName().equalsIgnoreCase("Blood Pressure")) {
                holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_rate_green_icon);
            } else */if (mList.get(position).getName().equalsIgnoreCase("Respiratory Rate") ||
                    mList.get(position).getName().equalsIgnoreCase("Blood Glucose") ||
                    mList.get(position).getName().equalsIgnoreCase("Resting Heart Rate")) {
                holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_rate_green_icon);
            } else {
                holder.mImageViewIcon.setBackgroundResource(R.drawable.ic_heart_green_icon);
            }

            List<Values> valuesList = mList.get(position).getValues();

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

                holder.mTextViewTrackerValue.setText(mContext.getString(R.string.empty_msg_disease));
            }

            if (mList.get(position).getLastRecordedDate() != null) {
                holder.mTextViewTrackerDate.setText("" + AppUtils.getDateOnlyFromDateTime(mList.get(position).getLastRecordedDate()));
            }
            if(mList.get(position).isDelete()){
                holder.tvButtonViewOption.setVisibility(View.VISIBLE);
            }else {
                holder.tvButtonViewOption.setVisibility(View.GONE);
            }

            holder.tvButtonViewOption.setOnClickListener(v -> {
                if (mContext != null) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, holder.tvButtonViewOption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.vitals_option);
                    Menu menu = popup.getMenu();
                    if(mList.get(position).isDelete()){
                        menu.findItem(R.id.menu2).setVisible(true);
                    }else {
                        menu.findItem(R.id.menu2).setVisible(false);
                    }
                    menu.findItem(R.id.menu1).setVisible(false);
                    menu.findItem(R.id.menu3).setVisible(false);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    HashMap<String, Object> healthMap = new HashMap<>();
                                    healthMap.put(Constants.HEALTH_MATRICS_NAME, mList.get(position).getName());
                                    AppUtils.logCleverTapEvent(mContext,
                                            Constants.CLICKED_ON_TRACK_BUTTON_ON_HEALTH_METRICS_REPORT_SCREEN, healthMap);
                                    Intent intent = new Intent(mContext, TrackActivity.class);
                                    intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                    intent.putExtra(Constants.PARAMETER_DTO,new Gson().toJson(mList.get(position)));
                                    intent.putExtra(Constants.WHICH_DISEASE,  mList.get(position).getName());
                                    intent.putExtra(Constants.IS_VITAL_EDIT, false);
                                    intent.putExtra(Constants.USER_PARA_TRACKING_ID, mList.get(position).getUserParameterId());
                                    intent.putExtra(Constants.IS_FROM_REPORT, false);
                                    mContext.startActivity(intent);
                                    return true;
                                case R.id.menu2:


                                    return true;
                                case R.id.menu3:

                                    if (mList.get(position).getName().equalsIgnoreCase(Constants.Weight)) {
                                        Intent intentm = new Intent(mContext, ConfigureWeightActivity.class);
                                        intentm.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                        intentm.putExtra(Constants.WHICH_DISEASE, mList.get(position).getName());
                                        intentm.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(mList.get(position)));
                                        mContext.startActivity(intentm);
                                    } else {
                                        Intent intentp = new Intent(mContext, ConfigureActivity.class);
                                        intentp.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
                                        intentp.putExtra(Constants.WHICH_DISEASE, mList.get(position).getName());
                                        intentp.putExtra(Constants.PARAMETER_DTO,new Gson().toJson(mList.get(position)));
                                        mContext.startActivity(intentp);
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
//                    ApplicationPreferences.get().saveStringValue(Constants.VIEWPAGER_POSITION, "1");
//
//                    Intent intent = new Intent(mContext, TrackerDetailActivity.class);
//                    intent.putExtra(Constants.USER_DTO, new Gson().toJson(userDto));
//                    intent.putExtra(Constants.WHICH_DISEASE, mList.get(position).getName());
//                    intent.putExtra(Constants.PARAMETER_DTO, new Gson().toJson(mList.get(position)));
//                    intent.putExtra(Constants.USER_PARA_TRACKING_ID, mList.get(position).getUserParameterId());
//                    intent.putExtra(Constants.POSITION, position);
//                    intent.putExtra("delete",mList.get(position).isDelete());
//                    Log.i("checkmodrulistg","adapter = "+String.valueOf(mList.get(position).isDelete()));
//                    mContext.startActivity(intent);
                }
            });
        }
    }
    private void apiUpdateParameterAPI(Long item){
        android.util.Log.i("checkmodrug", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));

        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.deleteParameterAPI(sharedPreferences.getString("kymPid", "134388"),String.valueOf(item),token).enqueue(new Callback<APIMessageResponse>() {
            @Override
            public void onResponse(Call<APIMessageResponse> call, retrofit2.Response<APIMessageResponse> response) {
                android.util.Log.i("checkmodrug", "api login response 0 code = " + response.code());
                android.util.Log.i("checkmodrug", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                   // AppUtils.hideProgressBar(progress);


                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.putExtra(Constants.IS_TEST_VACCINES_UPDATE, true);
                    intent.putExtra(Constants.FROM_WHERE, Constants.TRACKER_DETAILS);
                    mContext.startActivity(intent);

                    ((MyConditionsNewActivity)mContext).finish();
                }else if(response.code() == 401){
                    refreshToken();
                }  else {
                    Toast.makeText(mContext, "Please try after some time.", Toast.LENGTH_SHORT).show();
                   // AppUtils.openSnackBar(mParentView, "Please try after some time.");
                    android.util.Log.i("checkmodrug", "api response 1 code = " + response.code());
                  //  AppUtils.hideProgressBar(progress);
                }
            }

            @Override
            public void onFailure(Call<APIMessageResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
               // AppUtils.openSnackBar(mParentView, t.getMessage());
                Log.i("checkmodrug", "api error message response  = " + t.getMessage());
               // AppUtils.hideProgressBar(progress);
            }
        });
    }
    public void refreshToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(sharedPreferences.getString("refreshToken",""));
        Log.i("refreshToken", "refreshToken api request 278 = " + new Gson().toJson(request));

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, retrofit2.Response<RefreshTokenResponse> response) {
                Log.i("refreshToken", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("refreshToken", "prescription api response = " + new Gson().toJson(response.body()));

                    edit.putString("Ptoken", response.body().accessToken);
                    edit.apply();
                 //   AppUtils.openSnackBar(mParentView, "Try Again");
                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    ((MyConditionsNewActivity)mContext).finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });

    }

    private String checkForDateDifference(String fromDate, String toDate) {
        Date from = getDateFromString(fromDate);
        Date to = getDateFromString(toDate);

        try {
            long diff = from.getTime() - to.getTime();
            long difference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (difference == 0) {
                return "Today";
            } else if (difference == -1) {
                return "Yesterday";
            } else {
                return "Date";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "False";
    }

    private Date getDateFromString(String lastRecordedDate) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(lastRecordedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date1 + "\t" + lastRecordedDate);
        return date1;
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewIcon;
        private RelativeLayout mLayoutMain;
        private TextView mTextViewTrackerName, mTextViewTrackerValue,
                mTextViewTrackerUnit, mTextViewTrackerDate,tvButtonViewOption;

        public ViewHolder(View view) {
            super(view);
            sharedPreferences = mContext.getSharedPreferences("userData", MODE_PRIVATE);
            edit = sharedPreferences.edit();

            mLayoutMain = view.findViewById(R.id.layout_header);
            mImageViewIcon = view.findViewById(R.id.imageview_icon);
            tvButtonViewOption = (TextView) view.findViewById(R.id.textViewOptions);
            mTextViewTrackerDate = view.findViewById(R.id.textview_tracker_date);
            mTextViewTrackerName = view.findViewById(R.id.textview_tracker_name);
            mTextViewTrackerUnit = view.findViewById(R.id.textview_tracker_unit);
            mTextViewTrackerValue = view.findViewById(R.id.textview_tracker_value);
        }
    }
}
