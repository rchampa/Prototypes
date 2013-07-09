package com.example.prototipotreeview;

import java.util.List;

public class Nodo {
	private String clase;
	private List<NodoInfo> subclases;
	
	public void setClase(String clase){
		this.clase = clase;
	}
	
	public void setSubclases(List<NodoInfo> subclases){
		this.subclases = subclases;
	}
	
	public String getClase(){
		return this.clase;
	}
	
	public List<NodoInfo> getSubclases(){
		return this.subclases;
	}
}
