package com.samphippen.explaygamejam2012;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameLogic {	
	public static float MAX_DRIVE_TO_SCREW = 1800f; 
	public static float COG_MOVE_FRAMES = 120f;
	
	public TurnStage mState = TurnStage.GameStart; 	
	public Cog mSeletedCog;
	public boolean mCogWasFromBoard = false; 
	public float mCogOriginalX;
	public float mCogOriginalY; 
	public int mPlayerID; 
	
	private static GameLogic sInstance;
	
	public static GameLogic getInstance() {
	    Logger.println("called");
	    if (sInstance == null) sInstance = new GameLogic();
	    return sInstance;
	}
	
	public int mAnimationFrame = 0; 
	public float mTotalDriveToScrew;

    public int mRollDownFrame;
    public Sprite mRollDownSprite;
    public Sprite mPlayer1;
    public Sprite mPlayer2;
    
    public boolean mShouldPlaySoundOnNextFrame = false;
    
	
    private GameLogic() {
        Texture t = ResourceManager.get("rolldown");
        Logger.println(t);
        mRollDownSprite = new Sprite(t);
        mRollDownSprite.setPosition(0, (GameHolder.CanvasSizeY));
        
        mPlayer1 = new Sprite(ResourceManager.get("player1"));
        mPlayer2 = new Sprite(ResourceManager.get("player2"));
        
        mPlayer1.setPosition((GameHolder.CanvasSizeX * 0.5f) - (mPlayer1.getWidth() * 0.5f), (GameHolder.CanvasSizeY));
        mPlayer2.setPosition((GameHolder.CanvasSizeX * 0.5f) - (mPlayer2.getWidth() * 0.5f), (GameHolder.CanvasSizeY));
    }
	
	private void resetMove() { 
		mCogWasFromBoard = false; 
		mSeletedCog = null; 
		mCogOriginalX = -1;
		mCogOriginalY = -1; 
	}
	
	public void newGame() { 
		mState = TurnStage.ClearGameState; 
		mPlayerID = 1; 
		mTotalDriveToScrew = 0; 
		
		resetMove();
	}
	
	public void sartGame() { 
		mState = TurnStage.GameStart; 
		mPlayerID = 1; 
		mTotalDriveToScrew = 0; 		
		
		resetMove();
	}
	
	public void newTurn() {
	    SoundSystem.playTurnMusic();
		mAnimationFrame = 0;
		mRollDownFrame = 0;
		
		resetMove(); 
		
		mState = TurnStage.WaitingForPlayer; 
	}
	
	public void playerSelectedCog(Cog cog, boolean fromBoard) { 
		mCogWasFromBoard = fromBoard; 
		mSeletedCog = cog; 
		
		if (mCogWasFromBoard == true) { 
			mCogOriginalX = mSeletedCog.getCenterX();
			mCogOriginalY = mSeletedCog.getCenterY();
		}
		
		mState = TurnStage.MovingCog; 
	}
	
	public void placedGrid() {
	    mState = TurnStage.Animating;
	    SoundSystem.playBetweenMusic();
		SoundSystem.playWithDelay("hasplaced2", 100);		
		mShouldPlaySoundOnNextFrame = true; 
	    resetMove();
	}
	
	public void playerPlacedCog(boolean ontoBoard) { 	
		
		if (mCogWasFromBoard && ontoBoard)
		{
			if (mSeletedCog.getCenterX() == mCogOriginalX && 
				mSeletedCog.getCenterY() == mCogOriginalY)
			{
				// the cog has been moved back to its original position 
				mState = TurnStage.WaitingForPlayer; 
			}
			else 
			{
				// the cog has been moved
				mState = TurnStage.Animating;
				SoundSystem.playBetweenMusic();
				SoundSystem.playWithDelay("hasplaced2", 100);
				
				mShouldPlaySoundOnNextFrame = true; 
			}
		}
		else if (!mCogWasFromBoard && !ontoBoard)
		{
			// the cog started and ended in the tray
			mState = TurnStage.WaitingForPlayer; 			
		}
		else if (mCogWasFromBoard && !ontoBoard)
		{
			// the cog started on the board and ended in the tray
			mState = TurnStage.Animating;
			SoundSystem.playBetweenMusic();
			SoundSystem.playWithDelay("hasplaced2", 100);	
			
			mShouldPlaySoundOnNextFrame = true; 
		}
		else 
		{
			// the cog started in the tray and ended on the board 
			mState = TurnStage.Animating;
			SoundSystem.playBetweenMusic();
			SoundSystem.playWithDelay("hasplaced2", 100);
			
			mShouldPlaySoundOnNextFrame = true; 
		}
		
		resetMove(); 
	}		
	
	public void playerFailedToPlaceCog() {
		if (mCogWasFromBoard == true) {
			// the cog has been moved back to its original position
			mSeletedCog.setCenterX(mCogOriginalX);
			mSeletedCog.setCenterY(mCogOriginalY); 
			
			SoundSystem.playWithDelay("hasplaced2", 100);
		}
		
		mState = TurnStage.WaitingForPlayer;
		
		resetMove();
	}	
	
	public void animationTick() {
		mAnimationFrame++; 
		mShouldPlaySoundOnNextFrame = false; 
		
		if (mTotalDriveToScrew >= MAX_DRIVE_TO_SCREW) {
			// player 1 wins!
			SoundSystem.stopGrinding();	
			SoundSystem.stopSound("Rack");  	
			
			Logger.println("Player 1 WINS!!!!"); 
			mState = TurnStage.GameOver;
			SoundSystem.playGameOver();
			mPlayerID = 0; 
			mAnimationFrame = 0; 
		}
		else if (mTotalDriveToScrew <= -MAX_DRIVE_TO_SCREW) {
			// player 1 wins!
			SoundSystem.stopGrinding();	
			SoundSystem.stopSound("Rack");  	
			
			Logger.println("Player 2 WINS!!!!"); 
			mState = TurnStage.GameOver;
			SoundSystem.playGameOver();
			mPlayerID = 1; 
			mAnimationFrame = 0; 
		}
		else if (mAnimationFrame > COG_MOVE_FRAMES) {             
			mState = TurnStage.RollDownStart;
			SoundSystem.stopGrinding();	
			SoundSystem.stopSound("Rack");  			
		}
	}
	
	public void endGameTick() {
		mAnimationFrame++; 
	}

    public void rollDownTick() {
        mRollDownFrame++;
        
        float t = (mRollDownFrame-60)/2;
        float y = 2*(t*t);
        mRollDownSprite.setPosition(0, y);
        
        mPlayer1.setPosition((GameHolder.CanvasSizeX * 0.5f)
                - (mPlayer1.getWidth() * 0.5f), y + 200);
        mPlayer2.setPosition((GameHolder.CanvasSizeX * 0.5f)
                - (mPlayer1.getWidth() * 0.5f), y + 200);
       
        if (mRollDownFrame == 60) {
            mPlayerID = mPlayerID == 0 ? 1 : 0;
            mState = TurnStage.RollDownWaiting;
        } else if (mRollDownFrame == 120) {
            mState = TurnStage.NextPlayer;
        }       
    }
    
    public void setPlayerCogsToPausePosition() {
    		
        mPlayer1.setPosition((GameHolder.CanvasSizeX * 0.5f)
                - (mPlayer1.getWidth() * 0.5f), 200);
        mPlayer2.setPosition((GameHolder.CanvasSizeX * 0.5f)
                - (mPlayer1.getWidth() * 0.5f), 200);    	
    }
    
    public void rollDownWaitingTouched() {
        mState = TurnStage.RollDownEnd;

    }

	public void hidePlayerCogs() {
        mPlayer1.setPosition((GameHolder.CanvasSizeX * 0.5f)
                - (mPlayer1.getWidth() * 0.5f), GameHolder.CanvasSizeY);
        mPlayer2.setPosition((GameHolder.CanvasSizeX * 0.5f)
                - (mPlayer1.getWidth() * 0.5f), GameHolder.CanvasSizeY);    	
	}
}

