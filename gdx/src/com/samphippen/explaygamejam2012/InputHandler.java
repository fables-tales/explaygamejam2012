package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.Gdx;

public class InputHandler {
    
    static float TARGETHEIGHT = 1280-75;
    static float TARGETWIDTH  = 800;
    static float ratio = -1;
    
    public static void computeRatio() {
        ratio = TARGETHEIGHT/Gdx.graphics.getHeight();
    }
    
    public static int getScreenX() {
        if (ratio == -1) computeRatio();
        return (int) (Gdx.input.getX() * ratio);
    }
    
    public static int getScreenY() {
        if (ratio == -1) computeRatio();
        return (int)((Gdx.graphics.getHeight() - Gdx.input.getY())*ratio);
    }
}
