package com.max128.gravity;

import com.badlogic.gdx.utils.Array;

//Controller
public class EntityManager {
	private Array<Particle> p;
	private Entity mouse;
	private final int PARTICLE_LIMIT = 5000;
	boolean mouseGravity;
	final protected float G = 6.6743E-11f;

	public EntityManager() {
		mouseGravity = false;
		mouse = new Entity(0, 0, 5E27f);
		p = new Array<Particle>();
		newParticle(0, 6.371E6f + 400000, 440000, 55, 7660, 0); // ISS
		newParticle(0, 0, 5.972E24f, 6.371E6f, 0, 0); // Earth
		newParticle(0, 384403000, 7.3483E22f, 1738000, 1023, 0); // Moon
	}

	/**
	 * Inserts a Particle into the Entity-Array and returs whether it was successful
	 * 
	 * @param x  Position of the Particle
	 * @param y  Position of the Particle
	 * @param m  Mass of the Particle
	 * @param r  Radius of the Particle
	 * @param vx Velocity of the Particle
	 * @param vy Velocity of the Particle
	 * @return boolean if inserting was successful
	 */
	public boolean newParticle(float x, float y, float m, float r, float vx, float vy) {
		if (p.size > PARTICLE_LIMIT) {
			return false;
		}
		p.add(new Particle(x, y, m, r, vx, vy));
		return true;
	}

	/**
	 * deletes all Entities
	 */
	public void deleteAllEntities() {
		p = new Array<Particle>();
	}

	/**
	 * Executes the move-Method of every Entity STEPS times with the delta-time dt
	 * 
	 * @param dt ,a float
	 */
	public void moveParticles(float dt) {
		for (int i = 0; i < Main.STEPS; i++) {
			for (int j = 0; j < p.size; i++) {
				resolveCollision();
				p.get(j).pos.add(p.get(j).vel.copy().scl(dt / Main.STEPS));
				calcGravity(dt);
			}
		}
	}

	/**
	 * Calculates the gravity from each Entity to every other Particle and appyling
	 * it on the Velocity of the Particle
	 * 
	 * @param dt ,delta time
	 */
	public void calcGravity(float dt) {
		for (int i = 0; i < p.size; i++) {
			for (int j = i + 1; j < p.size; j++) {
				Vector dv = p.get(i).pos.sub(p.get(j).pos);
				float l = dv.lsq();
				dv.n().scl(G / l).scl(dt);
				p.get(i).vel.add(dv.copy().scl(p.get(j).m));
				p.get(j).vel.add(dv.scl(p.get(i).m * -1));
			}
		}
	}

	/**
	 * identifies a collision and resolves it using static and soft collision
	 */
	public void resolveCollision() {
		for (int ü = 0; ü < p.size; ü++) {
			for (int j = ü + 1; j < p.size; j++) {
				if (p.get(ü).pos.sub(p.get(j).pos).lsq() <= p.get(ü).r * p.get(ü).r + p.get(j).r * p.get(j).r) {
					if (p.get(ü).pos.x == p.get(j).pos.x && p.get(ü).pos.y == p.get(j).pos.y) {
						Vector random = new Vector(((float) Math.random() - 0.5f) * 1000,
								((float) Math.random() - 0.5f) * 1000);
						p.get(ü).pos.add(random);
					}
					Vector d = p.get(ü).pos.sub(p.get(j).pos).n()
							.scl(p.get(j).r + p.get(ü).r - p.get(ü).pos.sub(p.get(j).pos).l()).scl(0.5f);
					p.get(j).pos.add(d);
					p.get(ü).pos.add(d.scl(-1));
					// Soft Collision
					// Distance between balls
					float fDistance = p.get(ü).pos.sub(p.get(j).pos).l();

					// Normal
					float nx = (p.get(j).pos.x - p.get(ü).pos.x) / fDistance;
					float ny = (p.get(j).pos.y - p.get(ü).pos.y) / fDistance;

					// Tangent
					float tx = -ny;
					float ty = nx;

					// Dot Product Tangent
					float dpTan1 = p.get(ü).vel.x * tx + p.get(ü).vel.y * ty;
					float dpTan2 = p.get(j).vel.x * tx + p.get(j).vel.y * ty;

					// Dot Product Normal
					float dpNorm1 = p.get(ü).vel.x * nx + p.get(ü).vel.y * ny;
					float dpNorm2 = p.get(j).vel.x * nx + p.get(j).vel.y * ny;

					// Conservation of momentum in 1D
					float m1 = (dpNorm1 * (p.get(ü).m - p.get(j).m) + 2.0f * p.get(j).m * dpNorm2)
							/ (p.get(ü).m + p.get(j).m);
					float m2 = (dpNorm2 * (p.get(j).m - p.get(ü).m) + 2.0f * p.get(ü).m * dpNorm1)
							/ (p.get(ü).m + p.get(j).m);

					// Update ball velocities
					p.get(ü).vel.x = tx * dpTan1 + nx * m1;
					p.get(ü).vel.y = ty * dpTan1 + ny * m1;
					p.get(j).vel.x = tx * dpTan2 + nx * m2;
					p.get(j).vel.y = ty * dpTan2 + ny * m2;
				}
			}
		}
	}

	/**
	 * Returns a Array of all Particles in the Game
	 * 
	 * @return a Particle Array
	 */
	public Array<Particle> getParticles() {
		return p;
	}

	/**
	 * Returns the Number of total Particles in the Game
	 * 
	 * @return A Integer
	 */
	public int getNumberP() {
		return p.size - 1;
	}

	/**
	 * Updates the position of the mouse and therefore the source of gravity using a
	 * position vector
	 * 
	 * @param pos, a Vector
	 */
	public void updateMousePos(Vector pos) {
		mouse.pos = pos;
	}

}
