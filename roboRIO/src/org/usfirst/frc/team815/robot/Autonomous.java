package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
	
	public enum State {
		Positioning,
		Aligning;
	}
	
	private double horizontal = 0;
	private double vertical = 0;
	private double timerLimit = 4;
	private double startingAngle = 0;
	private Timer timer = new Timer();
	private State state = State.Positioning;
	
	public void StartAuto(Gyro gyro) {
		state = State.Positioning;
		startingAngle = gyro.GetAngle();
		timer.reset();
		timer.start();
	}
	
	public boolean GetIntoPosition(int angle, Gyro gyro) {
		if(timer.get() > timerLimit) {
			timer.stop();
			state = State.Aligning;
			return true;
		} else {
			vertical = 0.2;
			gyro.SetTargetAngle(startingAngle + timer.get() * -angle / timerLimit);
			return false;
		}
	}
	
	public void Align(int angle) {
		if(angle == -2 || angle == -3) {
    		horizontal = 0;
    		vertical = 0;
    	} else if (angle >= 0) {
    		horizontal = -0.2 * Math.cos(angle * 2 * Math.PI / 360);
    		vertical = 0.2 * Math.sin(angle * 2 * Math.PI / 360);
    	}
	}
	
	public State GetState() {
		return state;
	}
	
	public double GetHorizontal() {
		return horizontal;
	}
	
	public double GetVertical() {
		return vertical;
	}
}
