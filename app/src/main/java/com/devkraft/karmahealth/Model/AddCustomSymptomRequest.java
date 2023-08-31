package com.devkraft.karmahealth.Model;

import java.util.ArrayList;

public class AddCustomSymptomRequest {

    private ArrayList<CustomSymptom> symptoms;

    public void setSymptoms(ArrayList<CustomSymptom> symptoms) {
        this.symptoms = symptoms;
    }

    public ArrayList<AddCustomSymptomRequest.CustomSymptom> getSymptoms() {
        return symptoms;
    }

    public static class CustomSymptom {
        private String name;
        private String measurementType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMeasurementType() {
            return measurementType;
        }

        public void setMeasurementType(String measurementType) {
            this.measurementType = measurementType;
        }

    }
}
