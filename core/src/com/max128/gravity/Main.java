package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter implements InputProcessor {
	private final int SPACE = 62;
	private final int UP = 51;
	private final int DOWN = 47;
	private final int LEFT = 29;
	private final int RIGHT = 32;
	private final int OUT = 33;
	private final int IN = 45;

	// Booleans if the buttons are pressed
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean out;
	private boolean in;

	private float zoomTarget;
	private Vector2 lastPos;
	private Viewport viewport;
	final static int STEPS = 1;
	private OrthographicCamera camera;
	private ShapeRenderer sR;
	private EntityManager eR;
	static int WIDTH = 2500;
	static int HEIGHT = 1500;

	@Override
	public void create() {
		zoomTarget = 1f;
		lastPos = new Vector2();
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
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
		eR.moveParticles(0.0069f);
		camera.update();

		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
		sR.setProjectionMatrix(viewport.getCamera().combined);
		sR.begin();
		sR.set(ShapeType.Filled);
		for (Particle p : eR.getP()) {
			sR.setColor(1, 0, 0, 1);
			sR.circle(p.pos.x - p.r, (p.pos.y - p.r), p.r * 2);
		}
		sR.end();
		super.render();
	}

	private void checkForInput() {
		if (up) {
			camera.position.y += 20f * camera.zoom;
		}
		if (down) {
			camera.position.y -= 20f * camera.zoom;
		}
		if (right) {
			camera.position.x += 20f * camera.zoom;
		}
		if (left) {
			camera.position.x -= 20f * camera.zoom;
		}
		if (in) {
			zoomTarget += zoomTarget * 0.1f * 0.3f;
		}
		if (out) {
			zoomTarget += zoomTarget * -0.1f * 0.3f;
		}
	}

	private void zoomToTarget() {
		if (Math.abs(camera.zoom - zoomTarget) >= 0.01f * camera.zoom) {
			camera.zoom = zoomTarget - ((zoomTarget - camera.zoom) * 0.95f);
		} else if (camera.zoom != zoomTarget) {
			camera.zoom = zoomTarget;
		}
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.position.add(Gdx.input.getDeltaX() * -camera.zoom, Gdx.input.getDeltaY() * camera.zoom, 0);
		camera.update();
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		zoomTarget += zoomTarget * amountY * 0.3f;
		camera.update();
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
		case SPACE:
			eR.running = !eR.running;
			break;
		case UP:
			up = true;
			break;
		case DOWN:
			down = true;
			break;
		case LEFT:
			left = true;
			break;
		case RIGHT:
			right = true;
			break;
		case IN:
			in = true;
			break;
		case OUT:
			out = true;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case UP:
			up = false;
			break;
		case DOWN:
			down = false;
			break;
		case LEFT:
			left = false;
			break;
		case RIGHT:
			right = false;
			break;
		case IN:
			in = false;
			break;
		case OUT:
			out = false;
			break;
		}
		return true;
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
