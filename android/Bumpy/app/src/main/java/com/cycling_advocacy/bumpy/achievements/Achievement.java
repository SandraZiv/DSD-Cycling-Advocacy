package com.cycling_advocacy.bumpy.achievements;

import androidx.annotation.StringRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @StringRes
    @ColumnInfo(name = "title_id")
    private int titleId;

    @StringRes
    @ColumnInfo(name = "detail_id")
    private int detailId;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    // using id so it can support internationalization
    public Achievement(@StringRes int titleId, @StringRes int detailId, boolean isCompleted) {
        this.titleId = titleId;
        this.detailId = detailId;
        this.isCompleted = isCompleted;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
