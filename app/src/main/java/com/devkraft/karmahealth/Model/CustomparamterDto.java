package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.devkraft.karmahealth.db.NewDiseaseTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CustomparamterDto {
    private List<ParamDto> vitals;
    private List<ParamDto> tests;
    private List<ParamDto> vaccinations;

    public CustomparamterDto(List<ParamDto> vitals, List<ParamDto> tests, List<ParamDto> vaccinations) {
        this.vitals = vitals;
        this.tests = tests;
        this.vaccinations = vaccinations;
    }

    public CustomparamterDto() {
    }



    public List<ParamDto> getVitals() {
        return vitals;
    }

    public void setVitals(List<ParamDto> vitals) {
        this.vitals = vitals;
    }

    public List<ParamDto> getTests() {
        return tests;
    }

    public void setTests(List<ParamDto> tests) {
        this.tests = tests;
    }

    public List<ParamDto> getVaccines() {
        return vaccinations;
    }

    public void setVaccines(List<ParamDto> vaccinations) {
        this.vaccinations = vaccinations;
    }


    public ContentValues toCV(Long userId) {
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(NewDiseaseTable.USER_ID, userId);
        cv.put(NewDiseaseTable.VITALS, gson.toJson(vitals));
        cv.put(NewDiseaseTable.TESTS, gson.toJson(tests));
        cv.put(NewDiseaseTable.VACCINES, gson.toJson(vaccinations));

        return cv;
    }

    public static CustomparamterDto fromCursor(Cursor cursor) {


        String vitalsStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.VITALS));
        String testStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.TESTS));
        String vaccinesStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.VACCINES));


        Gson gson = new Gson();


        CustomparamterDto customparamterDto = new CustomparamterDto();

        if (vitalsStr != null) {
            Type type = new TypeToken<List<ParamDto>>() {
            }.getType();
            List<ParamDto> shortParaList = gson.fromJson(vitalsStr, type);
            customparamterDto.setVitals(shortParaList);
        }

        if(testStr != null){
            Type type = new TypeToken<List<ParamDto>>() {
            }.getType();
            List<ParamDto> longParaList = gson.fromJson(testStr, type);
            customparamterDto.setTests(longParaList);
        }

        if(vaccinesStr != null){
            Type type = new TypeToken<List<ParameterDto>>() {
            }.getType();
            List<ParamDto> longParaList = gson.fromJson(vaccinesStr, type);
            customparamterDto.setVaccines(longParaList);
        }


        return customparamterDto;
    }



}




