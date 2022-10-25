package com.max128.gravity;

public class Main {
	
	static final int STEPS = 1;//Number of times to calculate steps for each frame
	static final int SPEED = 1;//Speed in passed Seconds in Simulation per real Second
	private EntityManager eR;

	public Main(EntityManager eR) {
		this.eR = eR;
	}
	
	public void mainLoop() {
		eR.moveParticles(0.0069f);
	}
}
