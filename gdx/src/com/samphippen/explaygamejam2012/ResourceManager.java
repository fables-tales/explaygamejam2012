package com.samphippen.explaygamejam2012;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class ResourceManager {
    public static Map<String, Texture> mTextures = new HashMap<String, Texture>();

    public static void loadResources() {
        mTextures.put("cog1", new Texture(Gdx.files.internal("c1.png")));
        mTextures.put("cog2", new Texture(Gdx.files.internal("c2.png")));
        mTextures.put("cog3", new Texture(Gdx.files.internal("c3.png")));
        mTextures.put("cog4", new Texture(Gdx.files.internal("c4.png")));
        mTextures.put("cog5", new Texture(Gdx.files.internal("c5.png")));

        mTextures.put("tray", new Texture(Gdx.files.internal("cogcover.png")));
        mTextures.put("maskbutton",
                new Texture(Gdx.files.internal("maskbutton.png")));
        mTextures.put("maskbuttonpressed",
                new Texture(Gdx.files.internal("maskbuttonpressed.png")));
        mTextures.put("player1", new Texture(Gdx.files.internal("player1.png")));
        mTextures.put("player2", new Texture(Gdx.files.internal("player2.png")));
        mTextures.put("rolldown",
                new Texture(Gdx.files.internal("dropdown.png")));
        mTextures.put("background",
                new Texture(Gdx.files.internal("background.png")));
        mTextures.put("bigcog", new Texture(Gdx.files.internal("maincog.png")));
        mTextures.put("wheelcover",
                new Texture(Gdx.files.internal("wheelcover.png")));
        
        mTextures.put("splash", new Texture(Gdx.files.internal("text.png")));

        for (String key : mTextures.keySet()) {
            mTextures.get(key).setFilter(TextureFilter.Linear,
                    TextureFilter.Linear);
        }

        //Logger.println(new Texture(Gdx.files.internal("rolldown.png")));
    }

    public static void dispose() {
        for (String key : mTextures.keySet()) {
            mTextures.get(key).dispose();
        }

        mTextures.clear();
    }

    public static Texture get(String string) {
        return mTextures.get(string);
    }
}
