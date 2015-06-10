package com.michalpolic.weather.android.client.parser;

import android.app.Activity;

import com.michalpolic.weather.android.entity.WeatherItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Michal on 10.6.2015.
 */
public class JsonTodayClass {

    public static WeatherItem jsonToWeatherItem(Activity mContext, Date date, JSONObject json){
        try {
            String city  = "";
            String description = "";
            String icon = "";
            double temperature = 0;
            double precipitation = 0;
            double humidity = 0;
            double pressure = 0;
            double wind_speed = 0;
            double wind_direction = 0;

            JSONArray names = json.names();
            for(int i = 0; i < json.length(); i++){
                if(names.getString(i).equals("main")){
                    JSONObject main = json.getJSONObject("main");
                    if(main.has("temp")){
                        temperature = main.getDouble("temp");
                    }
                    if(main.has("humidity")){
                        humidity = main.getDouble("humidity");
                    }
                    if(main.has("pressure")){
                        pressure = main.getDouble("pressure");
                    }
                }

                if(names.getString(i).equals("weather")){
                    JSONObject weather = new JSONObject(json.getJSONArray("weather").getString(0));
                    if(weather.has("description")){
                        description = weather.getString("description");
                    }
                    if(weather.has("icon")){
                        icon = weather.getString("icon");
                    }
                }

                if(names.getString(i).equals("wind")){
                    JSONObject wind = json.getJSONObject("wind");
                    if(wind.has("speed")){
                        wind_speed = wind.getDouble("speed");
                    }
                    if(wind.has("deg")){
                        wind_direction = wind.getDouble("deg");
                    }
                }

                if(names.getString(i).equals("rain")){
                    JSONObject rain = json.getJSONObject("rain");
                    if(rain.has("3h")){
                        precipitation = rain.getDouble("3h");
                    }
                }

                if(names.getString(i).equals("snow")){
                    JSONObject snow = json.getJSONObject("snow");
                    if(snow.has("3h")){
                        precipitation = snow.getDouble("3h");
                    }
                }

                if(names.getString(i).equals("name")){
                    city = json.getString("name");
                }
            }

            WeatherItem weatherItem = new WeatherItem(mContext,date,city,temperature,description,icon,
                    precipitation,humidity,pressure,wind_speed,wind_direction);
            return weatherItem;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
