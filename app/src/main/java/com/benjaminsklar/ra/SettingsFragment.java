package com.benjaminsklar.ra;

import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Created by Ben on 4/21/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

}
