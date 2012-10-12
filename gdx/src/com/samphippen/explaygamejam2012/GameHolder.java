package com.samphippen.explaygamejam2012;

import javax.swing.text.MaskFormatter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class GameHolder implements ApplicationListener {

	private boolean mDebugging = false;
	
	public static float CanvasSizeX = 800;
	public static float CanvasSizeY = 1280 - 75; 
	
    private Camera mCamera;
    private Vector2 mCameraOrigin = new Vector2(0, 0);

    private int mCogTime;    
    private ShapeRenderer mDebugShapeRenderer;
    private CogGraph mGraph;
    private GridManager mGridManager;
    private boolean mTouchHeld = false;
    private Cog mHeldCog;
    private boolean mHoldingCog = false;
    private Cog mLastCog;
    private GameLogic mLogic;
    private int mMaskButtonCountDown = 0;
    private boolean mMaskButtonPressed = false;
    private Sprite mMaskButtonSprite;
    private Sprite mMaskButtonSpritePressed;
    private Sprite mRackSprite;
    private Sprite mRackHolderSprite;

    private Sprite mBackgroundSprite;
    private Sprite mSplashSprite;

    private Animator mPlayer1Char;
    private Animator mPlayer2Char; 
    
    private boolean mRunTurns = true;
    private SpriteBatch mSpriteBatch;
    private Tray mTray;
    private boolean mStartupScreen = true;
	private boolean mJammed = false;
	private boolean mJammedDir = true;
	private int mJammedTimer = 0;
	private boolean mKeyHasBeenReleased = true; 

    
    @Override
    public void create() {    

    	Logger.mIsLogging = mDebugging;  
    	
        ResourceManager.loadResources();
        SoundSystem.initialize();
        SoundSystem.setMusicVolume(0.45f); // 0.3f); 
        
        Logger.println("create");
        mLogic = GameLogic.getInstance();

        mTray = new Tray();
        mGraph = new CogGraph();

        Texture t = new Texture(Gdx.files.internal("rack.png"));
        mRackSprite = new Sprite(t);
        mRackSprite.setPosition(CanvasSizeX / 2 - mRackSprite.getWidth() / 2,
        		CanvasSizeY - mRackSprite.getHeight() - 3);

        t = new Texture(Gdx.files.internal("rackholder.png"));

        mRackHolderSprite = new Sprite(t);
        mRackHolderSprite.setPosition(
        		CanvasSizeX / 2 - mRackHolderSprite.getWidth() / 2,
        		CanvasSizeY - mRackHolderSprite.getHeight() + 5);

        mMaskButtonSprite = new Sprite(ResourceManager.get("maskbutton"));

        mMaskButtonSprite.setX(40);
        mMaskButtonSprite.setY(20);
        mMaskButtonSpritePressed = new Sprite(ResourceManager.get("maskbuttonpressed"));
        mMaskButtonSpritePressed.setX(40);
        mMaskButtonSpritePressed.setY(20);

        mBackgroundSprite = new Sprite(ResourceManager.get("background"));
        mBackgroundSprite.setPosition(0, -70);

        t = ResourceManager.get("rolldown");
        mSplashSprite = new Sprite(t);
        //mSplashSprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        mGridManager = new GridManager();
        mSpriteBatch = new SpriteBatch();
        mSpriteBatch.enableBlending();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mCamera = new OrthographicCamera(CanvasSizeX, CanvasSizeY);
        mCameraOrigin.set(CanvasSizeX * 0.5f, CanvasSizeY / 2);

        mPlayer1Char = new Animator("hh", 29, CanvasSizeX -240, 270);
        mPlayer2Char = new Animator("moose", 29, 0,  270);       

        mDebugShapeRenderer = new ShapeRenderer();

        mLastCog = mGraph.mDrive;
        
        // createTestGraph();
        // createTestGraph2();

        mLogic.newGame();
        
    	Gdx.input.setCatchBackKey(true); 
    }

    @Override
    public void dispose() {
        //ResourceManager.dispose();
    }

    public void draw() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor((1f / 255f) * 155f, (1f / 255f) * 121f,
                (1f / 255f) * 71f, 1);

        Matrix4 traslate = new Matrix4().translate(-getCameraOrigin().x,
                -getCameraOrigin().y, 0);

        mSpriteBatch.setProjectionMatrix(mCamera.combined);
        mSpriteBatch.setTransformMatrix(traslate);

        mSpriteBatch.begin();
        
        boolean animateJam = mLogic.mState == TurnStage.Animating && mJammed; 
        
        mBackgroundSprite.draw(mSpriteBatch);

        mGraph.mDrive.draw(mSpriteBatch, animateJam, mJammedDir);
        mGraph.mScrew.draw(mSpriteBatch, animateJam, mJammedDir);

        mTray.preDraw(mSpriteBatch);
        
        for (int i = 0; i < mGraph.mCogs.size(); i++) {
            Cog c = mGraph.mCogs.get(i);
            
            if (c == mHeldCog) continue; 
            if (c.getIsFixed() == false) c.draw(mSpriteBatch, animateJam, mJammedDir);
        }

        mRackSprite.draw(mSpriteBatch);
        mRackHolderSprite.draw(mSpriteBatch);
        mTray.draw(mSpriteBatch);
        mGridManager.drawCurrentPlayer(mSpriteBatch, mLogic.mPlayerID);
        mGridManager.drawOtherPlayer(mSpriteBatch, 1 - mLogic.mPlayerID);

        if (mMaskButtonPressed) {
            mMaskButtonSpritePressed.draw(mSpriteBatch);
        } else {
            mMaskButtonSprite.draw(mSpriteBatch);
        }
        
        if (mHeldCog != null) { 
        	mHeldCog.draw(mSpriteBatch); 
        }
        
        mPlayer1Char.incrementFrame(); 
        mPlayer1Char.draw(mSpriteBatch);
        
        mPlayer2Char.incrementFrame(); 
        mPlayer2Char.draw(mSpriteBatch);
        
        mLogic.mRollDownSprite.draw(mSpriteBatch);

        if (mLogic.mPlayerID == 0) { 
        	mLogic.mPlayer1.draw(mSpriteBatch);         	
        }
        else {  
        	mLogic.mPlayer2.draw(mSpriteBatch);
        }
        
        mSpriteBatch.end();

        if (mDebugging == true) {
            mDebugShapeRenderer.setProjectionMatrix(mCamera.combined);
            mDebugShapeRenderer.setTransformMatrix(traslate);

            mDebugShapeRenderer.begin(ShapeType.Line);
            mDebugShapeRenderer.setColor(0, 1, 0, 1);

            mGraph.renderDebugLines(mDebugShapeRenderer);

            mDebugShapeRenderer.end();
        }
        
        mJammedTimer++; 
        
        if (mJammedTimer > 3) { 
        	mJammedDir = !mJammedDir;
        	mJammedTimer = 0; 
        }
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    	mStartupScreen = true; 
    	SoundSystem.pauseGrinding(); 
    	SoundSystem.pauseMusic(); 
    }
    
    @Override
    public void render() {
    	if (Gdx.input.isKeyPressed(Keys.BACK) == true ||
    		Gdx.input.isKeyPressed(Keys.ESCAPE) == true) 
    	{
    		if (mKeyHasBeenReleased == true) {
	    		if (mStartupScreen == true) { 
	    			Gdx.app.exit();
	    		}
	    		else { 
	    			pause();     			
	    		}
	    		
	    		mKeyHasBeenReleased = false; 
    		}
    	}
    	else if (Gdx.input.isKeyPressed(Keys.SPACE) == true) { 
    		if (mKeyHasBeenReleased == true) {    			
    			mDebugging = !mDebugging;    
    			Logger.mIsLogging = mDebugging;
    			mKeyHasBeenReleased = false; 
    		}
    	}
    	else { 
    		mKeyHasBeenReleased = true;     		
    	}
    	
    	

        if (!mStartupScreen) {
            update();
            draw();            
        } else {
            drawStartupScreen();
        }
    }

    private void drawStartupScreen() {
    	mLogic.setPlayerCogsToPausePosition(); 
    	
    	if (Gdx.input.isTouched()) {
            mStartupScreen = false;
            SoundSystem.resumeMusic(); 
            SoundSystem.resumeGrinding(); 
            mLogic.hidePlayerCogs(); 
        }

        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor((1f / 255f) * 155f, (1f / 255f) * 121f,
                (1f / 255f) * 71f, 1);

        Matrix4 traslate = new Matrix4().translate(-getCameraOrigin().x,
                -getCameraOrigin().y, 0);

        mSpriteBatch.setProjectionMatrix(mCamera.combined);
        mSpriteBatch.setTransformMatrix(traslate);
        
        mSpriteBatch.begin();
        mSplashSprite.draw(mSpriteBatch);                
        
        if (mLogic.mPlayerID == 0) { 
        	mLogic.mPlayer1.draw(mSpriteBatch);         	
        }
        else {  
        	mLogic.mPlayer2.draw(mSpriteBatch);
        }
        
        mSpriteBatch.end();

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    public void update() {
        mMaskButtonCountDown -= 1;

        switch (mLogic.mState) {
        case ClearGameState:
            mTray.addCogs(mGraph.mCogs);
            mGraph.clear();
            mLogic.sartGame();
            mLogic.mRollDownFrame = 60; 
            doRollDown();
            break;
        case GameStart:        	
        	// other stuff
            //mLogic.newTurn();
            mLogic.mState = TurnStage.RollDownStart;
            mLogic.mRollDownFrame = 60; 
            doRollDown();
            break;
        case WaitingForPlayer:
            doSelectingEvents();
            break;
        case MovingCog:
            doMovingEvents();
            break;
        case Animating:
            doAnimation();
            break;
        case RollDownEnd:
        case RollDownStart:
            doRollDown();
            break;
        case RollDownWaiting:
            if (Gdx.input.isTouched()) {
                GameLogic.getInstance().rollDownWaitingTouched();
            }
            break;
        case NextPlayer:
            switchPlayerView();
            break;
        case GameOver:
            // other stuff
            doGameOverEvents();
            break;
        default:
            break;

        }

        mCogTime += 1;

        for (int i = 0; i < mGraph.mCogs.size(); i++) {
            Cog c = mGraph.mCogs.get(i);
            c.update();
        }
    }

    private void doRollDown() {
        mLogic.rollDownTick();
    }

    /*
     * private void createTestGraph() {
     * 
     * float w = Gdx.graphics.getWidth(); float h = Gdx.graphics.getHeight();
     * 
     * Cog cog1 = mTray.getCog(); cog1.setCenterX(w * 0.5f); cog1.setCenterY(h -
     * 80);
     * 
     * Cog cog2 = mTray.getCog(); cog2.setCenterX(w * 0.5f); cog2.setCenterY(h -
     * 160);
     * 
     * mGraph.addCog(cog1); mGraph.addCog(cog2);
     * 
     * mGraph.add(cog1, cog2);
     * 
     * mGraph.evaluate();
     * 
     * mHeldCog = mTray.getCog(); mGraph.addCog(mHeldCog);
     * 
     * mHeldCog.setCenterX(w * 0.5f); mHeldCog.setCenterY(h - 240);
     * 
     * mHeldCog.fixToGrid(); if (mGraph.dropCog(mHeldCog) == false) {
     * mGraph.removeCog(mHeldCog); mTray.addCog(mHeldCog); }
     * 
     * // mGraph.removeCog(mHeldCog); }
     * 
     * private void createTestGraph2() { float w = Gdx.graphics.getWidth();
     * float h = Gdx.graphics.getHeight();
     * 
     * Cog cog1 = mTray.getCog(); cog1.setCenterX(w * 0.5f); cog1.setCenterY(h -
     * 64);
     * 
     * Cog cog2 = mTray.getCog(); cog2.setCenterX(w * 0.5f); cog2.setCenterY(h -
     * 128);
     * 
     * Cog cog3 = mTray.getCog(); cog3.setCenterX(w * 0.5f); cog3.setCenterY(h -
     * 192);
     * 
     * Cog cog4 = mTray.getCog(); cog4.setCenterX(w * 0.5f); cog4.setCenterY(h -
     * 256);
     * 
     * mGraph.addCog(cog1); mGraph.addCog(cog2); mGraph.addCog(cog3);
     * mGraph.addCog(cog4);
     * 
     * mGraph.add(cog1, cog2); mGraph.add(cog2, cog3); mGraph.add(cog3, cog4);
     * 
     * mGraph.evaluate();
     * 
     * mHeldCog = mTray.getCog(); mGraph.addCog(mHeldCog);
     * 
     * mHeldCog.setCenterX(w * 0.5f); mHeldCog.setCenterY(h - 320);
     * 
     * mHeldCog.fixToGrid(); if (mGraph.dropCog(mHeldCog) == false) {
     * mGraph.removeCog(mHeldCog); mTray.addCog(mHeldCog); }
     * 
     * mGraph.removeCog(mHeldCog);
     * 
     * if (mGraph.dropCog(mHeldCog) == false) { mGraph.removeCog(mHeldCog);
     * mTray.addCog(mHeldCog); }
     * 
     * // mGraph.removeCog(cog2); }
     */

    private void doAnimation() {
    	
    	if (mLogic.mAnimationFrame <= 1) { 
    		mGraph.refactorForward();
    	}
    	
        mMaskButtonPressed = false;
        float oldScrewAngle = mGraph.mScrew.mAngle;

        mJammed = !mGraph.evaluate();
        
        if (mLogic.mShouldPlaySoundOnNextFrame == true) {
        	if (mJammed == false) {
        		SoundSystem.startGrinding();  
        	}
        	else { 
        		SoundSystem.playWithDelay("CogsJammed", 500);
        	}
        }

        float newScrewAngle = mGraph.mScrew.mAngle;

        mLogic.mTotalDriveToScrew += oldScrewAngle - newScrewAngle;
        
        if (Math.abs(oldScrewAngle - newScrewAngle) >= 1) {
            mRackSprite.translate((oldScrewAngle - newScrewAngle) * 0.04f, 0);
            mRackHolderSprite.translate((oldScrewAngle - newScrewAngle) * 0.04f * 0.5f, 0);
            
            if (mLogic.mShouldPlaySoundOnNextFrame == true) {
            	SoundSystem.playWithDelay("Rack", 500);
            }
            		
            if (((oldScrewAngle - newScrewAngle > 0 && mLogic.mTotalDriveToScrew > 0) ||
            	(oldScrewAngle - newScrewAngle < 0 && mLogic.mTotalDriveToScrew < 0)) &&
            	Math.abs(mLogic.mTotalDriveToScrew) == mLogic.MAX_DRIVE_TO_SCREW - 600) {            	
            	SoundSystem.playWithDelay("trumpet2", 300);
            }
        }

        mLogic.animationTick();
    }

    private void doMovingEvents() {

        if (mHoldingCog && !Gdx.input.isTouched()) {

            Logger.println("Dropping cog");

            mHeldCog.setMouseTracking(false);
            mHeldCog.fixToGrid();

            if (mTray.touchInside(InputHandler.getScreenX(),
                    InputHandler.getScreenY())) {
                mLogic.playerPlacedCog(false);
                mGraph.removeCog(mHeldCog);
                mTray.addCog(mHeldCog);
                mGraph.refactorForward();
                
            } else if (mGraph.dropCog(mHeldCog) == false) {
                Logger.println("Dropping failed");
                
                if (mLogic.mCogWasFromBoard == false) {
                    mGraph.removeCog(mHeldCog);
                    mTray.addCog(mHeldCog);
                    mLogic.playerFailedToPlaceCog();
                    mGraph.refactorForward();
                }
                else { 
                    mLogic.playerFailedToPlaceCog();
                	mGraph.dropCog(mHeldCog);             	
                }
            } else {
                mLogic.playerPlacedCog(true);
                mGraph.refactorForward();
            }

            Logger.println("");
            Logger.println("");

            mLastCog = mHeldCog;
            mHeldCog = null;
            mHoldingCog = false;
        } else {
            mHeldCog.fixToGrid();

            boolean canPlace = mGraph.checkDropCog(mHeldCog);

            mHeldCog.setCanPlace(canPlace);
        }

    }

    private void doSelectingEvents() {
        
    	if (Gdx.input.isTouched() == false) mTouchHeld = false; 
    		
    	if (mTouchHeld == true) { return; } 
    	
    	int gridX = getGridX(InputHandler.getScreenX());
        int gridY = getGridY(InputHandler.getScreenY());

        if (!mHoldingCog && Gdx.input.isTouched()) {
            if (inputInMaskButton(InputHandler.getScreenX(),
                    InputHandler.getScreenY())) {
                toggleMaskMode();
            } else if (mTray.touchInside(InputHandler.getScreenX(),
                    InputHandler.getScreenY())) {

                Logger.println("Selecting cog");

                int size = 5;

                int x = InputHandler.getScreenX();

                int rb = 121, b = 326, m = 498, s = 633, t = 732;

                if (x < rb + ((b - rb) / 2)) size = 5;
                else if (x < b + ((m - b) / 2)) size = 4;
                else if (x < m + ((s - m) / 2)) size = 3;
                else if (x < s + ((t - s) / 2)) size = 2;
                else size = 1;

                mHeldCog = mTray.getCog(size);

                if (mHeldCog != null) {
                    mHoldingCog = true;

                    mGraph.addCog(mHeldCog);
                    mHeldCog.setMouseTracking(true);
                    mCogTime = 0;

                    mLogic.playerSelectedCog(mHeldCog, false);
                }
            } 
            else if (inputInGrid(InputHandler.getScreenX(), InputHandler.getScreenY())
                    && !inputInMaskButton(InputHandler.getScreenX(), InputHandler.getScreenY())
                    && mMaskButtonPressed) {

                toggleGridSquare(gridX, gridY);                             
                
            }
            else {
                mHeldCog = mGraph.touchOnCog(InputHandler.getScreenX(),
                        InputHandler.getScreenY());
                int x = InputHandler.getScreenX();
                int y = InputHandler.getScreenY();

                if (mHeldCog != null && !mGridManager.touchInBlock(getGridX(x), getGridY(y), mLogic.mPlayerID)) {
                    Logger.println("Picking up cog");

                    mHoldingCog = true;
                    mHeldCog.setMouseTracking(true);

                    Logger.println("");
                    Logger.println("");

                    mLogic.playerSelectedCog(mHeldCog, true);
                } 
                else 
                {
                	mTouchHeld = true;
                	mHeldCog = null; 	                	
                }
            }
        }
    }

    private void doGameOverEvents() {

        mLogic.endGameTick();
        
        if (mLogic.mAnimationFrame == 1) { 
        	mGridManager.reset(); 
        }
        else if (mLogic.mAnimationFrame > 360) {
	        mPlayer1Char.mFalling = mLogic.mPlayerID == 0;
	        mPlayer2Char.mFalling = mLogic.mPlayerID == 1; 
    	}
        
        if (mLogic.mAnimationFrame > 480 && Gdx.input.isTouched()) {
        	mGridManager.reset(); 
        	
        	mPlayer1Char.reset(); 
        	mPlayer2Char.reset(); 
        	
        	mLogic.newGame();
        	
            mRackSprite.setPosition(CanvasSizeX / 2 - mRackSprite.getWidth() / 2,
            		CanvasSizeY - mRackSprite.getHeight() - 3);
            mRackHolderSprite.setPosition(
            		CanvasSizeX / 2 - mRackHolderSprite.getWidth() / 2,
            		CanvasSizeY - mRackHolderSprite.getHeight() + 5);
        }
    }

    private Vector2 getCameraOrigin() {
        return mCameraOrigin;
    }

    private int getGridX(int x) {
        return mGridManager.getGridX(x);
    }

    private int getGridY(int y) {
        return mGridManager.getGridY(y);
    }

    private boolean inputInGrid(int x, int y) {
        // rack and tray both occupy 64 pixels of space at the top of the screen
        return y > 64 && y < CanvasSizeY - 64;
    }

    private boolean inputInMaskButton(int x, int y) {
        return (x > mMaskButtonSprite.getX() && x < mMaskButtonSprite.getX()
                + mMaskButtonSprite.getHeight())
                && (y > mMaskButtonSprite.getY() && y < mMaskButtonSprite
                        .getY() + mMaskButtonSprite.getHeight());
    }

    private void switchPlayerView() {
        mLogic.newTurn();
    }

    private void toggleGridSquare(int gridX, int gridY) {
        mGridManager.receiveTouch(gridX, gridY, mLogic.mPlayerID);
    }

    private void toggleMaskMode() {
        if (mMaskButtonCountDown <= 0) {
            mMaskButtonPressed = !mMaskButtonPressed;
            Logger.println("triggering " + mMaskButtonPressed);
            mMaskButtonCountDown = 10;
            if (mMaskButtonPressed == true) {
                mGridManager.clearPlayer(mLogic.mPlayerID);
                mGridManager.resetCountdown();
            } else if (mMaskButtonPressed == false) {
                mGridManager.hideCandidateSquares(mLogic.mPlayerID);
            }
        }
    }

}
