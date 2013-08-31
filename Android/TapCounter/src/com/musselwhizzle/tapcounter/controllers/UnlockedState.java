package com.musselwhizzle.tapcounter.controllers;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;

import com.musselwhizzle.tapcounter.R;
import com.musselwhizzle.tapcounter.TapCounterApp;

public class UnlockedState extends TapState implements OnInitListener, OnSharedPreferenceChangeListener {
	
	private static final String TAG = UnlockedState.class.getSimpleName();
	private boolean allowClick = false;
	private boolean allowVoice = false;
	private boolean allowVibrate = false;
	private int voiceInterval = -1;
	private Vibrator vibrator;
	private TextToSpeech voice;
	private boolean voiceAvailable = false;
	private SoundPool soundPool;
	private float volume;
	private int clickId = -1;
	
	public UnlockedState(TapController controller) {
		super(controller);
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		
		setPrefs();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (voice != null) {
			voice.stop();
			voice.shutdown();
		}
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TapCounterApp.getContext());
		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public boolean handleMessage(int what) {
		switch(what) {
			case TapController.MESSAGE_INCREMENT_COUNT:
				moveCount(1);
				return true;
			case TapController.MESSAGE_DECREMENT_COUNT:
				moveCount(-1);
				return true;
			case TapController.MESSAGE_RESET_COUNT:
				model.setCount(0);
				return true;
			default:
				return super.handleMessage(what);
		}
		
	}
	
	@Override
	public boolean handleMessage(int what, Object data) {
		switch(what) {
			case TapController.MESSAGE_UPDATE_LOCK:
				updateLock((Boolean)data);
				return true;
			case TapController.MESSAGE_UPDATE_LABEL:
				updateLabel((String)data);
				return true;
			case TapController.MESSAGE_KEY_EVENT:
				return handleKeyEvent((KeyEvent)data);
			default:
				return super.handleMessage(what, data);
		}
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			Locale locale = new Locale(Locale.getDefault().getLanguage());
			int result = voice.setLanguage(locale);
			if (result == TextToSpeech.LANG_AVAILABLE) {
				voiceAvailable = true;
			} else {
				Log.i(TAG, "Language not available.");
			}
		} else {
			Log.i(TAG, "Voice service not available");
		}
		
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		setPrefs();
	}
	
	private boolean handleKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (action == KeyEvent.ACTION_UP) {
					moveCount(1);
					return true;
				} else {
					return true; // we are handling all actions, just ignoring the others.
				}
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (action == KeyEvent.ACTION_UP) {
					moveCount(-1);
					return true;
				} else {
					return true;
				}
			default:
				return false;
		}
	}
	
	private void moveCount(int amount) {
		model.setCount(model.getCount()+amount);
		
		if (allowVibrate) {
			vibrator.vibrate(50);
		}
		if (allowVoice && voiceAvailable && voiceInterval > -1) {
			int interval = model.getCount() % voiceInterval;
			if (interval == 0) {
				voice.speak(Integer.toString(model.getCount()), TextToSpeech.QUEUE_FLUSH, null);
			}
		}
		if (allowClick && clickId > -1) {
			soundPool.play(clickId, volume, volume, 1, 0, 1);
		}
	}
	
	private void updateLock(boolean lock) {
		model.setLocked(lock);
		controller.setMessageState(new LockedState(controller));
	}
	
	private void updateLabel(String label) {
		model.setLabel(label);
	}
	
	private void setPrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TapCounterApp.getContext());
		prefs.registerOnSharedPreferenceChangeListener(this);
		allowClick = prefs.getBoolean("click", true);
		allowVoice = prefs.getBoolean("speak", true);
		allowVibrate = prefs.getBoolean("vibrate", true);
		voiceInterval = new Integer(prefs.getString("speak_interval", "-1"));
		volume = prefs.getInt("volume", 70);
		volume /= 100;
		
		if (allowVibrate) {
			vibrator = (Vibrator)TapCounterApp.getContext().getSystemService(Context.VIBRATOR_SERVICE);
		}
		if (allowVoice) {
			voice = new TextToSpeech(TapCounterApp.getContext(), this);
		}
		
		if (allowClick) {
			clickId = soundPool.load(TapCounterApp.getContext(), R.raw.klik, 1);
		}
	}

	
}
