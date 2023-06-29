package com.max128.gravity;

import java.math.MathContext;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Particle {
	String name;
	BigVector2 pos; // position in meters
	BigVector2 vel; // velocity in m/s
	float r; // radius in meters
	float m; // mass in kg
	Texture tex; //Texture of the particle

	/**
	 * @param x  ,position x of the particle
	 * @param y  ,position y of the particle
	 * @param vx ,x-velocity of the particle
	 * @param vy ,y-velocity of the particle
	 * @param m  ,mass of the paricle
	 * @param r  ,radius of the particle
	 */
	public Particle(float x, float y, float vx, float vy, float m, float r, MathContext mc, Texture tex, String name) {
		pos = new BigVector2(x, y, mc);
		vel = new BigVector2(vx, vy, mc);
		this.r = r;
		this.m = m;
		this.tex = tex;
		this.name = name;
	}
}
