package com.devkraft.karmahealth.Model;


import java.util.ArrayList;

public class SymptomRequest {

    private ArrayList<Symptom> symptoms;

    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public static class Symptom {
        private Long symptomId;

        public Long getSymptomId() {
            return symptomId;
        }

        public void setSymptomId(Long symptomId) {
            this.symptomId = symptomId;
        }
    }
}
