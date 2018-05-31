package com.niceattiregames.andrey.getdevicestatsapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Toast;



public interface IPermissionCallback {
    void permissionGranted(String permission);
    void permissionDenied(String permission);
}


