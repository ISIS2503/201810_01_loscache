#include <PubSubClient.h>
#include <ESP8266WiFi.h>

//DEFINES
#define SUBSCRIBE1        "unires1.inmueble1.sensor.config"
#define PUBLISH1          "unires1.inmueble1.sensor"

#define SUBSCRIBE2        "unires2.inmueble2.sensor.config"
#define PUBLISH2          "unires2.inmueble2.sensor"

#define SUBSCRIBE3        "unires3.inmueble3.sensor.config"
#define PUBLISH3          "unires3.inmueble3.sensor"

#define SUBSCRIBE4        "unires4.inmueble4.sensor.config"
#define PUBLISH4          "unires4.inmueble4.sensor"

#define SIZE_BUFFER_DATA       50

//VARIABLES

#define SUBSCRIBE0  "unires0.inmueble0.sensor.config"
#define PUBLISH0 "unires0.inmueble0.sensor"


int alarma = 0;

const char* idDevice = "ISIS2503";
boolean     stringComplete = false;
boolean     init_flag = false;
String      inputString = "";
char        bufferData [SIZE_BUFFER_DATA];

// CLIENTE WIFI & MQTT`
WiFiClient    clientWIFI;
PubSubClient  clientMQTT(clientWIFI);

// CONFIG WIFI
const char* ssid = "isis2503";
const char* password = "Yale2018.";

// CONFIG MQTT
IPAddress serverMQTT (172,24,44,76);
const uint16_t portMQTT = 8083;
// const char* usernameMQTT = "admin";
// const char* passwordMQTT = "admin";

void connectWIFI() {
  // Conectar a la red WiFi
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  if(WiFi.status() != WL_CONNECTED) {
    WiFi.begin(ssid, password);
  }

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println(WiFi.localIP());
}

void reconnectWIFI() {
  // Conectar a la red WiFi
  if(WiFi.status() != WL_CONNECTED) {
    WiFi.begin(ssid, password);
  }

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.write(payload, length);
  Serial.println();
}

void setup() {
  Serial.begin(9600);
  inputString.reserve(100);

  clientMQTT.setServer(serverMQTT, portMQTT);
  clientMQTT.setCallback(callback);
  connectWIFI();
  delay(1000);
}

void processData() {
  if (WiFi.status() == WL_CONNECTED) {
    if(init_flag == false) {
      init_flag = true;

      boolean conectMQTT = false;
      if (!clientMQTT.connected()) {
        // if (!clientMQTT.connect(idDevice, usernameMQTT, passwordMQTT)) {
        if (!clientMQTT.connect(idDevice)) {
          conectMQTT = false;
        }
        conectMQTT = true;
      }
      else {
        conectMQTT = true;
      }

      if(conectMQTT) {
        if(alarma==0){ 
          if(clientMQTT.subscribe(SUBSCRIBE0)) {
           Serial.println("Subscribe OK");
          }
        }else if(alarma==1){
          if(clientMQTT.subscribe(SUBSCRIBE1)) {
           Serial.println("Subscribe OK");
          }
        }else if(alarma==2){
          if(clientMQTT.subscribe(SUBSCRIBE2)) {
           Serial.println("Subscribe OK");
          }
        }else if(alarma==3){
          if(clientMQTT.subscribe(SUBSCRIBE3)) {
           Serial.println("Subscribe OK");
          }
        }else if(alarma==4){
          if(clientMQTT.subscribe(SUBSCRIBE4)) {
           Serial.println("Subscribe OK");
          }
        }
      }
    }

    if (stringComplete && clientMQTT.connected()) {
      if(alarma==0){ 
          if(clientMQTT.publish(PUBLISH0, bufferData)) {
            inputString = "";
            stringComplete = false;
          }
        }else if(alarma==1){
          if(clientMQTT.publish(PUBLISH1, bufferData)) {
            inputString = "";
            stringComplete = false;
          }
        }else if(alarma==2){
          if(clientMQTT.publish(PUBLISH2, bufferData)) {
            inputString = "";
            stringComplete = false;
          }
        }else if(alarma==3){
          if(clientMQTT.publish(PUBLISH3, bufferData)) {
            inputString = "";
            stringComplete = false;
          }
        }else if(alarma==4){
          if(clientMQTT.publish(PUBLISH4, bufferData)) {
            inputString = "";
            stringComplete = false;
          }
        }
      init_flag = false;
    }
  }
  else {
    connectWIFI();
    init_flag = false;
  }
  clientMQTT.loop();
}

void receiveData() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      inputString.toCharArray(bufferData, SIZE_BUFFER_DATA);
      stringComplete = true;
    }
  }
}

void loop() {
  receiveData();
  processData();
}
