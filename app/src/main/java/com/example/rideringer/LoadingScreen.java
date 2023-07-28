package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoadingScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        progressBar = findViewById(R.id.progressBar);
        fetchBusStops();
        Log.e("test", "loading trigger");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fetchBusStops() {
        ArrayList<String> buses = new ArrayList<>();
        HashMap<String, Pair<String, Pair<Double, Double>>> locationMap = new HashMap<>();
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
                    try {
                        ResponseBody responseBody = response.body();
                        JsonReader reader = new JsonReader(new InputStreamReader(responseBody.byteStream()));
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String name = reader.nextName();
                            if (name.equals("value")) {
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    reader.beginObject();
                                    String busName = null;
                                    String roadName = null;
                                    double latitude = 0.0;
                                    double longitude = 0.0;

                                    while (reader.hasNext()) {
                                        String propertyName = reader.nextName();
                                        switch (propertyName) {
                                            case "Description":
                                                busName = reader.nextString();
                                                break;
                                            case "RoadName":
                                                roadName = reader.nextString();
                                                break;
                                            case "Latitude":
                                                latitude = reader.nextDouble();
                                                break;
                                            case "Longitude":
                                                longitude = reader.nextDouble();
                                                break;
                                            default:
                                                reader.skipValue();
                                        }
                                    }
                                    if (busName != null) {
                                        synchronized (this) {
                                            buses.add(busName);
                                            locationMap.put(busName, new Pair<>(roadName, new Pair<>(latitude, longitude)));
                                        }
                                    }

                                    reader.endObject();
                                }
                                reader.endArray();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    } catch (Exception e) {
                        Log.e("JSON Conversion", "Response not successful: " + response);
                    }
                } else {
                    Log.e("REST API", "Response not successful: " + response);
                }
            }
        };

        List<CompletableFuture<Void>> cf = new ArrayList<>();

        for (int i = 0; i < numOfCalls; i++) {
            final int index = i;
            CompletableFuture<Void> request = CompletableFuture.supplyAsync(() -> new Request.Builder())
                    .thenApplyAsync(x -> x.url("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=" + index * 500))
                    .thenApplyAsync(x -> x.addHeader("AccountKey", BuildConfig.LTA_KEY))
                    .thenApplyAsync(x -> x.build())
                    .thenAcceptAsync(x -> client.newCall(x).enqueue(cb));
            cf.add(request);
        }

        CompletableFuture.allOf(cf.toArray(new CompletableFuture[0])).thenRun(() -> {
            Log.d("REST API", "Number of buses: " + new Integer(buses.size()));
            LocationDetails details = (LocationDetails) getApplication();
            details.updateDetails(buses, locationMap);
            new Handler(Looper.getMainLooper()).post(() -> onFinishLoad());
        });
    }

    private void onFinishLoad() {
        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(i);
        finish();
    }
}