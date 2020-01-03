package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import java.net.InetAddress;

public class NetworkUtil {

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!isNetworkConnected(cm)) {
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
        if (!isNetworkConnected(cm)) {
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

    private static boolean isNetworkConnected(ConnectivityManager cm) {
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected()
                && isInternetAvailable();
    }

    // checks if device is connected to a network but there is no internet
    private static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !"".equals(ipAddr.toString());

        } catch (Exception e) {
            return false;
        }
    }
}
