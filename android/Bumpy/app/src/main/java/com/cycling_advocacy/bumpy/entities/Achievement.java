package com.cycling_advocacy.bumpy.entities;

public class Achievement {

    private String title;
    private String detail;
    private boolean isCompleted;

    public Achievement(String title, String detail, boolean isCompleted) {
        this.title = title;
        this.detail = detail;
        this.isCompleted = isCompleted;
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

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
