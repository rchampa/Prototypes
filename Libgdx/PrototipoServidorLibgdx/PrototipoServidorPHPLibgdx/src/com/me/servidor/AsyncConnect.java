package com.me.servidor;


/*
 * CLASE ASYNCTASK
 * 
 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos
 * y obtenemos los datos podria hacerse lo mismo sin usar esto pero si el
 * tiempo de respuesta es demasiado lo que podria ocurrir si la conexion es
 * lenta o el servidor tarda en responder la aplicacion sera inestable.
 * ademas observariamos el mensaje de que la app no responde.
 */


//public class AsyncConnect extends AsyncTask<String, String, String> {
public class AsyncConnect implements Runnable {


	//private ProgressDialog progressDialog;
	//private Context context;
	private Conexion conexion;
	private String[] params;
	
	private final boolean OK_CONNECTION = true;
	private final boolean ERROR_CONNECTION = false;
	
	public AsyncConnect(String[] params, Conexion conexion){
		this.params = params;
		this.conexion = conexion;
	}

	protected void onPreExecute() {
		// para el progress dialog
		//TODO: se cambia a una screen de loading...
	}

	protected boolean doInBackground(String... params) {
		
		// enviamos y recibimos y analizamos los datos en segundo plano.
		if (conexion.checkStatus(params)) {
			return OK_CONNECTION;
		} else {
			return ERROR_CONNECTION;
		}

	}

	/*
	 * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
	 * a la sig. activity o mostramos error
	 */
	protected void onPostExecute(boolean result) {

		//TODO
		//Se oculta la screen de loading
		
		if (result==OK_CONNECTION)
			conexion.conexionEstablecida();
		else
			conexion.errorEnConexion();
	}

	@Override
	public void run() {

		onPreExecute();
		
		boolean result = doInBackground(params);
		
		onPostExecute(result);
	}

}
