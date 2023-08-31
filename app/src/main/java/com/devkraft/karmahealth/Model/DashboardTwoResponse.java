package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardTwoResponse {
    @Expose
    @SerializedName("dosageForm")
    public String dosageFrom;

    @Expose
    @SerializedName("isDelete")
    public Boolean isDelete;

    @Expose
    @SerializedName("drugName")
    public String drugName;

    @Expose
    @SerializedName("dosageType")
    public String dosageType;


    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("shortName")
    public String shortName;

    @Expose
    @SerializedName("whenToTake")
    public String whenToTake;
    @Expose
    @SerializedName("url")
    public String url;
}
