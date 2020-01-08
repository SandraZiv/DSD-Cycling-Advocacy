package com.cycling_advocacy.bumpy.net.model;

import com.cycling_advocacy.bumpy.entities.GnssData;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class PastTripDetailedResponse {


    @SerializedName("tripUUID")
    private String tripUUID;

    @SerializedName("startTS")
    private Date startTS;

    @SerializedName("endTS")
    private Date endTS;

    @SerializedName("distance")
    private double distance;

    @SerializedName("speed")
    private Speed speed;

    @SerializedName("elevation")
    private Elevation elevation;

    @SerializedName("gnssData")
    private List<GnssData> gnssData;

    public PastTripDetailedResponse(String tripUUID, Date startTS, Date endTS, double distance, Speed speed, Elevation elevation, List<GnssData> gnssData) {
        this.tripUUID = tripUUID;
        this.startTS = startTS;
        this.endTS = endTS;
        this.distance = distance;
        this.speed = speed;
        this.elevation = elevation;
        this.gnssData = gnssData;
    }

    public String getTripUUID() { return tripUUID; }

    public void setTripUUID(String tripUUID) { this.tripUUID = tripUUID; }

    public Date getStartTS() { return startTS; }

    public void setStartTS(Date startTS) { this.startTS = startTS; }

    public Date getEndTS() { return endTS; }

    public void setEndTS(Date endTS) { this.endTS = endTS;}

    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }

    public Speed getSpeed() { return speed; }

    public void setSpeed(Speed speed) { this.speed = speed; }

    public Elevation getElevation() { return elevation; }

    public void setElevation(Elevation elevation) { this.elevation = elevation; }

    public List<GnssData> getGnssData() { return gnssData; }

    public void setGnssData(List<GnssData> gnssData) { this.gnssData = gnssData; }

    public class Speed {

        @SerializedName("maxSpeed")
        private double maxSpeed;

        @SerializedName("avgSpeed")
        private double avgSpeed;

        public Speed(double maxSpeed, double avgSpeed) {
            this.maxSpeed = maxSpeed;
            this.avgSpeed = avgSpeed;
        }

        public double getMaxSpeed() { return maxSpeed; }

        public void setMaxSpeed(double maxSpeed) { this.maxSpeed = maxSpeed; }

        public double getAvgSpeed() { return avgSpeed; }

        public void setAvgSpeed(double avgSpeed) { this.avgSpeed = avgSpeed; }
    }

    public class Elevation {

        @SerializedName("minElevation")
        private double minElevation;

        @SerializedName("maxElevation")
        private double maxElevation;

        @SerializedName("avgElevation")
        private double avgElevation;

        public Elevation(double minElevation, double maxElevation, double avgElevation) {
            this.minElevation = minElevation;
            this.maxElevation = maxElevation;
            this.avgElevation = avgElevation;
        }

        public double getMinElevation() { return minElevation; }

        public void setMinElevation(double minElevation) { this.minElevation = minElevation; }

        public double getMaxElevation() { return maxElevation; }

        public void setMaxElevation(double maxElevation) { this.maxElevation = maxElevation; }

        public double getAvgElevation() { return avgElevation; }

        public void setAvgElevation(double avgElevation) { this.avgElevation = avgElevation; }
    }
}
