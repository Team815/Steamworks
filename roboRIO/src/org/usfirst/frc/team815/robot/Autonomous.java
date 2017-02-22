package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
	
	public enum State {
		Positioning,
		Aligning,
		Inserting,
		Done;
	}
	
	Gyro gyro;
	Camera camera = new Camera();
	Relay lightRelay;
	private double horizontal = 0;
	private double vertical = 0;
	private double timerLimit = 2;
	private double startingAngle = 0;
	private boolean turningLeft;
	private Timer timer = new Timer();
	private State state = State.Positioning;
	
	public Autonomous(Gyro gyroIn, Relay lightRelayIn) {
		gyro = gyroIn;
		lightRelay = lightRelayIn;
	}
	public void StartAuto(State startingState) {
		state = startingState;
		startingAngle = gyro.GetAngle();
		if(startingState == State.Aligning) {
			camera.StartCamera();
    		lightRelay.set(Relay.Value.kOn);
		}
		timer.reset();
		timer.start();
	}
	
	public void Update() {
		if(state == State.Positioning) {
			GetIntoPosition();
		} else if(state == State.Aligning) {
			Align(camera.ReadBuffer());
		} else if(state == State.Inserting) {
			Insert();
		} else if(state == State.Done) {
			Done();
		}
		gyro.Update(false);
	}
	
	public void GetIntoPosition() {
		double angle = turningLeft ? 60 : -60;
		if(timer.get() > timerLimit) {
			timer.stop();
			state = State.Aligning;
			camera.StartCamera();
			lightRelay.set(Relay.Value.kOn);
		} else {
			vertical = 0.3;
			gyro.SetTargetAngle(startingAngle + timer.get() * -angle / timerLimit);
		}
	}
	
	public void Align(int angle) {
		if(angle == -3) {
			horizontal = 0;
    		vertical = 0;
			state = State.Inserting;
			lightRelay.set(Relay.Value.kOff);
			timer.reset();
			timer.start();
		}
		if(angle == -2) {
    		horizontal = 0;
    		vertical = 0;
    	} else if (angle >= 0) {
    		horizontal = -0.3 * Math.cos(angle * 2 * Math.PI / 360);
    		vertical = 0.3 * Math.sin(angle * 2 * Math.PI / 360);
    	}
	}
	
	public void Insert() {
		horizontal = 0;
		vertical =  0.3;
		if(timer.get() >= 0.2) {
			timer.stop();
			state = State.Done;
		}
	}
	
	public void Done() {
		horizontal = 0;
		vertical =  0;
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
	
	public void SetTurningLeft(boolean turningLeftIn) {
		turningLeft = turningLeftIn;
	}
}
