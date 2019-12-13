package com.cycling_advocacy.bumpy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            setUserIdentifier();

        }

        private void setUserIdentifier() {
            Preference preference = findPreference(getString(R.string.pref_uid_key));
            if (preference != null) {
                String userIdentifier = PreferenceUtil.getDeviceUUID(getContext());
                preference.setSummary(userIdentifier);
            }
        }
    }
}