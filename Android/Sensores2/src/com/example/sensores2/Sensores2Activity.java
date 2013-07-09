package com.example.sensores2;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Sensores2Activity extends Activity implements SensorEventListener {
	private List<Sensor> listaSensores;
	private TextView aTextView[][] = new TextView[20][3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout raiz = (LinearLayout) findViewById(R.id.raiz); 
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		listaSensores = sm.getSensorList(Sensor.TYPE_ALL);
		
		int n = 0;
		for (Sensor sensor : listaSensores) {
			TextView mTextView = new TextView(this);
			mTextView.setText(sensor.getName());
			raiz.addView(mTextView);
			LinearLayout nLinearLayout = new LinearLayout(this);
			raiz.addView(nLinearLayout);
			
			for (int i = 0; i < 3; i++) {
				aTextView[n][i] = new TextView(this);
				aTextView[n][i].setText("?");
				aTextView[n][i].setWidth(87);
			}
			
			TextView xTextView = new TextView(this);
			xTextView.setText("  X: ");
			nLinearLayout.addView(xTextView);
			nLinearLayout.addView(aTextView[n][0]);
			
			TextView yTextView = new TextView(this);
			yTextView.setText("  Y: ");
			nLinearLayout.addView(yTextView);
			nLinearLayout.addView(aTextView[n][1]);
			
			TextView zTextView = new TextView(this);
			zTextView.setText("  Z: ");
			nLinearLayout.addView(zTextView);
			nLinearLayout.addView(aTextView[n][2]);
			sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
			n++;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			int n = 0;
			for (Sensor sensor : listaSensores) {
				if (event.sensor == sensor) {
					for (int i = 0; i < event.values.length; i++) {
						aTextView[n][i]
								.setText(Float.toString(event.values[i]));
					}
				}
				n++;
			}
		}
	}
}