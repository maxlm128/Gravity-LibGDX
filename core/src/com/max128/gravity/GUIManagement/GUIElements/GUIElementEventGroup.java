package com.max128.gravity.GUIManagement.GUIElements;

import com.badlogic.gdx.utils.Array;

/**
 * A GUI element group, which stores elements, which positions are now rendered
 * relative to the position of the element group
 **/
public class GUIElementEventGroup extends GUIElement {
	public Array<GUIElement> subGuiElements;
	int width, height;

	/**
	 * Creates a new GUIElementGroup with its x and y position
	 * 
	 * @param posx ,the x position of the GUI Element
	 * @param posy ,the y position of the GUI Element
	 **/
	public GUIElementEventGroup(float posx, float posy, int width, int height) {
		super(posx, posy);
		this.width = width;
		this.height = height;
		this.subGuiElements = new Array<GUIElement>(10);
	}

	/**
	 * Adds the given GUI element to the GUI element group
	 * 
	 * @param guiElement ,the GUI element with the type GUIElement
	 **/
	public void addGUIElement(GUIElement guiElement) {
		subGuiElements.add(guiElement);
	}

	public boolean isInGroupArea(int posx, int posy) {
		return (0 <= posx - pos.x && posx - pos.x <= width && 0 <= posy - pos.y && posy - pos.y <= height);
	}

}
