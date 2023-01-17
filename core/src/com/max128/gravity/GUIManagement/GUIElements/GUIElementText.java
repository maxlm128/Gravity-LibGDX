package com.max128.gravity.GUIManagement.GUIElements;

public class GUIElementText extends GUIElement {
	String textContent;

	/** GUI element which contains text to display **/
	public GUIElementText(int posx, int posy, String textContent) {
		super(posx, posy);
		this.textContent = textContent;
	}

	/** Return the text content of this element **/
	public String getTextContent() {
		return textContent;
	}

	/** Updates the text content of this element 
	 * 
	 * @param textContent ,a String for the text content
	 * **/
	public void updateTextContent(String textContent) {
		this.textContent = textContent;
	}
}
