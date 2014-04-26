package es.ric.speaker;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnInitListener {

	private TextToSpeech tts;
	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tts = new TextToSpeech(this, this);
		
		button = (Button) findViewById(R.id.button1);
		button.setEnabled(false);
	}

	/**
	 * With our text-to-speech engine loaded, 
	 * we are prepared to handle the user button clicks. 
	 * @param v
	 */
	public void onClick(View v) {
		if (tts!=null) {
			String text = ((EditText)findViewById(R.id.editText1)).getText().toString();
			if (text!=null) {
				if (!tts.isSpeaking()) {
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
		}
	}

	/**
	 * Let's look at the on initialization callback. 
	 * This mechanism lets us know our text-to-speech engine 
	 * is ready to go and is the ideal place to set the language.
	 */
	@Override
	public void onInit(int code) {
		if (code==TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.getDefault());
			button.setEnabled(true);
		} else {
			tts = null;
			Toast.makeText(this, "Failed to initialize TTS engine.", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * We need to make sure we clean up the text-to-speech engine when our 
	 * application shuts down. Failing to do so can result in a crash if the 
	 * user suddenly switches applications while the engine is in the middle 
	 * of a sentence.
	 */
	@Override
	protected void onDestroy() {
		if (tts!=null) {
			tts.stop();
            tts.shutdown();
		}
		super.onDestroy();
	}

}
