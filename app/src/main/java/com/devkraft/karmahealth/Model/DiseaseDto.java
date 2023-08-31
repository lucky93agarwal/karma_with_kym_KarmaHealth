package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.devkraft.karmahealth.db.NewDiseaseTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DiseaseDto {

    private List<Disease> diseases;
    private List<ParameterDto> vitals;
    private List<ParameterDto> tests;
    private List<ParameterDto> vaccines;

    public DiseaseDto(List<Disease> diseases, List<ParameterDto> vitals, List<ParameterDto> tests, List<ParameterDto> vaccines) {
        this.diseases = diseases;
        this.vitals = vitals;
        this.tests = tests;
        this.vaccines = vaccines;
    }

    public DiseaseDto() {
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public List<ParameterDto> getVitals() {
        return vitals;
    }

    public void setVitals(List<ParameterDto> vitals) {
        this.vitals = vitals;
    }

    public List<ParameterDto> getTests() {
        return tests;
    }

    public void setTests(List<ParameterDto> tests) {
        this.tests = tests;
    }

    public List<ParameterDto> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<ParameterDto> vaccines) {
        this.vaccines = vaccines;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public ContentValues toCV(Long userId) {
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(NewDiseaseTable.USER_ID, userId);
        cv.put(NewDiseaseTable.DISEASE, gson.toJson(diseases));
        cv.put(NewDiseaseTable.VITALS, gson.toJson(vitals));
        cv.put(NewDiseaseTable.TESTS, gson.toJson(tests));
        cv.put(NewDiseaseTable.VACCINES, gson.toJson(vaccines));

        return cv;
    }

    public static DiseaseDto fromCursor(Cursor cursor) {

        String diseaseStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.DISEASE));

        String vitalsStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.VITALS));
        String testStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.TESTS));
        String vaccinesStr = cursor.getString(cursor.getColumnIndex(NewDiseaseTable.VACCINES));


        Gson gson = new Gson();


        DiseaseDto diseaseDto = new DiseaseDto();

        if (vitalsStr != null) {
            Type type = new TypeToken<List<ParameterDto>>() {
            }.getType();
            List<ParameterDto> shortParaList = gson.fromJson(vitalsStr, type);
            diseaseDto.setVitals(shortParaList);
        }

        if (testStr != null) {
            Type type = new TypeToken<List<ParameterDto>>() {
            }.getType();
            List<ParameterDto> longParaList = gson.fromJson(testStr, type);
            diseaseDto.setTests(longParaList);
        }

        if (vaccinesStr != null) {
            Type type = new TypeToken<List<ParameterDto>>() {
            }.getType();
            List<ParameterDto> longParaList = gson.fromJson(vaccinesStr, type);
            diseaseDto.setVaccines(longParaList);
        }

        if (diseaseStr != null) {
            Type diseaseType = new TypeToken<List<Disease>>() {
            }.getType();
            List<Disease> diseaseList = gson.fromJson(diseaseStr, diseaseType);
            diseaseDto.setDiseases(diseaseList);
        }
        return diseaseDto;
    }
}
