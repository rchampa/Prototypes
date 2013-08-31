package com.musselwhizzle.tapcounter.controllers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;

import com.musselwhizzle.tapcounter.daos.CounterDao;
import com.musselwhizzle.tapcounter.vos.CounterVo;

public class TapListController extends Controller {
	
	@SuppressWarnings("unused")
	private static final String TAG = TapListController.class.getSimpleName();
	private HandlerThread workerThread;
	private Handler workerHandler;
	
	
	public static final int MESSAGE_GET_COUNTERS = 1;
	public static final int MESSAGE_MODEL_UPDATED = 2;
	public static final int MESSAGE_DELETE_COUNTER = 3;
	public static final int MESSAGE_INCREMENT_COUNTER = 4;
	public static final int MESSAGE_DECREMENT_COUNTER = 5;
	
	private ArrayList<CounterVo> model;
	public ArrayList<CounterVo> getModel() {
		return model;
	}
	
	public TapListController(ArrayList<CounterVo> model) {
		this.model = model;
		workerThread = new HandlerThread("Worker Thread");
		workerThread.start();
		workerHandler = new Handler(workerThread.getLooper());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		workerThread.getLooper().quit();
	}
	
	
	@Override
	public boolean handleMessage(int what, Object data) {
		switch(what) {
			case MESSAGE_GET_COUNTERS:
				getCounters();
				return true;
			case MESSAGE_DELETE_COUNTER:
				deleteCounter((Integer)data);
				getCounters();
				return true;
			case MESSAGE_INCREMENT_COUNTER:
				changeCount(1, (CounterVo)data);
				getCounters();
				return true;
			case MESSAGE_DECREMENT_COUNTER:
				changeCount(-1, (CounterVo)data);
				getCounters();
				return true;
		}
		return false;
	}
	
	private void changeCount(final int amount, final CounterVo counter) {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (counter) {
					counter.setCount(counter.getCount() + amount);
					CounterDao dao = new CounterDao();
					dao.update(counter);
				}
			}
		});
		
	}
	
	private void getCounters() {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				CounterDao dao = new CounterDao();
				ArrayList<CounterVo> counters = dao.getAll();
				synchronized (model) {
					while(model.size() > 0) {
						model.remove(0);
					}
					for (CounterVo counter : counters) {
						model.add(counter);
					}
					notifyOutboxHandlers(MESSAGE_MODEL_UPDATED, 0, 0, null);
				}
			}
		});
	}
	
	private void deleteCounter(final int itemId) {
		workerHandler.post(new Runnable() {
			@Override
			public void run() {
				CounterDao dao = new CounterDao();
				dao.delete(itemId);
			}
		});
	}
}
