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
import com.max128.gravity.GUIManagement.GUIElements.GUIElementGroup;
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
	// ParticleGUI Texture Name Mass Radius PositionXY VelocityXY
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
	private Array<GUI> currentGUIs;

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
		currentGUIs = new Array<>(10);
		// Simulation GUI
		guis.add(new SimulationGUI(0, staticViewport.getWorldWidth(), staticViewport.getWorldHeight(), framesPerSecond,
				frameTime, gameState, gameSpeed, this.gameSteps, timeElapsed, cameraZoom, particleCount));
		guis.add(new ParticleGUI(1, particleTexture, particleName, particleMass, particleRadius, particlePositionX,
				particlePositionY, particleVelocity, particleVelocityX, particleVelocityY));
		currentGUIs.add(guis.first());
		currentGUIs.add(guis.get(1));

		this.batch = batch;
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 18;
		parameter.borderWidth = 2f;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		font = new FreeTypeFontGenerator(Gdx.files.internal("fonts/consolas.ttf")).generateFont(parameter);
		dF = new DecimalFormat("0.000");
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
		for (GUI gui : currentGUIs) {
			System.out.println(gui.id);
			for (GUIElement guiElement : gui.getGUIElements()) {
				drawGUIElement(guiElement, guiElement.pos.x, guiElement.pos.y);
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
		if (guiElement instanceof GUIElementText) {
			font.draw(batch, ((GUIElementText) guiElement).getTextContent(), posx, posy);
		} else if (guiElement instanceof GUIElementGroup) {
			for (GUIElement subGUIElement : ((GUIElementGroup) guiElement).subGuiElements) {
				drawGUIElement(subGUIElement, posx + subGUIElement.pos.x, posy + subGUIElement.pos.y);
			}
		} else if (guiElement instanceof GUIElementTexture) {
			batch.draw(((GUIElementTexture) guiElement).texture, posx, posy, ((GUIElementTexture) guiElement).width,
					((GUIElementTexture) guiElement).height);
		}
	}

//	/** Draws the HUD for Particles and the Simulation **/
//	public void drawHUD(float factor, Particle camFixedTo) {
//		updateAverageFrameTime();
//		// Particle GUI
//		batch.setProjectionMatrix(staticCam.combined);
//		if (camFixedTo != null) {
//			batch.draw(Textures.BOX, -staticViewport.getWorldWidth() / 2, -staticViewport.getWorldHeight() / 2);
//			batch.draw(camFixedTo.tex, (-staticViewport.getWorldWidth() / 2) + 30,
//					(-staticViewport.getWorldHeight() / 2) + 80, 200, 200);
//			font.draw(batch, camFixedTo.name, (-staticViewport.getWorldWidth() / 2) + 210,
//					(-staticViewport.getWorldHeight() / 2) + 340);
//			font.draw(batch, "Mass: " + camFixedTo.m, (-staticViewport.getWorldWidth() / 2) + 240,
//					(-staticViewport.getWorldHeight() / 2) + 280);
//			font.draw(batch, "Radius: " + dF.format(numberToFormattedNumber(camFixedTo.r)) + getUnit(camFixedTo.r),
//					(-staticViewport.getWorldWidth() / 2) + 240, (-staticViewport.getWorldHeight() / 2) + 260);
//			font.draw(batch, "Position:", (-staticViewport.getWorldWidth() / 2) + 240,
//					(-staticViewport.getWorldHeight() / 2) + 240);
//			font.draw(batch, "X: " + dF.format(numberToFormattedNumber(camFixedTo.pos.x)) + getUnit(camFixedTo.pos.x),
//					(-staticViewport.getWorldWidth() / 2) + 250, (-staticViewport.getWorldHeight() / 2) + 220);
//			font.draw(batch, "Y: " + dF.format(numberToFormattedNumber(camFixedTo.pos.y)) + getUnit(camFixedTo.pos.y),
//					(-staticViewport.getWorldWidth() / 2) + 250, (-staticViewport.getWorldHeight() / 2) + 200);
//			font.draw(batch, "Velocity:", (-staticViewport.getWorldWidth() / 2) + 240,
//					(-staticViewport.getWorldHeight() / 2) + 180);
//			font.draw(batch,
//					"X: " + dF.format(numberToFormattedNumber(camFixedTo.vel.x)) + getUnit(camFixedTo.vel.x) + "/s",
//					(-staticViewport.getWorldWidth() / 2) + 250, (-staticViewport.getWorldHeight() / 2) + 160);
//			font.draw(batch,
//					"Y: " + dF.format(numberToFormattedNumber(camFixedTo.vel.y)) + getUnit(camFixedTo.vel.y) + "/s",
//					(-staticViewport.getWorldWidth() / 2) + 250, (-staticViewport.getWorldHeight() / 2) + 140);
//		}
//		batch.draw(Textures.BOX, (staticViewport.getWorldWidth() / 2) - 360, -staticViewport.getWorldHeight() / 2, 360,
//				270);
//		font.draw(batch, "Simulation", (staticViewport.getWorldWidth() / 2) - 230,
//				(-staticViewport.getWorldHeight() / 2) + 250);
//		if (eM.running) {
//			font.draw(batch, "Running", (staticViewport.getWorldWidth() / 2) - 340,
//					(-staticViewport.getWorldHeight() / 2) + 210);
//		} else {
//			font.draw(batch, "Paused", (staticViewport.getWorldWidth() / 2) - 340,
//					(-staticViewport.getWorldHeight() / 2) + 210);
//		}
//		font.draw(batch, "Speed: " + eM.speed + " sec/sec", (staticViewport.getWorldWidth() / 2) - 340,
//				(-staticViewport.getWorldHeight() / 2) + 190);
//		font.draw(batch, "Particle count: " + eM.getP().size, (staticViewport.getWorldWidth() / 2) - 340,
//				(-staticViewport.getWorldHeight() / 2) + 170);
//		font.draw(batch, "Steps: " + eM.STEPS, (staticViewport.getWorldWidth() / 2) - 340,
//				(-staticViewport.getWorldHeight() / 2) + 150);
//		font.draw(batch, "Frametime: " + dF.format(Integer.parseInt("100")) + " ms/frame",
//				(staticViewport.getWorldWidth() / 2) - 340, (-staticViewport.getWorldHeight() / 2) + 130);
//		if (Integer.parseInt("100") != 0) {
//			font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + " frames/s",
//					(staticViewport.getWorldWidth() / 2) - 340, (-staticViewport.getWorldHeight() / 2) + 110);
//		} else {
//			font.draw(batch, "FPS: " + 0 + " frames/s", (staticViewport.getWorldWidth() / 2) - 340,
//					(-staticViewport.getWorldHeight() / 2) + 110);
//		}
//		font.draw(batch, "Time Elapsed: " + dF.format(eM.elapsedTime) + " s",
//				(staticViewport.getWorldWidth() / 2) - 340, (-staticViewport.getWorldHeight() / 2) + 90);
//		font.draw(batch, "Zoom: " + numberToFormattedNumber(factor) + getUnit(factor) + "/grid block",
//				(staticViewport.getWorldWidth() / 2) - 340, (-staticViewport.getWorldHeight() / 2) + 70);
//	}

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
}
