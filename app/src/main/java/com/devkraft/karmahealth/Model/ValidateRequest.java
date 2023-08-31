package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateRequest {
    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("patientId")
    public String patientId;
}
