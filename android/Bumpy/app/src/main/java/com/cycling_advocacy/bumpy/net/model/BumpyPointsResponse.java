package com.cycling_advocacy.bumpy.net.model;

import com.google.gson.annotations.SerializedName;

public class BumpyPointsResponse {

    @SerializedName("bumpyScore")
    private Integer bumpyScore;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    public BumpyPointsResponse(Integer bumpyScore, double lat, double lon) {
        this.bumpyScore = bumpyScore;
        this.lat = lat;
        this.lon = lon;
    }

    public Integer getBumpyScore() { return bumpyScore; }

    public void setBumpyScore(Integer bumpyScore) { this.bumpyScore = bumpyScore; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }

    public void setLon(double lon) { this.lon = lon; }
}
