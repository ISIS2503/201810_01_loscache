<?php

$email=$_POST["email"];
$pw=$_POST["pw"];
// The data to send to the API
$postData = array(
    'grant_type' => 'http://auth0.com/oauth/grant-type/password-realm',
    'username' => $email,
    'password' => $pw,
    'client_id' => '2ylfhutIMlUx5zBKGqSoJNvljBxQFyXW',
    'client_secret' => 'qNBo9Jdz1l9AkNZd52nWA9mTuYO8knGifuGyxVA9BXr5Np57y2pMslCGT1Hyv9eV',
	'realm' => 'Username-Password-Authentication'
	
);

// Setup cURL
$ch = curl_init('https://isis2503-fposada.auth0.com/oauth/token');
curl_setopt_array($ch, array(
    CURLOPT_POST => TRUE,
    CURLOPT_RETURNTRANSFER => TRUE,
    CURLOPT_HTTPHEADER => array('Content-Type: application/json'),
    CURLOPT_POSTFIELDS => json_encode($postData)
));

// Send the request
$response = curl_exec($ch);

// Check for errors
if($response === FALSE){
    die(curl_error($ch));
}

$responseData = json_decode($response, TRUE);
$tokensito=$responseData['id_token'];

if(is_null($tokensito))
{
    die("No se logro autenticar");
}
?>


<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><?php echo $title; ?></title>
        <link rel="stylesheet" type="text/css" href="Styles/Stylesheet.css" />
        <style></style>
    </head>
    <body>
        <div id="wrapper">
            <div id="banner">             
            </div>
            
            <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #6495ED;">
  <a class="navbar-brand" href="index.php">Los Cache</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="ConsultarRegistros.php"><b>Tablero de control</b></a>
      </li>
    </ul>
  </div>
</nav>
            
            <div id="content_area">
                <h2>Ha accedido correctamente</h2>
<form action="vistaRegistros.php" method="post">
<input hidden name="tok" value="<?php echo $tokensito ?>"></input>
<button type="submit" name="vr" formaction="vistaRegistros.php">Continuar</button>
</form>
            </div>
           
           <!--<div id="sidebar">
                
            </div>--> 
            
            
           <footer>
               <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #6495ED;">
                <font size=2 color="white">Los cache. Universidad de Los Andes. Todos los derechos reservados.</font></nav>
            </footer>

        </div>
    </body>
</html>
