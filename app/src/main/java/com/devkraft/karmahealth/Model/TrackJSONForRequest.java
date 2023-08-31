package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TrackJSONForRequest {
    @Expose
    @SerializedName("minBaselineValue")
    public String minBaselineValue;
    @Expose
    @SerializedName("maxBaselineValue")
    public String maxBaselineValue;
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
    public String userParameterTrackingId;
    @Expose
    @SerializedName("notes")
    public String notes;
    @Expose
    @SerializedName("source")
    public String source;
    @Expose
    @SerializedName("taken")
    public String taken;
    @Expose
    @SerializedName("sheduledDate")
    public String sheduledDate;
}
