package com.cycling_advocacy.bumpy.achievements.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

import java.util.Calendar;

public class AchievementsPrefs {

    private static final String COUNT_TOTAL_TRIPS = "COUNT_TOTAL_TRIPS";

    private static final String COUNT_DAILY_TRIPS_TIMESTAMP = "COUNT_DAILY_TRIPS_TIMESTAMP";
    private static final String COUNT_DAILY_TRIPS_COUNT = "COUNT_DAILY_TRIPS_COUNT";

    // daily trips
    public static void increaseDailyTripCount(Context context) {
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(context);

        long currentTimestamp = System.currentTimeMillis();
        long prefTimestamp = preferences.getLong(COUNT_DAILY_TRIPS_TIMESTAMP, currentTimestamp);

        if (isSameDay(currentTimestamp, prefTimestamp)) {
            // increase count
            // timestamp can stay the same since days are same

            // if this is user first trip today
            // method will ensure the trip is set to 1 by passing zero as default value
            increasePrefsCount(preferences, COUNT_DAILY_TRIPS_COUNT, 0);

        } else {
            // start new daily trip count
            preferences.edit()
                    .putLong(COUNT_DAILY_TRIPS_TIMESTAMP, currentTimestamp)
                    .putInt(COUNT_DAILY_TRIPS_COUNT, 1)
                    .apply();
        }
    }

    public static int getDailyTripCount(Context context) {
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(context);

        long currentTimestamp = System.currentTimeMillis();
        long prefTimestamp = preferences.getLong(COUNT_DAILY_TRIPS_TIMESTAMP, currentTimestamp);

        if (isSameDay(currentTimestamp, prefTimestamp)) {
            return preferences.getInt(COUNT_DAILY_TRIPS_COUNT, 0);
        } else {
            return 0;
        }
    }


    // total trips
    // todo should this be from backend?
    // todo should delete trip affect this?
    public static void increaseTotalTripCount(Context context) {
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(context);
        // zero is passed as default value since when increased it will be 1
        increasePrefsCount(preferences, COUNT_TOTAL_TRIPS, 0);
    }

    public static int getTotalTripCount(Context context) {
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(context);
        return preferences.getInt(COUNT_TOTAL_TRIPS, 0);
    }


    // helper methods
    private static void increasePrefsCount(SharedPreferences preferences, String key, int defaultValue) {
        int prefCount = preferences.getInt(key, defaultValue);
        prefCount++;
        preferences.edit().putInt(key, prefCount).apply();
    }

    private static boolean isSameDay(long date1, long date2) {
        // first day
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);

        // second day
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int day2 = calendar1.get(Calendar.DAY_OF_YEAR);
        int year2 = calendar1.get(Calendar.YEAR);

        return year1 == year2 && day1 == day2;
    }

}
