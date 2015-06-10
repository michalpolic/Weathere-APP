package com.michalpolic.weather.android.entity;

import android.app.Activity;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.michalpolic.weather.android.R;

import java.util.Date;

/**
 * Created by Michal on 8.6.2015.
 */
public class WeatherItem {

    private Activity mContext;

    private int mDatabaseId;
    private Date mDate;
    private String mCity;
    private String mDescription;
    private String mIcon;
    private double mTemperature;
    private double mPrecipitation;
    private double mHumidity;
    private double mPressure;
    private double mWind;
    private double mDirection;


    public WeatherItem(Activity context, Date mDate, String mCity, double mTemperature, String mDescription, String mIcon, double mPrecipitation,
                       double mHumidity, double mPressure, double mWind, double mDirection) {

        this.mDatabaseId = -1;
        this.mContext = context;
        this.mDate = mDate;
        this.mCity = mCity;
        this.mTemperature = mTemperature;
        this.mDescription = mDescription;
        this.mIcon = mIcon;
        this.mPrecipitation = mPrecipitation;
        this.mHumidity = mHumidity;
        this.mPressure = mPressure;
        this.mWind = mWind;
        this.mDirection = mDirection;
    }


    public WeatherItem(Activity context, int id, Date mDate, String mCity, double mTemperature, String mDescription, String mIcon, double mPrecipitation,
                       double mHumidity, double mPressure, double mWind, double mDirection) {

        this.mContext = context;
        this.mDatabaseId = id;
        this.mDate = mDate;
        this.mCity = mCity;
        this.mTemperature = mTemperature;
        this.mDescription = mDescription;
        this.mIcon = mIcon;
        this.mPrecipitation = mPrecipitation;
        this.mHumidity = mHumidity;
        this.mPressure = mPressure;
        this.mWind = mWind;
        this.mDirection = mDirection;
    }


    public WeatherItem(Activity context, Cursor c) {
        this.mContext = context;
        this.mDatabaseId = c.getInt(0);
        this.mDate = new Date(c.getInt(1));
        this.mCity = c.getString(2);
        this.mDescription = c.getString(3);
        this.mIcon = c.getString(4);
        this.mTemperature = c.getDouble(5);
        this.mPrecipitation = c.getDouble(6);
        this.mHumidity = c.getDouble(7);
        this.mPressure = c.getDouble(8);
        this.mWind = c.getDouble(9);
        this.mDirection = c.getDouble(10);
    }



    public long getmDate() {
        return mDate.getTime();
    }


    public String getmIcon(){
        return mIcon;
    }


    public String getmDescription() {
        return mDescription.substring(0,1).toUpperCase() + mDescription.substring(1);
    }


    public String getDescriptionHome() {
        String[] parts = mDescription.split(" ");
        StringBuilder out = new StringBuilder("");
        for(int i = 0; i < parts.length; i++){
            out.append(parts[i].substring(0, 1).toUpperCase());
            out.append(parts[i].substring(1)).append(" ");
        }
        return out.toString();
    }


    public double getmTemperature() {
        return mTemperature;
    }


    public String getTemperatureHome() {
        String unit = PreferenceManager.getDefaultSharedPreferences(mContext.getBaseContext()).getString("unit_of_temperature",mContext.getString(R.string.celsius));
        if(unit.equals(mContext.getString(R.string.fahrenheit))){
            return String.format( "%.0f°", ((mTemperature - 273.15)*1.8 + 32));
        }
        if(unit.equals(mContext.getString(R.string.kelvin))){
            return String.format( "%.0fK", mTemperature);
        }
        return String.format( "%.0f°", (mTemperature - 273.15));
    }


    public String getTemperatureFormated() {
        String unit = PreferenceManager.getDefaultSharedPreferences(mContext.getBaseContext()).getString("unit_of_temperature",mContext.getString(R.string.kelvin));
        if(unit.equals(mContext.getString(R.string.celsius))){
            return String.format( "%.0f °C", (mTemperature - 273.15));
        }
        if(unit.equals(mContext.getString(R.string.fahrenheit))){
            return String.format( "%.0f °F", ((mTemperature - 273.15)*1.8 + 32));
        }
        return String.format( "%.0f K", mTemperature);
    }


    public double getmPrecipitation() {
        return mPrecipitation;
    }


    public String getPrecipitationFormated() {
        return String.format( "%.0f mm", mPrecipitation);
    }


    public double getmHumidity() {
        return mHumidity;
    }


    public String getHumidityFormated() {
        return String.format( "%.0f%%", mHumidity);
    }


    public double getmPressure() {
        return mPressure;
    }


    public String getPressureFormated() {
        return String.format( "%.0f hPa", mPressure);
    }


    public double getmWind() {
        return mWind;
    }


    public String getWindFormated() {
        String unit = PreferenceManager.getDefaultSharedPreferences(mContext.getBaseContext()).getString("unit_of_length",mContext.getString(R.string.meter));
        if(unit.equals(mContext.getString(R.string.miles))){
            return String.format( "%.0f mi/h", (mWind * 2.2369362920544));
        }
        return String.format( "%.0f km/h", (mWind * 3.6) );
    }


    public double getmDirection() {
        return mDirection;
    }


    public String getDirectionFormated() {
        double[] tresholds = new double[]{337.5,292.5,247.5,202.5,157.5,112.5,67.5,22.5};
        String[] directions = new String[]{"E","SE","S","SW","W","NW","N","NE"};
        for(int i = 0; i<tresholds.length; i++){
            if(this.mDirection > tresholds[i]){
                return directions[i];
            }
        }
        return "E";
    }


    public String getmCity() {
        return mCity;
    }


    public int getmDatabaseId() {
        return mDatabaseId;
    }


    @Override
    public String toString() {
        return "WeatherItem{" +
                "City=" + mCity +
                "mDate=" + mDate.toString() +
                ", mTemperature=" + getTemperatureFormated() +
                ", mDescription='" + mDescription + '\'' +
                ", mIcon='" + mIcon + '\'' +
                ", mPrecipitation=" + getPrecipitationFormated() +
                ", mHumidity=" + getHumidityFormated() +
                ", mPressure=" + getPressureFormated() +
                ", mWind=" + getWindFormated() +
                ", mDirection=" + getDirectionFormated() +
                '}';
    }
}
