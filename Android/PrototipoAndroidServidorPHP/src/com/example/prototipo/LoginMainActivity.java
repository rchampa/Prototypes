package com.example.prototipo;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.droidlogin.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginMainActivity extends Activity {
	/** Called when the activity is first created. */

	private EditText userET;
	private EditText passET;
	private Button loginBT;
	private TextView registrar;
	private HttpPostConnector post;

	private String IP_Server = "192.168.1.126";// IP DE NUESTRO PC
	// ruta en donde estan nuestros archivos
	private String URL_connect = "http://" + IP_Server + "/prototipo_android/acces.php";
		

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		post = new HttpPostConnector();

		userET = (EditText) findViewById(R.id.edusuario);
		passET = (EditText) findViewById(R.id.edpassword);
		loginBT = (Button) findViewById(R.id.Blogin);
		registrar = (TextView) findViewById(R.id.link_to_register);

		// Login button action
		loginBT.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Extreamos datos de los EditText
				String usuario = userET.getText().toString();
				String passw = passET.getText().toString();

				// verificamos si estan en blanco
				if (checklogindata(usuario, passw) == true) {

					// si pasamos esa validacion ejecutamos el asynctask pasando
					// el usuario y clave como parametros

					new AsyncLogin().execute(usuario, passw);

				} else {
					// si detecto un error en la primera validacion vibrar y
					// mostrar un Toast con un mensaje de error.
					err_login();
				}

			}
		});

		registrar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Abre el navegador al formulario adduser.html
				String url = "http://" + IP_Server + "/droidlogin/adduser.html";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

	}

	// vibra y muestra un Toast
	public void err_login() {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
		Toast toast1 = Toast.makeText(getApplicationContext(),
				"Error:Nombre de usuario o password incorrectos",
				Toast.LENGTH_SHORT);
		toast1.show();
	}

	/*
	 * Valida el estado del logueo solamente necesita como parametros el usuario
	 * y passw
	 */
	public boolean loginstatus(String username, String password) {
		int logstatus = -1;

		/*
		 * Creamos un ArrayList del tipo nombre valor para agregar los datos
		 * recibidos por los parametros anteriores y enviarlo mediante POST a
		 * nuestro sistema para relizar la validacion
		 */
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("usuario", username));
		postParametersToSend.add(new BasicNameValuePair("password", password));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, URL_connect);

		/*
		 * como estamos trabajando de manera local el ida y vuelta sera casi
		 * inmediato para darle un poco realismo decimos que el proceso se pare
		 * por unos segundos para poder observar el progressdialog la podemos
		 * eliminar si queremos
		 */
		SystemClock.sleep(1500);

		// si lo que obtuvimos no es null
		if (jdata != null && jdata.length() > 0) {

			JSONObject json_data; // creamos un objeto JSON
			try {
				json_data = jdata.getJSONObject(0); // leemos el primer segmento
													// en nuestro caso el unico
				logstatus = json_data.getInt("estado");// accedemos al valor
				Log.e("estado", "estado= " + logstatus);// muestro por
																// log que
																// obtuvimos
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// validamos el valor obtenido
			if (logstatus == 0) {// [{"logstatus":"0"}]
				Log.e("loginstatus ", "invalido");
				return false;
			} else {// [{"logstatus":"1"}]
				Log.e("loginstatus ", "valido");
				return true;
			}

		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return false;
		}

	}

	// validamos si no hay ningun campo en blanco
	public boolean checklogindata(String username, String password) {

		if (username.equals("") || password.equals("")) {
			Log.e("Login ui", "checklogindata user or pass error");
			return false;

		} else {

			return true;
		}

	}

	/*
	 * CLASE ASYNCTASK
	 * 
	 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos
	 * y obtenemos los datos podria hacerse lo mismo sin usar esto pero si el
	 * tiempo de respuesta es demasiado lo que podria ocurrir si la conexion es
	 * lenta o el servidor tarda en responder la aplicacion sera inestable.
	 * ademas observariamos el mensaje de que la app no responde.
	 */

	private class AsyncLogin extends AsyncTask<String, String, String> {

		private String user, pass;
		private ProgressDialog progressDialog;

		protected void onPreExecute() {
			// para el progress dialog
			progressDialog = new ProgressDialog(LoginMainActivity.this);
			progressDialog.setMessage("Autenticando....");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		protected String doInBackground(String... params) {
			// obtnemos usr y pass
			user = params[0];
			pass = params[1];

			// enviamos y recibimos y analizamos los datos en segundo plano.
			if (loginstatus(user, pass)) {
				return "ok"; // login valido
			} else {
				return "err"; // login invalido
			}

		}

		/*
		 * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
		 * a la sig. activity o mostramos error
		 */
		protected void onPostExecute(String result) {

			progressDialog.dismiss();// ocultamos progess dialog.
			Log.e("onPostExecute=", "" + result);

			if (result.equals("ok")) {

				Intent i = new Intent(LoginMainActivity.this,
						BienvenidaActivity.class);
				i.putExtra("user", user);
				startActivity(i);

			} else {
				err_login();
			}

		}

	}

}