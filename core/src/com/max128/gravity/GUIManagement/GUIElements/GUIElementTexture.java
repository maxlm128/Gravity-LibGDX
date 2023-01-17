package com.max128.gravity.GUIManagement.GUIElements;

import com.badlogic.gdx.graphics.Texture;

public class GUIElementTexture extends GUIElement {
	public Texture texture;
	public float width;
	public float height;

	public GUIElementTexture(float posx, float posy, float width, float height, Texture texture) {
		super(posx, posy);
		this.width = width;
		this.height = height;
		this.texture = texture;
	}
	
	public void updateTexture(Texture texture) {
		this.texture = texture;
	}

}
