package com.cycling_advocacy.bumpy.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import okhttp3.ResponseBody;

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
        if (motionDataFile != null) {
            return motionDataFile.delete();
        }
        return false;
    }

    public static boolean writeResponseBodyToFile(ResponseBody body, Context context, Uri uri) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = context.getContentResolver().openOutputStream(uri);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return true;

            } catch (IOException e) {
                return false;

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
