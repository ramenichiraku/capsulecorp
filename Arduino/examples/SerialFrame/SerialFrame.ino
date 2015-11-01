#include <Frame.h>
#include <SerialFrame/SerialFrame.h>

/* commands and events interface */
/* EVENTS: communication from slave to master */
const static int EVENTO_TEMPERATURA = 0;
const static int EVENTO_LUZ         = 1;
const static int EVENTO_HUMEDAD     = 2;

/* COMMANDS: communication from master to slave */
const static int COMMAND_LED        = 0;


/*
IMPORTANT: In this example, this is the slave source intended to be executed into an Arduino.

  Slave:   Arduino microcontroller running this code.
  Master:  Host running the java interface as a master.

The COMMAND_LED is able to swith ON/OF a local led, remotely.
The EVENTS temperature, light and humidity are related to local sensors, and an event is sent to the host when changed.

*/


//int ledPin = 13;
int current = 0;

/* Create the SerialFrame object, and suscribe the andale method. */
void andale (int,int);
SerialFrame frame_ (andale);


void setup ()
{
  //pinMode(ledPin, OUTPUT);
  
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
  }; // wait for serial port to connect. Needed for native USB port only
  
//  frame_.setOnCommand (andale);
}

void loop ()
{
  frame_.checkCommands(); 
 
  if (current == 0)
  { 
    frame_.sendEvent (EVENTO_TEMPERATURA, 7);
  }
  else if (current == 1)
  {
    frame_.sendEvent (EVENTO_HUMEDAD    , 500);
  }
  else if (current == 2)
  {
    frame_.sendEvent (EVENTO_LUZ        , 100);
  }
  
  delay (5000);
}

/* Suscribed method */
void andale (int c,int d)
{
  Serial.println (String("andale andale!!! comando recibido: ") + String (c) + String(" y dato: ") + String(d));
  if (c == COMMAND_LED)
  {
    //digitalWrite (ledPin, d==0?0:1);
    current = d;
    
  }
}
