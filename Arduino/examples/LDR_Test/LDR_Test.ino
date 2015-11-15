
int analogPin = 5;
int redLedPin = 13;
int greenLedPin = 11;

int light=150;//Intervalo de luz. Testear y cambiarlo por el de vuestro entorno

void setup(){
  //inputs
  pinMode(analogPin,INPUT);
  
  //outputs
  pinMode(redLedPin,OUTPUT);
  pinMode(greenLedPin,OUTPUT);
}
  
void loop(){
  
  //Valor del sensor de luz
  int LDR_Value = analogRead(analogPin);
  
  if(LDR_Value>=45){
	digitalWrite(greenLedPin, HIGH);
  } else {
	digitalWrite(redLedPin, HIGH);
  }
    
  delay(700);
  digitalWrite(redLedPin, LOW);
  digitalWrite(greenLedPin, LOW);
  delay(700);
}
