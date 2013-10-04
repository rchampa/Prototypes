package com.example.reproductormusicalespecial;

import java.io.Serializable;

import android.net.Uri;

public class EstadoReproductor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int PLAYING = 0, STOPPED = 1, PAUSED = 2, READY = 3;
	private int estado;
	private int msec;
	private int volumen;
	private String audioUri;
	public EstadoReproductor(int estado, int msec, int volumen, String audioUri) {
		this.estado = estado;
		this.msec = msec;
		this.volumen = volumen;
		this.audioUri = audioUri;
	}
	public int getEstado() {
		return estado;
	}
	public int getMsec() {
		return msec;
	}
	public int getVolumen() {
		return volumen;
	}
	public String getAudioUri() {
		return audioUri;
	}
	
	
	
}
