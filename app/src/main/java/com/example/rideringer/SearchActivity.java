package com.example.rideringer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TransportTabAdapter ttAdapter;
    private GPSTracker gpsTracker;
    private ArrayList<String> busStops;
    private double latitude = 0.0d;
    private double longitude = 0.0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.busStops = getIntent().getStringArrayListExtra("Bus Stop Array");

        this.tabLayout = findViewById(R.id.tablayout);
        this.viewPager2 = findViewById(R.id.viewpager);
        this.ttAdapter = new TransportTabAdapter(this);

        viewPager2.setAdapter(ttAdapter);
        tabLayout.addOnTabSelectedListener(onTabSelected);
        viewPager2.registerOnPageChangeCallback(onPageChange);
        gpsTracker = new GPSTracker(SearchActivity.this);
    }

    private TabLayout.OnTabSelectedListener onTabSelected = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager2.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private ViewPager2.OnPageChangeCallback onPageChange = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            tabLayout.getTabAt(position).select();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTracker.stopUsingGPS();
    }

    class TransportTabAdapter extends FragmentStateAdapter {
        public TransportTabAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            BusFragment busFragment = new BusFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Bus Stops", busStops);
            busFragment.setArguments(bundle);

            switch (position) {
                case 1:
                    return new MRTFragment();
                default:
                    return busFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}