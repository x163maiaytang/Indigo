package com.fire.vector;

public class Vector2D {
	
	protected int x;
	protected int z;
	
	public Vector2D() {
		super();
	}

	public Vector2D(int x, int z) {
		super();
		this.x = x;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setZ(int z) {
		this.z = z;
	}

	public void moveTo(int x, int z) {
		this.x = x;
		this.z = z;
	}
}
