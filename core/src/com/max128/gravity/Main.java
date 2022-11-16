package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter implements InputProcessor {

	final static int WIDTH = 1920;
	final static int HEIGHT = 1080;

	private float zoomTarget;
	private Particle camFixedTo;

	private Viewport mainViewport;
	private Viewport gridViewport;
	private Viewport gridViewport2;
	private Viewport staticViewport;
	private OrthographicCamera mainCam;
	private OrthographicCamera gridCam;
	private OrthographicCamera gridCam2;
	private OrthographicCamera staticCam;
	private ShapeRenderer sR;
	private SpriteBatch batch;
	private EntityManager eM;

	@Override
	public void create() {
		mainCam = new OrthographicCamera(WIDTH, HEIGHT);
		gridCam = new OrthographicCamera(WIDTH, HEIGHT);
		gridCam2 = new OrthographicCamera(WIDTH, HEIGHT);
		staticCam = new OrthographicCamera(WIDTH, HEIGHT);
		mainViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainCam);
		gridViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gridCam);
		gridViewport2 = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gridCam2);
		staticViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), staticCam);
		Textures.loadTextures();
		eM = new EntityManager();
		sR = new ShapeRenderer();
		batch = new SpriteBatch();
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
		gridViewport.update(width, height);
		gridViewport2.update(width, height);
		staticViewport.update(width, height);
	}

	@Override
	public void dispose() {
		Textures.disposeTextures();
	}

	@Override
	public void render() {
		zoomToTarget();
		checkForInput();
		eM.moveParticles(Gdx.graphics.getDeltaTime());
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sR.begin();
		sR.set(ShapeType.Filled);
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

		// Draw first grid
		sR.setProjectionMatrix(gridCam.combined);
		double factor = Math.pow(10, Math.ceil(Math.log10(mainCam.zoom)));
		gridCam.zoom = (float) ((mainCam.zoom) / factor);
		sR.setColor(0.2f, 0.2f, 0.2f, (float) ((-2.2222 * gridCam.zoom) + 1.11111111));

		for (float x = (float) ((-mainCam.position.x / factor) % 10); gridCam.frustum.pointInFrustum(x - 4.5f, 0,
				0); x += 10) {
			drawVerticalPoints(x, factor);
		}
		for (float x = (float) ((-mainCam.position.x / factor) % 10) - 10; gridCam.frustum.pointInFrustum(x + 4.5f, 0,
				0); x -= 10) {
			drawVerticalPoints(x, factor);
		}

		// Draw the second grid which is bigger
		sR.setProjectionMatrix(gridCam2.combined);
		factor *= 10;
		gridCam2.zoom = gridCam.zoom / 10;
		sR.setColor(0.2f, 0.2f, 0.2f, (float) ((2.222 * gridCam.zoom) - 0.11111111));

		for (float x = (float) ((-mainCam.position.x / factor) % 10); gridCam2.frustum.pointInFrustum(x - 4.5f, 0,
				0); x += 10) {
			drawVerticalPoints(x, factor);
		}
		for (float x = (float) ((-mainCam.position.x / factor) % 10) - 10; gridCam2.frustum.pointInFrustum(x + 4.5f, 0,
				0); x -= 10) {
			drawVerticalPoints(x, factor);
		}

		// Draw particles
		sR.setColor(1, 1, 1, 1);
		for (Particle p : eM.getP()) {
			if (p.r / mainCam.zoom >= 1) {
				batch.setProjectionMatrix(mainCam.combined);
				batch.begin();
				batch.draw(p.tex, p.pos.x - p.r, p.pos.y - p.r, p.r * 2, p.r * 2);
				batch.end();
			} else {
				sR.setProjectionMatrix(staticCam.combined);
				sR.circle((-mainCam.position.x + p.pos.x) / mainCam.zoom, (-mainCam.position.y + p.pos.y) / mainCam.zoom, 1, 10);
			}
		}

		// Update position to fixed Particle
		if (camFixedTo != null) {
			mainCam.position.x = camFixedTo.pos.x;
			mainCam.position.y = camFixedTo.pos.y;
		}

		mainCam.update();
		gridCam.update();
		gridCam2.update();
		sR.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		super.render();
	}

	private void drawVerticalPoints(float x, double factor) {
		for (float y = (float) ((-mainCam.position.y / factor) % 10); gridCam.frustum.pointInFrustum(0, y - 4.5f,
				0); y += 10) {
			sR.rectLine(x - 0.5f, y, x + 0.5f, y, 1);
		}
		for (float y = (float) ((-mainCam.position.y / factor) % 10) - 10; gridCam.frustum.pointInFrustum(0, y + 4.5f,
				0); y -= 10) {
			sR.rectLine(x - 0.5f, y, x + 0.5f, y, 1);
		}
	}

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

	private void zoomToTarget() {
		if (Math.abs(mainCam.zoom - zoomTarget) >= 0.001f * mainCam.zoom) {
			mainCam.zoom = zoomTarget - ((zoomTarget - mainCam.zoom) * 0.95f);
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
