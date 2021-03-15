#include "Node.c"
//brianesp
int numOfNodesReceived(String);
char recieved[100];
Node nodeArray[300];
String in = "_";
int numOfNodes = 0;
void setup() {
  // put your setup code here, to run once:
Serial.begin(115200);
}

void loop() {
  // put your main code here, to run repeatedly:

  //need to comment out serial prints because they need to be the things sent to the esp
  while(Serial.available()){
    in = Serial.readStringUntil('\r');
//    Serial.println(in);  
    if(in[2] == 'r'){
      //started from _romi
        in = Serial.readStringUntil('[');
//        Serial.println(in);
        in = Serial.readStringUntil(']');
//        Serial.println(in);
        numOfNodes = numOfNodesReceived(in);
        Serial.println("done");
        Serial.println(nodeArray[2].x);

    }
  }

//delay(100);
}

int numOfNodesReceived(String str){
  //parse string into struct and count
  int returnVal=0; 
  String delimiter = "=";
  String s = "";
  String sub = "";
  String sub2 = "";
  String sub3 = "";
  double x=0;
  double y=0;
  double f=0;
  Node n = {0,0,0};
  int start = 1;
  int j=0;
  for(char i:str){
    while(i!='=' && i!='[' && i!=']'){
      s+=i;  
      break;
   }
    if(i=='{'){
      start = s.indexOf(i);
    }
   if(i=='}'){
    sub = s.substring(start+1,s.indexOf(i)); 
    s=s.substring(s.indexOf(i)+1,s.length());

    x=(sub.substring(0,sub.indexOf(','))).toDouble();
//   Serial.print("x: ");
//   Serial.println(x);
   sub2 = sub.substring(sub.indexOf(',')+1,sub.length());
   y=sub2.substring(0,sub2.indexOf(',')).toDouble();
//   Serial.print("y: ");
//   Serial.println(y);
   sub3 = sub2.substring(sub2.indexOf(',')+1,sub2.length());
   f=sub3.substring(0,sub2.indexOf(',')).toInt();
//   Serial.print("f: ");
//   Serial.println(f);
   n={x,y,f};

   nodeArray[j]=n;
   j++;
   returnVal = j;
   }  
  }

  return returnVal; 
}
