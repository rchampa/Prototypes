package com.example.prototipocargarficheros;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button cargarB;
	TextView contenidoTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		configurarViews();		
	}

	private void configurarViews(){
		cargarB = (Button)findViewById(R.id.cargarBT);
		contenidoTV = (TextView)findViewById(R.id.contenidoTV);
		
		cargarB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contenidoTV.setText("Contenido...");
				readTxtFileFromRaw(MainActivity.this, R.raw.usuarios);
			}
		});
		
	}
	
	private void readTxtFileFromRaw(Context ctx,int res_id) {

		int bufferSize = 2048;// 8192 is buffer size max
	    InputStream is = ctx.getResources().openRawResource(res_id);
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr, bufferSize); 
	  

	    try {
	        String test;
	        while (true) {
	            test = br.readLine();
	            if (test == null)
	                break;
	            else{
	            	String[] atributos = test.split(";");
	            	String nombre = atributos[0] + " " + atributos[1];
	            	String posicion = atributos[2] + " " + atributos[3];
	            	/*String sexo = atributos[4].equalsIgnoreCase("M") ? "Masculino" : "Femenino";
	            	String numero = atributos[5];*/
	            	String correo = atributos[6];
	            	String tipo = atributos[7];
	            	String contenido = "Nombre: "+nombre+", Posicion: "+posicion+", Correo: "+correo+", Tipo:"+tipo;
	            	contenidoTV.append("\n"+contenido);
	            }
	        }
	        isr.close();
	        is.close();
	        br.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	
	
}
