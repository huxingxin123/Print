package com.example.print3.util;

import android.util.Log;

public class LogUtils {

    /**
     * Debug on/off
     */
    private static boolean DEBUG = true;

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * error
     */
    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg + "");
        }
    }

    /**
     * info
     */
    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg + "");
        }
    }

    public static void d(String tag,String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * warning
     */
    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg + "");
        }
    }
}
