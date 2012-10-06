package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.Gdx;

public class InputHandler {
    public static int getScreenX() {
        return Gdx.input.getX() * 2;
    }
    
    public static int getScreenY() {
        return (Gdx.graphics.getHeight() - Gdx.input.getY())*2;
    }
}
