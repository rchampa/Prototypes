package com.musselwhizzle.tapcounter.test.controllers;

import android.test.AndroidTestCase;

import com.musselwhizzle.tapcounter.controllers.TapController;
import com.musselwhizzle.tapcounter.vos.CounterVo;

public class LockedTapControllerTest extends AndroidTestCase {
	
	@SuppressWarnings("unused")
	private static final String TAG = LockedTapControllerTest.class.getSimpleName();
	
	private CounterVo counter;
	private TapController controller;
	
	
	public LockedTapControllerTest() {
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		counter = new CounterVo();
		controller = new TapController(counter);
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, true);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		controller.dispose();
	}
	
	public void testIncrementModel() {
		counter.setCount(5);
		controller.handleMessage(TapController.MESSAGE_INCREMENT_COUNT);
		assertEquals(5, counter.getCount());
	}
	
	public void testLabelUpdate() {
		controller.handleMessage(TapController.MESSAGE_UPDATE_LABEL, "Hello World");
		assertEquals("", counter.getLabel());
	}
	
	public void testResetCount() {
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, false);
		counter.setCount(5);
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, true);
		controller.handleMessage(TapController.MESSAGE_RESET_COUNT);
		assertEquals(5, counter.getCount());
	}
	
	public void testLockedSave() {
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, false);
		counter.setCount(44);
		counter.setLabel("Test");
		controller.handleMessage(TapController.MESSAGE_SAVE_MODEL);
		
		
	}
}