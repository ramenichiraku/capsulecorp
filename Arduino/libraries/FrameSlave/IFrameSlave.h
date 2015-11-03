/***************************************************************************************
 *  File: IFrameSlave.h
 *  Name: Zero CrashOverride
 *  Name: eltiempopaso@gmail.com
 *  Description: Frame interface
 ***************************************************************************************/

#ifndef CLASS_FRAME_SLAVE
#define CLASS_FRAME_SLAVE

#include <Arduino.h>
#include <SoftwareSerial.h>


class IFrameSlave
{
public:
  typedef void (*Suscriptor)(int,int);

public:

  virtual void checkCommands (void) = 0;
    
  virtual boolean sendEvent (int id, int data) = 0;
  
  virtual boolean setOnCommand (IFrameSlave::Suscriptor) = 0;
};

#endif  /* CLASS_FRAME */
