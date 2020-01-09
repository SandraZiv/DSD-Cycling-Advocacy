package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtil {

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!isNetworkConnected(context)) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);

        } else {
            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo == null) {
                return false;
            }
            return networkInfo.isConnected();
        }
    }

    public static boolean isWifiOrMobileDataAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!isNetworkConnected(context)) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

        } else {
            NetworkInfo networkInfoWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfoMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfoWifi == null && networkInfoMobile == null) {
                return false;
            } else if (networkInfoWifi == null) {
                return networkInfoMobile.isConnected();
            } else if (networkInfoMobile == null) {
                return networkInfoWifi.isConnected();
            } else {
                return networkInfoWifi.isConnected() || networkInfoMobile.isConnected();
            }
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
