package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetSymptomsResponseDTO {

    private Long parameterId;
    private String parameterName;
    private String medicalParameterType;
    private List<String> measurementValues;
    private String measurementType;
    private String selectedSymptom;
    private Boolean isSelected;

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

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String getSelectedSymptom() {
        return selectedSymptom;
    }

    public void setSelectedSymptom(String selectedSymptom) {
        this.selectedSymptom = selectedSymptom;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetSymptomsResponseDTO)) return false;
        GetSymptomsResponseDTO that = (GetSymptomsResponseDTO) o;
        return getParameterId() == that.getParameterId() &&
                getParameterName().equals(that.getParameterName()) &&
                getMedicalParameterType().equals(that.getMedicalParameterType()) &&
                getMeasurementValues() == that.getMeasurementValues() &&
                getMeasurementType().equals(that.getMeasurementType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParameterId(), getParameterName(), getMedicalParameterType(),getMeasurementValues(),getMeasurementType());
    }

    public static class GetSymptomsListResponse extends ArrayList<GetSymptomsResponseDTO> {
    }
}
