package com.ronda.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: Ronda(1575558177@qq.com)
 * Date: 2017/10/05
 * Version: v1.0
 * <p>
 * 对SharedPreferences的封装
 */

public class PrefUtils {

    private static final String SP_NAME = "config";

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).commit();
    }


    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).commit();
    }


    public static String getString(Context context, String key, String defValue) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }
}
