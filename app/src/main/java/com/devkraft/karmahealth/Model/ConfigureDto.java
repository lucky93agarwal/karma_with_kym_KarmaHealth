package com.devkraft.karmahealth.Model;

public class ConfigureDto {

    private Long id;
    private Long userId;
    private Long parameterId;
    private Double frequencyNumber;
    private boolean remindFlag;
    private boolean shortTermFlag;
    private String frequencyUnit;
    private String parameterName;
    private String parameterReminderText;
    private String createdDate;
    private Double minBaselineValue;
    private String minBaselineDisplayName;
    private Double maxBaselineValue;
    private String maxBaselineDisplayName;
    private String medicalParameterType;
    //private String unit;
    private String frequencyValue;
    private String nextDueDate;

    private String measurementUnit;
    private Double currentWeight;
    private Double targetWeight;
    private String weightGoal;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public Double getFrequencyNumber() {
        return frequencyNumber;
    }

    public void setFrequencyNumber(Double frequencyNumber) {
        this.frequencyNumber = frequencyNumber;
    }

    public boolean isRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(boolean remindFlag) {
        this.remindFlag = remindFlag;
    }

    public boolean isShortTermFlag() {
        return shortTermFlag;
    }

    public void setShortTermFlag(boolean shortTermFlag) {
        this.shortTermFlag = shortTermFlag;
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterReminderText() {
        return parameterReminderText;
    }

    public void setParameterReminderText(String parameterReminderText) {
        this.parameterReminderText = parameterReminderText;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public String getMaxBaselineDisplayName() {
        return maxBaselineDisplayName;
    }

    public void setMaxBaselineDisplayName(String maxBaselineDisplayName) {
        this.maxBaselineDisplayName = maxBaselineDisplayName;
    }

    public String getMedicalparameterType() {
        return medicalParameterType;
    }

    public void setMedicalparameterType(String medicalparameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    public String getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(String frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    public Double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public Double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(String weightGoal) {
        this.weightGoal = weightGoal;
    }
}
