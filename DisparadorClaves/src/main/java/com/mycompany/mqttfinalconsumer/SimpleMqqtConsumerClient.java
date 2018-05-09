package com.mycompany.mqttfinalconsumer;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.*;
import org.bouncycastle.openssl.PEMReader;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

 */
/**
 *
 * @author s.guzmanm
 */
public class SimpleMqqtConsumerClient implements MqttCallback {

   private static final String brokerUrl = "tcp://172.24.41.200:8083";

	/**
	 * Id del cliente
	 */
	private static final String clientId = "Claves";

	private static final String clave = "Isis2503.";

	private static final String user = "microcontrolador";

	static final String CRT_FILE_PATH = "/mosquittoSantiago";
	static final String CA_FILE_PATH = "/m2mqtt_ca.crt";
	static final String CLIENT_CRT_FILE_PATH = "/m2mqtt_srv.crt";
	static final String CLIENT_KEY_FILE_PATH = "/m2mqtt_srv.key";
	static final String ROOT = "C:\\Users\\s.saenz11\\Desktop\\201810_01_loscache\\ssl";
	
	/**
	 * Nombre del topico
	 */
//	private static final String topico = "Yale.Hub1.UniRes1.Inmueble1.Sensor1.Escucho";
	private static final String topico = "unirest1.inmueble1.sensor1";
	//private static final String topico2 = "Yale.Hub1.UniRes1.Inmueble1.Sensor1.Escribo";
	private static final String topico2 = "unirest1.inmueble1.sensor1.config";
	
	private MqttClient sampleClient;

	private ArrayList<String> claves = new ArrayList<String>();

	private boolean termino = false;

	public static void main(String[] args) throws Exception {

		// leer archivo
		String fileData="";
		try {
			fileData = new String(Files.readAllBytes(Paths
					.get("the-file-name.txt")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block

		}

		SimpleMqqtConsumerClient mc = new SimpleMqqtConsumerClient();

		mc.darArregloJson(mc.getClaves(), fileData);

		mc.subscribe(topico2);
		mc.atender();

		ArrayList<String> arreglo = mc.getClaves();
		String json = new Gson().toJson(arreglo);
		PrintWriter writer;
		try {
			writer = new PrintWriter("the-file-name.txt", "UTF-8");
			writer.println(json);
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	static SSLSocketFactory getSocketFactory (final String caCrtFile, final String crtFile, final String keyFile, 
            final String password) throws Exception
{
Security.addProvider(new BouncyCastleProvider());
    System.out.println("lo logr{o");
// load CA certificate
PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
X509Certificate caCert = (X509Certificate)reader.readObject();
reader.close();
 System.out.println("lo logr{o");
// load client certificate
reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
X509Certificate cert = (X509Certificate)reader.readObject();
reader.close();
System.out.println("lo logr{o");
// load client private key
reader = new PEMReader(
new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))),
new PasswordFinder() {
public char[] getPassword() {
return password.toCharArray();
}
}
);
KeyPair key = (KeyPair)reader.readObject();
reader.close();

// CA certificate is used to authenticate server
KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
caKs.load(null, null);
caKs.setCertificateEntry("ca-certificate", caCert);
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(caKs);

// client key and certificates are sent to server so it can authenticate us
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, null);
ks.setCertificateEntry("certificate", cert);
ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, password.toCharArray());

// finally, create SSL socket factory
SSLContext context = SSLContext.getInstance("TLSv1.2");
context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

return context.getSocketFactory();
}
	

	/**
	 * Se suscribe al topico indicado en la url del broker.
	 * @throws Exception 
	 */
	public void subscribe(String topic) throws Exception {

		try {

			sampleClient = new MqttClient(brokerUrl, clientId);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			
			connOpts.setCleanSession(true);
			
			connOpts.setKeepAliveInterval(30);
            connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            connOpts.setUserName(user);
            connOpts.setPassword(clave.toCharArray());
            
            //socket factory
            System.out.println("lo logr{ol");
            SSLSocketFactory socketFactory = getSocketFactory(ROOT+CRT_FILE_PATH+CA_FILE_PATH, ROOT+CRT_FILE_PATH+CLIENT_CRT_FILE_PATH, ROOT+CRT_FILE_PATH+CLIENT_KEY_FILE_PATH, "");
            System.out.println("lo logr{ol");
            connOpts.setSocketFactory(socketFactory);
			System.out.println("lo logr{ol");
			
			sampleClient.connect(connOpts);
                        System.out.println("lo logr{ol");
			System.out.println("Conectado");
			sampleClient.setCallback(this);
			sampleClient.subscribe(topic);

			System.out.println("Bienvenido al sistema de gestion de claves");

		} catch (MqttException me) {

			System.out.println("Mqtt reason " + me.getReasonCode());
			System.out.println("Mqtt msg " + me.getMessage());
			System.out.println("Mqtt loc " + me.getLocalizedMessage());
			System.out.println("Mqtt cause " + me.getCause());
			System.out.println("Mqtt excep " + me);
		}
	}

	public void enviarMensaje(String content) throws MqttPersistenceException, MqttException {
		try {

			System.out.println("Publicando mensaje al topico: " + topico2);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(0);
			sampleClient.publish(topico2, message);
			System.out.println("sali");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();

		}
	}

	public void connectionLost(Throwable arg0) {
		arg0.printStackTrace();
		System.out.println("Conexion perdida");

	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		String m = message.toString();
		System.out.println(m);

		if (m.indexOf("CLAVES:") != -1) {
			String[] data = m.split(":");
			String clavesString = data[1];
			String[] clavez = clavesString.split(",");
			int numClaves = 0;
			for (int i = 0; i < clavez.length; i++) {
				claves.add(clavez[i]);
				numClaves++;
			}
			System.out.println("Se han recibido las claves de acceso");
			System.out.println("La cerradura actualmente posee " + numClaves + " claves");
			atender();
		}

	}

	public void darArregloJson(ArrayList<String> arreglo, String linea){
		
		if(linea.contains("[]"))
			linea = "";
		if(linea.equals("[]")||linea.equals("")) {
			
			return;
		}
		else {
			linea =linea.replace("[", "");
			linea =linea.replace("]", "");
			linea =linea.replace("\"", "");
			String[] arr = linea.split(",");
			for(int i = 0; i< arr.length; i++) {
				arreglo.add(new String(arr[i]));
				System.out.println(arreglo.get(i));
			}
		}

	}

	public void atender() throws MqttPersistenceException, MqttException {


		int id = 0;
		Scanner sc = new Scanner(System.in);

		while (!termino) {
			printMenu();
			String option = sc.next();
			if (option.equals("DeleteAll")) {
				System.out.println("Está seguro? (y/n)");
				String conf = sc.next();
				if (conf.equals("y")) {
					claves = new ArrayList<String>();
					String men = "DELETE_ALL_PASSWORDS";
					MqttMessage message = new MqttMessage(men.getBytes());
					message.setQos(0);
					sampleClient.publish(topico2, message);
				} else {
					break;
				}
			} 
			else if (option.contains("_")) {
				String[] datica = option.split("_");

				if (Integer.parseInt(datica[1]) == 1 && datica[0].equals("Add")==false) {
					int k = Integer.parseInt(datica[0]) - 1;
					boolean agregar = true;
					System.out.println("Cambiando la clave " + claves.get(k) + ". Ingrese la nueva clave con los cambios que desee");
					String nuev = sc.next();
					if (nuev.length() != 4) {
						System.out.println("Error, la clave debe ser de 4 digitos");
						agregar = false;
					}

					try {
						int test = Integer.parseInt(nuev);
					} catch (Exception e) {
						System.out.println("Error, la clave debe ser un numero de 4 digitos");
						agregar = false;
					}
					if (agregar) {
						claves.set(k, nuev);

						String men = "UPDATE_PASSWORD;"+(k+1)+";"+nuev;
						MqttMessage message = new MqttMessage(men.getBytes());
						message.setQos(0);
						sampleClient.publish(topico2, message);
					}
				}
				if (datica[0].equals("Add")) 
				{
					int id1 = Integer.parseInt(datica[1]);

					boolean vacio = idEsVacio(id1);
					if (claves.size() < 20&&id1>=1&&id1<=20&&vacio) {
						boolean agregar = true;
						System.out.println("Ingrese la clave de 4 digitos");
						String clave = sc.next();
						if (clave.length() != 4) {
							System.out.println("Error, la clave debe ser de 4 digitos");
							agregar = false;
						}

						try {
							int test = Integer.parseInt(clave);
						} catch (Exception e) {
							System.out.println("Error, la clave debe ser un numero de 4 digitos");
							agregar = false;
						}
						//mandar a persisitr en la entidad física
						if (agregar) {
							claves.add(id1-1,clave);
							String men = "ADD_PASSWORD;" +id1+";"+clave+"";
							MqttMessage message = new MqttMessage(men.getBytes());
							message.setQos(0);
							sampleClient.publish(topico2, message);
						}



					} else if (vacio==false)  {
						System.out.println("No puede agregarse una clave en un sitio existente, actualicela.");
					}

					else {
						System.out.println("No pueden agregarse mas de 20 claves.");
					}
				} 
				else if(Integer.parseInt(datica[1]) == 2&& datica[0].equals("Add")==false){
					int k = Integer.parseInt(datica[0]) - 1;
					System.out.println("Seguro que desea borrar la clave " + claves.get(k) + "? (y/n)");
					String conf = sc.next();
					if (conf.equals("y")) {
						String men = "DELETE_PASSWORD;"+claves.get(k);
						claves.remove(k);

						MqttMessage message = new MqttMessage(men.getBytes());
						message.setQos(0);
						sampleClient.publish(topico2, message);

					} 



				}    

				else if(Integer.parseInt(datica[1]) == 3&& datica[0].equals("Add")==false){
				
					int k = Integer.parseInt(datica[0]) - 1;
						String men = "COMPARE_PASSWORD;"+claves.get(k);
						MqttMessage message = new MqttMessage(men.getBytes());
						message.setQos(0);
						sampleClient.publish(topico2, message);

//						if(buscarCla(nuev)==false)
//						{System.out.println("La clave que se intenta añadir no existe.");
//						}
//						else {
//							System.out.println("La clave existe");
//						}
					}
				} 


			else if (option.equals("Fin")) {
				// System.out.println("Está seguro que desea enviar los cambios? (y/n)");
				//                String conf = sc.next();
				//                if (conf.equals("y")) {
				//                    String cont = "CLAVES:";
				//                    for (int j = 0; j < claves.size(); j++) {
				//                        cont += claves.get(j) + ",";
				//
				//                    }
				//                    String contenido = cont.substring(0, cont.length() - 1);
				//                    enviarMensaje(contenido);
				System.out.println("Se ha enviado " + "contenido" + " al microcontrolador");
				termino = true;
				sampleClient.disconnect();
				sampleClient.close();
			} else {
				System.out.println("Ingreso una opción que no existe");

			}
			}
	


}

	

	private boolean buscarCla(String nuev) 
	{
		boolean variable=false;

		for(String s:claves)
		{
			if(Integer.parseInt(s)==Integer.parseInt(nuev))
			{
				variable=true;
				break;
			}
		}

		return variable;
	}

	private boolean idEsVacio(int id) 
	{

		if(claves.isEmpty())
		{
			return true;
		}
		try
		{
			if(claves.get(id-1)!=null)
				return false;
		}
		catch (Exception e)
		{
			return true;
		}
		return false;
	}

	public void printMenu() {
		System.out.println("Menu de manejo de claves");
		System.out.println("---------------------------------------------------------------------------------------------");
		System.out.println("Add_"+ "id - Ingresar una nueva clave en la posición deseada. No es posible si hay 20 claves.");
		System.out.println("Las claves se deben agregar en orden numérico No es posible si hay 20 claves.");
		System.out.println("DeleteAll  - Borra todas las claves.");
		for (int i = 0; i < claves.size(); i++) {
			int x = i + 1;
			
			System.out.println(x + "_1 - Editar la clave: " + claves.get(i));
			System.out.println(x + "_2 - Borrar la clave: " + claves.get(i));
			System.out.println(x + "_3 - Comparar clave: " + claves.get(i));
		}
		System.out.println("Fin - Enviar cambios");
		System.out.println("---------------------------------------------------------------------------------------------");
		System.out.println("Para ejecutar una función, ingrese la opción correspondiente y presione Enter (ej: \"1_2\" + enter)");
	}

	public ArrayList<String> getClaves() {
		return claves;
	}

	public void setClaves(ArrayList<String> claves) {
		this.claves = claves;
	}

}
