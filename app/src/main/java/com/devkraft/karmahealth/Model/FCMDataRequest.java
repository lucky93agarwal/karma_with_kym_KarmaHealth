package com.devkraft.karmahealth.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMDataRequest {
    @Expose
    @SerializedName("userId")
    public String userId;

    @Expose
    @SerializedName("fcmToken")
    public String fcmToken;

    @Expose
    @SerializedName("source")
    public String source;

}
