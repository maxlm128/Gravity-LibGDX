package com.max128.gravity.GUIManagement.GUIs;

import com.max128.gravity.Textures;
import com.max128.gravity.GUIManagement.GUIElements.GUIElement;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementEventGroup;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementText;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementTexture;

public class SimulationGUI extends GUI {
	/** The GUI, where informations for the Simulation are displayed **/
	public SimulationGUI(boolean visible,float screenWidth, float screenHeight, GUIElement framesPerSecond, GUIElement frameTime,
			GUIElement gameState, GUIElement gameSpeed, GUIElement gameSteps, GUIElement timeElapsed,
			GUIElement cameraZoom, GUIElement particleCount) {
		super(visible, screenWidth, screenHeight);
		GUIElementEventGroup group = new GUIElementEventGroup(0, 0, 360, 270);

		// Set Variables of Gui elements with variables
		framesPerSecond.updatePos(20, 190);
		frameTime.updatePos(20, 170);
		gameSpeed.updatePos(20, 150);
		gameSteps.updatePos(20, 130);
		timeElapsed.updatePos(20, 110);
		cameraZoom.updatePos(20, 90);
		particleCount.updatePos(20, 70);

		group.addGUIElement(new GUIElementTexture(0, 0, 360, 270, Textures.BOX));
		group.addGUIElement(new GUIElementText(121, 250, "Simulation"));
		group.addGUIElement(framesPerSecond);
		group.addGUIElement(frameTime);
		group.addGUIElement(gameSpeed);
		group.addGUIElement(gameSteps);
		group.addGUIElement(timeElapsed);
		group.addGUIElement(cameraZoom);
		group.addGUIElement(particleCount);
		addGUIElement(group);
	}

	@Override
	public void updateWorldDimensions(float screenWidth, float screenHeight) {
		guiElements.get(0).updatePos((screenWidth / 2) - 360, -screenHeight / 2);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void processClick(int screenX, int screenY) {
		int posx = screenX - ((int) screenWidth / 2);
		int posy = -(screenY - ((int) screenHeight / 2));
		System.out.println("SimulationGUI: " + ((GUIElementEventGroup) guiElements.get(0)).isInGroupArea(posx, posy));
	}
}
