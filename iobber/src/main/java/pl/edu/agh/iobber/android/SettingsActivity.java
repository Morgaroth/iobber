package pl.edu.agh.iobber.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import java.util.logging.Logger;

import pl.edu.agh.iobber.R;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static Logger logger = Logger.getLogger(SettingsActivity.class.getSimpleName());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            preference.setOnPreferenceChangeListener(this);
            if (preference instanceof EditTextPreference) {
                updatePreference(preference, ((EditTextPreference) preference).getText());
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object o) {
        updatePreference(pref, o);
        return false;
    }

    private void updatePreference(Preference pref, Object value) {
        if (pref instanceof EditTextPreference) {
            EditTextPreference preference = (EditTextPreference) pref;
            String updatedKey = preference.getKey();
            if (updatedKey.equals(getString(R.string.PREF_DATE_FONT_SIZE))) {
                preference.setSummary(getString(R.string.set_font_size_in_date_field) + ": " + value);
            } else if (updatedKey.equals(getString(R.string.PREF_AUTHOR_FONT_SIZE))) {
                preference.setSummary(getString(R.string.set_font_size_in_author_field) + ": " + value);
            } else if (updatedKey.equals(getString(R.string.PREF_BODY_FONT_SIZE))) {
                preference.setSummary(getString(R.string.set_font_size_in_body_field) + ": " + value);
            }
        }
    }
}