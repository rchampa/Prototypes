<?php

$nickname = $_POST['nickname'];
$password = $_POST['password'];
$name = $_POST['name'];
$surname = $_POST['surname'];
$age = $_POST['age'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

	if($db->isuserexist($nickname)){
		echo(" Este usuario ya existe ingrese otro diferente!");
	}
	else{

		if($db->adduser($nickname,$password,$name,$surname,$age)){	
			$resultado[]=array("estado"=>"0", "mensaje"=>"El usuario fue agregado a la Base de Datos correctamente.");
		}else{
			$resultado[]=array("estado"=>"1", "mensaje"=>"Ha ocurrido un error.");
		}		

	}

echo json_encode($resultado);
	
?>



