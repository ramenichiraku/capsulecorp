
#ifndef MAX_SUSCRIPTORS
#define MAX_SUSCRIPTORS 5
#endif

#ifndef MY_SERIAL
#define MY_SERIAL Serial
#endif


#define SUSCRIPTOR(s) void (*s)(int,int)

/*
class ArduinoFrame
{
  // local definitions
  //
  static unsigned nSuscriptors = 0;
  static SUSCRIPTOR(mySuscriptors[MAX_SUSCRIPTORS]);

  public;
  static void checkCommands (void);
    
  static boolean sendEvent (int id, int data);
  
  static boolean setOnCommand (SUSCRIPTOR(z))
  private:
    
  boolean readACommand (int & command, int & data);
}
*/

// local definitions
  //
static unsigned nSuscriptors = 0;
static SUSCRIPTOR(mySuscriptors[MAX_SUSCRIPTORS]);


// Send event method
//
boolean sendEvent (int id, int data)
{
  String myEvent = "event;" + String(id)+";"+String(data);
  Serial.println (myEvent);
}

// Command suscriptions
//
boolean setOnCommand (SUSCRIPTOR(z))
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

boolean readACommand (int & command, int & data)
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
      
      //TBD: ACCEPT MORE THAN ONE COMMAND....
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
    
    
    if (String(buffer) == String("command"))
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
void checkCommands (void)
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

///////////////////////////////////////////

const static int EVENTO_TEMPERATURA = 0;
const static int EVENTO_LUZ         = 1;
const static int EVENTO_HUMEDAD     = 2;

const static int COMMAND_LED        = 0;




//int ledPin = 13;
int current = 0;

void andale (int c,int d)
{
  Serial.println (String("andale andale!!! comando recibido: ") + String (c) + String(" y dato: ") + String(d));
  if (c == COMMAND_LED)
  {
    //digitalWrite (ledPin, d==0?0:1);
    current = d;
    
  }
}

void setup ()
{
  
  //pinMode(ledPin, OUTPUT);
  
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
  }; // wait for serial port to connect. Needed for native USB port only
  
  setOnCommand (andale);
}

void loop ()
{
  checkCommands(); 
 
  if (current == 0)
  { 
    sendEvent (EVENTO_TEMPERATURA, 7);
  }
  else if (current == 1)
  {
    sendEvent (EVENTO_HUMEDAD    , 500);
  }
  else if (current == 2)
  {
    sendEvent (EVENTO_LUZ        , 100);
  }
  
  delay (5000);
}
