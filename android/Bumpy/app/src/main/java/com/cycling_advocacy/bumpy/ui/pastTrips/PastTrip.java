package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.widget.ImageButton;

public class PastTrip {

    private String start_time, end_time, vibration, date;
    private ImageButton upload_image;

    public PastTrip() {
    }

    public PastTrip(String start_time, String end_time, String vibration, String date, ImageButton upload_image) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.vibration = vibration;
        this.date = date;
        this.upload_image = upload_image;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
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

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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
