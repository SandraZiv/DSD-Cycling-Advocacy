package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.widget.ImageButton;

public class PastTrip {

    private String startTime, endTime, vibration, date;
    private ImageButton upload_image;

    public PastTrip() {
    }

    public PastTrip(String startTime, String endTime, String vibration, String date, ImageButton upload_image) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.vibration = vibration;
        this.date = date;
        this.upload_image = upload_image;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getVibration() {
        return vibration;
    }

    public String getDate() {
        return date;
    }

    public ImageButton getUpload_image() {
        return upload_image;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUpload_image(ImageButton upload_image) {
        this.upload_image = upload_image;
    }
}
