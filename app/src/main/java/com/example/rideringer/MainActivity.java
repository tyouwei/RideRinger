package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private Button saveButton;
    private Button settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.saved);
        searchButton = findViewById(R.id.find);
        settingsButton = findViewById(R.id.settings);

        saveButton.setOnClickListener(onSave);
        searchButton.setOnClickListener(onSearch);
        settingsButton.setOnClickListener(onSettings);
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
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
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