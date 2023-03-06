package com.max128.gravity.GUIManagement.GUIs;

import com.max128.gravity.Textures;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementEventGroup;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementText;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementTexture;

public class ParticleGUI extends GUI {

	// Texture Name Mass Radius PositionXY VelocityXY

	public ParticleGUI(boolean visible, float screenWidth, float screenHeight, GUIElementTexture particleTexture,
			GUIElementText particleName, GUIElementText particleMass, GUIElementText particleRadius,
			GUIElementText particlePositionX, GUIElementText particlePositionY, GUIElementText particleVelocity,
			GUIElementText particleVelocityX, GUIElementText particleVelocityY) {
		super(visible, screenWidth, screenHeight);
		GUIElementEventGroup group = new GUIElementEventGroup(0, 0, 480, 360);

		particleTexture.updatePos(280, 160);
		particleName.updatePos(50, 340);
		particleMass.updatePos(10, 300);
		particleRadius.updatePos(10, 280);
		particlePositionX.updatePos(20, 240);
		particlePositionY.updatePos(20, 220);
		particleVelocity.updatePos(20, 180);
		particleVelocityX.updatePos(20, 160);
		particleVelocityY.updatePos(20, 140);

		group.addGUIElement(new GUIElementTexture(0, 0, 480, 360, Textures.BOX));
		group.addGUIElement(new GUIElementText(10, 260, "Position: "));
		group.addGUIElement(new GUIElementText(10, 200, "Velocity: "));
		group.addGUIElement(particleTexture);
		group.addGUIElement(particleName);
		group.addGUIElement(particleMass);
		group.addGUIElement(particleRadius);
		group.addGUIElement(particlePositionX);
		group.addGUIElement(particlePositionY);
		group.addGUIElement(particleVelocity);
		group.addGUIElement(particleVelocityX);
		group.addGUIElement(particleVelocityY);

		addGUIElement(group);
	}

	@Override
	public void updateWorldDimensions(float screenWidth, float screenHeight) {
		guiElements.get(0).updatePos(-screenWidth / 2, -screenHeight / 2);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void processClick(int screenX, int screenY) {
		int posx = screenX - ((int) screenWidth / 2);
		int posy = -(screenY - ((int) screenHeight / 2));
		System.out.println("ParticleGUI: " + ((GUIElementEventGroup) guiElements.get(0)).isInGroupArea(posx, posy));
	}
}
