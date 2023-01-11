package com.max128.gravity;

import com.badlogic.gdx.utils.Array;

/**
 * A GUI element group, which stores elements, which positions are now rendered
 * relative to the position of the element group
 **/
public class GUIElementGroup extends GUIElement {
	Array<GUIElement> subGuiElements;

	/**
	 * Creates a new GUIElementGroup with its x and y position
	 * 
	 * @param posx ,the x position of the GUI Element
	 * @param posy ,the y position of the GUI Element
	 **/
	public GUIElementGroup(float posx, float posy) {
		super(posx, posy);
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

}
