package com.musselwhizzle.tapcounter.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {
	private SeekBar seekBar;
	private final int DEFAULT_VALUE = 80;
	
	
	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		
		LinearLayout frame = (LinearLayout)view.findViewById(android.R.id.widget_frame);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)frame.getLayoutParams();
		params.weight = 2f;
		
		seekBar = new SeekBar(getContext());
		seekBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT));
		seekBar.setProgress(getPersistedInt(DEFAULT_VALUE));
		seekBar.setOnSeekBarChangeListener(this);
		frame.addView(seekBar);
		
		if (shouldDisableDependents()) {
			seekBar.setEnabled(false);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (shouldPersist()) {
			persistInt(seekBar.getProgress());
		}
	}
}
