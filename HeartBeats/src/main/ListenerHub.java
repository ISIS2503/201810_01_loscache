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
public class ListenerHub implements MqttCallback {

    /** Url del mosquitto */
    private static final String brokerUrl = "tcp://172.24.41.200:8083";

    /** Id del cliente */
    private static final String clientId = "HealthChecksHub";

    /** Nombre del topico */
    private static final String topicoEntrada = "hub1";
    
    private static Date ultimoHB=new Date();

    private static int perdidos;

   
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
                   		SendMail.enviar("Hub1");
                   	};
                   }
               }, 0, 10, TimeUnit.SECONDS);
        new ListenerHub().subscribe(topicoEntrada);
        
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
    		SendMail.enviar("Hub1");
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

}
