package com.example.appfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetalleActivity extends FragmentActivity {

	public static final String DETALLES = "DETALLES";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle);

		FragmentDetalle detalle = 
				(FragmentDetalle)getSupportFragmentManager()
					.findFragmentById(R.id.FrgDetalle);

		detalle.mostrarDetalle(getIntent().getStringExtra(DETALLES));
	}
}
