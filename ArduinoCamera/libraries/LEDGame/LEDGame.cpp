#include "FastLED.h"

LEDGame::LEDGame() {
	FastLED.addLeds<NEOPIXEL, 6>(leds, 150);
	SetColor(0, 0, 0);
	colorDelay = random(5, 10);
	lastUpdate = millis();
	state = GettingNextColor;
}

void LEDGame::Update() {
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

void LEDGame::SetColor(int color, int sat, int value) {
	for(int i = 0; i < NUM_LEDS; i++) {
		leds[i] = CHSV(color, sat, value);
	}
	FastLED.show();
}

bool LEDGame::IsColorCorrect(int input){
	if(input == color.input) {
		return true;
	} else {
		return false; 
	}
}

int LEDGame::GetDifferentColor() {
	while(true) {
		int possibleColor = random(4);
		if(colors[possibleColor].hue != color.hue) {
			return possibleColor;
		}
	}
}

void LEDGame::Right() {
	for(int i = 0; i < NUM_LEDS; i++) {
		leds[i] = CHSV(random(256), 255, 255);
	}
	FastLED.show();
}