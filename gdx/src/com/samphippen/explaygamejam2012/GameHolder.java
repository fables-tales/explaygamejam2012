package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class GameHolder implements ApplicationListener {
    
    private Sprite mSprite;
    private SpriteBatch mSpriteBatch;
    private Camera mCamera;
    private Vector2 mCameraOrigin = new Vector2(0,0);
    
    
	@Override
	public void create() {
	    Texture t = new Texture(Gdx.files.internal("circle.png"));
	    mSprite = new Sprite(t);
	    mSpriteBatch = new SpriteBatch();
	    float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mCamera = new OrthographicCamera(w, h);
        mCameraOrigin.set(w/2, h/2);
	}

	@Override
	public void dispose() {
	}
	
	private Vector2 getCameraOrigin() {
	    return mCameraOrigin;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		mSprite.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
		mSpriteBatch.setProjectionMatrix(mCamera.combined);
		mSpriteBatch.setTransformMatrix(new Matrix4().translate(-getCameraOrigin().x,
                -getCameraOrigin().y, 0));
		mSpriteBatch.begin();
		mSprite.draw(mSpriteBatch);
		mSpriteBatch.end();
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
