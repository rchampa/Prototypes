package com.example.pantallatactil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView entrada = (TextView)findViewById(R.id.TextViewEntrada);
		entrada.setOnTouchListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View vista, MotionEvent evento) {
		TextView salida = (TextView) findViewById(R.id.TextViewSalida);
        salida.append("Pressure: "+evento.getPressure()+"\n" );
        salida.append("Size: "+evento.getSize()+"\n" );
		return true;
	}

}
