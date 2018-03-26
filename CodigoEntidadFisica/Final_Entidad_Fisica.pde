#include <Keypad.h>

/*
*FINAL
*/
#define SIZE_BUFFER_DATA       50
boolean     stringComplete = false;
String      inputString = "";
char        bufferData [SIZE_BUFFER_DATA];

/*
*  KEYPAD
 */
//Specified password
const String KEY1 = "1234";
const String KEY2 = "9876";
const String KEY3 = "4567";
const String KEY4 = "7890";

//Time in milliseconds which the system is locked
const int LOCK_TIME = 30000;

//Keypad rows
const byte ROWS = 4; 

//Keypad columns
const byte COLS = 3;

//Maximum number of attempts allowed
const byte maxAttempts = 3;

//Keypad mapping matrix
char hexaKeys[ROWS][COLS] = {
  {
    '1', '2', '3'
  }
  ,
  {
    '4', '5', '6'
  }
  ,
  {
    '7', '8', '9'
  }
  ,
  {
    '*', '0', '#'
  }
};

//Keypad row pins definition
byte rowPins[ROWS] = {
  9, 8, 7, 6
}; 

//Keypad column pins definition
byte colPins[COLS] = {
  5, 4, 3
};

//Keypad library initialization
Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS); 

//Current key variable
String currentKey;
//Door state
boolean open;


boolean openByKey;
//Number of current attempts
byte attempts;
//If the number of current attempts exceeds the maximum allowed
boolean block;



/*
*  CONTACT
 */
//Button pin
const int CONTACT_PIN = 11;

//R LED pin
const int R_LED_PIN = 13;

//G LED pin
const int G_LED_PIN = 12;

//B LED pin
const int B_LED_PIN = 10;


//Attribute that defines the button state
boolean buttonState;

//Current time when the button is tapped
long currTime;
long currTimeLowBattery;
long currTimeSound;


/*
 * PIR sensor tester
 */

int ledPin = 15;              // choose the pin for the LED
int pirPin = 14;             // choose the input pin (for PIR sensor)
int pirValue;                   // variable for reading the pin status
int pirState = LOW;



/*
*  BATERRY
 */

//Minimum voltage required for an alert
const double MIN_VOLTAGE = 1.2;

//Battery measure pin
const int BATTERY_PIN = 24;

//Battery indicator
const int BATTERY_LED = 16;

//Current battery charge
double batteryCharge;

//Low battery
boolean lowBattery = false;

/*
* BUZZER
 */

//BUZZER pin
const int BUZZER_PIN = 17;

boolean sound = false;

/*
* SET UP
 */



void setup() {
  /* KEYPAD */
  Serial.begin(9600);
  currentKey = "";
  open = false;
  attempts = 0;
  block = false;

  /* CONTACT */
  Serial.begin(9600);
  buttonState = false;

  pinMode(R_LED_PIN, OUTPUT);
  pinMode(G_LED_PIN, OUTPUT);
  pinMode(B_LED_PIN, OUTPUT);
  pinMode(CONTACT_PIN,INPUT);

  setColor(0, 0, 255);

  /* PIR */


  pinMode(ledPin, OUTPUT);      // declare LED as output
  pinMode(pirPin, INPUT);     // declare sensor as input


  /*
  * BATTERY
   */
  // Ouput pin definition for BATTERY_LED
  pinMode(BATTERY_LED,OUTPUT);

  //Input pin definition for battery measure
  pinMode(BATTERY_PIN,INPUT);

  /*
  * BUZZER
   */

  pinMode(BUZZER_PIN,OUTPUT);

  Serial.begin(9600);
}



/*
* LOOP
 */



void loop() {

  /* KEYPAD */
  char customKey;

  if(!block) {
    //Selected key parsed;
    customKey = customKeypad.getKey();
  }
  else {
    Serial.println("Number of attempts exceeded");
    while(true);
  }

  //Verification of input and appended value
  if (customKey) {  
    currentKey+=String(customKey);
    Serial.println(currentKey);

    if (currentKey.length()== KEY1.length()) {
      if(currentKey == KEY1 || currentKey == KEY2 ||currentKey == KEY3 ||currentKey == KEY4) {
        currTime = millis();
      }
    }
  }

  //If the current key contains '*' and door is open
  if(openByKey && currentKey.endsWith("*")) {
    setColor(0, 0, 255);
    open = false;
    openByKey = false;
    Serial.println("Door closed");
    //digitalWrite(10,LOW);
    currentKey = "";
  }
  //If the current key contains '#' reset attempt
  if(currentKey.endsWith("#")&&currentKey.length()<=KEY1.length()) {
    currentKey = "";
    Serial.println("Attempt deleted");
  }

  //If current key matches the key length
  if (currentKey.length()== KEY1.length()) {
    if(currentKey == KEY1 || currentKey == KEY2 ||currentKey == KEY3 ||currentKey == KEY4) {
      open = true;
      openByKey = true;
      Serial.println("Door opened!!");
      setColor(0, 255, 0);      
      attempts = 0;
    }
    else {
      attempts++; 

      if(attempts != 3)
      {     
        setColor(255, 0, 0);
        delay(1000);
        setColor(0, 0, 255);
      }

      currentKey = "";
      Serial.println("Number of attempts: "+String(attempts));
    }
  }
  else if(currentKey.length()> KEY1.length()) {
    setColor(0, 255, 0);    
    Serial.println("Door opened!!");
  }
  if(attempts>=maxAttempts) {
    currentKey = "";
    attempts = 0;
    Serial.println("System locked");
    setColor(255, 0, 0);
    delay(LOCK_TIME);
    Serial.println("System unlocked");
    setColor(0, 0, 255);
  }

  /* CONTACT */
  //Button input read and processing 
  if(!buttonState) {
    if(digitalRead(CONTACT_PIN) && !openByKey) {
      currTime = millis();
      buttonState = true;
      setColor(0, 255, 0);
      open = true;
      attempts = 0;
      Serial.println("Door opened!!");
    }
  }
  else {
    if(digitalRead(CONTACT_PIN)) {
      if((millis()-currTime)>=30000) {
        setColor(255, 0, 0);
        Serial.println("Door opened too much time");
      }
    }
    else {

      setColor(0, 0, 255);
      open = false;
      buttonState = false;
      Serial.println("Door closed!!");
    }
  }
  if(openByKey) {
    if((millis()-currTime)>=30000) {
      setColor(255, 0, 0);
      Serial.println("Door opened too much time");
    }
  }

  /* PIR */

  pirValue = digitalRead(pirPin);
  digitalWrite(ledPin, pirValue);
  if(pirValue == HIGH)
  {
    if (pirState == LOW) {
      // we have just turned on
      Serial.println("Motion detected");
      // We only want to print on the output change, not state
      pirState = HIGH;
    }
    delay(100);
  }
  else
  {
    if (pirState == HIGH) {
      // we have just turned of
      Serial.println("Motion ended");
      // We only want to print on the output change, not state
      pirState = LOW;
    }
  }

  /*
  * BATTERY
   */
  //Value conversion from digital to voltage
  batteryCharge = (analogRead(BATTERY_PIN)*5.4)/1024;

  //Measured value comparison with min voltage required
  if(batteryCharge<=MIN_VOLTAGE ) {
    Serial.println((millis()-currTimeLowBattery));

    if(!lowBattery) {

      currTimeLowBattery = millis(); 
      lowBattery = true;
      digitalWrite(BATTERY_LED,HIGH);
      Serial.println("LOW BATTERY");
      delay(200);
    }
  }
  else {
    digitalWrite(BATTERY_LED,LOW);
    lowBattery = false;
    analogWrite(BUZZER_PIN, 0);  
    sound = false;
  }

  if((millis()-currTimeLowBattery)>=5000 && !sound && lowBattery) {
    analogWrite(BUZZER_PIN, 255);  
    currTimeSound = millis();  
    sound =true;
  }
  if((millis()-currTimeSound)>=2000 && sound && lowBattery)
  {  
    analogWrite(BUZZER_PIN, 0);  
    currTimeSound = millis();
    sound = false;      
    currTimeLowBattery = millis();
  }
}

/*
* CONTACT METHOD
 */

//Method that outputs the RGB specified color
void setColor(int redValue, int greenValue, int blueValue) {
  analogWrite(R_LED_PIN, redValue);
  analogWrite(G_LED_PIN, greenValue);
  analogWrite(B_LED_PIN, blueValue);
}

/*
* METHODS FINAL
*/

void receiveData() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    if (inChar == '\n') {
      inputString.toCharArray(bufferData, SIZE_BUFFER_DATA);
      stringComplete = true;
    }
  }
}
 
void processData() {
  if (stringComplete) {
    // Implementación...
  }
}

