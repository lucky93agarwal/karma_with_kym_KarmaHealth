package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestTimingsModel {
    @Expose
    @SerializedName("Monday")
    public ArrayList<String> Monday;


    @Expose
    @SerializedName("Tuesday")
    public ArrayList<String> Tuesday;


    @Expose
    @SerializedName("Wednesday")
    public ArrayList<String> Wednesday;


    @Expose
    @SerializedName("Thursday")
    public ArrayList<String> Thursday;


    @Expose
    @SerializedName("Friday")
    public ArrayList<String> Friday;

    @Expose
    @SerializedName("Saturday")
    public ArrayList<String> Saturday;
}
