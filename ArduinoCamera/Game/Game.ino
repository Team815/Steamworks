#include "FastLED.h"

#define NUM_LEDS 150
CRGB leds[NUM_LEDS];

enum State{
  GettingNextColor,
  WaitingForInput,
  CorrectInput,
  WrongInput
};

typedef struct {
  int hue;
  int input;
} Color;

int colorDelay;
unsigned long lastUpdate;
unsigned long rightUpdate;

int inputsNeeded;
int inputsGiven;

Color colors[4] = {
  {0,114},
  {40, 121},
  {96, 103},
  {160, 98}
  };

Color color;

State state;

void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0));
  FastLED.addLeds<NEOPIXEL, 6>(leds, 150);
  SetColor(0, 0, 0);
  colorDelay = random(5, 10);
  lastUpdate = millis();
  state = GettingNextColor;
}

void loop() {
  int input = -1;

  if(Serial.available()) {
    input = Serial.read();
  }

  if(state == CorrectInput) {
    if(millis() - rightUpdate >= 50) {
      Right();
      rightUpdate = millis();
    }
  }
  
  if(state == GettingNextColor || state == CorrectInput || state == WrongInput) {
    if(millis() - lastUpdate >= colorDelay * 1000) {
      state = WaitingForInput;
      color = colors[random(4)];
      SetColor(color.hue, 255, 255);
      lastUpdate = millis();
      inputsNeeded += 1;
      inputsGiven = 0;
    }
  } else if(state == WaitingForInput) {
    if(IsColorCorrect(input)) {
      if(++inputsGiven == inputsNeeded) {
        state = CorrectInput;
        colorDelay = random(5, 10);
        rightUpdate = millis();
      } else {
        color = colors[GetDifferentColor()];
        SetColor(color.hue, 255, 255);
      }
      lastUpdate = millis();
    } else if(input != -1 || millis() - lastUpdate >= 1000) {
      state = WrongInput;
      SetColor(0, 0, 255);
      inputsNeeded = 0;
      colorDelay = random(5, 10);
      lastUpdate = millis();
    }
  }
}

void SetColor(int color, int sat, int value) {
  for(int i = 0; i < NUM_LEDS; i++) {
    leds[i] = CHSV(color, sat, value);
  }
  FastLED.show();
}

bool IsColorCorrect(int input){
  if(input == color.input) {
    return true;
  } else {
    return false; 
  }
}

int GetDifferentColor() {
  while(true) {
    int possibleColor = random(4);
    if(colors[possibleColor].hue != color.hue) {
      return possibleColor;
    }
  }
}

void Right() {
  for(int i = 0; i < NUM_LEDS; i++) {
    leds[i] = CHSV(random(256), 255, 255);
  }
  FastLED.show();
}

