package com.cycling_advocacy.bumpy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cycling_advocacy.bumpy.net.DataRetriever;
import com.cycling_advocacy.bumpy.net.model.PastTripDetailedResponse;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;

public class PastTripStatisticsActivity extends AppCompatActivity {

    public static final String EXTRA_TRIP_UUID = "tripUUID";

    private TextView tvTripStatUUID;
    private TextView tvTripStatStartTS;
    private TextView tvTripStatEndTS;
    private TextView tvTripStatDuration;
    private TextView tvTripStatDistance;
    private TextView tvTripStatMaxSpeed;
    private TextView tvTripStatAvgSpeed;
    private TextView tvTripStatMinElevation;
    private TextView tvTripStatMaxElevation;
    private TextView tvTripStatAvgElevation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trip_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        tvTripStatUUID = findViewById(R.id.tv_trip_stat_uuid);
        tvTripStatStartTS = findViewById(R.id.tv_trip_stat_start_ts);
        tvTripStatEndTS = findViewById(R.id.tv_trip_stat_end_ts);
        tvTripStatDuration = findViewById(R.id.tv_trip_stat_duration);
        tvTripStatDistance = findViewById(R.id.tv_trip_stat_distance);
        tvTripStatMaxSpeed = findViewById(R.id.tv_trip_stat_max_speed);
        tvTripStatAvgSpeed = findViewById(R.id.tv_trip_stat_avg_speed);
        tvTripStatMinElevation = findViewById(R.id.tv_trip_stat_min_elevation);
        tvTripStatMaxElevation = findViewById(R.id.tv_trip_stat_max_elevation);
        tvTripStatAvgElevation = findViewById(R.id.tv_trip_stat_avg_elevation);

        DataRetriever.getPastTripStatistics(this, this, getIntent().getStringExtra(EXTRA_TRIP_UUID));
    }

    public void setStatistics (PastTripDetailedResponse statistics) {
        tvTripStatUUID.setText(getString(R.string.trip_stat_uuid, statistics.getTripUUID()));

        if (statistics.getStartTS() != null) {
            tvTripStatStartTS.setText(getString(R.string.trip_stat_start_ts, statistics.getStartTS().toString()));
        }

        if (statistics.getEndTS() != null) {
            tvTripStatEndTS.setText(getString(R.string.trip_stat_end_ts, statistics.getEndTS().toString()));
        }

        if (statistics.getStartTS() != null && statistics.getEndTS() != null) {
            String duration = GeneralUtil.formatDuration(statistics.getStartTS(), statistics.getEndTS());
            tvTripStatDuration.setText(getString(R.string.trip_stat_duration, duration));
        }

        tvTripStatDistance.setText(getString(R.string.trip_stat_distance, statistics.getDistance()));

        if (statistics.getSpeed() != null) {
            PastTripDetailedResponse.Speed speed = statistics.getSpeed();
            tvTripStatMaxSpeed.setText(getString(R.string.trip_stat_max_speed, speed.getMaxSpeed()));
            tvTripStatAvgSpeed.setText(getString(R.string.trip_stat_avg_speed, speed.getAvgSpeed()));
        }

        if (statistics.getElevation() != null) {
            PastTripDetailedResponse.Elevation elevation = statistics.getElevation();
            tvTripStatMinElevation.setText(getString(R.string.trip_stat_min_elevation, elevation.getMinElevation()));
            tvTripStatMaxElevation.setText(getString(R.string.trip_stat_max_elevation, elevation.getMaxElevation()));
            tvTripStatAvgElevation.setText(getString(R.string.trip_stat_avg_elevation, elevation.getAvgElevation()));
        }
    }
}
