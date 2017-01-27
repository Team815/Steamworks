package org.usfirst.frc.team815.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.SerialPort;

public class Camera {
	private SerialPort serialPort;
	private String buffer;
	
	public Camera() {
		serialPort = new SerialPort(9600, SerialPort.Port.kUSB);
		buffer = "";
	}
	
	public void ReadBuffer() {
		
		buffer += serialPort.readString();
		
		if(buffer.startsWith("/")) {
			while(buffer.contains("\\")) {
				String message = buffer.substring(0, buffer.indexOf('\\')+1);
				ProcessBoxes(GetBoxes(message));
				buffer = buffer.substring(buffer.indexOf('\\') + 1);
			}
		} else if (buffer.contains("/")) {
			buffer = buffer.substring(buffer.indexOf('/'));
		}
	}
	
	private ArrayList<Box> GetBoxes(String message) {
		ArrayList<Box> boxes = new ArrayList<Box>();
		int xLocation = message.indexOf("x",  0);
		
		while(xLocation != -1) {
			String lookFor = message.indexOf("x", xLocation + 1) == -1 ? "\\" : "x";
			
			int x = Integer.valueOf(message.substring(message.indexOf("x",  xLocation) + 1, message.indexOf("y",  xLocation)));
			int y = Integer.valueOf(message.substring(message.indexOf("y",  xLocation) + 1, message.indexOf("w",  xLocation)));
			int w = Integer.valueOf(message.substring(message.indexOf("w",  xLocation) + 1, message.indexOf("h",  xLocation)));
			int h = Integer.valueOf(message.substring(message.indexOf("h",  xLocation) + 1, message.indexOf(lookFor,  xLocation+1)));
			
			boxes.add(new Box(x, y, w, h));
			
			xLocation = message.indexOf("x",  xLocation + 1);
		}
		
		return boxes;
	}
	
	private void ProcessBoxes(ArrayList<Box> boxes) {
		if(boxes.size() == 2) {
			String output = "";
			int box1x = boxes.get(0).GetX();
			int box2x = boxes.get(1).GetX();
			int separation = Math.abs(box1x - box2x);
			int midpoint = (box1x + box2x) / 2;
			
			if(separation < 160) {
				output += "Get closer. ";
			}
			if(midpoint < 160) {
				output += "Move right.";
			}
			if(midpoint > 160) {
				output += "Move left.";
			}
			
			System.out.println(output);
		} else {
			for(Box i : boxes) {
				System.out.print("(" + i.GetX() + ", " + i.GetY() + ");  ");
			}
			System.out.println("");
		}
	}
}
