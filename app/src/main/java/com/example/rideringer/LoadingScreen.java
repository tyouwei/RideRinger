package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadingScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        progressBar = findViewById(R.id.progressBar);
        ArrayList<String> arr = fetchBusStops();
        onFinishLoad(arr);
    }

    private ArrayList<String> fetchBusStops() {
        ArrayList<String> buses = new ArrayList<>();
        ArrayList<Void> completedList = new ArrayList<>();
        buses.add("");
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
                    Log.d("REST API", "Response is successful: " + responseBody);
                    try {
                        JSONArray obj = new JSONObject(responseBody)
                                .getJSONArray("value");
                        int len = obj.length();
                        for (int i = 0; i < len; i++) {
                            buses.add(obj.getJSONObject(i).getString("Description"));
                            //Log.e("REST API", new Integer(buses.size()).toString());
                        }
                        completedList.add(null);
                    } catch (JSONException e) {
                        Log.e("JSON Conversion", "Response not successful: " + response);
                    } finally {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
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
        //My bootleg implementation of join
        while (true) {
            boolean complete = true;
            if (completedList.size() != numOfCalls) {
                complete = false;
            }
            if (complete) {
                Log.e("REST API", new Integer(buses.size()).toString());
                return buses;
            }
        }
    }

    private void onFinishLoad(ArrayList<String> arr) {
        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
        i.putExtra("Bus Stop Array", arr);
        startActivity(i);
        finish();
    }
}