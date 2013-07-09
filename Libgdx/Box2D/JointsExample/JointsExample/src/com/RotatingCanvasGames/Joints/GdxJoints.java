package com.RotatingCanvasGames.Joints;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxJoints implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	PhysicsManager physics;
	
	@Override
	public void create() {		
		camera = CameraHelper.GetCamera(CameraSettings.VIRTUAL_WIDTH, CameraSettings.VIRTUAL_HEIGHT);
		batch = new SpriteBatch();
		
		physics=new PhysicsManager(camera);
	}

	@Override
	public void dispose() {
		
		physics.Dispose();
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		Update(Gdx.graphics.getDeltaTime());
		
		
		physics.Draw(batch);
	}

	private void Update(float dt) {
		// TODO Auto-generated method stub
		physics.Update(dt);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
