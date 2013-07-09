package com.example.ejemplograficos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MiVista extends View {
	 
	   public MiVista(Context context, AttributeSet attrs) {
	          super(context, attrs);
	          //Inicializa la vista
	          //OjO: Aún no se conocen sus dimensiones
	   }
	 
	   @Override 
	   protected void onSizeChanged(int ancho, int alto,
	                 int ancho_anterior, int alto_anterior){
	          //Te informan del ancho y el alto
	   }
	 
	   @Override 
	   protected void onDraw(Canvas canvas) {
	          //Dibuja aquí la vista
	   }
}
