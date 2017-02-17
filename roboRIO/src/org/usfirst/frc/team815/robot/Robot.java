package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Controller.AnalogName;
import org.usfirst.frc.team815.robot.Dpad.Direction;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
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
	RobotDrive robot;
	Autonomous auto = new Autonomous();
	Controller controller = new Controller();
	Relay lightRelay = new Relay(0, Relay.Direction.kForward);
	Camera camera = new Camera();
	Gyro gyro = new Gyro(1);
	final double minSpeedMultiplier = 0.2;
	final double maxSpeedMultiplier = 1;
	final double speedMultiplierIncrement = 0.01;
	double speedMultiplier = 1;
	double horizontal = 0;
	double vertical= 0;
	CANTalon ballPickup = new CANTalon(1);
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
		CANTalon talonFrontRight = new CANTalon(13);
		CANTalon talonRearRight = new CANTalon(12);
		CANTalon talonFrontLeft = new CANTalon(14);
		CANTalon talonRearLeft = new CANTalon(15);
    	
    	talonFrontRight.setInverted(true);
    	talonRearRight.setInverted(true);
    	  	
    	robot = new RobotDrive(talonFrontLeft, talonRearLeft, talonFrontRight, talonRearRight);
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    @Override
    public void autonomousInit() {
    	auto.StartAuto(gyro);
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	
    	if(auto.GetState() == Autonomous.State.Positioning) {
    		if(auto.GetIntoPosition(60, gyro)) {
    			camera.StartCamera();
    			lightRelay.set(Relay.Value.kOn);
    		}
    	} else if(auto.GetState() == Autonomous.State.Aligning) {
    		auto.Align(camera.ReadBuffer());
    	}
    	
    	gyro.Update(false);
    	
    	double horizontal = auto.GetHorizontal();
    	double vertical = auto.GetVertical();
    	double rotation = gyro.GetCompensation();
    	double gyroValue = auto.GetState() == Autonomous.State.Positioning ? gyro.GetAngle() : 0;
    	
    	System.out.println("Target Angle:" + gyro.GetTargetAngle() + ", Angle: " + gyro.GetAngle() + ", Compensation: " + gyro.GetCompensation());
    	
    	robot.mecanumDrive_Cartesian(horizontal, vertical, rotation, gyroValue);
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    @Override
    public void teleopInit(){
    	
//    	myRobot.setMaxOutput(minSpeedMultiplier);
//    	timer.start();
    	gyro.ResetTargetAngle();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	controller.Update();
    	
    	if(controller.WasDpadDirectionClicked(Direction.Up)) {
    		ballPickup.set(1);
    	} else if(controller.WasDpadDirectionClicked(Direction.Down)) {
    		ballPickup.set(-1);
    	} else if(controller.WasDpadDirectionClicked(Direction.Right)) {
    		ballPickup.set(0);
    	}
    	
    	if(controller.WasClicked(Controller.ButtonName.Select)) {
    		gyro.SetPlayerAngle();
    	}
    	
    	if(controller.WasClicked(Controller.ButtonName.X)) {
    		if(controller.IsToggled(Controller.ButtonName.X)) {
        		camera.StartCamera();
        		lightRelay.set(Relay.Value.kOn);
    		} else {
    			lightRelay.set(Relay.Value.kOff);
    		}
    	}
    	
    	if(controller.JustZeroed(AnalogName.RightJoyX)){
    		gyro.ResetTargetAngle();
    	}
    	
    	gyro.Update(controller.GetValue(Controller.AnalogName.RightJoyX) != 0);
    	
    	SetMaxSpeed();
    	
    	double horizontal = 0;
    	double vertical = 0;
    	double rotation = 0;
    	double gyroValue = 0;
    	
    	if(!controller.IsToggled(Controller.ButtonName.Start)) {
	    	if(controller.IsToggled(Controller.ButtonName.X)) {
	    		auto.Align(camera.ReadBuffer());
	    		horizontal = auto.GetHorizontal();
	    		vertical = auto.GetVertical();
	    		rotation = gyro.GetCompensation();
	    		gyroValue = 0;
	    	} else {
	    		horizontal = controller.GetValue(Controller.AnalogName.LeftJoyX);
	    		vertical = controller.GetValue(Controller.AnalogName.LeftJoyY);
	    		rotation = controller.GetValue(AnalogName.RightJoyX);
	    		rotation = rotation == 0 ? gyro.GetCompensation() : rotation;
	    		gyroValue = controller.IsToggled(Controller.ButtonName.A) ? 0 : gyro.GetAngle();
	    	}
    	}
    	
    	robot.mecanumDrive_Cartesian(horizontal, vertical, rotation, gyroValue);
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
    		robot.setMaxOutput(speedMultiplier);
    	}
    }
}