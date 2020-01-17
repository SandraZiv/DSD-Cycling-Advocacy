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

    @SerializedName("vibration")
    private Vibration vibration;

    @SerializedName("gnssData")
    private List<GnssData> gnssData;

    @SerializedName("bumpyPoints")
    private List<BumpyPoint> bumpyPoints;

    public PastTripDetailedResponse(String tripUUID, Date startTS, Date endTS, double distance, Speed speed, Elevation elevation, Vibration vibration, List<GnssData> gnssData, List<BumpyPoint> bumpyPoints) {
        this.tripUUID = tripUUID;
        this.startTS = startTS;
        this.endTS = endTS;
        this.distance = distance;
        this.speed = speed;
        this.elevation = elevation;
        this.vibration = vibration;
        this.gnssData = gnssData;
        this.bumpyPoints = bumpyPoints;
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

    public Vibration getVibration() { return vibration; }

    public void setVibration(Vibration vibration) { this.vibration = vibration; }

    public List<GnssData> getGnssData() { return gnssData; }

    public void setGnssData(List<GnssData> gnssData) { this.gnssData = gnssData; }

    public List<BumpyPoint> getBumpyPoints() { return bumpyPoints; }

    public void setBumpyPoints(List<BumpyPoint> bumpyPoints) { this.bumpyPoints = bumpyPoints; }

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

    public class Vibration {

        @SerializedName("minVibration")
        private double minVibration;

        @SerializedName("maxVibration")
        private double maxVibration;

        @SerializedName("avgVibration")
        private double avgVibration;

        public Vibration(double minVibration, double maxVibration, double avgVibration) {
            this.minVibration = minVibration;
            this.maxVibration = maxVibration;
            this.avgVibration = avgVibration;
        }

        public double getMinVibration() { return minVibration; }

        public void setMinVibration(double minVibration) { this.minVibration = minVibration; }

        public double getMaxVibration() { return maxVibration; }

        public void setMaxVibration(double maxVibration) { this.maxVibration = maxVibration; }

        public double getAvgVibration() { return avgVibration; }

        public void setAvgVibration(double avgVibration) { this.avgVibration = avgVibration; }
    }

    public class BumpyPoint {

        @SerializedName("bumpyScore")
        private Integer bumpyScore;

        @SerializedName("lat")
        private double lat;

        @SerializedName("lon")
        private double lon;

        public BumpyPoint(Integer bumpyScore, double lat, double lon) {
            this.bumpyScore = bumpyScore;
            this.lat = lat;
            this.lon = lon;
        }

        public Integer getBumpyScore() { return bumpyScore; }

        public void setBumpyScore(Integer bumpyScore) { this.bumpyScore = bumpyScore; }

        public double getLat() { return lat; }

        public void setLat(double lat) { this.lat = lat; }

        public double getLon() { return lon; }

        public void setLon(double lon) { this.lon = lon; }
    }
}
