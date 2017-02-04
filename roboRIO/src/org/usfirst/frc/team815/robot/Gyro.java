package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;

public class Gyro {
	AnalogGyro gyro;
	double rotationCompensation;
	double targetAngle = 0;
	double zeroedTime = 0.2;
	Timer timer = new Timer();
	boolean timerRunning = false;
	
	public Gyro(int port) {
		gyro = new AnalogGyro(port);
	}
	
	public void Calibrate() {
		gyro.calibrate();
		targetAngle = gyro.getAngle();
	}
	
	public void ResetTargetAngle() {
		timer.reset();
		timer.start();
		timerRunning = true;
	}
	
	public double GetTargetAngle() {
		return targetAngle;
	}
	
	public void Update(boolean rotating) {
		if(rotating) {
			timer.stop();
			timerRunning = false;
			rotationCompensation = 0;
		} else if (timerRunning) {
			if(timer.get() > zeroedTime) {
				targetAngle = gyro.getAngle();
				timer.stop();
				timerRunning = false;
			}
		} else {
			double sign = Math.signum(targetAngle - gyro.getAngle());
			rotationCompensation = sign * Math.min(.1, Math.abs(targetAngle - gyro.getAngle()) / 10);
		}
	}
	
	public double GetCompensation() {
		return rotationCompensation;
	}
	
	public double GetAngle() {
		return gyro.getAngle();
	}
}
