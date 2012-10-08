package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animator {
	public List<Sprite> mFrames = new ArrayList<Sprite>();
	public int mFrameNumber = 0;
	public int mTick = 0; 
	public int mFrameSpeed = 5; 
	public boolean mFalling = false; 
	
	public Animator(String baseName, int numberOfFrames, float locationX, float locationY) { 
		
		for (int i = 0; i < numberOfFrames; i++) { 
			mFrames.add(new Sprite(new Texture(Gdx.files.internal(baseName + (i < 10 ? "0" : "") + i + ".png"))));
		}
		
		for (int i = 0; i < mFrames.size(); i++) { 
			mFrames.get(i).setPosition(locationX, locationY);
		}
	}
	
	public void reset() { 
		mFrameNumber = 0; 
		mTick = 0; 
		mFalling = false; 
	}
	
	public void incrementFrame() { 
		mTick++; 
		
		if (mTick >= mFrameSpeed) { 
			mFrameNumber++;	
			mTick = 0;
			
			if (mFalling == false) { 
				if (mFrameNumber > 2) {
					mFrameNumber = 0; 
				}
			}			
		}
		
		if (mFrameNumber >= mFrames.size()) { 
			mFrameNumber = mFrames.size() - 1; 
		}
	}
	
	public void draw(SpriteBatch sb) {
		
		//Logger.println(mFrameNumber);
		mFrames.get(mFrameNumber).draw(sb);
    }
}
