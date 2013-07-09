package com.example.prototipomapas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends android.support.v4.app.FragmentActivity {
	
	private GoogleMap mapa = null;
	private int vista = 0;
	
	private static final LatLng ROMA = new LatLng(42.093230818037,11.7971813678741);
	private static final LatLng MADRID = new LatLng(40.417325, -3.683081);
	private static final LatLng BARCELONA = new LatLng(41.533254,2.15332);
    private LocationManager locationManager;
    private String provider;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
	}

	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	      super.onCreateOptionsMenu(menu);
		  getMenuInflater().inflate(R.menu.main, menu);
		  return true;
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 	case R.id.menu_legalnotices:
				String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
				getApplicationContext());
				AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
				LicenseDialog.setTitle("Legal Notices");
				LicenseDialog.setMessage(LicenseInfo);
				LicenseDialog.show();
				return true;
				
		 	case R.id.menu_vista:
				alternarVista();
				break;
				
			case R.id.menu_mover:
				//Centramos el mapa en España
				CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(new LatLng(40.41, -3.69));
				mapa.moveCamera(camUpd1);
				break;
			case R.id.menu_animar:
				//Centramos el mapa en España y con nivel de zoom 5
				CameraUpdate camUpd2 = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5F);
				mapa.animateCamera(camUpd2);
				break;
			case R.id.menu_3d:
				LatLng madrid = new LatLng(40.417325, -3.683081);
				CameraPosition camPos = new CameraPosition.Builder()
					    .target(madrid)   //Centramos el mapa en Madrid
					    .zoom(19)         //Establecemos el zoom en 19
					    .bearing(45)      //Establecemos la orientación con el noreste arriba
					    .tilt(70)         //Bajamos el punto de vista de la cámara 70 grados
					    .build();
				
				CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
				
				mapa.animateCamera(camUpd3);
				break;
			case R.id.menu_posicion:
				CameraPosition camPos2 = mapa.getCameraPosition();
				LatLng pos = camPos2.target;
				Toast.makeText(MainActivity.this, 
						"Lat: " + pos.latitude + " - Lng: " + pos.longitude, 
						Toast.LENGTH_LONG).show();
				break;
				
			case R.id.menu_posicionActual:
				Toast.makeText(MainActivity.this, "Buscando posición actual...", Toast.LENGTH_LONG).show();
				iniciarServicioGPS();
				break;
				
			case R.id.menu_MadridABarcelona:
				String url = makeURL(MADRID.latitude, MADRID.longitude, BARCELONA.latitude, BARCELONA.longitude);
				new connectAsyncTask(url).execute();
				break;
			
		 }
		 return super.onOptionsItemSelected(item);
	 }

	private void alternarVista(){
		vista = (vista + 1) % 4;
		
		switch(vista)
		{
			case 0:
				mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case 2:
				mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 3:
				mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
		}
	}
	
	private void insertarMarcador(double lat, double lng){
	    mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
	}

	private void iniciarServicioGPS(){
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	    boolean enabledGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    boolean enabledWiFi = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to 
        // go to the settings
        if (!enabledGPS) {
            Toast.makeText(this, "Se recomienda activar el GPS", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        provider = locationManager.getBestProvider(criteria, false);
        //Location location = locationManager.getLastKnownLocation(provider);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        
        // Initialize the location fields
        if (location == null) {
            Toast.makeText(this, "No se ha conseguido localizar el dispositivo.",Toast.LENGTH_SHORT).show();
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
	        insertarMarcador(location.getLatitude(), location.getLongitude());
			CameraPosition camPos = new CameraPosition.Builder()
				    .target(userLocation)   //Centramos el mapa en la posicion actual del dispositivo
				    .zoom(16)         //Establecemos el zoom en 19
				    .bearing(45)      //Establecemos la orientación con el noreste arriba
				    .tilt(0)         //Bajamos el punto de vista de la cámara 70 grados
				    .build();
			
			CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
			mapa.animateCamera(camUpd3);
        } 
        else{
	        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
	        insertarMarcador(location.getLatitude(), location.getLongitude());
			CameraPosition camPos = new CameraPosition.Builder()
				    .target(userLocation)   //Centramos el mapa en la posicion actual del dispositivo
				    .zoom(16)         //Establecemos el zoom en 19
				    .bearing(45)      //Establecemos la orientación con el noreste arriba
				    .tilt(0)         //Bajamos el punto de vista de la cámara 70 grados
				    .build();
			
			CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
			mapa.animateCamera(camUpd3);
        }
        
	}
	
	public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
	}
	
	public void drawPath(String  result) {

	    try {
	            //Tranform the string into a json object
	           final JSONObject json = new JSONObject(result);
	           JSONArray routeArray = json.getJSONArray("routes");
	           JSONObject routes = routeArray.getJSONObject(0);
	           JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
	           String encodedString = overviewPolylines.getString("points");
	           List<LatLng> list = decodePoly(encodedString);

	           for(int z = 0; z<list.size()-1;z++){
	                LatLng src= list.get(z);
	                LatLng dest= list.get(z+1);
	                Polyline line = mapa.addPolyline(new PolylineOptions()
	                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
	                .width(2)
	                .color(Color.BLUE).geodesic(true));
	            }

	    } 
	    catch (JSONException e) {

	    }
	} 
	
	private List<LatLng> decodePoly(String encoded) {

	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        LatLng p = new LatLng( (((double) lat / 1E5)),
	                 (((double) lng / 1E5) ));
	        poly.add(p);
	    }

	    return poly;
	}
	
	private class connectAsyncTask extends AsyncTask<Void, Void, String>{
	    private ProgressDialog progressDialog;
	    String url;
	    connectAsyncTask(String urlPass){
	        url = urlPass;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(MainActivity.this);
	        progressDialog.setMessage("Calculando ruta, por favor espere...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.show();
	    }
	    @Override
	    protected String doInBackground(Void... params) {
	        JSONParser jParser = new JSONParser();
	        String json = jParser.getJSONFromUrl(url);
	        return json;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);   
	        progressDialog.hide();        
	        if(result!=null){
	            drawPath(result);
	        }
	    }
	}
	
}
