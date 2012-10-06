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
	public int mFrameSpeed = 3; 
	
	public Animator(String baseName, int numberOfFrames, float locationX, float locationY) { 
		
		for (int i = 0; i < numberOfFrames; i++) { 
			mFrames.add(new Sprite(new Texture(Gdx.files.internal(baseName + numberOfFrames + ".png"))));
		}
		
		for (int i = 0; i < mFrames.size(); i++) { 
			mFrames.get(i).setPosition(locationX, locationX);
		}
	}
	
	public void reset() { 
		mFrameNumber = 0; 
		mTick = 0; 
	}
	
	public void incrementFrame() { 
		mTick++; 
		
		if (mTick >= mFrameSpeed) { 
			mFrameNumber++;	
			mTick = 0; 
		}
		
		if (mFrameNumber >= mFrames.size()) { 
			mFrameNumber =  mFrames.size() - 1; 
		}
	}
	
	public void draw(SpriteBatch sb) {
		mFrames.get(mFrameNumber).draw(sb);
    }
}
