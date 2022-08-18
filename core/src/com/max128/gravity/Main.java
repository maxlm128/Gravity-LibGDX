package com.max128.gravity;

public class Main extends Thread {

	private final GUI gui;
	private final EntityManager eM;
	private long lastTimestamp;
	private long lastTimestamp2;
	private boolean running;
	static final float SPEED = 23.25f * 60f; // Ingame Seconds per real Second (One ISS Orbit per 4 Seconds)
//	static final float SPEED = 86400 * 29; // Ingame Seconds per real Second (1 Day per Second)
	static final int STEPS = 10;
	static final int MSPC = 1000 / 144;// Milliseconds per calculation

	public Main(GUI gui) {
		this.gui = gui;
		eM = new EntityManager();
		lastTimestamp = System.nanoTime();
		lastTimestamp2 = (long) (System.nanoTime() * 1E-6);
		running = true;
		this.start();
	}

	public void run() {
		mainLoop();
	}

	/**
	 * Main-Loop of the Game
	 */
	private void mainLoop() {
		float dt;
		while (gui.running) {
			System.out.println("sld");
			dt = getDeltaTime();
			if (running) {
				eM.moveParticles((dt * SPEED));
			}
			gui.updateParticleArray(eM.getParticles());
			sleep();
		}
	}

	/**
	 * Sleeps for the time of MSPF minus the time the calculation of the remaining
	 * components, exept the time needed for the calculation is greater than MSPF,
	 * therefore trying to reach the target-frametime of MSPF
	 */
	private void sleep() {
		int t = (int) (System.nanoTime() * 1E-6 - lastTimestamp2);
		if (t < MSPC) {
			try {
				Thread.sleep(MSPC - t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lastTimestamp2 = (long) (System.nanoTime() * 1E-6);
	}

	/**
	 * Returns the delta time in seconds since the last frame
	 * 
	 * @return A Float
	 */
	private float getDeltaTime() {
		float dt = (System.nanoTime() - lastTimestamp) / 1E9f;
		lastTimestamp = System.nanoTime();
		return dt;
	}

	/**
	 * Converts one Position on the Screen to a Position of the ingame coorinates
	 * 
	 * @param pos ,the Coordinates on the screen in form of a Vector
	 * @return The Vector of the Position on the screen
	 */
	private Vector screenToGamePos(Vector pos) {
		pos.x = ((pos.x - GUI.WIDTH / 2) / gui.getCamera().scale) - gui.getCamera().pos.x;
		pos.y = ((pos.y - GUI.HEIGHT / 2) / gui.getCamera().scale) - gui.getCamera().pos.y;
		return pos;
	}
}
