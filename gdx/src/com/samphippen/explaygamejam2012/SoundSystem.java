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
    private static boolean mIsGrinding = false; 
    
    private static GameMusic mActiveMusic = GameMusic.None; 
    
    public static void setMusicVolume(float volume) { 
    	sTurnMusic.setVolume(volume); 
    	sBetweenMusic.setVolume(volume);
    	//sGameOverMusic.setVolume(volume);
    }    
    
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
        sGrinding.setVolume(0.6f); 
        
        addSound("canplace2");
        addSound("hasplaced2");
        addSound("CogsJammed");
        addSound("Rack");
        addSound("trumpet2");
    }

    private static void addSound(String string) {
        mSoundEffects.put(string,
                Gdx.audio.newSound(Gdx.files.internal(string + ".mp3")));
        mSoundCanPlay.put(string, true);
    }

    public static void playTurnMusic() {
    	mActiveMusic = GameMusic.Turn; 
        sTurnMusic.play();
        sBetweenMusic.stop();
    }

    public static void stopGrinding() {
        sGrinding.stop();
        mIsGrinding = false; 
    }
    
    public static void startGrinding() {
    	sGrinding.play();
    	mIsGrinding = true; 
    }

    public static void playBetweenMusic() {
    	mActiveMusic = GameMusic.Between; 
        sBetweenMusic.play();        
        sTurnMusic.stop();
    }

    public static void playWithDelay(final String string, int timeoutMS) {
        if (mSoundCanPlay.get(string)) {
            Logger.println("playing " + string);
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
    
    public static void stopSound(final String string) {
        if (mSoundCanPlay.get(string)) {
            Logger.println("stopping " + string);
            mSoundCanPlay.remove(string);
            mSoundCanPlay.put(string, true);
            mSoundEffects.get(string).stop();
        }
    }
    

    public static void playGameOver() {
    	mActiveMusic = GameMusic.GameOver; 
    	sBetweenMusic.stop();
        sGrinding.stop();
        sTurnMusic.stop();
        sGameOverMusic.play();
    }

	public static void stopMusic() {
		mActiveMusic = GameMusic.None;
		sBetweenMusic.stop();
        sTurnMusic.stop();
        sGameOverMusic.stop();		
	}
	
	public static void pauseMusic() { 
		sBetweenMusic.pause();
        sTurnMusic.pause();
        sGameOverMusic.pause();					
	}
	
	public static void resumeMusic() { 
		switch (mActiveMusic) {
		case Turn:
			sTurnMusic.play();	
			break;
		case Between:
			sBetweenMusic.play();
			break;
		case GameOver: 
			sGameOverMusic.play();
			break;
		default:
			break; 
		}
	}

	public static void pauseGrinding() {
		sGrinding.pause(); 
		
	}

	public static void resumeGrinding() {
		if (mIsGrinding == true) { 
			sGrinding.play(); 	
		}		
	}
}
