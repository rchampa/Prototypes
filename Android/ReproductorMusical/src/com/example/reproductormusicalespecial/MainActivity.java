package com.example.reproductormusicalespecial;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, ControlesReproductor{
	
	public final int PLAYING = 0,
			STOPPED = 1, 
			PAUSED = 2,
			NOT_READY = 3,
			CODIGO = 4;
	private final String RUTA = "tempData";
	
	private MediaPlayer reproductor;
	private Button playButton, pauseButton, stopButton, elegirButton;
	private TextView nombreCancionLB;
	private int estado;
	private Uri audioUri;
	private int msec;
	private boolean vienedeDeSC;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        playButton = (Button)this.findViewById(R.id.playB);
        pauseButton = (Button)this.findViewById(R.id.pauseB);
        stopButton = (Button)this.findViewById(R.id.stopB);
        elegirButton = (Button)this.findViewById(R.id.cancionB);
        nombreCancionLB = (TextView)this.findViewById(R.id.nombreCancionTV);
        
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        elegirButton.setOnClickListener(this);
        
        estadoSinCancion();
        
    }
    
    private void reiniciar(){
    	 playButton.setEnabled(true);
         stopButton.setEnabled(false);
         pauseButton.setEnabled(false);
         estado = STOPPED;
    }
    
    private void estadoSinCancion(){
   	 	playButton.setEnabled(false);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        estado = NOT_READY;
   }

    protected void onRestart() {
        super.onRestart();
        
        if(!vienedeDeSC){
			try {
				FileInputStream fis = this.openFileInput(RUTA);
				ObjectInputStream is = new ObjectInputStream(fis);
				
				EstadoReproductor estadoR = (EstadoReproductor) is.readObject();
				is.close();
				
				audioUri = Uri.parse(estadoR.getAudioUri());
				if(audioUri!=null && estadoR!=null){
					
					if(reproductor!=null) reproductor.release();
					
					reproductor = MediaPlayer.create(this,audioUri);
					reiniciar();
					obtenerDatosCancion(audioUri);
					
					int msec = estadoR.getMsec();
					reproductor.seekTo(msec);
					
					int estado = estadoR.getEstado(); 
					
					switch(estado){
						case PLAYING : pressPlay(reproductor);
										break;
										
						case STOPPED : 	estado = STOPPED;
										playButton.setEnabled(true);
										pauseButton.setEnabled(false);
										stopButton.setEnabled(false);
										break;
										
						case PAUSED : 	pressPause(reproductor);
										break;
					}
					
					Toast.makeText(this, "MiliSegundo "+msec, Toast.LENGTH_LONG).show();
					
				}
				else{
					estadoSinCancion();
				}
					
					
			} catch (ClassNotFoundException e) {
				e.printStackTrace();	        
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
    }
    
    protected void onPause() {
        super.onPause();
        
        if(estado!=NOT_READY){
        
			try {
				FileOutputStream fos = this.openFileOutput(RUTA, MODE_PRIVATE);
				ObjectOutputStream os = new ObjectOutputStream(fos);
				msec = reproductor.getCurrentPosition();
		        os.writeObject(new EstadoReproductor(estado, msec, 0, audioUri.toString()));
		        os.flush();
		        os.close();
		        
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
		if(isFinishing() && reproductor!=null){//si se cierra inesperadamente
	    	reproductor.release();//Se debe liberar el recurso
		}
		
    }

    
    protected void onStop(){
    	super.onStop();
    	if(reproductor!=null)
    		reproductor.release();//Se debe liberar el recurso
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
       super.onConfigurationChanged(newConfig);
    }
    
	public void onClick(View v) {
		
		
		if(v == playButton)
			pressPlay(this.reproductor);
		else if(v == stopButton)
			pressStop(this.reproductor);
		else if(v == pauseButton)
			pressPause(this.reproductor);
		else if(v == elegirButton){
			Intent intent = new Intent();
			intent.setType("audio/mp3");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Abrir audio mp3"), CODIGO);

		}
		
	}
	
	
	public void pressPlay(MediaPlayer reproductor) {
		
		//Empieza o reanuda la reproduccion
		reproductor.start();
		estado = PLAYING;
		
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
		stopButton.setEnabled(true);
		
	}

	public void pressPause(MediaPlayer reproductor) {
		
		reproductor.pause();
		estado = PAUSED;
		
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(true);
		
	}

	public void pressStop(MediaPlayer reproductor) {
		
		reproductor.stop();
		estado = STOPPED;
		
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		
		try {
			reproductor.prepare();
			reproductor.seekTo(0);
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK && requestCode == CODIGO){
			audioUri = data.getData();
			
			if(reproductor!=null)
				reproductor.release();
			
			reproductor = MediaPlayer.create(this,audioUri);
			reiniciar();
			obtenerDatosCancion(audioUri);
			vienedeDeSC = true;
		}
		
	}
	
	public void obtenerDatosCancion(Uri audioUri){
		String[] proj = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Artists.ARTIST };
				
		int col_index = -1;
		Cursor tempCursor = managedQuery(audioUri, proj, null, null, null);
		tempCursor.moveToFirst();
		col_index = tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		String rutaReal = tempCursor.getString(col_index);
		col_index = tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        String nombreCancion = tempCursor.getString(col_index);
        col_index = tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        String nombreArtista = tempCursor.getString(col_index);
        
        nombreCancionLB.setText("Cancion: "+nombreCancion.trim()+" - "+ nombreArtista.trim());
        
	}
}
