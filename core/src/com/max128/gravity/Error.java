package com.max128.gravity;

public class Error {
	double time;
	String err;

	public Error(String err) {
		this.err = err;
		time = (System.nanoTime() / 1E6);
	}

	/**
	 * Returns weather the Error is older than given
	 * 
	 * @param time ,the age is compared with in milliseconds
	 * @return a boolean
	 */
	public boolean isOlderThan(long time) {
		return (System.nanoTime() / 1E6) - time > this.time;
	}
}
