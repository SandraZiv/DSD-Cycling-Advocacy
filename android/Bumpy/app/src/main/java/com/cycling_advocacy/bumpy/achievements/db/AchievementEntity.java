package com.cycling_advocacy.bumpy.achievements.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class AchievementEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
