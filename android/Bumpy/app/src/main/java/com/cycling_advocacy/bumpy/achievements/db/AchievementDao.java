package com.cycling_advocacy.bumpy.achievements.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cycling_advocacy.bumpy.achievements.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {
    // define methods that access the db

    @Query("Select * from achievements")
    LiveData<List<Achievement>> getAchievemnts();

    @Insert
    void insertAll(Achievement... achievements);

    @Update
    void update(Achievement achievement);
}
