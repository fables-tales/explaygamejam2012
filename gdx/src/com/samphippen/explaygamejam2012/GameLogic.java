package com.samphippen.explaygamejam2012;

public class GameLogic {	
	public static float MAX_DRIVE_TO_SCREW = 360f; 
	
	public TurnStage mState = TurnStage.GameStart; 	
	public Cog mSeletedCog;
	public boolean mCogWasFromBoard = false; 
	public float mCogOriginalX;
	public float mCogOriginalY; 
	public int mPlayerID; 
	public int mAnimationFrame = 0; 
	public float mTotalDriveToScrew;
	
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
		mPlayerID = mPlayerID == 0 ? 1 : 0; 
		mAnimationFrame = 0;
		
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
			}
		}
		else if (!mCogWasFromBoard && !ontoBoard)
		{
			// the cog started and ended in the tray
			mState = TurnStage.WaitingForPlayer; 			
		}
		else 
		{
			// the cog started in the tray and ended on the board 
			mState = TurnStage.Animating;
		}
		
		resetMove(); 
	}		
	
	public void playerFailedToPlaceCog() {
		if (mCogWasFromBoard == true) {
			// the cog has been moved back to its original position
			mSeletedCog.setCenterX(mCogOriginalX);
			mSeletedCog.setCenterY(mCogOriginalY); 
		}
		
		mState = TurnStage.WaitingForPlayer;
		
		resetMove();
	}	
	
	public void animationTick() {
		mAnimationFrame++; 

		if (mTotalDriveToScrew >= MAX_DRIVE_TO_SCREW) {
			// player 1 wins!
			System.out.println("Player 1 WINS!!!!"); 
			mState = TurnStage.GameOver; 
		}
		else if (mTotalDriveToScrew <= -MAX_DRIVE_TO_SCREW) {
			// player 1 wins!
			System.out.println("Player 2 WINS!!!!"); 
			mState = TurnStage.GameOver; 
		}
		else if (mAnimationFrame > 120) { 
			mState = TurnStage.NextPlayer; 
		}
	}
}

