package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.devkraft.karmahealth.db.DashboardTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserAgendaDto {

    private List<DrugAgendaDto> drugs;
    private List<ParameterAgendaDto> vitals;
    private List<ParameterAgendaDto> tests;
    private List<ParameterAgendaDto> vaccines;

    //2.3.34
    private List<DrugAgendaDto> asNeededDrugs;

    private boolean nothingAdded;
    private boolean allDrugDone;
    private boolean allParameterDone;

    public UserAgendaDto(List<DrugAgendaDto> drugs, List<ParameterAgendaDto> vitals,
                         List<ParameterAgendaDto> tests, List<ParameterAgendaDto> vaccines,
                         List<DrugAgendaDto> asNeededDrugs, boolean nothingAdded,
                         boolean allDrugDone, boolean allParameterDone) {
        this.drugs = drugs;
        this.vitals = vitals;
        this.tests = tests;
        this.vaccines = vaccines;
        this.asNeededDrugs = asNeededDrugs;
        this.nothingAdded = nothingAdded;
        this.allDrugDone = allDrugDone;
        this.allParameterDone = allParameterDone;
    }

    public UserAgendaDto(List<ParameterAgendaDto> tests, List<ParameterAgendaDto> vaccines) {
        this.tests = tests;
        this.vaccines = vaccines;
    }

    public static UserAgendaDto fromCursor(Cursor cursor) {

        Gson gson = new Gson();

        String drugString = cursor.getString(cursor.getColumnIndex(DashboardTable.DRUGS));
        List<DrugAgendaDto> drugList = null;
        if (drugString != null) {
            Type reminderType = new TypeToken<List<DrugAgendaDto>>() {
            }.getType();

            drugList = gson.fromJson(drugString, reminderType);
        }

        String asNeededDrugString = cursor.getString(cursor.getColumnIndex(DashboardTable.ASNEEDEDDRUGS));
        List<DrugAgendaDto> asNeededDrugList = null;
        if (asNeededDrugString != null) {
            Type reminderType = new TypeToken<List<DrugAgendaDto>>() {
            }.getType();

            asNeededDrugList = gson.fromJson(asNeededDrugString, reminderType);
        }

        String vitalsString = cursor.getString(cursor.getColumnIndex(DashboardTable.VITALS));
        List<ParameterAgendaDto> vitalsList = null;
        if (vitalsString != null) {
            Type reminderType = new TypeToken<List<ParameterAgendaDto>>() {
            }.getType();

            vitalsList = gson.fromJson(vitalsString, reminderType);
        }

        String testString = cursor.getString(cursor.getColumnIndex(DashboardTable.TESTS));
        List<ParameterAgendaDto> testList = null;
        if (testString != null) {
            Type reminderType = new TypeToken<List<ParameterAgendaDto>>() {
            }.getType();

            testList = gson.fromJson(testString, reminderType);
        }

        String vaccinesString = cursor.getString(cursor.getColumnIndex(DashboardTable.VACCINES));
        List<ParameterAgendaDto> vaccinesList = null;
        if (vaccinesString != null) {
            Type reminderType = new TypeToken<List<ParameterAgendaDto>>() {
            }.getType();

            vaccinesList = gson.fromJson(vaccinesString, reminderType);
        }

        boolean flag = (cursor.getInt(cursor.getColumnIndex(DashboardTable.NOTHING_ADDED)) == 1);

        boolean allParameterDoneFlag = (cursor.getInt(cursor.getColumnIndex(DashboardTable.ALL_PARAMETER_DONE)) == 1);

        boolean allDrugDoneFlag = (cursor.getInt(cursor.getColumnIndex(DashboardTable.ALL_DRUG_DONE)) == 1);

        return new UserAgendaDto(drugList, vitalsList, testList, vaccinesList,asNeededDrugList,flag,allDrugDoneFlag,allParameterDoneFlag);
    }

    public static UserAgendaDto fromCursorForUpcomingReminder(Cursor cursor) {

        Gson gson = new Gson();

        String testString = cursor.getString(cursor.getColumnIndex(DashboardTable.TESTS));
        List<ParameterAgendaDto> testList = null;
        if (testString != null) {
            Type reminderType = new TypeToken<List<ParameterAgendaDto>>() {
            }.getType();

            testList = gson.fromJson(testString, reminderType);
        }

        String vaccinesString = cursor.getString(cursor.getColumnIndex(DashboardTable.VACCINES));
        List<ParameterAgendaDto> vaccinesList = null;
        if (vaccinesString != null) {
            Type reminderType = new TypeToken<List<ParameterAgendaDto>>() {
            }.getType();

            vaccinesList = gson.fromJson(vaccinesString, reminderType);
        }

        return new UserAgendaDto(testList, vaccinesList);
    }

    public ContentValues toCV(Long userId) {
        ContentValues cv = new ContentValues();
        cv.put(DashboardTable.USER_ID, userId);
        cv.put(DashboardTable.DRUGS, new Gson().toJson(drugs));
        cv.put(DashboardTable.VITALS, new Gson().toJson(vitals));
        cv.put(DashboardTable.TESTS, new Gson().toJson(tests));
        cv.put(DashboardTable.VACCINES, new Gson().toJson(vaccines));
        cv.put(DashboardTable.ASNEEDEDDRUGS, new Gson().toJson(asNeededDrugs));

        if(isNothingAdded()){
            cv.put(DashboardTable.NOTHING_ADDED,1);
        }else {
            cv.put(DashboardTable.NOTHING_ADDED,0);
        }

        if(isAllDrugDone()){
            cv.put(DashboardTable.ALL_DRUG_DONE, 1);
        }else {
            cv.put(DashboardTable.ALL_DRUG_DONE, 0);
        }


        if(isAllParameterDone()){
            cv.put(DashboardTable.ALL_PARAMETER_DONE, 1);
        }else {
            cv.put(DashboardTable.ALL_PARAMETER_DONE, 0);
        }


        return cv;
    }

    public ContentValues toUpcomingReminderCV(Long userId) {
        ContentValues cv = new ContentValues();
        cv.put(DashboardTable.USER_ID, userId);
        cv.put(DashboardTable.TESTS, new Gson().toJson(tests));
        cv.put(DashboardTable.VACCINES, new Gson().toJson(vaccines));
        return cv;
    }

    public boolean isNothingAdded() {
        return nothingAdded;
    }

    public void setNothingAdded(boolean nothingAdded) {
        this.nothingAdded = nothingAdded;
    }

    public List<DrugAgendaDto> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<DrugAgendaDto> drugs) {
        this.drugs = drugs;
    }

    public List<ParameterAgendaDto> getVitals() {
        return vitals;
    }

    public void setVitals(List<ParameterAgendaDto> vitals) {
        this.vitals = vitals;
    }

    public List<ParameterAgendaDto> getTests() {
        return tests;
    }

    public void setTests(List<ParameterAgendaDto> tests) {
        this.tests = tests;
    }

    public List<ParameterAgendaDto> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<ParameterAgendaDto> vaccines) {
        this.vaccines = vaccines;
    }

    public boolean isAllDrugDone() {
        return allDrugDone;
    }

    public void setAllDrugDone(boolean allDrugDone) {
        this.allDrugDone = allDrugDone;
    }

    public boolean isAllParameterDone() {
        return allParameterDone;
    }

    public void setAllParameterDone(boolean allParameterDone) {
        this.allParameterDone = allParameterDone;
    }

    public List<DrugAgendaDto> getAsNeededDrugs() {
        return asNeededDrugs;
    }

    public void setAsNeededDrugs(List<DrugAgendaDto> asNeededDrugs) {
        this.asNeededDrugs = asNeededDrugs;
    }
}
