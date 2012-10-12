package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.Gdx;

public class InputHandler {
    
    static float ratioY = -1;
    static float ratioX = -1;
    
    public static void computeRatio() {
    	ratioX = GameHolder.CanvasSizeX / Gdx.graphics.getWidth();
        ratioY = GameHolder.CanvasSizeY / Gdx.graphics.getHeight();        
    }
    
    public static int getScreenX() {
        if (ratioX == -1) computeRatio();
        return (int) (Gdx.input.getX() * ratioX);
    }
    
    public static int getScreenY() {
    	if (ratioY == -1) computeRatio();
        return (int)((Gdx.graphics.getHeight() - Gdx.input.getY()) * ratioY);
    }
}
