package org.usfirst.frc.team815.robot;

public class ControllerTester {
	
	private Controller controller;
	
    public void RunTest(Controller controllerIn) {
		
		controller = controllerIn;
    	
    	TestButton(controller.IsAPressed(), "A is pressed");
    	TestButton(controller.IsBPressed(), "B is pressed");
    	TestButton(controller.IsLeftBumpPressed(), "Left fist bump is pressed");
    	TestButton(controller.IsLeftJoyPressed(), "Left joy is pressed");
    	TestButton(controller.IsRightBumpPressed(), "Right bump is pressed");
    	TestButton(controller.IsRightJoyPressed(), "Right joy is pressed");
    	TestButton(controller.IsSelectPressed(), "Selected");
    	TestButton(controller.IsStartPressed(), "Let's start!");
    	TestButton(controller.IsXPressed(), "X marks the spot");
    	TestButton(controller.IsYPressed(), "Why?");
    	
    	double leftJoyX = Math.abs(controller.GetLeftJoyX());
    	double leftJoyY = Math.abs(controller.GetLeftJoyY());
    	double rightJoyX = Math.abs(controller.GetRightJoyX());
    	double rightJoyY = Math.abs(controller.GetRightJoyY());
    	
    	TestJoystick(leftJoyX, leftJoyY, "LeftJoystick");
    	TestJoystick(rightJoyX, rightJoyY, "RightJoystick");
    	TestTrigger(controller.GetLeftTrigger(), "LeftTrigger");
    	TestTrigger(controller.GetRightTrigger(), "RightTrigger");
    }
    
    private void TestButton(boolean buttonPressed, String message) {
    	if(buttonPressed) {
    		System.out.println(message);
    	}
    }
    
    private void TestJoystick(double joystickX, double joystickY, String joystickName) {
    	if(joystickX > controller.GetAnalogThreshold() && joystickX > joystickY) {
    		System.out.println(joystickName + "X: " + joystickX);
    	}
    	
    	if(joystickY > controller.GetAnalogThreshold() && joystickY > joystickX) {
    		System.out.println(joystickName + "Y: " + joystickY);
    	}
    }
    
    private void TestTrigger(double trigger, String triggerName) {
    	if(trigger > controller.GetAnalogThreshold()) {
    		System.out.println(triggerName + ": " + trigger);
    	}
    }
}
