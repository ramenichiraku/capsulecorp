#include <IFrameSlave.h>
#include <SerialFrameSlave/SerialFrameSlave.h>

/* commands and events interface */
/* EVENTS: communication from slave to master */
const static int EVENTO_TEMPERATURA = 0;
const static int EVENTO_LUZ         = 1;
const static int EVENTO_HUMEDAD     = 2;

/* COMMANDS: communication from master to slave */
const static int COMMAND_LED          = 0;


/*
IMPORTANT: In this example, this is the slave source intended to be executed into an Arduino.

  Slave:   Arduino microcontroller running this code.
  Master:  Host running the java interface as a master.

The COMMAND_LED is able to swith ON/OF a local led, remotely.
The EVENTS temperature, light and humidity are related to local sensors, and an event is sent to the host when changed.

*/


// Local sensors to read and send to host
//
static int temperaturePin = 1;
static int humidityPin    = 2;
static int lightPin       = 3;

// Local variables to store current data
//
static int temperatureData = 0;
static int humidityData = 0;
static int lightData = 0;

// Led pin controlled from host
//
static int ledPin = 13;

// Create the SerialFrame object, and suscribe the andale method used as a callback.
//
void andale (int,int);
SerialFrameSlave frame_ (andale);

// Arduino setup configurations
//
void setup ()
{
  // Configure pins
  //
    /* ATENTION: Analog pins already configured */
  pinMode(ledPin, OUTPUT);
  
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
  }; // wait for serial port to connect. Needed for native USB port only
  
  // Not necessary because already initialized in constructor
  //
//  frame_.setOnCommand (andale);
}

// Arduino loop
//
void loop ()
{
  // Protocol checks
  //
  frame_.checkCommands(); 
 
  // Read sensors
  //
  temperatureData = analogRead (temperaturePin);
  humidityData    = analogRead (humidityPin);
  lightData       = analogRead (lightPin);
 
  // Send sensors information
  // 
  frame_.sendEvent (EVENTO_TEMPERATURA, temperatureData);
  frame_.sendEvent (EVENTO_HUMEDAD    , humidityData);
  frame_.sendEvent (EVENTO_LUZ        , lightData);

  // Wait 1 second  
  //
  delay (1000);
}

// Suscribed method to SerialFrameSlave, here are the commands received
//
void andale (int c,int d)
{
  Serial.println (String("andale andale!!! comando recibido: ") + String (c) + String(" y dato: ") + String(d));
  if (c == COMMAND_LED)
  {
    digitalWrite (ledPin, d==0?0:1);
  }
}
