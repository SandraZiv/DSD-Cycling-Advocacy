package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CsvMotionUtil {

    private static final String BUMPY_MOTION_FOLDER = "BumpyMotion";
    private static final String CSV_EXTENSION = ".csv";

    public static FileWriter initFileWriter(Context context, String filename) throws IOException {
        File baseDirectory;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Environment.getExternalStorageDirectory() -> /BumpyMotion
            // Android/data/<package name>/files/BumpyMotion
            baseDirectory = new File(context.getExternalFilesDir(null), BUMPY_MOTION_FOLDER);
            baseDirectory.mkdirs();
        } else {
            // todo handle when can't read external storage
            return null;
        }

        File file = new File(baseDirectory, filename + CSV_EXTENSION);

        return new FileWriter(file, true);
    }

    public static void writeHeader(Writer w) throws IOException {
        w.append("timestamp,accelerometerX,accelerometerY,accelerometerZ,magnetometerX,magnetometerY,magnetometerZ,gyroscopeX,gyroscopeY,gyroscopeZ");
        w.append("\n");
    }

    public static void writeLine(Writer w, String value) throws IOException {
        w.append(value);
        w.append("\n");
    }

    public static void finish(Writer w) throws IOException {
        w.flush();
        w.close();
    }

    public static File getMotionDataFile(Context context, String tripUUID) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File motionDataFile = new File(context.getExternalFilesDir(null), BUMPY_MOTION_FOLDER);
            motionDataFile = new File(motionDataFile, tripUUID + CSV_EXTENSION);
            return motionDataFile;
        } else {
            // todo handle when can't read external storage
            return null;
        }
    }

    public static boolean deleteMotionDataFile(Context context, String tripUUID) {
        File motionDataFile = getMotionDataFile(context, tripUUID);
        return motionDataFile.delete();
    }
}
