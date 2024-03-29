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
import android.content.Context;
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
    private String[] locationPermission = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
    };
    private String[] backgroundLocationPermission = {
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
        permissionsManager = PermissionsManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);

        saveButton.setOnClickListener(onSave);
        searchButton.setOnClickListener(onSearch);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserSettings.registerPrefs(MainActivity.this, MainActivity.this);
        if (!permissionsManager.checkPermissions(locationPermission)) {
            permissionsManager.askPermissions(MainActivity.this, locationPermission, 200);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("track", false);
        editor.apply();
        locationManager.stopLocationUpdates();
        stopLocationWork();
        UserSettings.unregisterPrefs(MainActivity.this, MainActivity.this);
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!permissionsManager.checkPermissions(backgroundLocationPermission)) {
                permissionsManager.askPermissions(MainActivity.this, backgroundLocationPermission, 200);
            } else {
                Intent i = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(i);
            }
        }
    };

    private View.OnClickListener onSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!permissionsManager.checkPermissions(backgroundLocationPermission)) {
                permissionsManager.askPermissions(MainActivity.this, backgroundLocationPermission, 200);
            } else {
                Intent i = new Intent(getApplicationContext(), LoadingScreen.class);
                startActivity(i);
            }
        }
    };

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean track = sharedPreferences.getBoolean("track", false);
        if (track) {
            startLocationWork();
        } else {
            stopLocationWork();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.handlePermissionResult(MainActivity.this, 200, permissions, grantResults);
    }
    private void startLocationWork() {
        backgroundWorkRequest = new OneTimeWorkRequest.Builder(BackgroundLocation.class)
                .addTag("LocationWork")
                .setBackoffCriteria(BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(MainActivity.this).enqueue(backgroundWorkRequest);
    }

    private void stopLocationWork() {
        WorkManager.getInstance(MainActivity.this).cancelAllWork();
    }
}