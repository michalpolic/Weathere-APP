package com.michalpolic.weather.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.michalpolic.weather.android.R;
import com.michalpolic.weather.android.activity.WeatherActivity;
import com.michalpolic.weather.android.adapter.WeatherAdapter;
import com.michalpolic.weather.android.database.query.QueryClass;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class ForecastFragment extends Fragment {

    private AbsListView mListView;
    private WeatherAdapter mAdapter;
    private static ForecastFragment mForecastFragment = null;
    private QueryClass mQueryClass = null;

    public static ForecastFragment newInstance() {
        if(mForecastFragment == null) {
            mForecastFragment = new ForecastFragment();
        }
        return mForecastFragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager
     */
    public ForecastFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new WeatherAdapter(getActivity(), R.layout.item_weather_list, 0, new ArrayList());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecastitem, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((WeatherActivity) getActivity()).setmTitle(getString(R.string.title_forecast));
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }


    public void updateView() {
        // load database
        if(mQueryClass == null) mQueryClass = new QueryClass(getActivity());

        mAdapter.clear();
        mAdapter.addAll(mQueryClass.loadForecastItems());
        mAdapter.notifyDataSetChanged();
    }
}
