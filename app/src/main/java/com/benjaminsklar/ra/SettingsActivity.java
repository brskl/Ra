package com.benjaminsklar.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {
    static final String sSettingsRandomSeedEnabled = "RandomSeedEnabled"; // matches setting.xml key
    static final String sSettingsRandomSeedValue = "RandomSeedValue";
    static final String sSettingsAnimationEnabled = "AnimationEnabled"; // matches setting.xml key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display SettingsFragment as the main content
        getFragmentManager().beginTransaction()
                .replace(R.id.uxFragmentContainer, new SettingsFragment())
                .commit();
    }

}
