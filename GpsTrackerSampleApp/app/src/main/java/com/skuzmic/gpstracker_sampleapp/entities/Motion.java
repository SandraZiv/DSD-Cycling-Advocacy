package com.skuzmic.gpstracker_sampleapp.entities;

import androidx.annotation.NonNull;

import com.skuzmic.gpstracker_sampleapp.utils.Utils;

public class Motion {

    // timestamp stays as String because we don't do anything with it just export it as csv row
    private String timestamp;
    private String accelerometerString;
    private String magnetometerString;
    private String gyroscopeString;

    public Motion(float[] accelerometerData, float[] magnetometerData, float[] gyroscopeData) {
        this.timestamp = Utils.formatTimestamp(Utils.toDate(System.currentTimeMillis()));
        this.accelerometerString = accelerometerData[0] + "," + accelerometerData[1] + "," + accelerometerData[2];
        this.magnetometerString = magnetometerData[0] + "," + magnetometerData[1] + "," + magnetometerData[2];
        this.gyroscopeString = gyroscopeData[0] + "," + gyroscopeData[1] + "," + gyroscopeData[2];
    }

    @NonNull
    @Override
    public String toString() {
        return timestamp + "," + accelerometerString + "," + magnetometerString + "," + gyroscopeString;
    }
}
