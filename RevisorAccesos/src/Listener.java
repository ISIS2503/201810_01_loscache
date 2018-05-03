

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Este programa se apoya en el código realizado por el usuario m2mlO-gister, ubicado en el repositorio https://gist.github.com/m2mIO-gister/5275324
 * @author Los cache
 *
 */
public class Listener implements MqttCallback {

    /** Url del mosquitto */
    private static final String brokerUrl = "tcp://172.24.41.200:8083";

    /** Id del cliente */
    private static final String clientId = "Accesos";

    /** Nombre del topico */
    private static final String topicoEntrada = "unirest1.inmueble1.sensor1";
    
    private static final String topicoSalida = "unirest1.inmueble1.sensor1.config";

    
    private String sessionToken;
    
    private MqttClient sampleClient;

   
    public static void main(String[] args) throws Exception {

        new Listener().subscribe(topicoEntrada);
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
    		if(m.indexOf("VALID_ENTRY")!=-1)
    		{
    			try {
    				
    				String[] myMensaje=m.split(";");
    				pruebita(myMensaje[1]);

    			  } catch (MalformedURLException e) {

    				e.printStackTrace();

    			  } catch (IOException e) {

    				e.printStackTrace();

    			 }
    			

    		}
    	}
    }
    
    public void revisarAcceso() throws Exception
    {
    	if(sessionToken==null) {
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
    
    public void pruebita(String m) throws Exception
    {
    	revisarAcceso();
    	URL url = new URL("http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/inmuebles/Inmueble1/sensores/Sensor1/Horarios");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer "+sessionToken);

		if (conn.getResponseCode() != 200 && conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		
			output=br.readLine();	
		System.out.println(output);
		
		Gson gson=new Gson();
		Horario horarios[]=gson.fromJson(output, Horario[].class);
		System.out.println(horarios.length);
		
    	url = new URL("http://172.24.42.43:8080/Yale/hubs/Hub1/unidadesResidenciales/UniRes1/inmuebles/Inmueble1/sensores/Sensor1/Claves/"+m);
		HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();
		conn2.setDoOutput(true);
		conn2.setRequestMethod("GET");
		conn2.setRequestProperty("Content-Type", "application/json");
		conn2.setRequestProperty("Authorization", "Bearer "+sessionToken);

		if (conn2.getResponseCode() != 200 && conn2.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn2.getResponseCode());
		}

		BufferedReader br2 = new BufferedReader(new InputStreamReader((conn2.getInputStream())));

		String output2;
		System.out.println("Output from Server .... \n");
		output2=br2.readLine();	
		System.out.println(output2);
		
		Gson gson2=new Gson();
		Clave clave=gson2.fromJson(output2, Clave.class);
		System.out.println(clave.getClave());
		String dueno=clave.getDueno();
		boolean entra=false;
		
		for(Horario h:horarios)
		{
			if(h.getDueno().equals(dueno))
			{
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				Date hinicial=df.parse(h.getHoraInicial());
				Date hfinal=df.parse(h.getHoraFinal());
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();		
				String ahorita=dateFormat.format(date).substring(11, 16);
				//System.out.println(ahorita);
				Date now=df.parse(ahorita);
				if(hinicial.compareTo(now)<0&&hfinal.compareTo(now)>0)
				{
					entra=true;
				}

			}
		}
		
		if(entra)
			enviarMensaje("VALID_ENTRY;SI");
		else
			enviarMensaje("VALID_ENTRY;NO");	
		

		conn.disconnect();
    }
    
    public void enviarMensaje(String content)
    {
    	 try {            
            
             System.out.println("Publicando mensaje al topico: " + topicoSalida);
             MqttMessage message = new MqttMessage(content.getBytes());
             message.setQos(0);
             sampleClient.publish(topicoSalida, message);
             System.out.println("Message published");
                         
         } catch (MqttException me) {
             System.out.println("reason " + me.getReasonCode());
             System.out.println("msg " + me.getMessage());
             System.out.println("loc " + me.getLocalizedMessage());
             System.out.println("cause " + me.getCause());
             System.out.println("excep " + me);
             me.printStackTrace();
             
         }
    	
    }

}
