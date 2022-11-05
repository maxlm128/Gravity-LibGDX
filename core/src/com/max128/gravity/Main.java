package com.max128.gravity;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter implements InputProcessor, MouseInputListener {
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
		eR.moveParticles(0.0069f);
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
		sR.setColor(1f, 0f, 0f, 1);
		sR.setProjectionMatrix(viewport.getCamera().combined);
		sR.begin();
		for (Particle p : eR.getP()) {
			sR.circle(p.pos.x - p.r, (p.pos.y - p.r), p.r * 2);
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
		lastPos.set(Gdx.input.getX(), Gdx.input.getY());
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 screenPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 pos3 = camera.unproject(screenPos);
		Vector2 pos = new Vector2(pos3.x, pos3.y);
		Vector3 temp = new Vector3(Gdx.input.getDeltaX(), Gdx.input.getDeltaX(),0);
		temp = camera.unproject(temp);
		camera.translate(pos.x - lastPos.x, pos.y - lastPos.y);
		System.out.println("X: " + (pos.x - lastPos.x) + " Y: " + (pos.y - lastPos.y) + " x: " + temp.x + " y: " + temp.y);
		lastPos.x = pos.x;
		lastPos.y = pos.y;
		camera.update();
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		camera.zoom += camera.zoom * amountY * 0.3f;
		camera.update();
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
}
