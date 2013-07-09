package com.example.ejemplograficos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

public class EjemploView3 extends View {

	Path trazo;
	Paint pincel;
	private Drawable miImagenBM;
	private ShapeDrawable miImagen;

	public EjemploView3(Context context) {
		super(context);

		Resources res = context.getResources();
		miImagenBM = res.getDrawable(R.drawable.mi_imagen);
		miImagenBM.setBounds(30, 30, 200, 200);
		
		miImagen  = new ShapeDrawable(new OvalShape());
		miImagen.getPaint().setColor(0xff0000ff);
		miImagen.setBounds(10, 10, 310, 60);
	}
	
	@Override 
	protected void onSizeChanged(int ancho, int alto,
            int ancho_anterior, int alto_anterior){
     //Te informan del ancho y el alto
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Dibujar aquí

		/*
		 * Paint pincel = new Paint(); //pincel.setColor(Color.BLUE);
		 * pincel.setStrokeWidth(8); pincel.setStyle(Style.STROKE);
		 * pincel.setColor(getResources().getColor(R.color.color_circulo));
		 * canvas.drawCircle(150, 150, 100, pincel);
		 */

		/*
		 * if(trazo==null) trazo = new Path(); //trazo.addCircle(150, 150,
		 * 100, Direction.CCW); trazo.addCircle(150, 150, 100,
		 * Direction.CW); canvas.drawColor(Color.WHITE);
		 * 
		 * if(pincel==null) pincel = new Paint();
		 * pincel.setColor(Color.BLUE); pincel.setStrokeWidth(8);
		 * pincel.setStyle(Style.STROKE);
		 * 
		 * canvas.drawPath(trazo, pincel); pincel.setStrokeWidth(1);
		 * pincel.setStyle(Style.FILL); pincel.setTextSize(20);
		 * pincel.setTypeface(Typeface.SANS_SERIF); //canvas.drawTextOnPath(
		 * "Desarrollo de aplicaciones para móviles con Android", trazo, 10,
		 * 40, pincel); float desplazamientoHorizontal = 0f; float
		 * desplazamientoVertical = -20f; canvas.drawTextOnPath(
		 * "Desarrollo de aplicaciones para móviles con Android", trazo,
		 * desplazamientoHorizontal, desplazamientoVertical, pincel);
		 */

		if (trazo == null)
			trazo = new Path();
		// trazo.addCircle(150, 150, 100, Direction.CCW);
		trazo.moveTo(50, 100);
		trazo.cubicTo(60, 85, 150, 65, 200, 110);
		trazo.lineTo(300, 200);
		canvas.drawColor(Color.WHITE);

		if (pincel == null)
			pincel = new Paint();
		pincel.setColor(Color.BLUE);
		pincel.setStrokeWidth(8);
		pincel.setStyle(Style.STROKE);

		canvas.drawPath(trazo, pincel);
		pincel.setStrokeWidth(1);
		pincel.setStyle(Style.FILL);
		pincel.setTextSize(20);

		pincel.setTypeface(Typeface.SANS_SERIF);
		// canvas.drawTextOnPath("Desarrollo de aplicaciones para móviles con Android",
		// trazo, 10, 40, pincel);
		float desplazamientoHorizontal = 0f;
		float desplazamientoVertical = -20f;
		canvas.drawTextOnPath(
				"Desarrollo de aplicaciones para móviles con Android",
				trazo, desplazamientoHorizontal, desplazamientoVertical,
				pincel);
		
		miImagenBM.draw(canvas);
		
		miImagen.draw(canvas);

	}

}
