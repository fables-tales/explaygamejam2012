package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tray {
    public static final int MAX_COGS_EVER = 40;

    private Sprite mSprite;

    private List<Cog> mCogs = new ArrayList<Cog>();

    public Tray() {

        mSprite = new Sprite(ResourceManager.get("tray"));
        for (int i = 0; i < MAX_COGS_EVER; i++) {
            mCogs.add(Cog.getCog());
        }
    }

	public void addCog(Cog cog) {
		mCogs.add(cog);
	}
	
    public Cog getCog() {
        if (mCogs.size() > 0) {
            return mCogs.remove(0);
        }

        return null;
    }

    public void draw(SpriteBatch sb) {
        mSprite.setPosition(00, 0);
        mSprite.draw(sb);
    }

    public boolean touchInside(int x, int y) {
        return (x > mSprite.getX() && x < mSprite.getX() + mSprite.getWidth())
                && (y > mSprite.getY() && y < mSprite.getY()
                        + mSprite.getHeight());
    }
}
