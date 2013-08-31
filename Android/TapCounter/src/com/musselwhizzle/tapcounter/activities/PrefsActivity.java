package com.musselwhizzle.tapcounter.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.musselwhizzle.tapcounter.R;

public class PrefsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

}
