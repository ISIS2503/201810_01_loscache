#include <Keypad.h>
#include <EEPROM.h>
#include <string.h>

/*
  FINAL
*/
#define SIZE_BUFFER_DATA       50
boolean     stringComplete = false;
String      inputString = "";
char        bufferData [SIZE_BUFFER_DATA];

String firstVal = "";
String secondVal = "";
boolean validEntry;
boolean validateEntry;
boolean registroUnaVez;



const String ADD_PASSWORD = "ADD_PASSWORD";
const String UPDATE_PASSWORD = "UPDATE_PASSWORD";
const String DELETE_PASSWORD = "DELETE_PASSWORD";
const String DELETE_ALL_PASSWORDS = "DELETE_ALL_PASSWORDS";
const String COMPARE_PASSWORD = "COMPARE_PASSWORD";
const String VALID_ENTRY = "VALID_ENTRY";
const String Y = "Y";

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

boolean keyRegistered;


/*
   CONTACT
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
long timeTillHeartBeat;


/*
   PIR sensor tester
*/

int ledPin = 15;              // choose the pin for the LED
int pirPin = 14;             // choose the input pin (for PIR sensor)
int pirValue;                   // variable for reading the pin status
int pirState = LOW;



/*
   BATERRY
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
  BUZZER
*/

//BUZZER pin
const int BUZZER_PIN = 17;

boolean sound = false;

/*
  SET UP
*/



void setup() {
  /* KEYPAD */
  currentKey = "";
  open = false;
  attempts = 0;
  block = false;

  /* CONTACT */
  buttonState = false;

  pinMode(R_LED_PIN, OUTPUT);
  pinMode(G_LED_PIN, OUTPUT);
  pinMode(B_LED_PIN, OUTPUT);
  pinMode(CONTACT_PIN, INPUT);

  setColor(0, 0, 255);

  /* PIR */


  pinMode(ledPin, OUTPUT);      // declare LED as output
  pinMode(pirPin, INPUT);     // declare sensor as input


  /*
    BATTERY
  */
  // Ouput pin definition for BATTERY_LED
  pinMode(BATTERY_LED, OUTPUT);

  //Input pin definition for battery measure
  pinMode(BATTERY_PIN, INPUT);

  /*
    BUZZER
  */

  pinMode(BUZZER_PIN, OUTPUT);
  timeTillHeartBeat = millis();

  Serial.begin(9600);
}



/*
  LOOP
*/



void loop() {

  /* KEYPAD */
  char customKey;
  if ((millis() - timeTillHeartBeat) >= 5000)
  {
    Serial.println("HB");
    timeTillHeartBeat = millis();
  }

  if (!block) {
    //Selected key parsed;
    customKey = customKeypad.getKey();
    //Serial.println(currentKey);
  }
  else {
    //Serial.println("Number of attempts exceeded");
    while (true);
  }

  //Verification of input and appended value
  if (customKey && currentKey.length() <=3) {
    currentKey += String(customKey);
    Serial.println(currentKey);
  }
   if (currentKey.length() == 4) {
      if (compareKey(currentKey)) {
        currTime = millis();
      }
    }

  //If the current key contains '*' and door is open
  if (openByKey && currentKey.endsWith("*")) {
    setColor(0, 0, 255);
    open = false;
    openByKey = false;
    //Serial.println("Door closed");
    //digitalWrite(10,LOW);
    currentKey = "";
	keyRegistered = false;
  }
  //If the current key contains '#' reset attempt
  if (currentKey.endsWith("#") && currentKey.length() <= 4) {
    currentKey = "";
    Serial.println("Attempt deleted");
  }

  //If current key matches the key length and is saved at EEPROM
  if (compareKey(currentKey) && currentKey.length() == 4 && !registroUnaVez) {
    Serial.print("VALID_ENTRY;");
    Serial.println(currentKey);
    keyRegistered = true;
    registroUnaVez= true;
  }
  else if (validEntry && keyRegistered) {
    open = true;
    openByKey = true;
    //Serial.println("Door opened!!");
    setColor(0, 255, 0);
    attempts = 0;
  }
  else if (!compareKey(currentKey) && currentKey.length() == 4) {
    attempts++;

    if (attempts != 3)
    {
      setColor(255, 0, 0);
      delay(1000);
      setColor(0, 0, 255);
    }

    currentKey = "";
    //Serial.println("Number of attempts: "+String(attempts));
  }
  if (attempts >= maxAttempts) {
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
  if (!buttonState) {
    if (digitalRead(CONTACT_PIN) && !openByKey) {
      currTime = millis();
      buttonState = true;
      setColor(0, 255, 0);
      open = true;
      attempts = 0;
      //Serial.println("Door opened!!");
    }
  }
  else {
    if (digitalRead(CONTACT_PIN)) {
      if ((millis() - currTime) >= 30000) {
        setColor(255, 0, 0);
        Serial.println("Door opened too much time");
      }
    }
    else {

      setColor(0, 0, 255);
      open = false;
      buttonState = false;
      //Serial.println("Door closed!!");
    }
  }
  if (openByKey) {
    if ((millis() - currTime) >= 30000) {
      setColor(255, 0, 0);
      Serial.println("Door opened too much time");
    }
  }

  /* PIR */

  pirValue = digitalRead(pirPin);
  digitalWrite(ledPin, pirValue);
  if (pirValue == HIGH)
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
    BATTERY
  */
  //Value conversion from digital to voltage
  batteryCharge = (analogRead(BATTERY_PIN) * 5.4) / 1024;

  //Measured value comparison with min voltage required
  if (batteryCharge <= MIN_VOLTAGE ) {
    //Serial.println((millis()-currTimeLowBattery));

    if (!lowBattery) {

      currTimeLowBattery = millis();
      lowBattery = true;
      digitalWrite(BATTERY_LED, HIGH);
      Serial.println("LOW BATTERY");
      delay(200);
    }
  }
  else {
    digitalWrite(BATTERY_LED, LOW);
    lowBattery = false;
    analogWrite(BUZZER_PIN, 0);
    sound = false;
  }

  if ((millis() - currTimeLowBattery) >= 5000 && !sound && lowBattery) {
    analogWrite(BUZZER_PIN, 255);
    currTimeSound = millis();
    sound = true;
  }
  if ((millis() - currTimeSound) >= 2000 && sound && lowBattery)
  {
    analogWrite(BUZZER_PIN, 0);
    currTimeSound = millis();
    sound = false;
    currTimeLowBattery = millis();
  }
  receiveData();
  processData();
}

/*
  CONTACT METHOD
*/

//Method that outputs the RGB specified color
void setColor(int redValue, int greenValue, int blueValue) {
  analogWrite(R_LED_PIN, redValue);
  analogWrite(G_LED_PIN, greenValue);
  analogWrite(B_LED_PIN, blueValue);
}

// Method that compares a key with stored keys
boolean compareKey(String key) {
  //Serial.println("Se comparará la clave " + key);
  int acc = 3;
  int codif, arg0, arg1;
  for (int i = 0; i < 3; i++) {
    codif = EEPROM.read(i);
    while (codif != 0) {
      if (codif % 2 == 1) {
        arg0 = EEPROM.read(acc);
        arg1 = EEPROM.read(acc + 1) * 256;
        arg1 += arg0;
        if (String(arg1) == key) {
          return true;
        }
      }
      acc += 2;
      codif >>= 1;
    }
    acc = (i + 1) * 16 + 3;
  }
  return false;
}

// Methods that divides the command by parameters
void processCommand(String input) {
  boolean band = false;
  for (int i = 0; i < input.length(); i++) {
    if (input.substring(i, i + 1) == ";") {
      firstVal = input.substring(0, i);
      secondVal = input.substring(i + 1);
      band = true;
      break;
    }
  }

}

//Method that adds a password in the specified index
void addPassword(int val, int index) {
  Serial.println("Se agregará la clave");
  byte arg0 = val % 256;
  byte arg1 = val / 256;
  EEPROM.write((index * 2) + 3, arg0);
  EEPROM.write((index * 2) + 4, arg1);
  byte i = 1;
  byte location = index / 8;
  byte position = index % 8;
  i <<= position;
  byte j = EEPROM.read(location);
  j |= i;
  EEPROM.write(location, j);
}

//Method that updates a password in the specified index
void updatePassword(int val, int index) {
  Serial.println("Se actualizará la clave");
  byte arg0 = val % 256;
  byte arg1 = val / 256;
  EEPROM.write((index * 2) + 3, arg0);
  EEPROM.write((index * 2) + 4, arg1);
}

//Method that deletes a password in the specified index
void deletePassword(int index) {
  char s[4];//nice and big so no buffer overflow
  sprintf(s, "%d", index);
  Serial.println("Se borrará la clave");
  byte i = 1;
  byte location = index / 8;
  byte position = index % 8;
  i <<= position;
  byte j = EEPROM.read(location);
  j ^= i;
  EEPROM.write(location, j);
}

//Method that deletes all passwords
void deleteAllPasswords() {
  //Password reference to inactive
  Serial.println("Se borrarán todas las claves");
  EEPROM.write(0, 0);
  EEPROM.write(1, 0);
  EEPROM.write(2, 0);
}

/*
  METHODS FINAL
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
    processCommand(inputString);
    //Serial.println(firstVal);

    if (firstVal == ADD_PASSWORD)
    {
      //Formato:"ADD_PASSWORD;<idPassword>;<password>"

      processCommand(secondVal);
      addPassword(secondVal.toInt(), firstVal.toInt());
    }
    else if (firstVal == UPDATE_PASSWORD)
    {
      //Formato:"UPDATE_PASSWORD;<idPassword>;<password>"
      processCommand(secondVal);
      updatePassword(secondVal.toInt(), firstVal.toInt());
    }
    else if (firstVal == DELETE_PASSWORD)
    {
      //Formato:"DELETE_PASSWORD;<id>"
      deletePassword(secondVal.toInt());
    }
    else if (firstVal == DELETE_ALL_PASSWORDS)
    {
      //Formato:"DELETE_ALL_PASSWORDS"
      deleteAllPasswords();
    }
    else if (firstVal == COMPARE_PASSWORD)
    {
      //Formato:"COMPARE_PASSWORD;<password>"
      processCommand(secondVal);
      compareKey(firstVal);
    }
    else if (firstVal == VALID_ENTRY)
    {
      Serial.println("Se valida entrada");
      Serial.println(secondVal);
      if (secondVal == Y)
      {
        validEntry = true;
      }
      else if (secondVal == "N")
      {
        validEntry = false;
      }
      Serial.println(validEntry);
      validateEntry = true;
      registroUnaVez = false;
    }
    inputString = "";
    stringComplete = false;
  }
}
