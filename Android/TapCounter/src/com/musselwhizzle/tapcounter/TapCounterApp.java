package com.musselwhizzle.tapcounter;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class TapCounterApp extends Application {
	
	private static final String TAG = TapCounterApp.class.getSimpleName();
	private static TapCounterApp instance;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "TapCounterApp.onCreate was called");
		instance = this;
	}
	
	public static Context getContext() {
		return instance.getApplicationContext();
	}
}
