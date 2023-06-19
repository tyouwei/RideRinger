package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//TODO background service that repeatedly calls getLocation every second and compares it with latlng of destination
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button searchButton;
    private Button saveButton;
    private Button settingsButton;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.saved);
        searchButton = findViewById(R.id.find);
        settingsButton = findViewById(R.id.settings);
        gpsTracker = new GPSTracker(MainActivity.this);

        saveButton.setOnClickListener(onSave);
        searchButton.setOnClickListener(onSearch);
        settingsButton.setOnClickListener(onSettings);
    }

    @Override
    protected void onStart() {
        UserSettings.registerSettings(this, this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        UserSettings.unregisterSettings(this, this);
        gpsTracker.stopUsingGPS();
        super.onDestroy();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(i);
        }
    };

    private View.OnClickListener onSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), LoadingScreen.class);
            startActivity(i);
        }
    };

    private View.OnClickListener onSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        }
    };

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == getString(R.string.alarm_notification) || key == getString(R.string.banner_notification)) {
            boolean enabled = UserSettings.getSettings(MainActivity.this, key);
            int flag = (enabled
                    ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
            ComponentName componentName = new ComponentName(MainActivity.this, OnBootReceiver.class);
            getPackageManager().setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP);

            if (enabled) {
                OnBootReceiver.setAlarm(MainActivity.this);
            } else {
                OnBootReceiver.cancelAlarm(MainActivity.this);
            }
        }
    }
}