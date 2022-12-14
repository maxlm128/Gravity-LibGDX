package com.max128.gravity;

import com.badlogic.gdx.math.Vector2;

/**
 * General GUI element, which has a position and is rendered by the GUIRenderer
 **/
public class GUIElement {
	Vector2 pos;

	/**
	 * creates a new GUIElement
	 * 
	 * @param posx ,x position of the GUI element
	 * @param posy ,y position of the GUI element
	 **/
	public GUIElement(float posx, float posy) {
		pos = new Vector2(posx, posy);
	}

	/**
	 * updates the position of the GUI element
	 * 
	 * @param posx ,x position of the GUI element
	 * @param posy ,y position of the GUI element
	 **/
	public void updatePos(float posx, float posy) {
		pos.x = posx;
		pos.y = posy;
	}
}
