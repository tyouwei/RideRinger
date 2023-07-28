package com.example.rideringer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class BusFragment extends Fragment {
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private ArrayList<String> buses;
    private HashMap<String, Pair<String, Pair<Double, Double>>> locationMap;
    private Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus, container, false);
        this.db = new Database(getContext());


        v.findViewById(R.id.bus_save).setOnClickListener(onSave);
        v.findViewById(R.id.bus_alarm).setOnClickListener(onAlarm);

        LocationDetails details = (LocationDetails) getActivity().getApplication();
        buses = details.getBuses();
        locationMap = details.getHashmap();
        Toast.makeText(getContext(), buses.size() + "", Toast.LENGTH_SHORT).show();
        buses.sort(Comparator.naturalOrder());

        // For the drop-down list
        autoCompleteTextView = v.findViewById(R.id.bus_drop_list);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.bus_drop_down_item, buses);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(onClick);

        return v;
    }

    @Override
    public void onDestroyView() {
        db.close();
        super.onDestroyView();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nameStr = autoCompleteTextView.getText().toString();
            Pair<String, Pair<Double, Double>> pair = locationMap.get(nameStr);
            if (pair != null) {
                String descStr = pair.first;
                double lat = pair.second.first;
                double lon = pair.second.second;
                db.insert(nameStr, descStr, lat, lon);
            } else {
                Toast.makeText(getContext(), "Please Select a Bus Stop", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener onAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Set Alarm
            String nameStr = autoCompleteTextView.getText().toString();
            Pair<String, Pair<Double, Double>> pair = locationMap.get(nameStr);
            SharedPreferences prefs = getContext().getSharedPreferences(getContext().getString(R.string.prefs), Context.MODE_PRIVATE);
            if (pair != null) {
                double lat = pair.second.first;
                double lon = pair.second.second;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("track", true);
                editor.putLong("latitude", Double.doubleToRawLongBits(lat));
                editor.putLong("longitude", Double.doubleToRawLongBits(lon));
                editor.apply();

                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Please Select a Bus Stop", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AdapterView.OnItemClickListener onClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };
}