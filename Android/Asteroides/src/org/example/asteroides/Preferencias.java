package org.example.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias);
	}

    

}
