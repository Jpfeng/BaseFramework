package com.mix.framework.util;

import android.util.Log;

import com.mix.framework.BuildConfig;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/4
 */
public class Logger {

    private static final boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "CoinRising/" + BuildConfig.VERSION_NAME;

    private Logger() {
    }

    public static void verbose(String msg) {
        Log.v(TAG, msg);
    }

    public static void verbose(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void debug(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void info(String msg) {
        Log.i(TAG, msg);
    }

    public static void info(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void warn(String msg) {
        Log.w(TAG, msg);
    }

    public static void warn(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void error(String msg) {
        Log.e(TAG, msg);
    }

    public static void error(String tag, String msg) {
        Log.e(tag, msg);
    }
}
