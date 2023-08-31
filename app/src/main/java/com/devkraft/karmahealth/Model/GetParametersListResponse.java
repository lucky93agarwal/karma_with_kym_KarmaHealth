package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetParametersListResponse {
    @Expose
    @SerializedName("parameter")
    public String parameter;

    @Expose
    @SerializedName("unit")
    public String unit;

    @Expose
    @SerializedName("frequencyValue")
    public String frequencyValue;

    @Expose
    @SerializedName("createdDate")
    public String createdDate;

    @Expose
    @SerializedName("updatedDate")
    public String updatedDate;
}
