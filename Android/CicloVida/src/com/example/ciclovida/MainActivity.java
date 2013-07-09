package com.example.ciclovida;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	StringBuilder builder = new StringBuilder();
	TextView textView;

	private void log(String text) {
		Log.d("Ciclo de vida de una activity", text);
		builder.append(text);
		builder.append('\n');
		textView.setText(builder.toString());		
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView = (TextView)findViewById(R.id.logTV);
		
		//textView = new TextView(this);
		//textView.setText(builder.toString());
		//setContentView(textView);
		log("Estado: created");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		log("Estado: resumed");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		log("Estado: paused");
		if (isFinishing()) {
			log("finishing");
		}
	}	

}
