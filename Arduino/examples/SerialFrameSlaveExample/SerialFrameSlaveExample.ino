/***************************************************************************************
 *  File: SerialFrameSlaveExample.ino
 *  Name: Zero CrashOverride
 *  mail: eltiempopaso@gmail.com
 *  Description: Example of using the FrameSlave over both the Serial and Xbee channels.
 ***************************************************************************************/

/*
This is an example of how to use the FrameSlave over serial library, using any the Serial or Xbee chanel.

To configure which serial you are going to use, set SERIAL_IS_XBEE:
  - SERIAL_IS_XBEE=1 => XBee is going to be used for all serial communication
  - SERIAL_IS_XBEE=0 => Normal serial is going to be used for serial communication

Once you have chosen the serial port, you need to define the Frame interface that you are going to use. It means to define:
  - COMMANDS: Messages from Master to Slave
  - EVENTS:   Messages from Slave to Master
  
IMPORTANT: This is a FrameSlave example, it means that here commands will be received and events will be sent.

*/

/* libraries dependencies */
#include <SoftwareSerial.h>

#include <IFrameSlave.h>
#include <SerialFrameSlave/SerialFrameSlave.h>

/* commands and events interface */
/*

  Slave:   Arduino microcontroller running this code.
  Master:  Host running the java interface as a master.

The COMMANDS:
1. COMMAND_LED is able to swith ON/OF a local led, remotely.
2. COMMAND_ENABLE_SENSORS enables/disables the sensor information to be sent.

The EVENTS 
  temperature, 
  light
  humidity 
  
Are related to local sensors, and an event is sent to the host when changed.

*/

/* COMMANDS: communication from master to slave */
const static int COMMAND_LED              = 0;
const static int COMMAND_ENABLE_SENSORS   = 1;

/* EVENTS: communication from slave to master */
const static int EVENTO_TEMPERATURA = 0;
const static int EVENTO_LUZ         = 1;
const static int EVENTO_HUMEDAD     = 2;


// Select SERIAL channel (Serial or XBee)
//
#define SERIAL_IS_XBEE 0

//Configure Serial channel
//
#if SERIAL_IS_XBEE
static SoftwareSerial XBee(2, 3); // Arduino RX, TX (XBee Dout, Din)
#define MY_SERIAL XBee
#else
#define MY_SERIAL Serial 
#endif


// Local sensors to read and send to host
//
const static int temperaturePin = 1;
const static int humidityPin    = 2;
const static int lightPin       = 3;

// Local variables to store current data
//
static int temperatureData = 0;
static int humidityData = 0;
static int lightData = 0;

// Sensors status managed remotely
//
boolean sensorsAreEnabled = false;

// Led pin controlled from host
//
const static int ledPin = 13;

// Create the SerialFrame object, and suscribe the andale method used as a callback.
//
void andale (int,int);
SerialFrameSlave frame_ ((IFrameSlave::Suscriptor)andale, &MY_SERIAL);

// Arduino setup configurations
//
void setup ()
{
  // Configure pins
  //
    /* ATENTION: Analog pins already configured */
  pinMode(ledPin, OUTPUT);
  
  // Open serial communications and wait for port to open:
  MY_SERIAL.begin (9600);
  #if SERIAL_IS_XBEE == 0
  while (!MY_SERIAL) {
  }; // wait for serial port to connect. Needed for native USB port only
  #endif
 
  // Commented as it is not necessary anymore because already initialized in constructor
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
  if (sensorsAreEnabled)
  {
    frame_.sendEvent (EVENTO_TEMPERATURA, temperatureData);
    frame_.sendEvent (EVENTO_HUMEDAD    , humidityData);
    frame_.sendEvent (EVENTO_LUZ        , lightData);
  }

  // Wait 1 second  
  //
  delay (1000);
}

// Suscribed method to SerialFrameSlave, here are the commands received
//
//IFrameSlave::Suscriptor andale
void andale (int c,int d)
{
  MY_SERIAL.println (String("andale andale!!! comando recibido: ") + String (c) + String(" y dato: ") + String(d));

  if (c == COMMAND_LED)
  {
    digitalWrite (ledPin, d==0?0:1);
  } 
  else if (c == COMMAND_ENABLE_SENSORS)
  {
    sensorsAreEnabled = d?true:false;
  }
}
