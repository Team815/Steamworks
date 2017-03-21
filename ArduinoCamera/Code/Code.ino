#include <LEDGame.h>
#include <Camera.h>

Camera camera;
LEDGame ledGame;

void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0));
}

void loop() {
  camera.Update();
  ledGame.Update();
}

