package com.example.rideringer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

public class PermissionsManager {

    public static void checkGPSPermissions(Context mapCtx) {
        int finePermit = ActivityCompat.checkSelfPermission(mapCtx, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermit = ActivityCompat.checkSelfPermission(mapCtx, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (finePermit != PackageManager.PERMISSION_GRANTED || coarsePermit != PackageManager.PERMISSION_GRANTED) {
            showEnablePermissionAlert(mapCtx);
        }
    }

    public static boolean permissionsGranted(Context mapCtx) {
        int finePermit = ActivityCompat.checkSelfPermission(mapCtx, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermit = ActivityCompat.checkSelfPermission(mapCtx, Manifest.permission.ACCESS_COARSE_LOCATION);
        return finePermit == PackageManager.PERMISSION_GRANTED && coarsePermit == PackageManager.PERMISSION_GRANTED;
    }
    private static void showEnablePermissionAlert(Context mapCtx) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mapCtx)
                .setTitle("Location Permission Settings")
                .setMessage("Location Permission is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mapCtx.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private static void showEnableLocationAlert(Context mapCtx) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mapCtx)
                .setTitle("Location Service Settings")
                .setMessage("Location Service is not enabled. Do you want to go to settings menu?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mapCtx.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog.create().show();
    }
}
