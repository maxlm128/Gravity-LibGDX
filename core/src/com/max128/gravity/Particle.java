package com.max128.gravity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Particle {
	String name;
	Vector2 pos; // position in meters
	Vector2 vel; // velocity in m/s
	float r; // radius in meters
	float m; // mass in kg
	Texture tex;
	

	/**
	 * @param x  ,position x of the particle
	 * @param y  ,position y of the particle
	 * @param vx ,x-velocity of the particle
	 * @param vy ,y-velocity of the particle
	 * @param m  ,mass of the paricle
	 * @param r  ,radius of the particle
	 */
	public Particle(float x, float y, float vx, float vy, float m, float r, Texture tex, String name) {
		pos = new Vector2(x, y);
		vel = new Vector2(vx, vy);
		this.r = r;
		this.m = m;
		this.tex = tex;
		this.name = name;
	}
}
