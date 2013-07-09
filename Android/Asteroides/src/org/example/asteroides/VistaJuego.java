package org.example.asteroides;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener {
	
	
	private float mX=0, mY=0;
	private boolean disparo=false;

	
	//ASTEROIDES
    private Vector<Grafico> Asteroides; // Vector con los Asteroides
    private int numAsteroides= 5; // Número inicial de asteroides
    private int numFragmentos= 3; // Fragmentos en que se divide
    
    //NAVE
    private Grafico nave;// Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private float aceleracionNave; // aumento de velocidad
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;
    
    // MISIL 
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo = false;
    private int tiempoMisil;
    
    
	 // //// THREAD Y TIEMPO //////
	 // Thread encargado de procesar el juego
	 private ThreadJuego thread = new ThreadJuego();
	 // Cada cuanto queremos procesar cambios (ms)
	 private static int PERIODO_PROCESO = 50;
	 // Cuando se realizó el último proceso
	 private long ultimoProceso = 0;
	 
	 // SENSORES
	 private boolean hayValorInicial = false;
	 private float valorInicial;
    
    
    public VistaJuego(Context context, AttributeSet attrs) {

          super(context, attrs);

          Drawable drawableNave, drawableAsteroide, drawableMisil;
          drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);

          Asteroides = new Vector<Grafico>();

          for (int i = 0; i < numAsteroides; i++) {
                Grafico asteroide = new Grafico(this, drawableAsteroide);
                asteroide.setIncY(Math.random() * 4 - 2);
                asteroide.setIncX(Math.random() * 4 - 2);
                asteroide.setAngulo((int) (Math.random() * 360));
                asteroide.setRotacion((int) (Math.random() * 8 - 4));
                Asteroides.add(asteroide);
                
                /**
                 * Es imposible iniciar la posición de un asteroide, dado que no 
                 * conocemos el alto y ancho de la pantalla. Esta información será 
                 * conocida cuando se llame a onSizeChanged(). 
                 */
          }

          drawableNave = context.getResources().getDrawable(R.drawable.nave);
          nave = new Grafico(this, drawableNave);
          
          /*
          ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
          dMisil.getPaint().setColor(Color.WHITE);
          dMisil.getPaint().setStyle(Style.STROKE);
          dMisil.setIntrinsicWidth(15);
          dMisil.setIntrinsicHeight(3);
          drawableMisil = dMisil;*/
          
          drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
          misil = new Grafico(this, drawableMisil);
          
          
//          SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//          List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
//          
//          if (!listSensors.isEmpty()) {
//             Sensor orientationSensor = listSensors.get(0);
//             mSensorManager.registerListener(this, orientationSensor,SensorManager.SENSOR_DELAY_GAME);
//          }
    }

    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    @Override 
    public void onSensorChanged(SensorEvent event) {
          float valor = event.values[1];
          if (!hayValorInicial){
           valorInicial = valor;
                  hayValorInicial = true;
          }
          giroNave=(int) (valor-valorInicial)/3 ;
    }


    @Override 
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
          super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

          // Una vez que conocemos nuestro ancho y alto.
          
          //Se ubica la nave en el centro de la pantalla
          nave.setPosX(ancho/2 - nave.getAncho()/2);
          nave.setPosY(alto/2 - nave.getAlto()/2);
          
          for (Grafico asteroide: Asteroides) {
        	  
        	//Se intenta generar una posición del asteroide que no solape con la nave.   
			do {
				asteroide.setPosX(Math.random()	* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
          }
          
	      ultimoProceso = System.currentTimeMillis();
	      thread.start();     
    }



    @Override 
    protected synchronized void onDraw(Canvas canvas) {
          super.onDraw(canvas);

          for (Grafico asteroide: Asteroides) {
              asteroide.dibujaGrafico(canvas);
          }
          
          nave.dibujaGrafico(canvas);
          
          if(misilActivo)
        	  misil.dibujaGrafico(canvas);

    }
    
    protected synchronized void actualizaFisica() {
    	
        long ahora = System.currentTimeMillis();
        
        // No hagas nada si el período de proceso no se ha cumplido.
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
              return;
        }
        
        // Para una ejecución en tiempo real calculamos retardo           
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        
        ultimoProceso = ahora; // Para la próxima vez
        
        // Actualizamos velocidad y dirección de la nave a partir de 
        // giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
        
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        
        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX,nIncY) <= Grafico.MAX_VELOCIDAD){
              nave.setIncX(nIncX);
              nave.setIncY(nIncY);
        }
        
        // Actualizamos posiciones X e Y
        nave.incrementaPos(retardo);
        for (Grafico asteroide : Asteroides) {
              asteroide.incrementaPos(retardo);
        }  
        
        
        // Actualizamos posición de misil

        if (misilActivo) {
               misil.incrementaPos(retardo);
               tiempoMisil-=retardo;
               if (tiempoMisil < 0) {
                     misilActivo = false;
               } else {
			        for (int i = 0; i < Asteroides.size(); i++)
	                     if (misil.verificaColision(Asteroides.elementAt(i))) {
	                            destruyeAsteroide(i);
	                            break;
	                     }
               }
        }
		
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		float x = event.getX();
		float y = event.getY();
		
		float a;
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				disparo = true;
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = Math.abs(x - mX);
				float dy = Math.abs(y - mY);
				Log.d("TE", "mY="+mY+" dy="+dy);
				if (dy < 6 && dx > 6) {
					giroNave = Math.round((x - mX) / 2);
					disparo = false;
				} else if (dx < 6 && dy > 6) {
					a = mY - y;
					if(a>=0f)
						aceleracionNave = Math.round(a / 25);
					
					Log.d("TE", "y="+y);
					Log.d("TE", "aceleracion="+aceleracionNave);
					disparo = false;
				}
				break;
			case MotionEvent.ACTION_UP:
				giroNave = 0;
				aceleracionNave = 0;
				if (disparo) {
					 ActivaMisil();
				}
				break;
		}
		mX = x;
		mY = y;
		return true;
	}
    
    private class ThreadJuego extends Thread {
		@Override
		public void run() {
			while (true) {
				actualizaFisica();
				try {
					Thread.sleep(PERIODO_PROCESO);
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
    
    private void destruyeAsteroide(int i) {
        Asteroides.remove(i);
        misilActivo = false;
	}
	  
	private void ActivaMisil() {
	        misil.setPosX(nave.getPosX()+ nave.getAncho()/2-misil.getAncho()/2);
	        misil.setPosY(nave.getPosY()+ nave.getAlto()/2-misil.getAlto()/2);
	        misil.setAngulo(nave.getAngulo());
	        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) *
	                         PASO_VELOCIDAD_MISIL);
	        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) *
	                         PASO_VELOCIDAD_MISIL);
	        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs( misil.
	           getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
	        misilActivo = true;
	}
}