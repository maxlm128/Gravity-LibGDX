package com.max128.gravity.GUIManagement.GUIElements;

import com.badlogic.gdx.graphics.Texture;

public class GUIElementTexture extends GUIElement {
	public Texture texture;
	public float width;
	public float height;

	/**
	 * Creates a new GUIElement which contains a texture
	 * @param posx ,position x
	 * @param posy ,position y
	 * @param width ,width
	 * @param height ,height
	 * @param texture ,texture of the type Texture
	 */
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
