package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        SharedPreferences prefs = getApplicationContext().
                getSharedPreferences(getApplicationContext().
                        getString(R.string.prefs), Context.MODE_PRIVATE);


        OnBootReceiver.cancelAlarm(getApplicationContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("track", false);
        editor.apply();
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
    }
}