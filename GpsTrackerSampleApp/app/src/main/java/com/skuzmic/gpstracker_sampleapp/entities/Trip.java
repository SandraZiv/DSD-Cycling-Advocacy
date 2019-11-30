package com.skuzmic.gpstracker_sampleapp.entities;

import android.content.Context;
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
    private String uuid;
    @SerializedName("tripUUID")
    private String tripId;
    @SerializedName("startTS")
    private String startTs;  // in format 2017-07-21T17:32:28Z
    @SerializedName("endTS")
    private String stopTs;
    @SerializedName("distance")
    private double distance = 0;
    @SerializedName("gnssData")
    private List<GnssData> gnssDataList;

    //TODO: I suppose the uuid refers to tripUUID, not that it matters too much for the demo app
    public Trip(String uuid) {
        this.uuid = "dsfasgdgagsdgsd";
        this.tripId = uuid;
        this.gnssDataList = new LinkedList<>();
    }

    public void start() {
        this.startTs = Utils.formatTimestamp(System.currentTimeMillis());
    }

    public void stop() {
        this.stopTs = Utils.formatTimestamp(System.currentTimeMillis());
        // todo distance calculation (avg speed * duration?)
    }

    public void addGpsData(GnssData data) {
        this.gnssDataList.add(data);
    }

    public String getTripId() {
        return tripId;
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

        File file = new File(baseDirectory, tripId + ".txt");
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
                "uuid=" + uuid + '\n' +
                ", tripId=" + tripId + '\n' +
                ", startTs=" + startTs + '\n' +
                ", stopTs=" + stopTs + '\n' +
                ", distance=" + distance + '\n' +
                "{ " + gnssDataString + "}" +
                '}';
    }
}
