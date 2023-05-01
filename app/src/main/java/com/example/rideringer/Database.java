package com.example.rideringer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locationlist.db";
    private static final int SCHEMA_VERSION = 1;

    public Database(Context c){
        super(c, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE locations_table (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "locationName TEXT," +
                "locationAddress TEXT," +
                "lat REAL," +
                "lon REAL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAll() {
        return getReadableDatabase()
                .rawQuery(
                        "SELECT _id, locationName, locationAddress " +
                                "FROM locations_table " +
                                "ORDER BY locationName", null);
    }

    public void insert (String locationName, String locationAddress, double lat, double lon) {
        ContentValues cv = new ContentValues();
        cv.put("locationName", locationName);
        cv.put("locationAddress", locationAddress);
        cv.put("lat", lat);
        cv.put("lon", lon);
        getWritableDatabase().insert("locations_table", "locationName", cv);
    }

    public void update (String id, String locationName, String locationAddress, double lat, double lon) {
        ContentValues cv = new ContentValues();
        String[] args = {id};
        cv.put("locationName", locationName);
        cv.put("locationAddress", locationAddress);
        cv.put("lat", lat);
        cv.put("lon", lon);
        getWritableDatabase().update("locations_table", cv, "_ID=?", args);
    }

    public String getID(Cursor c) {
        return c.getString(0);
    }

    public String getLocationName(Cursor c) {
        return c.getString(1);
    }

    public String getLocationAddress(Cursor c) {
        return c.getString(2);
    }

    public String getLatitude(Cursor c) {
        return c.getString(3);
    }

    public String getLongitude(Cursor c) {
        return c.getString(4);
    }
}
