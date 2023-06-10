package com.example.rideringer;

// This is a model class for the MRT and LRT stations for the custom adapter in the MRT fragment
public class MrtLrtModelClass {
    private String stnName;
    private int stnLogo;

    public MrtLrtModelClass(String stnName, int stnLogo) {
        this.stnName = stnName;
        this.stnLogo = stnLogo;
    }

    // Setters and getters for the various elements
    public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }

    public int getStnLogo() {
        return stnLogo;
    }

    public void setStnLogo(int stnLogo) {
        this.stnLogo = stnLogo;
    }

    @Override
    public String toString() {
        return getStnName();
    }
}
