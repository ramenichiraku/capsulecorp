
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
private:
  SerialFrameSlave();

public:
  SerialFrameSlave (IFrameSlave::Suscriptor s, Stream * aStream);
  ~SerialFrameSlave ();

public:
  void checkCommands (void);
    
  boolean sendEvent (int id, int data);
  
  boolean setOnCommand (IFrameSlave::Suscriptor s);
private:
    
  boolean readACommand (int & command, int & data);


private:
  // local definitions
  //
  unsigned nSuscriptors = 0;
  IFrameSlave::Suscriptor mySuscriptors[MAX_SUSCRIPTORS];

  Stream * serial_;

};

#endif  /* CLASS_SERIAL_FRAME */
