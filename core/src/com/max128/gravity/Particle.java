package com.max128.gravity;

import com.badlogic.gdx.math.Vector2;

public class Particle {
	Vector2 pos; //Position in meters
	Vector2 vel; //Velocity in m/s
	float r; //Radius in meters
	float m; //Mass in kg

	
	/**
	 * @param x ,position x of the particle
	 * @param y ,position y of the particle
	 * @param vx ,x-velocity of the particle
	 * @param vy ,y-velocity of the particle
	 * @param m ,mass of the paricle
	 * @param r ,radius of the particle
	 */
	public Particle(float x, float y, float vx, float vy, float m, float r) {
		pos = new Vector2(x, y);
		vel = new Vector2(vx, vy);
		this.r = r;
		this.m = m;
	}
}
