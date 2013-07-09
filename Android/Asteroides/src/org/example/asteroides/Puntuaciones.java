package org.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Puntuaciones extends ListActivity {
    
	@Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);
        /*setListAdapter(new ArrayAdapter<String>(
        		this, 
        		android.R.layout.simple_list_item_1,
        		Localizacion.almacen.listaPuntuaciones(10)));
        */
        /*setListAdapter(new ArrayAdapter<String>(
                this,
                R.layout.elemento_lista,
                R.id.titulo,
                Localizacion.almacen.listaPuntuaciones(10)));
        */
        setListAdapter(new MiAdaptador(this, Localizacion.almacen.listaPuntuaciones(10)));
    }
	
	@Override 
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		Object o = getListAdapter().getItem(position);
		Toast.makeText(this, "Selección: " + Integer.toString(position)+  " - " + o.toString(),Toast.LENGTH_SHORT).show();
	}
}
