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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusFragment extends Fragment {
    private ArrayList<String> buses = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus, container, false);
        this.db = new Database(getActivity());
        buses.add("");
        fetchBusStops();

        v.findViewById(R.id.bus_save).setOnClickListener(onSave);
        v.findViewById(R.id.bus_alarm).setOnClickListener(onAlarm);

        // For the drop-down list
        autoCompleteTextView = v.findViewById(R.id.bus_drop_list);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, buses);
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
            String item = parent.getItemAtPosition(position).toString();
        }
    };

    private void fetchBusStops() {
        int numOfCalls = 11;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Callback cb = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.e("REST API", "Response is successful: " + responseBody);
                    try {
                        JSONArray obj = new JSONObject(responseBody)
                                .getJSONArray("value");
                        int len = obj.length();
                        for (int i = 0; i < len; i++) {
                            buses.add(obj.getJSONObject(i).getString("Description"));
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Conversion", "Response not successful: " + response);
                    }
                } else {
                    Log.e("REST API", "Response not successful: " + response);
                }
            }
        };

        CompletableFuture<Void>[] cf = new CompletableFuture[numOfCalls];
        for (int i = 0; i < numOfCalls; i++) {
            final int callSize = i * 500;
            CompletableFuture<Void> request = CompletableFuture.supplyAsync(() -> new Request.Builder())
                    .thenApplyAsync(x -> x.url("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=" + callSize))
                    .thenApplyAsync(x -> x.addHeader("AccountKey", "5c5Iep5nTs6hFLsVV9w4/A=="))
                    .thenApplyAsync(x -> x.build())
                    .thenAcceptAsync(x -> client.newCall(x).enqueue(cb));
            cf[i] = request;
        }

        CompletableFuture.allOf(cf).join();
    }
}