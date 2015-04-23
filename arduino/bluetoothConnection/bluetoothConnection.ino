#include <SoftwareSerial.h>
#define RX 1
#define TX 0


//pins for the shoe
int REDPIN1   = 8;
int GREENPIN1 = 9;
int BLUEPIN1  = 10;

//pins for the flap
int REDPIN2   = 11;
int GREENPIN2 = 12;
int BLUEPIN2  = 13;

//blinking
int tempR1;
int tempG1;
int tempB1;

int tempR2;
int tempG2;
int tempB2;

//default vars
int counter;
int delayCounter;
int msg[50];
int target;
String str = "";
boolean searching;

boolean blinking;
boolean blinked;

//get bluetooth
SoftwareSerial bt(RX, TX);

void setup() {
  
  //set defaults
  counter = 0;
  searching = false;
  
  //start bluetooth
  bt.begin(9600);
  
  blinking = false;
  blinked = false;
  delayCounter = 0;
  
}

void loop() {
  int c = bt.read();
 
  if(c > 0 ){ 
    startSearching(c);
  }

  if(c < 0){ 
   endSearching(c);
  }
  x
  if(blinking)
  {
    if(delayCounter == 10){
      if(blinked)
      {
        blinkShoeOn();
        blinked = false;
      }
      else
      {
        blinkShoeOff();
        blinked = true;
      }
      delayCounter = 0;
    }
  }else{
    if(delayCounter == 10){
      delayCounter = 0;
    }
  }
  
  delayCounter += 1;
  bt.print(delayCounter);
  delay(100);
}

void startSearching(int data){
  if(searching == false)
  {
    if(char(data) == '0'){       target = 1; }
    else if(char(data) == '1'){  target = 2; }
    else if(char(data) == '2'){  toggleBlink(); }
  } 
  searching = true;
  msg[counter] = data;
  counter += 1; 
}

void endSearching(int data){
  if(searching == true)
  {
    searching = false;
    for(int j = 1; j < counter; j++){ str += char(msg[j]); }
    for(int j = 0; j < counter; j++){ msg[j] = 0;}   
    displayColor(str, target);   
    str     = "";
    counter = 0;
  }
}

void toggleBlink(){
  if(blinking){ blinking = false; }
  else{ blinking = true; }
}

void displayColor(String data, int target){
  bt.print(data);
  
    if(data == "red") setColor(target, 255, 0, 0);
    else if(data == "yellow") setColor(target, 230, 230, 0); 
    else if(data == "green") setColor(target, 0, 255, 0);
    else if(data == "pink") setColor(target, 255, 0, 239);
    else if(data == "blue") setColor(target, 0, 0, 255);
    else if(data == "aqua") setColor(target, 0, 150, 150); 
    else if(data == "orange") setColor(target, 255, 125, 0);
    else if(data == "none") setColor(target, 0, 0, 0);
}

void setColor (int target, int red, int green, int blue) {
      
    if(target == 1){
      analogWrite(REDPIN1, red); 
      analogWrite(GREENPIN1, green); 
      analogWrite(BLUEPIN1, blue); 
      
      tempR1 = red;
      tempB1 = blue;
      tempG1 = green;
    }
    else if(target == 2){
      analogWrite(REDPIN2, red); 
      analogWrite(GREENPIN2, green); 
      analogWrite(BLUEPIN2, blue); 
      
      tempR2 = red;
      tempB2 = blue;
      tempG2 = green;
    }
    

}

void blinkShoeOn(){
  bt.print("Toggle On");
  analogWrite(REDPIN1, tempR1); 
  analogWrite(GREENPIN1, tempG1); 
  analogWrite(BLUEPIN1, tempB1); 
  
  analogWrite(REDPIN2, tempR2); 
  analogWrite(GREENPIN2, tempG2); 
  analogWrite(BLUEPIN2, tempB2); 
}

void blinkShoeOff(){
  if(blinking){
    bt.print("Toggle Off");  
    analogWrite(REDPIN1, 0); 
    analogWrite(GREENPIN1, 0); 
    analogWrite(BLUEPIN1, 0);
    
    analogWrite(REDPIN2, 0); 
    analogWrite(GREENPIN2, 0); 
    analogWrite(BLUEPIN2, 0);
  }
  else{
    analogWrite(REDPIN1, tempR1); 
    analogWrite(GREENPIN1, tempG1); 
    analogWrite(BLUEPIN1, tempB1); 
  
    analogWrite(REDPIN2, tempR2); 
    analogWrite(GREENPIN2, tempG2); 
    analogWrite(BLUEPIN2, tempB2); 
  } 
}
