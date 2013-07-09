package com.me.servidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;

public class Registro implements Conexion {
	
	private HttpPostConnector post;
	private String nickname	=	"Xavi";
	private String password	=	"Xavi";
	private String name		=	"Xavier"; 
	private String surname	=	"Xavier";
	private String age		= 	"25";
	
	public Registro(){
		
		post = new HttpPostConnector();

		String[] params = {nickname,password,name,surname,age};
		
		if(validarDatos(params))
			Gdx.app.postRunnable(new AsyncConnect(params, this));
		
	}

	@Override
	public boolean checkStatus(String... params) {
		int logstatus = -1;
//		String nickname = params[0];
//		String password = params[1];
//		String name = params[2];
//		String surname = params[3];
//		String age = params[3];

		/*
		 * Creamos un ArrayList del tipo nombre valor para agregar los datos
		 * recibidos por los parametros anteriores y enviarlo mediante POST a
		 * nuestro sistema para relizar la validacion
		 */
		ArrayList<NameValuePair> postParametersToSend = new ArrayList<NameValuePair>();

		postParametersToSend.add(new BasicNameValuePair("nickname", nickname));
		postParametersToSend.add(new BasicNameValuePair("password", password));
		postParametersToSend.add(new BasicNameValuePair("name", name));
		postParametersToSend.add(new BasicNameValuePair("surname", surname));
		postParametersToSend.add(new BasicNameValuePair("age", age));

		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postParametersToSend, HttpPostConnector.URL_CONNECT_REGISTRO);


		// si lo que obtuvimos no es null, es decir, hay respuesta válida
		if (jdata != null && jdata.length() > 0) {

			JSONObject json_data; // creamos un objeto JSON
			try {
				json_data = jdata.getJSONObject(0); // leemos el primer segmento
													// en nuestro caso el unico
				logstatus = json_data.getInt("estado");// accedemos al valor
			
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// validamos el valor obtenido
			if (logstatus == 1) {// [{"estado":"1"}]
				
				return false;
			} else {// [{"estado":"0"}]
				return true;
			}

		} else { // json obtenido invalido verificar parte WEB.
			
			return false;
		}
	}

	@Override
	public boolean validarDatos(String... params) {
		// TODO validar... meh
		return true;
	}

	@Override
	public void conexionEstablecida() {
		
		//TODO Para la version de ANDROID usar SI o SI los toast
		System.out.println("Registro con exito.");
	}

	@Override
	public void errorEnConexion() {
		//TODO Para la version de ANDROID usar SI o SI los toast
		System.out.println(post.getTrace());
		System.out.println("No se ha podido registrar.");
	}

}
