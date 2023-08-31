package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrofitPrescriptionsModel {
    @Expose
    @SerializedName("success")
    public Boolean success;
    @Expose
    @SerializedName("data")
    public ArrayList<RetrofitDataPrescriptionsModel> data;
    @Expose
    @SerializedName("message")
    public String message;
}
