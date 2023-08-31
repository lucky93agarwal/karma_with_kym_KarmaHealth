package com.devkraft.karmahealth.Utils;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.clevertap.android.sdk.CleverTapAPI;
import com.devkraft.karmahealth.BroadcastReceiver.AlarmReceiver;
import com.devkraft.karmahealth.BroadcastReceiver.NotificationReceiver;
import com.devkraft.karmahealth.BuildConfig;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.FitBitMultipleParameterDto;
import com.devkraft.karmahealth.Model.ParamDto;
import com.devkraft.karmahealth.Model.UserAgendaDto;
import com.devkraft.karmahealth.Screen.HomeActivity;
import com.devkraft.karmahealth.db.AlarmTable;
import com.devkraft.karmahealth.Model.DosageDropDownDto;
import com.devkraft.karmahealth.Model.DosageDto;
import com.devkraft.karmahealth.Model.DrugDosageAdherenceDTO;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Model.PrescriptionRefillDto;
import com.devkraft.karmahealth.Model.ReminderDto;
import com.devkraft.karmahealth.Model.Time;
import com.devkraft.karmahealth.Model.UserDrugDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.WeeklyDays;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.db.PrescriptionRefillTable;
import com.devkraft.karmahealth.fragment.DialogFragmentDiseases;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static final int WEIGHT_MAX_VALUE_KG = 500;
    public static final int MIN_VALUE_TEMP_CELSIUS = 35;
    public static final int MAX_VALUE_TEMP_CELSIUS = 41;
    private static final String GROUP_KEY = "com.android.reminder";
    public static final int RR_MAX_VALUE = 30;
    private static int fitbitApiCallCount = 0;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String mAccessToken;
    private static final List<FitBitMultipleParameterDto> fitBitMultipleParameterDtos = new ArrayList<>();


    public static boolean isApiCalled = false, isFitBitApiRunning = false;
    public static final int MAX_VALUE_TEMP_FAHRENHEIT = 106;
    public static final int MIN_VALUE_TEMP_FAHRENHEIT = 95;
    public static final int RR_MIN_VALUE = 10;
    public static final int HEART_RATE_MAX_VALUE = 300;
    public static final int BLOOD_SUGAR_FASTING_MAX_VALUE = 750;
    public static final int SYSTOLIC_MIN_VALUE = 75;
    public static final int DIASTOLIC_MAX_VALUE = 250;
    public static final int SYSTOLIC_MAX_VALUE = 300;
    public static final int MIN_VALUE = 0;
    public static final int WEIGHT_MAX_VALUE_LBS = 1000;
    public static final int DAILY_LIMIT = 4;
    public static final int WEEKLY_DAY_LIMIT = 3;
    public static final int WEEKLY_LIMIT = 1;
    public static final long WEEKLY_INTERVAL = 24 * 7 * 60 * 60 * 1000;
    public static boolean isDataChanged = true;
    public static String getDateOnlyFromDateTime(String stringDate) {
        String str = stringDate;

        String fmt = "yyyy-MM-dd HH:mm";
        DateFormat df = new SimpleDateFormat(fmt);

        Date dt = null;
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat tdf = new SimpleDateFormat("HH:mm");
        DateFormat dfmt = new SimpleDateFormat("MM/dd/yyyy");

        String timeOnly = tdf.format(dt);
        String dateOnly = dfmt.format(dt);

        return dateOnly;
    }
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static void logDrugAddedRequestEvent(String drugName) {
       /* AppEventsLogger logger = Analytics.get();
        if (logger != null && isProdBuild()) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DRUG_NAME, drugName);
            logger.logEvent(Constants.DRUG_ADDED, bundle);
        }*/
    }
    public static void setAlarmForDrug(Context context, UserDrugDto userDrugDto,
                                       UserDrugDto oldUserDrugDto, PrescriptionRefillDto prescriptionRefillDto) {
        setAlarm(context, userDrugDto, oldUserDrugDto, prescriptionRefillDto);
    }

    public static void setAlarmAfterBoot(Context context) {
        try {
            if (context != null) {
                List<UserDrugDto> userDrugDtoList = ApplicationDB.get().getAllDrug();

                if (userDrugDtoList != null && userDrugDtoList.size() > 0) {

                    for (int i = 0; i < userDrugDtoList.size(); i++) {
                        UserDrugDto userDrugDto = userDrugDtoList.get(i);
                        AppUtils.setAlarmForDrug(context, userDrugDto, null, userDrugDto.getPrescriptionRefill());
                    }

                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
    }
    public static boolean isTimePassed(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Long currentTimeMillis = System.currentTimeMillis();
        String currentTimeStr = simpleDateFormat.format(currentTimeMillis);

        try {
            Date todayDate = simpleDateFormat.parse(currentTimeStr);
            Date dateToBeChecked = simpleDateFormat.parse(time);

            if (dateToBeChecked.after(todayDate)) {
                return false;
            } else {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void setPrescriptionRefillAlarm(Context context, UserDrugDto userDrugDto, PrescriptionRefillDto prescriptionRefill) {
        Long WEEK_INTERVAL = 24L * 7L * 60L * 60L * 1000L;
        long TWO_WEEK_INTERVAL = 24 * 7 * 60 * 60 * 1000 * 2;
        long THREE_WEEK_INTERVAL = 24 * 7 * 60 * 60 * 1000 * 3;
        long ONE_MONTH_INTERVAL = AlarmManager.INTERVAL_DAY * 30;
        long TWO_MONTH_INTERVAL = AlarmManager.INTERVAL_DAY * 30 * 2;
        long THREE_MONTH_INTERVAL = AlarmManager.INTERVAL_DAY * 30 * 3;

        if (prescriptionRefill != null && userDrugDto != null && context != null) {
            try {

                String displayName = prescriptionRefill.getValue();

                if (!displayName.equalsIgnoreCase(Constants.no_reminder)) {

                    SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Cursor cursor = ApplicationDB.get().getLastPrescriptionEntry();

                    if (cursor.moveToFirst()) {
                        String dbTime = cursor.getString(cursor.getColumnIndex(PrescriptionRefillTable.TIME));

                        String todayDate = getTodayDateForSchedule();

                        String dateTime = todayDate + " " + dbTime;

                        Date formattedDate = sDFormat.parse(dateTime);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(formattedDate);

                        cal.add(Calendar.SECOND, 31);

                        long dateTobeConvert = cal.getTimeInMillis();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        String time = simpleDateFormat.format(dateTobeConvert);

                        String[] split = time.split(":");

                        //String uId = userDrugDto.getDrugId() + split[0] + split[1] + split[2];
                        String uId = userDrugDto.getDrugId() + userDrugDto.getUserId() + "";
                        int identifier = Integer.parseInt(uId); //drugId + time int

                        if (userDrugDto.getDisplayName() != null) {
                            String title = userDrugDto.getDisplayName() + "-" + context.getString(R.string.prescription_refill_reminder);
                            String subTitle = context.getString(R.string.ensure) + context.getString(R.string.space)
                                    + userDrugDto.getDisplayName() + context.getString(R.string.space) +
                                    context.getString(R.string.prescription_dose_not_run_out);

                            updatePrescriptionTable(uId, time, title, subTitle);
                        }
                    } else {
                        String time = "09" + ":" + 13 + ":" + "00";
                        String todayDate = getTodayDateForSchedule();

                        String dateTime = todayDate + " " + time;

                        Date formattedDate = sDFormat.parse(dateTime);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(formattedDate);

                        String[] split = time.split(":");

                        //String uId = userDrugDto.getDrugId() + split[0] + split[1] + split[2];
                        String uId = userDrugDto.getDrugId() + userDrugDto.getUserId() + "";
                        int identifier = Integer.parseInt(uId); //drugId + userId int


                      /*  String title = userDrugDto.getUserName() + "-" + userDrugDto.getDrugName() + "-" + context.getString(R.string.prescription_refill_reminder);
                        String subTitle = context.getString(R.string.ensure) + context.getString(R.string.space)
                                + userDrugDto.getDrugName() + context.getString(R.string.space) +
                                context.getString(R.string.prescription_dose_not_run_out);*/

                        String title = userDrugDto.getUserName() + "-" + userDrugDto.getDisplayName() + "-" + context.getString(R.string.prescription_refill_reminder);
                        String subTitle = context.getString(R.string.ensure) + context.getString(R.string.space)
                                + userDrugDto.getDisplayName() + context.getString(R.string.space) +
                                context.getString(R.string.prescription_dose_not_run_out);

                        // setting alarm for particular time
                        // if time is already passed then start alarm form the next day
                        if (AppUtils.isTimePassed(time)) {
                            cal.add(Calendar.DATE, 1);
                        }

                        if (displayName.equalsIgnoreCase(Constants.one_week)) {
                            setAlarmForPrescription(context, cal.getTimeInMillis(), time, WEEK_INTERVAL);
                        } else if (displayName.equalsIgnoreCase(Constants.two_week)) {
                            setAlarmForPrescription(context, cal.getTimeInMillis(), time, TWO_WEEK_INTERVAL);
                        } else if (displayName.equalsIgnoreCase(Constants.three_week)) {
                            setAlarmForPrescription(context, cal.getTimeInMillis(), time, THREE_WEEK_INTERVAL);
                        } else if (displayName.equalsIgnoreCase(Constants.one_month)) {
                            setAlarmForPrescription(context, cal.getTimeInMillis(), time, ONE_MONTH_INTERVAL);
                        } else if (displayName.equalsIgnoreCase(Constants.two_months)) {
                            setAlarmForPrescription(context, cal.getTimeInMillis(), time, TWO_MONTH_INTERVAL);
                        } else if (displayName.equalsIgnoreCase(Constants.three_months)) {
                            setAlarmForPrescription(context, cal.getTimeInMillis(), time, THREE_MONTH_INTERVAL);
                        }

                        updatePrescriptionTable(uId, time, title, subTitle);
                        //setAlarmForPrescription(context, cal.getTimeInMillis(), time, interval);
                    }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
    private static void updatePrescriptionTable(String identifier, String time, String title, String subTitle) {
        ApplicationDB.get().insertPrescriptionValue(identifier, time, title, subTitle);
    }

    private static void setAlarmForPrescription(Context context, long timeInMillis, String time, long intervalTime) {
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        notificationIntent.putExtra(Constants.TIME, time);
        notificationIntent.putExtra(Constants.PRESCRIPTION_REFILL, Constants.PRESCRIPTION_REFILL);


        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String[] split = time.split(":");

        String requestCodeStr = split[0] + split[1] + split[2];
        int requestCode = Integer.parseInt(requestCodeStr);

        PendingIntent broadcast;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= 31) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalTime, broadcast);
                } else {
                    Intent intent = new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    context.startActivity(intent);
                }
            } else {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalTime, broadcast);
            }
        }
    }

    private static void setAlarm(Context context, UserDrugDto userDrugDto, UserDrugDto oldUserDrugDto, PrescriptionRefillDto prescriptionRefillDto) {
        try {
            String startDateStr = userDrugDto.getStartDate();

            String dosageType = userDrugDto.getDosageType();

            Long userDrugId = userDrugDto.getId();
//            String drugName = userDrugDto.getDrugName();
            String drugName = userDrugDto.getDisplayName();

            List<DrugDosageAdherenceDTO> dosageAdherenceDTOList = new ArrayList<>();

            setPrescriptionRefillAlarm(context, userDrugDto, prescriptionRefillDto);

            if (oldUserDrugDto != null) {
                deleteAlarmForDrug(context, oldUserDrugDto);
                deleteAlarmForPrescriptionRefill(context, oldUserDrugDto);
            }

            if (dosageType != null) {
                if (dosageType.equalsIgnoreCase(context.getString(R.string.weekly))) {

                    List<ReminderDto> reminderDtoList = userDrugDto.getReminderList();

                    DrugDosageAdherenceDTO drugDosageAdherenceDTO;
                    if (reminderDtoList != null && reminderDtoList.size() > 0) {
                        for (int i = 0; i < reminderDtoList.size(); i++) {
                            ReminderDto reminderDto = reminderDtoList.get(i);

                            setAlarmForWeekly(userDrugId, drugName, reminderDto,
                                    userDrugDto.getUserId(), startDateStr, userDrugDto,
                                    oldUserDrugDto, context, userDrugDto.getDosage());
                        }

                    }

                } else if (dosageType.equalsIgnoreCase(context.getString(R.string.monthly))) {

                    List<ReminderDto> reminderDtoList = userDrugDto.getReminderList();
                    DrugDosageAdherenceDTO drugDosageAdherenceDTO;
                    if (reminderDtoList != null && reminderDtoList.size() > 0) {
                        for (int i = 0; i < reminderDtoList.size(); i++) {
                            ReminderDto reminderDto = reminderDtoList.get(i);

                            setAlarmForMonthly(userDrugId, drugName, reminderDto,
                                    userDrugDto.getUserId(), startDateStr, userDrugDto,
                                    oldUserDrugDto, context, userDrugDto.getDosage());
                        }
                    }

                } else if (dosageType.equalsIgnoreCase(context.getString(R.string.daily))) {

                    List<ReminderDto> reminderDtoList = userDrugDto.getReminderList();
                    DrugDosageAdherenceDTO drugDosageAdherenceDTO;
                    if (reminderDtoList != null && reminderDtoList.size() > 0) {
                        for (int i = 0; i < reminderDtoList.size(); i++) {
                            ReminderDto reminderDto = reminderDtoList.get(i);

                            setAlarmForDaily(userDrugId, drugName, reminderDto,
                                    userDrugDto.getUserId(), startDateStr, userDrugDto,
                                    oldUserDrugDto, context, userDrugDto.getDosage());
                        }
                    }
                }
            } else {
                if (userDrugDto.getCustom()) {
                    List<ReminderDto> reminderDtoList = userDrugDto.getReminderList();
                    if (reminderDtoList != null && reminderDtoList.size() > 0) {
                        for (int i = 0; i < reminderDtoList.size(); i++) {
                            ReminderDto reminderDto = reminderDtoList.get(i);

                            String dtoDosageType = reminderDto.getDosageType();
                            String dosage = reminderDto.getDosage();
                            if (dtoDosageType != null) {
                                if (dtoDosageType.equalsIgnoreCase(context.getString(R.string.weekly))) {
                                    setAlarmForWeekly(userDrugId, drugName, reminderDto,
                                            userDrugDto.getUserId(), startDateStr, userDrugDto,
                                            oldUserDrugDto, context, dosage);
                                } else if (dtoDosageType.equalsIgnoreCase(context.getString(R.string.monthly))) {
                                    setAlarmForMonthly(userDrugId, drugName, reminderDto,
                                            userDrugDto.getUserId(), startDateStr, userDrugDto,
                                            oldUserDrugDto, context, dosage);
                                } else if (dtoDosageType.equalsIgnoreCase(context.getString(R.string.daily))) {
                                    setAlarmForDaily(userDrugId, drugName, reminderDto,
                                            userDrugDto.getUserId(), startDateStr, userDrugDto,
                                            oldUserDrugDto, context, dosage);
                                }
                            }

                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteAlarmForPrescriptionRefill(Context context, UserDrugDto oldUserDrugDto) {
        if (context != null) {
            PrescriptionRefillDto prescriptionRefill = oldUserDrugDto.getPrescriptionRefill();
            if (prescriptionRefill != null) {

                String uId = oldUserDrugDto.getDrugId() + oldUserDrugDto.getUserId() + "";
                int identifier = Integer.parseInt(uId); //drugId + drugId int

                Cursor cursor = ApplicationDB.get().getPrescriptionEntry(uId);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        String time = cursor.getString(cursor.getColumnIndex(PrescriptionRefillTable.TIME));

                        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                        notificationIntent.addCategory("android.intent.category.DEFAULT");

                        notificationIntent.putExtra(Constants.TIME, time);
                        notificationIntent.putExtra(Constants.PRESCRIPTION_REFILL, Constants.PRESCRIPTION_REFILL);

                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        String[] split = time.split(":");

                        String requestCodeStr = split[0] + split[1] + split[2];
                        int requestCode = Integer.parseInt(requestCodeStr);

                        PendingIntent broadcast;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        if (alarmManager != null) {
                            alarmManager.cancel(broadcast);
                        }


                        ApplicationDB.get().deletePrescriptionEntry(uId);
                    }
                }
            }
        }
    }

    public static void deleteAlarmForDrug(Context context, UserDrugDto userDrugDto) {

        if (context != null) {

            String reminderType = userDrugDto.getDosageType();

            deleteAlarmForPrescriptionRefill(context, userDrugDto);

            List<ReminderDto> reminderList = userDrugDto.getReminderList();
            if (reminderList != null && reminderList.size() > 0) {
                for (int j = 0; j < reminderList.size(); j++) {
                    ReminderDto reminderDto = reminderList.get(j);

                    String dataToBeSend = reminderDto.getValue();

                    String time = reminderDto.getReminderTime();

                    if (time != null) {
                        String[] split = time.split(":");
                        String uId = userDrugDto.getUserId() + "";
                        if (split.length > 0) {
                            String timeStr = split[0];

                            if (timeStr != null) {
                                if (context.getString(R.string.weekly).equalsIgnoreCase(reminderType)) {
                                    String[] timeSplit = timeStr.split(":");
                                    if (timeSplit.length > 0) {
                                        String hourStr = timeSplit[0];
                                        int hour = (Integer.parseInt(hourStr) - 1);
                                          /*  uId = uId + hour + "15" + "00";
                                        timeStr = timeStr + ":15" + ":00";*/

                                        uId = uId + hour + "00" + "00";
                                        timeStr = timeStr + ":00" + ":00";
                                    }
                                } else if (context.getString(R.string.monthly).equalsIgnoreCase(reminderType)) {
                                    String[] timeSplit = timeStr.split(":");
                                    if (timeSplit.length > 0) {
                                        String hourStr = timeSplit[0];
                                        int hour = (Integer.parseInt(hourStr) - 1);
                                        uId = uId + hour + "30" + "00";
                                        timeStr = timeStr + ":30" + ":00";
                                    }
                                } else if (context.getString(R.string.daily).equalsIgnoreCase(reminderType)) {
                                    uId = uId + timeStr + "00" + "00";
                                    timeStr = timeStr + ":00" + ":00";
                                }


                                String identifier = uId; // userId + time int
                                Log.e("delete_identifier", identifier);
                                // delete alarm row from db
                                ApplicationDB.get().deleteAlarmValue(identifier);

                                if (!ApplicationDB.get().isAlarmExistForSameTime(time)) {
                                    Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                                    notificationIntent.addCategory("android.intent.category.DEFAULT");

                                    notificationIntent.putExtra(Constants.TIME, time);
                                    notificationIntent.putExtra(Constants.REMINDER_TYPE, reminderType);

                                    if (context.getString(R.string.weekly).equalsIgnoreCase(reminderType)) {
                                        notificationIntent.putExtra(Constants.DATA_PARA, dataToBeSend);
                                    } else if (context.getString(R.string.monthly).equalsIgnoreCase(reminderType)) {
                                        notificationIntent.putExtra(Constants.DATA_PARA, dataToBeSend);
                                    }

                                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                    String[] splitStr = timeStr.split(":");

                                    int requestCode = 0;
                                    if (splitStr.length > 2) {
                                        String requestCodeStr = splitStr[0] + splitStr[1] + splitStr[2];
                                        requestCode = Integer.parseInt(requestCodeStr);
                                    }

                                    PendingIntent broadcast;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                                    } else {
                                        broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    }

                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    if (alarmManager != null) {
                                        alarmManager.cancel(broadcast);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void setAlarmForDaily(Long userDrugId, String drugName, ReminderDto reminderDto,
                                         Long userId, String startDateStr, UserDrugDto userDrugDto,
                                         UserDrugDto oldUserDrugDto, Context context, String dosage) {
        try {
            Long userDrugDosageId = reminderDto.getId();
            String scheduleTime = reminderDto.getReminderTime();

            DrugDosageAdherenceDTO drugDosageAdherenceDTO = new DrugDosageAdherenceDTO();

            drugDosageAdherenceDTO.setUserDrugId(userDrugId);
            drugDosageAdherenceDTO.setUserDrugDosageId(userDrugDosageId);
            drugDosageAdherenceDTO.setTime(scheduleTime);
            drugDosageAdherenceDTO.setDrugName(drugName);

            List<DrugDosageAdherenceDTO> dosageAdherenceDTOList = new ArrayList<>();
            dosageAdherenceDTOList.add(drugDosageAdherenceDTO);

            String timeStr = reminderDto.getReminderTime();

            SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String[] timeSplit = timeStr.split(":");
            String hourStr = timeSplit[0];
            String min = "00";
            String sec = "00";

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date startDate = dateFormat.parse(startDateStr);

            if (startDate.before(new Date())) {
                Calendar cal = Calendar.getInstance();
                long date = System.currentTimeMillis();
                cal.setTimeInMillis(date);
                startDateStr = dateFormat.format(cal.getTime());
            }

            String time = hourStr + ":" + min + ":" + sec;

            String dateTime = startDateStr + " " + time;

            Date formattedDate = sDFormat.parse(dateTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(formattedDate);


            String[] split = time.split(":");

            String uId = userDrugDto.getUserId() + split[0] + min + sec;
            //int identifier = Integer.parseInt(uId); //userId + time int
            String identifier = uId; //userId + time int
            Log.e("identifier", identifier);

            boolean isTimeExist = ApplicationDB.get().isAlarmExistForSameTime(time);

            if (isTimeExist) {
                // Time is exist update db for same time
                updateAlarmDB(identifier, time, startDateStr, userDrugDto,
                        oldUserDrugDto, null, dosageAdherenceDTOList, dosage);

            } else {
                // Time is not exist add into db and set alarm
                updateAlarmDB(identifier, time, startDateStr, userDrugDto, oldUserDrugDto,
                        null, dosageAdherenceDTOList, dosage);

                // setting alarm for particular time
                // if time is already passed then start alarm form the next day
                if (AppUtils.isTimePassed(time)) {
                    cal.add(Calendar.DATE, 1);
                }
                setAlarmForTime(context, time, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, Constants.DAILY, null,"");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
    private static void updateAlarmDB(String identifier, String time, String startDateStr,
                                      UserDrugDto userDrugDto, UserDrugDto oldUserDrugDto,
                                      String weekDayName,
                                      List<DrugDosageAdherenceDTO> drugDosageAdherenceDTOList,
                                      String dosage) {
        Cursor cursor = null;
        if (weekDayName == null) {
            cursor = ApplicationDB.get().getAlarmCursor(identifier);
        } else {
            cursor = ApplicationDB.get().getAlarmCursorForWeeklyMonthly(identifier, weekDayName);
        }

        //Cursor cursor = ApplicationDB.get().getAlarmCursor(identifier);

        if (cursor == null) {
            String subTitle = "";
            if (dosage != null) {
//                subTitle = userDrugDto.getDrugName().concat("-").concat(dosage);
                subTitle = userDrugDto.getDisplayName().concat("-").concat(dosage);
            }
            ApplicationDB.get().insertAlarmValue(identifier, userDrugDto.getUserName(), subTitle,
                    startDateStr, time, userDrugDto.getStrength(), weekDayName, drugDosageAdherenceDTOList, userDrugDto.getUserId());
        } else {
            // update subTitle value
            if (cursor.moveToFirst()) {
                String dbSubTitle = cursor.getString(cursor.getColumnIndex(AlarmTable.SUB_TITLE));

                if (oldUserDrugDto != null) {
//                    String oldValue = oldUserDrugDto.getDrugName();
                    String oldValue = oldUserDrugDto.getDisplayName();
                    String oldStrength = oldUserDrugDto.getDosage();
                    if (oldStrength != null && oldStrength.length() > 0) {
                        oldValue = oldValue.concat("-").concat(oldStrength);
                    }

                    if (dbSubTitle.contains(oldValue)) {
                        // removing old subtitle for same drug and replace with new one
//                        String drugName = userDrugDto.getDrugName();
                        String drugName = userDrugDto.getDisplayName();
                        String strength = dosage;
                        if (strength != null && strength.length() > 0) {
                            drugName = drugName.concat("-").concat(strength);
                        }

                        dbSubTitle = dbSubTitle.replace(oldValue, drugName);
                    } else {
//                        dbSubTitle = dbSubTitle.concat("&&&").concat(userDrugDto.getDrugName());
                        dbSubTitle = dbSubTitle.concat("&&&").concat(userDrugDto.getDisplayName());
                        String strength = dosage;
                        if (strength != null && strength.length() > 0) {
                            dbSubTitle = dbSubTitle.concat("-").concat(strength);
                        }
                    }

                    ApplicationDB.get().updateAlarmSubTitle(identifier, dbSubTitle);

                } else {
//                    dbSubTitle = dbSubTitle.concat("&&&").concat(userDrugDto.getDrugName());
                    dbSubTitle = dbSubTitle.concat("&&&").concat(userDrugDto.getDisplayName());
                    String strength = dosage;
                    if (strength != null && strength.length() > 0) {
                        dbSubTitle = dbSubTitle.concat("-").concat(strength);
                    }
                    ApplicationDB.get().updateAlarmSubTitle(identifier, dbSubTitle);
                }

                String drugAdherenceDtoStr = cursor.getString(cursor.getColumnIndex(AlarmTable.DRUG_ADHERENCE_DTO));
                if (drugAdherenceDtoStr != null) {

                    Gson gson = new Gson();
                    Type drugAdherenceDtoType = new TypeToken<List<DrugDosageAdherenceDTO>>() {
                    }.getType();
                    List<DrugDosageAdherenceDTO> adherenceDTOList = gson.fromJson(drugAdherenceDtoStr, drugAdherenceDtoType);


                    adherenceDTOList.addAll(drugDosageAdherenceDTOList);
                    ApplicationDB.get().updateAdherenceDto(identifier, gson.toJson(adherenceDTOList));
                }
            }
        }
    }

    private static void setAlarmForMonthly(Long userDrugId, String drugName, ReminderDto reminderDto,
                                           Long userId, String startDateStr,
                                           UserDrugDto userDrugDto, UserDrugDto oldUserDrugDto,
                                           Context context, String dosage) {

        try {
            Long userDrugDosageId = reminderDto.getId();
            String scheduleTime = reminderDto.getReminderTime();

            DrugDosageAdherenceDTO drugDosageAdherenceDTO = new DrugDosageAdherenceDTO();

            drugDosageAdherenceDTO.setUserDrugId(userDrugId);
            drugDosageAdherenceDTO.setUserDrugDosageId(userDrugDosageId);
            drugDosageAdherenceDTO.setTime(scheduleTime);
            drugDosageAdherenceDTO.setDrugName(drugName);

            List<DrugDosageAdherenceDTO> dosageAdherenceDTOList = new ArrayList<>();
            dosageAdherenceDTOList.add(drugDosageAdherenceDTO);


            String day = reminderDto.getValue();

            Calendar calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);
            long interval = getIntervalTime(currentMonth, currentYear);

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

            String timeStr = reminderDto.getReminderTime();

            String[] timeSplit = timeStr.split(":");
            String hourStr = timeSplit[0];
            int hour = (Integer.parseInt(hourStr));
//            String min = "05";
            String min = "00";
            String sec = "00";

            String time = hour + ":" + min + ":" + sec;


            String dateTime = startDateStr + " " + time;
            SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = sDFormat.parse(dateTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(formattedDate);


       /* calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);*/


            String[] split = time.split(":");


            String uId = userDrugDto.getUserId() + split[0] + min + sec;
            String identifier = uId; //userId + time int


            boolean isTimeExist = ApplicationDB.get().isAlarmExistForMonthTime(time, day);
            if (isTimeExist) {

                // Time is exist update db for same time and same weekday
                updateAlarmDB(identifier, time, startDateStr, userDrugDto, oldUserDrugDto, day,
                        dosageAdherenceDTOList, dosage);

            } else {
                // Time is not exist add into db and set alarm
                updateAlarmDB(identifier, time, startDateStr, userDrugDto, oldUserDrugDto, day,
                        dosageAdherenceDTOList, dosage);

                if (AppUtils.isTimePassed(time)) {
                    cal.add(Calendar.DATE, 1);
                }

                setAlarmForTime(context, time, cal.getTimeInMillis(), interval, Constants.MONTHLY, day,"");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

    }
    private static long getIntervalTime(int currentMonth, int currentYear) {
        if (currentMonth == Calendar.JANUARY || currentMonth == Calendar.MARCH || currentMonth == Calendar.MAY || currentMonth == Calendar.JULY
                || currentMonth == Calendar.AUGUST || currentMonth == Calendar.OCTOBER || currentMonth == Calendar.DECEMBER) {
            return AlarmManager.INTERVAL_DAY * 31;
        }
        if (currentMonth == Calendar.APRIL || currentMonth == Calendar.JUNE || currentMonth == Calendar.SEPTEMBER
                || currentMonth == Calendar.NOVEMBER) {
            return AlarmManager.INTERVAL_DAY * 30;
        }

        if (currentMonth == Calendar.FEBRUARY) {
            GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
            if (cal.isLeapYear(currentYear)) {
                return AlarmManager.INTERVAL_DAY * 29;
            } else {
                return AlarmManager.INTERVAL_DAY * 28;

            }
        }

        return -1;
    }

    public static void logg(String key, String data){
        if(BuildConfig.DEBUG){
            Log.i(key,data);
        }
    }
    public static void setAlarmForTime(Context context, String time, long timeInMillis,
                                       long intervalTime, String reminderType, String dataToBeSend,String endDate) {
        // set alarm

        //Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        Log.i("checkmodeldashb", " endDate 2  : " + endDate);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        notificationIntent.putExtra(Constants.TIME, time);
        notificationIntent.putExtra(Constants.REMINDER_TYPE, reminderType);
        notificationIntent.putExtra(Constants.TODAY_DATE, getTodayDate());
        notificationIntent.putExtra(Constants.END_DATE, endDate);
        notificationIntent.setClass(context, AlarmReceiver.class);

        if (reminderType.equalsIgnoreCase(context.getString(R.string.weekly))) {
            notificationIntent.putExtra(Constants.DATA_PARA, dataToBeSend);
        } else if (reminderType.equalsIgnoreCase(context.getString(R.string.monthly))) {
            notificationIntent.putExtra(Constants.DATA_PARA, dataToBeSend);
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        String[] split = time.split(":");

        String requestCodeStr = split[0] + split[1] + split[2];
        int requestCode = Integer.parseInt(requestCodeStr);
        Log.e("requestCode", requestCode + "");

        PendingIntent broadcast;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i("checkmodeldashb","upload data api S requestCode =");
            broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            Log.i("checkmodeldashb","upload data api not S requestCode =");
            broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalTime, broadcast);
//        }

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= 31) {
                Log.i("checkmodeldashb","upload data api alarmManager = timeInMillis = "+String.valueOf(timeInMillis)+", intervalTime = "+String.valueOf(intervalTime));
                if (alarmManager.canScheduleExactAlarms()) {
                    Log.i("checkmodeldashb","upload data api alarmManager 2 = ");
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalTime, broadcast);
                } else {
                    Log.i("checkmodeldashb","upload data api alarmManager 1 = ");
                    Intent intent = new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    context.startActivity(intent);
                }
            } else {
                Log.i("checkmodeldashb","upload data api alarmManager 3 = ");
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalTime, broadcast);
            }
        }
    }

    private static void setAlarmForWeekly(Long userDrugId, String drugName, ReminderDto reminderDto,
                                          Long userId, String startDateStr, UserDrugDto userDrugDto,
                                          UserDrugDto oldUserDrugDto, Context context, String dosage) {


        try {
            Long userDrugDosageId = reminderDto.getId();
            String scheduleTime = reminderDto.getReminderTime();

            DrugDosageAdherenceDTO drugDosageAdherenceDTO = new DrugDosageAdherenceDTO();

            drugDosageAdherenceDTO.setUserDrugId(userDrugId);
            drugDosageAdherenceDTO.setDrugName(drugName);
            drugDosageAdherenceDTO.setUserDrugDosageId(userDrugDosageId);
            drugDosageAdherenceDTO.setTime(scheduleTime);

            List<DrugDosageAdherenceDTO> dosageAdherenceDTOList = new ArrayList<>();
            dosageAdherenceDTOList.add(drugDosageAdherenceDTO);

            String weekDayName = reminderDto.getValue();

            Calendar calendar = Calendar.getInstance();


            if (weekDayName.equalsIgnoreCase("monday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 2);
            } else if (weekDayName.equalsIgnoreCase("tuesday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 3);
            } else if (weekDayName.equalsIgnoreCase("wednesday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 4);
            } else if (weekDayName.equalsIgnoreCase("thursday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 5);
            } else if (weekDayName.equalsIgnoreCase("friday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 6);
            } else if (weekDayName.equalsIgnoreCase("saturday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 7);
            } else if (weekDayName.equalsIgnoreCase("sunday")) {
                calendar.set(Calendar.DAY_OF_WEEK, 1);
            }


            String timeStr = reminderDto.getReminderTime();


            String[] timeSplit = timeStr.split(":");
            String hourStr = timeSplit[0];
            int hour = (Integer.parseInt(hourStr));
//            int min = 3;
//            int min = 0;
            String min = "00";
            String sec = "00";

//            String time = hour + ":" + "0" + min + ":" + sec;
            String time = hour + ":" + min + ":" + sec;

            calendar.set(Calendar.HOUR_OF_DAY, hour);
//            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.MINUTE, Integer.parseInt(min));
            calendar.set(Calendar.SECOND, Integer.parseInt(sec));

            String[] split = time.split(":");

            String uId = userId + split[0] + min + sec;
            String identifier = uId; //userId + time int
            Log.e("add_identifier", identifier);

            String dateTime = startDateStr + " " + time;
            SimpleDateFormat sDFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = sDFormat.parse(dateTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(formattedDate);


            boolean isTimeExist = ApplicationDB.get().isAlarmExistForSameTime(time, weekDayName);
            if (isTimeExist) {

                // Time is exist update db for same time and same weekday
                updateAlarmDB(identifier, time, startDateStr, userDrugDto, oldUserDrugDto, weekDayName,
                        dosageAdherenceDTOList, dosage);

            } else {
                // Time is not exist add into db and set alarm
                updateAlarmDB(identifier, time, startDateStr, userDrugDto, oldUserDrugDto, weekDayName,
                        dosageAdherenceDTOList, dosage);

                if (AppUtils.isTimePassed(time)) {
                    cal.add(Calendar.DATE, 1);
                }

                setAlarmForTime(context, time, cal.getTimeInMillis(), WEEKLY_INTERVAL, Constants.WEEKLY, weekDayName,"");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static void callTodayAgendaAPI(Context context, UserDto userDto) {
        if (userDto != null && context != null) {
            //showProgressBar();
            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(context);
            GenericRequest<UserAgendaDto> todayAgendaRequest = new GenericRequest<UserAgendaDto>
                    (Request.Method.GET, APIUrls.get().getTodayAgenda(userDto.getId(), AppUtils.getTodayDateForSchedule()),
                            UserAgendaDto.class, null,
                            new Response.Listener<UserAgendaDto>() {
                                @Override
                                public void onResponse(UserAgendaDto userAgendaDto) {
                                    authExpiredCallback.hideProgressBar();
                                    ApplicationDB.get().updateDashboardTable(userDto.getId(), userAgendaDto);
                                    Log.e("TodayAgendaFromNoti", new Gson().toJson(userAgendaDto));

                                    Intent intent = new Intent();
                                    intent.setAction(Constants.UPDATE_TODAYS_AGENDA);
                                    context.sendBroadcast(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    authExpiredCallback.hideProgressBar();
                                    String res = AppUtils.getVolleyError(context, error, authExpiredCallback);
                                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                                }
                            });
            authExpiredCallback.setRequest(todayAgendaRequest);
            ApiService.get().addToRequestQueue(todayAgendaRequest);
        }
    }

    public static String getVolleyErrorForNoti(Context context, VolleyError error, AuthExpiredCallback authExpiredCallback) {
        if (context != null) {
            if (error instanceof TimeoutError) {
                return context.getString(R.string.time_our_error);
            } else if (error instanceof NoConnectionError) {
                return context.getString(R.string.can_not_connect);
            } else if (error instanceof AuthFailureError) {
                if (error.networkResponse.statusCode == 401) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("message").toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (authExpiredCallback != null) {
                        authExpiredCallback.onAuthExpired(true);
                    }
                    return null;
                }
                return context.getString(R.string.auth_fail);
            } else if (error instanceof ServerError) {
                if (error.networkResponse.statusCode == 400 ||
                        error.networkResponse.statusCode == 401 ||
                        error.networkResponse.statusCode == 404 ||
                        error.networkResponse.statusCode == 422) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("message").toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return srt;
                }
                return context.getString(R.string.server_error);
            } else if (error instanceof NetworkError) {
                return context.getString(R.string.netwotk_error);
            } else if (error instanceof ParseError) {
                return context.getString(R.string.parser_error);
            }

            String msg = error.getMessage();

            if (msg != null && msg.trim().length() > 0) {
                return msg;
            }
            return context.getString(R.string.unknow_error_txt);
        } else {
            return "Unknown Error";
        }
    }

    public static void logReminderAdded(String drugName, List<DosageDto> userDrugDosagesList) {
       /* AppEventsLogger logger = Analytics.get();

        if (logger != null && isProdBuild()) {
            if (userDrugDosagesList != null && userDrugDosagesList.size() > 0) {
                for (int i = 0; i < userDrugDosagesList.size(); i++) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DRUG_NAME, drugName);
                    bundle.putString(Constants.TIME, userDrugDosagesList.get(i).getReminderTime());
                    logger.logEvent(Constants.PILL_REMINDER_ADDED, bundle);
                }
            }
        }*/
    }
    public static void setSelectedDate(DatePickerDialog datePickerDialog, String selectedDate) {
        try {
            if (selectedDate != null && selectedDate.length() > 0) {
                String[] dateArray = selectedDate.split("/");
                if (dateArray != null) {
                    String monthStr = dateArray[0];
                    String day = dateArray[1];
                    String year = dateArray[2];

                    int month = Integer.parseInt(monthStr) - 1;
                    datePickerDialog.updateDate(Integer.parseInt(year), month, Integer.parseInt(day));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static List<MonthlyDays> getMonthlyList(List<String> selectedList) {
        List<MonthlyDays> monthlyDaysList = new ArrayList<>();
        MonthlyDays monthlyDays;
        for (int i = 1; i <= 31; i++) {
            monthlyDays = new MonthlyDays();
            monthlyDays.setDay(i);

            if (i == 1) {
                monthlyDays.setDisplayName("1st");
            } else if (i == 2) {
                monthlyDays.setDisplayName("2nd");
            } else if (i == 3) {
                monthlyDays.setDisplayName("3rd");
            } else if (i == 21) {
                monthlyDays.setDisplayName("21st");
            } else if (i == 22) {
                monthlyDays.setDisplayName("22nd");
            } else if (i == 23) {
                monthlyDays.setDisplayName("23rd");
            } else if (i == 31) {
                monthlyDays.setDisplayName("31st");
            } else {
                monthlyDays.setDisplayName(i + "th");
            }

            if (selectedList == null) {
                if (i == 1) {
                    monthlyDays.setSelected(true);
                }
            } else {
                for (int j = 0; j < selectedList.size(); j++) {
                    if (String.valueOf(i).equalsIgnoreCase(selectedList.get(j))) {
                        monthlyDays.setSelected(true);
                    }
                }
            }

            monthlyDaysList.add(monthlyDays);
        }

        return monthlyDaysList;
    }
    public static List<WeeklyDays> getWeeklyList(List<String> selectedList) {
        List<WeeklyDays> weeklyDaysList = new ArrayList<>();
        WeeklyDays weeklyDays;
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] weekdays = dfs.getWeekdays();
        String[] shortWeekdays = dfs.getShortWeekdays();

        for (int i = 1; i < weekdays.length; i++) {
            String day = weekdays[i];
            weeklyDays = new WeeklyDays();
            weeklyDays.setDisplayFormat(shortWeekdays[i]);
            weeklyDays.setFullName(weekdays[i]);
            weeklyDays.setDayNum(i);

            if (selectedList == null) {
                if (i == 1) {
                    weeklyDays.setSelected(true);
                }
            } else {
                for (int j = 0; j < selectedList.size(); j++) {
                    if (day.equalsIgnoreCase(selectedList.get(j))) {
                        weeklyDays.setSelected(true);
                    }
                }
            }

            weeklyDaysList.add(weeklyDays);
        }
        return weeklyDaysList;
    }
    public static void setDialogBoxButton(Context context, DatePickerDialog datePickerDialog) {
        if (context != null && datePickerDialog != null) {
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, context.getString(R.string.ok), datePickerDialog);
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        }
    }
    public static void callDosageDropDownAPI(Context context) {
        AppUtils.callTabletAPI(context);
        AppUtils.callLiquidAPI(context);
        AppUtils.callInjectionAPI(context);
    }
    public static void callTabletAPI(final Context context) {
        if (context != null) {
            if (ApplicationPreferences.get().getDosageList(Constants.TABLET) == null) {
                GenericRequestWithoutAuth<DosageDropDownDto.DosageDropDownDtoList> tabletRequest = new GenericRequestWithoutAuth<DosageDropDownDto.DosageDropDownDtoList>
                        (Request.Method.GET, APIUrls.get().getDosage(Constants.TABLET),
                                DosageDropDownDto.DosageDropDownDtoList.class, null,
                                new Response.Listener<DosageDropDownDto.DosageDropDownDtoList>() {
                                    @Override
                                    public void onResponse(DosageDropDownDto.DosageDropDownDtoList dosageDropDownDtoList) {
                                        ApplicationPreferences.get().setDosageData(Constants.TABLET, dosageDropDownDtoList);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String volleyError = getVolleyError(context, error);
                                        if (volleyError != null)
                                            Log.e("TabletApiFailure", volleyError);
                                    }
                                });
                ApiService.get().addToRequestQueue(tabletRequest);
            }
        }
    }
    public static void callRefillReminderAPI(final Context context) {
        Log.i("checkSearchText","updateRefillData api url = "+APIUrls.get().prescriptionRefill().toString());
        if (ApplicationPreferences.get().getRemindRefillData() == null) {
            GenericRequestWithoutAuth<PrescriptionRefillDto.PrescriptionRefillDtoList> addDrugRequest = new GenericRequestWithoutAuth<PrescriptionRefillDto.PrescriptionRefillDtoList>
                    (Request.Method.GET, APIUrls.get().prescriptionRefill(),
                            PrescriptionRefillDto.PrescriptionRefillDtoList.class, null,
                            new Response.Listener<PrescriptionRefillDto.PrescriptionRefillDtoList>() {
                                @Override
                                public void onResponse(PrescriptionRefillDto.PrescriptionRefillDtoList remindRefillList) {
                                    Log.i("checkSearchText","updateRefillData api response = "+remindRefillList.toString());
                                    ApplicationPreferences.get().setRefillData(remindRefillList);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("checkSearchText","updateRefillData api error = "+error.getMessage().toString());
                                    String volleyError = getVolleyError(context, error);
                                    if (volleyError != null)
                                        Log.e("RefillReminderFailure", volleyError);
                                }
                            });
            ApiService.get().addToRequestQueue(addDrugRequest);
        }
    }
    public static void callLiquidAPI(final Context context) {
        if (context != null) {
            if (ApplicationPreferences.get().getDosageList(Constants.LIQUID) == null) {
                GenericRequestWithoutAuth<DosageDropDownDto.DosageDropDownDtoList> liquidRequest = new GenericRequestWithoutAuth<DosageDropDownDto.DosageDropDownDtoList>
                        (Request.Method.GET, APIUrls.get().getDosage(Constants.LIQUID),
                                DosageDropDownDto.DosageDropDownDtoList.class, null,
                                new Response.Listener<DosageDropDownDto.DosageDropDownDtoList>() {
                                    @Override
                                    public void onResponse(DosageDropDownDto.DosageDropDownDtoList dosageDropDownDtoList) {
                                        ApplicationPreferences.get().setDosageData(Constants.LIQUID, dosageDropDownDtoList);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String volleyError = getVolleyError(context, error);
                                        if (volleyError != null)
                                            Log.e("LiquidAPIFailure", volleyError);
                                    }
                                });
                ApiService.get().addToRequestQueue(liquidRequest);
            }
        }
    }

    public static void callInjectionAPI(final Context context) {
        if (context != null) {
            if (ApplicationPreferences.get().getDosageList(Constants.INJECTION) == null) {
                GenericRequestWithoutAuth<DosageDropDownDto.DosageDropDownDtoList> injectionRequest = new GenericRequestWithoutAuth<DosageDropDownDto.DosageDropDownDtoList>
                        (Request.Method.GET, APIUrls.get().getDosage(Constants.INJECTION),
                                DosageDropDownDto.DosageDropDownDtoList.class, null,
                                new Response.Listener<DosageDropDownDto.DosageDropDownDtoList>() {
                                    @Override
                                    public void onResponse(DosageDropDownDto.DosageDropDownDtoList dosageDropDownDtoList) {
                                        ApplicationPreferences.get().setDosageData(Constants.INJECTION, dosageDropDownDtoList);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String volleyError = getVolleyError(context, error);
                                        if (volleyError != null)
                                            Log.e("InjectionAPI", volleyError);
                                    }
                                });
                ApiService.get().addToRequestQueue(injectionRequest);
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    public static boolean getFitBitAPIFlag() {
        boolean callAPI = false;
        String dateString = ApplicationPreferences.get()
                .getStringValue(Constants.LAST_FITBIT_PARAMETER_UPDATE_TIME);
        if (dateString == null || dateString.matches("")) {
            callAPI = true;
        } else {
            DateFormat simpleDateFormat = new SimpleDateFormat(Constants.LOCAL_TIME_FORMAT);
            ParsePosition position = new ParsePosition(0);
            Date lastDate = null;
            lastDate = simpleDateFormat.parse(dateString, position);

            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            if (lastDate != null) {
                long millisDifference = currentDate.getTime() - lastDate.getTime();
                long diffMinites = (millisDifference / 1000) / 60;

                if (diffMinites >= 150) {
                    callAPI = true;
                }
            } else {
                callAPI = true;
            }
        }
        return callAPI;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static UserDto getLoggedInUser() {
        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto userDto = loginResponse.getUserDTO();
            if (userDto != null) {
                return userDto;
            }
        }
        return null;
    }

    public static String pastDateBy3Months() {

        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        c.add(Calendar.MONTH, -2);
        c.add(Calendar.DATE, -1);
        c.add(Calendar.HOUR, 1);
        c.add(Calendar.MINUTE, 1);
        c.add(Calendar.SECOND, 1);

        Date currentDatePlusOne = c.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(c.getTime());
    }

    private static void callGetFitBitWaterDataApi(boolean isFromMyCondition) {
        String isWaterSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WATER_SWITCH);
        if (isWaterSwitchOn != null && isWaterSwitchOn.equalsIgnoreCase("false")) {
            callUpdateParameterApi(isFromMyCondition);
        } else {
            if (mAccessToken != null && !mAccessToken.equalsIgnoreCase("")) {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + mAccessToken);
                String startDate;
                startDate = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WATER_START_DATE);
                Gson userProfileGson = new Gson();
                String userProfileString = ApplicationPreferences.get().getStringValue(Constants.FITBIT_USER_PROFILE);
                if (startDate != null && startDate.equalsIgnoreCase("")) {
                    Type TypeUserProfile = new TypeToken<JsonObject>() {
                    }.getType();
                    JsonObject userProfile = userProfileGson.fromJson(userProfileString, TypeUserProfile);
                    if (userProfile.get("user").getAsJsonObject() != null) {
                        startDate = userProfile.get("user").getAsJsonObject().get("memberSince").getAsString();
                    }
                }
                startDate = AppUtils.pastDateBy3Months();
                String endDate = AppUtils.getTodayDateWithFormat();
                GenericRequestWithoutAuth<Object> profileRequest = new GenericRequestWithoutAuth<>(
                        Request.Method.GET, "https://api.fitbit.com/1/user/-/foods/log/water/date/" + startDate + "/" + endDate + ".json", Object.class, null,
                        response -> {
                            Gson gson = new Gson();
                            String userWaterString = gson.toJson(response);
                            Type TypeWater = new TypeToken<JsonObject>() {
                            }.getType();
                            JsonObject water = userProfileGson.fromJson(userWaterString, TypeWater);
                            JsonArray waterArray = water.getAsJsonArray("foods-log-water");
                            FitBitMultipleParameterDto fitBitMultipleParameterDto = new FitBitMultipleParameterDto();
                            List<FitBitMultipleParameterDto.FitBitMultipleParameter> fitBitMultipleParameters = new ArrayList<>();
                            if (waterArray != null) {
                                for (int i = 0; i < waterArray.size(); i++) {
                                    JsonObject jsonObject = waterArray.get(i).getAsJsonObject();
                                    if (jsonObject != null) {
                                        FitBitMultipleParameterDto.FitBitMultipleParameter fitBitMultipleParameter
                                                = new FitBitMultipleParameterDto.FitBitMultipleParameter();
                                        fitBitMultipleParameter.setMaxBaselineDisplayName("Water Intake");
                                        fitBitMultipleParameter.setMaxBaselineValue((jsonObject.get("value").getAsDouble()) / 250);
                                        fitBitMultipleParameter.setMeasurementUnit("Number of glasses");
                                        fitBitMultipleParameter.setRecordedDate(jsonObject.get("dateTime").getAsString() + " " + "00:00:00");
                                        fitBitMultipleParameter.setSource("fitbit");
                                        fitBitMultipleParameters.add(fitBitMultipleParameter);
                                        ApplicationPreferences.get().saveStringValue(Constants.FITBIT_WATER_START_DATE,
                                                jsonObject.get("dateTime").getAsString());


                                    }
                                }

                                JsonObject jsonObject;
                                if (waterArray.size() >= 1)
                                    jsonObject = waterArray.get(waterArray.size() - 1).getAsJsonObject();
                                else
                                    jsonObject = waterArray.get(0).getAsJsonObject();
                                HashMap<String, Object> fitbitMap = new HashMap<>();
                                fitbitMap.put(Constants.HEALTH_MATRICS_NAME, "Water Intake");
                                fitbitMap.put(Constants.VALUE, "Min = 0" + "Max =" + jsonObject.get("value").getAsDouble());
                                fitbitMap.put(Constants.UNIT, "Number of glasses");
                                fitbitMap.put(Constants.DATE, AppUtils.getTodayDate());
                                fitbitMap.put(Constants.TIME, AppUtils.getCurrentTime());
                                AppUtils.logCleverTapEvent(mContext, Constants.HEALTH_METRICS_TRACK_READING_UPDATED_ON_TRACK_SCREEN, fitbitMap);


                                fitBitMultipleParameterDto.setParameterId(225L);
                                fitBitMultipleParameterDto.setReadings(fitBitMultipleParameters);
                                fitBitMultipleParameterDtos.add(fitBitMultipleParameterDto);
                            }
                            callUpdateParameterApi(isFromMyCondition);
                        }, error -> isFitBitApiRunning = false, headers);
                ApiService.get().addToRequestQueue(profileRequest);
            }
        }
    }
    private static void callUpdateParameterApi(boolean isFromMyCondition) {
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(mContext);
        UserDto userDto = ApplicationPreferences.get().getUserDetails().getUserDTO();
        if (userDto != null) {
            GenericRequest<FitBitMultipleParameterDto.FitBitMultipleParameterIdsDtoList> todayAgendaRequest = new GenericRequest<>
                    (Request.Method.POST, APIUrls.get().updateFitBitMultipleParameter(userDto.getId()),
                            FitBitMultipleParameterDto.FitBitMultipleParameterIdsDtoList.class, fitBitMultipleParameterDtos,
                            fitBitMultipleParameterResponse -> {
                                isFitBitApiRunning = false;
                                if (fitBitMultipleParameterResponse != null &&
                                        fitBitMultipleParameterResponse.size() != 0 && !isFromMyCondition) {
                                    StringBuilder message = new StringBuilder();
                                    message.append(mContext.getString(R.string.dear)).append(" ").append(userDto.getName()).
                                            append(mContext.getString(R.string.fitbit_notification_message)).append(" ");
                                    boolean stringAdded = false;
                                    for (int i = 0; i < fitBitMultipleParameterResponse.size(); i++) {
                                        if (fitBitMultipleParameterResponse.get(i) == 414) {
                                            message.append(mContext.getString(R.string.heart_rate));
                                            stringAdded = true;
                                        } else if (fitBitMultipleParameterResponse.get(i) == 3) {
                                            message.append(mContext.getString(R.string.resting_heart_rate));
                                            stringAdded = true;
                                        } else if (fitBitMultipleParameterResponse.get(i) == 2) {
                                            message.append(mContext.getString(R.string.weight));
                                            stringAdded = true;
                                        } else if (fitBitMultipleParameterResponse.get(i) == 225) {
                                            message.append(mContext.getString(R.string.water_intake));
                                            stringAdded = true;
                                        } else if (fitBitMultipleParameterResponse.get(i) == 272) {
                                            message.append(mContext.getString(R.string.hours_of_sleep));
                                            stringAdded = true;
                                        }

                                        if (stringAdded && i != fitBitMultipleParameterResponse.size() - 1) {
                                            if (i == fitBitMultipleParameterResponse.size() - 2) {
                                                message.append(" ").append(mContext.getString(R.string.and)).append(" ");
                                            } else {
                                                message.append(", ");
                                            }
                                            stringAdded = false;
                                        }
                                    }
                                    AppUtils.showNotification(mContext, message.toString(), "");
                                    saveTheCurrentTimeString();
                                }
                            },
                            error -> {
                                isFitBitApiRunning = false;
                                AppUtils.hideKeyboard((Activity) mContext);
                            });
            authExpiredCallback.setRequest(todayAgendaRequest);
            ApiService.get().addToRequestQueue(todayAgendaRequest);
        }
    }
    @SuppressLint("SimpleDateFormat")
    private static void saveTheCurrentTimeString() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(Constants.LOCAL_TIME_FORMAT);
        String currentTimeString = simpleDateFormat.format(date);
        ApplicationPreferences.get().saveStringValue(Constants.LAST_FITBIT_PARAMETER_UPDATE_TIME,
                currentTimeString);
    }
    public static void showNotification(Context context, String title,
                                        String time) {
        Intent notificationIntent = new Intent(context, HomeActivity.class);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(notificationIntent);


        int num = (int) System.currentTimeMillis();

        if (num < 0) {
            num = num * -1;
        }

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = stackBuilder.getPendingIntent(num, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = stackBuilder.getPendingIntent(num, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String id = "notification_channel_id";
        // The user-visible name of the channel.
        CharSequence name = "Message_Notification";
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_new);


        Intent takenIntent = new Intent(context, NotificationReceiver.class);
        takenIntent.setAction(Constants.TAKEN);
        takenIntent.putExtra(Constants.TIME, time);
        takenIntent.putExtra(Constants.SCHEDULE_DATE, AppUtils.getTodayDateForSchedule());
        takenIntent.putExtra(Constants.NOTI_ID, num);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, id)
                .setAutoCancel(true)
                .setSound(uri)
                .setGroupSummary(true)
                .setGroup(GROUP_KEY)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setChannelId(id);

        notificationBuilder.setContentText(title + context.getResources()
                .getString(R.string.space) + AppUtils.get12HrsDate(time));
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(title + context.getResources()
                .getString(R.string.space) + AppUtils.get12HrsDate(time)));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_kym_logo);
            notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_new);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            notificationBuilder.setLargeIcon(bm);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // The user.-visible description of the channel.
            String description = context.getString(R.string.messages_sent_by_kym_team);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        // Sets an ID for the notification

        // Gets an instance of the NotificationManager service
        final NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(num, notificationBuilder.build());
        }
    }
    private static void callGetFitBitSleepDataApi(boolean isFromMyCondition) {
        String isSleepSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_SLEEP_SWITCH);
        if (isSleepSwitchOn != null && isSleepSwitchOn.equalsIgnoreCase("false")) {
            callGetFitBitWaterDataApi(isFromMyCondition);
        } else {
            if (mAccessToken != null && !mAccessToken.equalsIgnoreCase("")) {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + mAccessToken);
                String startDate;
                startDate = ApplicationPreferences.get().getStringValue(Constants.FITBIT_SLEEP_START_DATE);
                Gson userProfileGson = new Gson();
                String userProfileString = ApplicationPreferences.get().getStringValue(Constants.FITBIT_USER_PROFILE);
                if (startDate != null && startDate.equalsIgnoreCase("")) {
                    Type TypeUserProfile = new TypeToken<JsonObject>() {
                    }.getType();
                    JsonObject userProfile = userProfileGson.fromJson(userProfileString, TypeUserProfile);
                    if (userProfile.get("user").getAsJsonObject() != null) {
                        startDate = userProfile.get("user").getAsJsonObject().get("memberSince").getAsString();
                    }
                }
                startDate = AppUtils.pastDateBy3Months();
                String endDate = AppUtils.getTodayDateWithFormat();
                GenericRequestWithoutAuth<Object> profileRequest = new GenericRequestWithoutAuth<>(
//                        Request.Method.GET, "https://api.fitbit.com/1.2/user/-/sleep/date/" + startDate + "/" + endDate + ".json", Object.class, null,
                        Request.Method.GET, "https://api.fitbit.com/1.2/user/-/sleep/date/" + startDate + "/" + endDate + ".json", Object.class, null,
                        response -> {
                            Gson gson = new Gson();
                            String userSleepString = gson.toJson(response);
                            ApplicationPreferences.get().saveStringValue("FitBit-Sleep-Data", userSleepString);
                            Type TypeSleep = new TypeToken<JsonObject>() {
                            }.getType();
                            JsonObject sleep = userProfileGson.fromJson(userSleepString, TypeSleep);
                            JsonArray sleepArray = sleep.getAsJsonArray("sleep");
                            FitBitMultipleParameterDto fitBitMultipleParameterDto = new FitBitMultipleParameterDto();
                            List<FitBitMultipleParameterDto.FitBitMultipleParameter> fitBitMultipleParameters = new ArrayList<>();
                            if (sleepArray != null) {
                                Log.e("sleepArray = ", " " + new Gson().toJson(sleepArray) + "size =" + sleepArray.size());
                                for (int i = 0; i < sleepArray.size(); i++) {
                                    JsonObject jsonObject = sleepArray.get(i).getAsJsonObject();
                                    if (jsonObject != null) {
                                        FitBitMultipleParameterDto.FitBitMultipleParameter fitBitMultipleParameter
                                                = new FitBitMultipleParameterDto.FitBitMultipleParameter();
                                        fitBitMultipleParameter.setMaxBaselineDisplayName("Hours of sleep during night");
                                        fitBitMultipleParameter.setMaxBaselineValue(jsonObject.get("minutesAsleep").getAsDouble() / 60);
                                        fitBitMultipleParameter.setMeasurementUnit("Hours");
                                        String startDateTime = jsonObject.get("startTime").getAsString().split("T")[0];
                                        String time = (jsonObject.get("startTime").getAsString().split("T")[1]).split("\\.")[0];
                                        fitBitMultipleParameter.setRecordedDate(startDateTime + " " + time);
                                        fitBitMultipleParameter.setSource("fitbit");
                                        fitBitMultipleParameters.add(fitBitMultipleParameter);
                                        ApplicationPreferences.get().saveStringValue(Constants.FITBIT_SLEEP_START_DATE,
                                                jsonObject.get("startTime").getAsString().split("T")[0]);
                                    }
                                }
                                fitBitMultipleParameterDto.setParameterId(272L);
                                fitBitMultipleParameterDto.setReadings(fitBitMultipleParameters);
                                fitBitMultipleParameterDtos.add(fitBitMultipleParameterDto);


                                if (sleepArray.size() != 0) {
                                    JsonObject jsonObject;
                                    if (sleepArray.size() >= 1)
                                        jsonObject = sleepArray.get(sleepArray.size() - 1).getAsJsonObject();
                                    else
                                        jsonObject = sleepArray.get(0).getAsJsonObject();

                                    HashMap<String, Object> fitbitMap = new HashMap<>();
                                    fitbitMap.put(Constants.HEALTH_MATRICS_NAME, "Hours of sleep during night");
                                    fitbitMap.put(Constants.VALUE, "Min = 0" + "Max =" + jsonObject.get("minutesAsleep").getAsDouble());
                                    fitbitMap.put(Constants.UNIT, "Hours");
                                    fitbitMap.put(Constants.DATE, AppUtils.getTodayDate());
                                    fitbitMap.put(Constants.TIME, AppUtils.getCurrentTime());
                                    AppUtils.logCleverTapEvent(mContext, Constants.HEALTH_METRICS_TRACK_READING_UPDATED_ON_TRACK_SCREEN, fitbitMap);
                                }
                            }

                            callGetFitBitWaterDataApi(isFromMyCondition);
                        }, error -> isFitBitApiRunning = false, headers);
                ApiService.get().addToRequestQueue(profileRequest);
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    private static void callGetFitBitHeartRateDataApi(boolean isFromMyCondition) {
        String isHeartRateSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_HEART_RATE_SWITCH);
        String isRestingHeartRateSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_RESTING_HEART_SWITCH);
        if (isHeartRateSwitchOn != null && isHeartRateSwitchOn.equalsIgnoreCase("false") &&
                isRestingHeartRateSwitchOn != null && isRestingHeartRateSwitchOn.equalsIgnoreCase("false")) {
            callGetFitBitSleepDataApi(isFromMyCondition);
        } else {
            if (mAccessToken != null && !mAccessToken.equalsIgnoreCase("")) {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + mAccessToken);
                String startDateHeartRate, startDateRestingHeartRate, finalDate = "";
                startDateHeartRate = ApplicationPreferences.get()
                        .getStringValue(Constants.FITBIT_HEART_RATE_START_DATE);
                startDateRestingHeartRate = ApplicationPreferences.get()
                        .getStringValue(Constants.FITBIT_RESTING_HEART_RATE_START_DATE);
                Date heartRateDate = null, restingHeartRateDate = null;
                try {
                    heartRateDate = new
                            SimpleDateFormat("yyyy-MM-dd").parse(startDateHeartRate);
                    restingHeartRateDate = new
                            SimpleDateFormat("yyyy-MM-dd").parse(startDateRestingHeartRate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (heartRateDate != null && restingHeartRateDate != null) {
                    if (restingHeartRateDate.before(heartRateDate)) {
                        finalDate = startDateRestingHeartRate;
                    } else {
                        finalDate = startDateHeartRate;
                    }
                }
                Gson userProfileGson = new Gson();
                String userProfileString = ApplicationPreferences.get().getStringValue(Constants.FITBIT_USER_PROFILE);
                if (finalDate.equalsIgnoreCase("")) {
                    Type TypeUserProfile = new TypeToken<JsonObject>() {
                    }.getType();
                    JsonObject userProfile = userProfileGson.fromJson(userProfileString, TypeUserProfile);
                    if (userProfile.get("user").getAsJsonObject() != null) {
                        finalDate = userProfile.get("user").getAsJsonObject().get("memberSince").getAsString();
                    }
                }
                finalDate = AppUtils.pastDateBy3Months();
                String endDate = AppUtils.getTodayDateWithFormat();
                GenericRequestWithoutAuth<Object> profileRequest = new GenericRequestWithoutAuth<>(
                        Request.Method.GET, "https://api.fitbit.com/1/user/-/activities/heart/date/" + finalDate + "/" + endDate + ".json", Object.class, null,
                        response -> {
                            Gson gson = new Gson();
                            String userWeightString = gson.toJson(response);
                            Type TypeWeight = new TypeToken<JsonObject>() {
                            }.getType();
                            JsonObject heartRate = userProfileGson.fromJson(userWeightString, TypeWeight);
                            JsonArray heartRateArray = heartRate.getAsJsonArray("activities-heart");
                            FitBitMultipleParameterDto fitBitHeartRateDto = new FitBitMultipleParameterDto();
                            FitBitMultipleParameterDto fitBitRestingHeartRateDto = new FitBitMultipleParameterDto();
                            List<FitBitMultipleParameterDto.FitBitMultipleParameter>
                                    fitBitHearthRateParameters = new ArrayList<>();
                            List<FitBitMultipleParameterDto.FitBitMultipleParameter>
                                    fitBitRestingHearthRateParameters = new ArrayList<>();
                            if (heartRateArray != null) {
                                for (int i = 0; i < heartRateArray.size(); i++) {
                                    JsonObject json = heartRateArray.get(i).getAsJsonObject();
                                    JsonArray heartZonesArray = json.get("value").getAsJsonObject().get("heartRateZones").getAsJsonArray();
                                    if (heartZonesArray != null) {
                                        for (int j = 0; j < heartZonesArray.size(); j++) {
                                            JsonObject jsonObject = heartZonesArray.get(j).getAsJsonObject();
                                            if (jsonObject != null) {
                                                // Heart Rate Data
                                                if (jsonObject.get("name").getAsString().equalsIgnoreCase("Fat Burn") &&
                                                        isHeartRateSwitchOn != null && isHeartRateSwitchOn.equalsIgnoreCase("true")) {
                                                    FitBitMultipleParameterDto.FitBitMultipleParameter fitBitMultipleParameter
                                                            = new FitBitMultipleParameterDto.FitBitMultipleParameter();
                                                    fitBitMultipleParameter.setMaxBaselineDisplayName("Heart Rate");
                                                    fitBitMultipleParameter.setMaxBaselineValue(jsonObject.get("max").getAsDouble());
                                                    fitBitMultipleParameter.setMeasurementUnit("Beats per minute");
                                                    fitBitMultipleParameter.setRecordedDate(json.get("dateTime").getAsString() + " " + "00:00:00");
                                                    fitBitMultipleParameter.setSource("fitbit");
                                                    fitBitHearthRateParameters.add(fitBitMultipleParameter);

                                                    ApplicationPreferences.get().saveStringValue(Constants.FITBIT_HEART_RATE_START_DATE,
                                                            json.get("dateTime").getAsString().split("T")[0]);
                                                }
                                                // Resting Heart Rate Data
                                                if (jsonObject.get("name").getAsString().equalsIgnoreCase("Out of Range") &&
                                                        isRestingHeartRateSwitchOn != null && isRestingHeartRateSwitchOn.equalsIgnoreCase("true")) {
                                                    FitBitMultipleParameterDto.FitBitMultipleParameter fitBitMultipleParameter
                                                            = new FitBitMultipleParameterDto.FitBitMultipleParameter();
                                                    fitBitMultipleParameter.setMaxBaselineDisplayName("Resting Heart Rate");
                                                    fitBitMultipleParameter.setMaxBaselineValue(jsonObject.get("max").getAsDouble());
                                                    fitBitMultipleParameter.setMeasurementUnit("Beats per minute");
                                                    fitBitMultipleParameter.setRecordedDate(json.get("dateTime").getAsString() + " " + "00:00:00");
                                                    fitBitMultipleParameter.setSource("fitbit");
                                                    fitBitRestingHearthRateParameters.add(fitBitMultipleParameter);
                                                    ApplicationPreferences.get().saveStringValue(Constants.FITBIT_RESTING_HEART_RATE_START_DATE,
                                                            json.get("dateTime").getAsString().split("T")[0]);
                                                }
                                            }
                                        }

                                    }
                                }
                                // Heart Rate
                                fitBitHeartRateDto.setParameterId(414L);
                                fitBitHeartRateDto.setReadings(fitBitHearthRateParameters);
                                fitBitMultipleParameterDtos.add(fitBitHeartRateDto);

                                // Resting Heart Rate
                                fitBitRestingHeartRateDto.setParameterId(3L);
                                fitBitRestingHeartRateDto.setReadings(fitBitRestingHearthRateParameters);
                                fitBitMultipleParameterDtos.add(fitBitRestingHeartRateDto);
                            }
                            callGetFitBitSleepDataApi(isFromMyCondition);
                        }, error -> {
                    isFitBitApiRunning = false;
                }, headers);
                ApiService.get().addToRequestQueue(profileRequest);
            }
        }
    }



    private static void callGetFitBitWeightDataApi(boolean isFromMyCondition) {
        String isWeightSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WEIGHT_SWITCH);
        if (isWeightSwitchOn != null && isWeightSwitchOn.equalsIgnoreCase("false")) {
            callGetFitBitHeartRateDataApi(isFromMyCondition);
        } else {
            if (mAccessToken != null && !mAccessToken.equalsIgnoreCase("")) {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + mAccessToken);
                String startDate;
                startDate = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WEIGHT_START_DATE);
                Gson userProfileGson = new Gson();
                String userProfileString = ApplicationPreferences.get().getStringValue(Constants.FITBIT_USER_PROFILE);
                if (startDate != null && startDate.equalsIgnoreCase("")) {
                    Type TypeUserProfile = new TypeToken<JsonObject>() {
                    }.getType();
                    JsonObject userProfile = userProfileGson.fromJson(userProfileString, TypeUserProfile);
                    if (userProfile.get("user").getAsJsonObject() != null) {
                        startDate = userProfile.get("user").getAsJsonObject().get("memberSince").getAsString();
                    }
                }
                startDate = AppUtils.pastDateBy3Months();
                String endDate = AppUtils.getTodayDateWithFormat();
                GenericRequestWithoutAuth<Object> profileRequest = new GenericRequestWithoutAuth<>(
                        Request.Method.GET, "https://api.fitbit.com/1/user/-/body/weight/date/" + startDate + "/" + endDate + ".json", Object.class, null,
                        response -> {
                            Gson gson = new Gson();
                            String userWeightString = gson.toJson(response);
                            Type TypeWeight = new TypeToken<JsonObject>() {
                            }.getType();
                            JsonObject Weight = userProfileGson.fromJson(userWeightString, TypeWeight);
                            JsonArray weightArray = Weight.getAsJsonArray("body-weight");
                            FitBitMultipleParameterDto fitBitMultipleParameterDto = new FitBitMultipleParameterDto();
                            List<FitBitMultipleParameterDto.FitBitMultipleParameter> fitBitMultipleParameters = new ArrayList<>();
                            if (weightArray != null) {
                                for (int i = 0; i < weightArray.size(); i++) {
                                    JsonObject jsonObject = weightArray.get(i).getAsJsonObject();
                                    if (jsonObject != null) {
                                        FitBitMultipleParameterDto.FitBitMultipleParameter fitBitMultipleParameter
                                                = new FitBitMultipleParameterDto.FitBitMultipleParameter();
                                        fitBitMultipleParameter.setMaxBaselineDisplayName("Weight");
                                        fitBitMultipleParameter.setMaxBaselineValue(jsonObject.get("value").getAsDouble());
                                        fitBitMultipleParameter.setMeasurementUnit("kgs");
                                        fitBitMultipleParameter.setRecordedDate(jsonObject.get("dateTime").getAsString() + " " + "00:00:00");
                                        fitBitMultipleParameter.setSource("fitbit");
                                        fitBitMultipleParameters.add(fitBitMultipleParameter);
                                        ApplicationPreferences.get().saveStringValue(Constants.FITBIT_WEIGHT_START_DATE,
                                                jsonObject.get("dateTime").getAsString());
                                    }
                                }
                                fitBitMultipleParameterDto.setParameterId(2L);
                                fitBitMultipleParameterDto.setReadings(fitBitMultipleParameters);
                                fitBitMultipleParameterDtos.add(fitBitMultipleParameterDto);

                                JsonObject jsonObject = weightArray.get(weightArray.size() - 1).getAsJsonObject();
                                HashMap<String, Object> fitbitMap = new HashMap<>();
                                fitbitMap.put(Constants.HEALTH_MATRICS_NAME, "Weight");
                                fitbitMap.put(Constants.VALUE, "Min = 0" + "Max =" + jsonObject.get("value").getAsDouble());
                                fitbitMap.put(Constants.UNIT, "kgs");
                                fitbitMap.put(Constants.DATE, AppUtils.getTodayDate());
                                fitbitMap.put(Constants.TIME, AppUtils.getCurrentTime());
                                AppUtils.logCleverTapEvent(mContext, Constants.HEALTH_METRICS_TRACK_READING_UPDATED_ON_TRACK_SCREEN, fitbitMap);

                            }
                            callGetFitBitHeartRateDataApi(isFromMyCondition);
                        }, error -> isFitBitApiRunning = false, headers);
                ApiService.get().addToRequestQueue(profileRequest);
            }
        }
    }


    public static void addParameterDtoValue(Context context) {
        addHeartRateParameterDtoValue(context);
        addRestingHeartRateParameterDtoValue(context);
        addWeightParameterDtoValue(context);
        addWaterParameterDtoValue(context);
        addSleepParameterDtoValue(context);
    }

    public static void addHeartRateParameterDtoValue(Context context) {
        String isHeartRateSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_HEART_RATE_SWITCH);
        if (isHeartRateSwitchOn.equalsIgnoreCase("") || isHeartRateSwitchOn.equalsIgnoreCase("true")) {
            ParamDto paramDto = new ParamDto();
            paramDto.setFrequencyNumber(1.0);
            paramDto.setFrequencyUnit(Constants.DAY);
            paramDto.setId(414);
            paramDto.setLink("heart-rate");
            paramDto.setMaxBaselineDisplayName("Maximum");
            paramDto.setMeasurementUnit("Beats per minute");
            paramDto.setMedicalParameterType("VITALS");
            paramDto.setMinBaselineDisplayName("Minimum");
            paramDto.setName(Constants.Heart_Rate);
            AppUtils.addParameterAPI(paramDto, context);
        }
    }
    private static void addParameterAPI(ParamDto param, Context context) {
        UserDto userDto = ApplicationPreferences.get().getUserDetails().getUserDTO();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(context);
        GenericRequest<APIMessageResponse> getDiseasRequest = new GenericRequest<>
                (Request.Method.POST, APIUrls.get().getAddParameter(userDto.getId()),
                        APIMessageResponse.class, param,
                        dosageDropDownDtoList -> AppUtils.isDataChanged = true,
                        error -> {
                        });
        authExpiredCallback.setRequest(getDiseasRequest);
        ApiService.get().addToRequestQueue(getDiseasRequest);
    }
    public static String getBackendFormattedDateForSymptoms(String selectedDate) {
        String[] dateArray = selectedDate.split("/");
        String monthStr = dateArray[0];
        String dayStr = dateArray[1];
        String year = dateArray[2];
        return monthStr + "-" + dayStr + "-" + year;
    }
    public static Integer getUserResourcedId(Context context, String profilePicName) {
        if (context != null && profilePicName != null) {
            return AppUtils.getDrawableImageResource(context, profilePicName);
        }
        return null;
    }
    public static Integer getDrawableImageResource(Context context, String imageName) {
        if (context != null && imageName != null) {
            return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        }
        return null;
    }
    public static void addRestingHeartRateParameterDtoValue(Context context) {
        String isRestingHeartRateSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_RESTING_HEART_SWITCH);
        if (isRestingHeartRateSwitchOn.equalsIgnoreCase("true")) {
            ParamDto paramDto = new ParamDto();
            paramDto.setFrequencyNumber(1.0);
            paramDto.setFrequencyUnit(Constants.DAY);
            paramDto.setId(3);
            paramDto.setLink("resting-heart-rate");
            paramDto.setMaxBaselineDisplayName("Maximum");
            paramDto.setMeasurementUnit("Beats per minute");
            paramDto.setMedicalParameterType("VITALS");
            paramDto.setMinBaselineDisplayName("Minimum");
            paramDto.setName(context.getResources().getString(R.string.resting_heart_rate));
            AppUtils.addParameterAPI(paramDto, context);
        }
    }

    public static void addWeightParameterDtoValue(Context context) {
        String isWeightSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WEIGHT_SWITCH);
        if (isWeightSwitchOn.equalsIgnoreCase("") || isWeightSwitchOn.equalsIgnoreCase("true")) {
            ParamDto weightParamDto = new ParamDto();
            weightParamDto.setFrequencyNumber(1.0);
            weightParamDto.setFrequencyUnit(Constants.DAY);
            weightParamDto.setId(2);
            weightParamDto.setLink("weight");
            weightParamDto.setMaxBaselineDisplayName(Constants.Weight);
            weightParamDto.setMedicalParameterType("VITALS");
            weightParamDto.setMinBaselineDisplayName("Minimum");
            weightParamDto.setName(Constants.Weight);
            AppUtils.addParameterAPI(weightParamDto, context);
        }
    }

    public static void addWaterParameterDtoValue(Context context) {
        String isWaterSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_WATER_SWITCH);
        if (isWaterSwitchOn.equalsIgnoreCase("") || isWaterSwitchOn.equalsIgnoreCase("true")) {
            ParamDto waterParamDto = new ParamDto();
            waterParamDto.setFrequencyNumber(1.0);
            waterParamDto.setFrequencyUnit(Constants.DAY);
            waterParamDto.setId(225);
            waterParamDto.setMaxBaselineDisplayName("Count");
            waterParamDto.setMeasurementUnit("Number of glasses");
            waterParamDto.setMedicalParameterType("VITALS");
            waterParamDto.setMinBaselineDisplayName("Minimum");
            waterParamDto.setName("Water Intake");
            AppUtils.addParameterAPI(waterParamDto, context);
        }

    }

    public static void addSleepParameterDtoValue(Context context) {
        String isSleepSwitchOn = ApplicationPreferences.get().getStringValue(Constants.FITBIT_SLEEP_SWITCH);
        if (isSleepSwitchOn.equalsIgnoreCase("") || isSleepSwitchOn.equalsIgnoreCase("true")) {
            ParamDto sleepParamDto = new ParamDto();
            sleepParamDto.setFrequencyUnit(Constants.DAY);
            sleepParamDto.setFrequencyNumber(1.0);
            sleepParamDto.setId(272);
            sleepParamDto.setMaxBaselineDisplayName("Count");
            sleepParamDto.setMeasurementUnit("Hours");
            sleepParamDto.setMedicalParameterType("VITALS");
            sleepParamDto.setMinBaselineDisplayName("Minimum");
            sleepParamDto.setName("Hours of sleep during night");
            AppUtils.addParameterAPI(sleepParamDto, context);
        }
    }
    public static void syncFitBitData(Context context,
                                      boolean isFromMyCondition, boolean isSwitchCall) {
        String userProfileString = ApplicationPreferences.get().getStringValue(Constants.FITBIT_USER_PROFILE);
        mContext = context;
        mAccessToken = ApplicationPreferences.get().getStringValue(Constants.FITBIT_ACCESS_TOKEN_NAME);
        String fitBitConnected = ApplicationPreferences.get().getStringValue("FitBit-Connected");
        if (userProfileString != null && !userProfileString.matches("")
                && mAccessToken != null && !mAccessToken.equalsIgnoreCase("") &&
                isNetworkAvailable(mContext) && getLoggedInUser() != null && !isFitBitApiRunning &&
                fitBitConnected != null && fitBitConnected.equalsIgnoreCase("true")) {
            if (isSwitchCall) {
                isFitBitApiRunning = true;
                fitbitApiCallCount = 0;
                fitBitMultipleParameterDtos.clear();
                callGetFitBitWeightDataApi(isFromMyCondition);
            } else {
                boolean isCallAPI = getFitBitAPIFlag();
                if (isCallAPI) {
                    isFitBitApiRunning = true;
                    fitbitApiCallCount = 0;
                    fitBitMultipleParameterDtos.clear();
                    addParameterDtoValue(context);
                    callGetFitBitWeightDataApi(isFromMyCondition);
                }
            }
        } else {
            fitbitApiCallCount++;
        }
        if (fitbitApiCallCount == 3) {
            isFitBitApiRunning = false;
        }
    }
    public static void logCleverTapEvent(Context context, String
            eventMessage, HashMap<String, Object> eventObject) {
        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(context);
        HashMap<String, Object> profileUpdate = new HashMap<>();
        profileUpdate.put("OS", android.os.Build.VERSION.RELEASE);
        profileUpdate.put("Device", Build.MANUFACTURER + " " + Build.MODEL);
        profileUpdate.put("CleverTap ID ", cleverTapDefaultInstance.getCleverTapID());
        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();

        String medicineName = ApplicationPreferences.get().getStringValue(Constants.MEDICINE_NAME);
        String conditionListString = ApplicationPreferences.get().getStringValue(Constants.CONDITION_NAME);

        if (loginResponse != null) {
            UserDto userDto = loginResponse.getUserDTO();
            profileUpdate.put("Identity", userDto.getId());
            profileUpdate.put("Name", userDto.getName());
            profileUpdate.put("Email", userDto.getEmail());
            profileUpdate.put("Location", userDto.getLocation());
            profileUpdate.put("User Type", userDto.getUserType());
            profileUpdate.put("tenantId", userDto.getTenantId());

            if (loginResponse.getEmployerCode() != null && !loginResponse.getEmployerCode().equalsIgnoreCase(""))
                profileUpdate.put("CT Employer Code", loginResponse.getEmployerCode());

            if (medicineName != null) {
                cleverTapDefaultInstance.addMultiValueForKey("Medicine_Name", medicineName);
            }

            if (conditionListString != null) {
                cleverTapDefaultInstance.addMultiValueForKey("Condition_Name", conditionListString);
            }
        }

        cleverTapDefaultInstance.pushProfile(profileUpdate);

        if (eventObject != null) {
            cleverTapDefaultInstance.pushEvent(eventMessage, eventObject);
        } else {
            cleverTapDefaultInstance.pushEvent(eventMessage);
        }
    }
    public static List<Time> getTimeList(int limit, List<String> selectedTimeList, String
            whichType) {

        String todayDate = getTodayDate();
        List<Time> timeList = new ArrayList<>();
        Time time;
        for (int i = 0; i < 24; i++) {
            time = new Time();
            String dateTime = todayDate;

            if (String.valueOf(i).length() == 1) {
                dateTime = dateTime.concat(" ").concat("0").concat(i + "");
            } else {
                dateTime = dateTime.concat(" " + i + "");
            }

            dateTime = dateTime.concat(":00");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            try {
                Date formattedDate = simpleDateFormat.parse(dateTime);

                DateFormat displayFormat = new SimpleDateFormat("hh aa", Locale.ENGLISH);
                displayFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                String displayFormatStr = displayFormat.format(formattedDate);

                DateFormat _24HrsFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                _24HrsFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                String _24HoursFormatStr = _24HrsFormat.format(formattedDate);

                DateFormat _24HrsFormatWithSec = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                _24HrsFormatWithSec.setTimeZone(TimeZone.getTimeZone("IST"));
                //String withSecFormatStr = _24HrsFormatWithSec.format(formattedDate);

                DateFormat _HrsFormat = new SimpleDateFormat("HH");
                _HrsFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                String withSecFormatStr = _HrsFormat.format(formattedDate);

                if (whichType.equalsIgnoreCase(Constants.DAILY)) {
                    withSecFormatStr = withSecFormatStr.concat(":").concat("00").concat("00");
                } else if (whichType.equalsIgnoreCase(Constants.WEEKLY)) {
                    withSecFormatStr = withSecFormatStr.concat(":").concat("15").concat("00");
                } else if (whichType.equalsIgnoreCase(Constants.MONTHLY)) {
                    withSecFormatStr = withSecFormatStr.concat(":").concat("30").concat("00");
                }

                time.setDisplayFormat(displayFormatStr);
                time.setLongTime(formattedDate.getTime());
                time.set_24HrsFormat(_24HoursFormatStr);
                time.setWithSecondFormat(withSecFormatStr);

                if (selectedTimeList == null) {
                    if (limit == DAILY_LIMIT) {
                        if (i == 9) {
                            time.setSelected(true);
                        }
                    } else {
                        if (i == 9) {
                            time.setSelected(true);
                        }
                    }
                } else {
                    for (int ii = 0; ii < selectedTimeList.size(); ii++) {
                        if (_24HoursFormatStr.equalsIgnoreCase(selectedTimeList.get(ii))) {
                            time.setSelected(true);
                        }
                    }
                }
                timeList.add(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return timeList;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void performLogout(Context context, boolean isFromHelp) {
        if (context != null) {
/*            Intent intent;
            ApplicationPreferences.get().setUserDetails(null);
            ApplicationPreferences.get().logPremiumMsgExpireDate(null);
            ApplicationPreferences.get().logIsNeedToShowBanner(true);
            ApplicationPreferences.get().deleteAllDoneView();
            ApplicationPreferences.get().setLogDeviceAPICalledDate(null);
            PersistentPreferences.get().storeStatusOfIsFCMTokenUploadedToServer(false);
            ApplicationDB.get().clearUserRelatedTable();
            ApplicationPreferences.get().saveStringValue(Constants.FITBIT_ACCESS_TOKEN_NAME, null);
            ApplicationPreferences.get().saveStringValue("FitBit-Connected", "false");
            ApplicationPreferences.get().setDrugAdherenceDrugs(null);
            ApplicationPreferences.get().saveStringValue(Constants.SELECTED_DATE, null);
            ApplicationPreferences.get().saveStringValue(Constants.EMPLOYER_CODE, null);
            ApplicationPreferences.get().saveStringValue(Constants.MEDICINE_NAME, null);
            ApplicationPreferences.get().saveStringValue(Constants.CONDITION_NAME, null);
            ApplicationPreferences.get().saveStringValue(Constants.CONDITION_LIST, null);
            ApplicationPreferences.get().saveStringValue(Constants.ONBOARDING_FLAG, null);
            ApplicationPreferences.get().saveStringValue(Constants.ONBORDING_GENDER, null);
            ApplicationPreferences.get().saveStringValue(Constants.TRACKING_FOOD_LIST, null);
            ApplicationPreferences.get().saveStringValue(Constants.ONBORDING_USERNAME, null);
            ApplicationPreferences.get().saveStringValue(Constants.ONBORDING_YEAR_OF_BIRTH, null);
            ApplicationPreferences.get().saveStringValue(Constants.ONBORDING_QUESTIONS_LIST, null);
            ApplicationPreferences.get().saveStringValue(Constants.SYMPTOMS_LIST_ONBOARDING, null);
            ApplicationPreferences.get().saveStringValue(Constants.SELECTED_ONBOARDING_CONDITION, null);
            ApplicationPreferences.get().setNotificationCount(0);
            ApplicationPreferences.get().saveStringValue(Constants.MOOD_UPDATED_DATE, null);
            ApplicationPreferences.get().saveStringValue(Constants.SUBSCRIPTION_MESSAGE, null);
            Onboarding4Activity.symptomArrayList = null;

            ApplicationPreferences.get().saveStringValue(Constants.BLOG_RESPONSE,null);
            ApplicationPreferences.get().saveStringValue(Constants.SYMPTOMS_RESPONSE,null);
            ApplicationPreferences.get().saveStringValue(Constants.FACT_SHEET_RESPONSE,null);

            intent = new Intent(context, DisneyLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);*/

//            clearAppData(context);
        }
    }
    public static boolean isProdBuild() {
        return BuildConfig.APP_MODE == Constants.PRODUCTION;
    }

    public static String getEncodedStringForDrugAdapter(String drugName) {
        String outputStr = "";
        if (drugName != null) {
            try {
                outputStr = (URLDecoder.decode(drugName, "UTF-8"));
            } catch (Exception ex) {
                outputStr = drugName;
                ex.printStackTrace();
            }
        }

        return outputStr;
    }
    public static void hideProgressBar(ProgressDialogSetup progress) {
        if (progress != null) {
            progress.hide();
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if (activity.getCurrentFocus() != null) {
                    if (activity.getCurrentFocus().getWindowToken() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        }
    }

    public static String getFormattedDate(String selectedDate) {
        if (selectedDate != null) {
            String[] dateArray = selectedDate.split("-");
            String year = dateArray[0];
            String monthStr = dateArray[1];
            String day = dateArray[2];
            return monthStr + "/" + day + "/" + year;
        }
        return "";
    }
    public static void cancelAPICalls() {
        ApiService.get().cancelApiCalls();
    }
    public static String getVolleyError(Context context, VolleyError error, AuthExpiredCallback authExpiredCallback) {
        if (context != null) {
            if (error instanceof TimeoutError) {
                return context.getString(R.string.time_our_error);
            } else if (error instanceof NoConnectionError) {
                return context.getString(R.string.can_not_connect);
            } else if (error instanceof AuthFailureError) {
                if (error.networkResponse.statusCode == 401) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("message").toString();

                        if (srt.equalsIgnoreCase(Constants.INVALID_TOKEN)) {
                            AppUtils.performLogout(context, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (authExpiredCallback != null) {
                        authExpiredCallback.onAuthExpired(false);
                    }
                    return srt;
                }
                return context.getString(R.string.auth_fail);
            } else if (error instanceof ServerError) {
                if (error.networkResponse.statusCode == 400 ||
                        error.networkResponse.statusCode == 401 ||
                        error.networkResponse.statusCode == 404 ||
                        error.networkResponse.statusCode == 422) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("message").toString();

                        if (srt.equalsIgnoreCase(Constants.INVALID_TOKEN)) {
                            AppUtils.performLogout(context, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return srt;
                }
                return context.getString(R.string.server_error);
            } else if (error instanceof NetworkError) {
                return context.getString(R.string.netwotk_error);
            } else if (error instanceof ParseError) {
                return context.getString(R.string.parser_error);
            }

            String msg = error.getMessage();

            if (msg != null && msg.trim().length() > 0) {
                return msg;
            }
            return context.getString(R.string.unknow_error_txt);
        } else {
            return "Unknown Error";
        }
    }
    public static String getVolleyError(Context context, VolleyError error) {
        if (context != null) {
            if (error instanceof TimeoutError) {
                return context.getString(R.string.time_our_error);
            } else if (error instanceof NoConnectionError) {
                return context.getString(R.string.can_not_connect);
            } else if (error instanceof AuthFailureError) {
                if (error.networkResponse.statusCode == 401) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("message").toString();
                        if (srt.equalsIgnoreCase(Constants.INVALID_TOKEN)) {
                            AppUtils.performLogout(context, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return srt;
                }
                return context.getString(R.string.auth_fail);
            } else if (error instanceof ServerError) {
                if (error.networkResponse.statusCode == 400 ||
                        error.networkResponse.statusCode == 401 ||
                        error.networkResponse.statusCode == 404 ||
                        error.networkResponse.statusCode == 422) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("message").toString();
                        if (srt.equalsIgnoreCase(Constants.INVALID_TOKEN)) {
                            AppUtils.performLogout(context, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return srt;
                }
                return context.getString(R.string.server_error);
            } else if (error instanceof NetworkError) {
                return context.getString(R.string.netwotk_error);
            } else if (error instanceof ParseError) {
                return context.getString(R.string.parser_error);
            }

            String msg = error.getMessage();

            if (msg != null && msg.trim().length() > 0) {
                return msg;
            }

            return context.getString(R.string.unknow_error_txt);
        } else {
            return "Unknown Error";
        }
    }
    public static void logEvent(String eventName) {
//        AppEventsLogger logger = Analytics.get();
//        if (logger != null && isProdBuild()) {
//            logger.logEvent(eventName);
//        }
    }
    public static boolean isValidString(String stringName) {
        String regex = "^[a-zA-Z0-9-_ ]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringName);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static void openDialogBox(Activity activity, UserDto userDto, FragmentTransaction
            ft,
                                     Fragment prev, boolean isAddDrug) {
        if (activity != null && userDto != null && ft != null) {
            if (prev != null) {
                ft.remove(prev);
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constants.USER_DTO, new Gson().toJson(userDto));
            bundle.putBoolean(Constants.ADD_DRUG, isAddDrug);
            DialogFragment dialogFragment = new DialogFragmentDiseases();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(ft, "dialog");
        }
    }
    public static String get24HrsTime(String _12HrsTime) {

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);


        try {
            Date date = parseFormat.parse(_12HrsTime);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
    public static String getTodayDateForSchedule() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long date = System.currentTimeMillis();
        cal.setTimeInMillis(date);
        return simpleDateFormat.format(cal.getTime());
    }

    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        long date = System.currentTimeMillis();
        cal.setTimeInMillis(date);
        return simpleDateFormat.format(cal.getTime());
    }
    public static String getSevenDaysBackDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 7);
        return simpleDateFormat.format(cal.getTime());
    }
    public static String getBackendFormattedDate(String selectedDate) {
        String[] dateArray = selectedDate.split("/");
        String monthStr = dateArray[0];
        String dayStr = dateArray[1];
        String year = dateArray[2];
        return year + "-" + monthStr + "-" + dayStr;
    }
    public static long getDateInMillis(String selectedDate) {
        try {
            Date date = simpleDateFormat.parse(selectedDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static String getTodayDateWithFormat() {
        Calendar cal = Calendar.getInstance();
        long date = System.currentTimeMillis();
        cal.setTimeInMillis(date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(cal.getTime());
    }
    public static InputFilter setInputFilterToRestrictEmoticons() {
        return (source, start, end, dest, dstart, dend) -> {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        };
    }
    public static String get12HrsDate(String _24HrsTime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        Date date = null;
        String output = "";
        try {
            //Conversion of input String to date
            date = df.parse(_24HrsTime);
            //old date format to new date format
            output = outputformat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return output;
    }
    public static void openSnackBar(View view, String msg) {
        try {
            if (view != null && msg != null && !msg.equalsIgnoreCase(Constants.JWT_TOEKN_EXPIRED)) {
                Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
         /*       View view1 = snackbar.getView();
                TextView tv = view1.findViewById(R.id.snackbar_text);
                tv.setMaxLines(3);
                tv.setTextColor(Color.WHITE);*/
                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public static String getEncodedString(String drugName) {
        String outputStr = "";
        if (drugName != null) {
            try {
              //  String selectedLanguage = ApplicationPreferences.get().getSelectedLanguage();
                /*if (selectedLanguage.equals(Constants.SPANISH) || selectedLanguage.equals(Constants.ENGLISH)) {
                    byte[] arrByteForSpanish = drugName.getBytes("ISO-8859-1");
                    outputStr = new String(arrByteForSpanish);
                } else {*/
                    outputStr = (URLDecoder.decode(drugName, "UTF-8"));
                /*}*/
            } catch (Exception ex) {
                outputStr = drugName;
                ex.printStackTrace();
            }
        }

        return outputStr;
    }


    public static String getDateInReportFormatted(String inputDate) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yy HH:mm", Locale.ENGLISH);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.ENGLISH);

        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
            output = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String getValueWithOneDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(value);
    }

    public static String getValueWithoutDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(value);
    }


    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public static String getTrackFormattedDateFromReportScreen(String selectedDateStr) {
        String displayFormatStr = "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy HH:mm");
        try {
            Date formattedDate = simpleDateFormat.parse(selectedDateStr);
            DateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            displayFormatStr = displayFormat.format(formattedDate);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return displayFormatStr;
    }


    public static String getCurrentTimeInString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Long dateTobeConvert = System.currentTimeMillis();
        return simpleDateFormat.format(dateTobeConvert);
    }

    public static String change_dd_mm_yyyy_to_mm_dd_yyyy(String date) {
        String[] dateArray = date.split("/");
        String dayStr = dateArray[0];
        String monthStr = dateArray[1];
        String year = dateArray[2];
        return monthStr + "/" + dayStr + "/" + year;
    }


    public static String getBackendFormattedDateForTrack(String selectedDate) {
        String[] dateArray = selectedDate.split("/");
        String monthStr = dateArray[0];
        String dayStr = dateArray[1];
        String year = dateArray[2];
        return year + "-" + dayStr + "-" + monthStr;
    }
    public static void showAlert(Context context, String message) {

        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setMessage(message)
                    .setTitle("");

            builder.setPositiveButton(context.getString(R.string.ok), (dialog, id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    public static void storeScreenName(String screenName) {
        ApplicationPreferences.get().saveStringValue
                (Constants.CURRENT_OPEN_FRAGMENT, screenName);
    }

    public static void setTitle(FragmentActivity activity, String title) {
        if (activity != null) {
            activity.setTitle(title);
        }
    }


    public static String convertDateInMMDDYYYY(String input) {
        String[] dateArray = input.split("-");
        String monthStr = dateArray[0];
        String dayStr = dateArray[1];
        String year = dateArray[2];
        return monthStr + "/" + dayStr + "/" + year;
    }



    public static String getLongToStringDate(long longDate) {
        long val = longDate;
        Date date = new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("MM-dd-yyyy");
        String dateText = df2.format(date);
        System.out.println(dateText);
        return dateText;
    }
    public static void hideSoftKeyboard(FragmentActivity activity) {
        if (activity != null) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned getHtmlString(String html) {
        Spanned result;
        if (html != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
            } else {
                result = Html.fromHtml(html);
            }
            return result;
        } else {
            return Html.fromHtml("");
        }
    }


    public static void openMyAccountPage(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, HomeActivity.class);
           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.SHOW_ACCOUNT_PAGE, Constants.SHOW_ACCOUNT_PAGE);*/
            context.startActivity(intent);
        }
    }




    public static String getSimpleFormatDate(String selectedDateStr) {
        String displayFormatStr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formattedDate = simpleDateFormat.parse(selectedDateStr);
            DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
            displayFormatStr = displayFormat.format(formattedDate);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return displayFormatStr;
    }
    public static void changeStatusBarColor(Activity activity) {
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorAccent));
            }
        }
    }


    public static String getOnlyDateFromDateTime(String inputDate) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yy HH:mm", Locale.ENGLISH);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
            output = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }


    public static String get12HoursTimeFormat(String time) {
        String stringDate = time;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date dateObject = new Date();
        try {
            dateObject = sdf.parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdf.format(dateObject);
    }



    public static String getOnlyTimeFromDate(String stringDate) {
        String str = stringDate;

        String fmt = "DD MMM yy HH:mm";
        DateFormat df = new SimpleDateFormat(fmt);

        Date dt = null;
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat tdf = new SimpleDateFormat("HH:mm");
        DateFormat dfmt = new SimpleDateFormat("MM/dd/yyyy");

        String timeOnly = tdf.format(dt);
        String dateOnly = dfmt.format(dt);

        return timeOnly;
    }




    public static int getFeet(int inc) {
        return (inc / 12);
    }

    public static int getIncs(int inc) {
        return (inc % 12);
    }
    public static int getIncsFromFtIncs(int ft, int incs) {
        int value = ft * 12;
        return value + incs;
    }

    public static long getIncsFromCms(int cms) {
        return Math.round(cms / 2.54);
    }

    public static void setIsEmailConfirm(boolean isEmailConfirm) {
        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto userDTO = loginResponse.getUserDTO();
            if (userDTO != null) {
                userDTO.setEmailConfirm(isEmailConfirm);
                ApplicationPreferences.get().setUserDetails(loginResponse);
            }
        }
    }


    public static boolean isEmpty(String et) {
        return et.isEmpty() || et.length() == 0 || et.equals("");
    }


    public static String getOnlyTimeFromSymptomDate(String stringDate) {
        String str = stringDate;

        String fmt = "MM-dd-yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(fmt);

        Date dt = null;
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat tdf = new SimpleDateFormat("HH:mm");
        DateFormat dfmt = new SimpleDateFormat("MM-dd-yyyy");

        String timeOnly = tdf.format(dt);
        String dateOnly = dfmt.format(dt);

        return timeOnly;
    }

    public static String getOnlyTimeFromSymptomDateTwo(String stringDate) {
        String str = stringDate;

        String fmt = "MM-dd-yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(fmt);

        Date dt = null;
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat tdf = new SimpleDateFormat("HH:mm");
        DateFormat dfmt = new SimpleDateFormat("MM-dd-yyyy");

        String timeOnly = tdf.format(dt);
        String dateOnly = dfmt.format(dt);

        String datetime = dateOnly;
        return datetime;
    }



    public static String getDateInDDMMMYY(String input) {
        SimpleDateFormat inFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date d1 = null;
        try {
            d1 = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outFormat = new SimpleDateFormat("dd-MMM-yy");

        Log.e("Out_date_DDMMMYY =", " " + outFormat.format(d1));

        return outFormat.format(d1);
    }

    public static long getTwoDaysBackDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 2);
        return cal.getTimeInMillis();
    }

    public static String getStringDateForNotification(Long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        if (simpleDateFormat.format(cal.getTime()).equalsIgnoreCase(simpleDateFormat.format(now.getTime()))) {
            return timeFormat.format(cal.getTime());
        } else {
            return simpleDateFormat.format(cal.getTime());
        }
    }



    public static String getInitialFromName(String userName) {
        String name = "";
        if (userName != null && userName.trim().length() > 0) {
            if (userName.contains(" ")) {
                String splitName[] = userName.split(" ");
                String firstName = splitName[0];
                String lastName = splitName[splitName.length - 1];

                name = name.concat(firstName.substring(0, 1));
                name = name.concat(lastName.substring(0, 1));
            } else {
                if (userName.length() == 1) {
                    return userName.toUpperCase();
                } else {
                    name = userName.substring(0, 2);
                }
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return "";
        }
    }
    public static void callGetUserSubscriptionAPI(final Context context, final View view) {
        //removed the old payment gateway api call
    }

    public static boolean isNeedToShowBatteryOptimization(String lastLogDate) {
        boolean isTrue = true;
        if (lastLogDate != null) {
            try {
                String todayDateStr = getTodayDate();
                Date selectedDate = simpleDateFormat.parse(lastLogDate);
                Date todayDate = simpleDateFormat.parse(todayDateStr);
                if (todayDate.equals(selectedDate)) {
                    isTrue = false;
                } else {
                    isTrue = todayDate.after(selectedDate);
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return isTrue;
    }
}
