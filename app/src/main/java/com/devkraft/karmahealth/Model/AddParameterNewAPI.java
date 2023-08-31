package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddParameterNewAPI {
    @Expose
    @SerializedName("parameterId")
    public String parameterId;

    @Expose
    @SerializedName("unit")
    public String unit;

    @Expose
    @SerializedName("frequencyNumber")
    public String frequencyNumber;

    @Expose
    @SerializedName("frequencyUnit")
    public String frequencyUnit;

    @Expose
    @SerializedName("MinBaselineValue")
    public String minValue;

    @Expose
    @SerializedName("MaxBaselineValue")
    public String maxValue;


}
