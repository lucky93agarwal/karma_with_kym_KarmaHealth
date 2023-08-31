package com.devkraft.karmahealth.Model;

import java.util.List;
import java.util.Objects;

public class ParameterDto {

    private Long id;
    private String name;
    private String frequency;
    private Double frequencyNumber;
    private String informationLink;
    private String medicalParameterType;
    private Double minBaselineValue;
    private String minBaselineDisplayName;
    private Double maxBaselineValue;
    private Double margin;
    private String maxBaselineDisplayName;
    private String measurementUnit;
    private String nextDueDate;
    private String lastRecordedDate;
    private List<Values> values;
    private String frequencyUnit;
    private Long userParameterId;
    private String measured;
    private String notes;
    private Boolean active;
    private String compositeIndividual;
    private boolean showAlbuminLevelValue;
    private boolean isDelete;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterDto that = (ParameterDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(frequency, that.frequency) &&
                Objects.equals(frequencyNumber, that.frequencyNumber) &&
                Objects.equals(informationLink, that.informationLink) &&
                Objects.equals(medicalParameterType, that.medicalParameterType) &&
                Objects.equals(minBaselineValue, that.minBaselineValue) &&
                Objects.equals(minBaselineDisplayName, that.minBaselineDisplayName) &&
                Objects.equals(maxBaselineValue, that.maxBaselineValue) &&
                Objects.equals(margin, that.margin) &&
                Objects.equals(maxBaselineDisplayName, that.maxBaselineDisplayName) &&
                Objects.equals(measurementUnit, that.measurementUnit) &&
                Objects.equals(nextDueDate, that.nextDueDate) &&
                Objects.equals(lastRecordedDate, that.lastRecordedDate) &&
                Objects.equals(values, that.values) &&
                Objects.equals(frequencyUnit, that.frequencyUnit) &&
                Objects.equals(userParameterId, that.userParameterId) &&
                Objects.equals(measured, that.measured) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, frequency, frequencyNumber, informationLink, medicalParameterType, minBaselineValue, minBaselineDisplayName, maxBaselineValue, margin, maxBaselineDisplayName, measurementUnit, nextDueDate, lastRecordedDate, values, frequencyUnit, userParameterId, measured, notes);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMeasured() {
        return measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }

    public Long getId() {
        return id;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Double getFrequencyNumber() {
        return frequencyNumber;
    }

    public void setFrequencyNumber(Double frequencyNumber) {
        this.frequencyNumber = frequencyNumber;
    }

    public String getInformationLink() {
        return informationLink;
    }

    public void setInformationLink(String informationLink) {
        this.informationLink = informationLink;
    }

    public String getMedicalParameterType() {
        return medicalParameterType;
    }

    public void setMedicalParameterType(String medicalParameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    public Double getMinBaselineValue() {
        return minBaselineValue;
    }

    public void setMinBaselineValue(Double minBaselineValue) {
        this.minBaselineValue = minBaselineValue;
    }

    public String getMinBaselineDisplayName() {
        return minBaselineDisplayName;
    }

    public void setMinBaselineDisplayName(String minBaselineDisplayName) {
        this.minBaselineDisplayName = minBaselineDisplayName;
    }

    public Double getMaxBaselineValue() {
        return maxBaselineValue;
    }

    public void setMaxBaselineValue(Double maxBaselineValue) {
        this.maxBaselineValue = maxBaselineValue;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public String getMaxBaselineDisplayName() {
        return maxBaselineDisplayName;
    }

    public void setMaxBaselineDisplayName(String maxBaselineDisplayName) {
        this.maxBaselineDisplayName = maxBaselineDisplayName;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public String getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(String nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public String getLastRecordedDate() {
        return lastRecordedDate;
    }

    public void setLastRecordedDate(String lastRecordedDate) {
        this.lastRecordedDate = lastRecordedDate;
    }

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public Long getUserParameterId() {
        return userParameterId;
    }

    public void setUserParameterId(Long userParameterId) {
        this.userParameterId = userParameterId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean mActive) {
        active = mActive;
    }

    public String getCompositeIndividual() {
        return compositeIndividual;
    }

    public void setCompositeIndividual(String compositeIndividual) {
        this.compositeIndividual = compositeIndividual;
    }

    public boolean getShowAlbuminLevelValue() {
        return showAlbuminLevelValue;
    }

    public void setShowAlbuminLevelValue(Boolean showAlbuminLevelValue) {
        this.showAlbuminLevelValue = showAlbuminLevelValue;
    }
}
