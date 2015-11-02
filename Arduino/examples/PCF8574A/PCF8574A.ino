// Wire Master Reader
// by Nicholas Zambetti <http://www.zambetti.com>

// Demonstrates use of the Wire library
// Reads data from an I2C/TWI slave device
// Refer to the "Wire Slave Sender" example for use with this

// Created 29 March 2006

// This example code is in the public domain.

const static int intPin = 6;

#include <Wire.h>

void setup()
{
  pinMode(intPin, INPUT);
  Wire.begin();        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start serial for output
}

void loop()
{
  
  if (digitalRead (intPin) == LOW)
  {
    Serial.println ("Detected a change, going to read pcf.");
  
    Wire.requestFrom(0x38, 1);    // request 1 bytes from slave device #0x38
  
    while(Wire.available())    // slave may send less than requested
    { 
      char c = Wire.read(); // receive a byte as character
      //Serial.print(c);         // print the character
      Serial.println ("I have read: " + String (c));
    }
  }

  //delay(5000);
}
