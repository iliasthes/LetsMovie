package com.example.android.letsmovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;


public class SortingSettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener //implements Preference.OnPreferenceChangeListener
 {
     public static final String KEY_SORTING_ORDER = "sorting_order_key";
    String sortingOrderValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsPreferenceFragment()).commit();
    }

     @Override
     public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
         if (key.equals(KEY_SORTING_ORDER)) {
             Preference connectionPref = findPreference(key);
             // Set summary to be the user-description for the selected value
             connectionPref.setSummary(sharedPreferences.getString(key, ""));
         }
     }



     //  @Override
 //   public boolean onPreferenceChange(Preference preference, Object value) {
 //       String stringValue = value.toString();
 //       if (preference instanceof ListPreference) {
  //          ListPreference listPreference = (ListPreference) preference;
   //         int prefIndex = listPreference.findIndexOfValue(stringValue);
   //         if (prefIndex > 0)
    //            preference.setSummary(listPreference.getEntries()[prefIndex]);
   //     } else
     //       preference.setSummary(stringValue);

    //    return true;
  //  }

    public static class SettingsPreferenceFragment extends PreferenceFragment {
            @Override
            public void onCreate(final Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
            }
        }


}
