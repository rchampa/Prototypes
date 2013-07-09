package org.example.asteroides;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Localizacion extends Activity {
	
	private static final int DIALOGO_ALERTA = 1;
	private static final int DIALOGO_CONFIRMACION = 2;
	private static final int DIALOGO_SELECCION = 3;
	
	private Button bAcercaDe;
	private Button bConfig;
	private Button bPuntuaciones;
	private Button bSalir;
	
	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*
		bAcercaDe =(Button) findViewById(R.id.acercaDeB);
		bConfig =(Button) findViewById(R.id.configurarB);
		bPuntuaciones =(Button) findViewById(R.id.puntuacionesB);
		bSalir =(Button) findViewById(R.id.salirB);

		
        bAcercaDe.setOnClickListener(new OnClickListener() {

	          public void onClick(View view) {
	                lanzarAcercaDe(view);
	          }

        });
        
        bConfig.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
            	lanzarPreferencias(view);
            }

        });
        
        bPuntuaciones.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
            	lanzarPuntuaciones(view);
            }

        });
        
        bSalir.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
            	salir(view);
            }

        });
        */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	       super.onCreateOptionsMenu(menu);
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.menu, menu);
	       return true; /** true -> el menú ya está visible */
	       //return false; /** true -> el menú no está visible */
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		
        switch (item.getItemId()) {
        	case R.id.acercaDe:
               lanzarAcercaDe(null);
               break;
        	case R.id.config:
                lanzarPreferencias(null);
                break;
        }
        return true; /** true -> consumimos el item, no se propaga*/
	}
	
	public void lanzarAcercaDe(View view){
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}
	
	public void lanzarPreferencias(View view){
		Intent i = new Intent(this, Preferencias.class);
		startActivity(i);
	}
	
	public void lanzarPuntuaciones(View view) {
		Intent i = new Intent(this, Puntuaciones.class);
		startActivity(i);
	}
	
	public void lanzarJuego(View view) {
		Intent i = new Intent(this, Juego.class);
		startActivity(i);
	}
	
	public void salir(View view){

		showDialog(DIALOGO_CONFIRMACION);

	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialogo = null;

    	switch(id)
    	{
    		case DIALOGO_ALERTA:
    			dialogo = crearDialogoAlerta();
    			break;
    		case DIALOGO_CONFIRMACION:
    			dialogo = crearDialogoConfirmacion();
    			break;
    		case DIALOGO_SELECCION:
    			dialogo = crearDialogoSeleccion();
    			break;
    		default:
    			dialogo = null;
    			break;
    	}
    
    	return dialogo;
    }
    
    private Dialog crearDialogoAlerta()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle("Informacion");
    	builder.setMessage("Esto es un mensaje de alerta.");
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoConfirmacion()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle("Confirmacion");
    	builder.setMessage("¿Estás seguro que deseas salir de asteroides?");
    	builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i("Dialogos", "Confirmacion Cancelada.");
				dialog.cancel();
			}
		});
    	builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i("Dialogos", "Confirmacion Aceptada.");
				finish();
			}
		});
    	
    	
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoSeleccion()
    {
    	final String[] items = {"Español", "Inglés", "Francés"};
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle("Selección");
    	builder.setItems(items, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	        Log.i("Dialogos", "Opción elegida: " + items[item]);
    	    }
    	});
    	
    	//Dialogo de selección simple
//    	builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
//			public void onClick(DialogInterface dialog, int item, boolean isChecked) {
//				Log.i("Dialogos", "Opción elegida: " + items[item]);
//			}
//		});
    	
    	//Diálogo de selección múltiple
//    	builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//    	    public void onClick(DialogInterface dialog, int item) {
//    	        Log.i("Dialogos", "Opción elegida: " + items[item]);
//    	    }
//    	});
    	
    	return builder.create();
    }

}
