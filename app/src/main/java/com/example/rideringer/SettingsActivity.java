package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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

        initSwitchListener();
    }

    private void initSwitchListener() {
        banner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(UserSettings.NOTIFICATION_SETTINGS, isChecked);
                editor.apply();
                SharedPreferences test = getSharedPreferences(UserSettings.PREFERENCES,MODE_PRIVATE);
                boolean bool = test.getBoolean(UserSettings.NOTIFICATION_SETTINGS, true);
                String bool2 = new Boolean(bool).toString();
                Toast.makeText(getApplicationContext(), bool2, Toast.LENGTH_SHORT).show();
            }
        });

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(UserSettings.ALARM_SETTINGS, isChecked);
                editor.apply();
                SharedPreferences test = getSharedPreferences(UserSettings.PREFERENCES,MODE_PRIVATE);
                boolean bool = test.getBoolean(UserSettings.ALARM_SETTINGS, true);
                String bool2 = new Boolean(bool).toString();
                Toast.makeText(getApplicationContext(), bool2, Toast.LENGTH_SHORT).show();
            }
        });

        /***************Leave it here first, Ive already made a GPS settings alert in GPS tracker***********/

//        tracker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
//                editor.putBoolean(UserSettings.GPS_SETTINGS, isChecked);
//                editor.apply();
////                SharedPreferences test = getSharedPreferences(UserSettings.PREFERENCES,MODE_PRIVATE);
////                boolean bool = test.getBoolean(UserSettings.NOTIFICATION_SETTINGS, true);
////                String bool2 = new Boolean(bool).toString();
////                Toast.makeText(getApplicationContext(), bool2, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}