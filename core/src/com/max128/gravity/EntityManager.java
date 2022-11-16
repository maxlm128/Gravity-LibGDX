package com.max128.gravity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class EntityManager {

	final static int STEPS = 100; // calculation steps per move
	float speed = 1; // ingame-seconds per real seconds
	Array<Particle> p;
	final protected float G = 6.6743E-11f;
	boolean running;

	public EntityManager() {
		p = new Array<Particle>();
		p.add(new Particle(1E7f, 0, 29780f, 0, 5.972E24f, 6.371E6f, Textures.EARTH)); // Earth
		p.add(new Particle(1E7f, 149600000000f, 0, 0, 1.9884E30f, 696340000f, Textures.SUN)); // Sun
		p.add(new Particle(1E7f, 384403000, 1023 + 29780f, 0, 7.3483E22f, 1738000, Textures.MOON)); // Moon
		p.add(new Particle(1E7f, 6.371E6f + 400000, 7660 + 29780f, 0, 440000, 55, Textures.ISS)); // ISS
	}

	public void moveParticles(float dt) {
		if (running) {
			for (int i = 0; i < STEPS; i++) {
				calcGravity((dt * speed) / STEPS);
				for (Particle p : p) {
					resolveCollision(p);
					p.pos.add(p.vel.cpy().scl((speed * dt) / STEPS));
				}
			}
		}
	}

	public Particle posInParticle(Vector3 pos) {
		for (Particle p : p) {
			if (p.pos.dst(pos.x, pos.y) <= p.r) {
				return p;
			}
		}
		return null;
	}

	private void calcGravity(float dt) {
		for (int i = 0; i < p.size; i++) {
			for (int j = i + 1; j < p.size; j++) {
				Vector2 dv = p.get(j).pos.cpy().sub(p.get(i).pos);
				float l = dv.len2();
				dv.nor().scl(G / l).scl(dt);
				p.get(i).vel.add(dv.cpy().scl(p.get(j).m));
				p.get(j).vel.add(dv.scl(p.get(i).m * -1));
			}
		}
	}

	private void resolveCollision(Particle p) {

	}

	public Array<Particle> getP() {
		return this.p;
	}
}
