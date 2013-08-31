package com.musselwhizzle.tapcounter.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.musselwhizzle.tapcounter.R;
import com.musselwhizzle.tapcounter.controllers.TapListController;
import com.musselwhizzle.tapcounter.lists.CounterListAdapter;
import com.musselwhizzle.tapcounter.vos.CounterVo;

public class TapListActivity extends Activity implements Handler.Callback {
	
	private ArrayList<CounterVo> counters;
	private CounterListAdapter adapter;
	private TapListController controller;
	private CounterVo contextCounter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tap_list);
		
		counters = new ArrayList<CounterVo>();
		controller = new TapListController(counters);
		controller.addOutboxHandler(new Handler(this));
		
		adapter = new CounterListAdapter(this, counters);
		ListView list = (ListView)findViewById(R.id.listView);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int i, long l) {
				Intent intent = new Intent(TapListActivity.this, TapActivity.class);
				intent.putExtra(TapActivity.EXTRA_TAP_ID, adapter.getItem(i).getId());
				startActivity(intent);
			}
		});
		registerForContextMenu(list);
		
		fetchData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		controller.dispose();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.listView) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			contextCounter = adapter.getItem(info.position);
			if (contextCounter.getLabel().length() > 0) {
				menu.setHeaderTitle(contextCounter.getLabel());
			} else {
				menu.setHeaderTitle(getResources().getString(R.string.select));
			}
			menu.add(Menu.NONE, 0, 0, getResources().getString(R.string.load));
			menu.add(Menu.NONE, 1, 1, getResources().getString(R.string.delete));
			menu.add(Menu.NONE, 2, 2, getResources().getString(R.string.increment));
			menu.add(Menu.NONE, 3, 3, getResources().getString(R.string.decrement));
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				Intent intent = new Intent(TapListActivity.this, TapActivity.class);
				intent.putExtra(TapActivity.EXTRA_TAP_ID, contextCounter.getId());
				startActivity(intent);
				return true;
			case 1: 
				return controller.handleMessage(TapListController.MESSAGE_DELETE_COUNTER, contextCounter.getId());
			case 2: 
				return controller.handleMessage(TapListController.MESSAGE_INCREMENT_COUNTER, contextCounter);
			case 3: 
				return controller.handleMessage(TapListController.MESSAGE_DECREMENT_COUNTER, contextCounter);
			
			default: 
				return super.onContextItemSelected(item);
				
		}
	}
	
	private void fetchData() {
		controller.handleMessage(TapListController.MESSAGE_GET_COUNTERS);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case TapListController.MESSAGE_MODEL_UPDATED:
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
				return true;
		}
		return false;
	}
}
