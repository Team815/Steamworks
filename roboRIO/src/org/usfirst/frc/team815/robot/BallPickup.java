package org.usfirst.frc.team815.robot;

import com.ctre.CANTalon;

public class BallPickup {
	enum BPState {
		Suck(1),
		Blow(-1),
		Off(0);
		
		private int value;
		
		private BPState(int valueIn) {
			value = valueIn;
		}
		
		public int GetValue() {
			return value;
		}
	}
	
	CANTalon ballPickup;
	BPState state = BPState.Off;
	
	public BallPickup(int motorPort) {
		ballPickup = new CANTalon(motorPort);
	}
	
	public void Toggle(BPState stateIn) {
		if(state != stateIn) {
			state = stateIn;
		} else {
			state = BPState.Off;
		}
		ballPickup.set(state.GetValue());
	}
}
