package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardResponse {
    @Expose
    @SerializedName("morningDrug")
    public ArrayList<DashboardTwoResponse> morningDrug;

    @Expose
    @SerializedName("morningDrugEndDate")
    public String morningDrugEndDate;

    @Expose
    @SerializedName("afternoonDrug")
    public ArrayList<DashboardTwoResponse> afternoonDrug;

    @Expose
    @SerializedName("afternoonDrugEndDate")
    public String afternoonDrugEndDate;

    @Expose
    @SerializedName("eveningDrug")
    public ArrayList<DashboardTwoResponse> eveningDrug;

    @Expose
    @SerializedName("eveningDrugEndDate")
    public String eveningDrugEndDate;

    @Expose
    @SerializedName("nightDrug")
    public ArrayList<DashboardTwoResponse> nightDrug;

    @Expose
    @SerializedName("nightDrugEndDate")
    public String nightDrugEndDate;
}
