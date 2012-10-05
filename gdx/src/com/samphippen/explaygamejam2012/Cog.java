package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cog {

    public static Cog getCog() {
        Texture t = new Texture(Gdx.files.internal("circle.png"));
        Cog c = new Cog(new Sprite(t), 37);
        return c;
    }

    private Sprite mSprite;
    // THIS MUST BE DEGREEEEES
    private float mAngle;
    private boolean mMouseTracking = false;
    private boolean mIsDrive = false;

    public Cog(Sprite s, float a) {
        mSprite = s;
        mAngle = a;
    }

    public void draw(SpriteBatch sb) {
        mSprite.setRotation(mAngle);
        mSprite.draw(sb);
    }

    public void update() {
        if (mMouseTracking) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            mSprite.setPosition(x, y);
        }
        
        if (mIsDrive) {
            mAngle += 1;
        }
    }

    public void setMouseTracking(boolean b) {
        mMouseTracking = b;
    }
    
    public void promoteToDrive() {
        mIsDrive = true;
    }
}
