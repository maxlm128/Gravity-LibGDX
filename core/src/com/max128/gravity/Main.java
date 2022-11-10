package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter implements InputProcessor {

	final static int WIDTH = 1920;
	final static int HEIGHT = 1080;

	private float zoomTarget;

	private Vector2 lastPos;
	private Viewport viewport;
	private OrthographicCamera cam;
	private ShapeRenderer sR;
	private EntityManager eR;

	@Override
	public void create() {
		zoomTarget = 1f;
		lastPos = new Vector2();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
		eR = new EntityManager();
		sR = new ShapeRenderer();
		sR.setAutoShapeType(true);
		Gdx.input.setInputProcessor(this);
		super.create();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render() {
		zoomToTarget();
		checkForInput();
		eR.moveParticles(Gdx.graphics.getDeltaTime());
		cam.update();

		sR.begin();
		sR.setProjectionMatrix(viewport.getCamera().combined);
		sR.set(ShapeType.Filled);
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

		// TODO: Draw grid

		// Draw particles
		for (Particle p : eR.getP()) {
			sR.setColor(1, 1, 1, 1);
			sR.circle(p.pos.x, p.pos.y, p.r);
		}

		sR.end();
		super.render();
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
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			zoomTarget += zoomTarget * 0.1f * 0.3f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			zoomTarget += zoomTarget * -0.1f * 0.3f;
		}
	}

	private void zoomToTarget() {
		if (Math.abs(cam.zoom - zoomTarget) >= 0.01f * cam.zoom) {
			cam.zoom = zoomTarget - ((zoomTarget - cam.zoom) * 0.95f);
		} else if (cam.zoom != zoomTarget) {
			cam.zoom = zoomTarget;
		}
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
