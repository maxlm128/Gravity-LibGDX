package com.max128.gravity;

//Controller, Model
public class Vector {
	float x, y;

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculates the directional Vector from this Vector to the Parameter Vector
	 * 
	 * @param vec2 ,second Vector
	 * @return directional Vector
	 */
	public Vector sub(Vector vec2) {
		return new Vector(vec2.x - x, vec2.y - y);
	}

	/**
	 * Moves this Vector by the directional Vector in the parameter
	 * 
	 * @param vec2, directional Vector
	 * @return This Vector
	 */
	public Vector add(Vector vec2) {
		x = x + vec2.x;
		y = y + vec2.y;
		return this;
	}

	/**
	 * Scales the Vector by the given multiplier
	 * 
	 * @param mul ,the float multiplier
	 * @return This Vector
	 */
	public Vector scl(float mul) {
		x = (float) (x * mul);
		y = (float) (y * mul);
		return this;
	}

	/**
	 * normalizes the Vector
	 * 
	 * @return This Vector
	 */
	public Vector n() {
		float l = l();
		x = x / l;
		y = y / l;
		return this;
	}

	/**
	 * Calculates the lenght of the vector
	 * 
	 * @return A float of the length
	 */
	public float l() {
		return (float) Math.sqrt(lsq());
	}

	/**
	 * Returns the length of the Vector squared, therefore faster
	 * 
	 * @return A float of the sqared lenght
	 */
	public float lsq() {
		return (float) x * x + y * y;
	}

	/**
	 * Resets the Vector to be x = 0 and y = 0
	 */
	public void reset() {
		x = 0;
		y = 0;
	}

	/**
	 * Returns a copy of this vector
	 * 
	 * @return A new Vector
	 */
	public Vector copy() {
		return new Vector(x, y);
	}
}
