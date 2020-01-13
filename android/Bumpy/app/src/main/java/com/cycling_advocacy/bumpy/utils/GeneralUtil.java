package com.cycling_advocacy.bumpy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

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

    public static String formatInt(int data) {
        return String.format(Locale.getDefault(), "%d", data);
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

    public static int getColorFromRoadQuality(Double quality) {
        if (quality == null) {
            return Color.GRAY;
        } else if (quality < 0.1) {
            return Color.parseColor("#B02727");
        } else if (quality >= 0.1 && quality < 0.2) {
            return Color.parseColor("#FC0303");
        } else if (quality >= 0.2 && quality < 0.3) {
            return Color.parseColor("#FF5900");
        } else if (quality >= 0.3 && quality < 0.4) {
            return Color.parseColor("#FF8C00");
        } else if (quality >= 0.4 && quality < 0.5) {
            return Color.parseColor("#FFBF00");
        } else if (quality >= 0.5 && quality < 0.6) {
            return Color.parseColor("#FFFF00");
        } else if (quality >= 0.6 && quality < 0.7) {
            return Color.parseColor("#CDEB0C");
        } else if (quality >= 0.7 && quality < 0.8) {
            return Color.parseColor("#0CEB4E");
        } else if (quality >= 0.8 && quality < 0.9) {
            return Color.parseColor("#00B837");
        } else {
            return Color.parseColor("#078D2F");
        }
    }
}
