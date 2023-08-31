package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrugListAPIResponse {

    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("medicineName")
    public String medicineName;


    @Expose
    @SerializedName("company")
    public String company;



    @Expose
    @SerializedName("kym_synonym_id")
    public String kym_synonym_id;





}
