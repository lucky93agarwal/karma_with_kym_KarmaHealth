package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetrofitPatientIDRequest {
    @Expose
    @SerializedName("PatientId")
    public String PatientId;
}
