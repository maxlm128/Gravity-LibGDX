package com.max128.gravity.GUIManagement;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.max128.gravity.GUIManagement.GUIElements.GUIElement;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementEventGroup;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementText;
import com.max128.gravity.GUIManagement.GUIElements.GUIElementTexture;
import com.max128.gravity.GUIManagement.GUIs.GUI;
import com.max128.gravity.GUIManagement.GUIs.ParticleGUI;
import com.max128.gravity.GUIManagement.GUIs.SimulationGUI;

/** Class for rendering and managing all GUIs **/
public class GUIRenderer {

	private SpriteBatch batch;
	private BitmapFont font;
	private DecimalFormat dF;

	// Variables for GUIs
	// SimulationGUI
	private GUIElementText framesPerSecond;
	private GUIElementText frameTime;
	private GUIElementText gameState;
	private GUIElementText gameSpeed;
	private GUIElementText gameSteps;
	private GUIElementText timeElapsed;
	private GUIElementText cameraZoom;
	private GUIElementText particleCount;
	// ParticleGUI
	private GUIElementTexture particleTexture;
	private GUIElementText particleName;
	private GUIElementText particleMass;
	private GUIElementText particleRadius;
	private GUIElementText particlePositionX;
	private GUIElementText particlePositionY;
	private GUIElementText particleVelocity;
	private GUIElementText particleVelocityX;
	private GUIElementText particleVelocityY;

	private float frameTimeCounter;
	private int frameTimeAddingCounter;
	private Viewport staticViewport;
	private Array<GUI> guis;

	public GUIRenderer(SpriteBatch batch, Viewport staticViewport, int gameSteps) {

		this.staticViewport = staticViewport;

		// Variable GUIElements for the GUIs
		// SimulationGUI
		framesPerSecond = new GUIElementText(0, 0, "FPS: 0 frames/s");
		frameTime = new GUIElementText(0, 0, "FrameTime: 0 ms/frame");
		gameState = new GUIElementText(0, 0, "");
		this.gameSpeed = new GUIElementText(0, 0, "");
		this.gameSteps = new GUIElementText(0, 0, "");
		timeElapsed = new GUIElementText(0, 0, "");
		cameraZoom = new GUIElementText(0, 0, "");
		particleCount = new GUIElementText(0, 0, "");
		// ParticleGUI
		particleTexture = new GUIElementTexture(0, 0, 150, 150, null);
		particleName = new GUIElementText(0, 0, "");
		particleMass = new GUIElementText(0, 0, "");
		particleRadius = new GUIElementText(0, 0, "");
		particlePositionX = new GUIElementText(0, 0, "");
		particlePositionY = new GUIElementText(0, 0, "");
		particleVelocity = new GUIElementText(0, 0, "");
		particleVelocityX = new GUIElementText(0, 0, "");
		particleVelocityY = new GUIElementText(0, 0, "");

		// Setup of the GUIs
		guis = new Array<>(10);
		// Simulation GUI
		guis.add(new SimulationGUI(true, staticViewport.getWorldWidth(), staticViewport.getWorldHeight(),
				framesPerSecond, frameTime, gameState, gameSpeed, this.gameSteps, timeElapsed, cameraZoom,
				particleCount));
		guis.add(new ParticleGUI(true, staticViewport.getWorldWidth(), staticViewport.getWorldHeight(),
				particleTexture, particleName, particleMass, particleRadius, particlePositionX, particlePositionY,
				particleVelocity, particleVelocityX, particleVelocityY));

		this.batch = batch;
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 18;
		parameter.borderWidth = 2f;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		font = new FreeTypeFontGenerator(Gdx.files.internal("fonts/consolas.ttf")).generateFont(parameter);
		dF = new DecimalFormat("0.000");
	}

	public void processClick(int screenX, int screenY) {
		for (GUI gui : guis) {
			if (gui.visible) {
				gui.processClick(screenX, screenY);
			}
		}
	}

	public void processHover(int screenX, int screenY) {
		for (GUI gui : guis) {
			if (gui.visible) {
				gui.processHover(screenX, screenY);
			}
		}
	}

	public void updateParticleVelocity(Vector2 vel) {
		particleVelocity
				.updateTextContent("Vel: " + dF.format(numberToFormattedNumber(vel.len())) + getUnit(vel.len()) + "/s");
		particleVelocityX.updateTextContent("X: " + dF.format(numberToFormattedNumber(vel.x)) + getUnit(vel.x) + "/s");
		particleVelocityY.updateTextContent("Y: " + dF.format(numberToFormattedNumber(vel.y)) + getUnit(vel.y) + "/s");
	}

	public void updateParticlePosition(Vector2 pos) {
		particlePositionX.updateTextContent("X: " + dF.format(numberToFormattedNumber(pos.x)) + getUnit(pos.x));
		particlePositionY.updateTextContent("Y: " + dF.format(numberToFormattedNumber(pos.y)) + getUnit(pos.y));
	}

	public void updateParticleRadius(float particleRadius) {
		this.particleRadius.updateTextContent("Radius: " + particleRadius);
	}

	public void updateParticleMass(float particleMass) {
		this.particleMass.updateTextContent("Mass: " + particleMass);
	}

	public void updateParticleName(String particleName) {
		this.particleName.updateTextContent(particleName);
		;
	}

	public void updateParticleTexure(Texture particleTex) {
		this.particleTexture.updateTexture(particleTex);
		;
	}

	/** Draws all the GUIs stored in the GUIRenderer **/
	public void drawCurrentGUIs() {
		updateAverageFrameTime();
		updateFramesPerSecond();
		batch.setProjectionMatrix(staticViewport.getCamera().combined);
		for (GUI gui : guis) {
			if (gui.visible) {
				for (GUIElement guiElement : gui.getGUIElements()) {
					drawGUIElement(guiElement, guiElement.pos.x, guiElement.pos.y);
				}
			}
		}
	}

	/**
	 * Draws the GUIElement given
	 * 
	 * @param guiElement ,the guiElement that is drawn
	 * @param posx       ,the x-position of the guiElement
	 * @param posy       ,the y-position of the guiElement
	 **/
	public void drawGUIElement(GUIElement guiElement, float posx, float posy) {
		if (guiElement.visible) {
			if (guiElement instanceof GUIElementText) {
				font.draw(batch, ((GUIElementText) guiElement).getTextContent(), posx, posy);
			} else if (guiElement instanceof GUIElementEventGroup) {
				for (GUIElement subGUIElement : ((GUIElementEventGroup) guiElement).subGuiElements) {
					drawGUIElement(subGUIElement, posx + subGUIElement.pos.x, posy + subGUIElement.pos.y);
				}
			} else if (guiElement instanceof GUIElementTexture) {
				batch.draw(((GUIElementTexture) guiElement).texture, posx, posy, ((GUIElementTexture) guiElement).width,
						((GUIElementTexture) guiElement).height);
			}
		}
	}

	private float numberToFormattedNumber(float number) {
		if (number < 10000) {
			return number;
		} else if (number < 10000000) {
			return number / 1000;
		} else if (number < 10000000000f) {
			return number / 1000000;
		} else {
			return number / 1000000000;
		}
	}

	private String getUnit(float number) {
		if (number < 10000) {
			return " m";
		} else if (number < 10000000) {
			return " km";
		} else if (number < 10000000000f) {
			return " Tkm";
		} else {
			return " Mkm";
		}
	}

	private void updateFramesPerSecond() {
		framesPerSecond.updateTextContent("FPS: " + Gdx.graphics.getFramesPerSecond() + " frames/s");
	}

	/** Updates the average frametime of the last second **/
	private void updateAverageFrameTime() {
		frameTimeAddingCounter += 1;
		frameTimeCounter += Gdx.graphics.getDeltaTime();
		if (frameTimeCounter >= 1) {
			frameTime.updateTextContent(
					"Frametime: " + dF.format(frameTimeCounter * 1000 / frameTimeAddingCounter) + " ms/frame");
			frameTimeCounter = 0;
			frameTimeAddingCounter = 0;
		}
	}

	public void updateGameState(boolean running) {
		if (running) {
			gameState.updateTextContent("Running");
		} else {
			gameState.updateTextContent("Paused");
		}
	}

	public void updateGameSpeed(float gameSpeed) {
		this.gameSpeed.updateTextContent("Speed: " + gameSpeed + " sec/sec");
	}

	public void updateTimeElapsed(float timeElapsed) {
		this.timeElapsed.updateTextContent("Time elapsed: " + dF.format(timeElapsed) + " s");
	}

	public void updateCameraZoom(float factor) {
		this.cameraZoom.updateTextContent("Zoom: " + numberToFormattedNumber(factor) + getUnit(factor) + "/grid block");
	}

	public void updateParticleCount(int particleCount) {
		this.particleCount.updateTextContent("Particle count: " + particleCount);
	}

	public void updateGameSteps(int gameSteps) {
		this.gameSteps.updateTextContent("Steps: " + gameSteps);
	}

	/**
	 * Updates the world dimensions of all the GUIs in order to update the positions
	 * of its elements
	 * 
	 * @param screenWidth  ,width of the screen
	 * @param screenHeight ,height of the screen
	 **/
	public void updateGUIWorldDimensions() {
		for (GUI gui : guis) {
			gui.updateWorldDimensions(staticViewport.getWorldWidth(), staticViewport.getWorldHeight());
		}
	}
	
	public GUI getGUI(int id) {
		return guis.get(id);
	}
}
