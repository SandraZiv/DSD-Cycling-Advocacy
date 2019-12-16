package com.cycling_advocacy.bumpy.net;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

// This serves as the model for POST response body (ex: note the backend API for /insertNewTrip)
public class ApiResponse {

    @SerializedName("code")
    public String code;
    @SerializedName("type")
    public String type;
    @SerializedName("message")
    public String message;

    public ApiResponse(String code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return code + ", " + type + ", " + message;
    }
}
