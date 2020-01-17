package com.cycling_advocacy.bumpy.motion;

import java.util.LinkedList;

class VibrationManager {

    // The size of the window (linked list of bumps) used for evaluation
    private static final int WINDOW_SIZE = 100;

    // Coefficient Used to evaluate if the current accelerometer reading is a spike compared to the previous one
    private static final double BUMP_THRESHOLD = 0.7;

    // List of N integers representing accelerometer readings where 1 represents a bump (spike in data), 0 represents a non-bump
    private LinkedList<Integer> bumpWindow = new LinkedList<>();

    // The last accelerometer data reading; used to determine if the current reading is a bump (data spike) compared to the last one
    // Initially, this value is null and the first reading can be treated as a non-bump
    private float[] lastAccelerometerMeasurement = null;

    // The number of bumps
    private int nBumps;

    VibrationManager() {
        for (int i = 0; i < WINDOW_SIZE; i++) {
            // Initially there are no bumps in the evaluated window
            bumpWindow.add(0);
        }
        nBumps = 0;
    }

    void addAccelerometerMeasurement(float[] accelerometerMeasurement) {
        // Remove the oldest value in the evaluated window
        int removedValue = bumpWindow.removeFirst();
        if (removedValue == 1) {
            nBumps--;
        }

        if (lastAccelerometerMeasurement != null) {
            float absPrevZ = Math.abs(lastAccelerometerMeasurement[2]);
            float absZ = Math.abs(accelerometerMeasurement[2]);
            float diffZ = Math.abs(absPrevZ - absZ);

            if (diffZ >= BUMP_THRESHOLD * absPrevZ) {
                bumpWindow.add(1);
                nBumps++;
            } else {
                bumpWindow.add(0);
            }
        }

        lastAccelerometerMeasurement = accelerometerMeasurement;
    }

    double getBumpPercentage() {
        return ((float) nBumps / WINDOW_SIZE) * 100;
    }
}
