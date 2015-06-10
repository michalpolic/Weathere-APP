package com.michalpolic.weather.android.task;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.michalpolic.weather.android.activity.WeatherActivity;
import com.michalpolic.weather.android.client.LoaderWeatherClass;
import com.michalpolic.weather.android.database.query.QueryClass;
import com.michalpolic.weather.android.entity.WeatherItem;
import com.michalpolic.weather.android.geolocation.LocationClass;

import java.util.ArrayList;

/**
 * Created by Michal on 9.6.2015.
 */
public class WeatherUpdateRunnable implements Runnable {

    private static final String TAG = "WeatherUpdateRunnable";
    private static final long msSleepDelayUnknownWeather = 3 * 1000;
    private static final long msSleepDelayKnownWeather = 3600 * 1000;

    private static boolean msRun = false;
    private static boolean msIsKnownWeature = false;

    private static WeatherUpdateRunnable msWeatherUpdateRunnable = null;
    private static Location msLocation = null;
    private static LoaderWeatherClass msLoaderWeatherClass;
    private static QueryClass msQueryClass;
    private static Activity msContext;

    private static Handler mHandler;


    // interface for calling actualization of UI
    public static interface WeatherUpdatedListener {
        void onWeatherUpdated();
    }


    // singleton
    public static WeatherUpdateRunnable newInstance(Activity context){
        if(msWeatherUpdateRunnable == null){
            msWeatherUpdateRunnable = new WeatherUpdateRunnable(context);
        }
        return msWeatherUpdateRunnable;
    }


    private WeatherUpdateRunnable(Activity context){
        this.msContext = context;
        msLoaderWeatherClass = new LoaderWeatherClass(context);
        msQueryClass = new QueryClass(context);
        mHandler = new Handler(context.getMainLooper());
    }


    @Override
    public void run() {
        if(msRun == true) return;

        // updating weather process
        msRun = true;
        while(msRun) {
            // update location after long time
            if(msIsKnownWeature){
                msIsKnownWeature = false;
                LocationClass locationClass = LocationClass.newInstance(msContext);
                locationClass.getLocation();    // when location is find than is location set to variable msLocation
            }

            // wait to known location
            if(msLocation == null) {
                WeatherItem weatherItem = msLoaderWeatherClass.getWeatherAtLocation(14.504629131406546, 50.05787611473352);  // test for known location
                ArrayList<WeatherItem> forecastItems = msLoaderWeatherClass.getForecastAtLocation(14.504629131406546, 50.05787611473352);  // test for known location

                // Log.d(TAG, "lon: " + msLocation.getLongitude() + " , lat: " + msLocation.getLatitude());
                //WeatherItem weatherItem = msLoaderWeatherClass.getWeatherAtLocation(msLocation.getLongitude(), msLocation.getLatitude());
                //ArrayList<WeatherItem> forecastItems = msLoaderWeatherClass.getForecastAtLocation(msLocation.getLongitude(), msLocation.getLatitude());

                if(weatherItem!=null) {
                    msQueryClass.insertItem(weatherItem);
                }

                if(forecastItems!=null && !forecastItems.isEmpty()) {
                    msQueryClass.updateItems(forecastItems);
                }

                // all data updated
                if(weatherItem!=null && forecastItems!=null && !forecastItems.isEmpty()) {
                    msIsKnownWeature = true;
                    mHandler.sendEmptyMessage(WeatherActivity.sREFRESH_VIEW);
                }
            }


            // sleep
            try {
                if(msIsKnownWeature) Thread.sleep(msSleepDelayKnownWeather);
                else Thread.sleep(msSleepDelayUnknownWeather);

            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException: " + e.toString());
                msRun = false;
            }
        }
    }


    public static Handler getHandler() {
        return mHandler;
    }


    public static void setLocation(Location location) {
        msLocation = location;
    }

}
