package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GUI extends ApplicationAdapter implements InputProcessor{
	private OrthographicCamera camera;
	private ShapeRenderer sR;
	private EntityManager eR;
	private Main main;
	static int WIDTH = 2500;
	static int HEIGHT = 1500;

	@Override
	public void create() {
		camera = new OrthographicCamera(WIDTH* 3,HEIGHT * 3);
		eR = new EntityManager();
		main = new Main(eR);
		sR = new ShapeRenderer();
		sR.setAutoShapeType(true);
		Gdx.input.setInputProcessor(this);
		super.create();
	}

	@Override
	public void render() {
		main.mainLoop();
		ScreenUtils.clear(0, 0, 0, 1);
	    sR.setProjectionMatrix(camera.combined);
		sR.begin();
		for (Particle p : eR.getP()) {
			sR.circle(((p.pos.x - p.r) + WIDTH / 2),
					(p.pos.y - p.r) + HEIGHT / 2, p.r * 2);
		}
		sR.end();
		super.render();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		System.out.println("Touch: ScreenX: " + screenX);
		System.out.println("Touch: ScreenY: " + screenY);
		System.out.println("Touch: Pointer: " + pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		System.out.println("Mouse: ScreenX: " + screenX);
		System.out.println("Mouse: ScreenY: " + screenY);
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		camera.zoom(amountX);
		return false;
	}
	
	

}
