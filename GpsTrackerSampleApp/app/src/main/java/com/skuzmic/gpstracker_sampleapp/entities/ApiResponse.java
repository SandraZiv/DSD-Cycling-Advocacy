package com.skuzmic.gpstracker_sampleapp.entities;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

//TODO This serves as the model for POST response bodies (ex: note the backend API for /insertNewTrip); can this be handled better? Do we even need a response for those cases?
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
