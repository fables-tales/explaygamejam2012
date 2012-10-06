package com.samphippen.explaygamejam2012;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundSystem {

    private static Music sTurnMusic;
    private static Music sBetweenMusic;
    private static Music sGrinding;
    private static Music sGameOverMusic;
    private static HashMap<String, Sound> mSoundEffects = new HashMap<String, Sound>();
    private static HashMap<String, Boolean> mSoundCanPlay = new HashMap<String, Boolean>();
    private static Timer t = new Timer();

    public static void initialize() {
        sTurnMusic = Gdx.audio.newMusic(Gdx.files
                .internal("Topology_-_Michael_Nyman_-_And_Do_They_Do_1.mp3"));
        sBetweenMusic = Gdx.audio.newMusic(Gdx.files
                .internal("Topology_-_Michael_Nyman_-_And_Do_They_Do_2.mp3"));

        sGrinding = Gdx.audio.newMusic(Gdx.files.internal("grindingCogs.mp3"));
        sGameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("win.mp3"));

        sTurnMusic.setLooping(true);
        sBetweenMusic.setLooping(true);
        sGrinding.setLooping(true);
        sGameOverMusic.setLooping(false);

        addSound("canplace");
        addSound("hasplaced");
        addSound("CogsJammed");
    }

    private static void addSound(String string) {
        mSoundEffects.put(string,
                Gdx.audio.newSound(Gdx.files.internal(string + ".mp3")));
        mSoundCanPlay.put(string, true);
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

    public static void playWithDelay(final String string, int timeoutMS) {
        if (mSoundCanPlay.get(string)) {
            System.out.println("playing " + string);
            mSoundCanPlay.remove(string);
            mSoundCanPlay.put(string, false);
            mSoundEffects.get(string).play();
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    mSoundCanPlay.remove(string);
                    mSoundCanPlay.put(string, true);
                }
            }, timeoutMS);
        }

    }

    public static void playGameOver() {
        sBetweenMusic.stop();
        sGrinding.stop();
        sTurnMusic.stop();
        sGameOverMusic.play();
    }
}
