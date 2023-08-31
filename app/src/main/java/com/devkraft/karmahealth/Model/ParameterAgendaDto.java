package com.devkraft.karmahealth.Model;

public class ParameterAgendaDto {

    private Long parameterId;
    private String parameterName;
    private String nextDueDate;
    private boolean parameterTaken;
    private Long userParameterTrackingId;
    private Long userParameterId;
    private Double minBaseLineValue;
    private String minBaseLineDisplayName;
    private Double maxBaseLineValue;
    private String maxBaseLineDisplayName;
    private String measurementUnit;


    public Double getMinBaseLineValue() {
        return minBaseLineValue;
    }

    public void setMinBaseLineValue(Double minBaseLineValue) {
        this.minBaseLineValue = minBaseLineValue;
    }

    public String getMinBaseLineDisplayName() {
        return minBaseLineDisplayName;
    }

    public void setMinBaseLineDisplayName(String minBaseLineDisplayName) {
        this.minBaseLineDisplayName = minBaseLineDisplayName;
    }

    public Double getMaxBaseLineValue() {
        return maxBaseLineValue;
    }

    public void setMaxBaseLineValue(Double maxBaseLineValue) {
        this.maxBaseLineValue = maxBaseLineValue;
    }

    public String getMaxBaseLineDisplayName() {
        return maxBaseLineDisplayName;
    }

    public void setMaxBaseLineDisplayName(String maxBaseLineDisplayName) {
        this.maxBaseLineDisplayName = maxBaseLineDisplayName;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Long getUserParameterId() {
        return userParameterId;
    }

    public void setUserParameterId(Long userParameterId) {
        this.userParameterId = userParameterId;
    }

    public Long getUserParameterTrackingId() {
        return userParameterTrackingId;
    }

    public void setUserParameterTrackingId(Long userParameterTrackingId) {
        this.userParameterTrackingId = userParameterTrackingId;
    }

    public boolean isParameterTaken() {
        return parameterTaken;
    }

    public void setParameterTaken(boolean parameterTaken) {
        this.parameterTaken = parameterTaken;
    }

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

    public String getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(String nextDueDate) {
        this.nextDueDate = nextDueDate;
    }
}
