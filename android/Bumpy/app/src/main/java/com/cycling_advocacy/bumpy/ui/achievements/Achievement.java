package com.cycling_advocacy.bumpy.ui.achievements;

import android.media.Image;
import android.widget.ImageView;

public class Achievement {

    private String title, detail;
    private ImageView trophy;

    public Achievement() {
    }

    public Achievement(String title, String detail, ImageView trophy) {
        this.title = title;
        this.detail = detail;
        this.trophy = trophy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ImageView getTrophy() {
        return trophy;
    }

    public void setTrophy(ImageView trophy) {
        this.trophy = trophy;
    }
}
