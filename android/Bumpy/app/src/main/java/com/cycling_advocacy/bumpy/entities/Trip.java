package com.cycling_advocacy.bumpy.entities;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.cycling_advocacy.bumpy.utils.GeneralUtil;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Trip implements Serializable {

    @SerializedName("deviceUUID")
    private String deviceUUID;
    @SerializedName("tripUUID")
    private String tripUUID;
    @SerializedName("startTS")
    private Date startTs;
    @SerializedName("endTS")
    private Date stopTs;
    @SerializedName("distance")
    private double distance = 0.0;
    @SerializedName("gnssData")
    private List<GnssData> gnssDataList;

    public Trip(Context context) {
        this.deviceUUID = PreferenceUtil.getLongDeviceUUID(context);
        this.tripUUID = GeneralUtil.generateUUID();
        this.gnssDataList = new LinkedList<>();
    }

    public void start() {
        this.startTs = GeneralUtil.toDate(System.currentTimeMillis());
    }

    public void stop() {
        this.stopTs = GeneralUtil.toDate(System.currentTimeMillis());
    }

    public void addGpsData(GnssData data) {
        this.gnssDataList.add(data);
        calculateNewDistance();
    }

    // Calculates distance (adds distance for new GNSS points) based on the distance between the last two GNSS points
    private void calculateNewDistance() {
        int gnssListSize = gnssDataList.size();
        if (gnssListSize > 1) {
            GnssData point1 = gnssDataList.get(gnssListSize - 2);
            GnssData point2 = gnssDataList.get(gnssListSize - 1);
            float[] results = new float[1];

            // return distance in meters
            Location.distanceBetween(point1.getLat(), point1.getLon(), point2.getLat(), point2.getLon(), results);
            // convert to kms
            distance += (results[0] / 1000);
        }
    }

    public String getTripUUID() {
        return tripUUID;
    }

    public double getDistance() {
        return distance;
    }

    public String getFormattedDistance(){
        return GeneralUtil.formatDecimal(distance);
    }

    public Date getStartTs() {
        return startTs;
    }

    public Date getStopTs() {
        return stopTs;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder gnssDataString = new StringBuilder();
        for (GnssData data : gnssDataList) {
            gnssDataString.append(data.toString()).append("\n\n");
        }

        return "Trip{" +
                "deviceUUID=" + deviceUUID + '\n' +
                ", tripUUID=" + tripUUID + '\n' +
                ", startTs=" + GeneralUtil.formatTimestampISO(startTs) + '\n' +
                ", stopTs=" + GeneralUtil.formatTimestampISO(stopTs) + '\n' +
                ", distance=" + distance + '\n' +
                "{ " + gnssDataString + "}" +
                '}';
    }
}