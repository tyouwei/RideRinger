package com.example.rideringer;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {
    private static final String PREFERENCES = "preferences";

    public static void saveNotificationSettings(Context context, boolean bool) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getString(R.string.banner_notification), bool);
        editor.apply();
    }

    public static void saveAlarmSettings(Context context, boolean bool) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getString(R.string.alarm_notification), bool);
        editor.apply();
    }

    public static boolean getNotificationSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(context.getString(R.string.banner_notification), false);
    }

    public static boolean getAlarmSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(context.getString(R.string.alarm_notification), false);
    }

    public static boolean getSettings(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static void registerSettings(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterSettings(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
