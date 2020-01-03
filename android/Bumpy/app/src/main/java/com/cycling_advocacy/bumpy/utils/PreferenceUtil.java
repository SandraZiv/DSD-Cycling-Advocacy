package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.TripUploadType;

public class PreferenceUtil {

    private static final String DEVICE_UUID_KEY = "DEVICE_UUID_KEY";
    private static final String DEVICE_UUID_DEFAULT = "";

    private static final String SHOULD_SHOW_ONBOARDING_KEY = "SHOULD_SHOW_ONBOARDING_KEY";
    private static final boolean SHOULD_SHOW_ONBOARDING_DEFAULT = true;

    public static SharedPreferences getSharedPreference(Context context) {
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

    public static boolean shouldKeepScreenAwake(Context context) {
        return PreferenceUtil.getSharedPreference(context)
                .getBoolean(
                        context.getString(R.string.pref_keep_awake_key),
                        context.getResources().getBoolean(R.bool.pref_keep_awake_default)
                );
    }

    public static TripUploadType getTripUploadType(Context context) {
        String key = PreferenceUtil.getSharedPreference(context)
                .getString(
                        context.getString(R.string.pref_upload_trip_data_key),
                        TripUploadType.WIFI.name()
                );

        return TripUploadType.valueOf(key);
    }
}
