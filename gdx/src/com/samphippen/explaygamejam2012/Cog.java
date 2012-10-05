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

    public Cog(Sprite s, float a) {
        mSprite = s;
        mAngle = a;
    }

    public void draw(SpriteBatch sb) {
        mSprite.setRotation(mAngle);
        mSprite.draw(sb);
    }

    public void update() {

    }
}
