package com.cycling_advocacy.bumpy.net.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoadQualitySegmentsResponse {

    @SerializedName("pathUUID")
    private String pathUUID;

    @SerializedName("segments")
    private List<Segment> segments;

    public RoadQualitySegmentsResponse(String pathUUID, List<Segment> segments) {
        this.pathUUID = pathUUID;
        this.segments = segments;
    }

    public String getPathUUID() { return pathUUID; }

    public void setPathUUID(String pathUUID) { this.pathUUID = pathUUID; }

    public List<Segment> getSegments() { return segments; }

    public void setSegments(List<Segment> segments) { this.segments = segments; }

    public class Segment {

        @SerializedName("segmentUUID")
        private String segmentUUID;

        @SerializedName("startLat")
        private double startLat;

        @SerializedName("startLon")
        private double startLon;

        @SerializedName("endLat")
        private double endLat;

        @SerializedName("endLon")
        private double endLon;

        @SerializedName("qualityScore")
        private double qualityScore;

        public Segment(String segmentUUID, double startLat, double startLon, double endLat, double endLon, double qualityScore) {
            this.segmentUUID = segmentUUID;
            this.startLat = startLat;
            this.startLon = startLon;
            this.endLat = endLat;
            this.endLon = endLon;
            this.qualityScore = qualityScore;
        }

        public String getSegmentUUID() { return segmentUUID; }

        public void setSegmentUUID(String segmentUUID) { this.segmentUUID = segmentUUID; }

        public double getStartLat() { return startLat; }

        public void setStartLat(double startLat) { this.startLat = startLat; }

        public double getStartLon() { return startLon; }

        public void setStartLon(double startLon) { this.startLon = startLon; }

        public double getEndLat() { return endLat; }

        public void setEndLat(double endLat) { this.endLat = endLat; }

        public double getEndLon() { return endLon; }

        public void setEndLon(double endLon) { this.endLon = endLon; }

        public double getQualityScore() { return qualityScore; }

        public void setQualityScore(double qualityScore) { this.qualityScore = qualityScore; }
    }
}
