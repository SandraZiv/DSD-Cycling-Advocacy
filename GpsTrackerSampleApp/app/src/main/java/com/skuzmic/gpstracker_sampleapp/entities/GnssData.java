package com.skuzmic.gpstracker_sampleapp.entities;

import android.location.Location;

import androidx.annotation.NonNull;

import com.skuzmic.gpstracker_sampleapp.utils.Utils;

public class GnssData {

    private String timestamp;  // in format 2017-07-21T17:32:28Z
    private double lat;
    private double lon;
    private double ele;
    private float speed;
    private double accuracy;

    public GnssData(Location location) {
        this.timestamp = Utils.formatTimestamp(location.getTime());
        this.lat = location.getLatitude();
        this.lon = location.getLongitude();
        this.ele = location.getAltitude();
        this.speed = location.getSpeed();
        this.accuracy = location.getAccuracy();
    }

    @NonNull
    @Override
    public String toString() {
        return timestamp + "\n" +
                "Lat: " + lat + "\n" +
                "Lon: " + lon + "\n" +
                "Ele: " + ele + "\n" +
                "Speed: " + speed + "\n" +
                "Acc: " + accuracy;
    }
}
