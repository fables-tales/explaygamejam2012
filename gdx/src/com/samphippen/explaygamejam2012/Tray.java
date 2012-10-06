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
    
    private List<Cog> mCogs_ReallyBig = new ArrayList<Cog>();
    private List<Cog> mCogs_Big = new ArrayList<Cog>();
    private List<Cog> mCogs_Medium = new ArrayList<Cog>();
    private List<Cog> mCogs_Small = new ArrayList<Cog>();
    private List<Cog> mCogs_Tiny = new ArrayList<Cog>();
    
    private static Random sRng = new Random();

    public Tray() {

        mSprite = new Sprite(ResourceManager.get("tray"));
        mWheelCoverSprite = new Sprite(ResourceManager.get("wheelcover"));
        
        for (int i = 0; i < MAX_COGS_EVER / 5; i++) {
        	mCogs_ReallyBig.add(Cog.getCog(5));
        }

        for (int i = 0; i < MAX_COGS_EVER / 5; i++) {
        	mCogs_Big.add(Cog.getCog(4));
        }
        
        for (int i = 0; i < MAX_COGS_EVER / 5; i++) {
        	mCogs_Medium.add(Cog.getCog(3));
        }

        for (int i = 0; i < MAX_COGS_EVER / 5; i++) {
        	mCogs_Small.add(Cog.getCog(2));
        }

        for (int i = 0; i < MAX_COGS_EVER / 5; i++) {
        	mCogs_Tiny.add(Cog.getCog(1));
        }

    }

	public void addCog(Cog cog) {
		
		if (cog.mSize == 1) mCogs_Tiny.add(cog);
		else if (cog.mSize == 2) mCogs_Small.add(cog);
		else if (cog.mSize == 3) mCogs_Medium.add(cog);
		else if (cog.mSize == 4) mCogs_Big.add(cog);
		else if (cog.mSize == 5) mCogs_ReallyBig.add(cog);
		
	}
	
	public void addCogs(List<Cog> cogs) {
		
		for (int i = 0; i < cogs.size(); i++) { 
			Cog cog = cogs.get(i); 
			
			if (cog.getIsFixed() == false) { 
				
				addCog(cog); 				
			}
		}		
	}
	
    public Cog getCog(int size) {
    	
    	List<Cog> list = null; 
    	
		if (size == 1) list = mCogs_Tiny;
		else if (size == 2) list = mCogs_Small;
		else if (size == 3) list = mCogs_Medium;
		else if (size == 4) list = mCogs_Big;
		else if (size == 5) list = mCogs_ReallyBig;
		
        if (list.size() > 0) {
            return list.remove(0);
        }

        return null;
    }
    
    public void preDraw(SpriteBatch sb) {
        mWheelCoverSprite.setPosition(0, -110);
        mWheelCoverSprite.draw(sb);    	
    }

    public void draw(SpriteBatch sb) {	
    	if (mCogs_ReallyBig.size() > 0) {
    		mCogs_ReallyBig.get(0).setCenterX(121);
    		mCogs_ReallyBig.get(0).setCenterY(156);
    		mCogs_ReallyBig.get(0).draw(sb);
    	}
        
    	if (mCogs_Big.size() > 0) {
    		mCogs_Big.get(0).setCenterX(326);
    		mCogs_Big.get(0).setCenterY(112);
    		mCogs_Big.get(0).draw(sb);
    	}
        
        if (mCogs_Medium.size() > 0) {
        	mCogs_Medium.get(0).setCenterX(498);
        	mCogs_Medium.get(0).setCenterY(95);
        	mCogs_Medium.get(0).draw(sb);
    	} 
        
    	if (mCogs_Small.size() > 0) {
    		mCogs_Small.get(0).setCenterX(633);
    		mCogs_Small.get(0).setCenterY(83);
    		mCogs_Small.get(0).draw(sb);
    	}
        
    	if (mCogs_Tiny.size() > 0) {
    		mCogs_Tiny.get(0).setCenterX(732);
    		mCogs_Tiny.get(0).setCenterY(69);
    		mCogs_Tiny.get(0).draw(sb);
    	}
    	
        mSprite.setPosition(0, 0);
        mSprite.draw(sb);
        
        
    }

    public boolean touchInside(int x, int y) {
        return (x > mSprite.getX() && x < mSprite.getX() + mSprite.getWidth())
                && (y > mSprite.getY() && y < mSprite.getY()
                        + mSprite.getHeight());
    }
}
