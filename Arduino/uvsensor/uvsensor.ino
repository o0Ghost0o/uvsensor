// Includes
#include <SoftwareSerial.h>

// Pins
#define UVSENSOR_PIN A0
#define BT_RX 2
#define BT_TX 3
#define B_LED 6
#define G_LED 5

// Variables
bool B_LED_STATUS = LOW;
bool G_LED_STATUS = LOW;
bool is_nano = false;

// Serials
SoftwareSerial BT(BT_RX, BT_TX);

void setup() {

	if(is_nano) {
		Serial.begin(9600);
	}

	BT.begin(9600);
	pinMode(UVSENSOR_PIN, INPUT);
	pinMode(B_LED, OUTPUT);
	pinMode(G_LED, OUTPUT);
	blink(B_LED, 3);
	blink(G_LED, 3);
	if(is_nano) {
 		Serial.println("Setup complete!");
	}
}

void loop() {
	String data = "";

	if(BT.available()) {
// 		// Serial.println("Reading BT serial...");
		data = BT.readString();
//		blink(B_LED, 1);
		if(is_nano) {
	 		Serial.println(data);
		}
	}

	// blink(G_LED, 1);
	// digitalWrite(B_LED, HIGH);

	int uvindex = 0;
	int uvread = analogRead(UVSENSOR_PIN);

	if(uvread > 1170) {
		uvindex = 11;
	} else if(uvread > 1079) {
		uvindex = 10;
	} else if(uvread > 976) {
		uvindex = 9;
	} else if(uvread > 881) {
		uvindex = 8;
	} else if(uvread > 795) {
		uvindex = 7;
	} else if(uvread > 696) {
		uvindex = 6;
	} else if(uvread > 606) {
		uvindex = 5;
	} else if(uvread > 503) {
		uvindex = 4;
	} else if(uvread > 408) {
		uvindex = 3;
	} else if(uvread > 318) {
		uvindex = 2;
	} else if(uvread > 227) {
		uvindex = 1;
	} else {
		uvindex = 0;
	}

// 	Serial.print(uvread);
// 	Serial.print(":");
// 	Serial.println(uvindex);

	if(data == "conn_on") {
		blink(G_LED, 1);
		digitalWrite(B_LED, HIGH);
		BT.print(String(uvread) + ":" + String(uvindex) + "@");
	} else if(data == "conn_off") {
		blink(G_LED, 1);
		digitalWrite(B_LED, LOW);
	}
}

void blink(int led, int times) {
	for(int i = 0; i < times; i++) {
		digitalWrite(led, HIGH);
		delay(100);
		digitalWrite(led, LOW);
		delay(100);
	}
}