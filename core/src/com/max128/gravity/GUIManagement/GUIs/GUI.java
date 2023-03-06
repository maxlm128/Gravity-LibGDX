package com.max128.gravity.GUIManagement.GUIs;

import com.badlogic.gdx.utils.Array;
import com.max128.gravity.GUIManagement.GUIElements.GUIElement;

public class GUI {
	protected Array<GUIElement> guiElements;
	public boolean visible;
	float screenWidth, screenHeight;

	/**
	 * General class GUI, which stores GUI elements and which are rendered in one
	 * piece. Orders the GUI elements into one type of GUI
	 **/
	public GUI(boolean visible, float screenWidth, float screenHeight) {
		this.visible = visible;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		guiElements = new Array<>(10);
	}

	/**
	 * Adds the given guiElement to the GUI
	 * 
	 * @param guiElement ,the GUI element with the type GUIElement
	 **/
	protected void addGUIElement(GUIElement guiElement) {
		guiElements.add(guiElement);
	}
	
	public Array<GUIElement> getGUIElements(){
		return guiElements;
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
	
	public void processClick(int screenX, int screenY) {	
	}
	
	public void processHover(int screenX, int screenY) {
	}
}
