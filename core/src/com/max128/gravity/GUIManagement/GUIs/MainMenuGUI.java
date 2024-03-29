package com.max128.gravity.GUIManagement.GUIs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.max128.gravity.Textures;
import com.max128.gravity.GUIManagement.GUIRenderer;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementEventGroup;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementText;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementTexture;

public class MainMenuGUI extends GUI {

	private float earthX;
	private int playXTarget;
	private float playX;
	private int settingsXTarget;
	private float settingsX;
	private int quitXTarget;
	private float quitX;

	public MainMenuGUI(boolean visible, float screenWidth, float screenHeight, GUIRenderer guiR) {
		super(visible, screenWidth, screenHeight, guiR);

		addGUIElement(new GUIElementTexture(252,0,10,10,Textures.SUN));
		addGUIElement(new GUIElementTexture(0, -250, 1000, 1000, Textures.EARTH));

		GUIElementEventGroup playButton = new GUIElementEventGroup(0, 0, 800, 69);
		playButton.addGUIElement(new GUIElementTexture(0, 0, 800, 69, Textures.UIBACKGROUND));
		playButton.addGUIElement(new GUIElementTexture(731, 0, 69, 69, Textures.PLAY));
		playButton.addGUIElement(new GUIElementText(22, 22, "Play"));
		addGUIElement(playButton);

		GUIElementEventGroup settingsButton = new GUIElementEventGroup(0, 0, 800, 69);
		settingsButton.addGUIElement(new GUIElementTexture(0, 0, 800, 69, Textures.UIBACKGROUND));
		settingsButton.addGUIElement(new GUIElementTexture(740, 9.5f, 50, 50, Textures.SETTINGS));
		settingsButton.addGUIElement(new GUIElementText(22, 22, "Settings"));
		addGUIElement(settingsButton);

		GUIElementEventGroup quitButton = new GUIElementEventGroup(0, 0, 800, 69);
		quitButton.addGUIElement(new GUIElementTexture(0, 0, 800, 69, Textures.UIBACKGROUND));
		quitButton.addGUIElement(new GUIElementTexture(745, 9.5f, 50, 50, Textures.QUIT));
		quitButton.addGUIElement(new GUIElementText(22, 22, "Quit"));
		addGUIElement(quitButton);

		addGUIElement(new GUIElementTexture(500, -500, 200, 200, Textures.MOON));
		addGUIElement(new GUIElementTexture(-206, 400, 413, 131, Textures.GRAVITY));
		addGUIElement(new GUIElementTexture(-386, 373, 200, 200, Textures.PLAY));
	}

	@Override
	public void updateWorldDimensions(float screenWidth, float screenHeight) {
		guiElements.get(1).updatePos((-screenWidth / 2) + 50, (screenHeight / 2) - 400);
		guiElements.get(2).updatePos((-screenWidth / 2) + 50, (screenHeight / 2) - 500);
		guiElements.get(3).updatePos((-screenWidth / 2) + 50, (screenHeight / 2) - 600);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void processClick(int screenX, int screenY) {
		int posx = screenX - ((int) screenWidth / 2);
		int posy = -(screenY - ((int) screenHeight / 2));
		if (((GUIElementEventGroup) guiElements.get(2)).isInGroupArea(posx, posy)) {
			visible = false;
			guiR.guiVisibility(1, true);
			guiR.gameRendering(true);
		}
		if (((GUIElementEventGroup) guiElements.get(3)).isInGroupArea(posx, posy)) {
			visible = false;
			guiR.guiVisibility(2, true);
		}
		if (((GUIElementEventGroup) guiElements.get(4)).isInGroupArea(posx, posy)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void processHover(int screenX, int screenY) {
		int posx = screenX - ((int) screenWidth / 2);
		int posy = -(screenY - ((int) screenHeight / 2));
		if (((GUIElementEventGroup) guiElements.get(2)).isInGroupArea(posx, posy)) {
			playXTarget = 150;
		} else {
			playXTarget = 50;
		}
		if (((GUIElementEventGroup) guiElements.get(3)).isInGroupArea(posx, posy)) {
			settingsXTarget = 150;
		} else {
			settingsXTarget = 50;
		}
		if (((GUIElementEventGroup) guiElements.get(4)).isInGroupArea(posx, posy)) {
			quitXTarget = 150;
		} else {
			quitXTarget = 50;
		}
	}
	


	@Override
	public void animate() {
		if (earthX < 3000) {
			earthX += 0.2f;
		} else {
			earthX = 0;
		}
		guiElements.get(1).updatePos(-2000 + earthX, -earthX / 5);
		guiElements.get(5).updatePos(1000 - earthX, -750 + earthX / 5);
		if(Math.abs((playXTarget - playX)) > 0.1f) {
			playX += (playXTarget - playX) * 0.01f;		
		} else {
			playX = playXTarget;
		}
		if(Math.abs((settingsXTarget - settingsX)) > 0.1f) {
			settingsX += (settingsXTarget - settingsX) * 0.01f;			
		} else {
			settingsX = settingsXTarget;
		}
		if(Math.abs((quitXTarget - quitX)) > 0.1f) {
			quitX += (quitXTarget - quitX) * 0.01f;			
		} else {
			quitX = quitXTarget;
		}
		((GUIElementEventGroup) guiElements.get(2)).updatePos((-screenWidth / 2) + playX, (screenHeight / 2) - 400);
		((GUIElementEventGroup) guiElements.get(3)).updatePos((-screenWidth / 2) + settingsX, (screenHeight / 2) - 500);
		((GUIElementEventGroup) guiElements.get(4)).updatePos((-screenWidth / 2) + quitX, (screenHeight / 2) - 600);
	}
}
