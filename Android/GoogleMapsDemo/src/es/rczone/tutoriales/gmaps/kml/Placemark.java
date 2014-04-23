package es.rczone.tutoriales.gmaps.kml;

import java.util.ArrayList;

public class Placemark {

	private String title;
	private String description;
	private ArrayList<String> coordinates;
	private String address;
	
	public Placemark(){
		this.coordinates = new ArrayList<String>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getCoordinates() {
		return coordinates;
	}

//	public void setCoordinates(String coordinates) {
//		this.coordinates = coordinates;
//	}
	
	public void addCoordinates(String coordinates){
		this.coordinates.add(coordinates);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
