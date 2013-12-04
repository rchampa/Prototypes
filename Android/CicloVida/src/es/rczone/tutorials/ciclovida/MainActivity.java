package es.rczone.tutorials.ciclovida;

import com.example.ciclovida.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

	private StringBuilder builder = new StringBuilder();
	private TextView textView;
	private long timer;
	private final long control = 1500;
	
	public void onClick(View v){
		
		if(v.getId()==R.id.button1){
			Intent intent = new Intent(this, AnotherActivity.class);
        	startActivity(intent);
			
			
		}
	}

	protected void log(String text) {
		
		if(System.currentTimeMillis()-timer>control){
			builder.append("----------------------------------");
			builder.append('\n');
		}
		
		Log.d("Activity lifecycle", text);
		builder.append(text);
		builder.append('\n');
		textView.setText(builder.toString());		
		
		timer = System.currentTimeMillis();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView = (TextView)findViewById(R.id.logTV);
		
		log("State: created");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		log("State: started");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		log("State: resumed");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		log("State: paused");
		if (isFinishing()) {
			log("finishing");
		}
	}	
	
	@Override
	protected void onStop() {
		super.onStop();
		log("State: stopped");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		log("State: restarted");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		log("State: destroyed");
	}

}
