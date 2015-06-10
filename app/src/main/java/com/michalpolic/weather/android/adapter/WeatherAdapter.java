package com.michalpolic.weather.android.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michalpolic.weather.android.R;
import com.michalpolic.weather.android.entity.WeatherItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Michal on 8.6.2015.
 */
public class WeatherAdapter extends ArrayAdapter {

    private final Activity mContext;


    class WeatherViewHolder {
        public TextView description;
        public TextView temperature;
        public ImageView icon;
    }


    public WeatherAdapter(Activity context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_weather_list, parent, false);
            WeatherViewHolder viewHolder = new WeatherViewHolder();
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon_weather_id);
            viewHolder.description = (TextView) rowView.findViewById(R.id.description_weather_id);
            viewHolder.temperature = (TextView) rowView.findViewById(R.id.temperature_wheather_id);
            rowView.setTag(viewHolder);
        }

        WeatherViewHolder holder = (WeatherViewHolder) rowView.getTag();
        WeatherItem i = (WeatherItem) this.getItem(position);
        holder.description.setText(i.getmDescription() +  " on " +  new SimpleDateFormat("EEEE").format(i.getmDate()));
        holder.temperature.setText(i.getTemperatureFormated());

        File f = new File(mContext.getFilesDir() + File.separator + i.getmIcon() + ".png");
        if(f.exists()) {
            holder.icon.setImageDrawable(Drawable.createFromPath(f.getPath()));
        }

        return rowView;
    }

}
