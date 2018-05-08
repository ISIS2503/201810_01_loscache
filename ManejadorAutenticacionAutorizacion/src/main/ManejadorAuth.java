package main;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Este programa se apoya en el cÃ³digo realizado por el usuario m2mlO-gister, ubicado en el repositorio https://gist.github.com/m2mIO-gister/5275324
 * @author Los cache
 *
 */
public class ManejadorAuth  {

    
    private static final String mainDir = "http://localhost:8080/Yale/hubs/";
    
    private static final String unRes="/unidadesResidenciales";
    
    private static final String inmuebles="/inmuebles";
    
    private static final String sensores="/sensores";
    
    private static final String alarmas="/alarmas";
    
    private static String sessionToken;
    
    private static String authToken;
    
    private static boolean termino=false;

   
    public static void main(String[] args) throws Exception  {
    	atender();
        
    }
    
    public static void atender() throws Exception
    {
    	Scanner sc=new Scanner(System.in);
    	
    	while(!termino)
    	{
    		printMenu();
    		String option=sc.next();
    		if(option.equals("1"))
    		{       			
    			String us;
    			String con;
    			
    			System.out.println("Ingrese su email");
    			us=sc.next();
    			System.out.println("Ingrese su contraseña");
    			con=sc.next();
    			
    			String input="{\n" + 
    					"\"grant_type\":\"http://auth0.com/oauth/grant-type/password-realm\",\n" + 
    					"\"username\": \""+us+"\",\n" + 
    					"\"password\": \""+con+"\",\n" + 
    					"\"client_id\": \"2ylfhutIMlUx5zBKGqSoJNvljBxQFyXW\", \n" + 
    					"\"client_secret\": \"qNBo9Jdz1l9AkNZd52nWA9mTuYO8knGifuGyxVA9BXr5Np57y2pMslCGT1Hyv9eV\", \n" + 
    					"\"realm\": \"Username-Password-Authentication\"\n" + 
    					"}";
    			
    			//System.out.println(input);
    			
    			URL url = new URL("https://isis2503-fposada.auth0.com/oauth/token");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
				
				boolean exito=true;

				if (conn.getResponseCode() != 200 && conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					System.out.println("Error en la autenticacion, intentelo de nuevo.");
					System.out.println(conn.getResponseMessage());
					System.out.println();
					System.out.println();
					exito=false;
				}
				
				if(exito) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				
				while ((output = br.readLine()) != null) {
					if(output.contains("id_token"))
					{
						String[] paso1=output.split("\"id_token\":\"");
						String[] paso2=paso1[1].split("\"");
						sessionToken=paso2[0];						
					}					
				}
				
				System.out.println(sessionToken);

				
				System.out.println("Ha ingresado correctamente");
				System.out.println();
				
				}
    			
    		}
    		else if(option.equals("2"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				
    				String dir=mainDir+hubid+unRes+"/"+unid+"/reporte";
    				String tip="GET";
    				ejecutarTarea(tip,dir,null);   				
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("3"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+"/reporte";
    				String tip="GET";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("4"))
    		{
    			if(sessionToken!=null)
    			{
    			 System.out.println("Ingrese el json del hub en una sola linea");
    			 String input=sc.next();
    			 
    				String dir="http://localhost:8080/Yale/hubs";
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("5"))
    		{
    			if(sessionToken!=null)
    			{
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    			 System.out.println("Ingrese el json de la unidad residencial en una sola linea");
    			 String input=sc.next();
    			 
    				String dir=mainDir+hubid+unRes;
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("6"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    			 System.out.println("Ingrese el json del inmueble en una sola linea");
    			 String input=sc.next();
    			 
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles;
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("7"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    			 System.out.println("Ingrese el json del sensor en una sola linea");
    			 String input=sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores;
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("8"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				System.out.println("Ingrese el id del sensor");
    				String seid = sc.next();    		
    				System.out.println("Ingrese el json de la alarma en una sola linea");
    				String input=sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid+alarmas;
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("9"))
    		{
    			if(sessionToken!=null)
    			{
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				String dir=mainDir+hubid;
    				String tip="DELETE";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("10"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles;
    				String tip="DELETE";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("11"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid;
    				String tip="DELETE";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("12"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				System.out.println("Ingrese el id del sensor");
    				String seid = sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid;
    				String tip="DELETE";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("13"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				System.out.println("Ingrese el id del sensor");
    				String seid = sc.next();    		
    				System.out.println("Ingrese el id de la alarma");
    				String alid=sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid+alarmas+"/"+alid;
    				String tip="DELETE";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("14"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				System.out.println("Ingrese el id del sensor");
    				String seid = sc.next();    		
    				System.out.println("Ingrese el json del horario en una sola linea");
    				String input=sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid+"/Horarios";
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    		}
    		else if(option.equals("15"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				System.out.println("Ingrese el id del sensor");
    				String seid = sc.next();    		
    				System.out.println("Ingrese el id del horario");
    				String alid=sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid+"/Horarios/"+alid;
    				String tip="DELETE";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("16"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+"/allHorarios";
    				String tip="GET";
    				ejecutarTarea(tip,dir,null);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    			
    		}
    		else if(option.equals("17"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				System.out.println("Ingrese el id del sensor");
    				String seid = sc.next();    		
    				System.out.println("Ingrese el json de la clave en una sola linea");
    				String input=sc.next();
    				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid+"/Claves";
    				String tip="POST";
    				ejecutarTarea(tip,dir,input);
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    		}
    		else if(option.equals("18"))
    		{
    			String unid;
				String hubid;
				System.out.println("Ingrese el id del hub");
				hubid=sc.next();
				System.out.println("Ingrese el id de la unidad residencial");
				unid=sc.next();
				System.out.println("Ingrese el id del inmueble");
				String inid = sc.next();
				System.out.println("Ingrese el id del sensor");
				String seid = sc.next(); 
				String dir=mainDir+hubid+unRes+"/"+unid+inmuebles+"/"+inid+sensores+"/"+seid+"/Claves";
				String tip="GET";
				ejecutarTarea(tip,dir,null);
    		}
    		else if(option.equals("U1"))
    		{
    			revisarAccesoAuth();
    			System.out.println("Ingrese el json del usuario (Ej:{\n" + 
    					"	\"connection\": \"Username-Password-Authentication\",\n" + 
    					"	\"email\":\"hernandofinal@securitas.com\",\n" + 
    					"	\"password\":\"Loscache2018\"\n" + 
    					"})");
    			String input=sc.next();
    			String dir="https://isis2503-fposada.auth0.com/api/v2/users";
    			ejecutarTareaAuth("POST", dir, input);
    			
    		}
    		else if(option.equals("U2"))
    		{
    			revisarAccesoAuth();
    			String dir="https://isis2503-fposada.auth0.com/api/v2/users";
    			ejecutarTareaAuth("GET", dir, null);
    		}
    		else if(option.equals("U3"))
    		{
    			revisarAccesoAuth();
    			System.out.println("Ingrese la id del usuario, las ids son de la forma auth0|xxxxxxxxxxxx");
    			String usid=sc.next();
    			String dir="https://isis2503-fposada.auth0.com/api/v2/users/"+usid;
    			ejecutarTareaAuth("GET", dir, null);
    		}
    		else if(option.equals("U4"))
    		{
    			revisarAccesoAuth();
    			System.out.println("Ingrese la id del usuario, las id's son de la forma auth0|xxxxxxxxxxxx");
    			String usid=sc.next();
    			System.out.println("Ingrese el json con la nueva informacion");
    			String input=sc.next();
    			String dir="https://isis2503-fposada.auth0.com/api/v2/users/"+usid;
    			ejecutarTareaAuth("PATCH",dir,input);

    		}
    		else if(option.equals("U5"))
    		{
    			revisarAccesoAuth();
    			System.out.println("Ingrese la id del usuario, las ids son de la forma auth0|xxxxxxxxxxxx");
    			String usid=sc.next();
    			String dir="https://isis2503-fposada.auth0.com/api/v2/users/"+usid;
    			ejecutarTareaAuth("DELETE", dir, null);
    		}
    		else if(option.equals("F1"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el nombre del barrio");
    				unid=sc.next();
    				
    				String dir=mainDir+hubid+"/mensualesBarrio/"+unid;
    				String tip="GET";
    				ejecutarTarea(tip,dir,null);   				
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    		}
    		else if(option.equals("F3"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				
    				String dir=mainDir+hubid+unRes+"/"+unid+"/mensuales";
    				String tip="GET";
    				ejecutarTarea(tip,dir,null);   				
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    		}
    		else if(option.equals("F2"))
    		{
    			if(sessionToken!=null)
    			{
    				String unid;
    				String hubid;
    				System.out.println("Ingrese el id del hub");
    				hubid=sc.next();
    				System.out.println("Ingrese el id de la unidad residencial");
    				unid=sc.next();
    				System.out.println("Ingrese el id del inmueble");
    				String inid = sc.next();
    				
    				String dir=mainDir+hubid+unRes+"/"+unid+"/inmuebles/"+inid+"/mensuales";
    				String tip="GET";
    				ejecutarTarea(tip,dir,null);   				
    			}
    			else
    				System.out.println("No ha ingresado el sistema, debe iniciar sesion");
    		}
    		else if(option.equals("SalirSesion"))
    		{
    			sessionToken=null;
    			System.out.println("Ha salido del sistema, vuelva a iniciar sesión.");
    		}    		
    		
    		else if(option.equals("Fin"))
    		{
    			termino=true;
    			sessionToken=null;   				
    		}
    		else
			{
				System.out.println("Ingreso una opción que no existe");
				break;
			}
    		
    	}
    }

    
       
        
    public static void printMenu()
    {
    	System.out.println("Menu de manejo de solicitudes Yale");
    	System.out.println("---------------------------------------------------------------------------------------------");
    	System.out.println("U1. Crear un usuario nuevo");
    	System.out.println("U2. Listar los usuarios");
    	System.out.println("U3. Consultar un usuario particular");
    	System.out.println("U4. Actualizar un usuario (No es soportado en Java pero sí en Postman)");
    	System.out.println("U5. Eliminar un usuario");
    	System.out.println("---------------------------------------------------------------------------------------------");
    	System.out.println("1. Ingresar con email y contraseña, si no ingresa no podrá realizar ninguna operación.");
    	System.out.println("2. Generar el reporte de alarmas de una unidad residencial.");
    	System.out.println("3. Generar el reporte de alarmas de un inmueble.");
    	System.out.println("4. Agregar un Hub.");
    	System.out.println("5. Agregar una unidad residencial en un Hub.");
    	System.out.println("6. Agregar un inmueble en una unidad residencial.");
    	System.out.println("7. Agregar un sensor en un inmueble.");
    	System.out.println("8. Agregar una alarma en un sensor.");
    	System.out.println("9. Borrar un Hub.");
    	System.out.println("10. Borrar una unidad residencial en un Hub.");
    	System.out.println("11. Borrar un inmueble en un Hub.");
    	System.out.println("12. Borrar un sensor en un inmueble.");
    	System.out.println("13. Borrar una alarma en un sensor.");
    	System.out.println("14. Agregar un horario en un sensor.");
    	System.out.println("15. Borrar un horario en un sensor.");
    	System.out.println("16. Consultar horarios en un sensor.");
    	System.out.println("17. Agregar una clave en un sensor.");
    	System.out.println("18. Consultar claves en un sensor.");
    	System.out.println("---------------------------------------------------------------------------------------------");
    	System.out.println("F1. Consultar alarmas mensuales de un barrio.");
    	System.out.println("F2. Consultar alarmas mensuales de un inmueble.");
    	System.out.println("F3. Consultar alarmas mensuales de una unidad.");

    	System.out.println("SalirSesion - Cierra la sesión para ingresar con otra cuenta de usuario");

    	System.out.println("Fin - Enviar cambios");
    	System.out.println("---------------------------------------------------------------------------------------------");
    	System.out.println("Para ejecutar una función, ingrese la opción correspondiente y presione Enter (ej: \"2\" + enter");
    }
    
    public static void ejecutarTarea(String tipo, String dir, String input) throws Exception
    {
    	URL url = new URL(dir);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(tipo);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer "+sessionToken);
		
		System.out.println(dir);

		if(input!=null) {
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();}
		
		boolean exito=true;

		if (conn.getResponseCode() != 200 && conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			System.out.println("Error en la tarea, intentelo de nuevo.");
			System.out.println(conn.getResponseCode());
			System.out.println(conn.getResponseMessage());
			System.out.println();
			System.out.println();
			exito=false;
		}

		if(exito) { 
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;
			System.out.println("Respuesta del servidor:");

			while ((output = br.readLine()) != null) {
				System.out.println(output);				
			}
		}
    }
    
    public static void ejecutarTareaAuth(String tipo, String dir, String input) throws Exception
    {
    	URL url = new URL(dir);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		
		conn.setRequestMethod(tipo);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer "+authToken);
		
		System.out.println(dir);

		if(input!=null) {
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();}
		
		boolean exito=true;

		if (conn.getResponseCode() != 200 && conn.getResponseCode() != 201 && conn.getResponseCode() != 204 && conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			System.out.println("Error en la tarea, intentelo de nuevo.");
			System.out.println(conn.getResponseMessage());
			System.out.println();
			System.out.println();
			exito=false;
		}

		if(exito) { 
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;

			while ((output = br.readLine()) != null) {
				System.out.println(output);				
			}
		}
    }
    
    public static void revisarAccesoAuth() throws Exception
    {
    	if(authToken==null) {
    	URL url = new URL("https://isis2503-fposada.auth0.com/oauth/token");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		
		String input="{\n" + 
				"	\"grant_type\":\"client_credentials\",\n" + 
				"	\"client_id\": \"2ylfhutIMlUx5zBKGqSoJNvljBxQFyXW\",\n" + 
				"	\"client_secret\": \"qNBo9Jdz1l9AkNZd52nWA9mTuYO8knGifuGyxVA9BXr5Np57y2pMslCGT1Hyv9eV\",\n" + 
				"	\"audience\": \"https://isis2503-fposada.auth0.com/api/v2/\"\n" + 
				"}";

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		
		boolean exito=true;

		if (conn.getResponseCode() != 200 && conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			System.out.println("Error en la tarea, intentelo de nuevo.");
			System.out.println(conn.getResponseMessage());
			System.out.println();
			System.out.println();
			exito=false;
		}

		if(exito) { 
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;

			while ((output = br.readLine()) != null) {
				if(output.contains("access_token"))
				{
					String[] paso1=output.split("\"access_token\":\"");
					String[] paso2=paso1[1].split("\"");
					authToken=paso2[0];						
				}				
			}
		}
    	}
    }

}
