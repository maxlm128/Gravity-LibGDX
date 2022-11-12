package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter implements InputProcessor {

	final static int WIDTH = 1920;
	final static int HEIGHT = 1080;

	private float zoomTarget;

	private Vector2 lastPos;
	private Viewport viewport;
	private Viewport gridViewport;
	private Viewport gridViewport2;
	private OrthographicCamera cam;
	private OrthographicCamera gridCam;
	private OrthographicCamera gridCam2;
	private ShapeRenderer sR;
	private EntityManager eR;

	@Override
	public void create() {
		zoomTarget = 1f;
		lastPos = new Vector2();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		gridCam = new OrthographicCamera(WIDTH, HEIGHT);
		gridCam2 = new OrthographicCamera(WIDTH, HEIGHT);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
		gridViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gridCam);
		gridViewport2 = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gridCam2);
		eR = new EntityManager();
		sR = new ShapeRenderer();
		sR.setAutoShapeType(true);
		Gdx.input.setInputProcessor(this);
		super.create();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		gridViewport.update(width, height);
		gridViewport2.update(width, height);
	}

	@Override
	public void render() {
		zoomToTarget();
		checkForInput();
		eR.moveParticles(Gdx.graphics.getDeltaTime());
		cam.update();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sR.begin();
		sR.set(ShapeType.Filled);
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

		// Draw first grid
		sR.setProjectionMatrix(gridViewport.getCamera().combined);
		double factor = (double) (Math.pow(10, Math.ceil(Math.log10(cam.zoom * 1.5))));
		gridCam.zoom = (float) ((cam.zoom) / factor);
		sR.setColor(0.2f, 0.2f, 0.2f, (float) ((-2.2222 * gridCam.zoom) + 1.11111111));
		
		for (float x = (float) ((-cam.position.x / factor) % 10); gridCam.frustum.pointInFrustum(x - 4.5f, 0,
				0); x += 10) {
			drawVerticalPoints(x, factor);
		}
		for (float x = (float) ((-cam.position.x / factor) % 10) - 10; gridCam.frustum.pointInFrustum(x + 4.5f, 0,
				0); x -= 10) {
			drawVerticalPoints(x, factor);
		}

		// Draw the second grid which is bigger
		sR.setProjectionMatrix(gridViewport2.getCamera().combined);
		factor *= 10;
		gridCam2.zoom = gridCam.zoom / 10;
		sR.setColor(0.2f, 0.2f, 0.2f, (float) ((2.222 * gridCam.zoom) - 0.11111111));
		
		for (float x = (float) ((-cam.position.x / factor) % 10); gridCam2.frustum.pointInFrustum(x - 4.5f, 0,
				0); x += 10) {
			drawVerticalPoints(x, factor);
		}
		for (float x = (float) ((-cam.position.x / factor) % 10) - 10; gridCam2.frustum.pointInFrustum(x + 4.5f, 0,
				0); x -= 10) {
			drawVerticalPoints(x, factor);
		}
		
		gridCam.update();
		gridCam2.update();

		// Draw particles
		sR.setProjectionMatrix(viewport.getCamera().combined);
		for (Particle p : eR.getP()) {
			sR.setColor(1, 1, 1, 1);
			sR.circle(p.pos.x, p.pos.y, p.r);
		}

		sR.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		super.render();
	}

	private void drawVerticalPoints(float x, double factor) {
		for (float y = (float) ((-cam.position.y / factor) % 10); gridCam.frustum.pointInFrustum(0, y - 4.5f,
				0); y += 10) {
			sR.rectLine(x - 0.5f, y, x + 0.5f, y, 1);
		}
		for (float y = (float) ((-cam.position.y / factor) % 10) - 10; gridCam.frustum.pointInFrustum(0, y + 4.5f,
				0); y -= 10) {
			sR.rectLine(x - 0.5f, y, x + 0.5f, y, 1);
		}
	}

	private void checkForInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.position.y += 20f * cam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.position.y -= 20f * cam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.position.x += 20f * cam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.position.x -= 20f * cam.zoom;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			zoomTarget += zoomTarget * 0.03f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			zoomTarget += zoomTarget  * -0.03f;
		}
	}

	private void zoomToTarget() {
		if (Math.abs(cam.zoom - zoomTarget) >= 0.001f * cam.zoom) {
			cam.zoom = zoomTarget - ((zoomTarget - cam.zoom) * 0.95f);
		} else if (cam.zoom != zoomTarget) {
			cam.zoom = zoomTarget;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.SPACE:
			eR.running = !eR.running;
			break;
		case Input.Keys.F11:
			if (!Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);
			}
			break;
		case Input.Keys.UP:
			eR.speed *= 2;
			break;
		case Input.Keys.DOWN:
			eR.speed *= 0.5f;
			break;
		case Input.Keys.R:
			cam.position.setZero();
			zoomTarget = 1f;
			cam.zoom = 1f;
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		cam.position.add(Gdx.input.getDeltaX() * -cam.zoom, Gdx.input.getDeltaY() * cam.zoom, 0);
		cam.update();
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		zoomTarget += zoomTarget * amountY * 0.3f;
		cam.update();
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastPos.set(Gdx.input.getX(), Gdx.input.getY());
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
