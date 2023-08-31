package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

public class RetrofitPatientModel {
    @Expose
    @SerializedName("Doctor Name")
    public String dname;
    @Expose
    @SerializedName("Gender")
    public String Gender;
    @Expose
    @SerializedName("Timing")
    public String Timing;
    @Expose
    @SerializedName("Qualification")
    public String Qualification;
    @Expose
    @SerializedName("Speciality")
    public String Speciality;
    @Expose
    @SerializedName("Experience_Hindi")
    public String Experience_Hindi;
    @Expose
    @SerializedName("Experience_English")
    public String Experience_English;

    @Expose
    @SerializedName("DoctorImage")
    public String DoctorImage;
    @Expose
    @SerializedName("Timings")
    public Object Timings;


}
