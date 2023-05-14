package com.example.rideringer;

import android.content.Intent;
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

import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MRTFragment extends Fragment {
    private String[] mrt= {"Woodlands", "Yishun", "Choa Chu Kang"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    ArrayList<Pair<String, String>> stations;
    private Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_m_r_t, container, false);
        this.db = new Database(getActivity());
        v.findViewById(R.id.mrt_save).setOnClickListener(onSave);
        v.findViewById(R.id.mrt_alarm).setOnClickListener(onAlarm);
        autoCompleteTextView = v.findViewById(R.id.mrt_drop_list);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, mrt);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(onClick);

        // Test of Excel File Reader Functionality
        // As of now, station names are written in the Log Cat
        ExcelFileReader fileReader = new ExcelFileReader();
        Sheet stationSheet = fileReader.readExcelFromStorage(getContext(), "mrt_lrt_stations.xls");
        stations = fileReader.populateOnLineCode(stationSheet, "NEL");
        for (Pair pair: stations) {
            Log.d("STATIONS", pair.first.toString() + ", " + pair.second.toString());
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        db.close();
        stations.clear();
        super.onDestroyView();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //db.insert(nameStr, addrStr);
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
            String item = parent.getItemAtPosition(position).toString();
        }
    };
}