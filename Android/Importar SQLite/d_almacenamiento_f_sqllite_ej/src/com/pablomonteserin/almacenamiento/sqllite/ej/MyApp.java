package com.pablomonteserin.almacenamiento.sqllite.ej;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class MyApp extends Application {
	
	private final String PREFERENCES_FILE = "Ajustes";
	private boolean hayActualizacion;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		SharedPreferences prefs = this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    	
    	boolean flag = prefs.getBoolean("flag",false);
    	int version = prefs.getInt("version", -1);
    	
    	PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int posible_nueva_version = pInfo.versionCode;
			
			if(version<posible_nueva_version){
				hayActualizacion = true;
				prefs = this.getApplicationContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
		        SharedPreferences.Editor editor = prefs.edit();
		        editor.putInt("version", posible_nueva_version);
		        editor.commit();
			}
			else{
				hayActualizacion = false;
			}
			
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	
    	if(!flag || hayActualizacion){
		
			prefs = this.getApplicationContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putBoolean("flag", true);
	        editor.commit();
			
	        //Cargar base de datos...
	        
			File file = this.getDatabasePath("invitado_db");
			file.getParentFile().mkdirs();// Esto crea todas las carpetas para que esta ruta exista. Esta es la ruta de la base de datos.
			Log.d("paso1", "copiamos la base de datos si no existía antes");
			OutputStream os = null;
			InputStream is = null;
			try {
				is = this.getResources().openRawResource(R.raw.invitado_db);
				os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				os.flush();
			} catch (Throwable t) {//e.printStackTrace();
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (IOException e) {//e.printStackTrace();
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {//e.printStackTrace();
					}
				}
			}
    	}
	}
}