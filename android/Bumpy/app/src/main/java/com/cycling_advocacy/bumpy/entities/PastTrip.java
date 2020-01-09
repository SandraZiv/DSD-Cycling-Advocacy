package com.cycling_advocacy.bumpy.entities;

import com.cycling_advocacy.bumpy.net.model.PastTripGeneralResponse;
import com.cycling_advocacy.bumpy.pending_trips.PendingTrip;
import com.cycling_advocacy.bumpy.pending_trips.PendingTripsManager;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;

import java.util.Date;

public class PastTrip implements Comparable{

    private String tripUUID;
    private Date startTime;
    private Date endTime;
    private double distance;// in km
    private long duration;  // in seconds
    private boolean isUploaded;

    public PastTrip(PastTripGeneralResponse pastTrip) {
        this.tripUUID = pastTrip.getTripUUID();
        this.startTime = pastTrip.getStartTS();
        this.endTime = pastTrip.getEndTS();
        this.distance = pastTrip.getDistance();
        this.duration = GeneralUtil.getDurationInSeconds(this.startTime, this.endTime);
        this.isUploaded = true;
    }

    public PastTrip(PendingTrip pendingTrip) {
        Trip trip = PendingTripsManager.convertToTrip(pendingTrip);
        if (trip != null) {
            this.tripUUID = trip.getTripUUID();
            this.startTime = trip.getStartTs();
            this.endTime = trip.getStopTs();
            this.distance = trip.getDistance();
            this.duration = GeneralUtil.getDurationInSeconds(this.startTime, this.endTime);
            this.isUploaded = false;
        }
    }

    public String getTripUUID() { return tripUUID; }

    public void setTripUUID(String tripUUID) { this.tripUUID = tripUUID; }

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

    @Override
    public int compareTo(Object o) {
        if (o instanceof PastTrip) {
            PastTrip pastTrip = (PastTrip)o;
            if (!this.isUploaded() && !pastTrip.isUploaded()){
                return pastTrip.getStartTime().compareTo(this.getStartTime());
            }

            if (!this.isUploaded()) {
                return 1;
            }

            if (!pastTrip.isUploaded()) {
                return -1;
            }

            return pastTrip.getStartTime().compareTo(this.getStartTime());
        }
        return 0;
    }
}
