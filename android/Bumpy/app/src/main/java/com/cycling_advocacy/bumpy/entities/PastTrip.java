package com.cycling_advocacy.bumpy.entities;

import com.cycling_advocacy.bumpy.utils.GeneralUtil;

import java.util.Date;

// TODO: Is this for the database (locally stored data)? It will probably need to be reworked.
public class PastTrip {

    private Date startTime;
    private Date endTime;
    private double distance;
    // in seconds
    private long duration;
    private boolean isUploaded;

    public PastTrip(Date startTime, Date endTime, double distance, boolean isUploaded) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.duration = GeneralUtil.getDurationInSeconds(startTime, endTime);
        this.isUploaded = isUploaded;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
