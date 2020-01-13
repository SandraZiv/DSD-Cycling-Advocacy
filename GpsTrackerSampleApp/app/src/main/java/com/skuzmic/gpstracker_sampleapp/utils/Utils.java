package com.skuzmic.gpstracker_sampleapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        Activity activity = (Activity)context;
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                activity.finish();
            }
            return false;
        }

        return true;
    }

    public static double convertMetersToKilometers(double meters) {
        return meters / 1000;
    }

    public static float convertMpsToKph(float metersPerSecond) {
        return metersPerSecond * 3.6f;
    }

    public static Date toDate(long timestamp) {
        Date date = new Date();
        date.setTime(timestamp);
        return date;
    }

    public static String formatTimestamp(Date timestamp) {
        // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        return new SimpleDateFormat(DATE_FORMAT).format(timestamp);
    }

    public static long getDurationInSeconds(Date startTS, Date endTS) {
        long diff = endTS.getTime() - startTS.getTime();
        return diff / 1000 % 60;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static void deleteMotionData(Context context, String tripUUID) {
        if (CsvUtils.deleteMotionDataFile(context, tripUUID)) {
            Log.d("Motion data", "Motion data file for trip " + tripUUID + " deleted");
        }
    }
}
