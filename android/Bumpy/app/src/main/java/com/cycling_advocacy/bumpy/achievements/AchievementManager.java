package com.cycling_advocacy.bumpy.achievements;

import com.cycling_advocacy.bumpy.achievements.db.AchievementEntity;
import com.cycling_advocacy.bumpy.achievements.db.AchievementsInitial;

public class AchievementManager {

    private static final String COUNT_TOTAL_TRIPS = "COUNT_TOTAL_TRIPS";

    private static final String COUNT_DAILY_TRIPS_TIMESTAMP = "COUNT_DAILY_TRIPS_TIMESTAMP";
    private static final String COUNT_DAILY_TRIPS_COUNT = "COUNT_DAILY_TRIPS_COUNT";

        // save in preference totalTrips

        // save in preference day + trip count



    public static Achievement convertToAchievement(AchievementEntity entity) {
        Achievement achievement = AchievementsInitial.getAchievements()[entity.getId()];
        achievement.setCompleted(entity.isCompleted());
        return achievement;
    }

}
