package com.treytomes.notepad;

import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GeorgeTomes on 3/8/2016.
 *
 * Helper methods for accessing the file system.
 */
public class FileSystem {

    private static final String LOG_TAG = "FileSystem";

    public static void writeAllText(File file, String text) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        }
    }

    public static String readAllText(File file) throws IOException {
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        String text = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((text = reader.readLine()) != null) {
                contents.append(text).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
            }
        }

        return contents.toString();
    }

    public static List<File> getFiles(String endsWith) {
        File[] allFiles = getDocumentStoragePath().listFiles();
        List<File> someFiles = new ArrayList<File>();
        for (File file : allFiles) {
            if (file.isFile() && file.getName().endsWith(endsWith)) {
                someFiles.add(file);
            }
        }
        return someFiles;
    }

    /**
     * Get the full path for the file in the user's public documents folder.
     */
    public static File getDocumentStoragePath() {
        // Get the directory for the user's public documents.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created.");
        }
        return file;
    }

    /**
     * Checks if external storage is available for read and write.
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read.
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
