void setup() {
  // put your setup code here, to run once:

  //switch pin
  pinMode(3, INPUT);

  //leds pins
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(11, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  boolean interruptor=digitalRead(3);
  if(interruptor){
    digitalWrite(10, HIGH);
  }else{
    digitalWrite(9, HIGH);
    digitalWrite(11, HIGH);  
  }
  
  delay(500);
  
  digitalWrite(9, LOW);
  digitalWrite(10, LOW);
  digitalWrite(11, LOW);
  delay(500);
}
