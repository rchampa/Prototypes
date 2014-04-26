package es.ric.listener;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  {

	private final String SPANISH = "es-ES";
	private final String ENGLISH = "en-US";
	
	protected static final int REQUEST_OK = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {
		 Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
         i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, SPANISH);
         try {
             startActivityForResult(i, REQUEST_OK);
         } catch (Exception e) {
        	 Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
         }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
        	ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        	((TextView)findViewById(R.id.text1)).setText(thingsYouSaid.get(0));
        }
    }

}
