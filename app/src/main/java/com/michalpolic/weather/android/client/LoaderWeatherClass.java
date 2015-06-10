package com.michalpolic.weather.android.client;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.michalpolic.weather.android.activity.WeatherActivity;
import com.michalpolic.weather.android.client.parser.JsonForecastClass;
import com.michalpolic.weather.android.client.parser.JsonTodayClass;
import com.michalpolic.weather.android.entity.WeatherItem;
import com.michalpolic.weather.android.task.WeatherUpdateRunnable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michal on 9.6.2015.
 */
public class LoaderWeatherClass {

    private Activity mContext;


    /**
     * Load today weather
     * @param lon - double longitude
     * @param lat - double latitude
     * @return WeatherItem
     */
    public WeatherItem getWeatherAtLocation(double lon, double lat) {
        // check internet connection
        if(!isConected()){
            WeatherUpdateRunnable.getHandler().sendEmptyMessage(WeatherActivity.sNO_INTERNET_CONECTON);
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            //Log.d("NET","http://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon=" + lon);
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon=" + lon);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = streamToJson(in);
            return JsonTodayClass.jsonToWeatherItem(mContext, new Date(System.currentTimeMillis()), json);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(urlConnection!=null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }


    /**
     * Load forecast at 16 days
     * @param lon - double longitude
     * @param lat - double latitude
     * @return ArrayList<WeatherItem>
     */
    public ArrayList<WeatherItem> getForecastAtLocation(double lon, double lat) {
        // check internet connection
        if(!isConected()){
            WeatherUpdateRunnable.getHandler().sendEmptyMessage(WeatherActivity.sNO_INTERNET_CONECTON);
            return null;
        }

        // load weather
        ArrayList<WeatherItem> items  = null;
        HttpURLConnection urlConnection = null;
        try {
            //Log.d("NET", "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&cnt=16&mode=json");
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&cnt=16&mode=json");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = streamToJson(in);
            return JsonForecastClass.jsonToForecast(mContext, json);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(urlConnection!=null) {
                urlConnection.disconnect();
            }
        }
        return items;
    }


    /**
     * Create json object from loaded stream
     * @param in - InputStream
     * @return JSONObject
     */
    private JSONObject streamToJson(InputStream in) {

        StringBuilder total = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            JSONObject json = new JSONObject(total.toString());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Check if internet is enable
     * @return
     */
    public boolean isConected(){
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
        }
        return false;
    }


    public LoaderWeatherClass(Activity context){
        this.mContext = context;
    }
}
