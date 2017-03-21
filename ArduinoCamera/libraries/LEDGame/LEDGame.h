#ifndef CAMERA_H
#define CAMERA_H

#define NUM_LEDS 150
#include "FastLED.h"
#include "Arduino.h"

class LEDGame {
public:
	LEDGame();
	void Update();
private:
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
	
	void SetColor(int color, int sat, int value);
	bool IsColorCorrect(int input);
	int GetDifferentColor();
	void Right();
	
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
	
	
	CRGB leds[NUM_LEDS];
};

#endif