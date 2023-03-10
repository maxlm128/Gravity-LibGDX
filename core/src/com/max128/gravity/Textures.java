package com.max128.gravity;

import com.badlogic.gdx.graphics.Texture;
/** Manages the Textures **/
public class Textures {

	public static Texture SUN,EARTH,ISS,MOON,BOX,PLAY,QUIT,SETTINGS,UIBACKGROUND,GRAVITY;

	/** Loads the textures from local files into the graphics-card memory and stores
	them into variables **/
	public static void loadTextures() {
		SUN = new Texture("textures/particles/sun.png");
		EARTH = new Texture("textures/particles/earth.png");
		ISS = new Texture("textures/particles/iss.png");
		MOON = new Texture("textures/particles/moon.png");
		BOX = new Texture("textures/gui/box.png");
		PLAY = new Texture("textures/gui/play.png");
		QUIT = new Texture("textures/gui/quit.png");
		SETTINGS = new Texture("textures/gui/settings.png");
		UIBACKGROUND = new Texture("textures/gui/uibackground.png");
		GRAVITY = new Texture("textures/gui/gravity.png");
		
	}

	/** Disposes the textures **/
	public static void disposeTextures() {
		SUN.dispose();
		EARTH.dispose();
		ISS.dispose();
		MOON.dispose();
		BOX.dispose();
		PLAY.dispose();
		QUIT.dispose();
		SETTINGS.dispose();
		UIBACKGROUND.dispose();
		GRAVITY.dispose();
	}
}
