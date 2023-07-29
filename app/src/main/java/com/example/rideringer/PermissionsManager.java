package com.example.rideringer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class PermissionsManager {
    private static PermissionsManager instance = null;
    private Context context;

    private PermissionsManager() {
    }

    public static PermissionsManager getInstance(Context context) {
        if (instance == null) {
            instance = new PermissionsManager();
        }
        instance.init(context);
        return instance;
    }

    private void init(Context context) {
        this.context = context;
    }

    public boolean checkPermissions(String... permissions) {
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(context, p) != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void askPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public boolean handlePermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        boolean isAllPermissionsGranted = true;

        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    isAllPermissionsGranted = false;
                    //Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show();
                    showPermissionRational(activity, requestCode,permissions, permissions[i]);
                    break;
                }
            }
        } else {
            isAllPermissionsGranted = false;
        }
        return isAllPermissionsGranted;
    }

    private void showPermissionRational(Activity activity, int requestCode, String[] permissions, String deniedPermission) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askPermissions(activity, permissions, requestCode);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(activity, "" + ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission), Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission)) {
                showMessageOKCancel("Allow access to these permissions(s)", listener);
            }
        }
    }
    private void showMessageOKCancel(String msg, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("Ok", onClickListener)
                .setNegativeButton("Cancel", onClickListener)
                .create()
                .show();
    }
}
