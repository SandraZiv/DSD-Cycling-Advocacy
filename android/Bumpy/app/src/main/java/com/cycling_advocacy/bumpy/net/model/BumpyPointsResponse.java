package com.cycling_advocacy.bumpy.net.model;

import com.google.gson.annotations.SerializedName;

public class BumpyPointsResponse {

    @SerializedName("bumpyScore")
    private int bumpyScore;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    public BumpyPointsResponse(int bumpyScore, double lat, double lon) {
        this.bumpyScore = bumpyScore;
        this.lat = lat;
        this.lon = lon;
    }

    public int getBumpyScore() { return bumpyScore; }

    public void setBumpyScore(int bumpyScore) { this.bumpyScore = bumpyScore; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }

    public void setLon(double lon) { this.lon = lon; }
}
