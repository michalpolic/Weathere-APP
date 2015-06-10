package com.michalpolic.weather.android.geolocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.michalpolic.weather.android.dialog.DialogClass;
import com.michalpolic.weather.android.task.WeatherUpdateRunnable;

/**
 * Created by Michal on 9.6.2015.
 */
public class LocationClass implements LocationListener{

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    private static final long mDelayLocation = 3600 * 1000;
    private static final long mDistanceLocation = 10 * 1000;

    private static long mTimeStampLocation = 0;
    private static Location mLocation = null;
    private static Activity mContext = null;
    private static LocationClass mLocationClass = null;

    private static LocationManager locationManager;
    private static LocationListener locationListener;


    // singleton
    public static LocationClass newInstance(Activity context){
        if(mLocationClass == null){
            mLocationClass = new LocationClass(context);
        }
        return mLocationClass;
    }


    private LocationClass(Activity context){
        this.mContext = context;
        this.mLocation = null;
    }


    public Location getLocation(){
        if(mLocation == null || (mTimeStampLocation + mDelayLocation) < System.currentTimeMillis()){
            updateLocation();
        }
        return mLocation;
    }


    private void updateLocation() {
        if(locationManager == null) {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // accessibility of location - show dialog
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){
                DialogClass.newInstance(mContext).showSettingsDialog();
            }

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,0, 0, this);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (isGPSEnabled) {
                if (mLocation == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location obtain!!!!!!!!!");
        mLocation = location;
        WeatherUpdateRunnable.setLocation(mLocation);
        mTimeStampLocation = System.currentTimeMillis();
        locationManager.removeUpdates(this);
        locationManager = null;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onProviderDisabled(String provider) {

    }
}
