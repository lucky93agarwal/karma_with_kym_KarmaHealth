package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserDetailsResponse {

    @Expose
    @SerializedName("id")
    public String id;// kym id


    @Expose
    @SerializedName("patientId")
    public String patientId;// karma


    @Expose
    @SerializedName("patientName")
    public String patientName;


    @Expose
    @SerializedName("phone")
    public String phone;


    @Expose
    @SerializedName("age")
    public String age;



    @Expose
    @SerializedName("gender")
    public String gender;
}
