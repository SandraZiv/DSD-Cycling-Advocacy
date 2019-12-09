package com.skuzmic.gpstracker_sampleapp.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {

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

    public static String formatTimestamp(long timestamp) {
        Date date = new Date();
        date.setTime(timestamp);
        // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
    }

    public static Date formatTimestamp(String timestamp) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return format.parse(timestamp);
    }

    public static long getDurationInSeconds(Date startTS, Date endTS) {
        long diff = endTS.getTime() - startTS.getTime();
        return diff / 1000 % 60;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
