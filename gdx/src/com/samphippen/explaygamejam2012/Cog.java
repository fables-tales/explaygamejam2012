package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cog {
    
    private Sprite mSprite;
    //THIS MUST BE DEGREEEEES
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
