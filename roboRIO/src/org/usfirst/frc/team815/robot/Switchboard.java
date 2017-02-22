package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Switchboard {
	private Joystick stick = new Joystick(1);
	
	private Button switch1 = new Button(1);
	private Button switch2 = new Button(2);
	private Button switch3 = new Button(3);
	private Button switch4 = new Button(4);
	
	public int GetBinaryValue() {
		int value = 0;
		value += switch4.IsPressed() ? 8 : 0;
		value += switch3.IsPressed() ? 4 : 0;
		value += switch2.IsPressed() ? 2 : 0;
		value += switch1.IsPressed() ? 1 : 0;
		return value;
	}
	
	public void Update() {
		switch1.Update(stick);
		switch2.Update(stick);
		switch3.Update(stick);
		switch4.Update(stick);
	}
}
