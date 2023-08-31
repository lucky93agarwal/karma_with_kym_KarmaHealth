package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDrugDto {

    private String dosage;
    private String userName;
    private int dosageCount;
    private Long drugId;
    private String source;
    private String startDate;
    private Long userId;

    // userDrugId
    private Long id;

    private String strength;
    private String drugName;
    private PrescriptionRefillDto prescriptionRefillReminder;
    private String dosageType;

    //
    private List<ReminderDto> reminderList;

    private String dosageForm;
    private String dosageDisplayName;
    private String  endDate;
    private String drugFreq;
    private Long combinationId;
    private List<DosageDto> userDrugDosages;
    private int quantity;
    private String unit;
    private boolean custom;
    private String url;
    private String cerner;

    //2.3.34
    private String displayName;

    public UserDrugDto(Long id, PrescriptionRefillDto prescriptionRefill, String dosageType, List<ReminderDto> reminderList, int dosageCount, Long drugId,
                       String source, String startDate, Long userId, String strength, String drugName,
                       String dosageForm, String dosageDisplayName, String dosage, String url,String cerner,String displayName) {
        this.prescriptionRefillReminder = prescriptionRefill;
        this.dosageType = dosageType;
        this.reminderList = reminderList;
        this.dosageCount = dosageCount;
        this.drugId = drugId;
        this.source = source;
        this.startDate = startDate;
        this.userId = userId;
        this.id = id;
        this.strength = strength;
        this.drugName = drugName;
        this.dosageForm = dosageForm;
        this.dosageDisplayName = dosageDisplayName;
        this.dosage = dosage;
        this.url = url;
        this.cerner = cerner;
        this.displayName = displayName;
    }

    public static UserDrugDto fromCursor(Cursor cursor) {

        String prescriptionRefillStr = cursor.getString(cursor.getColumnIndex(NewDrugTable.PRESCRIPTION_REFILL));
        Gson gson = new Gson();
        PrescriptionRefillDto prescriptionRefillDto = null;
        if(prescriptionRefillStr != null){
            prescriptionRefillDto = gson.fromJson(prescriptionRefillStr,PrescriptionRefillDto.class);
        }

        String reminderString = cursor.getString(cursor.getColumnIndex(NewDrugTable.REMINDER_LIST));
        List<ReminderDto> reminderDtoList = null;
        if(reminderString != null){
            Type reminderType = new TypeToken<List<ReminderDto>>() {
            }.getType();
            reminderDtoList = gson.fromJson(reminderString, reminderType);
        }


        UserDrugDto userDrugDTO = new UserDrugDto(cursor.getLong(cursor.getColumnIndex(NewDrugTable.SID)),
                prescriptionRefillDto,
                cursor.getString(cursor.getColumnIndex(NewDrugTable.DOSAGE_TYPE)),
                reminderDtoList,
                cursor.getInt(cursor.getColumnIndex(NewDrugTable.DRUG_DOSAGE_COUNT)),
                cursor.getLong(cursor.getColumnIndex(NewDrugTable.DRUG_ID)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.SOURCE)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.START_DATE)),
                cursor.getLong(cursor.getColumnIndex(NewDrugTable.USER_ID)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.STRENGTH)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.DRUG_NAME)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.DOSAGE_FORM)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.DOSAGE_DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.DOSAGE)),
                cursor.getString(cursor.getColumnIndex(NewDrugTable.URL)),
                cursor.getString((cursor.getColumnIndex(NewDrugTable.CERNER))),
                cursor.getString((cursor.getColumnIndex(NewDrugTable.DISPLAY_NAME))));
        userDrugDTO.setCustom((cursor.getInt(cursor.getColumnIndex(NewDrugTable.CUSTOM)) == 1) ? true : false);

        return userDrugDTO;
    }

    public ContentValues toCV() {
        ContentValues cv = new ContentValues();

        cv.put(NewDrugTable.STRENGTH,strength);
        cv.put(NewDrugTable.DRUG_NAME, drugName);
        cv.put(NewDrugTable.SID, id);
        cv.put(NewDrugTable.START_DATE, startDate);
        cv.put(NewDrugTable.SOURCE, source);
        cv.put(NewDrugTable.USER_ID, userId);
        cv.put(NewDrugTable.DRUG_ID, drugId);
        cv.put(NewDrugTable.DRUG_DOSAGE_COUNT,dosageCount);

        if(prescriptionRefillReminder != null){
            cv.put(NewDrugTable.PRESCRIPTION_REFILL,new Gson().toJson(prescriptionRefillReminder));
        }

        cv.put(NewDrugTable.DOSAGE_TYPE,dosageType);
        cv.put(NewDrugTable.REMINDER_LIST, new Gson().toJson(reminderList));
        cv.put(NewDrugTable.DOSAGE_FORM,dosageForm);
        cv.put(NewDrugTable.DOSAGE,dosage);
        cv.put(NewDrugTable.DOSAGE_DISPLAY_NAME,dosageDisplayName);
        cv.put(NewDrugTable.URL,url);
        cv.put(NewDrugTable.CERNER,cerner);
        cv.put(NewDrugTable.CUSTOM, custom);
        cv.put(NewDrugTable.DISPLAY_NAME, displayName);

        return cv;
    }

    public String getDosageDisplayName() {
        return dosageDisplayName;
    }

    public void setDosageDisplayName(String dosageDisplayName) {
        this.dosageDisplayName = dosageDisplayName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public PrescriptionRefillDto getPrescriptionRefill() {
        return prescriptionRefillReminder;
    }

    public void setPrescriptionRefill(PrescriptionRefillDto prescriptionRefill) {
        this.prescriptionRefillReminder = prescriptionRefill;
    }

    public String getDosageType() {
        return dosageType;
    }

    public void setDosageType(String dosageType) {
        this.dosageType = dosageType;
    }

    public PrescriptionRefillDto getPrescriptionRefillReminder() {
        return prescriptionRefillReminder;
    }

    public void setPrescriptionRefillReminder(PrescriptionRefillDto prescriptionRefillReminder) {
        this.prescriptionRefillReminder = prescriptionRefillReminder;
    }

    public List<ReminderDto> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<ReminderDto> reminderList) {
        this.reminderList = reminderList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDosageCount() {
        return dosageCount;
    }

    public void setDosageCount(int dosageCount) {
        this.dosageCount = dosageCount;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDrugFreq() {
        return drugFreq;
    }

    public void setDrugFreq(String drugFreq) {
        this.drugFreq = drugFreq;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public List<DosageDto> getUserDrugDosages() {
        return userDrugDosages;
    }

    public void setUserDrugDosages(List<DosageDto> userDrugDosages) {
        this.userDrugDosages = userDrugDosages;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    //prescriptionRefillReminder.equals(that.prescriptionRefillReminder) &&


    public boolean getCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCerner() {
        return cerner;
    }

    public void setCerner(String cerner) {
        this.cerner = cerner;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDrugDto that = (UserDrugDto) o;
        return dosageCount == that.dosageCount &&
                Objects.equals(dosage, that.dosage) &&
                Objects.equals(drugId, that.drugId) &&
                Objects.equals(source, that.source) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(strength, that.strength) &&
                Objects.equals(drugName, that.drugName) &&
                prescriptionRefillReminder.equals(that.prescriptionRefillReminder) &&
                Objects.equals(dosageType, that.dosageType) &&
                reminderList.equals(that.reminderList) &&
                Objects.equals(dosageForm, that.dosageForm) &&
                Objects.equals(dosageDisplayName, that.dosageDisplayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dosage, dosageCount, drugId, source, startDate, userId, id, strength, drugName, prescriptionRefillReminder, dosageType, reminderList, dosageForm, dosageDisplayName);
//
    }

    public static class UserDrugDtoList extends ArrayList<UserDrugDto> {
    }
}
