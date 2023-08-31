package com.devkraft.karmahealth.Model;

import java.util.List;

public class SymptomsDTO {

    private Long parameterId;
    private String parameterName;
    private String medicalParameterType;
    private List<String> measurementValues;

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getMedicalParameterType() {
        return medicalParameterType;
    }

    public void setMedicalParameterType(String medicalParameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    public List<String> getMeasurementValues() {
        return measurementValues;
    }

    public void setMeasurementValues(List<String> measurementValues) {
        this.measurementValues = measurementValues;
    }
}

