package org.usfirst.frc.team815.robot;

public class Box {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Box(int xIn, int yIn,  int widthIn, int heightIn) {
		x = xIn;
		y = yIn;
		width = widthIn;
		height = heightIn;
	}
	
	public void SetX(int xIn) {
		x = xIn;
	}
	
	public int GetX() {
		return x;
	}
	
	public void SetY(int yIn) {
		y = yIn;
	}
	
	public int GetY() {
		return y;
	}
	
	public void SetWidth(int widthIn) {
		width = widthIn;
	}
	
	public int GetWidth() {
		return width;
	}
	
	public void SetHeight(int heightIn) {
		height = heightIn;
	}
	
	public int GetHeight() {
		return height;
	}
}
