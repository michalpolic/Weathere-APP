package com.michalpolic.weather.android.fragment;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.michalpolic.weather.android.R;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static SettingsFragment mSettingsFragment = null;


    public static SettingsFragment newInstance() {
        if(mSettingsFragment == null) {
            mSettingsFragment = new SettingsFragment();
        }
        return mSettingsFragment;
    }

    public SettingsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_hardware_keyboard_backspace);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            CharSequence listDesc = listPref.getEntry();
            if (!TextUtils.isEmpty(listDesc)) {
                pref.setSummary(listDesc);
            }
        }
    }

}
