package com.michalpolic.weather.android.client.parser;

import android.app.Activity;
import android.util.Log;

import com.michalpolic.weather.android.entity.WeatherItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michal on 10.6.2015.
 */
public class JsonForecastClass {

    public static ArrayList<WeatherItem> jsonToForecast(Activity mContext, JSONObject json) {
        ArrayList<WeatherItem> items = new ArrayList<WeatherItem>();
        try {
            String city  = json.getJSONObject("city").getString("name");

            JSONArray daysWeature = json.getJSONArray("list");
            for(int i = 0; i < daysWeature.length(); i++){
                JSONObject oneDay = daysWeature.getJSONObject(i);
                Date d = new Date(System.currentTimeMillis()+((i+1)*(24*3600*1000)));
                String description = "";
                String icon = "";
                double temperature = 0;
                double precipitation = 0;
                double humidity = 0;
                double pressure = 0;
                double wind_speed = 0;
                double wind_direction = 0;


                if(oneDay.has("temp") && oneDay.getJSONObject("temp").has("day")){
                    temperature = oneDay.getJSONObject("temp").getDouble("day");
                }

                if(oneDay.has("pressure")){
                    pressure = oneDay.getDouble("pressure");
                }

                if(oneDay.has("humidity")){
                    humidity = oneDay.getDouble("humidity");
                }

                if(oneDay.has("weather")){
                    //Log.d("HELP","sdljl: " + json.getString("weather"));

                    JSONObject weather = new JSONObject(oneDay.getJSONArray("weather").getString(0));
                    if(weather.has("description")){
                        description = weather.getString("description");
                    }
                    if(weather.has("icon")){
                        icon = weather.getString("icon");
                    }
                }

                if(oneDay.has("speed")){
                    wind_speed = oneDay.getDouble("speed");
                }

                if(oneDay.has("deg")){
                    wind_direction = oneDay.getDouble("deg");
                }

                if(oneDay.has("rain")){
                    precipitation = oneDay.getDouble("rain");
                }

                if(oneDay.has("snow")){
                    precipitation = oneDay.getDouble("snow");
                }

                items.add(new WeatherItem(mContext,d,city,temperature,description,icon,
                        precipitation,humidity,pressure,wind_speed,wind_direction));
            }

            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
