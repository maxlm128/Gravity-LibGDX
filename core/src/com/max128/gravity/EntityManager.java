package com.max128.gravity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class EntityManager {

	final static int STEPS = 100; //calculation steps per move
	float speed = 1; //ingame-seconds per real seconds
	Array<Particle> p;
	final protected float G = 6.6743E-11f;
	boolean running;

	public EntityManager() {
		running = true;
		p = new Array<Particle>();
//		p.add(new Particle(0, 0, 2.0f, 0, 1E15f, 6369000));
		p.add(new Particle(0, 0, 2.0f, 0, 1E15f, 10));
		p.add(new Particle(0, 100, -1f, 0, 1E15f, 10));
		p.add(new Particle(0, 1000, 1f, 0, 1E15f, 10));
		p.add(new Particle(0, 1100, -2f, 0, 1E15f, 10));
	}

	public void moveParticles(float dt) {
		if (running) {
			for (int i = 0; i < STEPS; i++) {
				calcGravity((dt * speed) / STEPS);
				for (Particle p : p) {
					resolveCollision(p);
					p.pos.add(p.vel.cpy().scl(speed / STEPS));
				}
			}
		}
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
