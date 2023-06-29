package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.max128.gravity.GUIManagement.GUIRenderer;

/** Main class and Renderer of the Game **/
public class Main extends ApplicationAdapter implements InputProcessor {

	final static int WIDTH = 1920;
	final static int HEIGHT = 1080;

	private float zoomTarget;
	private Particle camFixedTo;
	public boolean simulationRunning;

	private Viewport mainViewport;
	private Viewport farGridViewport;
	private Viewport nearGridViewport;
	private Viewport staticViewport;

	private OrthographicCamera mainCam;
	private OrthographicCamera farGridCam;
	private OrthographicCamera nearGridCam;
	private OrthographicCamera staticCam;

	private GUIRenderer guiR;
	private ShapeRenderer sR;
	private SpriteBatch batch;
	private EntityManager eM;

	@Override
	public void create() {
		Textures.loadTextures();

		// Viewports and Cameras
		mainCam = new OrthographicCamera(WIDTH, HEIGHT);
		farGridCam = new OrthographicCamera(WIDTH, HEIGHT);
		nearGridCam = new OrthographicCamera(WIDTH, HEIGHT);
		staticCam = new OrthographicCamera(Main.WIDTH, Main.HEIGHT);
		mainViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainCam);
		farGridViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), farGridCam);
		nearGridViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), nearGridCam);
		staticViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), staticCam);

		eM = new EntityManager();
		sR = new ShapeRenderer();
		batch = new SpriteBatch();
		guiR = new GUIRenderer(batch, staticViewport, eM.STEPS, this);

		if (eM.getP().size > 0) {
			camFixedTo = eM.getP().get(0);
			mainCam.zoom = camFixedTo.r / 100;
		}
		sR.setAutoShapeType(true);
		Gdx.input.setInputProcessor(this);
		zoomTarget = mainCam.zoom;

		// Update the GUIVariables on startup
		// Simulation GUI
		guiR.updateCameraZoom((float) Math.pow(10, Math.ceil(Math.log10(mainCam.zoom))) * 100f);
		guiR.updateGameSpeed(eM.speed);
		guiR.updateGameState(eM.running);
		guiR.updateGameSteps(eM.STEPS);
		guiR.updateParticleCount(eM.getP().size);
		guiR.updateTimeElapsed(eM.elapsedTime);
		// ParticleGUI
		guiR.updateParticleName(camFixedTo.name);
		guiR.updateParticleTexure(camFixedTo.tex);
		guiR.updateParticleMass(camFixedTo.m);
		guiR.updateParticleRadius(camFixedTo.r);

		super.create();
	}

	@Override
	public void resize(int width, int height) {
		staticViewport.update(width, height);
		mainViewport.update(staticViewport.getScreenWidth(), staticViewport.getScreenHeight());
		farGridViewport.update(staticViewport.getScreenWidth(), staticViewport.getScreenHeight());
		nearGridViewport.update(staticViewport.getScreenWidth(), staticViewport.getScreenHeight());
		guiR.updateGUIWorldDimensions();
	}

	@Override
	public void dispose() {
		batch.dispose();
		Textures.disposeTextures();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
		if (simulationRunning) {
			// Update variables
			guiR.updateTimeElapsed(eM.elapsedTime);
			if (camFixedTo != null) {
				guiR.updateParticlePosition(new Vector2(camFixedTo.pos.x.floatValue(),camFixedTo.pos.y.floatValue()));
				guiR.updateParticleVelocity(new Vector2(camFixedTo.vel.x.floatValue(),camFixedTo.vel.y.floatValue()));
			}

			// Calculations
			zoomToTarget(Gdx.graphics.getDeltaTime());
			checkForInput();
			eM.moveParticles(Gdx.graphics.getDeltaTime());

			// ShapeRenderer rendering preperation
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			sR.begin();
			sR.set(ShapeType.Filled);

			// Rendering with Shaperenderer
			double factor = Math.pow(10, Math.ceil(Math.log10(mainCam.zoom)));
			drawGrids(factor);
			drawFarParticles();

			// ShapeRenderer rendering postprocessing
			sR.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}

		// SpriteBatch rendering preperation
		batch.begin();
		if (simulationRunning) {
			batch.setProjectionMatrix(mainCam.combined);

			// Rendering with spriteBatch
			drawNearParticles();

			// Update camera position to fixed position
			updateToFixedPos();
		}
		guiR.drawCurrentGUIs();

		// SpriteBatch rendering postprocessing
		batch.end();

		// Updating Cameras
		if (simulationRunning) {
			mainCam.update();
			farGridCam.update();
			nearGridCam.update();
		}
		super.render();
	}

	/** draws the far and the near grid depending on the zoom **/
	private void drawGrids(double factor) {
		// Draw first grid
		sR.setProjectionMatrix(farGridCam.combined);
		farGridCam.zoom = (float) ((mainCam.zoom) / factor);
		sR.setColor(0.2f, 0.2f, 0.2f, (float) ((-2.2222 * farGridCam.zoom) + 1.11111111));

		for (float x = (float) ((-mainCam.position.x / factor) % 10); farGridCam.frustum.pointInFrustum(x - 4.5f, 0,
				0); x += 10) {
			drawVerticalPoints(x, factor);
		}
		for (float x = (float) ((-mainCam.position.x / factor) % 10) - 10; farGridCam.frustum.pointInFrustum(x + 4.5f,
				0, 0); x -= 10) {
			drawVerticalPoints(x, factor);
		}

		// Draw the second grid which is bigger
		sR.setProjectionMatrix(nearGridCam.combined);
		factor *= 10;
		nearGridCam.zoom = farGridCam.zoom / 10;
		sR.setColor(0.2f, 0.2f, 0.2f, (float) ((2.222 * farGridCam.zoom) - 0.11111111));
		for (float x = (float) ((-mainCam.position.x / factor) % 10); nearGridCam.frustum.pointInFrustum(x - 4.5f, 0,
				0); x += 10) {
			drawVerticalPoints(x, factor);
		}
		for (float x = (float) ((-mainCam.position.x / factor) % 10) - 10; nearGridCam.frustum.pointInFrustum(x + 4.5f,
				0, 0); x -= 10) {
			drawVerticalPoints(x, factor);
		}
	}

	/** Draw near particles with Textures **/
	private void drawNearParticles() {
		sR.setColor(1, 1, 1, 1);
		for (Particle p : eM.getP()) {
			if (p.r / mainCam.zoom >= 1) {
				batch.draw(p.tex, p.pos.x.floatValue() - p.r, p.pos.y.floatValue() - p.r, p.r * 2, p.r * 2);
			}
		}
	}

	/** Draw far particles with ShapeRenderer **/
	private void drawFarParticles() {
		sR.setColor(1f, 1f, 1f, 1f);
		sR.setProjectionMatrix(staticCam.combined);
		for (Particle p : eM.getP()) {
			if (p.r / mainCam.zoom < 1) {
				sR.circle((-mainCam.position.x + p.pos.x.floatValue()) / mainCam.zoom,
						(-mainCam.position.y + p.pos.y.floatValue()) / mainCam.zoom, 1, 10);
			}
		}
	}

	/** Update camera position to fixed Particle **/
	private void updateToFixedPos() {
		if (camFixedTo != null) {
			mainCam.position.x = camFixedTo.pos.x.floatValue();
			mainCam.position.y = camFixedTo.pos.y.floatValue();
		}
	}

	/**
	 * Draws a line of points on the screen with a distance dependent from the
	 * camera-zoom and beginning always at the same ingame position
	 * 
	 * @param x      ,Position where drawing of the points in y direction should
	 *               begin
	 * @param factor ,the next power of ten of the zoom for calculating the
	 *               beginning y position
	 **/
	private void drawVerticalPoints(float x, double factor) {
		for (float y = (float) ((-mainCam.position.y / factor) % 10); farGridCam.frustum.pointInFrustum(0, y - 4.5f,
				0); y += 10) {
			sR.rectLine(x - 0.5f, y, x + 0.5f, y, 1);
		}
		for (float y = (float) ((-mainCam.position.y / factor) % 10) - 10; farGridCam.frustum.pointInFrustum(0,
				y + 4.5f, 0); y -= 10) {
			sR.rectLine(x - 0.5f, y, x + 0.5f, y, 1);
		}
	}

	/** Checks for inputs from the keyboard and reacts **/
	private void checkForInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camFixedTo = null;
			mainCam.position.y += 20f * mainCam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camFixedTo = null;
			mainCam.position.y -= 20f * mainCam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camFixedTo = null;
			mainCam.position.x += 20f * mainCam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camFixedTo = null;
			mainCam.position.x -= 20f * mainCam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			zoomTarget += zoomTarget * 0.03f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			zoomTarget += zoomTarget * -0.03f;
		}
	}

	/**
	 * Approaches the camera-zoom to the zoomTarget with every execution
	 * 
	 * @param dt ,delta time of the type float
	 **/
	private void zoomToTarget(float dt) {
		if (Math.abs(mainCam.zoom - zoomTarget) >= 0.001f * mainCam.zoom) {
			mainCam.zoom += (zoomTarget - mainCam.zoom) * dt * 7;
			double factor = Math.pow(10, Math.ceil(Math.log10(mainCam.zoom)));
			guiR.updateCameraZoom((float) (factor * 100));
		} else if (mainCam.zoom != zoomTarget) {
			mainCam.zoom = zoomTarget;
			double factor = Math.pow(10, Math.ceil(Math.log10(mainCam.zoom)));
			guiR.updateCameraZoom((float) (factor * 100));
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		guiR.processKeyboardInput(keycode);
		if (simulationRunning) {
			switch (keycode) {
			case Input.Keys.SPACE:
				eM.running = !eM.running;
				guiR.updateGameState(eM.running);
				break;
			case Input.Keys.F11:
				if (!Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				} else {
					Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);
				}
				break;
			case Input.Keys.UP:
				eM.speed *= 2;
				guiR.updateGameSpeed(eM.speed);
				break;
			case Input.Keys.DOWN:
				eM.speed *= 0.5f;
				guiR.updateGameSpeed(eM.speed);
				break;
			case Input.Keys.R:
				camFixedTo = null;
				mainCam.position.setZero();
				zoomTarget = 1f;
				mainCam.zoom = 1f;
				guiR.updateCameraZoom((float) (Math.pow(10, Math.ceil(Math.log10(mainCam.zoom))) * 100));
				break;
			case Input.Keys.ESCAPE:
				simulationRunning = false;
				guiR.guiVisibility(0, true);
				guiR.guiVisibility(1, false);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (simulationRunning) {
			camFixedTo = eM.posInParticle(mainCam.unproject(new Vector3(screenX, screenY, 0)));
			if (camFixedTo != null) {
				guiR.updateParticleName(camFixedTo.name);
				guiR.updateParticleTexure(camFixedTo.tex);
				guiR.updateParticleMass(camFixedTo.m);
				guiR.updateParticleRadius(camFixedTo.r);
			}
		}
		guiR.processClick(screenX, screenY);
		if (simulationRunning)
			guiR.getGUI(0).getGUIElements().get(1).visible = camFixedTo != null;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (simulationRunning) {
			camFixedTo = null;
			mainCam.position.add(Gdx.input.getDeltaX() * -mainCam.zoom, Gdx.input.getDeltaY() * mainCam.zoom, 0);
			mainCam.update();
		}
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if (simulationRunning) {
			zoomTarget += zoomTarget * amountY * 0.3f;
			mainCam.update();
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		guiR.processHover(screenX, screenY);
		return false;
	}

}
