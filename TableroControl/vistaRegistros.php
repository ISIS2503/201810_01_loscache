<html>
    <head>
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title><?php echo $title; ?></title>
        <link rel="stylesheet" type="text/css" href="Styles/Stylesheet.css" />
        <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
       #map {
          float: right;
        height: 500px;
        margin: 10 10px;
  width: 50%;
  border: 2px solid black;
    border-radius: 5px;
      }
      #mapA {
          float: right;
        height: 500px;
        margin: 10 10px;
  width: 50%;
  border: 2px solid black;
    border-radius: 5px;
    display:none;
      }
      #mapF {
          float: right;
        height: 500px;
        margin: 10 10px;
  width: 50%;
  border: 2px solid black;
    border-radius: 5px;
        display:none;
      }
      #myTable {
          margin: 0 1.5%;
  width: 40%;
          float:left;
          table-layout: auto;
    font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
    border-collapse: collapse;
}

#myTable td, #myTable th {
    border: 1px solid #ddd;
    padding: 2px;
}

#myTable tr:nth-child(even){background-color: #f2f2f2;}

#myTable tr:hover {background-color: #ddd;}

#myTable th {
    padding-top: 12px;
    padding-bottom: 12px;
    text-align: center;
    background-color: #000080;
    color: white;
}

 #infoInmueble {
          margin: 0 1.5%;
  width: 40%;
          float:left;
          table-layout: auto;
    font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
    border-collapse: collapse;
    display:none;
}

#infoInmueble td, #infoInmueble th {
    border: 1px solid #ddd;
    padding: 2px;
}

#infoInmueble tr:nth-child(even){background-color: #f2f2f2;}

#infoInmueble tr:hover {background-color: #ddd;}

#infoInmueble th {
    padding-top: 12px;
    padding-bottom: 12px;
    text-align: center;
    background-color: #4169E1;
    color: white;
}

a:link {
    color: blue;
}

a:visited {
    color: blue;
}

a:hover {
    color: #006400;
}

a:active {
    color: green;
}

#restablecedor
{
    float:right;
}

#otro {
    background-color: #000080; /* Green background */
    border: 1px solid blue; /* Green border */
    color: white; /* White text */
    padding: 10px 20px; /* Some padding */
}

.btn-group button {
    background-color: #4169E1; /* Green background */
    border: 1px solid blue; /* Green border */
    color: white; /* White text */
    padding: 5px 10px; /* Some padding */
    cursor: pointer; /* Pointer/hand icon */
    float: left; /* Float the buttons side by side */
}

.btn-group button:not(:last-child) {
    border-right: none; /* Prevent double borders */
}

/* Clear floats (clearfix hack) */
.btn-group:after {
    content: "";
    clear: both;
    display: table;
}

.btn-group {
    width:45%;
   padding-top:10px;
}

/* Add a background color on hover */
.btn-group button:hover {
    background-color: #1E90FF;
}
.divider{
    width:auto;
    height:10px;
}


      
    </style>
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
                <?php
                
                


?>

<input type="text" style="display:none" id="hiddenVal" />

<h1 id="nomMapita">Nombre mapa</h1>
<div id="map"></div>
<div id="mapA"></div>
<div id="mapF"></div>

 <button id="otro" onclick="UserAction()">INICIAR TABLERO</button>
     <div class="divider"></div>
<h4>Seleccione el nombre del sensor para ver su ubicación en el mapa.</h4>
<h4>Seleccione el nombre del inmueble para ver su información:</h4>

    <table id="myTable" cellpadding="2" cellspacing="2" border="1"> <tr><th>Sensor</th> <th>Propietario</th> </tr></table>
    
    <div class="btn-group">
        <h4>Ver en el mapa:</h4><br>
        <button id="gen" onclick="mapGeneral()">Todos los sensores</button>
    <button id="fallos" onclick="showMapA()">Solo alarmas</button>
    <button id="alarmas" onclick="showMapF()">Solo fallos</button>
    </div>
         <div class="divider"></div>

    <table id="infoInmueble">
        <tr>
            <th>Dato</th>
            <th>Valor</th>
        </tr>
        <tr>
            <td>Inmueble</td>
            <td id="idIn"></td>
        </tr>
        <tr>
            <td>Propietario</td>
            <td id="propIn"></td>
        </tr>
        <tr>
            <td>Telefono</td>
            <td id="telIn"></td>
        </tr>
        <tr>
            <td>Email</td>
            <td id="emailIn"></td>
        </tr>
    </table>

    <script>
    var response;
    var map;
    var mapF;
    var mapA;
      var customLabel = {
        0: {
          label: 'Z'
        },
        4: {
          label: 'Aber'
        }
      };
      
      var customIcono = {
        0: {
          icono: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
        },
        1: {
          icono: 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png'
        },
        2:{
        icono:'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
        }
      };
      var infoWindow;
        function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          center: new google.maps.LatLng(4.919787, -74.042576),
          zoom: 16
        });
        
        mapA = new google.maps.Map(document.getElementById('mapA'), {
          center: new google.maps.LatLng(4.919787, -74.042576),
          zoom: 16
        });
        
        mapF = new google.maps.Map(document.getElementById('mapF'), {
          center: new google.maps.LatLng(4.919787, -74.042576),
          zoom: 16
        });
        infoWindow = new google.maps.InfoWindow;
              
            };
          
        
        
        function addVaino(nom,lat,lo,alarmas)
        {           
            var icono;
            var latitud = parseFloat(lat);
            var longitud = parseFloat(lo);
            var content=nom+": ";
            if (typeof alarmas !== 'undefined' && alarmas.length < 1) {
                icono='http://maps.google.com/mapfiles/ms/icons/green-dot.png';
               var marker = new google.maps.Marker({
            position: new google.maps.LatLng(latitud, longitud),
            map: map,
                icon:icono
            });
            marker.addListener('click', function() {
                infoWindow.setContent(content);
                infoWindow.open(map, marker);
              });
            }
            else{
            icono='http://maps.google.com/mapfiles/ms/icons/red-dot.png';
            var checkF=0;
            var checkA=0;
             for (var i = 0; i < alarmas.length; i++) {
                 var actual=alarmas[i].split(";");
                 var textico=actual[1];
                 
                 if(textico.indexOf("FUERA")!=-1)
                 checkF=1;
                 else
                 checkA=1;
                 
                 content=content+"/"+textico+"/";
             }
             
             var marker = new google.maps.Marker({
            position: new google.maps.LatLng(latitud, longitud),
            map: map,
                icon:icono
            });
            marker.addListener('click', function() {
                infoWindow.setContent(content);
                infoWindow.open(map, marker);
              });
             
             if(checkF==1)
             {
                var marker2 = new google.maps.Marker({
            position: new google.maps.LatLng(latitud, longitud),
            map: mapF,
                icon:icono
            });
            marker2.addListener('click', function() {
                infoWindow.setContent(content);
                infoWindow.open(mapF, marker2);
              }); 
             }
             
             if(checkA==1)
             {
                  var marker3 = new google.maps.Marker({
            position: new google.maps.LatLng(latitud, longitud),
            map: mapA,
                icon:icono
            });
            marker3.addListener('click', function() {
                infoWindow.setContent(content);
                infoWindow.open(mapA, marker3);
              }); 
             }
             
            }
            
            
        }
        
        
        
        function centrarMapa(lat,lo)
        {
            var latitud = parseFloat(lat);
            var longitud = parseFloat(lo);
            map.setCenter(new google.maps.LatLng(latitud, longitud));
            map.setZoom(18);
        }
        
              function actualizarInmueble(idInmueble)
      {
          var xhttp = new XMLHttpRequest();
           var toke="<?php echo $_POST['tok']; ?>";
    xhttp.open("GET", "http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/inmuebles/"+idInmueble, false);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.setRequestHeader("Authorization", "Bearer "+toke);

    xhttp.send(null);
    response = JSON.parse(xhttp.responseText);
    
    var celdaId=document.getElementById("idIn");
    celdaId.innerHTML=response.id;
    
    var celdaProp=document.getElementById("propIn");
    celdaProp.innerHTML=response.propietario;
    
    var celdaEmail=document.getElementById("emailIn");
    celdaEmail.innerHTML=response.emailPropietario;
    
    var celdaTel=document.getElementById("telIn");
    celdaTel.innerHTML=response.telPropietario;
    
                document.getElementById("infoInmueble").style.display = "block";

      }
        
        function mapGeneral()
        {
            document.getElementById("nomMapita").innerHTML = "Todos los sensores";
            document.getElementById("map").style.display = "block";
    document.getElementById("mapA").style.display = "none";
    document.getElementById("mapF").style.display = "none";
        }
        
        function showMapA()
        {
                        document.getElementById("nomMapita").innerHTML = "Mapa de fallos";

            document.getElementById("mapA").style.display = "block";
    document.getElementById("map").style.display = "none";
    document.getElementById("mapF").style.display = "none";
        }
        
        function showMapF()
        {
                        document.getElementById("nomMapita").innerHTML = "Mapa de alarmas";

            document.getElementById("mapF").style.display = "block";
    document.getElementById("map").style.display = "none";
    document.getElementById("mapA").style.display = "none";
        }
        
        function centroOriginal()
        {
            map.setCenter(new google.maps.LatLng(4.919787, -74.042576));
            map.setZoom(16);
        }

      function downloadUrl(url, callback) {
        var request = window.ActiveXObject ?
            new ActiveXObject('Microsoft.XMLHTTP') :
            new XMLHttpRequest;

        request.onreadystatechange = function() {
          if (request.readyState == 4) {
            request.onreadystatechange = doNothing;
            callback(request, request.status);
          }
        };

        request.open('GET', url, true);
        request.send(null);
      }
      
     var myFunc = function() {
   var xhttp = new XMLHttpRequest();
           var toke="<?php echo $_POST['tok']; ?>";
    xhttp.open("GET", "http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/sensores", false);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.setRequestHeader("Authorization", "Bearer "+toke);

    xhttp.send(null);
    response = JSON.parse(xhttp.responseText);
    

    
    for(var i = 0; i < response.length; i++)
    {
    var obj = response[i];
    var table = document.getElementById("myTable");
                var row = table.insertRow(i+1);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                
                var link = document.createElement("a");
                link.setAttribute("href", "#");
                link.setAttribute("onclick","centrarMapa("+obj.lat+","+obj.lon+")");
                var linkText = document.createTextNode(obj.id);
                link.appendChild(linkText);
                var link2 = document.createElement("a");
                link2.setAttribute("href", "#");
                link2.setAttribute("onclick","actualizarInmueble(\""+obj.idInmueble+"\")");
                var linkText2 = document.createTextNode(obj.idInmueble);
                link2.appendChild(linkText2);
                // Add the link to the previously created TableCell.
                cell1.appendChild(link);
                cell2.appendChild(link2);
    
    }
}();



      
      
      function UserAction() {
          var toke="<?php echo $_POST['tok']; ?>";
    var xhttp = new XMLHttpRequest();

    xhttp.open("GET", "http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/sensores", false);

    xhttp.setRequestHeader("Content-type", "application/json");
    
    xhttp.setRequestHeader("Authorization", "Bearer "+toke);

    xhttp.send(null);

    response = JSON.parse(xhttp.responseText);
    
    for(var i = 0; i < response.length; i++)
    {
    var obj = response[i];
    addVaino(obj.id,obj.lat,obj.lon,obj.alarmas);
    }
    
    window.setTimeout(UserAction, 5000);
}


    
   

      function doNothing() {}
       function doTest() {console.log("HOLA");}
    </script>
    
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBHkdO45WJYaOCxQVuDY7LKqiDpWRIJY9w&callback=initMap">
    </script>
    
    

   

        
                <br>
            </div>
           
           <!--<div id="sidebar">
                
            </div>--> 
            
            
            <footer>
               <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #6495ED;">
                <font size=2 color="white">Los Cache. Universidad de Los Andes.</font></nav>
            </footer>
        </div>
    </body>
</html>