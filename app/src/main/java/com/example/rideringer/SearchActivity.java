package com.example.rideringer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TransportTabAdapter ttAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.tabLayout = findViewById(R.id.tablayout);
        this.viewPager2 = findViewById(R.id.viewpager);
        this.ttAdapter = new TransportTabAdapter(this);
        viewPager2.setAdapter(ttAdapter);
        tabLayout.addOnTabSelectedListener(onTabSelected);
        viewPager2.registerOnPageChangeCallback(onPageChange);
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
    }

    class TransportTabAdapter extends FragmentStateAdapter {
        public TransportTabAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new BusFragment();
                case 1:
                    return new MRTFragment();
                default:
                    return new BusFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}