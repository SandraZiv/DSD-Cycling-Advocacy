package com.cycling_advocacy.bumpy.entities;

public class PastTrip {

    // todo fix this types
    private String startTime, endTime, vibration;
    private boolean isUploaded;

    public PastTrip(String startTime, String endTime, String vibration, boolean isUploaded) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.vibration = vibration;
        this.isUploaded = isUploaded;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public String getVibration() {
        return vibration;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }
}
