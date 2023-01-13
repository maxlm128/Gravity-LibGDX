package com.max128.gravity;

public class SimulationGUI extends GUI {
	/** The GUI, where informations for the Simulation are displayed **/
	public SimulationGUI(int id, float screenWidth, float screenHeight, GUIElement framesPerSecond, GUIElement frameTime, GUIElement gameState,
			GUIElement gameSpeed, GUIElement gameSteps, GUIElement timeElapsed, GUIElement cameraZoom,
			GUIElement particleCount) {
		super(id);
		GUIElementGroup group = new GUIElementGroup((screenWidth / 2) - 480, -screenHeight / 2);
		
		//Set Variables of Gui elements with variables
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
	}
}
