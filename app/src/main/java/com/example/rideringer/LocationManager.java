package com.example.rideringer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationManager {
    public static LocationManager instance = null;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static int REQUEST_CHECK_SETTINGS = 200;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Intent backgroundLocationIntent = new Intent("background_location");
    StringBuilder stringBuilder = new StringBuilder();
    private Activity activity;

    private LocationManager() {
    }

    public static LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager();
        }
        instance.init(context);
        return instance;
    }

    private void init(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
                double lat = Double.longBitsToDouble(prefs.getLong("latitude", 0));
                double lon = Double.longBitsToDouble(prefs.getLong("longitude", 0));
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        double distance = distance(lat, location.getLatitude(), lon, location.getLongitude());
                        stringBuilder.setLength(0);
                        if (distance > 0.05) {
                            stringBuilder.append(distance + " km away");
                            backgroundLocationIntent.putExtra("location", stringBuilder.toString());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(backgroundLocationIntent);
                        } else {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("track", false);
                            editor.apply();
                        }
                    }
                }
            }
        };
        createLocationRequest();
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .build();
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                    } catch (Exception sendEx) {
                        //do nothing
                    }
                }
            }
        });
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
