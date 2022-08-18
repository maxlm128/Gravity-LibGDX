package com.max128.gravity;

//Controller, Model
public class Particle extends Entity {
	float r;
	Vector vel;

	public Particle(float x, float y, float m, float r, float vx, float vy) {
		super(x, y, m);
		vel = new Vector(vx, vy);
		this.r = r;
	}
}
