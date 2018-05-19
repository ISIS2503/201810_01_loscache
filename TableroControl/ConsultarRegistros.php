<?php
$content = "

Ingrese su información:
<br>


<form action=\"vistaRegistros.php\" method=\"post\">
Correo:<input name=\"email\"></input>
Contraseña:<input name=\"pw\"></input>

<button type=\"submit\" name=\"vr\" formaction=\"verificando.php\">Acceder</button>
</form>
";


include 'Template.php';
?>

