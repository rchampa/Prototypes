package com.musselwhizzle.tapcounter.controllers;

import android.view.KeyEvent;

public class LockedState extends TapState {
	
	public LockedState(TapController controller) {
		super(controller);
	}

	@Override
	public boolean handleMessage(int what) {
		switch(what) {
			case TapController.MESSAGE_INCREMENT_COUNT:
			case TapController.MESSAGE_DECREMENT_COUNT:
			case TapController.MESSAGE_RESET_COUNT:
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
				return true;
			case TapController.MESSAGE_KEY_EVENT:
				return handleKeyEvent((KeyEvent)data);
			default:
				return super.handleMessage(what, data);
		}
	}
	
	private boolean handleKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				return true;
			default:
				return false;
		}
	}
		
	private void updateLock(boolean lock) {
		model.setLocked(lock);
		controller.setMessageState(new UnlockedState(controller));
	}
}
