package com.cycling_advocacy.bumpy;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.cycling_advocacy.bumpy.entities.GnssData;
import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.location.LocationChangedListener;
import com.cycling_advocacy.bumpy.location.LocationService;
import com.cycling_advocacy.bumpy.motion.MotionManager;
import com.cycling_advocacy.bumpy.motion.VibrationChangedListener;
import com.cycling_advocacy.bumpy.net.DataSender;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class TripInProgressActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationChangedListener,
        VibrationChangedListener {

    private Button btnEndTrip;
    private GoogleApiClient googleApiClient;

    private LocationService locationService;
    private MotionManager motionManager;

    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_in_progress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnEndTrip = findViewById(R.id.button_trip_end);
        btnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
                finish();
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this)
                .build();

        motionManager = new MotionManager(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!GeneralUtil.checkPlayServices(this)) {
            Toast.makeText(this, R.string.google_api_install, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        locationService.stopTracking();
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.grant_location, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        finish();
    }

    public void startTracking() {
        trip = new Trip(this);
        trip.start();

        locationService.startTracking();
        motionManager.startSensorUpdates(this, trip.getTripUUID());
    }

    public void stopTracking() {
        trip.stop();

        locationService.stopTracking();
        motionManager.stopSensorUpdates();

        DataSender.sendData(this, trip);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        private static final String serviceName = "LocationService";

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            String name = componentName.getClassName();
            // it is full name with package id
            if (name.endsWith(serviceName)) {
                locationService = ((LocationService.LocationServiceBinder) iBinder).getService();
                locationService.setListener(TripInProgressActivity.this);

                startTracking();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (componentName.getClassName().endsWith(serviceName)) {
                locationService = null;
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        GnssData gnssData = new GnssData(location);
        trip.addGpsData(gnssData);
        // todo update UI
    }

    @Override
    public void onVibrationChanged(int vibrationPercentage) {
        // todo update UI
    }
}
