package com.michalpolic.weather.android.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.michalpolic.weather.android.activity.WeatherActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by Michal on 9.6.2015.
 */
public class ImageLoaderAsyncTask extends AsyncTask<Activity, Integer, Activity> {

    private static final String[] icons = new String[]{"01d.png","01n.png","02d.png","02n.png",
    "03d.png","03n.png","04d.png","04n.png","09d.png","09n.png","10d.png","10n.png","11d.png",
    "11n.png","13d.png","13n.png","50d.png","50n.png"};

    @Override
    protected Activity doInBackground(Activity... context) {
        File f = context[0].getFilesDir();
        for(int i = 0; i < icons.length; i++) {
            File icon = new File(f.getPath() + File.separator + icons[i]);
            if(!icon.exists()) {
                URL url = null;
                try {
                    url = new URL("http://openweathermap.org/img/w/" + icons[i]);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    FileOutputStream out = new FileOutputStream(icon);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return context[0];
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(Activity a) {
        ((WeatherActivity)a).onWeatherUpdated();
    }
}
