package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOTPRequest {
    @Expose
    @SerializedName("code")
    public String code;

    @Expose
    @SerializedName("to")
    public String to;

    @Expose
    @SerializedName("pathServiceSid")
    public String pathServiceSid;
}
