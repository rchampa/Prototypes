package es.rczone.tutoriales.gmaps.radius;

import java.util.TimerTask;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;

public class RadiusControlTask extends TimerTask{

	private Activity context;
	private GoogleMap map;
	private Circle currentCircle;
	
	public RadiusControlTask(Activity context, GoogleMap map){
		this.context = context;
		this.map = map;
	}
	
	
	
	@Override
	public void run() {

		
		
	}
	
	public Circle getCircle(){
		return currentCircle;
	}

}
