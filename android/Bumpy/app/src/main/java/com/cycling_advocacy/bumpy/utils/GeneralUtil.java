package com.cycling_advocacy.bumpy.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GeneralUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String formatTimestamp(Date timestamp) {
        // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        return new SimpleDateFormat(DATE_FORMAT).format(timestamp);
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

    public static double roundDouble(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException("Rounding places must not be less than 0");
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
