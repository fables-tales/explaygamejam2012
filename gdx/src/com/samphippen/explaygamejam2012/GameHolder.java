package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;

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

    private SpriteBatch mSpriteBatch;
    private Camera mCamera;
    private Vector2 mCameraOrigin = new Vector2(0, 0);
    private List<Cog> mCogs = new ArrayList<Cog>();
    private Tray mTray;
    private boolean mHoldingCog = false;
    private Cog mHeldCog;
    private int mCogTime;

    @Override
    public void create() {
        Texture t = new Texture(Gdx.files.internal("circle.png"));
        Cog c = new Cog(new Sprite(t), 37);
        mTray = new Tray();
        mCogs.add(c);
        mSpriteBatch = new SpriteBatch();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mCamera = new OrthographicCamera(w, h);
        mCameraOrigin.set(w / 2, h / 2);
    }

    @Override
    public void dispose() {
    }

    private Vector2 getCameraOrigin() {
        return mCameraOrigin;
    }

    public void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        mSpriteBatch.setProjectionMatrix(mCamera.combined);
        mSpriteBatch.setTransformMatrix(new Matrix4().translate(
                -getCameraOrigin().x, -getCameraOrigin().y, 0));

        mSpriteBatch.begin();
        mTray.draw(mSpriteBatch);
        for (int i = 0; i < mCogs.size(); i++) {
            Cog c = mCogs.get(i);
            c.draw(mSpriteBatch);
        }

        mSpriteBatch.end();

    }

    public void update() {
        mCogTime += 1;
        if (!mHoldingCog && Gdx.input.isTouched()) {
            if (mTray.touchInside(Gdx.input.getX(), Gdx.graphics.getHeight()
                    - Gdx.input.getY())) {
                mHoldingCog = true;
                mHeldCog = mTray.getCog();
                mCogs.add(mHeldCog);
                mHeldCog.setMouseTracking(true);
                mCogTime = 0;
            }
        }

        if (mHoldingCog && Gdx.input.isTouched() && mCogTime > 8) {
            mHeldCog.setMouseTracking(false);
            mHeldCog = null;
            mHoldingCog = false;
        }

        for (int i = 0; i < mCogs.size(); i++) {
            Cog c = mCogs.get(i);
            c.update();
        }
    }

    @Override
    public void render() {
        update();
        draw();
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
