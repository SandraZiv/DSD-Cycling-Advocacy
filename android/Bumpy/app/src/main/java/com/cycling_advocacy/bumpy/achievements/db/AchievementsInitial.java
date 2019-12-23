package com.cycling_advocacy.bumpy.achievements.db;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.entities.Trip;

import java.util.Calendar;

public class AchievementsInitial {

    public static AchievementEntity[] getDbEntities() {
        Achievement[] achievements = getAchievements();
        AchievementEntity[] entities = new AchievementEntity[achievements.length];

        for (int i = 0; i < achievements.length; i++) {
            Achievement a = achievements[i];

            AchievementEntity entity = new AchievementEntity();
            entity.setId(a.getId());
            entity.setCompleted(a.isCompleted());

            entities[i] = entity;
        }


        return entities;
    }

    public static Achievement[] getAchievements() {
        return new Achievement[]{a1, a2, a3, a4, a5, a6, a7, a8};
    }

    private static Achievement a1 = new Achievement(
            1,
            R.string.a1_title,
            R.string.a1_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    return false;
                }
            }
    );

    private static Achievement a2 = new Achievement(
            2,
            R.string.a2_title,
            R.string.a2_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(trip.getStartTs());

                    Calendar stopCalendar = Calendar.getInstance();
                    stopCalendar.setTime(trip.getStopTs());

                    int start = startCalendar.get(Calendar.HOUR_OF_DAY);
                    int stop = stopCalendar.get(Calendar.HOUR_OF_DAY);

                    return !(start >= 6 && stop < 22);
                }
            }
    );

    private static Achievement a3 = new Achievement(
            3,
            R.string.a3_title,
            R.string.a3_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    return false;
                }
            }

    );

    private static Achievement a4 = new Achievement(
            4,
            R.string.a4_title,
            R.string.a4_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    return false;
                }
            }
    );

    private static Achievement a5 = new Achievement(
            5,
            R.string.a5_title,
            R.string.a5_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    return false;
                }
            }
    );

    private static Achievement a6 = new Achievement(
            6,
            R.string.a6_title,
            R.string.a6_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    return trip.getDistance() > 5;
                }
            }
    );

    private static Achievement a7 = new Achievement(
            7,
            R.string.a7_title,
            R.string.a7_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    return trip.getDistance() > 20;
                }
            }
    );

    private static Achievement a8 = new Achievement(
            8,
            R.string.a8_title,
            R.string.a8_detail,
            false,
            new Achievement.AchievementCondition() {
                @Override
                public boolean isCompleted(Trip trip) {
                    long twoHours = 2 * 60 * 60 * 1000;  // in millis
                    return (trip.getStopTs().getTime() - trip.getStartTs().getTime()) > twoHours;
                }
            }
    );
}
