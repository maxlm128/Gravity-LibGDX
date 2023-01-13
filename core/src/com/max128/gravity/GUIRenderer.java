package com.max128.gravity;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Class for rendering and managing all GUIs **/
public class GUIRenderer {

	private SpriteBatch batch;
	private BitmapFont font;
	private DecimalFormat dF;

	// Variables for GUIs
	private GUIElementText framesPerSecond;
	private GUIElementText frameTime;
	private GUIElementText gameState;
	private GUIElementText gameSpeed;
	private GUIElementText gameSteps;
	private GUIElementText timeElapsed;
	private GUIElementText cameraZoom;
	private GUIElementText particleCount;

	private float frameTimeCounter;
	private int frameTimeAddingCounter;
	private Viewport staticViewport;
	private Array<GUI> guis;
	private Array<GUI> currentGUIs;

	public GUIRenderer(SpriteBatch batch, Viewport staticViewport, int gameSteps) {

		this.staticViewport = staticViewport;

		// Variable GUIElements for the GUIs
		framesPerSecond = new GUIElementText(0, 0, "FPS: 0 frames/s");
		frameTime = new GUIElementText(0, 0, "FrameTime: 0 ms/frame");
		gameState = new GUIElementText(0, 0, "");
		this.gameSpeed = new GUIElementText(0, 0, "");
		this.gameSteps = new GUIElementText(0, 0, "");
		timeElapsed = new GUIElementText(0, 0, "");
		cameraZoom = new GUIElementText(0, 0, "");
		particleCount = new GUIElementText(0, 0, "");

		// Setup of the GUIs
		guis = new Array<>(10);
		currentGUIs = new Array<>(10);
		// Simulation GUI
		guis.add(new SimulationGUI(0, staticViewport.getWorldWidth(), staticViewport.getWorldHeight(), framesPerSecond,
				frameTime, gameState, gameSpeed, this.gameSteps, timeElapsed, cameraZoom, particleCount));
		currentGUIs.add(guis.first());

		this.batch = batch;
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 18;
		parameter.borderWidth = 2f;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		font = new FreeTypeFontGenerator(Gdx.files.internal("fonts/consolas.ttf")).generateFont(parameter);
		dF = new DecimalFormat("0.000");
	}

	/** Draws all the GUIs stored in the GUIRenderer **/
	public void drawCurrentGUIs() {
		updateAverageFrameTime();
		updateFramesPerSecond();
		batch.setProjectionMatrix(staticViewport.getCamera().combined);
		for (GUI gui : currentGUIs) {
			for (GUIElement guiElement : gui.guiElements) {
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
		framesPerSecond.setTextContent("FPS: " + Gdx.graphics.getFramesPerSecond() + " frames/s");
	}

	/** Updates the average frametime of the last second **/
	private void updateAverageFrameTime() {
		frameTimeAddingCounter += 1;
		frameTimeCounter += Gdx.graphics.getDeltaTime();
		if (frameTimeCounter >= 1) {
			frameTime.setTextContent(
					"Frametime: " + dF.format(frameTimeCounter * 1000 / frameTimeAddingCounter) + " ms/frame");
			frameTimeCounter = 0;
			frameTimeAddingCounter = 0;
		}
	}

	public void updateGameState(boolean running) {
		if (running) {
			gameState.setTextContent("Running");
		} else {
			gameState.setTextContent("Paused");
		}
	}

	public void updateGameSpeed(float gameSpeed) {
		this.gameSpeed.setTextContent("Speed: " + gameSpeed + " sec/sec");
	}

	public void updateTimeElapsed(float timeElapsed) {
		this.timeElapsed.setTextContent("Time elapsed: " + dF.format(timeElapsed) + " s");
	}

	public void updateCameraZoom(float factor) {
		this.cameraZoom.setTextContent("Zoom: " + numberToFormattedNumber(factor) + getUnit(factor) + "/grid block");
	}

	public void updateParticleCount(int particleCount) {
		this.particleCount.setTextContent("Particle count: " + particleCount);
	}

	public void updateGameSteps(int gameSteps) {
		this.gameSteps.setTextContent("Steps: " + gameSteps);
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
