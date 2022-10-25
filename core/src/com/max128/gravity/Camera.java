package com.max128.gravity;

import com.badlogic.gdx.math.Vector2;

public class Camera {
	Vector2 pos;
	float scale; //How many Pixels represent one meter in the simulation
	
	public Camera(float x, float y, float scale) {
		pos = new Vector2(x,y);
		this.scale = scale;
	}
}
