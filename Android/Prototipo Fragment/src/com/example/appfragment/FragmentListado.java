package com.example.appfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
 
public class FragmentListado extends Fragment {
 
 
    private ListView lstListado;
    private CorreosListener listener;
    private Correo[] datos =
            new Correo[]{
                new Correo("Persona 1", "Asunto del correo 1", "Texto del correo 1"),
                new Correo("Persona 2", "Asunto del correo 2", "Texto del correo 2"),
                new Correo("Persona 3", "Asunto del correo 3", "Texto del correo 3"),
                new Correo("Persona 4", "Asunto del correo 4", "Texto del correo 4"),
                new Correo("Persona 5", "Asunto del correo 5", "Texto del correo 5")};
 
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.fragment_listado, container, false);
    }
 
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
 
        lstListado = (ListView)getView().findViewById(R.id.LstListado);
 
        lstListado.setAdapter(new AdaptadorCorreos(this, datos));
        lstListado.setOnItemClickListener(new OnItemClickListener() {
			
        	@Override
			public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
				if (listener!=null) {
					listener.onCorreoSeleccionado((Correo)lstListado.getAdapter().getItem(pos));
				}
			}

			
		});
    }
    
    public void setCorreosListener(CorreosListener listener) {
        this.listener=listener;
    }
 
}