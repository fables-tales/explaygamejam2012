package com.samphippen.explaygamejam2012;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ResourceManager {
	public static Map<String, Texture> mTextures = new HashMap<String, Texture>(); 
	
	public static void loadResources() {
		mTextures.put("cogA", new Texture(Gdx.files.internal("circle.png")));
		mTextures.put("tray", new Texture(Gdx.files.internal("tray.png")));
		mTextures.put("maskbutton", new Texture(Gdx.files.internal("maskbutton.png")));
	}
	
	public static void dispose() { 
		mTextures.get("cogA").dispose(); 
		mTextures.get("tray").dispose();
		mTextures.get("maskbutton").dispose();
		
		mTextures.clear(); 
	}

	public static Texture get(String string) {
		return mTextures.get(string);
	}
}
