package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private Button saveButton;
    private Button settingsButton;
    private GPSTracker gpsTracker;
    private SharedPreferences prefs = null;
    private  SharedPreferences.OnSharedPreferenceChangeListener onChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //TODO
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.saved);
        searchButton = findViewById(R.id.find);
        settingsButton = findViewById(R.id.settings);
        gpsTracker = new GPSTracker(MainActivity.this);
        prefs = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);

        saveButton.setOnClickListener(onSave);
        searchButton.setOnClickListener(onSearch);
        settingsButton.setOnClickListener(onSettings);
    }

    @Override
    protected void onResume() {
        prefs.registerOnSharedPreferenceChangeListener(onChange);
        super.onResume();
    }

    @Override
    protected void onPause() {
        prefs.unregisterOnSharedPreferenceChangeListener(onChange);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTracker.stopUsingGPS();
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
}