package com.michalpolic.weather.android.database.query;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.michalpolic.weather.android.database.model.DatabaseHelper;
import com.michalpolic.weather.android.entity.WeatherItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michal on 10.6.2015.
 */
public class QueryClass {
    private Activity mContext;
    private DatabaseHelper mHelper = null;
    private SQLiteDatabase mDB = null;


    public QueryClass(Activity context) {
        this.mContext = context;
        mHelper = new DatabaseHelper(this.mContext.getBaseContext());
        mDB = mHelper.getWritableDatabase();
    }


    //Load last update of today weather.
    public WeatherItem loadTodayItem() {
        WeatherItem item = null;

        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = format.format(new Date(now.getTime()-(24*3600*1000)));
        String tomorrow = format.format(new Date(now.getTime()+(24*3600*1000)));

        Cursor c = null;
        try {
            c = mDB.query(DatabaseHelper.sWEATHER_TABLE_NAME,
                    new String[]{"id","date","city","description","icon","temperature","precipitation","humidity","pressure","wind","direction"},
                    "date > ? AND date < ? ", new String[]{String.valueOf(format.parse(yesterday).getTime()),
                            String.valueOf(format.parse(tomorrow).getTime())}, null, null, "date DESC","0,1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(c.moveToFirst() != false) {
            item = new WeatherItem(mContext, c);
        }
        return item;
    }


    // insert
    public boolean insertItem(WeatherItem item){
        ContentValues cv = fillCV(new ContentValues(), item);
        if(mDB.insert(DatabaseHelper.sWEATHER_TABLE_NAME, null, cv) == -1){
            return false;
        }else {
            return true;
        }
    }


    // update forecast for city
    public void updateItems(ArrayList<WeatherItem> forecastItems) {
        //deleteForecastForCity(forecastItems.get(0).getmCity());
        deleteFuture();
        for(int i = 0; i < forecastItems.size(); i++){
            insertItem(forecastItems.get(i));
        }
    }


    // delete forecast for city
    public boolean deleteForecastForCity(String city){
        if(mDB.delete(DatabaseHelper.sWEATHER_TABLE_NAME, "city=? and date>?", new String[]{city,String.valueOf(System.currentTimeMillis())}) == 0){
            return false;
        }else{
            return true;
        }
    }


    // delete
    public boolean deleteFuture(){
        if(mDB.delete(DatabaseHelper.sWEATHER_TABLE_NAME, "date>?", new String[]{String.valueOf(System.currentTimeMillis())}) == 0){
            return false;
        }else{
            return true;
        }
    }


    private ContentValues fillCV(ContentValues cv, WeatherItem item){
        if(item.getmDatabaseId() != -1){
            cv.put("id", item.getmDatabaseId());
        }
        cv.put("date", item.getmDate());
        cv.put("city", item.getmCity());
        cv.put("description", item.getmDescription());
        cv.put("icon", item.getmIcon());
        cv.put("temperature", item.getmTemperature());
        cv.put("precipitation", item.getmPrecipitation());
        cv.put("humidity", item.getmHumidity());
        cv.put("pressure", item.getmPressure());
        cv.put("wind", item.getmWind());
        cv.put("direction", item.getmDirection());
        return cv;
    }


    public ArrayList<WeatherItem> loadForecastItems() {
        ArrayList<WeatherItem> items = new ArrayList<>();
        //WeatherItem i = loadTodayItem();

        Cursor c = null;
        c = mDB.query(DatabaseHelper.sWEATHER_TABLE_NAME,
                new String[]{"id","date","city","description","icon","temperature","precipitation","humidity","pressure","wind","direction"},
                "date >= ?", new String[]{String.valueOf(System.currentTimeMillis())}, null, null, "date ASC",null);  //  and city = ? - i.getmCity()

        if(c.moveToFirst() != false) {
            items.add(new WeatherItem(mContext, c));
            while (c.moveToNext() != false) {
                items.add(new WeatherItem(mContext, c));
            }
        }
        return items;
    }
}
