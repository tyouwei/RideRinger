package com.example.rideringer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarm(context);
    }

    public static void setAlarm(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), getPendingIntent(context));
    }

    public static void cancelAlarm(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent i = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE);
    }
}
