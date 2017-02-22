package org.usfirst.frc.team815.robot;

import com.ctre.CANTalon;

public class Shooter {
	private CANTalon shooter;
	
	public Shooter(int motorPort) {
		shooter = new CANTalon(motorPort);
	}
	
	public void SetShooting(boolean shooting) {
		if(shooting) {
			shooter.set(1);
		} else {
			shooter.set(2);
		}
	}
}