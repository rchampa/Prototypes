package com.musselwhizzle.tapcounter.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.musselwhizzle.tapcounter.R;
import com.musselwhizzle.tapcounter.controllers.TapController;
import com.musselwhizzle.tapcounter.vos.CounterVo;
import com.musselwhizzle.tapcounter.vos.OnChangeListener;

public class TapActivity extends Activity implements OnChangeListener<CounterVo> {
	
	// monkey -p com.musselwhizzle.tapcounter -v 2000 --pct-syskeys 0 --pct-anyevent 0
	
	// TODO: Tap noise should happen on touch and not release, feels like too much latency. 
	
	private static final String TAG = TapActivity.class.getSimpleName();
	private TapController controller;
	private CounterVo counter;
	
	private EditText label;
	private TextView count;
	private ImageView plusBtn;
	private Button minusBtn;
	private CompoundButton lockedBtn;
	private Dialog saveDialog;
	
	public static final String EXTRA_TAP_ID = "tapId";
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);
        
        counter = new CounterVo();
        counter.addListener(this);
        controller = new TapController(counter);
        
        label = (EditText)findViewById(R.id.label);
        count = (TextView)findViewById(R.id.countView);
        plusBtn = (ImageView)findViewById(R.id.plusBtn);
        minusBtn = (Button)findViewById(R.id.minusBtn);
        lockedBtn = (CompoundButton)findViewById(R.id.lockBtn);
        
        int tapId = getIntent().getIntExtra(EXTRA_TAP_ID, -1);
        Log.i(TAG, "/onCreate tapId:" + tapId);
        controller.handleMessage(TapController.MESSAGE_POPULATE_MODEL_BY_ID, tapId);
        
        
        label.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				controller.handleMessage(TapController.MESSAGE_UPDATE_LABEL, s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        
        plusBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.handleMessage(TapController.MESSAGE_INCREMENT_COUNT);
			}
		});
        
        minusBtn.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		controller.handleMessage(TapController.MESSAGE_DECREMENT_COUNT);
        	}
        });
        
        lockedBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				controller.handleMessage(TapController.MESSAGE_UPDATE_LOCK, isChecked);
			}
		});
    }
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if (counter.getId() > 0) {
			controller.handleMessage(TapController.MESSAGE_POPULATE_MODEL_BY_ID, counter.getId());
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		controller.dispose();
		if (saveDialog != null && saveDialog.isShowing()) saveDialog.dismiss();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.reset:
				return controller.handleMessage(TapController.MESSAGE_RESET_COUNT);
			case R.id.save:
				return controller.handleMessage(TapController.MESSAGE_SAVE_MODEL);
			case R.id.prefs:
				startActivity(new Intent(this, PrefsActivity.class));
				return true;
			case R.id.taps:
				startActivity(new Intent(this, TapListActivity.class));
				return true;
			case R.id.new_:
				createSaveDialog();
				return true;
			case R.id.source:
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.therealjoshua.com/blog/"));
				startActivity(intent);
				return true;
			default: 
				return false;
			
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean handled = controller.handleMessage(TapController.MESSAGE_KEY_EVENT, event);
		if (!handled) {
			return super.dispatchKeyEvent(event);
		}
		return handled;
	}

	@Override
	public void onChange(CounterVo counter) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateView();
			}
		});
	}
	
	private void createSaveDialog() {
		if (saveDialog != null && saveDialog.isShowing()) saveDialog.dismiss();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Save current counter?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				controller.handleMessage(TapController.MESSAGE_SAVE_MODEL);
				controller.handleMessage(TapController.MESSAGE_CREATE_NEW_MODEL);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				controller.handleMessage(TapController.MESSAGE_CREATE_NEW_MODEL);
			}
		});
		saveDialog = builder.show();
	}
	
	private void updateView() {
		if (!label.getText().toString().equals(counter.getLabel()))
			label.setText(counter.getLabel());
		label.setEnabled(!counter.isLocked());
		count.setText(Integer.toString(counter.getCount()));
		lockedBtn.setChecked(counter.isLocked());
	}
}