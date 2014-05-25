package pl.edu.agh.iobber.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import pl.edu.agh.iobber.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}