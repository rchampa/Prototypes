<?php
 
class funciones_BD {
 
    private $db;
 
    // constructor

    function __construct() {
        require_once 'connectbd.php';
        // connecting to database

        $this->db = new DB_Connect();
        $this->db->connect();

    }
 
    // destructor
    function __destruct() {
 
    }
 
    /**
     * agregar nuevo usuario
     */
    public function adduser($nickname, $password, $name, $surname, $age) {	
	
    $result = mysql_query("INSERT INTO usuarios (nickname,password,name,surname,age) VALUES('$nickname', '$password', '$name', '$surname', $age)");
        // check for successful store

        if ($result) {

            return true;

        } else {

            return false;
        }

    }
 
 
     /**
     * Verificar si el usuario ya existe por el username
     */

    public function isuserexist($nickname) {

        $result = mysql_query("SELECT nickname from usuarios WHERE nickname = '$nickname'");

        $num_rows = mysql_num_rows($result); //numero de filas retornadas		
		
        if ($num_rows > 0) {
	    echo("lalala");
            return true; // el usuario existe 
        } else {
            return false; // no existe
        }
    }
 
   
	public function login($id,$passw){

		$result = mysql_query("SELECT COUNT(*) FROM usuarios WHERE id='$id'"); 
		$count = mysql_fetch_row($result);

		/*como el usuario debe ser unico cuenta el numero de ocurrencias con esos datos*/
		if ($count[0]==0){
			return true;
		}else{
			return false;
		}
	}
	
	public function update_location($id,$longitud,$latitud){
		//INSERT INTO traza_usuarios(id, latitud, longitud) VALUES ('ricardo',22.33,33.66)
		$traza = mysql_query("INSERT INTO traza_usuarios(id, latitud, longitud) VALUES ('$id', $latitud, $longitud)");  
		//INSERT INTO localizacion (id, latitud, longitud) VALUES ('ricardo', 3, 3) ON DUPLICATE KEY UPDATE latitud=3, longitud=3;
		$result = mysql_query("INSERT INTO localizacion (id, latitud, longitud) VALUES ('$id', $latitud, $longitud) ON DUPLICATE KEY UPDATE latitud=$latitud, longitud=$longitud;"); 
		
		if ($result) {

            return true;

        } else {

            return false;
        }
		
	}
  
}
 
?>
