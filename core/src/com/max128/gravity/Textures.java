package com.max128.gravity;

import com.badlogic.gdx.graphics.Texture;
/** Manages the Textures **/
public class Textures {

	public static Texture SUN;
	public static Texture EARTH;
	public static Texture ISS;
	public static Texture MOON;
	public static Texture BOX;

	/** Loads the textures from local files into the graphics-card memory and stores
	them into variables **/
	public static void loadTextures() {
		SUN = new Texture("textures/particles/sun.png");
		EARTH = new Texture("textures/particles/earth.png");
		ISS = new Texture("textures/particles/iss.png");
		MOON = new Texture("textures/particles/moon.png");
		BOX = new Texture("textures/gui/box.png");
	}

	/** Disposes the textures **/
	public static void disposeTextures() {
		SUN.dispose();
		EARTH.dispose();
		ISS.dispose();
		MOON.dispose();
		BOX.dispose();
	}
}
