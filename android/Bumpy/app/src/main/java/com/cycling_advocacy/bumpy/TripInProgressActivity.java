package com.cycling_advocacy.bumpy;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.ntt.customgaugeview.library.GaugeView;

import java.util.Random;

public class TripInProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_in_progress);

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
                gaugeViewSpeed.setTargetValue(random.nextInt(50));
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
