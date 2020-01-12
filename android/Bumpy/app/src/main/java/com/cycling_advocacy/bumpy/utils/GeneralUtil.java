package com.cycling_advocacy.bumpy.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class GeneralUtil {

    public static final String DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DURATION_FORMAT = "%d:%02d:%02d";

    public static String formatTimestampISO(Date timestamp) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_ISO, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(timestamp);
    }

    public static String formatTimestampLocale(Date timestamp){
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
        return df.format(timestamp);
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

    public static String formatDecimal(double data) {
        return String.format(Locale.getDefault(), "%.2f", data);
    }

    public static String formatNoDecimal(double data) {
        return String.format(Locale.getDefault(), "%.0f", data);
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
