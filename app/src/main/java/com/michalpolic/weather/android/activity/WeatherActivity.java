package com.michalpolic.weather.android.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.michalpolic.weather.android.R;
import com.michalpolic.weather.android.dialog.DialogClass;
import com.michalpolic.weather.android.fragment.ForecastFragment;
import com.michalpolic.weather.android.fragment.NavigationDrawerFragment;
import com.michalpolic.weather.android.fragment.SettingsFragment;
import com.michalpolic.weather.android.fragment.TodayFragment;
import com.michalpolic.weather.android.task.ImageLoaderAsyncTask;
import com.michalpolic.weather.android.task.WeatherUpdateRunnable;


public class WeatherActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, WeatherUpdateRunnable.WeatherUpdatedListener {

    //private static final String TAG =  "WeatherActivity";
    public static final int sREFRESH_VIEW = 1;
    public static final int sNO_INTERNET_CONECTON = 2;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private boolean mSettingsEnable = false;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == sREFRESH_VIEW){
                onWeatherUpdated();
            }
            if(msg.what == sNO_INTERNET_CONECTON){
                internetConectionDialog();
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // create left-side menu fragment
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

        // start updating
        new ImageLoaderAsyncTask().execute(this);
        new Thread(WeatherUpdateRunnable.newInstance(this)).start();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0: getFragmentManager().beginTransaction().replace(R.id.container, TodayFragment.newInstance()).commit();
                mTitle = getString(R.string.title_today);
                break;
            case 1: getFragmentManager().beginTransaction().replace(R.id.container, ForecastFragment.newInstance()).commit();
                mTitle = getString(R.string.title_forecast);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.weather, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mSettingsEnable){
            mSettingsEnable = false;
            onNavigationDrawerItemSelected(mNavigationDrawerFragment.getCurrentSelectedPosition());
            restoreActionBar();
            return true;
        }

        switch (item.getItemId()){
            case android.R.id.home:
                mNavigationDrawerFragment.toogleDrawerLayout();
                return true;
            case R.id.action_settings:
                mSettingsEnable = true;
                getFragmentManager().beginTransaction().replace(R.id.container, SettingsFragment.newInstance()).commit();

                return true;
            case R.id.action_about:
                DialogClass.newInstance(this).showAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_navigation_menu);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void setmTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }


    /**
     * This method is called when location or weather was updated.
     */
    public void onWeatherUpdated(){
        if(mNavigationDrawerFragment.getCurrentSelectedPosition() == 0){
            TodayFragment.newInstance().updateView();
        }else if(mNavigationDrawerFragment.getCurrentSelectedPosition() == 1){
            ForecastFragment.newInstance().updateView();
        }
    }


    private void internetConectionDialog() {
        DialogClass.newInstance(this).showInternetDialog();
    }
}
