package com.skuzmic.gpstracker_sampleapp.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CsvUtils {

    public static FileWriter initFileWriter(Context context, String filename) throws IOException {
        File baseDirectory;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Environment.getExternalStorageDirectory() -> /BumpyMotion
            // Android/data/<package name>/files/BumpyMotion
            baseDirectory = new File(context.getExternalFilesDir(null), "BumpyMotion");
            baseDirectory.mkdirs();
        } else {
            // todo handle when can't read external storage
            return null;
        }

        File file = new File(baseDirectory, System.currentTimeMillis() + ".csv");
        Log.d("csvv", file.getPath());

        return new FileWriter(file, true);
    }

    public static void writeLine(Writer w, String value) throws IOException {
        Log.d("csv", value);
        w.append(value);
        w.append("\n");
    }

    public static void finish(Writer w) throws IOException {
        w.flush();
        w.close();
    }

}
