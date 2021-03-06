
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

public class Listener implements MqttCallback {

    
    private static final String brokerUrl = "tcp://172.24.41.200:8083";
    
    private static final String clientId = "Consumidor";

    private static final String topicoEntrada = "unirest1.inmueble1.sensor1";
  

    
    private int numMensajes=0;
    
    private int tiempoTotal=0;

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
    	
    	
        new Listener().subscribe(topicoEntrada);
    }

    /**
     * Subscribe.
     *
     * @param topic
     *            the topic
     */
    public void subscribe(String topic) {

        MemoryPersistence persistence = new MemoryPersistence();

        try {

            MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("checking");

            System.out.println("Conectandose al broker ubicado en: " + brokerUrl);
            sampleClient.connect(connOpts);
            System.out.println("Conexion establecida");

            sampleClient.setCallback(this);
            sampleClient.subscribe(topic);

            System.out.println("Se esta escuchando");

        } catch (MqttException me) {

            System.out.println("Mqtt reason " + me.getReasonCode());
            System.out.println("Mqtt msg " + me.getMessage());
            System.out.println("Mqtt loc " + me.getLocalizedMessage());
            System.out.println("Mqtt cause " + me.getCause());
            System.out.println("Mqtt excep " + me);
        }
    }

    
    public void connectionLost(Throwable arg0) {
    	System.out.println("Conexion perdida");
    	arg0.printStackTrace();

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }
    
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        System.out.println("Mqtt topic : " + topic);
        System.out.println("Mqtt msg : " + message.toString());
        if(message.toString().indexOf("ALERTA")!=-1)
        	invocarPost(message);
    }
    
    public void invocarPost(MqttMessage me) throws Exception{
   	 
    		 numMensajes++;
    		 
    		 String m=me.toString();
    		 
    		 if(m.indexOf("ALERTA")!=-1)
     		{
    			 SendMail.enviar(m);


    			URL url = new URL("http://172.24.42.84:8085/correos");
    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			conn.setDoOutput(true);
    			conn.setRequestMethod("POST");
    			conn.setRequestProperty("Content-Type", "application/json");

    			String input = "{\"correo\":\"correouniandes\",\"mensaje\":\""+m+"\"}";

    			OutputStream os = conn.getOutputStream();
    			os.write(input.getBytes());
    			os.flush();
    			
    			Long a=System.currentTimeMillis();

    			if (conn.getResponseCode() != 200) {
    				throw new RuntimeException("Failed : HTTP error code : "
    					+ conn.getResponseCode());
    			}
    			
    			Long b=System.currentTimeMillis();
    			
    			Long c=b-a;
    			tiempoTotal+=c;
    			int promedio=tiempoTotal/numMensajes;

    			System.out.println("Tiempo promedio a "+numMensajes+" mensajes es : "+promedio);

    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					(conn.getInputStream())));

    			String output;
    			System.out.println("Output from Server .... \n");
    			while ((output = br.readLine()) != null) {
    				System.out.println(output);
    			}
    			conn.disconnect();


     		}
    }


}
