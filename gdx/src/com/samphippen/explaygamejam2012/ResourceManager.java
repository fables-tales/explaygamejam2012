package com.samphippen.explaygamejam2012;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class ResourceManager {
	public static Map<String, Texture> mTextures = new HashMap<String, Texture>(); 
	
	public static void loadResources() {
		mTextures.put("cogA", new Texture(Gdx.files.internal("circle.png")));
		mTextures.put("tray", new Texture(Gdx.files.internal("tray.png")));
		mTextures.put("maskbutton", new Texture(Gdx.files.internal("maskbutton.png")));
		mTextures.put("p1wins", new Texture(Gdx.files.internal("p1_wins.png")));
		mTextures.put("p2wins", new Texture(Gdx.files.internal("p2_wins.png")));
		mTextures.put("rolldown", new Texture(Gdx.files.internal("rolldown.png")));
		mTextures.put("background", new Texture(Gdx.files.internal("background.png")));
		
		for (String key : mTextures.keySet()) {
		    mTextures.get(key).setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		System.out.println(new Texture(Gdx.files.internal("rolldown.png")));
	}
	
	public static void dispose() { 
		mTextures.get("cogA").dispose(); 
		mTextures.get("tray").dispose();
		mTextures.get("maskbutton").dispose();
		mTextures.get("p1wins").dispose();
		mTextures.get("p2wins").dispose();
		
		mTextures.clear(); 
	}

	public static Texture get(String string) {
		return mTextures.get(string);
	}
}
