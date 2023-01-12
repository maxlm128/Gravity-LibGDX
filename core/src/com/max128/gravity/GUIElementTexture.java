package com.max128.gravity;

import com.badlogic.gdx.graphics.Texture;

public class GUIElementTexture extends GUIElement {
	Texture texture;
	float width, height;

	public GUIElementTexture(float posx, float posy, float width, float height, Texture texture) {
		super(posx, posy);
		this.width = width;
		this.height = height;
		this.texture = texture;
	}

}
