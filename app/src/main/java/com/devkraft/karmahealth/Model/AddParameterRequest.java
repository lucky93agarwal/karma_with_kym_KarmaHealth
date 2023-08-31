package com.devkraft.karmahealth.Model;

public class AddParameterRequest {

    private String medicalParameterType;
    private String name;
    private String frequencyUnit;
    private String uiText;
    private String measurementUnit;
    private long id;

    public String getMedicalParameterType() {
        return medicalParameterType;
    }

    public void setMedicalParameterType(String medicalParameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public String getUiText() {
        return uiText;
    }

    public void setUiText(String uiText) {
        this.uiText = uiText;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
