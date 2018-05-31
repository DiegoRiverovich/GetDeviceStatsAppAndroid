package com.niceattiregames.andrey.getdevicestatsapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements /*IPermissionCallback,*/ DataGPSCallback {

    private static final int MY_PERMISSION_READ_STATE = 1;
    private static final int MY_PERMISSION_ACCESS_NETWORK_STATE = 2;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 3;
    private static final int MY_PERMISSION_ACCESS_WIFI_STATE = 4;


    private TextView deviceModel;
    private TextView osVersion;
    private TextView IMEI;
    private TextView screenResolution;
    private TextView screenDIP;
    private TextView GPSCoordinates;
    private TextView networkConnectionType;
    private TextView ssid;
    private TextView bssid;
    private TextView currentTime;

    private Button pushButton;

    private LinearLayout invisibleLayout;

    private Boolean coordinatesReceived;
    private String longitude;
    private String latitude;

    GPSService gpsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isScreenBigerThen1080();

        coordinatesReceived = false;

        deviceModel = findViewById(R.id.deviceModel);
        osVersion = findViewById(R.id.OSVersion);
        IMEI = findViewById(R.id.IMEI);
        screenResolution = findViewById(R.id.screenResolution);
        screenDIP = findViewById(R.id.screenDPI);
        GPSCoordinates = findViewById(R.id.GPSCoordinates);
        networkConnectionType = findViewById(R.id.networkConnectionType);
        ssid = findViewById(R.id.WiFiSSID);
        bssid = findViewById(R.id.WiFiBSSID);
        currentTime = findViewById(R.id.currentTime);
        invisibleLayout = findViewById(R.id.invisibleLayout);

        pushButton = findViewById(R.id.button);

        DeviceStats.getInstance().setContext(this);

        invisibleLayout.setAlpha(0.0f);

        isScreenBigerThen1080();

        /*
        showDeviceName();
        showOSVersion();

        getScreenResolution();
        getDPI();
        showIMEI();

        getNetworkType();
        getSSID();
        getBSSID();

        getCurrentTime();

        gpsService = new GPSService(this);
        */

    }

    /*
    public void getAllChildElementsAndChangeFontSize(ViewGroup layoutCont) {
        if (layoutCont == null) return;

        final int mCount = layoutCont.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i) {
            final View mChild = layoutCont.getChildAt(i);

            if (mChild == null) return;

            if (mChild instanceof TextView) {
                Log.d("TextView: ", " instance ");
                // Recursively attempt another ViewGroup.
                //setAppFont((ViewGroup) mChild, mFont);
                ((TextView) mChild).setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            } else {
                Log.d("TextView: ", " NOT instance ");
                // Set the font if it is a TextView.

            }
        }
    }
    */

    public void getAllChildElementsAndChangeFontSize(ViewGroup layoutCont) {
        if (layoutCont == null) return;

        final int mCount = layoutCont.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i) {
            final ViewGroup mChild = (ViewGroup) layoutCont.getChildAt(i);

            final int mmCount = mChild.getChildCount();

            for (int j = 0; j < mmCount; ++j) {
                if (mChild == null) return;

                final View mmChild = mChild.getChildAt(j);

                if (mmChild instanceof TextView) {
                    //Log.d("TextView: ", " instance ");
                    // Recursively attempt another ViewGroup.
                    //setAppFont((ViewGroup) mChild, mFont);
                    ((TextView) mmChild).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                } else {
                    //Log.d("TextView: ", " NOT instance ");
                    // Set the font if it is a TextView.

                }
            }

        }
    }



    private void showDeviceName() {
        //Log.d("ShowDeviceName: ", " Name " + DeviceStats.getInstance().getDeviceName());
        deviceModel.setText(DeviceStats.getInstance().getDeviceName());
    }

    private void showOSVersion() {
        //Log.d("OSVersion: ", " Name " + DeviceStats.getInstance().getAndroidVersion());
        osVersion.setText(DeviceStats.getInstance().getAndroidVersion());
    }

    private void showIMEI() {
        checkReadPhoneStatePermission();

    }

    private void getScreenResolution() {
        //Log.d("Screen resolution: ", " resolution " + DeviceStats.getInstance().getScreenResolution());
        screenResolution.setText(DeviceStats.getInstance().getScreenResolution());
    }

    private  void getDPI() {
        //Log.d("DPI: ", " dpi " + DeviceStats.getInstance().getDPI());
        screenDIP.setText(DeviceStats.getInstance().getDPI());
    }

    private void getNetworkType() {
        //Log.d("Networktype: ", " type:  " + DeviceStats.getInstance().getNetworkType());
        networkConnectionType.setText(DeviceStats.getInstance().getNetworkType());
    }

    private void getSSID() {
        //Log.d("SSID: ", " type:  " + DeviceStats.getInstance().getWiFiInfo("ssid"));
        ssid.setText(DeviceStats.getInstance().getWiFiInfo("ssid"));
    }

    private void getBSSID() {
        //Log.d("BSSID: ", " type:  " + DeviceStats.getInstance().getWiFiInfo("bssid"));
        bssid.setText(DeviceStats.getInstance().getWiFiInfo("bssid"));
    }

    private void getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss");
        String datetime = dateformat.format(c.getTime());
        System.out.println(datetime);
        currentTime.setText(datetime);
    }

    private void getCoordinates() {
        if (coordinatesReceived) {
            GPSCoordinates.setText("Longitude: " + longitude + "\nLatitude: " + latitude);
        } else {
            //GPSCoordinates.setText("NoPerm");
            if (gpsService == null ) {
                gpsService = new GPSService(this);
            } else {
                gpsService.checkPermissionsAndServices();
            }

            gpsService.getDeviceLocation();
        }
    }

    public void pushButton(View view) {
        showDeviceName();
        showOSVersion();
        showIMEI();
        getScreenResolution();
        getDPI();
        getNetworkType();
        getSSID();
        getBSSID();
        getCurrentTime();
        getCoordinates();
        invisibleLayout.animate().alpha(1.0f).setDuration(1000);
    }

    // +++ Check permission

    public void checkReadPhoneStatePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //Log.d("IMEI Log NOT granted: ", " TEST1 ");
            IMEI.setText("NoPerm");
            ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_PHONE_STATE }, MY_PERMISSION_READ_STATE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //Log.d("IMEI Log granted: ", " TEST1 ");
            IMEI.setText(DeviceStats.getInstance().getIMEI());
        } else {

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            //Log.d("Access :", " ACCESS_NETWORK_STATE NOT granted:  ");
            networkConnectionType.setText("NoPerm");
            ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.ACCESS_NETWORK_STATE }, MY_PERMISSION_ACCESS_NETWORK_STATE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            //Log.d("Access :", " ACCESS_NETWORK_STATE granted:  ");
            networkConnectionType.setText(DeviceStats.getInstance().getNetworkType());
        } else {

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            //Log.d("Access :", " ACCESS_WIFI_STATE NOT granted:  ");
            ssid.setText("NoPerm");
            bssid.setText("NoPerm");
            ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.ACCESS_WIFI_STATE }, MY_PERMISSION_ACCESS_WIFI_STATE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            //Log.d("Access :", " ACCESS_WIFI_STATE granted:  ");
            ssid.setText(DeviceStats.getInstance().getWiFiInfo("ssid"));
            bssid.setText(DeviceStats.getInstance().getWiFiInfo("bssid"));
        } else {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (android.os.Build.VERSION.SDK_INT > 23) {
            switch (requestCode) {
                case MY_PERMISSION_READ_STATE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        DeviceStats.getInstance().getIMEI();
                    } else {
                        //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        //ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_READ_STATE);
                        //permissionDenied("NoPerm");
                        IMEI.setText("NoPerm");
                    }
                    return;
                }

                case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Log.d("Access :", " ACCESS_FINE_LOCATION granted:  ");
                        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            gpsService.mLocationPermissionGranted = true;
                            //if (timeIsComeToUpdateLocation()) {
                            gpsService.getDeviceLocation();
                            //saveTime();
                            //}
                        //}
                    } else {
                        //Log.d("Access :", " ACCESS_FINE_LOCATION NOT granted:  ");
                        GPSCoordinates.setText("NoPerm");
                        //ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_READ_STATE);
                        //Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();

                    }
                    return;
                }

                case MY_PERMISSION_ACCESS_NETWORK_STATE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        networkConnectionType.setText(DeviceStats.getInstance().getNetworkType());
                    } else {
                        //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        //ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_READ_STATE);
                        networkConnectionType.setText("NoPerm");
                    }
                    return;
                }

                case MY_PERMISSION_ACCESS_WIFI_STATE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        ssid.setText(DeviceStats.getInstance().getWiFiInfo("ssid"));
                        bssid.setText(DeviceStats.getInstance().getWiFiInfo("bssid"));
                    } else {
                        //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        //ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_READ_STATE);
                        ssid.setText(DeviceStats.getInstance().getWiFiInfo("NoPerm"));
                        bssid.setText(DeviceStats.getInstance().getWiFiInfo("NoPerm"));
                    }
                    return;
                }
            }
        } else {

        }
    }

    // --- Check permission


    // +++ GPS callback
    @Override
    public void noPerm() {
        GPSCoordinates.setText("NoPerm");
    }

    @Override
    public void onDataReceive(double lon, double lat) {
        //Log.d("Location: ", " Long " + lon + " Lat " + lat);
        coordinatesReceived = true;
        longitude = Double.toString(lon);
        latitude = Double.toString(lat);;
        GPSCoordinates.setText("Longitude: " + lon + "\nLatitude: " + lat);
    }

    @Override
    public void gpsDataTimeOut() {
        GPSCoordinates.setText("Null");
    }
    // --- GPS callback

    // +++ Check screen
    private void isScreenBigerThen1080() {
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        boolean biger;

        if (width > 1090) {
            biger = true;
            getAllChildElementsAndChangeFontSize(invisibleLayout);
            //Log.d("isScreenBigerThen1080 :", " True:  ");
        } else {
            biger = false;
            //Log.d("isScreenBigerThen1080 :", "  False:  ");
        }
    }
    // --- Check screen
}
