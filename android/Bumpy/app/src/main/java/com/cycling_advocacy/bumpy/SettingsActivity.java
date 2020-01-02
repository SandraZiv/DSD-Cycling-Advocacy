package com.cycling_advocacy.bumpy;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navigation_settings, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            //init listener
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

            setUserIdentifier();
            setUploadTrip(sharedPreferences);
        }

        private void setUserIdentifier() {
            Preference preference = findPreference(getString(R.string.pref_uid_key));
            if (preference != null) {
                String userIdentifier = PreferenceUtil.getDeviceUUID(getContext());
                preference.setSummary(userIdentifier);
            }
        }

        // this automatically gets preference
        // and calls overloaded method that takes list preference as argument
        private void setUploadTrip(SharedPreferences sharedPreferences) {
            Preference preference = findPreference(getString(R.string.pref_upload_trip_data_key));
            if (preference != null) {
                setUploadTripDataSummary(sharedPreferences, (ListPreference) preference);
            }
        }

        private void setUploadTripDataSummary(SharedPreferences sharedPreferences, ListPreference listPreference) {
            int prefIndex = listPreference.findIndexOfValue(
                    sharedPreferences.getString(listPreference.getKey(), "0")
            );
            listPreference.setSummary(listPreference.getEntries()[prefIndex]);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);
            if (preference instanceof ListPreference) {
                setUploadTripDataSummary(sharedPreferences, (ListPreference) preference);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}