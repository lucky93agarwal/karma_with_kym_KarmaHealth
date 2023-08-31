package com.devkraft.karmahealth.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.devkraft.karmahealth.Model.DosageDropDownDto;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.Notification;
import com.devkraft.karmahealth.Model.PrescriptionRefillDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplicationPreferences {
    private static final String NOTIFICATION_LIST = "notificationList";
    private static final String NOTIFICATION_COUNT = "NOTIFICATION_COUNT";
    private static ApplicationPreferences instance;
    private final SharedPreferences sharedPrefs;
    private static final String BATTERY_MSG_EXPIRE = "BATTERY_MSG_EXPIRE";
    private static String spFileKey = ApplicationPreferences.class.getName() + ".prefs";
    private static final String DOSAGE_DTO = "DOSAGE_DTO";
    private static final String REMIND_REFILL_DTO = "REMIND_REFILL_DTO";
    public String getLastBatteryOptimizationDate() {
        return sharedPrefs.getString(BATTERY_MSG_EXPIRE,null);
    }

    public void logBatteryOptimizationDate(String date) {
        setStringValue(BATTERY_MSG_EXPIRE, date);
    }
    public static void init(Context context) {
        instance = new ApplicationPreferences(context);
    }
    private static final String USER_DETAILS = "user.details";

    private ApplicationPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(spFileKey, Context.MODE_PRIVATE);
    }
    public void setRefillData(PrescriptionRefillDto.PrescriptionRefillDtoList remindRefillList) {
        setStringValue(REMIND_REFILL_DTO, new Gson().toJson(remindRefillList));
    }
    public void setDosageData(String key, List<DosageDropDownDto> dosageDropDownDtoList) {
        setStringValue(key + DOSAGE_DTO , new Gson().toJson(dosageDropDownDtoList));
    }
    public void setUserDetails(LoginResponse loginResponse) {
        Gson gson = new Gson();
        String udString = gson.toJson(loginResponse);
        setStringValue(USER_DETAILS, udString);
    }
    public String getStringValue(String key){
        return sharedPrefs.getString(key, "");
    }
    private void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static ApplicationPreferences get() {
        if (instance == null) {
            throw new IllegalStateException("ApplicationPreferences should be initialized by calling init()");
        }
        return instance;
    }
    public List<PrescriptionRefillDto> getRemindRefillData() {
        String newsDtoStr = sharedPrefs.getString(REMIND_REFILL_DTO, null);
        if (newsDtoStr != null) {
            Gson gson = new Gson();

            Type type = new TypeToken<List<PrescriptionRefillDto>>() {
            }.getType();
            List<PrescriptionRefillDto> newsDtoList = gson.fromJson(newsDtoStr, type);
            return newsDtoList;
        }
        return null;
    }
    public LoginResponse getUserDetails() {
        Gson gson = new Gson();
        String udString = sharedPrefs.getString(USER_DETAILS, null);
        if (udString == null)
            return null;

        return gson.fromJson(udString, LoginResponse.class);
    }
    public List<DosageDropDownDto> getDosageList(String key) {
        try {
            String dosageStr = sharedPrefs.getString(key + DOSAGE_DTO, null);
            if (dosageStr != null) {
                Gson gson = new Gson();

                Type type = new TypeToken<List<DosageDropDownDto>>() {
                }.getType();
                List<DosageDropDownDto> dosageDropDownDtoList = gson.fromJson(dosageStr, type);
                return dosageDropDownDtoList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void saveStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public void setNotificationCount(int unreadCount) {
        setIntegerValue(NOTIFICATION_COUNT, unreadCount);
    }
    public int getNotificationCount() {
        return sharedPrefs.getInt(NOTIFICATION_COUNT, -1);
    }
    private void setIntegerValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public List<Notification> getNotificationList() {
        Gson gson = new Gson();
        String notiStr = sharedPrefs.getString(NOTIFICATION_LIST, null);
        if (notiStr == null)
            return null;

        Type notificationType = new TypeToken<List<Notification>>() {
        }.getType();
        return gson.fromJson(notiStr, notificationType);
    }
    public void setNotificationList(List<Notification> newNotificationList) {
        if (newNotificationList != null) {
            List<Notification> notificationList = new ArrayList<>();
            List<Notification> loadedNotificationList = getNotificationList();
            if (loadedNotificationList != null && loadedNotificationList.size() > 0) {
                notificationList.addAll(loadedNotificationList);
            }

            if (newNotificationList.size() > 0) {
                notificationList.addAll(newNotificationList);
            }

            Gson gson = new Gson();
            String notificationStr = gson.toJson(notificationList);
            setStringValue(NOTIFICATION_LIST, notificationStr);
        } else {
            setStringValue(NOTIFICATION_LIST, null);
        }
    }
}
