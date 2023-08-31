package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TrackConfigurationDto {

    @Expose
    @SerializedName("minBaselineValue")
    public Double minBaselineValue;
    @Expose
    @SerializedName("maxBaselineValue")
    public Double maxBaselineValue;
    @Expose
    @SerializedName("minBaselineDisplayName")
    public String minBaselineDisplayName;

    @Expose
    @SerializedName("maxBaselineDisplayName")
    public String maxBaselineDisplayName;
    @Expose
    @SerializedName("measurementUnit")
    public String measurementUnit;
    @Expose
    @SerializedName("recordedDate")
    public String recordedDate;
    @Expose
    @SerializedName("measured")
    public String measured;
    @Expose
    @SerializedName("userParameterTrackingId")
    public Long userParameterTrackingId;
    @Expose
    @SerializedName("notes")
    public String notes;
    @Expose
    @SerializedName("source")
    public String source;
    @Expose
    @SerializedName("taken")
    public Boolean taken;
    @Expose
    @SerializedName("sheduledDate")
    public Date sheduledDate;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getUserParameterTrackingId() {
        return userParameterTrackingId;
    }

    public void setUserParameterTrackingId(Long userParameterTrackingId) {
        this.userParameterTrackingId = userParameterTrackingId;
    }

    public String getMeasured() {
        return measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }

    public Double getMinBaselineValue() {
        return minBaselineValue;
    }

    public void setMinBaselineValue(Double minBaselineValue) {
        this.minBaselineValue = minBaselineValue;
    }

    public Double getMaxBaselineValue() {
        return maxBaselineValue;
    }

    public void setMaxBaselineValue(Double maxBaselineValue) {
        this.maxBaselineValue = maxBaselineValue;
    }

    public String getMinBaselineDisplayName() {
        return minBaselineDisplayName;
    }

    public void setMinBaselineDisplayName(String minBaselineDisplayName) {
        this.minBaselineDisplayName = minBaselineDisplayName;
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

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }

    public Date getSheduledDate() {
        return sheduledDate;
    }

    public void setSheduledDate(Date sheduledDate) {
        this.sheduledDate = sheduledDate;
    }
}
