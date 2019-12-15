package com.cycling_advocacy.bumpy;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.ntt.customgaugeview.library.GaugeView;

import java.util.Random;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TripInProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_in_progress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        final GaugeView gaugeViewSpeed  =  findViewById(R.id.gauge_view_speed);
        final GaugeView gaugeViewVibration = findViewById(R.id.gauge_view_vibration);
        gaugeViewSpeed.setShowRangeValues(true);
        gaugeViewVibration.setShowRangeValues(true);
        gaugeViewSpeed.setTargetValue(0);
        gaugeViewVibration.setTargetValue(0);
        final Random random = new Random();
        final CountDownTimer timer = new CountDownTimer(10000, 2) {
            @Override
            public void onTick(long millisUntilFinished) {
                gaugeViewSpeed.setTargetValue(random.nextInt(200));
                gaugeViewVibration.setTargetValue(random.nextInt(100));
            }

            @Override
            public void onFinish() {
                gaugeViewSpeed.setTargetValue(0);
                gaugeViewVibration.setTargetValue(0);
            }
        };
        timer.start();

    }
}
