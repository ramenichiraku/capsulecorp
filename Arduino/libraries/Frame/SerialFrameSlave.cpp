
/***************************************************************************************
 *  File: SerialFrameSlave.cpp
 *  Name: Zero CrashOverride
 *  Name: eltiempopaso@gmail.com
 *  Description: Serial Frame implementation
 ***************************************************************************************/

#include <SerialFrameSlave/SerialFrameSlave.h>

#ifndef MY_SERIAL
#define MY_SERIAL Serial
#endif


SerialFrameSlave::SerialFrameSlave ()
{
   nSuscriptors = 0;
}


SerialFrameSlave::SerialFrameSlave (SUSCRIPTOR(z))
{
   nSuscriptors = 0;

   setOnCommand (z);
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
boolean SerialFrameSlave::setOnCommand (SUSCRIPTOR(z))
{
  boolean res = false;
  
  if (nSuscriptors < MAX_SUSCRIPTORS)
  {
    mySuscriptors[nSuscriptors] = z;
    nSuscriptors++;
    res = true;
  }
  
  return res;
}

boolean SerialFrameSlave::readACommand (int & command, int & data)
{
  boolean res = false;
  static char buffer[50];
  char * commandChar;
  char * dataChar;
  
  // Check serial information
  //
  byte bytes = MY_SERIAL.available();
  
//  assert_non_removable ();
  
  // If available data
  //
  if (bytes) 
  {
    int i;
    for (i=0; i<bytes; i++)
    {
      buffer[i] = MY_SERIAL.read();
      
      //TBD: ACCEPT MORE THAN ONE COMMAND IN THE BUFFER.... (VERY IMPORTANT)
      if (buffer[i] == '\n')
      {
        break;
      }
      
      if (buffer[i] == ';')
      {
        if (!commandChar)
        {
          commandChar = &buffer[i+1];
        }
        else
        {
          dataChar  = &buffer[i+1];
        }
        
        buffer[i] = '\0';
      }
    }    
    buffer[i] = '\0';
    
    
    if (String(buffer) == String("c"))
    {
      command = atoi(commandChar);
      data    = atoi(dataChar);
      
      res = true;
    }
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


