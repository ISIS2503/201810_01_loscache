package main;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Este programa se apoya en el código realizado por el usuario m2mlO-gister, ubicado en el repositorio https://gist.github.com/m2mIO-gister/5275324
 * @author Los cache
 *
 */
public class Listener implements MqttCallback {

    /** Url del mosquitto */
    private static final String brokerUrl = "tcp://172.24.41.200:8083";

    /** Id del cliente */
    private static final String clientId = "Persistencia";

    /** Nombre del topico */
    private static final String topicoEntrada = "unirest1.inmueble1.sensor1";
    
    private static final String topicoSalida = "unirest1.inmueble1.sensor1.config";
    
    private static Date ultimoHB=new Date();

    private static int perdidos;
    
    private static String sessionToken;


   
    public static void main(String[] args) {

    	ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
                   public void run() {
                	   long ultimoRecibido=ultimoHB.getTime()/1000;
                   	long actual=new Date().getTime()/1000;
                   	long dif=actual-ultimoRecibido;
                   	if(dif>10)
                   	{
                   		perdidos++;
                   		System.out.println("perdidos= "+perdidos);
                   	}
                   	
                   	if(perdidos==3)
                   	{
                   		SendMail.enviar("Sensor1");
                   		try {
							enviarFallo();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                   	};
                   }
               }, 0, 10, TimeUnit.SECONDS);
        new Listener().subscribe(topicoEntrada);
        
    }
    
    public static void checkear()
    {
    	long ultimoRecibido=ultimoHB.getTime()/1000;
    	long actual=new Date().getTime()/1000;
    	long dif=actual-ultimoRecibido;
    	if(dif>10)
    	{
    		System.out.println("perdidos= "+perdidos);
    		perdidos++;
    	}
    	
    	if(perdidos==3)
    	{
    		SendMail.enviar("Sensor1");
    		
    	}
    	
    }

    /**
     * Se suscribe al topico indicado en la url del broker.
     */
    public void subscribe(String topic) {

        MemoryPersistence persistence = new MemoryPersistence();

        try {

            MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);


            System.out.println("Conectandose al broker HB ubicado en la direccion: " + brokerUrl);
            sampleClient.connect(connOpts);
            System.out.println("Conectado");

            sampleClient.setCallback(this);
            sampleClient.subscribe(topic);

            System.out.println("Se ha logrado suscribirse al topico, se esta escuchando");

        } catch (MqttException me) {

            System.out.println("Mqtt reason " + me.getReasonCode());
            System.out.println("Mqtt msg " + me.getMessage());
            System.out.println("Mqtt loc " + me.getLocalizedMessage());
            System.out.println("Mqtt cause " + me.getCause());
            System.out.println("Mqtt excep " + me);
        }
    }

    
    public void connectionLost(Throwable arg0) {
    	arg0.printStackTrace();
    	System.out.println("Conexion perdida");

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }
    
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    	if(message.toString().equals(""))
    		return;
    	else { 

    		String m = message.toString();
    		if(m.indexOf("HB")!=-1)
    		{
    			System.out.println("HB recibido");
    			ultimoHB=new Date(); 
    			perdidos=0;

    		}
    	}
    }
    
    public static void enviarFallo() throws Exception
    {
    	if(sessionToken==null)
    		token();
    	URL url = new URL("http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/inmuebles/Inmueble1/sensores/Sensor1/alarmas");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer "+ sessionToken);
		
		System.out.println(url.toString());
		String input = "{\"mensaje\":\"SENSOR FUERA DE LINEA\",\"fecha\":\"19/05/2018\"}";
		

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

		if (conn.getResponseCode() != 200 && conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
    }
    
    public static void token() throws Exception
    {
    	String input="{\n" + 
				"\"grant_type\":\"http://auth0.com/oauth/grant-type/password-realm\",\n" + 
				"\"username\": \"kelly@yale.com\",\n" + 
				"\"password\": \"Loscache2018\",\n" + 
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
    }
    }

}
