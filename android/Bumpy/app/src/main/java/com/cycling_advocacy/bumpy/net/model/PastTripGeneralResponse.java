package com.cycling_advocacy.bumpy.net.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PastTripGeneralResponse {

    // TODO: Data from this model should be displayed in the trip list. Add anything else?
    // TODO: At time of writing the backend API response was not updated: Changes might be necessary!

    @SerializedName("trip_uuid")
    private String tripUUID;

    @SerializedName("start_ts")
    private Date startTS;

    @SerializedName("end_ts")
    private Date endTS;

    public PastTripGeneralResponse(String tripUUID, Date startTS, Date endTS) {
        this.tripUUID = tripUUID;
        this.startTS = startTS;
        this.endTS = endTS;
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
}
