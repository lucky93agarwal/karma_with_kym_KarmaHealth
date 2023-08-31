package com.devkraft.karmahealth.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Screen.TrackActivity;
import com.devkraft.karmahealth.Screen.TrackDetailsActivity;
import com.devkraft.karmahealth.Screen.TrackerDetailActivity;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import com.devkraft.karmahealth.R;

public class ReportVitalAdapter extends ArrayAdapter<TrackConfigurationDto> {

    private ProgressDialogSetup progress;
    private List<TrackConfigurationDto> list;
    private Context context;
    private LayoutInflater inflater;
    private Long parameterId;
    private Long userParameterId;
    private String name;
    private UserDto userDto;
    private TrackerDetailActivity reportActivity;
    private String startDate, endDate;


    public ReportVitalAdapter(Context context, int resource, List<TrackConfigurationDto> list, Long pId,
                              String name, UserDto userDto, TrackerDetailActivity reportActivity,
                              String startDate, String endDate, Long userParameterId) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
        this.parameterId = pId;
        this.name = name;
        this.userDto = userDto;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userParameterId = userParameterId;
        this.reportActivity = reportActivity;
        inflater = LayoutInflater.from(context);
        Log.e("list_log", " = " + new Gson().toJson(list));
        Log.e("name_log", " = " + name);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recyclerview_track_details, null);
            holder = new ViewHolder();

            // 1) Initialize UI components.

            holder.mView = convertView.findViewById(R.id.view);
            holder.dateTv = convertView.findViewById(R.id.dateTv);
            holder.editIv = convertView.findViewById(R.id.editIv);
            holder.deleteIv = convertView.findViewById(R.id.deleteIv);
            holder.maxHeader = convertView.findViewById(R.id.maxHeader);
            holder.maxHeaderLl = convertView.findViewById(R.id.maxHeaderLl);
            holder.mTextViewMaxHeader = convertView.findViewById(R.id.maxHeader1);
            holder.mLayoutSingleEntry = convertView.findViewById(R.id.layout_single_entry);
            holder.mLayoutDoubleEntry = convertView.findViewById(R.id.layout_double_entry);
            holder.mTextViewBloodGlucoseTrackTime = convertView.findViewById(R.id.textview_blood_glucose_parameter);

            holder.maxValue = convertView.findViewById(R.id.maxValue);
            holder.minHeaderLl = convertView.findViewById(R.id.minHeaderLl);
            holder.mTextViewMaxValue = convertView.findViewById(R.id.maxValue1);

            holder.minHeader = convertView.findViewById(R.id.minHeader);
            holder.minValue = convertView.findViewById(R.id.minValue);
            holder.parentLayout = convertView.findViewById(R.id.parentLayout);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TrackConfigurationDto item = getItem(position);

        String lastRecordedDate = item.getRecordedDate();
        if (lastRecordedDate != null) {
            holder.dateTv.setText(AppUtils.getDateInReportFormatted(lastRecordedDate));
        }

        String maxBaselineDisplayName = item.getMaxBaselineDisplayName();
        Double maxBaselineValue = item.getMaxBaselineValue();
        String measurementUnit = item.getMeasurementUnit();

        if (measurementUnit.equalsIgnoreCase(Constants.F)) {
            measurementUnit = context.getString(R.string.fahrenheit);
        } else if (measurementUnit.equalsIgnoreCase(Constants.C)) {
            measurementUnit = context.getString(R.string.celsius);
        }

        if (maxBaselineValue == null) {
            holder.maxHeaderLl.setVisibility(View.GONE);
        } else {
            holder.maxHeaderLl.setVisibility(View.VISIBLE);
            holder.mLayoutDoubleEntry.setVisibility(View.GONE);
            holder.mLayoutSingleEntry.setVisibility(View.VISIBLE);

            if (name.equalsIgnoreCase(Constants.Blood_Glucose)) {
                holder.mTextViewBloodGlucoseTrackTime.setVisibility(View.VISIBLE);
                holder.mTextViewBloodGlucoseTrackTime.setText(item.getMeasured());
            } else {
                holder.mTextViewBloodGlucoseTrackTime.setVisibility(View.GONE);
            }

            String maxHeaderStr = "";
            if (name.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {
                if (maxBaselineDisplayName != null)
//                maxHeaderStr = maxHeaderStr + maxBaselineDisplayName + " (" + measurementUnit + ")";
                    maxHeaderStr = maxHeaderStr + maxBaselineDisplayName;
            } else {
//                maxHeaderStr = maxHeaderStr + name + " (" + measurementUnit + ")";
                maxHeaderStr = maxHeaderStr + name;
            }


            holder.maxHeader.setText(maxHeaderStr);
            holder.mTextViewMaxHeader.setText(maxHeaderStr);

            if (maxBaselineDisplayName != null) {
                if (maxBaselineDisplayName.equalsIgnoreCase(Constants.Weight) ||
                        maxBaselineDisplayName.equalsIgnoreCase(Constants.Temperature)) {
//                    holder.maxValue.setText(AppUtils.getValueWithOneDecimal(maxBaselineValue));
                    holder.maxValue.setText(AppUtils.getValueWithOneDecimal(maxBaselineValue) + " " + measurementUnit);
//                    holder.mTextViewMaxValue.setText(AppUtils.getValueWithOneDecimal(maxBaselineValue));
                    holder.mTextViewMaxValue.setText(AppUtils.getValueWithOneDecimal(maxBaselineValue) + " " + measurementUnit);
                } else {
//                    holder.maxValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue));
                    holder.maxValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue) + " " + measurementUnit);
//                    holder.mTextViewMaxValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue));
                    holder.mTextViewMaxValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue) + " " + measurementUnit);
                }
            }
        }

        String minBaselineDisplayName = item.getMinBaselineDisplayName();
        Double minBaselineValue = item.getMinBaselineValue();

        if (minBaselineValue == null) {
            holder.mView.setVisibility(View.GONE);
            holder.minHeaderLl.setVisibility(View.GONE);
        } else {
            holder.mView.setVisibility(View.VISIBLE);
            holder.minHeaderLl.setVisibility(View.VISIBLE);
            holder.mLayoutSingleEntry.setVisibility(View.GONE);
            holder.mLayoutDoubleEntry.setVisibility(View.VISIBLE);

            String minHeaderStr = "";
            if (name.equalsIgnoreCase(Constants.Blood_Pressure_LEVELS)) {
                if (maxBaselineDisplayName != null)
//                minHeaderStr = minHeaderStr + minBaselineDisplayName + " (" +measurementUnit + ")" ;
                    minHeaderStr = minHeaderStr + minBaselineDisplayName;
            } else {
//                minHeaderStr = minHeaderStr + name + " (" + measurementUnit + ")";
                minHeaderStr = minHeaderStr + name;
            }
            holder.minHeader.setText(minHeaderStr);
            holder.minValue.setText(AppUtils.getValueWithoutDecimal(minBaselineValue) + " " + measurementUnit);
            holder.maxValue.setText(AppUtils.getValueWithoutDecimal(maxBaselineValue) + " " + measurementUnit);
        }


        ViewHolder finalHolder = holder;
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(item, position, finalHolder.parentLayout, name);
            }
        });

        holder.editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put(Constants.HEALTH_MATRICS_NAME,name);
                AppUtils.logCleverTapEvent(context,
                        Constants.CLICKED_ON_TRACK__ENTRY_EDIT_BUTTON_ON_REPORT_SCREEN, hashMap);
                ParameterDto parameterDto = new ParameterDto();

                parameterDto.setId(parameterId);
                parameterDto.setUserParameterId(userParameterId);
                parameterDto.setName(name);


                //parameterDto.setLastRecordedDate(AppUtils.getDisplayFormattedFormReport(item.getRecordedDate()));
                parameterDto.setLastRecordedDate(AppUtils.getTrackFormattedDateFromReportScreen(item.getRecordedDate()));
                parameterDto.setMeasurementUnit(item.getMeasurementUnit());

                parameterDto.setMaxBaselineDisplayName(item.getMaxBaselineDisplayName());
                parameterDto.setMaxBaselineValue(item.getMaxBaselineValue());

                parameterDto.setMinBaselineDisplayName(item.getMinBaselineDisplayName());
                parameterDto.setMinBaselineValue(item.getMinBaselineValue());

                parameterDto.setMeasured(item.getMeasured());

                parameterDto.setNotes(item.getNotes());

                //AppUtils.getTrackFormattedDate();
                Intent intent = new Intent(context, TrackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.USER_DTO, Constants.GSON.toJson(userDto));
                intent.putExtra(Constants.WHICH_DISEASE, name);
                intent.putExtra(Constants.PARAMETER_DTO, Constants.GSON.toJson(parameterDto));
                intent.putExtra(Constants.OPEN_TRACK, true);
                intent.putExtra(Constants.IS_VITAL_EDIT, true);
                intent.putExtra(Constants.USER_PARA_TRACKING_ID, item.getUserParameterTrackingId());
                intent.putExtra(Constants.IS_FROM_REPORT, true);
                intent.putExtra(Constants.START_DATE, startDate);
                intent.putExtra(Constants.END_DATE, endDate);
                context.startActivity(intent);

            }
        });


        return convertView;
    }


    private void showDialogBox(final TrackConfigurationDto item, final int position,
                               LinearLayout parentLayout, String name) {
        if (userDto != null && context != null && item != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View promptView = layoutInflater.inflate(R.layout.delete_drug_dialog_box, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            alertDialogBuilder.setView(promptView);

            TextView title = promptView.findViewById(R.id.title);
            TextView msg = promptView.findViewById(R.id.msg);

            title.setText(context.getString(R.string.are_you_sure));

            String msgStr = context.getString(R.string.deleteing_this_reading) + context.getString(R.string.space)
                    + name + context.getString(R.string.space) + context.getString(R.string.entry_from_profile);

            msg.setText(msgStr);

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            callDeleteParameterAPI(userDto.getId(), item.getUserParameterTrackingId(),
                                    position, parentLayout,item);
                        }
                    });

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void callDeleteParameterAPI(Long userId, Long userParameterTrackingId, final int position, LinearLayout parentLayout, TrackConfigurationDto item) {
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(context);
        GenericRequest<APIMessageResponse> drugTakenRequest = new GenericRequest<APIMessageResponse>
                (Request.Method.DELETE, APIUrls.get().deleteVital(userDto.getId(), userParameterTrackingId),
                        APIMessageResponse.class, null,
                        new Response.Listener<APIMessageResponse>() {
                            @Override
                            public void onResponse(APIMessageResponse apiMessageResponse) {
                                authExpiredCallback.hideProgressBar();
                                HashMap<String,Object> healthMatrics = new HashMap<>();
                                healthMatrics.put(Constants.HEALTH_MATRICS_NAME,name);
                                healthMatrics.put(Constants.VALUE,"Min =" +item.getMinBaselineValue() + "Max =" +item.getMaxBaselineValue());
                                healthMatrics.put(Constants.UNIT,item.getMeasurementUnit());
                                healthMatrics.put(Constants.DATE,AppUtils.getTodayDate());
                                healthMatrics.put(Constants.TIME,AppUtils.getCurrentTime());
                                AppUtils.logCleverTapEvent(context,
                                        Constants.HEALTH_METRICS_TRACK_ENTRY_DELETED_ON_REPORT_SCREEN, healthMatrics);
                                list.remove(position);
                                notifyDataSetChanged();
                                AppUtils.isDataChanged = true;
                                if (list.size() == 0) {
                                    reportActivity.callAPI();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyErrorForNoti(context, error, authExpiredCallback);
                                AppUtils.openSnackBar(parentLayout, res);
                            }
                        });
        authExpiredCallback.setRequest(drugTakenRequest);
        ApiService.get().addToRequestQueue(drugTakenRequest);
    }

    private void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
        if (list.size() == 0) {
            // vitalsFragment.showEmptyView();
        }
    }

    @Override
    public int getCount() {
        if (this.list != null && this.list.size() > 0) {
            return this.list.size();
        } else {
            return 0;
        }
    }

    private class ViewHolder {
        private View mView;
        private TextView dateTv, maxHeader, maxValue, minHeader, minValue;
        private ImageView editIv, deleteIv;
        private LinearLayout maxHeaderLl, minHeaderLl, parentLayout;
        private LinearLayout mLayoutSingleEntry, mLayoutDoubleEntry;
        private TextView mTextViewMaxHeader, mTextViewMaxValue;
        private TextView mTextViewBloodGlucoseTrackTime;
    }
}
