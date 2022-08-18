package com.max128.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GUI extends ApplicationAdapter implements InputProcessor {
	static final int WIDTH = 1440;
	static final int HEIGHT = 1080;
	boolean running = true;
	Array<Particle> p;
	private Camera c;
	private ShapeRenderer sR;

	public GUI() {
	}

	@Override
	public void create() {
		c = new Camera();
		sR = new ShapeRenderer();
		sR.setAutoShapeType(true);
		p = new Array<Particle>();
		new Main(this);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		sR.begin();
		for (Particle p : p) {
			sR.circle(((p.pos.x - p.r + c.pos.x) * c.scale) + WIDTH / 2,
					((p.pos.y - p.r + c.pos.y) * c.scale) + HEIGHT / 2, p.r * 2 * c.scale);
		}
		sR.end();
	}

	@Override
	public void dispose() {
		running = false;
		sR.dispose();
	}

	public void drawErr(String err) {

	}

	public void updateParticleArray(Array<Particle> p) {
		this.p = p;
	}

	public Camera getCamera() {
		return c;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
}
