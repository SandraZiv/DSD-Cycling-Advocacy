package com.cycling_advocacy.bumpy.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class GeneralUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DURATION_FORMAT = "%d:%02d:%02d";

    public static String formatTimestamp(Date timestamp) {
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(timestamp);
    }

    public static Date toDate(long timestamp) {
        Date date = new Date();
        date.setTime(timestamp);
        return date;
    }

    public static long getDurationInSeconds(Date startTS, Date endTS) {
        long diff = endTS.getTime() - startTS.getTime();
        return diff / 1000;
    }

    public static String formatDuration(Date start, Date end) {
        long duration = GeneralUtil.getDurationInSeconds(start, end);
        return String.format(Locale.getDefault(), DURATION_FORMAT,
                duration / 3600,
                (duration % 3600) / 60,
                (duration % 60));
    }


    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

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
}
