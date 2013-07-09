package com.example.prototipotreeview;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;

public class MainActivity extends Activity {

	private ExpandableListAdapter adpt;
	private ExpandableListView listView;
	private List<Nodo> lista;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ExpandableListView)findViewById(R.id.expList);
        lista = new ArrayList<Nodo>();
        
        //load model here
        loadSampleData();        
        adpt = new ExpandableListAdapter(this, lista);
        listView.setAdapter(adpt);
    }
    
    private void loadSampleData(){
   
    	//Clase 1
    	Nodo nodo = new Nodo();
    	nodo.setClase("Clase1");
   
    	List<NodoInfo> hijos = new ArrayList<NodoInfo>();
    	
    	NodoInfo hijo = new NodoInfo();
    	
    	hijo.setNombre("Subclase11");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase12");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase13");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase14");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase15");
    	hijos.add(hijo);
    	
    	nodo.setSubclases(hijos);
    	lista.add(nodo);
    	
    	
    	//Clase 2
    	nodo = new Nodo();
    	nodo.setClase("Clase 2");
    	
    	hijos = new ArrayList<NodoInfo>();
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase21");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase22");
    	hijos.add(hijo);
    	
    	nodo.setSubclases(hijos);
    	lista.add(nodo);
    	
    	
    	//Clase 3
    	nodo = new Nodo();
    	nodo.setClase("Clase 3");
    	
    	hijos = new ArrayList<NodoInfo>();
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase31");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase32");
    	hijos.add(hijo);
    	
    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase33");
    	hijos.add(hijo);

    	hijo = new NodoInfo();
    	hijo.setNombre("Subclase34");
    	hijos.add(hijo);
    	
    	nodo.setSubclases(hijos);
    	lista.add(nodo);
    	    	    	    	  
    }

}
