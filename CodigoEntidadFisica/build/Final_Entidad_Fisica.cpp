#include <Keypad.h>


/*
*  KEYPAD
 */
//Specified password
#include "WProgram.h"
void setup();
void loop();
void setColor(int redValue, int greenValue, int blueValue);
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



/*
 * PIR sensor tester
 */

int ledPin = 15;              // choose the pin for the LED
int pirPin = 14;             // choose the input pin (for PIR sensor)
int pirValue;                   // variable for reading the pin status
int pirState = LOW;


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


