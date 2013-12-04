package es.rczone.tutorials.ciclovida;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import es.rczone.tutorials.R;
import es.rczone.tutorials.tools.MyDebug;


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
		
		if(MyDebug.LOG) Log.d("Activity lifecycle", text);
		
		builder.append(text);
		builder.append('\n');
		textView.setText(builder.toString());		
		
		timer = System.currentTimeMillis();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceEstado) {
		
		super.onCreate(savedInstanceEstado);
		setContentView(R.layout.activity_main);
		
		textView = (TextView)findViewById(R.id.logTV);
		
		log("Estado: created");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		log("Estado: started");
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
	
	@Override
	protected void onStop() {
		super.onStop();
		log("Estado: stopped");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		log("Estado: restarted");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		log("Estado: destroyed");
	}

}
