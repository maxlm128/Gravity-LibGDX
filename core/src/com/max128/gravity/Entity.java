package com.max128.gravity;

public class Entity {
	float m;
	Vector pos;

	public Entity(float x, float y, float m) {
		pos = new Vector(x, y);
		this.m = m;
		pos.x = x;
		pos.y = y;
	}
}
