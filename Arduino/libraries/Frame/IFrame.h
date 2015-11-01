/***************************************************************************************
 *  File: Frame.h
 *  Name: Zero CrashOverride
 *  Name: eltiempopaso@gmail.com
 *  Description: Frame interface
 ***************************************************************************************/

#ifndef CLASS_FRAME
#define CLASS_FRAME

#include <Arduino.h>

#define SUSCRIPTOR(s) void (*s)(int,int)

class IFrame
{

public:

  virtual void checkCommands (void) = 0;
    
  virtual boolean sendEvent (int id, int data) = 0;
  
  virtual boolean setOnCommand (SUSCRIPTOR(z)) = 0;
};

#endif  /* CLASS_FRAME */
