
/***************************************************************************************
 *  File: SerialFrameSlave.cpp
 *  Name: Zero CrashOverride
 *  Name: eltiempopaso@gmail.com
 *  Description: Serial Frame implementation
 ***************************************************************************************/

#include <SerialFrameSlave/SerialFrameSlave.h>


SerialFrameSlave::SerialFrameSlave ()
{
   nSuscriptors = 0;
}


SerialFrameSlave::SerialFrameSlave (IFrameSlave::Suscriptor s, Stream * aStream)
{
   nSuscriptors = 0;

   setOnCommand (s);

   serial_ = aStream;

}

SerialFrameSlave::~SerialFrameSlave ()
{
}

// Send event method
//
boolean SerialFrameSlave::sendEvent (int id, int data)
{
  String myEvent = "e;" + String(id)+";"+String(data);
  Serial.println (myEvent);
}

// Command suscriptions
//
boolean SerialFrameSlave::setOnCommand (IFrameSlave::Suscriptor s)
{
  boolean res = false;
  
  if (nSuscriptors < MAX_SUSCRIPTORS)
  {
    mySuscriptors[nSuscriptors] = s;
    nSuscriptors++;
    res = true;
  }
  
  return res;
}

boolean SerialFrameSlave::readACommand (int & command, int & data)
{
  boolean res = false;
  String line;
  
  // Check serial information
  //
  byte bytes = Serial.available();

  // Read type of Frame
  //
  line = serial_->readStringUntil(';');

  // If a command is received
  //
  if (line == "c")
  {
      // Retrieve the command number
      //
      line = serial_->readStringUntil(';');
      command = atoi(line.c_str());
      
      // Retrieve the command attached data
      //
      line = serial_->readStringUntil('\n');
      data    = atoi(line.c_str());

      // Set the command as received
      //
      res = true;
  }

  return res;
}

// Check if there are commands available
//
void SerialFrameSlave::checkCommands (void)
{
  int c = 0;
  int d = 0;
  
  // Read the serial information
  //
  if (readACommand (c,d))
  {
    // Call suscriptors
    //
    for (unsigned i = 0; i<nSuscriptors; i++)
    {
      mySuscriptors[i](c,d);
    }
  }
}


