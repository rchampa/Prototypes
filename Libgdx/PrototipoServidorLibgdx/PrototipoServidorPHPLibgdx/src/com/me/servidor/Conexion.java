package com.me.servidor;

public interface Conexion {

	//FIXME cambiar arrays por List "especializada" y no sólo quiero decir parametrizada
	boolean checkStatus(String... params);
	boolean validarDatos(String... params);
	void conexionEstablecida();
	void errorEnConexion();
}
