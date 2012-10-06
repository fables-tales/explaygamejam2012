package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tray {
    public static final int MAX_COGS_EVER = 40;

    private Sprite mSprite;
    private Sprite mWheelCoverSprite;

    private List<Cog> mCogs = new ArrayList<Cog>();
    private static Random sRng = new Random();

    public Tray() {

        mSprite = new Sprite(ResourceManager.get("tray"));
        mWheelCoverSprite = new Sprite(ResourceManager.get("wheelcover"));
        for (int i = 0; i < MAX_COGS_EVER; i++) {
            mCogs.add(Cog.getCog(sRng.nextInt(5)+1));
        }
    }

	public void addCog(Cog cog) {
		mCogs.add(cog);
	}
	
	public void addCogs(List<Cog> cogs) {
		
		for (int i = 0; i < cogs.size(); i++) { 
			Cog cog = cogs.get(i); 
			
			if (cog.getIsFixed() == false) { 
				mCogs.add(cog); 
			}
		}		
	}
	
    public Cog getCog() {
        if (mCogs.size() > 0) {
            return mCogs.remove(0);
        }

        return null;
    }

    public void draw(SpriteBatch sb) {
        mWheelCoverSprite.setPosition(0, -150);
        mWheelCoverSprite.draw(sb);
        mSprite.setPosition(00, 0);
        mSprite.draw(sb);
    }

    public boolean touchInside(int x, int y) {
        return (x > mSprite.getX() && x < mSprite.getX() + mSprite.getWidth())
                && (y > mSprite.getY() && y < mSprite.getY()
                        + mSprite.getHeight());
    }
}
