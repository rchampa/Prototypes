package com.example.appfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements CorreosListener{

	//@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        FragmentListado frgListado =(FragmentListado)getSupportFragmentManager().findFragmentById(R.id.FrgListado);
        frgListado.setCorreosListener(this);
        /*
        Configuration config = getResources().getConfiguration();
        int x = config.smallestScreenWidthDp;
        int d = 9;
        */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onCorreoSeleccionado(Correo c) {
		
		FragmentDetalle frgDetalle = (FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle);
		
		boolean hayDetalle = frgDetalle != null;
	 
        if(hayDetalle) {
        	frgDetalle.mostrarDetalle(c.getAsunto());
        }
        else {
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra(DetalleActivity.DETALLES, c.getTexto());
            startActivity(i);
        }
		
		
		
	}

}
