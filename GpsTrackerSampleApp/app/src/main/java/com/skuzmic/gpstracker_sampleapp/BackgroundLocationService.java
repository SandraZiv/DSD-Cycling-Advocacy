package com.skuzmic.gpstracker_sampleapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class BackgroundLocationService extends Service {

    private static final long UPDATE_INTERVAL = 3000;
    private static final long FASTEST_INTERVAL = 3000; // = 3 seconds

    private BackgroundLocationServiceBinder binder = new BackgroundLocationServiceBinder();

    private FusedLocationProviderClient locationProviderClient;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    Log.d("aabc", "Update");
                    if (listener != null) {
                        listener.onLocationChanged(location);
                    }
                }
            }
        }
    };

    private BackgroundLocationChangesListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        // todo ili u startTracking?
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // todo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(12345, getNotification(getApplicationContext()));
        }
    }

    @Override
    public void onDestroy() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(12345);

        super.onDestroy();
        if (locationProviderClient != null) {
            locationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    public void startTracking() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        // todo? Looper
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void stopTracking() {
        this.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification(Context context) {
        NotificationChannel channel = new NotificationChannel(
                "channel_01",
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        // ili Context.NOTIFICATION_SERVICE
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        Notification.Builder builder = new Notification.Builder(context, "channel_01");
        return builder.build();
    }

    public void setListener(BackgroundLocationChangesListener listener) {
        this.listener = listener;
    }

    public class BackgroundLocationServiceBinder extends Binder {
        public BackgroundLocationService getService() {
            return BackgroundLocationService.this;
        }
    }



    public interface BackgroundLocationChangesListener {
        public void onLocationChanged(Location location);
    }
}
