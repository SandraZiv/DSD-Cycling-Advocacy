package com.cycling_advocacy.bumpy.achievements.util;

import android.content.Context;

import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.achievements.AchievementCondition;
import com.cycling_advocacy.bumpy.achievements.db.AchievementEntity;
import com.cycling_advocacy.bumpy.achievements.db.AchievementsInitial;
import com.cycling_advocacy.bumpy.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {

    public static ArrayList<Achievement> manageAchievements(Context context, Trip trip, List<Achievement> achievements) {
        // todo trip condition check?
        AchievementsPrefs.increaseDailyTripCount(context);
        AchievementsPrefs.increaseTotalTripCount(context);

        // check for completed achievement
        ArrayList<Achievement> completedAchievements = new ArrayList<>();

        for (Achievement achievement : achievements) {
            AchievementCondition condition = achievement.getCondition();
            if (!achievement.isCompleted()
                    && (condition.isCompleted(context) || condition.isCompleted(trip))) {
                achievement.setCompleted(true);
                completedAchievements.add(achievement);
            }
        }

        return completedAchievements;
    }

    public static Achievement convertToAchievement(AchievementEntity entity) {
        Achievement achievement = AchievementsInitial.getAchievements()[entity.getId()-1];
        achievement.setCompleted(entity.isCompleted());
        return achievement;
    }

    public static AchievementEntity convertToEntity(Achievement achievement) {
        AchievementEntity entity = new AchievementEntity();
        entity.setId(achievement.getId());
        entity.setCompleted(achievement.isCompleted());
        return entity;
    }

}
