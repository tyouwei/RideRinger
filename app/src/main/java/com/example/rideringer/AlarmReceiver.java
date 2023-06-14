package com.example.rideringer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(UserSettings.PREFERENCES, Context.MODE_PRIVATE);
        boolean useAlarm = prefs.getBoolean(UserSettings.ALARM_SETTINGS, true);
        boolean useNotification = prefs.getBoolean(UserSettings.NOTIFICATION_SETTINGS, true);

        if (useNotification) {

        } else if (useAlarm) {

        }
    }
}
