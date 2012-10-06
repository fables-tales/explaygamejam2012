package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "InCogNeto";
        cfg.useGL20 = true;
        cfg.width = 800 / 2;
        cfg.height = (1280-75) / 2;

        new LwjglApplication(new GameHolder(), cfg);
    }
}
