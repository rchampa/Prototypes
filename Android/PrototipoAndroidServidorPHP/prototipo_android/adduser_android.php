<?php

$usuario = $_GET['usuario'];
$passw = $_GET['password'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

	if($db->isuserexist($usuario,$passw)){

	echo(" Este usuario ya existe ingrese otro diferente!");
	}else{

		if($db->adduser($usuario,$passw))
		{	echo(" El usuario fue agregado a la Base de Datos correctamente.");			
			 }else{
			echo(" ha ocurrido un error.");
			}		

	}



?>



