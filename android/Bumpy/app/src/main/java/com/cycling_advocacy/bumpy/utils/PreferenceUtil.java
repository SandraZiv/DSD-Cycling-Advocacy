package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PreferenceUtil {

    private static final String DEVICE_UUID_KEY = "DEVICE_UUID_KEY";
    private static final String DEVICE_UUID_DEFAULT = "";

    private static final String SHOULD_SHOW_ONBOARDING_KEY = "SHOULD_SHOW_ONBOARDING_KEY";
    private static final boolean SHOULD_SHOW_ONBOARDING_DEFAULT = true;

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setOnboardingScreenIsShown(Context context) {
        getSharedPreference(context).edit().putBoolean(SHOULD_SHOW_ONBOARDING_KEY, false).apply();
    }

    public static boolean shouldShowOnboardingScreen(Context context) {
        return getSharedPreference(context).getBoolean(SHOULD_SHOW_ONBOARDING_KEY, SHOULD_SHOW_ONBOARDING_DEFAULT);
    }

    // this is should be called only once
    public static void setDeviceUUID(Context context) {
        SharedPreferences preferences = getSharedPreference(context);
        if (DEVICE_UUID_DEFAULT.equals(preferences.getString(DEVICE_UUID_KEY, DEVICE_UUID_DEFAULT))) {
            String uuid = GeneralUtil.generateUUID();
            preferences.edit().putString(DEVICE_UUID_KEY, uuid).apply();
        }
    }

    public static String getDeviceUUID(Context context) {
        return getSharedPreference(context).getString(DEVICE_UUID_KEY, DEVICE_UUID_DEFAULT);
    }
}
