package com.max128.gravity;

public class Camera {
	Vector pos;
	float scale;

	public Camera() {
		scale = 0.001f;
		pos = new Vector(0, 0);
	}

	/**
	 * Moves the camera by the vector given
	 * 
	 * @param mov ,a directional Vector
	 */
	public void moveCamera(Vector mov) {
		if (mov != null) {
			pos.add(mov.scl((float) 1 / scale));
		}
	}

	/**
	 * Scales the scale of the camera by the scrolled amount given
	 * 
	 * @param amount ,a Integer
	 */
	public void zoomCamera(int amount) {
		scale += (float) (scale * amount * -0.03f);
		if (scale < 0) {
			scale *= -1;
		}
	}
}
