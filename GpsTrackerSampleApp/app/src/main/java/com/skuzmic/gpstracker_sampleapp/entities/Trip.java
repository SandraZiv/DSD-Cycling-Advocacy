package com.skuzmic.gpstracker_sampleapp.entities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.skuzmic.gpstracker_sampleapp.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Trip {

    private String uuid;
    private String tripId;
    private String startTs;  // in format 2017-07-21T17:32:28Z
    private String stopTs;
    private double distance = 0;
    private List<GnssData> gnssDataList;

    public Trip(String uuid) {
        this.uuid = "dsfasgdgagsdgsd";
        this.tripId = "fdsf1dsf4sdf1sdf1sd2f3f";
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

        File file = new File(baseDirectory, System.currentTimeMillis() + ".txt");
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
