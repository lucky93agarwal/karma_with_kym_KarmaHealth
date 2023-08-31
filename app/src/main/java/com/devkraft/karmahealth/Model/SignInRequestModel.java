package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInRequestModel {
    @Expose
    @SerializedName("phone")
    public String phone;
}
