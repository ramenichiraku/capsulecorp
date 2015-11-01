
/***************************************************************************************
 *  File: SerialFrame.h
 *  Name: Zero CrashOverride
 *  Name: eltiempopaso@gmail.com
 *  Description: Serial Frame declaration 
 ***************************************************************************************/


#ifndef CLASS_SERIAL_FRAME
#define CLASS_SERIAL_FRAME

#include <IFrame.h>


#ifndef MAX_SUSCRIPTORS
#define MAX_SUSCRIPTORS 5
#endif


class SerialFrame : public IFrame
{
  // local definitions
  //
  unsigned nSuscriptors = 0;
  SUSCRIPTOR(mySuscriptors[MAX_SUSCRIPTORS]);

public:
  SerialFrame ();
  SerialFrame (SUSCRIPTOR(z));
  ~SerialFrame ();

public:
  void checkCommands (void);
    
  boolean sendEvent (int id, int data);
  
  boolean setOnCommand (SUSCRIPTOR(z));
private:
    
  boolean readACommand (int & command, int & data);
};

#endif  /* CLASS_SERIAL_FRAME */
