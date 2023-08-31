package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrescriptionSymptomsModel {
    @Expose
    @SerializedName("Symptom")
    public String Symptom;


    @Expose
    @SerializedName("Duration")
    public String Duration;


    @Expose
    @SerializedName("Severity")
    public String Severity;



}
