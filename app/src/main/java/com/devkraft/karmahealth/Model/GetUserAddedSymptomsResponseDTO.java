package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.devkraft.karmahealth.db.SymptomsTable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetUserAddedSymptomsResponseDTO {

    private String name;
    private Long symptomId;
    private String condition;
    private String recordedDate;
    private Long userSymptomId;
    private String symptomsType;
    private String severityLevel;
    private String measurementType;
    private List<String> measurementValue;
    private String selectedMeasurementValues;

    public GetUserAddedSymptomsResponseDTO(String symptomName,long symptomId, String condition, String recordDate, long userSymptomId, String symptomsType, String severityLevel, String measurementType, String measurementValue, String selectedMeasurementValues) {
        this.name = symptomName;
        this.symptomId = symptomId;
        this.condition = condition;
        this.recordedDate = recordDate;
        this.userSymptomId = userSymptomId;
        this.symptomsType = symptomsType;
        this.severityLevel = severityLevel;
        this.measurementType = measurementType;
        this.measurementValue = Collections.singletonList(measurementValue);
        this.selectedMeasurementValues = selectedMeasurementValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(Long symptomId) {
        this.symptomId = symptomId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Long getUserSymptomId() {
        return userSymptomId;
    }

    public void setUserSymptomId(Long userSymptomId) {
        this.userSymptomId = userSymptomId;
    }

    public String getSymptomsType() {
        return symptomsType;
    }

    public void setSymptomsType(String symptomsType) {
        this.symptomsType = symptomsType;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public List<String> getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(List<String> measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getSelectedMeasurementValues() {
        return selectedMeasurementValues;
    }

    public void setSelectedMeasurementValues(String selectedMeasurementValues) {
        this.selectedMeasurementValues = selectedMeasurementValues;
    }

    public static GetUserAddedSymptomsResponseDTO fromCursor(Cursor cursor) {
        return new GetUserAddedSymptomsResponseDTO(cursor.getString(cursor.getColumnIndex(SymptomsTable.NAME)),
                cursor.getLong(cursor.getColumnIndex(SymptomsTable.SYMPTOM_ID)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.CONDITION)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.RECORDEDDATE)),
                cursor.getLong(cursor.getColumnIndex(SymptomsTable.USERSYMPTOMID)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.SYMPTOMSTYPE)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.SEVERITYLEVEL)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.MEASUREMENTTYPE)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.MEASUREMENTVALUE)),
                cursor.getString(cursor.getColumnIndex(SymptomsTable.SELECTEDMEASUREMENTVALUES)));
    }

    public ContentValues toCV(Long userId) {
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();

        cv.put(SymptomsTable.USER_ID, userId);
        cv.put(SymptomsTable.NAME, name);
        cv.put(SymptomsTable.SYMPTOM_ID, symptomId);
        cv.put(SymptomsTable.CONDITION, condition);
        cv.put(SymptomsTable.RECORDEDDATE, recordedDate);
        cv.put(SymptomsTable.USERSYMPTOMID, userSymptomId);
        cv.put(SymptomsTable.SYMPTOMSTYPE, symptomsType);
        cv.put(SymptomsTable.SEVERITYLEVEL, severityLevel);
        cv.put(SymptomsTable.MEASUREMENTTYPE, measurementType);
        cv.put(SymptomsTable.MEASUREMENTVALUE, gson.toJson(measurementValue));
        cv.put(SymptomsTable.SELECTEDMEASUREMENTVALUES, selectedMeasurementValues);

        return cv;
    }

    public static class GetUserAddedSymptomsListResponseDTO extends ArrayList<GetUserAddedSymptomsResponseDTO> {

    }
}
