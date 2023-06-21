package com.example.rideringer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.Manifest;
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

import java.util.concurrent.TimeUnit;

//TODO background service that repeatedly calls getLocation every second and compares it with latlng of destination
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button searchButton;
    private Button saveButton;
    private Button settingsButton;
    private String[] backgroundLocationPermission = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    private PermissionsManager permissionsManager;
    private LocationManager locationManager;
    private WorkRequest backgroundWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.saved);
        searchButton = findViewById(R.id.find);
        settingsButton = findViewById(R.id.settings);
        permissionsManager = PermissionsManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);

        saveButton.setOnClickListener(onSave);
        searchButton.setOnClickListener(onSearch);
        settingsButton.setOnClickListener(onSettings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserSettings.registerSettings(this, this);
        if (!permissionsManager.checkPermissions(backgroundLocationPermission)) {
            permissionsManager.askPermissions(this, backgroundLocationPermission, 200);
        } else {
            startLocationWork();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.stopLocationUpdates();
        WorkManager.getInstance(MainActivity.this).cancelAllWork();
        UserSettings.unregisterSettings(this, this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissionsManager.handlePermissionResult(MainActivity.this, 200, permissions, grantResults)) {
            startLocationWork();
        }
    }
    private void startLocationWork() {
        backgroundWorkRequest = new OneTimeWorkRequest.Builder(BackgroundLocation.class)
                .addTag("LocationWork")
                .setBackoffCriteria(BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(MainActivity.this).enqueue(backgroundWorkRequest);
    }
}