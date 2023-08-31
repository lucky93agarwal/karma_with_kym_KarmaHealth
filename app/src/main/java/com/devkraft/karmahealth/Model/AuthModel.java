package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthModel {
    @Expose
    @SerializedName("status")
    public Boolean status;
    @Expose
    @SerializedName("message")
    public String message;
}
