package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "gdx";
		cfg.useGL20 = false;
		cfg.width = 800/2;
		cfg.height = 1280/2;
		
		new LwjglApplication(new GameHolder(), cfg);
	}
}
