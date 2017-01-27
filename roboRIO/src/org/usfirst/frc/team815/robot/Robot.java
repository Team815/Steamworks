package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
//import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.IterativeRobot;
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
	
	Controller controller;
	Camera camera;
	AnalogGyro gyro;
	Timer timer;
	int autoLoopCounter;
	double speedMultiplier = 1;
	final double minSpeedMultiplier = 0.2;
	final double maxSpeedMultiplier = 1;
	final double speedMultiplierIncrement = 0.01;
	
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
    	controller = new Controller();
    	camera = new Camera();
    	gyro = new AnalogGyro(1);
    	timer = new Timer();
    	
    	//myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
    	//myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    	myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
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
    	
    	timer.start();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	//controller.SetController();
    	SetMaxSpeed();
    	
    	camera.ReadBuffer();
    	
    	if(timer.hasPeriodPassed(10)) {
    		gyro.calibrate();
    	}
    	
    	//controller.Output();
    	
    	//myRobot.mecanumDrive_Cartesian(controller.GetLeftJoyX(), controller.GetLeftJoyY(), controller.GetRightJoyX(), 0);
    	
    	//System.out.println(gyro.getAngle());
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
    	if(controller.IsLeftBumpPressed() && speedMultiplier > minSpeedMultiplier) {
    		speedMultiplier -= speedMultiplierIncrement;
    	} else if (controller.IsRightBumpPressed() && speedMultiplier < maxSpeedMultiplier) {
    		speedMultiplier += speedMultiplierIncrement;
    	}
    	
    	
    	
    	if(controller.IsLeftBumpPressed() || controller.IsRightBumpPressed()) {
    		myRobot.setMaxOutput(speedMultiplier);
    	}
    }
}