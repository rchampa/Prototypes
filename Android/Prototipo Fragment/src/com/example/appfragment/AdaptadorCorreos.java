package com.example.appfragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorCorreos extends ArrayAdapter<Correo> {
	 
    Activity context;
    Correo[] datos;

    AdaptadorCorreos(Fragment context, Correo[] datos) {
        super(context.getActivity(), R.layout.listitem_correo, datos);
        this.context = context.getActivity();
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    View item = inflater.inflate(R.layout.listitem_correo, null);
	
	    TextView lblDe = (TextView)item.findViewById(R.id.LblDe);
	    lblDe.setText(datos[position].getDe());
	
	    TextView lblAsunto = (TextView)item.findViewById(R.id.LblAsunto);
	    lblAsunto.setText(datos[position].getAsunto());
	
	    return(item);
	}
}
