package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrofitDataPrescriptionsModel {
    @Expose
    @SerializedName("ConsultationId")//
    public int ConsultationId;

    @Expose
    @SerializedName("ConsultationDate")//
    public String ConsultationDate;


    @Expose
    @SerializedName("patient")//
    public PatientModel patient;

    @Expose
    @SerializedName("Weight")//
    public String Weight;

    @Expose
    @SerializedName("Height")//
    public String Height;

    @Expose
    @SerializedName("Bp_high")//
    public String Bp_high;

    @Expose
    @SerializedName("Bp_low")//
    public String Bp_low;

    @Expose
    @SerializedName("Pulse")//
    public String Pulse;

    @Expose
    @SerializedName("temperature")//
    public String temperature;



    @Expose
    @SerializedName("resp_rate")//
    public String resp_rate;

    @Expose
    @SerializedName("Spo2")//
    public String Spo2;

    @Expose
    @SerializedName("karma_doctor")//
    public KarmaDoctorModel karma_doctor;

    @Expose
    @SerializedName("diagnosis")//
    public String diagnosis;


    @Expose
    @SerializedName("ReferredTo")//
    public String ReferredTo;


    @Expose
    @SerializedName("prescriptions")
    public ArrayList<PrescriptionMedicinesModel> prescription_medicines;


    @Expose
    @SerializedName("prescription_symptoms")//
    public ArrayList<PrescriptionSymptomsModel> prescription_symptoms;


    @Expose
    @SerializedName("PrescriptionComments")//
    public String PrescriptionComments;

    @Expose
    @SerializedName("URL")//
    public String URL;


    @Expose
    @SerializedName("ReviewDate")//
    public String ReviewDate;


}
