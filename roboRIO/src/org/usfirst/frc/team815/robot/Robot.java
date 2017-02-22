package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Autonomous.State;
import org.usfirst.frc.team815.robot.BallPickup.BPState;
import org.usfirst.frc.team815.robot.Controller.AnalogName;
import org.usfirst.frc.team815.robot.Controller.ButtonName;
import org.usfirst.frc.team815.robot.Dpad.Direction;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
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
	Controller controller = new Controller();
	Switchboard switchboard = new Switchboard();
	Relay lightRelay = new Relay(0, Relay.Direction.kForward);
	Gyro gyro = new Gyro(1);
	Autonomous auto = new Autonomous(gyro, lightRelay);
	Lift lift = new Lift(30);
	Shooter shooter = new Shooter(3);
	BallPickup ballpickup = new BallPickup(1);
	CANTalon agitator = new CANTalon(2);
	final double minSpeedMultiplier = 0.2;
	final double maxSpeedMultiplier = 1;
	final double speedMultiplierIncrement = 0.01;
	double speedMultiplier = 1;
	double horizontal = 0;
	double vertical= 0;
	
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
    	switchboard.Update();
    	gyro.SetPlayerAngle();
    	int autoState = switchboard.GetBinaryValue();
    	
    	System.out.println(autoState);
    	
    	if(autoState == 1) {
    		auto.SetTurningLeft(true);
    		auto.StartAuto(State.Positioning);
    	} else if(autoState == 4) {
    		auto.SetTurningLeft(false);
    		auto.StartAuto( State.Positioning);
    	} else if(autoState == 2) {
    		auto.StartAuto(State.Aligning);
    	}
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	
    	auto.Update();
    	
    	double horizontal = auto.GetHorizontal();
    	double vertical = auto.GetVertical();
    	double rotation = gyro.GetCompensation();
    	double gyroValue = auto.GetState() == State.Positioning ? gyro.GetAngle() : 0;
    	
    	//System.out.println("Target Angle:" + gyro.GetTargetAngle() + ", Angle: " + gyro.GetAngle() + ", Compensation: " + gyro.GetCompensation());
    	
    	robot.mecanumDrive_Cartesian(horizontal, vertical, rotation, gyroValue);
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    @Override
    public void teleopInit(){
    	
    	gyro.ResetTargetAngle();
    	lightRelay.set(Relay.Value.kOff);
    	//agitator.set(1);
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	controller.Update();
    	
    	// Ball Pickup Section
    	if(controller.WasDpadDirectionClicked(Direction.Up)) {
    		ballpickup.Toggle(BPState.Suck);
    	} else if(controller.WasDpadDirectionClicked(Direction.Down)) {
    		ballpickup.Toggle(BPState.Blow);
    	} else if(controller.WasDpadDirectionClicked(Direction.Right)) {
    		ballpickup.Toggle(BPState.Off);
    	}
    	
    	// Lift Section
    	if(controller.WasClicked(ButtonName.B)) {
    		lift.StartClimb();
    	}
    	
    	if(controller.IsPressed(ButtonName.B)) {
    		lift.Climb();
    	}
    	
    	// Shooter Section
    	
    	// Gyro Section
    	if(controller.WasClicked(ButtonName.Y)) {
    		gyro.SetPlayerAngle();
    	}
    	
    	if(controller.WasClicked(ButtonName.Select)) {
    		gyro.Calibrate();
    		gyro.SetPlayerAngle();
    	}
    	
    	if(controller.JustZeroed(AnalogName.RightJoyX)){
    		gyro.ResetTargetAngle();
    	}
    	
    	gyro.Update(controller.GetValue(AnalogName.RightJoyX) != 0);
    	
    	// Auto Align Section
    	if(controller.WasClicked(ButtonName.X)) {
    		if(controller.IsToggled(ButtonName.X)) {
    			auto.StartAuto(State.Aligning);
    		} else {
    			lightRelay.set(Relay.Value.kOff);
    		}
    	}
    	
    	// Speed Control Section
    	if(controller.WasClicked(ButtonName.RB) || controller.WasClicked(ButtonName.LB)) {
    		SetMaxSpeed();
    	}
    	
    	
    	// Drive Section
    	double horizontal = 0;
    	double vertical = 0;
    	double rotation = 0;
    	double gyroValue = 0;
    	
    	if(!controller.IsToggled(ButtonName.Start)) {
	    	if(controller.IsToggled(ButtonName.X)) {
	    		auto.Update();
    			horizontal = auto.GetHorizontal();
	    		vertical = auto.GetVertical();
	    		rotation = gyro.GetCompensation();
	    		gyroValue = 0;
	    	} else {
	    		horizontal = -controller.GetValue(AnalogName.LeftJoyX);
	    		vertical = -controller.GetValue(AnalogName.LeftJoyY);
	    		rotation = controller.GetValue(AnalogName.RightJoyX);
	    		rotation = rotation == 0 ? gyro.GetCompensation() : rotation;
	    		gyroValue = controller.IsToggled(ButtonName.A) ? 0 : gyro.GetAngle();
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
    	if(controller.IsPressed(ButtonName.LB) && speedMultiplier > minSpeedMultiplier) {
    		speedMultiplier -= speedMultiplierIncrement;
    	} else if (controller.IsPressed(ButtonName.RB) && speedMultiplier < maxSpeedMultiplier) {
    		speedMultiplier += speedMultiplierIncrement;
    	}
    	
    	if(speedMultiplier != lastSpeedMultiplier) {
    		robot.setMaxOutput(speedMultiplier);
    	}
    }
}