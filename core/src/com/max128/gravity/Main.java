package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Main class and Renderer of the Game **/
public class Main extends ApplicationAdapter implements InputProcessor {

	final static int WIDTH = 1920;
	final static int HEIGHT = 1080;

	private float zoomTarget;
	private Particle camFixedTo;

	private Viewport mainViewport;
	private Viewport farGridViewport;
	private Viewport nearGridViewport;
	private Viewport staticViewport;

	private OrthographicCamera mainCam;
	private OrthographicCamera farGridCam;
	private OrthographicCamera nearGridCam;
	private OrthographicCamera staticCam;

	BitmapFont font;

	private ShapeRenderer sR;
	private SpriteBatch batch;
	private EntityManager eM;

	@Override
	public void create() {
		mainCam = new OrthographicCamera(WIDTH, HEIGHT);
		farGridCam = new OrthographicCamera(WIDTH, HEIGHT);
		nearGridCam = new OrthographicCamera(WIDTH, HEIGHT);
		staticCam = new OrthographicCamera(WIDTH, HEIGHT);
		mainViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainCam);
		farGridViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), farGridCam);
		nearGridViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), nearGridCam);
		staticViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), staticCam);
		Textures.loadTextures();
		eM = new EntityManager();
		sR = new ShapeRenderer();
		batch = new SpriteBatch();
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 18;
		parameter.borderWidth = 2f;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		font = new FreeTypeFontGenerator(Gdx.files.internal("consolas.ttf")).generateFont(parameter);
		sR.setAutoShapeType(true);
		Gdx.input.setInputProcessor(this);
		if (eM.getP().size > 0) {
			camFixedTo = eM.getP().get(0);
			mainCam.zoom = camFixedTo.r / 100;
		}
		zoomTarget = mainCam.zoom;
		super.create();
	}

	@Override
	public void resize(int width, int height) {
		mainViewport.update(width, height);
		farGridViewport.update(width, height);
		nearGridViewport.update(width, height);
		staticViewport.update(width, height);
	}

	@Override
	public void dispose() {
		Textures.disposeTextures();
	}

	@Override
	public void render() {
		// Calculations
		zoomToTarget(Gdx.graphics.getDeltaTime());
		checkForInput();
		eM.moveParticles(Gdx.graphics.getDeltaTime());

		// ShapeRenderer rendering preperation
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sR.begin();
		sR.set(ShapeType.Filled);
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

		// Rendering with Shaperenderer
		drawGrids();
		drawFarParticles();

		// ShapeRenderer rendering postprocessing
		sR.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		// SpriteBatch rendering preperation
		batch.setProjectionMatrix(mainCam.combined);
		batch.begin();

		// Rendering with spriteBatch
		drawNearParticles();
		drawGUI();

		// SpriteBatch rendering postprocessing
		batch.end();

		// Update camera position to fixed position
		updateToFixedPos();

		// Updating Cameras
		mainCam.update();
		farGridCam.update();
		nearGridCam.update();
		super.render();
	}

	/** Draws the graphical user interface for interaction with Particles **/
	private void drawGUI() {
		// Particle GUI
		if (camFixedTo != null) {
			batch.setProjectionMatrix(staticCam.combined);
			batch.draw(Textures.BOX, -staticViewport.getWorldWidth() / 2, -staticViewport.getWorldHeight() / 2);
			batch.draw(camFixedTo.tex, (-staticViewport.getWorldWidth() / 2) + 30,
					(-staticViewport.getWorldHeight() / 2) + 80, 200, 200);
			font.draw(batch, camFixedTo.name, (-staticViewport.getWorldWidth() / 2) + 210,
					(-staticViewport.getWorldHeight() / 2) + 340);
			font.draw(batch, "Mass: " + camFixedTo.m, (-staticViewport.getWorldWidth() / 2) + 240,
					(-staticViewport.getWorldHeight() / 2) + 280);
			font.draw(batch, "Radius: " + camFixedTo.r, (-staticViewport.getWorldWidth() / 2) + 240,
					(-staticViewport.getWorldHeight() / 2) + 260);
			font.draw(batch, "Position:", (-staticViewport.getWorldWidth() / 2) + 240,
					(-staticViewport.getWorldHeight() / 2) + 240);
			font.draw(batch, "X: " + camFixedTo.pos.x, (-staticViewport.getWorldWidth() / 2) + 250,
					(-staticViewport.getWorldHeight() / 2) + 220);
			font.draw(batch, "Y: " + camFixedTo.pos.y, (-staticViewport.getWorldWidth() / 2) + 250,
					(-staticViewport.getWorldHeight() / 2) + 200);
			font.draw(batch, "Velocity:", (-staticViewport.getWorldWidth() / 2) + 240,
					(-staticViewport.getWorldHeight() / 2) + 180);
			font.draw(batch, "X: " + camFixedTo.vel.x, (-staticViewport.getWorldWidth() / 2) + 250,
					(-staticViewport.getWorldHeight() / 2) + 160);
			font.draw(batch, "Y: " + camFixedTo.vel.y, (-staticViewport.getWorldWidth() / 2) + 250,
					(-staticViewport.getWorldHeight() / 2) + 140);
		}
		// TODO: Simulation GUI
	}

	/** draws the far and the near grid depending on the zoom **/
	private void drawGrids() {
		// Draw first grid
		sR.setProjectionMatrix(farGridCam.combined);
		double factor = Math.pow(10, Math.ceil(Math.log10(mainCam.zoom)));
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
				batch.draw(p.tex, p.pos.x - p.r, p.pos.y - p.r, p.r * 2, p.r * 2);
			}
		}
	}

	/** Draw far particles with ShapeRenderer **/
	private void drawFarParticles() {
		sR.setColor(1f, 1f, 1f, 1f);
		sR.setProjectionMatrix(staticCam.combined);
		for (Particle p : eM.getP()) {
			if (p.r / mainCam.zoom < 1) {
				sR.circle((-mainCam.position.x + p.pos.x) / mainCam.zoom,
						(-mainCam.position.y + p.pos.y) / mainCam.zoom, 1, 10);
			}
		}
	}

	/** Update position to fixed Particle **/
	private void updateToFixedPos() {
		if (camFixedTo != null) {
			mainCam.position.x = camFixedTo.pos.x;
			mainCam.position.y = camFixedTo.pos.y;
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

	/** Approaches the camera-zoom to the zoomTarget with every execution **/
	private void zoomToTarget(float dt) {
		if (Math.abs(mainCam.zoom - zoomTarget) >= 0.001f * mainCam.zoom) {
			mainCam.zoom += (zoomTarget - mainCam.zoom) * dt * 7;
		} else if (mainCam.zoom != zoomTarget) {
			mainCam.zoom = zoomTarget;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.SPACE:
			eM.running = !eM.running;
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
			break;
		case Input.Keys.DOWN:
			eM.speed *= 0.5f;
			break;
		case Input.Keys.R:
			mainCam.position.setZero();
			zoomTarget = 1f;
			mainCam.zoom = 1f;
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camFixedTo = eM.posInParticle(mainCam.unproject(new Vector3(screenX, screenY, 0)));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camFixedTo = null;
		mainCam.position.add(Gdx.input.getDeltaX() * -mainCam.zoom, Gdx.input.getDeltaY() * mainCam.zoom, 0);
		mainCam.update();
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		zoomTarget += zoomTarget * amountY * 0.3f;
		mainCam.update();
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
		return false;
	}

}
