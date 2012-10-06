package com.samphippen.explaygamejam2012;

import javax.swing.text.MaskFormatter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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

    private Camera mCamera;
    private Vector2 mCameraOrigin = new Vector2(0, 0);

    private int mCogTime;
    private boolean mDebugging = false;
    private ShapeRenderer mDebugShapeRenderer;
    private CogGraph mGraph;
    private GridManager mGridManager;
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
    private Sprite mPlayer1Wins;
    private Sprite mPlayer2Wins;
    private Sprite mBackgroundSprite;
    private Sprite mSplashSprite;

    private boolean mRunTurns = true;
    private SpriteBatch mSpriteBatch;
    private Tray mTray;
    private boolean mStartupScreen = true;

    @Override
    public void create() {
        ResourceManager.loadResources();
        SoundSystem.initialize();
        System.out.println("create");
        mLogic = GameLogic.getInstance();

        mTray = new Tray();
        mGraph = new CogGraph();

        Texture t = new Texture(Gdx.files.internal("rack.png"));
        mRackSprite = new Sprite(t);
        mRackSprite.setPosition(800 / 2 - mRackSprite.getWidth() / 2,
                1280 - mRackSprite.getHeight());

        t = new Texture(Gdx.files.internal("rackholder.png"));

        mRackHolderSprite = new Sprite(t);
        mRackHolderSprite.setPosition(
                800 / 2 - mRackHolderSprite.getWidth() / 2,
                1280 - mRackHolderSprite.getHeight());

        mMaskButtonSprite = new Sprite(ResourceManager.get("maskbutton"));

        mMaskButtonSprite.setX(40);
        mMaskButtonSprite.setY(20);
        mMaskButtonSpritePressed = new Sprite(ResourceManager.get("maskbuttonpressed"));
        mMaskButtonSpritePressed.setX(40);
        mMaskButtonSpritePressed.setY(20);

        mBackgroundSprite = new Sprite(ResourceManager.get("background"));
        mBackgroundSprite.setPosition(0, -70);

        t = ResourceManager.get("splash");
        mSplashSprite = new Sprite(t);

        mGridManager = new GridManager();
        mSpriteBatch = new SpriteBatch();
        mSpriteBatch.enableBlending();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mCamera = new OrthographicCamera(800, 1280);
        mCameraOrigin.set(400, 1280 / 2);

        mPlayer1Wins = new Sprite(ResourceManager.get("p1wins"));
        mPlayer2Wins = new Sprite(ResourceManager.get("p2wins"));

        mPlayer1Wins.setPosition((800 * 0.5f)
                - (mPlayer1Wins.getWidth() * 0.5f), (1280 * 0.5f)
                - (mPlayer1Wins.getHeight() * 0.5f));
        mPlayer2Wins.setPosition((800 * 0.5f)
                - (mPlayer2Wins.getWidth() * 0.5f), (1280 * 0.5f)
                - (mPlayer2Wins.getHeight() * 0.5f));

        mDebugShapeRenderer = new ShapeRenderer();

        mLastCog = mGraph.mDrive;

        // createTestGraph();
        // createTestGraph2();

        mLogic.newGame();
    }

    @Override
    public void dispose() {
        ResourceManager.dispose();
    }

    public void draw() {
        Gdx.gl.glClearColor((1f / 255f) * 155f, (1f / 255f) * 121f,
                (1f / 255f) * 71f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Matrix4 traslate = new Matrix4().translate(-getCameraOrigin().x,
                -getCameraOrigin().y, 0);

        mSpriteBatch.setProjectionMatrix(mCamera.combined);
        mSpriteBatch.setTransformMatrix(traslate);

        mSpriteBatch.begin();
        mBackgroundSprite.draw(mSpriteBatch);

        mGraph.mDrive.draw(mSpriteBatch);
        mGraph.mScrew.draw(mSpriteBatch);

        mTray.preDraw(mSpriteBatch);

        for (int i = 0; i < mGraph.mCogs.size(); i++) {
            Cog c = mGraph.mCogs.get(i);

            if (c.getIsFixed() == false) c.draw(mSpriteBatch);
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

        mLogic.mRollDownSprite.draw(mSpriteBatch);

        if (mLogic.mState == TurnStage.GameOver) {
            if (mLogic.mPlayerID == 0) {
                mPlayer1Wins.draw(mSpriteBatch);
            } else {
                mPlayer2Wins.draw(mSpriteBatch);
            }
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
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render() {
        if (!mStartupScreen) {
            update();
            draw();
        } else {
            drawStartupScreen();
        }
    }

    private void drawStartupScreen() {
        if (Gdx.input.isTouched()) {
            mStartupScreen = false;
        }

        mSpriteBatch.begin();
        mSplashSprite.draw(mSpriteBatch);
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
            break;
        case GameStart:
            // other stuff
            mLogic.newTurn();
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
        mMaskButtonPressed = false;
        float oldScrewAngle = mGraph.mScrew.mAngle;

        mGraph.evaluate();

        float newScrewAngle = mGraph.mScrew.mAngle;

        mLogic.mTotalDriveToScrew += oldScrewAngle - newScrewAngle;
        if (Math.abs(oldScrewAngle - newScrewAngle) > 0.1) {
            mRackSprite.translate((oldScrewAngle - newScrewAngle) * 0.2f, 0);
            mRackHolderSprite.translate((oldScrewAngle - newScrewAngle) * 0.2f,
                    0);
            SoundSystem.playWithDelay("Rack", 500);
        }

        mLogic.animationTick();
    }

    private void doMovingEvents() {

        if (mHoldingCog && !Gdx.input.isTouched()) {

            System.out.println("Dropping cog");

            mHeldCog.setMouseTracking(false);
            mHeldCog.fixToGrid();

            if (mTray.touchInside(InputHandler.getScreenX(),
                    InputHandler.getScreenY())) {
                mLogic.playerPlacedCog(false);
                mGraph.removeCog(mHeldCog);
                mTray.addCog(mHeldCog);
            } else if (mGraph.dropCog(mHeldCog) == false) {
                System.out.println("Dropping failed");

                if (mLogic.mCogWasFromBoard == false) {
                    mGraph.removeCog(mHeldCog);
                    mTray.addCog(mHeldCog);
                }

                mLogic.playerFailedToPlaceCog();
            } else {
                mLogic.playerPlacedCog(true);
            }

            System.out.println("");
            System.out.println("");

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
        int gridX = getGridX(InputHandler.getScreenX());
        int gridY = getGridY(InputHandler.getScreenY());

        if (!mHoldingCog && Gdx.input.isTouched()) {
            if (inputInMaskButton(InputHandler.getScreenX(),
                    InputHandler.getScreenY())) {
                toggleMaskMode();
            } else if (mTray.touchInside(InputHandler.getScreenX(),
                    InputHandler.getScreenY())) {

                System.out.println("Selecting cog");

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
            } else {
                mHeldCog = mGraph.touchOnCog(InputHandler.getScreenX(),
                        InputHandler.getScreenY());
                int x = InputHandler.getScreenX();
                int y = InputHandler.getScreenY();

                if (mHeldCog != null
                        && !mGridManager.touchInBlock(getGridX(x), getGridY(y),
                                mLogic.mPlayerID)) {
                    System.out.println("Picking up cog");

                    mHoldingCog = true;
                    mHeldCog.setMouseTracking(true);

                    System.out.println("");
                    System.out.println("");

                    mLogic.playerSelectedCog(mHeldCog, true);
                } else if (inputInGrid(InputHandler.getScreenX(), InputHandler.getScreenY())
                        && !inputInMaskButton(
                                InputHandler.getScreenX(), InputHandler.getScreenY())
                        && mMaskButtonPressed) {

                    toggleGridSquare(gridX, gridY);
                }
            }
        }
    }

    private void doGameOverEvents() {

        mLogic.endGameTick();

        if (mLogic.mAnimationFrame > 60 && Gdx.input.isTouched()) {
            mLogic.newGame();
            mRackSprite.setPosition(800 / 2 - mRackSprite.getWidth() / 2,
                    1280 - mRackSprite.getHeight());
            mRackHolderSprite.setPosition(
                    800 / 2 - mRackHolderSprite.getWidth() / 2,
                    1280 - mRackHolderSprite.getHeight());
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
        return y > 64 && y < 1280 - 64;
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
            System.out.println("triggering " + mMaskButtonPressed);
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
