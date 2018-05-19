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
                <?php echo $content; ?>
            </div>
           
           <!--<div id="sidebar">
                
            </div>--> 
            
            
           <footer>
               <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #6495ED;">
                <font size=2 color="white">Los Cache. Universidad de Los Andes. Todos los derechos reservados.</font></nav>
            </footer>

        </div>
    </body>
</html>
