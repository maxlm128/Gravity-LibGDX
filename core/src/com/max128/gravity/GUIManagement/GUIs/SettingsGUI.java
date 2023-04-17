package com.max128.gravity.GUIManagement.GUIs;

import com.badlogic.gdx.Input;
import com.max128.gravity.Textures;
import com.max128.gravity.GUIManagement.GUIRenderer;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementEventGroup;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementText;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementTexture;

public class SettingsGUI extends GUI{

	public SettingsGUI(boolean visible, float screenWidth, float screenHeight, GUIRenderer guiR) {
		super(visible, screenWidth, screenHeight, guiR);
		
		GUIElementEventGroup title = new GUIElementEventGroup(0, 0, 800, 69);
		title.addGUIElement(new GUIElementTexture(0, 0, 800, 69, Textures.UIBACKGROUND));
		title.addGUIElement(new GUIElementText(22, 22, "Settings"));
		addGUIElement(title);
	}

	@Override
	public void processKeyboardInput(int keycode) {
		if(keycode == Input.Keys.ESCAPE) {
			visible = false;
			guiR.guiVisibility(0, true);
		}
	}
}
