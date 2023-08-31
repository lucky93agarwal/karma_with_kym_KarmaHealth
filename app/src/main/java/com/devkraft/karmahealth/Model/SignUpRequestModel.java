package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpRequestModel {
    @Expose
    @SerializedName("patient_id")
    public String patient_id;

    @Expose
    @SerializedName("patient_name")
    public String patient_name;


    @Expose
    @SerializedName("patient_age")
    public String patient_age;


    @Expose
    @SerializedName("patient_phone")
    public String patient_phone;


    @Expose
    @SerializedName("patient_gender")
    public String patient_gender;




    @Expose
    @SerializedName("patient_email")
    public String patient_email;

    @Expose
    @SerializedName("patient_createdts")
    public String patient_createdts;


    @Expose
    @SerializedName("patient_updatedts")
    public String patient_updatedts;


    @Expose
    @SerializedName("patient_dob")
    public String patient_dob;


    @Expose
    @SerializedName("patient_location")
    public String patient_location;

    @Expose
    @SerializedName("patient_village")
    public String patient_village;


    @Expose
    @SerializedName("password")
    public String password;
}
