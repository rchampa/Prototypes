package com.ricardo.pestanias;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Resources res = getResources();
        
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_menu_myplaces));
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Pestania2", res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);
        
        tabs.setCurrentTab(0);
        
        tabs.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				Log.i("Pestania", "Pulsada pestaña: " + tabId);
			}
		});
    }

}
