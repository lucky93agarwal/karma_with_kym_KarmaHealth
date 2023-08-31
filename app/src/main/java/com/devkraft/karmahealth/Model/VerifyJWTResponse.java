package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyJWTResponse {

    @Expose
    @SerializedName("token")
    public String token;
}
