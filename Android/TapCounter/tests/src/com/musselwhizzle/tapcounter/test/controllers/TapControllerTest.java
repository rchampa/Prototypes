package com.musselwhizzle.tapcounter.test.controllers;

import android.test.AndroidTestCase;

import com.musselwhizzle.tapcounter.controllers.TapController;
import com.musselwhizzle.tapcounter.vos.CounterVo;

public class TapControllerTest extends AndroidTestCase {
	
	@SuppressWarnings("unused")
	private static final String TAG = TapControllerTest.class.getSimpleName();
	
	private CounterVo counter;
	private TapController controller;
	
	
	public TapControllerTest() {
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		counter = new CounterVo();
		controller = new TapController(counter);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		controller.dispose();
	}
	
	public void testIncrementModel() {
		counter.setCount(5);
		controller.handleMessage(TapController.MESSAGE_INCREMENT_COUNT);
		assertEquals(6, counter.getCount());
	}
	
	public void testLabelUpdate() {
		controller.handleMessage(TapController.MESSAGE_UPDATE_LABEL, "Hello World");
		assertEquals("Hello World", counter.getLabel());
	}
	
	public void testLock() {
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, true);
		assertTrue(counter.isLocked());
	}
	
	public void testLockedLabel() {
		counter.setLabel("foo");
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, true);
		controller.handleMessage(TapController.MESSAGE_UPDATE_LABEL, "Hello World");
		assertEquals("foo", counter.getLabel());
	}
	
	public void testLockedCount() {
		counter.setCount(5);
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, true);
		controller.handleMessage(TapController.MESSAGE_INCREMENT_COUNT);
		assertEquals(5, counter.getCount());
	}
	
	public void testResetCount() {
		counter.setCount(5);
		controller.handleMessage(TapController.MESSAGE_RESET_COUNT);
		assertEquals(0, counter.getCount());
	}
	
	public void testLockedResetCount() {
		counter.setCount(5);
		controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, true);
		controller.handleMessage(TapController.MESSAGE_RESET_COUNT);
		assertEquals(5, counter.getCount());
	}
	
	
}