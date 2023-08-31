package com.devkraft.karmahealth.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.DrugDosageAdherence;
import com.devkraft.karmahealth.Model.DrugDosageAdherenceDTO;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.AlarmTable;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {

                String time = intent.getStringExtra(Constants.TIME);
                String scheduleDate = intent.getStringExtra(Constants.SCHEDULE_DATE);
                int notiId = intent.getIntExtra(Constants.NOTI_ID,-1);
                Cursor cursor = ApplicationDB.get().getAlarms(time);


                if (action.equalsIgnoreCase(Constants.TAKEN)) {

                    if (cursor != null && cursor.moveToFirst()) {

                        String drugAdherenceDtoStr = cursor.getString(cursor.getColumnIndex(AlarmTable.DRUG_ADHERENCE_DTO));
                        Long notificationUserId = cursor.getLong(cursor.getColumnIndex(AlarmTable.USER_ID));

                        Gson gson = new Gson();
                        Type drugAdherenceDtoType = new TypeToken<List<DrugDosageAdherenceDTO>>() {
                        }.getType();
                        List<DrugDosageAdherenceDTO> adherenceDTOList = gson.fromJson(drugAdherenceDtoStr, drugAdherenceDtoType);

                        if (adherenceDTOList != null) {
                            for (int i = 0; i < adherenceDTOList.size(); i++) {
                                DrugDosageAdherenceDTO drugDosageAdherenceDTO = adherenceDTOList.get(i);

                                drugDosageAdherenceDTO.setScheduleDate(scheduleDate);
                                drugDosageAdherenceDTO.setDrugTakenDate(AppUtils.getTodayDateForSchedule());
                                drugDosageAdherenceDTO.setDrugTaken(true);

                            }

                            DrugDosageAdherence drugDosageAdherence = new DrugDosageAdherence();
                            drugDosageAdherence.setDrugDosageAdherenceDTO(adherenceDTOList);


                            callDrugIntakeAPI(context,drugDosageAdherence,notiId,notificationUserId);
                        }
                    }


                } else if (action.equalsIgnoreCase(Constants.NOT_TAKEN)) {

                    if (cursor != null && cursor.moveToFirst()) {

                        String drugAdherenceDtoStr = cursor.getString(cursor.getColumnIndex(AlarmTable.DRUG_ADHERENCE_DTO));
                        Long notificationUserId = cursor.getLong(cursor.getColumnIndex(AlarmTable.USER_ID));

                        Gson gson = new Gson();
                        Type drugAdherenceDtoType = new TypeToken<List<DrugDosageAdherenceDTO>>() {
                        }.getType();
                        List<DrugDosageAdherenceDTO> adherenceDTOList = gson.fromJson(drugAdherenceDtoStr, drugAdherenceDtoType);

                        if (adherenceDTOList != null) {
                            for (int i = 0; i < adherenceDTOList.size(); i++) {
                                DrugDosageAdherenceDTO drugDosageAdherenceDTO = adherenceDTOList.get(i);

                                drugDosageAdherenceDTO.setScheduleDate(scheduleDate);
                                drugDosageAdherenceDTO.setDrugTakenDate(AppUtils.getTodayDateForSchedule());
                                drugDosageAdherenceDTO.setDrugTaken(false);

                            }

                            DrugDosageAdherence drugDosageAdherence = new DrugDosageAdherence();
                            drugDosageAdherence.setDrugDosageAdherenceDTO(adherenceDTOList);


                            callDrugIntakeAPI(context,drugDosageAdherence,notiId,notificationUserId);
                        }
                    }
                }
            }
        }
    }

    private void callDrugIntakeAPI(final Context context, DrugDosageAdherence drugDosageAdherence,
                                   final int notiId, Long notificationUserId) {
        UserDto userDto = ApplicationPreferences.get().getUserDetails().getUserDTO();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(context);
        GenericRequest<APIMessageResponse> postSideEffectRequest = new GenericRequest<APIMessageResponse>
                (Request.Method.POST, APIUrls.get().drugInTakeDetails(userDto.getId()),
                        APIMessageResponse.class, drugDosageAdherence,
                        new Response.Listener<APIMessageResponse>() {
                            @Override
                            public void onResponse(APIMessageResponse apiMessageResponse) {
                                authExpiredCallback.hideProgressBar();

                               /* NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                if(notificationManager != null){
                                    notificationManager.cancel(notiId);
                                }
                                UserDto userDto1 = new UserDto();
                                userDto1.setId(notificationUserId);
                                AppUtils.callTodayAgendaAPI(context,userDto1);*/
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyErrorForNoti(context, error, authExpiredCallback);
                                if(res != null){
                                }
                                AppUtils.callTodayAgendaAPI(context,userDto);
                            }
                        });
        authExpiredCallback.setRequest(postSideEffectRequest);
        ApiService.get().addToRequestQueue(postSideEffectRequest);
    }
}
