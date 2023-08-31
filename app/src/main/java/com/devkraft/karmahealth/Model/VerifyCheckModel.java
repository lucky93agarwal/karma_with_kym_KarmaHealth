package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyCheckModel {
/*    @Expose
    @SerializedName("status")
    public String status;*/

    @Expose
    @SerializedName("valid")
    public Boolean valid;
}
