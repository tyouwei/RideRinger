package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private Switch alarm;
    private Switch banner;
    private Switch tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        alarm = findViewById(R.id.switch_alarm);
        banner = findViewById(R.id.switch_banner);
        tracker = findViewById(R.id.switch_tracker);
    }
}