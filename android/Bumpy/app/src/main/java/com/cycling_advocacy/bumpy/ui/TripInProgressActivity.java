package com.cycling_advocacy.bumpy.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.achievements.AchievementsViewModel;
import com.cycling_advocacy.bumpy.achievements.db.AchievementEntity;
import com.cycling_advocacy.bumpy.achievements.ui.AchievementCompletedActivity;
import com.cycling_advocacy.bumpy.achievements.util.AchievementManager;
import com.cycling_advocacy.bumpy.entities.GnssData;
import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.location.LocationChangedListener;
import com.cycling_advocacy.bumpy.location.LocationService;
import com.cycling_advocacy.bumpy.motion.MotionManager;
import com.cycling_advocacy.bumpy.motion.VibrationChangedListener;
import com.cycling_advocacy.bumpy.ui.map.MapFragment;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;
import com.github.anastr.speedviewlib.RaySpeedometer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TripInProgressActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationChangedListener,
        VibrationChangedListener {

    private TextView tvDistance;
    private Chronometer chronometerDuration;
    private RaySpeedometer speedometer;
    private RaySpeedometer vibrationMeter;

    private GoogleApiClient googleApiClient;

    private LocationService locationService;
    private MotionManager motionManager;

    private Intent serviceIntent;

    private Trip trip;

    private Set<Achievement> achievements = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceUtil.shouldKeepScreenAwake(this)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        setContentView(R.layout.activity_trip_in_progress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvDistance = findViewById(R.id.tv_distance);
        tvDistance.setText(GeneralUtil.formatDecimal(0.00));
        chronometerDuration = findViewById(R.id.chronometer_duration);

        speedometer = findViewById(R.id.gauge_view_speed);
        speedometer.speedTo(0);
        vibrationMeter = findViewById(R.id.gauge_view_vibration);

        Button btnEndTrip = findViewById(R.id.button_trip_end);
        btnEndTrip.setOnClickListener(view -> {
            stopTracking();

            ArrayList<Achievement> completedAchievements =
                    AchievementManager.manageAchievements(TripInProgressActivity.this, trip, achievements);
            if (!completedAchievements.isEmpty()) {
                Intent intent = new Intent(TripInProgressActivity.this, AchievementCompletedActivity.class);
                intent.putExtra(AchievementCompletedActivity.EXTRA_COMPLETED_ACHIEVEMENTS, completedAchievements);
                startActivity(intent);
            }

            Intent data = new Intent();
            data.putExtra(MapFragment.EXTRA_TRIP, trip);
            setResult(Activity.RESULT_OK, data);
            finish();
        });

        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this)
                .build();

        motionManager = new MotionManager(this, this);

        // gather achievements for later usage when trip is ended
        AchievementsViewModel achievementsViewModel = ViewModelProviders
                .of(this).get(AchievementsViewModel.class);
        achievementsViewModel.achievementsLiveData.observe(this, achievementEntities -> {
            for (AchievementEntity entity : achievementEntities) {
                achievements.add(AchievementManager.convertToAchievement(entity));
            }
        });
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

        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
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

        serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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

        chronometerDuration.start();
        
        locationService.startTracking();
        motionManager.startSensorUpdates(this, trip.getTripUUID());
    }

    public void stopTracking() {
        trip.stop();

        chronometerDuration.stop();

        locationService.stopTracking();
        motionManager.stopSensorUpdates();
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

        speedometer.speedTo((int)gnssData.getSpeed(), 10);
        tvDistance.setText(trip.getFormattedDistance());
    }

    @Override
    public void onVibrationChanged(int vibrationPercentage) {
        vibrationMeter.speedTo(vibrationPercentage, 0);
        trip.updateMaxVibration(vibrationPercentage);
    }
}
