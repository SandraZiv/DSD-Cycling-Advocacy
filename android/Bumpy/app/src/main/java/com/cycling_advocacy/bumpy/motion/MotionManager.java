package com.cycling_advocacy.bumpy.motion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.cycling_advocacy.bumpy.entities.Motion;
import com.cycling_advocacy.bumpy.utils.CsvMotionUtil;

import java.io.FileWriter;
import java.io.IOException;

public class MotionManager implements SensorEventListener {

    private VibrationManager accumulatedVibrationsManager;

    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer, gyroscope;

    private float[] accelerometerData = null;
    private float[] magnetometerData = null;
    private float[] gyroscopeData = null;

    private FileWriter fileWriter;

    private VibrationChangedListener listener;

    public MotionManager(Context context, VibrationChangedListener listener) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // todo check for nulls if the device does not have that sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        this.listener = listener;
    }

    public void startSensorUpdates(Context context, String tripUUID) {
        accumulatedVibrationsManager = new VibrationManager();

        try {
            fileWriter = CsvMotionUtil.initFileWriter(context, tripUUID);
            // TODO: Is there a good way to link this to motion data writing, to ensure that the order of sensors in the header is the same as the values
            CsvMotionUtil.writeHeader(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);  // 50hz
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSensorUpdates() {
        accumulatedVibrationsManager = null;

        try {
            CsvMotionUtil.finish(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);
        sensorManager.unregisterListener(this, gyroscope);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // sensors are working in background as well
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = sensorEvent.values.clone();
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerData = sensorEvent.values.clone();
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeData = sensorEvent.values.clone();
        }

        if (accelerometerData != null && magnetometerData != null && gyroscopeData != null) {
            Motion motion = new Motion(accelerometerData, magnetometerData, gyroscopeData);
            try {
                CsvMotionUtil.writeLine(fileWriter, motion.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            accumulatedVibrationsManager.addAccelerometerMeasurement(accelerometerData);
            listener.onVibrationChanged((int) accumulatedVibrationsManager.getBumpPercentage());

            // clean
            accelerometerData = null;
            magnetometerData = null;
            gyroscopeData = null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
