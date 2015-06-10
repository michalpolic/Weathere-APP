package com.michalpolic.weather.android.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.michalpolic.weather.android.R;
import com.michalpolic.weather.android.activity.WeatherActivity;
import com.michalpolic.weather.android.database.query.QueryClass;
import com.michalpolic.weather.android.entity.WeatherItem;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    private static TodayFragment fragment = null;
    private static QueryClass mQueryClass = null;

    public static TodayFragment newInstance() {
        if(fragment==null) {
            fragment = new TodayFragment();
        }
        return fragment;
    }

    public TodayFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((WeatherActivity) getActivity()).setmTitle(getString(R.string.title_today));
    }


    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    public void updateView(){
        // load database
        if(mQueryClass == null) mQueryClass = new QueryClass(getActivity());

        // show data from database if exist
        WeatherItem item = mQueryClass.loadTodayItem();
        View v = getView();
        if(item != null && v != null) {
            File home = getActivity().getFilesDir();
            // scale if isn't space
            if(item.getmCity().length() > 10){
                TableRow.LayoutParams params;
                String unit = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("unit_of_temperature",getActivity().getString(R.string.celsius));
                if(unit.equals(getActivity().getString(R.string.fahrenheit))) {
                    params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, (float)0.01);
                    ((TextView) v.findViewById(R.id.temperature_home)).setTextSize(50);
                    ((TextView) v.findViewById(R.id.temperature_home)).setPadding(0, 20, 0, 0);
                }else{
                    params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, (float)0.2);
                }
                ((TableRow) v.findViewById(R.id.home_space1)).setLayoutParams(params);
            }

            ((TextView) v.findViewById(R.id.city_home)).setText(item.getmCity());
            ((TextView) v.findViewById(R.id.description_home)).setText(item.getDescriptionHome());
            ((TextView) v.findViewById(R.id.temperature_home)).setText(item.getTemperatureHome());
            ((ImageView) v.findViewById(R.id.icon_home)).setImageDrawable(Drawable.createFromPath(home.getPath() + File.separator + item.getmIcon() + ".png" ));
            ((TextView) v.findViewById(R.id.humidity_home)).setText(item.getHumidityFormated());
            ((TextView) v.findViewById(R.id.precipitation_home)).setText(item.getPrecipitationFormated());
            ((TextView) v.findViewById(R.id.pressure_home)).setText(item.getPressureFormated());
            ((TextView) v.findViewById(R.id.wind_home)).setText(item.getWindFormated());
            ((TextView) v.findViewById(R.id.direction_home)).setText(item.getDirectionFormated());
        }
    }
}
