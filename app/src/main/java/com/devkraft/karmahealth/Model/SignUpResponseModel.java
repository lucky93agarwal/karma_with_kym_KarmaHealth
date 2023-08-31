package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponseModel {

    @Expose
    @SerializedName("token")
    public String token;

    @Expose
    @SerializedName("refreshToken")
    public String refreshToken;

    @Expose
    @SerializedName("type")
    public String type;


    @Expose
    @SerializedName("id")
    public String id;// kym id


    @Expose
    @SerializedName("username")
    public String username;
}
