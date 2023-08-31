package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionControllModel {
    @Expose
    @SerializedName("needToUpdate")
    public Boolean needToUpdate;
    @Expose
    @SerializedName("forceUpdate")
    public Boolean forceUpdate;
    @Expose
    @SerializedName("version")
    public String version;
    @Expose
    @SerializedName("versionCode")
    public String versionCode;
}
