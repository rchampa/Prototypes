package com.musselwhizzle.tapcounter.controllers;

import android.util.Log;

import com.musselwhizzle.tapcounter.vos.CounterVo;

public class TapController extends Controller {
	
	private static final String TAG = TapController.class.getSimpleName();
	
	
	private ControllerState messageState;
	protected void setMessageState(ControllerState messageState) {
		if (this.messageState != null) {
			this.messageState.dispose();
		}
		this.messageState = messageState;
	}
	
	public static final int MESSAGE_SAVE_MODEL = 1;
	public static final int MESSAGE_INCREMENT_COUNT = 2;
	public static final int MESSAGE_DECREMENT_COUNT = 3;
	public static final int MESSAGE_UPDATE_LOCK = 4;
	public static final int MESSAGE_UPDATE_LABEL = 5;
	public static final int MESSAGE_KEY_EVENT = 6;
	public static final int MESSAGE_SAVE_COMPLETE = 7;
	public static final int MESSAGE_RESET_COUNT = 8;
	public static final int MESSAGE_POPULATE_MODEL_BY_ID = 9;
	public static final int MESSAGE_CREATE_NEW_MODEL = 10;
	
	private CounterVo model;
	public CounterVo getModel() {
		return model;
	}
	
	public TapController(CounterVo model) {
		this.model = model;
		messageState = new UnlockedState(this);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		messageState.dispose();
	}
	
	@Override
	public boolean handleMessage(int what) {
		Log.i(TAG, "handling message code of " + what);
		return messageState.handleMessage(what);
	}
	
	@Override
	public boolean handleMessage(int what, Object data) {
		Log.i(TAG, "handling message code of " + what);
		return messageState.handleMessage(what, data);
	}
}
