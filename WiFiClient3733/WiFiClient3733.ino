/*
 *  This sketch sends data via HTTP GET requests to data.sparkfun.com service.
 *
 *  You need to get streamId and privateKey at data.sparkfun.com and paste them
 *  below. Or just customize this script to talk to other HTTP servers.
 *
 */

#include <WiFi.h>
#include "Node.c"

const char* ssid     = "itHertzwhenIP"; //my wifi
const char* password = "11@35*13";      //my password;

const char* host = "192.168.1.131";
const char* streamId   = "....................";
const char* privateKey = "....................";

int numOfNodesReceived(String);

Node nodeArray[300];
String stringFromServer = "";
int numOfNodes = 0;

String test = "[{484.0,1135.0,0}={580.0,1139.0,0}={719.0,977.0,0}={845.0,946.0,0}={953.0,927.0,0}={1024.0,937.0,0}={1077.0,973.0,0}={1204.0,972.0,0}={1260.0,976.0,0}={1308.0,995.0,0}={1352.0,977.0,0}={1471.0,927.0,0}={1585.0,900.0,0}={1585.0,802.0,0}={1483.0,704.0,0}]";
String test2 = "[{2,4,6}={3,5,7}]";
void setup()
{
    Serial.begin(115200);
    delay(10);

    // We start by connecting to a WiFi network

    Serial.println();
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);

    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

//int k = numOfNodesReceived(test2);

    
}

int value = 0;

void loop()
{
    delay(5000);
    ++value;

    Serial.print("connecting to ");
    Serial.println(host);

    // Use WiFiClient class to create TCP connections
    WiFiClient client;
    const int port = 4444;
    if (!client.connect(host, port)) {
        Serial.println("connection failed");
        return;
    }

    // We now create a URI for the request
    String url = "/input/";
    url += streamId;
    url += "?private_key=";
    url += privateKey;
    url += "&value=";
    url += value;

    Serial.print("Requesting URL: ");
    Serial.println(url);

//    // This will send the request to the server           //we aint using http
//    client.print(String("GET ") + url + " HTTP/1.1\r\n" +
//                 "Host: " + host + "\r\n" +
//                 "Connection: close\r\n\r\n");
    unsigned long timeout = millis();
    while (client.available() == 0) {
        if (millis() - timeout > 5000) {
            Serial.println(">>> Client Timeout !");
            client.stop();
            return;
        }
    }
 /*
    * protocol
    * WAITING
    * s:ready to send?
    * SENDNODES
    * c:yes
    * S:~list of node~
    * VERIFY
    * c:~#of nodes~
    * s:verified, bye
    * WAITING
    *
    *
    * [{x.x,y.y,floor}={x.x,y.y,floor}=...
    * */
    // Read all the lines of the reply from server and print them to Serial
    while(client.available()) {
        String line = client.readStringUntil('\r');//WAITING
        Serial.println("Server:"+line);
        if(line == "Ready to send?"){
          Serial.println("client:yes");
          client.println("yes");
          
          line = client.readStringUntil('\r');
          Serial.println("Server:"+line);
          Serial.println(line[1]=='[');
        if(line[1] == '['){
           stringFromServer = line
           numOfNodes = numOfNodesReceived(line);
           Serial.println(numOfNodes);
           client.println(numOfNodes);
           
           line = client.readStringUntil('\r');
        Serial.println(line);
        if(line[1] == 'B'){
          Serial.println("verified");  
          client.println("");
        }
        }
        }         
      
    }

    Serial.println();
    Serial.println("closing connection");
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
