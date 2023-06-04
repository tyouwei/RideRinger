package com.example.rideringer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class BusFragment extends Fragment {
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;
    private ArrayList<String> buses;
    private String nameStr;
    private HashMap<String, LatLng> locationMap;
    private Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus, container, false);
        this.db = new Database(getContext());


        v.findViewById(R.id.bus_save).setOnClickListener(onSave);
        v.findViewById(R.id.bus_alarm).setOnClickListener(onAlarm);
        buses = getArguments().getStringArrayList("BUS_STOP_ARRAY");
        locationMap = (HashMap<String, LatLng>) getArguments().getSerializable("LOCATION_DETAILS_HASHMAP");

        // Sorts the bus stops fetched from Data Mall and deals with exception by reloading
        try {
            buses.sort(Comparator.naturalOrder());
        } catch (NullPointerException e) {
            Log.e("NullPointerException", String.valueOf(e));
            Intent i = new Intent(getActivity(), LoadingScreen.class);
            startActivity(i);
        }

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
            //db.insert(nameStr, addStr);
        }
    };

    private View.OnClickListener onAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Set Alarm
        }
    };

    private AdapterView.OnItemClickListener onClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            nameStr = parent.getItemAtPosition(position).toString();

            Toast.makeText(getActivity(), "" + locationMap.get(nameStr).latitude, Toast.LENGTH_LONG).show();
        }
    };
}