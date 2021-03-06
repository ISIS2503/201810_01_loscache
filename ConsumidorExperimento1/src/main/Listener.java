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
    
    private static final String topicoSalida = "hub1";

    private static MqttClient sampleClient;

   
    public static void main(String[] args) {
    	new Listener().subscribe(topicoEntrada);
    	ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
                   public void run() {
                	   enviarMensaje("HB");
                   }
               }, 0, 10, TimeUnit.SECONDS);
        
        
    }
    
    public static void enviarMensaje(String content)
    {
    	 try {        
    		 
    		 if(sampleClient!=null) {            
             System.out.println("Publicando mensaje al topico: " + topicoSalida);
             MqttMessage message = new MqttMessage(content.getBytes());
             message.setQos(0);
             sampleClient.publish(topicoSalida, message);
             System.out.println("Message published");
    		 }
         } catch (MqttException me) {
             System.out.println("reason " + me.getReasonCode());
             System.out.println("msg " + me.getMessage());
             System.out.println("loc " + me.getLocalizedMessage());
             System.out.println("cause " + me.getCause());
             System.out.println("excep " + me);
             me.printStackTrace();
             
         }
    	
    }

    /**
     * Se suscribe al topico indicado en la url del broker.
     */
    public void subscribe(String topic) {

        MemoryPersistence persistence = new MemoryPersistence();

        try {

            sampleClient = new MqttClient(brokerUrl, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);


            System.out.println("Conectandose al broker ubicado en la direccion: " + brokerUrl);
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
    		if(m.indexOf("ALERTA")!=-1)
    		{
    			try {
    				
    				//String[] myMensaje=m.split(";");

    				URL url = new URL("http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/inmuebles/Inmueble1/sensores/Sensor1/alarmas");
    				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    				conn.setDoOutput(true);
    				conn.setRequestMethod("POST");
    				conn.setRequestProperty("Content-Type", "application/json");
    				

    				String input = "{\"mensaje\":\""+m+"\"}";
    				

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

    			  } catch (MalformedURLException e) {

    				e.printStackTrace();

    			  } catch (IOException e) {

    				e.printStackTrace();

    			 }
    			

    		}
    	}
    }

}
