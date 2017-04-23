package in.agrostar.ulink.clothpicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import in.agrostar.ulink.clothpicker.Constants;

/**
 * Created by ayush on 20/4/17.
 */

public class SharedPreferencesUtil {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static SharedPreferencesUtil instance;

    private static void initializeSharedPref(Context context) {
        instance = new SharedPreferencesUtil();
        instance.sharedPreferences = context.getSharedPreferences(
                Constants.PREFERENCES_FILENAME, Activity.MODE_PRIVATE);
        instance.editor = instance.sharedPreferences
                .edit();
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (instance == null) {
            initializeSharedPref(context);
        }
        return instance;
    }

    private SharedPreferencesUtil() {
        // TODO Auto-generated constructor stub
    }

    public synchronized boolean saveData(String key, String value) {
        Logger.logInfo("saving " + key + " = " + value);
        editor.putString(key, value);
        return editor.commit();
    }

    public synchronized boolean saveData(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public synchronized boolean saveData(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }


    public synchronized boolean saveData(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }


    public synchronized boolean saveData(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }

    public synchronized boolean removeData(String key) {
        editor.remove(key);
        return editor.commit();
    }

    public synchronized Boolean getData(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public synchronized String getData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public synchronized float getData(String key, float defaultValue) {

        return sharedPreferences.getFloat(key, defaultValue);
    }

    public synchronized int getData(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public synchronized long getData(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }
}