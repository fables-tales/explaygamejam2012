package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundSystem {

    private static Music sTurnMusic;
    private static Music sBetweenMusic;
    private static Music sGrinding;

    public static void initialize() {
        sTurnMusic = Gdx.audio.newMusic(Gdx.files
                .internal("Topology_-_Michael_Nyman_-_And_Do_They_Do_1.mp3"));
        sBetweenMusic = Gdx.audio.newMusic(Gdx.files
                .internal("Topology_-_Michael_Nyman_-_And_Do_They_Do_2.mp3"));
        
        sGrinding = Gdx.audio.newMusic(Gdx.files.internal("grindingCogs.mp3"));
        
        sTurnMusic.setLooping(true);
        sBetweenMusic.setLooping(true);
        sGrinding.setLooping(true);
    }

    public static void playTurnMusic() {
        sTurnMusic.play();
        sBetweenMusic.stop();
    }
    
    public static void stopGrinding() {
        sGrinding.stop();
    }

    public static void playBetweenMusic() {
        sBetweenMusic.play();
        sGrinding.play();
        sTurnMusic.stop();
    }
}
