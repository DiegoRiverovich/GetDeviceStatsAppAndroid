package com.niceattiregames.andrey.getdevicestatsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;


final public class DeviceStats /*extends Activity*/ {

    private static final int MY_PERMISSION_READ_STATE = 1;
    private static final int MY_PERMISSION_ACCESS_NETWORK_STATE = 2;

    String IMEI;

    private static DeviceStats shared;
    private Context context;
    //private IPermissionCallback callback;

    public void setContext(Context context) {
        this.context = context;
        //this.callback = (IPermissionCallback) context;
    }

    private DeviceStats (){
    }

    public static DeviceStats getInstance(){
        if (null == shared){
            shared = new DeviceStats();
        }
        return shared;
    }

    // +++ Device name
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    // --- Device name

    // +++ OS version
    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }
    // --- OS version


/*
    // +++ Check permission


    public void checkPermission() {

        //ContextCompat.checkSelfPermission(context,      Manifest.permission.READ_PHONE_STATE);
        //ContextCompat.checkSelfPermission(context,      Manifest.permission.ACCESS_NETWORK_STATE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("IMEI Log NOT granted: ", " TEST1 ");
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSION_READ_STATE);
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("IMEI Log NOT granted: ", " TEST2 ");
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    MY_PERMISSION_ACCESS_NETWORK_STATE);
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("IMEI Log granted: ", " TEST1 ");
            getIMEI();
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (android.os.Build.VERSION.SDK_INT > 23) {
            switch (requestCode) {
                case MY_PERMISSION_READ_STATE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                        getIMEI();
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_READ_STATE);
                        Log.d("IMEI Log denied: ", " Denied ");
                        callback.permissionDenied("Denied");
                    }
                    return;
                }
                case MY_PERMISSION_ACCESS_NETWORK_STATE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                        getIMEI();
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, MY_PERMISSION_ACCESS_NETWORK_STATE);
                        Log.d("IMEI Log denied: ", " Denied ");
                        callback.permissionDenied("Denied");
                    }
                    return;
                }
                // other 'case' lines to check for other
                // permissions this app might request
            }
        } else {

        }
    }

    // --- Check permission

*/

    // +++ IMEI

    public String getIMEI() {
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        IMEI = mngr.getDeviceId();
        //Log.d("IMEI Log granted: ", " Name " + IMEI);
        return IMEI;
        //callback.permissionGranted(IMEI);
    }

    // --- IMEI

    // +++ Screen resolution


    public String getScreenResolution() {
        int width= context.getResources().getDisplayMetrics().widthPixels;
        int height= context.getResources().getDisplayMetrics().heightPixels;
        return "" + width + "x" +height;
    }


    // --- Screen resolution


    // +++ DPI

    public String getDPI() {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "~640 dpi";
        }
        if (density >= 3.0) {
            return "~480 dpi";
        }
        if (density >= 2.0) {
            return "~320 dpi";
        }
        if (density >= 1.5) {
            return "~240 dpi";
        }
        if (density >= 1.0) {
            return "~160 dpi";
        }
        return "~120 dpi";
    }

    // --- DPI

    // +++ Network type

    public String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info==null || !info.isConnected())
            return "Not connected"; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:  //api<25 : replace by 17
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:  //api<25 : replace by 18
                case 19:  //LTE_CA
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    // --- Network type


    // +++ SSID

    public String getWiFiInfo(String id) {


        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        String info;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            if (id == "ssid") { info = wifiInfo.getSSID(); } else { info = wifiInfo.getBSSID(); }

            return info;
        }
        return "Not connected";

    }

    // --- SSID

}
