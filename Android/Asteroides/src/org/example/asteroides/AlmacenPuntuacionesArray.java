package org.example.asteroides;

import java.util.Vector;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones{

    private Vector<String> puntuaciones;
    private Vector<String> mejoresPuntuaciones;

	public AlmacenPuntuacionesArray() {
		puntuaciones= new Vector<String>();
		puntuaciones.add("123000 Pepito Domingez");
		puntuaciones.add("111000 Pedro Martinez");
		puntuaciones.add("011000 Paco Pérez");
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
         puntuaciones.add(0, puntos + " "+ nombre);
    }

    @Override 
    public Vector<String> listaPuntuaciones(int cantidad) {
    	if(mejoresPuntuaciones==null)
    		mejoresPuntuaciones = new Vector<String>();
    	else
    		mejoresPuntuaciones.clear();
		
		for(int i=0; i<cantidad && i<puntuaciones.size(); i++){
			mejoresPuntuaciones.add(puntuaciones.get(i));
		}
		
		return  mejoresPuntuaciones;
    }

}
