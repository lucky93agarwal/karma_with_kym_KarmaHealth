package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ValidationLoginResponse {
    @Expose
    @SerializedName("success")
    public Boolean success;

    @Expose
    @SerializedName("message")
    public String message;

    @Expose
    @SerializedName("data")
    public ArrayList<ValidationlArrayListData> data;
}
