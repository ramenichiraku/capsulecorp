/***************************************************************************************
 *  File: SerialFrameSlaveExample.ino
 *  Name: Zero CrashOverride
 *      : Chisigo
 *  Description:
 ***************************************************************************************/

/*
*/

/* libraries dependencies */
#include <SoftwareSerial.h>

#include <IFrameSlave.h>
#include <SerialFrameSlave/SerialFrameSlave.h>

/* commands and events interface */
/*

*/

/* COMMANDS: communication from master to slave */
const static int COMMAND_ONOFF            = 0;

/* EVENTS: communication from slave to master */
const static int EVENTO_ONOFF             = 0;


// Select SERIAL channel (Serial or XBee)
//
#define SERIAL_IS_XBEE 0


// Local pins
//
const static int pinRelay1      = 4;
const static int pinRelay2      = 5;

const static int pinSerial_rx      = 2;
const static int pinSerial_tx      = 3;

//Configure Serial channel
//
#if SERIAL_IS_XBEE
static SoftwareSerial XBee(pinSerial_rx, pinSerial_tx); // Arduino RX, TX (XBee Dout, Din)
#define MY_SERIAL XBee
#else
#define MY_SERIAL Serial 
#endif


// Local variables to store current data
//


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
  pinMode(pinRelay1,OUTPUT);
  
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
 

  // Wait 1 second  
  //
  delay (1);
}

// Suscribed method to SerialFrameSlave, here are the commands received
//
//IFrameSlave::Suscriptor andale
void andale (int c,int d)
{
  MY_SERIAL.println (String("andale andale!!! comando recibido: ") + String (c) + String(" y dato: ") + String(d));

  if (c == COMMAND_ONOFF)
  {
    digitalWrite (pinRelay1, d==0?0:1);
  } 
}
