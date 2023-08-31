package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddDrugRequest {
    @Expose
    @SerializedName("drugId")
    public String drugId;

    @Expose
    @SerializedName("timing")
    public ArrayList<String> timing = new ArrayList<>();
    @Expose
    @SerializedName("dosageForm")
    public String dosageForm;
    @Expose
    @SerializedName("whenToTake")
    public String whenToTake;

    @Expose
    @SerializedName("startDate")
    public String startDate;

    @Expose
    @SerializedName("endDate")
    public String endDate;
    @Expose
    @SerializedName("dosageType")
    public String dosageType;
}
