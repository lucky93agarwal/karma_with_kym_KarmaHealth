package com.devkraft.karmahealth.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.Model.DependentTable;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DrugDosageAdherenceDTO;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.NewDrugTable;
import com.devkraft.karmahealth.Model.Notification;
import com.devkraft.karmahealth.Model.ParameterDto;
import com.devkraft.karmahealth.Model.PrescriptionRefillDto;
import com.devkraft.karmahealth.Model.UserAgendaDto;
import com.devkraft.karmahealth.Model.UserDrugDto;
import com.devkraft.karmahealth.Model.Weights;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ApplicationDB {
    private String appName;

    private static ApplicationDB ourInstance = null;
    private ApplicationDBHelper dbHelper;
    private boolean notificationPresent;
    public static ApplicationDB init(Context context, String appName) {
        if (ourInstance == null) {
            Log.i("checkmodeldashboard", "This method call ApplicationDB  = 1 ");
            ourInstance = new ApplicationDB(context, appName);
        }
        return ourInstance;
    }

    private ApplicationDB(Context context, String appName) {
        Log.i("checkmodeldashboard", "This method call ApplicationDB  = ");
        dbHelper = new ApplicationDBHelper(context, appName);
    }
    private void clearSymptoms() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(SymptomsTable.TABLE_NAME, null, null);
        db.close();
    }
    public void upsertSymptoms(Long userId, List<GetUserAddedSymptomsResponseDTO> symptomsList) {
        if (symptomsList != null) {
            if (symptomsList.size() > 0 || symptomsList.size() == 0) {
                clearSymptoms();
                for (int i = 0; i < symptomsList.size(); i++) {
                    upsertSymptom(symptomsList.get(i), userId);
                }
            }
        }
    }
    private void upsertSymptom(GetUserAddedSymptomsResponseDTO userAddedSymptomsResponseDTO, Long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

//        GetUserAddedSymptomsResponseDTO.setUserId(userId);

        Cursor cursor = db.query(SymptomsTable.TABLE_NAME, null,
                SymptomsTable.USER_ID + " = ? AND " + SymptomsTable.SYMPTOM_ID + " = ? ",
                new String[]{userId + "", userAddedSymptomsResponseDTO.getSymptomId() + ""},
                null, null, null, "1");

        boolean isRecordExists = (cursor.getCount() > 0);
        if (!isRecordExists) {
            ContentValues values = userAddedSymptomsResponseDTO.toCV(userId);

            db.insertWithOnConflict(
                    SymptomsTable.TABLE_NAME,
                    SymptomsTable.COLUMN_NAME_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);

            db.close();
        }
        cursor.close();
    }
    public boolean isStandardDrugAdded(Long userId) {
        boolean isAdded = false;
        if(userId != null){
            List<UserDrugDto> userDrugDtoList = getDrugs(userId);
            if(userDrugDtoList != null && userDrugDtoList.size() > 0){
                for(UserDrugDto userDrugDto : userDrugDtoList) {
                    if(!userDrugDto.getCustom()){
                        isAdded = true;
                        break;
                    }
                }
            }
        }
        return isAdded;
    }
    public boolean isCustomDrugAdded(Long userId) {
        boolean isAdded = false;
        if(userId != null){
            List<UserDrugDto> userDrugDtoList = getDrugs(userId);
            if(userDrugDtoList != null && userDrugDtoList.size() > 0){
                for(UserDrugDto userDrugDto : userDrugDtoList) {
                    if(userDrugDto.getCustom()){
                        isAdded = true;
                        break;
                    }
                }
            }
        }
        return isAdded;
    }
    public static ApplicationDB get() {
        if (ourInstance == null) {
            throw new IllegalStateException("Call init to initialize the database before using it.");
        }
        return ourInstance;
    }

    public List<UserDrugDto> getDrugs(Long userId) {
        SQLiteDatabase db = null;
        List<UserDrugDto> items = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(true, NewDrugTable.TABLE_NAME, null, NewDrugTable.USER_ID + "= ? ", new String[]{userId + ""}, null, null,
                    DrugTable.DRUG_NAME + " ASC", null);

            if (cursor.moveToFirst()) {
                do {
                    UserDrugDto userDrugDto = UserDrugDto.fromCursor(cursor);
                    items.add(userDrugDto);
                } while (cursor.moveToNext());
            }
            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }
    public void upsertDisease(Long userId, DiseaseDto diseaseDto) {
        deleteDisease(userId);
        insertDisease(userId,diseaseDto);
    }
    private void insertDisease(Long userId, DiseaseDto dScheduleDto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = dScheduleDto.toCV(userId);
        db.insertWithOnConflict(
                NewDiseaseTable.TABLE_NAME,
                NewDiseaseTable.COLUMN_NAME_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public void deleteDisease(Long userId) {
        Log.e("Login_response", "user id message  = " +userId.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(NewDiseaseTable.TABLE_NAME, NewDiseaseTable.USER_ID + " = ? ",
                new String[]{userId + ""});

        db.close();
    }
    public void deleteDashboard(Long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, DashboardTable.TABLE_NAME, null, DashboardTable.USER_ID + " = ? ", new String[]{userId + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(DashboardTable.TABLE_NAME, DashboardTable.USER_ID + " = " + userId , null);
        }
        cursor.close();
        db.close();
    }
    public void updateDashboardTable(Long userId, UserAgendaDto userAgendaDto) {
        deleteDashboard(userId);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = userAgendaDto.toCV(userId);
        db.insertWithOnConflict(
                DashboardTable.TABLE_NAME,
                DashboardTable.COLUMN_NAME_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }
    public List<DependentDto> getDependents() {
        SQLiteDatabase db = null;
        List<DependentDto> items = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(true, DependentTable.TABLE_NAME, null, null, null, null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                do {
                    DependentDto userDrugDto = DependentDto.fromCursor(cursor);
                    items.add(userDrugDto);
                } while (cursor.moveToNext());
            }
            return items;
        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }
    public Cursor getLastPrescriptionEntry() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.query(PrescriptionRefillTable.TABLE_NAME, null, null,
                null, null, null, PrescriptionRefillTable.ID +" DESC", "1");
    }
    public Cursor getPrescriptionEntry(String identifier) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(PrescriptionRefillTable.TABLE_NAME, null, PrescriptionRefillTable.IDENTIFIER + " = ? " , new String[]{identifier}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
    public DiseaseDto getDiseases(Long userId) {
        if(userId != null){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(true, NewDiseaseTable.TABLE_NAME, null, NewDiseaseTable.USER_ID + " = ? ", new String[]{userId + ""}, null, null, null, null);
            if(cursor.moveToFirst()){
                return DiseaseDto.fromCursor(cursor);
            }
        }
        return null;
    }
    public Cursor getAlarmCursor(String identifier) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null, AlarmTable.IDENTIFIER + " = ? ", new String[]{identifier + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
    public Cursor getAlarmCursorForWeeklyMonthly(String identifier,String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null, AlarmTable.IDENTIFIER + " = ? ", new String[]{identifier + ""}, null, null, null, null);
        Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null,
                AlarmTable.IDENTIFIER + " = ? AND " + AlarmTable.WEEK_DAY + " = ? ", new String[]{identifier , data}, null, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
    public void insertAlarmValue(String identifier, String title, String subTitle, String date, String time,
                                 String strength, String weekDayName, List<DrugDosageAdherenceDTO> drugDosageAdherenceDTO, Long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(AlarmTable.IDENTIFIER, identifier);
        cv.put(AlarmTable.TITLE, title);

        if(strength != null && strength.length() > 0){
            subTitle = subTitle.concat("-").concat(strength);
        }
        cv.put(AlarmTable.SUB_TITLE, subTitle);
        cv.put(AlarmTable.DATE, date);
        cv.put(AlarmTable.TIME, time);
        cv.put(AlarmTable.WEEK_DAY, weekDayName);
        cv.put(AlarmTable.DRUG_ADHERENCE_DTO, new Gson().toJson(drugDosageAdherenceDTO));
        cv.put(AlarmTable.USER_ID, userId);

        db.insertWithOnConflict(
                AlarmTable.TABLE_NAME,
                AlarmTable.COLUMN_NAME_NULLABLE, cv, SQLiteDatabase.CONFLICT_REPLACE);

        //db.close();
    }
    public void updateAlarmSubTitle(String identifier, String subTitle) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(AlarmTable.SUB_TITLE, subTitle);

        db.update(AlarmTable.TABLE_NAME, cv, AlarmTable.IDENTIFIER + " = " + identifier, null);
        //db.close();
    }
    public void updateAdherenceDto(String identifier, String drugAdherenceDto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(AlarmTable.DRUG_ADHERENCE_DTO, drugAdherenceDto);

        db.update(AlarmTable.TABLE_NAME, cv, AlarmTable.IDENTIFIER + " = " + identifier, null);
    }

    public boolean isAlarmExistForMonthTime(String time, String day) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null,
                AlarmTable.TIME + " = ? AND " + AlarmTable.MONTH_DAY + " = ? ", new String[]{time , day}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        return false;
    }
    public void upsertDrug(Context context, UserDrugDto userDrugDto, Long userId, String userName,
                           PrescriptionRefillDto prescriptionRefillDto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor drug = db.query(NewDrugTable.TABLE_NAME, null,
                NewDrugTable.USER_ID + "= ? AND " + NewDrugTable.DRUG_ID + " = ? ",
                new String[]{userId + "", userDrugDto.getDrugId() + ""},
                null, null, null, "1");

        boolean isRecordExists = (drug.getCount() > 0);
        if (!isRecordExists) {
            ContentValues values = userDrugDto.toCV();

            values.put(NewDrugTable.USER_NAME, userName);

            userDrugDto.setUserName(userName);
            AppUtils.setAlarmForDrug(context,userDrugDto,null,prescriptionRefillDto);

            db.insertWithOnConflict(
                    NewDrugTable.TABLE_NAME,
                    NewDrugTable.COLUMN_NAME_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);
            drug.close();
        }else {
            drug.close();
        }
    }
    public boolean isAlarmExistForSameTime(String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null, AlarmTable.TIME + " = ? ", new String[]{time}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        return false;
    }

    public Cursor getAlarmsForWeekly(String time, String weekDay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(AlarmTable.TABLE_NAME, null, AlarmTable.TIME + " = ? AND " + AlarmTable.WEEK_DAY + " = ? " , new String[]{time , weekDay}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
    public Cursor getAlarmsForMonthly(String time, String monthDay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(AlarmTable.TABLE_NAME, null, AlarmTable.TIME + " = ? AND " + AlarmTable.WEEK_DAY + " = ? " , new String[]{time , monthDay}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
    public Cursor getAlarmsForPrescription(String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(PrescriptionRefillTable.TABLE_NAME, null, PrescriptionRefillTable.TIME + " = ? " , new String[]{time}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public Cursor getAlarms(String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(AlarmTable.TABLE_NAME, null, AlarmTable.TIME + " = ? " , new String[]{time}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
    public void updateDrug(UserDrugDto userDrugDto, Long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor drug = db.query(NewDrugTable.TABLE_NAME, null,
                NewDrugTable.USER_ID + " = ? AND " + NewDrugTable.DRUG_ID + " = ? ",
                new String[]{userId + "", userDrugDto.getDrugId() + ""},
                null, null, null, "1");

        boolean isRecordExists = (drug.getCount() > 0);
        if (isRecordExists) {
            db.delete(NewDrugTable.TABLE_NAME,
                    NewDrugTable.USER_ID + " = ? AND " + NewDrugTable.DRUG_ID + " = ? ",
                    new String[]{userId + "", userDrugDto.getDrugId() + ""});
        }

        ContentValues values = userDrugDto.toCV();

        db.insertWithOnConflict(
                NewDrugTable.TABLE_NAME,
                NewDrugTable.COLUMN_NAME_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);
        drug.close();
        db.close();
    }
    public boolean isAlarmExistForSameTime(String time, String weekDayName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null,
                AlarmTable.TIME + " = ? AND " + AlarmTable.WEEK_DAY + " = ? ", new String[]{time , weekDayName}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        return false;
    }
    public void deleteAlarmValue(String identifier) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, AlarmTable.TABLE_NAME, null, AlarmTable.IDENTIFIER + " = ? ", new String[]{identifier + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(AlarmTable.TABLE_NAME, AlarmTable.IDENTIFIER + " = " + identifier, null);
        }
        cursor.close();
        db.close();
    }
    public void deletePrescriptionEntry(String identifier) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, PrescriptionRefillTable.TABLE_NAME, null, PrescriptionRefillTable.IDENTIFIER + " = ? ", new String[]{identifier + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(PrescriptionRefillTable.TABLE_NAME, PrescriptionRefillTable.IDENTIFIER + " = " + identifier, null);
        }
        cursor.close();
        db.close();
    }
    public void insertPrescriptionValue(String identifier, String time,String title, String subTitle) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(PrescriptionRefillTable.IDENTIFIER, identifier);
        cv.put(PrescriptionRefillTable.TIME, time);
        cv.put(PrescriptionRefillTable.TITLE, title);
        cv.put(PrescriptionRefillTable.SUB_TITLE, subTitle);

        db.insertWithOnConflict(
                PrescriptionRefillTable.TABLE_NAME,
                PrescriptionRefillTable.COLUMN_NAME_NULLABLE, cv, SQLiteDatabase.CONFLICT_REPLACE);

    }


    public List<Notification> getTop2Notifications() {
        SQLiteDatabase db = null;
        List<Notification> items = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();

            String order = NotificationTable.CREATION_DATE + " COLLATE NOCASE";
            /*Cursor cursor = db.query(true, NotificationTable.TABLE_NAME,
                    null, null, null, null, null, order + " DESC ", "2");*/

           /* String order = NotificationTable.CREATION_DATE + " COLLATE NOCASE";
            Cursor cursor = db.query(true, NotificationTable.TABLE_NAME,
                    null, null, null, null, null, order + " DESC ", "2");*/

            String sq = "SELECT * FROM " + NotificationTable.TABLE_NAME + " WHERE " +
                    NotificationTable.CREATION_DATE + " > " + AppUtils.getTwoDaysBackDate() + " AND " +
                    NotificationTable.READ + " = 0" + " limit 2";



            String[] columns = {NotificationTable.SID};
            String selection = NotificationTable.CREATION_DATE + " = ? AND " + NotificationTable.READ;
            String[] selectionArgs = { " > " + AppUtils.getTwoDaysBackDate() , 0 + ""};
            String limit = "2";

            Cursor cursor = db.rawQuery(sq, null);
            //Cursor cursor = db.query(NotificationTable.TABLE_NAME, null, selection, selectionArgs, null, null, order, limit);


            if (cursor.moveToFirst()) {
                do {
                    Notification notification = Notification.fromCursor(cursor);
                    items.add(notification);
                } while (cursor.moveToNext());
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }


    public List<Notification> getNotifications() {
        SQLiteDatabase db = null;
        List<Notification> items = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(true, NotificationTable.TABLE_NAME, null, null,null, null, null,
                    NotificationTable.CREATION_DATE + " DESC", null);

            if (cursor.moveToFirst()) {
                do {
                    Notification notification = Notification.fromCursor(cursor);
                    items.add(notification);
                } while (cursor.moveToNext());
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }

    public int getUnreadNotifications() {
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getReadableDatabase();

            String[] columns = {NotificationTable.READ};
            String selection = NotificationTable.READ + " = ? ";
            String[] selectionArgs = {0 + ""};

            Cursor cursor = db.query(false, NotificationTable.TABLE_NAME, columns, selection,selectionArgs, null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                return cursor.getCount();

            }
            cursor.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return 0;
    }


    public void upsertNotification(Notification notification) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {NotificationTable.SID};
        String selection = NotificationTable.SID + " =?";
        String[] selectionArgs = {notification.getId() + ""};
        String limit = "1";

        Cursor cursor = db.query(NotificationTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean isRecordExists = (cursor.getCount() > 0);
        if (!isRecordExists) {
            ContentValues values = notification.toCV();

            db.insertWithOnConflict(
                    NotificationTable.TABLE_NAME,
                    NotificationTable.COLUMN_NAME_NULLABLE, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        cursor.close();
        db.close();
    }


    public void clearNotifications() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NotificationTable.TABLE_NAME, null, null);
        db.close();
    }

    public boolean isNotificationPresent() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sq = "SELECT * FROM " + NotificationTable.TABLE_NAME + " WHERE " +
                NotificationTable.CREATION_DATE + " > " + AppUtils.getTwoDaysBackDate() + " AND " +
                NotificationTable.READ + " = 0";

        Cursor cursor = db.rawQuery(sq, null);
        if(cursor != null && cursor.moveToFirst()){
            cursor.close();
            return true;
        }else {
            return  false;
        }


       /* if (DatabaseUtils.queryNumEntries(db, NotificationTable.TABLE_NAME) == 0) {
            return false;
        }
        return true;*/
    }

    public void deleteVaccines(Long userId, ParameterDto item) {
        DiseaseDto diseaseDto = getDiseases(userId);
        if(diseaseDto != null){
            List<ParameterDto> vaccinesList = diseaseDto.getVaccines();
            if(vaccinesList.remove(item)){
                upsertDisease(userId,diseaseDto);
            }
        }
    }


    public void deleteTest(Long userId, ParameterDto item) {
        DiseaseDto diseaseDto = getDiseases(userId);
        if(diseaseDto != null){
            List<ParameterDto> testList = diseaseDto.getTests();
            if(testList.remove(item)){
                upsertDisease(userId,diseaseDto);
            }
        }
    }


    public Weights getWeightRange(long incsLong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(WeightTable.TABLE_NAME, null, WeightTable.HEIGHT + " = ? " , new String[]{incsLong + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return Weights.fromCursor(cursor);
        }
        return null;
    }
    public void deleteVitals(Long userId, ParameterDto item) {
        DiseaseDto diseaseDto = getDiseases(userId);
        if(diseaseDto != null){
            List<ParameterDto> vitalList = diseaseDto.getVitals();
            if(vitalList.remove(item)){
                upsertDisease(userId,diseaseDto);
            }
        }
    }


    public void deleteSymptom(Long userId, Long userSymptomId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(SymptomsTable.TABLE_NAME, SymptomsTable.USER_ID + " = ? AND " + SymptomsTable.USERSYMPTOMID + " = ? ",
                new String[]{userId + "", userSymptomId + ""});

        db.close();
    }

    public void deleteNotification(Long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(true, NotificationTable.TABLE_NAME, null, NotificationTable.SID + " = ? ", new String[]{id + ""}, null, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(NotificationTable.TABLE_NAME, NotificationTable.SID + " = " + id , null);
        }
        cursor.close();
        db.close();
    }

    public void changeNotificationReadStatus(Long id, boolean isRead) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(isRead){
            cv.put(NotificationTable.READ, 1);
        }else {
            cv.put(NotificationTable.READ, 0);
        }
        db.update(NotificationTable.TABLE_NAME, cv, NotificationTable.SID + " = " + id, null);
        db.close();
    }

    public void updateNotificationReadCount() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE " + NotificationTable.TABLE_NAME + " SET " + NotificationTable.READ + " = " + 1);
        db.close();
    }
    public void upsertNotifications(List<Notification> notificationList, boolean isDeleteRequired) {
        if (notificationList != null) {
            if (notificationList.size() > 0 || notificationList.size() == 0) {
                if (isDeleteRequired) {
                    clearNotifications();
                }
                for (int i = 0; i < notificationList.size(); i++) {
                    upsertNotification(notificationList.get(i));
                }
            }
        }
    }
    public List<UserDrugDto> getAllDrug() {

        SQLiteDatabase db = null;
        List<UserDrugDto> items = new ArrayList<>();

        try {
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(true, NewDrugTable.TABLE_NAME, null, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    UserDrugDto userDrugDto = UserDrugDto.fromCursor(cursor);
                    String userName = cursor.getString(cursor.getColumnIndex(NewDrugTable.USER_NAME));
                    userDrugDto.setUserName(userName);
                    items.add(userDrugDto);
                } while (cursor.moveToNext());
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }
}
