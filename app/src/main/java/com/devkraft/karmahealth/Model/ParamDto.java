package com.devkraft.karmahealth.Model;

import java.util.Objects;

public class ParamDto {
    private long id;
    private String name;
    private String uiText;
    private String link;
    private Double minBaselineValue;
    private String minBaselineDisplayName;
    private Double maxBaselineValue;
    private Double margin;
    private String maxBaselineDisplayName;
    private String measurementUnit;
    private String nextDueDate;
    private String lastRecordedDate;
    private String frequencyUnit;
    private Double frequencyNumber;
    private String frequencyValue;
    private String medicalParameterType;

    public String getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(String frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    public String getMedicalParameterType() {
        return medicalParameterType;
    }

    public void setMedicalParameterType(String medicalParameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUiText() {
        return uiText;
    }

    public void setUiText(String uiText) {
        this.uiText = uiText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public Double getFrequencyNumber() {
        return frequencyNumber;
    }

    public void setFrequencyNumber(Double frequencyNumber) {
        this.frequencyNumber = frequencyNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamDto)) return false;
        ParamDto paramDto = (ParamDto) o;
        return id == paramDto.id &&
                Objects.equals(name, paramDto.name) &&
                Objects.equals(uiText, paramDto.uiText) &&
                Objects.equals(link, paramDto.link) &&
                Objects.equals(minBaselineValue, paramDto.minBaselineValue) &&
                Objects.equals(minBaselineDisplayName, paramDto.minBaselineDisplayName) &&
                Objects.equals(maxBaselineValue, paramDto.maxBaselineValue) &&
                Objects.equals(margin, paramDto.margin) &&
                Objects.equals(maxBaselineDisplayName, paramDto.maxBaselineDisplayName) &&
                Objects.equals(measurementUnit, paramDto.measurementUnit) &&
                Objects.equals(nextDueDate, paramDto.nextDueDate) &&
                Objects.equals(lastRecordedDate, paramDto.lastRecordedDate) &&
                Objects.equals(frequencyUnit, paramDto.frequencyUnit) &&
                Objects.equals(frequencyNumber, paramDto.frequencyNumber)&&
                Objects.equals(frequencyValue, paramDto.frequencyValue)&&
                Objects.equals(medicalParameterType, paramDto.medicalParameterType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uiText, link, minBaselineValue, minBaselineDisplayName, maxBaselineValue, margin, maxBaselineDisplayName, measurementUnit, nextDueDate, lastRecordedDate, frequencyUnit, frequencyNumber, frequencyValue, medicalParameterType);
    }
}