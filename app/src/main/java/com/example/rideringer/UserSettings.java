package com.example.rideringer;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    public static void registerPrefs(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPrefs(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
