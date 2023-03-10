package com.max128.gravity.GUIManagement.GUIs;

import com.max128.gravity.Textures;
import com.max128.gravity.GUIManagement.GUIRenderer;
import com.max128.gravity.GUIManagement.GUIElements.GUIElement;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementEventGroup;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementText;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementTexture;

public class GameGUI extends GUI {
	/** The GUI, where informations for the Simulation are displayed **/
	public GameGUI(boolean visible,float screenWidth, float screenHeight, GUIRenderer guiR, GUIElement framesPerSecond, GUIElement frameTime,
			GUIElement gameState, GUIElement gameSpeed, GUIElement gameSteps, GUIElement timeElapsed,
			GUIElement cameraZoom, GUIElement particleCount, GUIElementTexture particleTexture,
			GUIElementText particleName, GUIElementText particleMass, GUIElementText particleRadius,
			GUIElementText particlePositionX, GUIElementText particlePositionY, GUIElementText particleVelocity,
			GUIElementText particleVelocityX, GUIElementText particleVelocityY) {
		super(visible, screenWidth, screenHeight, guiR);

		// Set Variables of Gui Elements with variables
		//Simulation
		GUIElementEventGroup simulationDisplay = new GUIElementEventGroup(0, 0, 360, 270);
		framesPerSecond.updatePos(20, 180);
		frameTime.updatePos(20, 160);
		gameSpeed.updatePos(20, 140);
		gameSteps.updatePos(20, 120);
		timeElapsed.updatePos(20, 100);
		cameraZoom.updatePos(20, 80);
		particleCount.updatePos(20, 60);

		simulationDisplay.addGUIElement(new GUIElementTexture(0, 0, 360, 270, Textures.BOX));
		simulationDisplay.addGUIElement(new GUIElementText(121, 240, "Simulation"));
		simulationDisplay.addGUIElement(framesPerSecond);
		simulationDisplay.addGUIElement(frameTime);
		simulationDisplay.addGUIElement(gameSpeed);
		simulationDisplay.addGUIElement(gameSteps);
		simulationDisplay.addGUIElement(timeElapsed);
		simulationDisplay.addGUIElement(cameraZoom);
		simulationDisplay.addGUIElement(particleCount);
		addGUIElement(simulationDisplay);
		
		//Particle
		GUIElementEventGroup particleDisplay = new GUIElementEventGroup(0, 0, 480, 360);

		particleTexture.updatePos(280, 160);
		particleName.updatePos(50, 330);
		particleMass.updatePos(10, 290);
		particleRadius.updatePos(10, 270);
		particlePositionX.updatePos(20, 230);
		particlePositionY.updatePos(20, 210);
		particleVelocity.updatePos(20, 170);
		particleVelocityX.updatePos(20, 150);
		particleVelocityY.updatePos(20, 130);

		particleDisplay.addGUIElement(new GUIElementTexture(0, 0, 480, 360, Textures.BOX));
		particleDisplay.addGUIElement(new GUIElementText(10, 250, "Position: "));
		particleDisplay.addGUIElement(new GUIElementText(10, 190, "Velocity: "));
		particleDisplay.addGUIElement(particleTexture);
		particleDisplay.addGUIElement(particleName);
		particleDisplay.addGUIElement(particleMass);
		particleDisplay.addGUIElement(particleRadius);
		particleDisplay.addGUIElement(particlePositionX);
		particleDisplay.addGUIElement(particlePositionY);
		particleDisplay.addGUIElement(particleVelocity);
		particleDisplay.addGUIElement(particleVelocityX);
		particleDisplay.addGUIElement(particleVelocityY);

		addGUIElement(particleDisplay);
	}

	@Override
	public void updateWorldDimensions(float screenWidth, float screenHeight) {
		guiElements.get(0).updatePos((screenWidth / 2) - 360, -screenHeight / 2);
		guiElements.get(1).updatePos(-screenWidth / 2, -screenHeight / 2);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void processClick(int screenX, int screenY) {
		int posx = screenX - ((int) screenWidth / 2);
		int posy = -(screenY - ((int) screenHeight / 2));
	}
}
