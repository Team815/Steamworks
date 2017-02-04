package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Controller.AnalogName;

import edu.wpi.first.wpilibj.AnalogGyro;
//import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	Controller controller = new Controller();
	Relay lightRelay = new Relay(0, Relay.Direction.kForward);
	Camera camera = new Camera();
	Gyro gyro = new Gyro(1);
	Timer timer = new Timer();
	int autoLoopCounter;
	final double minSpeedMultiplier = 0.2;
	final double maxSpeedMultiplier = 1;
	final double speedMultiplierIncrement = 0.01;
	double speedMultiplier = 1;
	double direction = 0;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
    	
    	final int frontLeftMotor = 0;
    	final int rearLeftMotor = 1;
    	final int frontRightMotor = 2;
    	final int rearRightMotor = 3;
    	  	
    	myRobot = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    	
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    	
    	lightRelay.set(Relay.Value.kOn);
    	gyro.Calibrate();
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    @Override
    public void autonomousInit() {
    	autoLoopCounter = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    @Override
    public void teleopInit(){
    	
//    	myRobot.setMaxOutput(minSpeedMultiplier);
//    	timer.start();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	controller.Update();
    	
    	if(controller.WasClicked(Controller.ButtonName.Select)) {
    		gyro.Calibrate();
    	}
    	
    	if(controller.WasClicked(Controller.ButtonName.X) && controller.IsToggled(Controller.ButtonName.X)) {
    		camera.StartCamera();
    	}
    	
    	if(controller.JustZeroed(AnalogName.RightJoyX)){
    		gyro.ResetTargetAngle();
    	}
    	
    	gyro.Update(controller.GetValue(Controller.AnalogName.RightJoyX) != 0);
    	
    	SetMaxSpeed();
    	
    	if(controller.IsToggled(Controller.ButtonName.X)) {
    		Align(camera.ReadBuffer());
    	} else {
    		double x = controller.GetValue(Controller.AnalogName.LeftJoyX);
    		double y = controller.GetValue(Controller.AnalogName.LeftJoyY);
    		double rotation = controller.GetValue(AnalogName.RightJoyX);
    		rotation = rotation == 0 ? gyro.GetCompensation() : rotation;
    		double gyroValue = controller.IsToggled(Controller.ButtonName.A) ? 0 : gyro.GetAngle();
    		
    		myRobot.mecanumDrive_Cartesian(x, y, rotation, gyroValue);
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
    @Override
    public void disabledInit() {
    	
    }
    
    private void SetMaxSpeed() {
    	double lastSpeedMultiplier = speedMultiplier;
    	if(controller.IsPressed(Controller.ButtonName.LB) && speedMultiplier > minSpeedMultiplier) {
    		speedMultiplier -= speedMultiplierIncrement;
    	} else if (controller.IsPressed(Controller.ButtonName.RB) && speedMultiplier < maxSpeedMultiplier) {
    		speedMultiplier += speedMultiplierIncrement;
    	}
    	
    	if(speedMultiplier != lastSpeedMultiplier) {
    		myRobot.setMaxOutput(speedMultiplier);
    	}
    }
    
    private void Align(char message) {
    	//System.out.println(message);
    	if (message == '9' || message == '3') {
    		direction = 0;
    	} else if(message == '2' || message == '1') {
    		direction = -0.2;
    	} else if (message == '4' || message == '5') {
    		direction = 0.2;
    	}
    	myRobot.mecanumDrive_Cartesian(direction, 0, gyro.GetCompensation(), 0);
    }
}