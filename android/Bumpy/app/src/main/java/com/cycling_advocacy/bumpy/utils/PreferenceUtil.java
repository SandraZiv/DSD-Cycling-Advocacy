package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.TripUploadType;
import com.cycling_advocacy.bumpy.net.service.BumpyService;
import com.cycling_advocacy.bumpy.net.service.BumpyServiceBuilder;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PreferenceUtil {

    private static final String DEVICE_LONG_UUID_KEY = "DEVICE_LONG_UUID_KEY";
    private static final String DEVICE_LONG_UUID_DEFAULT = "";

    private static final String DEVICE_SHORT_UUID_KEY = "DEVICE_SHORT_UUID_KEY";
    private static final String DEVICE_SHORT_UUID_DEFAULT = "";

    private static final String ONBOARDING_VERSION_KEY = "ONBOARDING_VERSION_KEY";
    private static final int ONBOARDING_VERSION_DEFAULT = 0;
    private static final int ONBOARDING_CURRENT_VERSION = 1;

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setOnboardingScreenIsShown(Context context) {
        getSharedPreference(context).edit().putInt(ONBOARDING_VERSION_KEY, ONBOARDING_CURRENT_VERSION).apply();
    }

    public static boolean shouldShowOnboardingScreen(Context context) {
        int onboardingVersion = getSharedPreference(context).getInt(ONBOARDING_VERSION_KEY, ONBOARDING_VERSION_DEFAULT);
        return onboardingVersion < ONBOARDING_CURRENT_VERSION;
    }

    // this is should be called only once
    public static void setDeviceUUID(Context context) {
        SharedPreferences preferences = getSharedPreference(context);
        String longUUID = preferences.getString(DEVICE_LONG_UUID_KEY, DEVICE_LONG_UUID_DEFAULT);
        if (DEVICE_LONG_UUID_DEFAULT.equals(longUUID)) {
            String uuid = GeneralUtil.generateUUID();
            preferences.edit().putString(DEVICE_LONG_UUID_KEY, uuid).apply();

            setShortUUID(uuid, preferences);

        } else if (DEVICE_SHORT_UUID_DEFAULT.equals(preferences.getString(DEVICE_SHORT_UUID_KEY, DEVICE_SHORT_UUID_DEFAULT))) {
            // only shortUUID is missing
            setShortUUID(longUUID, preferences);
        }
    }

    private static void setShortUUID(String uuid, SharedPreferences preferences) {
        BumpyService bumpyService = BumpyServiceBuilder.createService(BumpyService.class);
        bumpyService.getShortDeviceUUID(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.isSuccessful()) {
                            preferences.edit().putString(DEVICE_SHORT_UUID_KEY, response.body()).apply();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public static String getLongDeviceUUID(Context context) {
        return getSharedPreference(context).getString(DEVICE_LONG_UUID_KEY, DEVICE_LONG_UUID_DEFAULT);
    }

    // if short device is not set, return long uuid
    public static String getShortDeviceUUID(Context context) {
        String uuid = getSharedPreference(context).getString(DEVICE_SHORT_UUID_KEY, DEVICE_SHORT_UUID_DEFAULT);
        if (DEVICE_SHORT_UUID_DEFAULT.equals(uuid)) {
            uuid = getLongDeviceUUID(context);
        }
        return uuid;
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
