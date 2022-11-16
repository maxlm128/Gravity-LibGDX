package com.max128.gravity;

import com.badlogic.gdx.graphics.Texture;

public class Textures {
	
	public static Texture SUN;
	public static Texture EARTH;
	public static Texture ISS;
	public static Texture MOON;

	public static void loadTextures(){
		SUN = new Texture("sun.png");
		EARTH = new Texture("earth.png");
		ISS = new Texture("iss.png");
		MOON = new Texture("moon.png");
	}
	
	public static void disposeTextures() {
		SUN.dispose();
		EARTH.dispose();
		ISS.dispose();
		MOON.dispose();
	}
}

