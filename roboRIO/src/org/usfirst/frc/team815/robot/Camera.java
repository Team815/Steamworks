package org.usfirst.frc.team815.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

public class Camera {
	private SerialPort serialPort;
	private String buffer;
	Timer timer = new Timer();
	
	public Camera() {
		serialPort = new SerialPort(9600, SerialPort.Port.kUSB);
		buffer = "";
	}
	
	public void StartCamera() {
		timer.reset();
		timer.start();
	}
	
	public char ReadBuffer() {
		
		String buffer = serialPort.readString();
		System.out.println("Timer: " + timer.get() + ", buffer length: " + buffer.length() + ", buffer: " + buffer);
		if(buffer.length() > 0) {
			timer.reset();
			return buffer.charAt(buffer.length() - 1);
		} else if(timer.get() > 0.5) {
			return '9';
		} else {
			return '0';
		}
	}
}
