package com.pablomonteserin.almacenamiento.sqllite.ej;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {
	SQLiteDatabase db;
	ListView v;
	Spinner sp;
	String txt;
	ArrayList<String> invitados;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		db = openOrCreateDatabase("invitado_db", MODE_PRIVATE,
				null);
		// La siguiente línea busca la base de datos. Si la encuentra la abre.
		// si no, la crea
		v = (ListView) findViewById(R.id.listView1);
		sp = (Spinner) findViewById(R.id.spinner1);
		consultar();
	}

	public void insertar(View view) {
		txt = ((EditText) findViewById(R.id.editText1)).getText().toString();
		db.execSQL("INSERT INTO invitado VALUES ('" + txt + "')");
		consultar();

	}

	public void borrar(View view) {
		int position = sp.getSelectedItemPosition();
		txt = sp.getItemAtPosition(position).toString();
		db.execSQL("DELETE FROM invitado WHERE nombre='" + txt + "'");
		consultar();

	}

	public void consultar() {

		Cursor c = db.rawQuery("SELECT * FROM invitado", null);
		invitados = new ArrayList<String>();
		invitados.clear();
		while(c.moveToNext()){
			invitados.add(c.getString(0));
		}

		c.close();
		ArrayAdapter<String> ap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, invitados);
		v.setAdapter(ap);
		sp.setAdapter(ap);
	

	
	}
}