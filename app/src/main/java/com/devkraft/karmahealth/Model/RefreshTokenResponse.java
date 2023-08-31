package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshTokenResponse {
    @Expose
    @SerializedName("accessToken")
    public String accessToken;
    @Expose
    @SerializedName("refreshToken")
    public String refreshToken;
    @Expose
    @SerializedName("tokenType")
    public String tokenType;
}
