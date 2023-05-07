package com.example.rideringer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusFragment extends Fragment {
    private String[] mrt= {"Woodlands", "Yishun", "Choa Chu Kang"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus, container, false);
        this.db = new Database(getActivity());
        v.findViewById(R.id.bus_save).setOnClickListener(onSave);
        v.findViewById(R.id.bus_alarm).setOnClickListener(onAlarm);
        autoCompleteTextView = v.findViewById(R.id.bus_drop_list);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, mrt);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(onClick);
        fetchBusStops();
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

    private void fetchBusStops() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusStops")
                .addHeader("AccountKey", "5c5Iep5nTs6hFLsVV9w4/A==")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("MyService", "Response body: " + responseBody);
                } else {
                    Log.e("MyService", "Response not successful: " + response);
                }
            }
        });
    }
}