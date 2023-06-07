package com.example.rideringer;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationDetails extends Application {
    private ArrayList<String> buses;
    private HashMap<String, Pair<String, LatLng>> hashmap;

    public LocationDetails() {
        this.buses = new ArrayList<>();
        this.hashmap = new HashMap<>();
    }

    public HashMap<String, Pair<String, LatLng>> getHashmap() {
        return this.hashmap;
    }

    public ArrayList<String> getBuses() {
        return this.buses;
    }

    public void updateDetails(ArrayList<String> buses, HashMap<String, Pair<String, LatLng>> hashmap) {
        this.buses = buses;
        this.hashmap = hashmap;
    }
}
