package in.agrostar.ulink.clothpicker.utils;

import android.util.Log;

import in.agrostar.ulink.clothpicker.Constants;

/**
 * Created by ayush on 20/4/17.
 */


public class Logger {
    private static final boolean isLoggerOn = true;
    private static final String TAG = Constants.LOGGER_TAG;

    public static void logError(String message) {
        if (MiscUtils.isLoggerOn())
            Log.e(TAG, message);
    }

    public static void logInfo(String message) {
        if (MiscUtils.isLoggerOn())
            Log.d(TAG, message);
    }
}