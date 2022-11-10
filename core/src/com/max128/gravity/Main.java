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
	private final int BUTTON_SPACE = 62;
	private final int BUTTON_W = 51;
	private final int BUTTON_S = 47;
	private final int BUTTON_A = 29;
	private final int BUTTON_D = 32;
	private final int BUTTON_Q = 33;
	private final int BUTTON_E = 45;
	private final int BUTTON_F11 = 141;
	private final int BUTTON_ARROW_UP = 19;
	private final int BUTTON_ARROW_DOWN = 20;

	// Booleans if the buttons are pressed
	private boolean button_up;
	private boolean button_down;
	private boolean button_left;
	private boolean button_right;
	private boolean button_out;
	private boolean button_in;
	
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

		// Draw grid
//		sR.setColor(0.2f, 0.2f, 0.2f, 1f);
//		Vector3 origin = cam.unproject(new Vector3(0, HEIGHT / 2, 0));
		
		// Draw particles
		for (Particle p : eR.getP()) {
			sR.setColor(1, 1, 1, 1);
			sR.circle(p.pos.x, p.pos.y, p.r);
		}

		sR.end();
		super.render();
	}

	private void checkForInput() {
		if (button_up) {
			cam.position.y += 20f * cam.zoom;
		}
		if (button_down) {
			cam.position.y -= 20f * cam.zoom;
		}
		if (button_right) {
			cam.position.x += 20f * cam.zoom;
		}
		if (button_left) {
			cam.position.x -= 20f * cam.zoom;
		}
		if (button_in) {
			zoomTarget += zoomTarget * 0.1f * 0.3f;
		}
		if (button_out) {
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
		case BUTTON_SPACE:
			eR.running = !eR.running;
			break;
		case BUTTON_F11:
			if(!Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				Gdx.graphics.setWindowedMode(WIDTH,HEIGHT);
			}
			break;
		case BUTTON_ARROW_UP:
			eR.speed *= 2;
			break;
		case BUTTON_ARROW_DOWN:
			eR.speed *= 0.5f;
			break;
		case BUTTON_W:
			button_up = true;
			break;
		case BUTTON_S:
			button_down = true;
			break;
		case BUTTON_A:
			button_left = true;
			break;
		case BUTTON_D:
			button_right = true;
			break;
		case BUTTON_E:
			button_in = true;
			break;
		case BUTTON_Q:
			button_out = true;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case BUTTON_W:
			button_up = false;
			break;
		case BUTTON_S:
			button_down = false;
			break;
		case BUTTON_A:
			button_left = false;
			break;
		case BUTTON_D:
			button_right = false;
			break;
		case BUTTON_E:
			button_in = false;
			break;
		case BUTTON_Q:
			button_out = false;
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
