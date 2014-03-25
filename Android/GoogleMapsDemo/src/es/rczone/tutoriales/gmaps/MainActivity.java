package es.rczone.tutoriales.gmaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends android.support.v4.app.FragmentActivity {
    
	private List<Polyline> listaPolylineas;
    private GoogleMap mapa = null;
    private int vista = 0;
    private String location;
    
    private Circle circulo_radio;
    private Location pos_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            
            mapa = ((SupportMapFragment) getSupportFragmentManager()
                               .findFragmentById(R.id.map)).getMap();
            
            listaPolylineas = new ArrayList<Polyline>();
            
            mapa.setMyLocationEnabled(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.activity_main, menu);
            return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {        
            switch(item.getItemId())
            {
		            case R.id.menu_legalnotices:
						String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
						AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(this);
						LicenseDialog.setTitle("Legal Notices");
						LicenseDialog.setMessage(LicenseInfo);
						LicenseDialog.show();
						break;
                    case R.id.menu_vista:
                            alternarVista();
                            break;
                    case R.id.menu_mover:                  
                            //Centramos el mapa en Espa�a
                            CameraUpdate camUpd1 = 
                                    CameraUpdateFactory.newLatLng(new LatLng(40.41, -3.69));
                            mapa.moveCamera(camUpd1);
                            break;
                    case R.id.menu_animar:
                            //Centramos el mapa en Espa�a y con nivel de zoom 5
                            CameraUpdate camUpd2 = 
                                    CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5F);
                            mapa.animateCamera(camUpd2);
                            break;
                    case R.id.menu_3d:
                            LatLng madrid = new LatLng(40.417325, -3.683081);
                            CameraPosition camPos = new CameraPosition.Builder()
                                        .target(madrid)   //Centramos el mapa en Madrid
                                        .zoom(19)         //Establecemos el zoom en 19
                                        .bearing(45)      //Establecemos la orientaci�n con el noreste arriba
                                        .tilt(70)         //Bajamos el punto de vista de la c�mara 70 grados
                                        .build();
                            
                            CameraUpdate camUpd3 = 
                                            CameraUpdateFactory.newCameraPosition(camPos);
                            
                            mapa.animateCamera(camUpd3);
                            break;
                    case R.id.menu_posicion:
//                            CameraPosition camPos2 = mapa.getCameraPosition();
//                            LatLng pos = camPos2.target;
//                            Toast.makeText(MainActivity.this, 
//                                            "Lat: " + pos.latitude + " - Lng: " + pos.longitude, 
//                                            Toast.LENGTH_LONG).show();
                    		this.pos_actual = mapa.getMyLocation();
                    		
                            break;
                    case R.id.Madrid_Barcelona:
                    	LatLng Madrid = new LatLng(40.416690900000000000, -3.700345400000060000);
                    	LatLng Barcelona = new LatLng(41.387917000000000000, 2.169918700000039300);
                    	String url = makeURL(Madrid.latitude, Madrid.longitude, Barcelona.latitude, Barcelona.longitude);
                		new connectAsyncTask(url).execute();
                		//insertarMarcador(Barcelona.latitude, Barcelona.longitude, "Barcelona", , BitmapDescriptorFactory.fromResource(recursoIcon));
                		break;
                		
                    case R.id.menu_borrar_polylineas:
        				borrarPolylineas();				
        				break;
        				
                    case R.id.menu_direccion:
        				String direccion = "Fernando el catolico 48, Madrid";	
        				if(direccion!=null){
        					try {
	        					Geocoder geocoder = new Geocoder(this);  
	        					List<Address> addresses;
	        					addresses = geocoder.getFromLocationName(direccion, 1);
	        					if(addresses.size() > 0) {
	        					    double latitude= addresses.get(0).getLatitude();
	        					    double longitude= addresses.get(0).getLongitude();
	        					    Toast.makeText(this, "Latitud="+latitude+" Longitud="+longitude, Toast.LENGTH_SHORT).show();
	        					}
        					} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
        				}
        				break;
        				
                    case R.id.menu_mostrar_radio:
                    	
                    	if(pos_actual==null){
                    		Toast.makeText(this, "Primero debes consultar tu posición", Toast.LENGTH_SHORT).show();
                    		super.onOptionsItemSelected(item);
                    	}
                    		
                    	
                    	if(circulo_radio==null){
                    		LatLng pos = new LatLng(pos_actual.getLatitude(), pos_actual.getLongitude());
	                    	CircleOptions circleOptions = new CircleOptions();
	                    	circleOptions.center(pos);
	                        circleOptions.radius(500); // In meters
	                    	circleOptions.strokeColor(Color.BLUE);
	                    	circleOptions.fillColor(Color.argb(100, 0, 0, 255));
	
		                    circulo_radio = mapa.addCircle(circleOptions);
                    	}
                    	else{
                    		LatLng pos = new LatLng(pos_actual.getLatitude(), pos_actual.getLongitude());
                    		circulo_radio.setCenter(pos);
                    		circulo_radio.setRadius(200);
                    	}
	                    break;

            }
            
            return super.onOptionsItemSelected(item);
    }
    
    
    private boolean estaEnElRadio(Location mi_posicion, Location otra_posicion, double radio) {
    	
        double distancia = mi_posicion.distanceTo(otra_posicion);
        // if less than 10 metres
        if (distancia < radio) {
            return true;         
        } 
        else{
            return false;
        }
    }
    
    
    private void alternarVista()
    {
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
    
    private void borrarPolylineas(){
		 for(Polyline linea: listaPolylineas){
			 //linea.setVisible(false);
			 linea.remove();
		 }
	}
    
    private  String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=walking&alternatives=true");
        return urlString.toString();
	}
	
	private void drawPath(String  result) {

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
	                .color(Color.BLUE)
	                .geodesic(true)
	                .width(6f)
	                );
	                listaPolylineas.add(line);
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
	        progressDialog.setCancelable(false);
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

