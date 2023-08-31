package com.devkraft.karmahealth.Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParameterListAPIResponse {
    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("parameterName")
    public String parameterName;

    @Expose
    @SerializedName("unit")
    public String unit;

    @Expose
    @SerializedName("kymParameterId")
    public String kymParameterId;


}
