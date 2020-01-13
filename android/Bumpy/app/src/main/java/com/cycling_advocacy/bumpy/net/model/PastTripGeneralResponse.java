package com.cycling_advocacy.bumpy.net.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PastTripGeneralResponse {

    // TODO: Data from this model should be displayed in the trip list. Add anything else?

    @SerializedName("tripUUID")
    private String tripUUID;

    @SerializedName("startTS")
    private Date startTS;

    @SerializedName("endTS")
    private Date endTS;

    @SerializedName("distance")
    private double distance;

    public PastTripGeneralResponse(String tripUUID, Date startTS, Date endTS, double distance) {
        this.tripUUID = tripUUID;
        this.startTS = startTS;
        this.endTS = endTS;
        this.distance = distance;
    }

    public String getTripUUID() {
        return tripUUID;
    }

    public void setTripUUID(String tripUUID) {
        this.tripUUID = tripUUID;
    }

    public Date getStartTS() {
        return startTS;
    }

    public void setStartTS(Date startTS) {
        this.startTS = startTS;
    }

    public Date getEndTS() {
        return endTS;
    }

    public void setEndTS(Date endTS) {
        this.endTS = endTS;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
