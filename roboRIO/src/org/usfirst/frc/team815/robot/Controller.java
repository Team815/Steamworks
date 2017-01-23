package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	
	final int joystickPort = 0;
	final double analogThreshold = 0.1;
	
	private Joystick stick;
	ControllerTester controllerTester;
	
	private boolean buttonAPressed = false;
	private boolean buttonBPressed = false;
	private boolean buttonXPressed = false;
	private boolean buttonYPressed = false;
	private boolean buttonRightBumpPressed = false;
	private boolean buttonLeftBumpPressed = false;
	private boolean buttonSelectPressed = false;
	private boolean buttonStartPressed = false;
	private boolean buttonLeftJoyPressed = false;
	private boolean buttonRightJoyPressed = false;
	
	private double leftJoyX = 0;
	private double leftJoyY = 0;
	private double rightJoyX = 0;
	private double rightJoyY = 0;
	private double leftTrigger = 0;
	private double rightTrigger = 0;

	public Controller() {
		stick = new Joystick(joystickPort);
		controllerTester = new ControllerTester();
	}
	
	public void SetController() {
		buttonAPressed = stick.getRawButton(1);
		buttonBPressed = stick.getRawButton(2);
		buttonXPressed = stick.getRawButton(3);
		buttonYPressed = stick.getRawButton(4);
		buttonLeftBumpPressed = stick.getRawButton(5);
		buttonRightBumpPressed = stick.getRawButton(6);
		buttonSelectPressed = stick.getRawButton(7);
		buttonStartPressed = stick.getRawButton(8);
		buttonLeftJoyPressed = stick.getRawButton(9);
		buttonRightJoyPressed = stick.getRawButton(10);
		
		leftJoyX = Math.abs(stick.getRawAxis(0)) > analogThreshold ? stick.getRawAxis(0) : 0;
		leftJoyY = Math.abs(stick.getRawAxis(1)) > analogThreshold ? stick.getRawAxis(1) : 0;
		leftTrigger = stick.getRawAxis(2) > analogThreshold ? stick.getRawAxis(2) : 0;
		rightTrigger = stick.getRawAxis(3) > analogThreshold ? stick.getRawAxis(3) : 0;
		rightJoyX = Math.abs(stick.getRawAxis(4)) > analogThreshold ? stick.getRawAxis(4) : 0;
		rightJoyY = Math.abs(stick.getRawAxis(5)) > analogThreshold ? stick.getRawAxis(5) : 0;
	}
	
	public boolean IsAPressed() {
		return buttonAPressed;
	}
	
	public boolean IsBPressed() {
		return buttonBPressed;
	}
	
	public boolean IsXPressed() {
		return buttonXPressed;
	}
	
	public boolean IsYPressed() {
		return buttonYPressed;
	}
	
	public boolean IsStartPressed() {
		return buttonStartPressed;
	}
	
	public boolean IsSelectPressed() {
		return buttonSelectPressed;
	}
	
	public boolean IsRightBumpPressed() {
		return buttonRightBumpPressed;
	}
	
	public boolean IsLeftBumpPressed() {
		return buttonLeftBumpPressed;
	}
	
	public boolean IsRightJoyPressed() {
		return buttonRightJoyPressed;
	}
	
	public boolean IsLeftJoyPressed() {
		return buttonLeftJoyPressed;
	}
	
	public double GetLeftJoyX() {
		return leftJoyX;
	}
	
	public double GetLeftJoyY() {
		return leftJoyY;
	}
	
	public double GetRightJoyX() {
		return rightJoyX;
	}
	
	public double GetRightJoyY() {
		return rightJoyY;
	}
	
	public double GetLeftTrigger() {
		return leftTrigger;
	}
	
	public double GetRightTrigger() {
		return rightTrigger;
	}
	
	public void Output() {
		controllerTester.RunTest(this);
	}
	
	public double GetAnalogThreshold() {
		return analogThreshold;
	}
}
