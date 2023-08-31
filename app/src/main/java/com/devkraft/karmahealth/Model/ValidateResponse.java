package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateResponse {
    @Expose
    @SerializedName("validation")
    public boolean validation;
}
