package org.usfirst.frc.team815.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {
	private CANTalon lift;
	private Timer timer = new Timer();
	private final double MAX_SPEED = 1;
	private final double SPEEDUP_TIME = 1;
	
	public Lift(int motorPort) {
		lift = new CANTalon(motorPort);
	}
	
	public void StartClimb() {
		timer.reset();
		timer.start();
	}
	
	public void Climb() {
		if(timer.get() < SPEEDUP_TIME) {
			lift.set(timer.get() * MAX_SPEED);
		} else {
			timer.stop();
			lift.set(MAX_SPEED);
		}
	}
}
