package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.SerialPort;

public class Camera {
	private SerialPort serialPort;
	private String buffer;
	
	public Camera() {
		serialPort = new SerialPort(9600, SerialPort.Port.kUSB);
		buffer = "";
	}
	
	public void Process() {
		
		// /ads
		
		
		buffer += serialPort.readString();
		
		if(buffer.startsWith("/")) {
			while(buffer.contains("\\")) {
				System.out.println(buffer.substring(0, buffer.indexOf('\\')+1));
				buffer = buffer.substring(buffer.indexOf('\\') + 1);
			}
		} else if (buffer.contains("/")) {
			buffer = buffer.substring(buffer.indexOf('/'));
		}
	}
}
