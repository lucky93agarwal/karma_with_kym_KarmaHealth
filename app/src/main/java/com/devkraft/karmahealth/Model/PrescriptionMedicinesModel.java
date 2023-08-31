package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrescriptionMedicinesModel {
    @Expose
    @SerializedName("MedicineName")
    public String MedicineName;

    @Expose
    @SerializedName("Quantity")
    public String Quantity;

    @Expose
    @SerializedName("Route")
    public String Route;

    @Expose
    @SerializedName("Timing")
    public String Timing;


    @Expose
    @SerializedName("Days")
    public String Days;



    @Expose
    @SerializedName("Dose")
    public String Dose;


    @Expose
    @SerializedName("WhenToTake")
    public String WhenToTake;
}
