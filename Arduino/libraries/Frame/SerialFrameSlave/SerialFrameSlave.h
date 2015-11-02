
/***************************************************************************************
 *  File: SerialFrameSlave.h
 *  Name: Zero CrashOverride
 *  Name: eltiempopaso@gmail.com
 *  Description: Serial Frame declaration 
 ***************************************************************************************/


#ifndef CLASS_SERIAL_FRAME_SLAVE
#define CLASS_SERIAL_FRAME_SLAVE

#include <IFrameSlave.h>


#ifndef MAX_SUSCRIPTORS
#define MAX_SUSCRIPTORS 5
#endif


class SerialFrameSlave : public IFrameSlave
{
  // local definitions
  //
  unsigned nSuscriptors = 0;
  SUSCRIPTOR(mySuscriptors[MAX_SUSCRIPTORS]);

public:
  SerialFrameSlave ();
  SerialFrameSlave (SUSCRIPTOR(z));
  ~SerialFrameSlave ();

public:
  void checkCommands (void);
    
  boolean sendEvent (int id, int data);
  
  boolean setOnCommand (SUSCRIPTOR(z));
private:
    
  boolean readACommand (int & command, int & data);
};

#endif  /* CLASS_SERIAL_FRAME */
