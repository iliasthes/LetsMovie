package com.example.android.letsmovie;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class SortingSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsPreferenceFragment()).commit();

        bindPreferenceSummaryToValue( new SettingsPreferenceFragment().findPreference(getString(R.string.sorting_key)));
    }

    private void bindPreferenceSummaryToValue(Preference pref){
        pref.setOnPreferenceChangeListener(this);

        onPreferenceChange(pref, PreferenceManager.getDefaultSharedPreferences(pref.getContext()).getString(pref.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference,Object value){
        String stringValue = value.toString();
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if(prefIndex>0)
                preference.setSummary(listPreference.getEntries()[prefIndex]);
        }else
            preference.setSummary(stringValue);

        return true;

    }
    public static class SettingsPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
