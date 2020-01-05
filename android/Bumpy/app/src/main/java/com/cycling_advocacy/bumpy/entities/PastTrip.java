package com.cycling_advocacy.bumpy.entities;

// TODO: Is this for the database (locally stored data)? It will probably need to be reworked.
public class PastTrip {

    // todo fix this types
    private String startTime, endTime;
    private double distance;
    // in seconds
    private long duration;
    private boolean isUploaded;

    public PastTrip(String startTime, String endTime, double distance, long duration, boolean isUploaded) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.duration = duration;
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

    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }

    public long getDuration() { return duration; }

    public void setDuration(long duration) { this.duration = duration; }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }
}
