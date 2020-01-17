package com.skuzmic.gpstracker_sampleapp.utils;

import java.util.LinkedList;

public class AccumulatedVibrationsManager {

    // The size of the window (linked list of bumps) used for evaluation
    public static final int WINDOW_SIZE = 100;

    // Coefficient Used to evaluate if the current accelerometer reading is a spike compared to the previous one
    public static final double BUMP_THRESHOLD = 0.7;

    // List of N integers representing accelerometer readings where 1 represents a bump (spike in data), 0 represents a non-bump
    private LinkedList<Integer> bumpWindow = new LinkedList<>();

    // The last accelerometer data reading; used to determine if the current reading is a bump (data spike) compared to the last one
    // Initially, this value is null and the first reading can be treated as a non-bump
    private float[] lastAccelerometerMeasurement = null;

    // The number of bumps
    private int nBumps;

    public AccumulatedVibrationsManager() {
        for (int i = 0; i < WINDOW_SIZE; i++) {
            // Initially there are no bumps in the evaluated window
            bumpWindow.add(0);
        }
        nBumps = 0;
    }

    public void addAccelerometerMeasurement(float[] accelerometerMeasurement) {
        // Remove the oldest value in the evaluated window
        int removedValue = bumpWindow.removeFirst();
        if (removedValue == 1) {
            nBumps--;
        }

        if (lastAccelerometerMeasurement != null) {
            //float absPrevX = Math.abs(lastAccelerometerMeasurement[0]);
            //float absPrevY = Math.abs(lastAccelerometerMeasurement[1]);
            float absPrevZ = Math.abs(lastAccelerometerMeasurement[2]);

            //float absX = Math.abs(accelerometerMeasurement[0]);
            //float absY = Math.abs(accelerometerMeasurement[1]);
            float absZ = Math.abs(accelerometerMeasurement[2]);

            //float diffX = Math.abs(absPrevX - absX);
            //float diffY = Math.abs(absPrevY - absY);
            float diffZ = Math.abs(absPrevZ - absZ);

            // TODO: For now we evaluate only along Z-axis, therefore the angle/tilt of the device plays a part (completely horizontal would be ideal)
            // TODO: In the future it would be good if we can evaluate the value of the vector vertical to the ground, so that device angle/tilt does not play a part
            if (diffZ >= BUMP_THRESHOLD * absPrevZ) {
                bumpWindow.add(1);
                nBumps++;
            } else {
                bumpWindow.add(0);
            }
        }

        lastAccelerometerMeasurement = accelerometerMeasurement;
    }

    public double getBumpPercentage() {
        return ((float) nBumps / WINDOW_SIZE) * 100;
    }
}
