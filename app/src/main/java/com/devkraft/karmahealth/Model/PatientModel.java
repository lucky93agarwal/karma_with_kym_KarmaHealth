package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientModel {
    @Expose
    @SerializedName("PatientName")
    public String PatientName;

    @Expose
    @SerializedName("Age")
    public String Age;

    @Expose
    @SerializedName("Gender")
    public String Gender;

    @Expose
    @SerializedName("Village")
    public String Village;
}
