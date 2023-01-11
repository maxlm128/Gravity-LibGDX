package com.max128.gravity;

import com.badlogic.gdx.utils.Array;

public class GUI {
	Array<GUIElement> guiElements;
	int id;
	float screenWidth, screenHeight;

	/**
	 * General class GUI, which stores GUI elements and which are rendered in one
	 * piece. Orders the GUI elements into one type of GUI
	 **/
	public GUI(int id) {
		this.id = id;
	}

	/**
	 * Adds the given guiElement to the GUI
	 * 
	 * @param guiElement ,the GUI element with the type GUIElement
	 **/
	protected void addGUIElement(GUIElement guiElement) {
		guiElements = new Array<>(10);
		guiElements.add(guiElement);
	}

	/**
	 * The GUI reacts to the changed World Dimensions by updating the position of
	 * its elements
	 * 
	 * @param screenWidth  ,height of the screen
	 * @param screenHeight ,width of the screen
	 **/
	public void updateWorldDimensions(float screenWidth, float screenHeight) {
	}
}
