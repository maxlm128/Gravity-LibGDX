package com.max128.gravity;

public class SimulationGUI extends GUI {
	/** The GUI, where informations for the Simulation are displayed **/
	public SimulationGUI(int id, float screenWidth, float screenHeight, GUIElement frameTime) {
		super(id);
		GUIElementGroup group = new GUIElementGroup((screenWidth / 2) - 480, -screenHeight / 2);
		group.addGUIElement(new GUIElementTexture(0, 0, 480, 360, Textures.BOX));
		group.addGUIElement(frameTime);
		frameTime.updatePos(0, 40);
		group.addGUIElement(new GUIElementText(0, 20, "Moin Moin"));
		addGUIElement(group);
	}

	@Override
	public void updateWorldDimensions(float screenWidth, float screenHeight) {
		System.out.println(screenHeight);
		guiElements.get(0).updatePos((screenWidth / 2) - 480, -screenHeight / 2);
	}
}
