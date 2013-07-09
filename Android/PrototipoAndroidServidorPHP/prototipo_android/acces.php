
<?php

/*LOGIN*/

$usuario = $_POST['usuario'];
$passw = $_POST['password'];


require_once 'funciones_bd.php';
$db = new funciones_BD();

	if($db->login($usuario,$passw)){

		$resultado[]=array("estado"=>"0", "mensaje"=>"Login incorrecto");
	}
	else{
		$resultado[]=array("estado"=>"1", "mensaje"=>"Login correcto");
	}

echo json_encode($resultado);




?>
