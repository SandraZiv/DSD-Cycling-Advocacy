package com.cycling_advocacy.bumpy.achievements.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.db.BumpyDB;

import java.util.List;
import java.util.concurrent.Executors;

public class AchievementRepository {

    private final AchievementDao achievementDao;
    private LiveData<List<Achievement>> achievements;

    public AchievementRepository(Context context) {
        this.achievementDao = BumpyDB.getInstance(context).achievementDao();
        achievements = achievementDao.getAchievemnts();
    }

    public void updateAsync(final Achievement achievement) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                achievementDao.update(achievement);
            }
        });
    }

    public LiveData<List<Achievement>> getAchievements() {
        return achievements;
    }

}
