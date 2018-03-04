
package main;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
/**
 * Este programa se apoya en el código realizado por el usuario m2mlO-gister, ubicado en el repositorio https://gist.github.com/m2mIO-gister/5275324
 * @author Los cache
 *
 */
public class Listener implements MqttCallback {

    /** Url del mosquitto */
    private static final String brokerUrl = "tcp://172.24.42.43:8083";

    /** Id del cliente */
    private static final String clientId = "Consumidor";

    /** Nombre del topico */
    private static final String topico = "Alertas";

   
    public static void main(String[] args) {

        new Listener().subscribe(topico);
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
    	System.out.println("Conexion perdida");

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }
    
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    	if(message.toString().equals(""))
    		return;
    	else {
        System.out.println("Topico: " + topic);
        System.out.println("Mensaje: " + message.toString());}
    }

}