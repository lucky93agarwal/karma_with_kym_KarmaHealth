package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidationlArrayListData {
    @Expose
    @SerializedName("patient_id")
    public String patient_id;

    @Expose
    @SerializedName("patient_name")
    public String patient_name;

    @Expose
    @SerializedName("patient_age")
    public String patient_age;

    @Expose
    @SerializedName("patient_phone")
    public String patient_phone;

    @Expose
    @SerializedName("patient_gender")
    public String patient_gender;
}
