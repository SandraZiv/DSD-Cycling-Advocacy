package com.skuzmic.gpstracker_sampleapp.entities;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.skuzmic.gpstracker_sampleapp.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Trip {

    @SerializedName("deviceUUID")
    private String deviceUUID;
    @SerializedName("tripUUID")
    private String tripUUID;
    @SerializedName("startTS")
    private String startTs;  // in format 2017-07-21T17:32:28Z
    @SerializedName("endTS")
    private String stopTs;
    @SerializedName("distance")
    private double distance = 0.0;
    @SerializedName("gnssData")
    private List<GnssData> gnssDataList;

    //TODO: Device uuid will be stored/managed elsewhere, therefore the parameter received in this constructor is for the purposes of the alpha-prototype only!
    public Trip(String deviceUUID) {
        this.deviceUUID = deviceUUID;

        this.tripUUID = Utils.generateUUID();
        Log.d("Trip UUID", "Generated trip UUID: " + tripUUID);

        this.gnssDataList = new LinkedList<>();
    }

    public void start() {
        this.startTs = Utils.formatTimestamp(System.currentTimeMillis());
    }

    public void stop() {
        this.stopTs = Utils.formatTimestamp(System.currentTimeMillis());
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

            Location.distanceBetween(point1.getLat(), point1.getLon(), point2.getLat(), point2.getLon(), results);
            distance += results[0];
        }
    }

    public String getTripUUID() {
        return tripUUID;
    }

    public void exportToTxt(Context context)  {
        Log.d("loc", this.toString());

        File baseDirectory;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            baseDirectory = new File(context.getExternalFilesDir(null), "BumpyTrips");
            baseDirectory.mkdirs();
        } else {
            // todo handle when can't read external storage
            return;
        }

        File file = new File(baseDirectory, tripUUID + ".txt");
        try {
            FileWriter w = new FileWriter(file, true);
            w.append(this.toString());
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder gnssDataString = new StringBuilder();
        for (GnssData data: gnssDataList) {
            gnssDataString.append(data.toString()).append("\n\n");
        }

        return "Trip{" +
                "deviceUUID=" + deviceUUID + '\n' +
                ", tripUUID=" + tripUUID + '\n' +
                ", startTs=" + startTs + '\n' +
                ", stopTs=" + stopTs + '\n' +
                ", distance=" + distance + '\n' +
                "{ " + gnssDataString + "}" +
                '}';
    }
}
