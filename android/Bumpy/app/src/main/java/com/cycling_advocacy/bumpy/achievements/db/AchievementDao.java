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

    @Query("Select * from achievements order by id")
    LiveData<List<AchievementEntity>> getAchievements();

    @Insert
    void insertAll(AchievementEntity... achievements);

    @Update
    void update(AchievementEntity achievement);
}
