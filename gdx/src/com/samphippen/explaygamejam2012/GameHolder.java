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
    private boolean mDebugging = true;
    private ShapeRenderer mDebugShapeRenderer;
    private CogGraph mGraph;
    private GridManager mGridManager;
    private Cog mHeldCog;
    private boolean mHoldingCog = false;
    private Cog mLastCog;
    private GameLogic mLogic = new GameLogic();
    private int mMaskButtonCountDown = 0;
    private boolean mMaskButtonPressed = false;
    private Sprite mMaskButtonSprite;
    private Sprite mRackSprite;
    private boolean mRunTurns = true;
    private SpriteBatch mSpriteBatch;
    private Tray mTray;

    @Override
    public void create() {
        ResourceManager.loadResources();
        mTray = new Tray();
        mGraph = new CogGraph();

        Texture t = new Texture(Gdx.files.internal("tray.png"));
        mRackSprite = new Sprite(t);
        mRackSprite.setPosition(0, 1280 - mRackSprite.getHeight());

        mMaskButtonSprite = new Sprite(ResourceManager.get("maskbutton"));

        mGridManager = new GridManager();
        mSpriteBatch = new SpriteBatch();
        mSpriteBatch.enableBlending();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mCamera = new OrthographicCamera(800, 1280);
        mCameraOrigin.set(400, 1280 / 2);

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Matrix4 traslate = new Matrix4().translate(-getCameraOrigin().x,
                -getCameraOrigin().y, 0);

        mSpriteBatch.setProjectionMatrix(mCamera.combined);
        mSpriteBatch.setTransformMatrix(traslate);

        mSpriteBatch.begin();

        mTray.draw(mSpriteBatch);
        for (int i = 0; i < mGraph.mCogs.size(); i++) {
            Cog c = mGraph.mCogs.get(i);
            c.draw(mSpriteBatch);
        }

        mRackSprite.draw(mSpriteBatch);
        mGridManager.draw(mSpriteBatch);
        mMaskButtonSprite.draw(mSpriteBatch);

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
        update();
        draw();
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
        case NextPlayer:
            switchPlayerView();
            break;
        case GameOver:
            // other stuff
            mLogic.newGame();
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

    private void createTestGraph() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Cog cog1 = mTray.getCog();
        cog1.setCenterX(w * 0.5f);
        cog1.setCenterY(h - 80);

        Cog cog2 = mTray.getCog();
        cog2.setCenterX(w * 0.5f);
        cog2.setCenterY(h - 160);

        mGraph.addCog(cog1);
        mGraph.addCog(cog2);

        mGraph.add(cog1, cog2);

        mGraph.evaluate();

        mHeldCog = mTray.getCog();
        mGraph.addCog(mHeldCog);

        mHeldCog.setCenterX(w * 0.5f);
        mHeldCog.setCenterY(h - 240);

        mHeldCog.fixToGrid();
        if (mGraph.dropCog(mHeldCog) == false) {
            mGraph.removeCog(mHeldCog);
            mTray.addCog(mHeldCog);
        }

        // mGraph.removeCog(mHeldCog);
    }

    private void createTestGraph2() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Cog cog1 = mTray.getCog();
        cog1.setCenterX(w * 0.5f);
        cog1.setCenterY(h - 64);

        Cog cog2 = mTray.getCog();
        cog2.setCenterX(w * 0.5f);
        cog2.setCenterY(h - 128);

        Cog cog3 = mTray.getCog();
        cog3.setCenterX(w * 0.5f);
        cog3.setCenterY(h - 192);

        Cog cog4 = mTray.getCog();
        cog4.setCenterX(w * 0.5f);
        cog4.setCenterY(h - 256);

        mGraph.addCog(cog1);
        mGraph.addCog(cog2);
        mGraph.addCog(cog3);
        mGraph.addCog(cog4);

        mGraph.add(cog1, cog2);
        mGraph.add(cog2, cog3);
        mGraph.add(cog3, cog4);

        mGraph.evaluate();

        mHeldCog = mTray.getCog();
        mGraph.addCog(mHeldCog);

        mHeldCog.setCenterX(w * 0.5f);
        mHeldCog.setCenterY(h - 320);

        mHeldCog.fixToGrid();
        if (mGraph.dropCog(mHeldCog) == false) {
            mGraph.removeCog(mHeldCog);
            mTray.addCog(mHeldCog);
        }

        mGraph.removeCog(mHeldCog);

        if (mGraph.dropCog(mHeldCog) == false) {
            mGraph.removeCog(mHeldCog);
            mTray.addCog(mHeldCog);
        }

        // mGraph.removeCog(cog2);
    }

    private void doAnimation() {
        mLogic.animationTick();
        mGraph.evaluate();
    }

    private void doMovingEvents() {

        if (mHoldingCog && !Gdx.input.isTouched()) {

            System.out.println("Dropping cog");

            mHeldCog.setMouseTracking(false);
            mHeldCog.fixToGrid();

            if (mGraph.dropCog(mHeldCog) == false) {
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
        }
    }

    private void doSelectingEvents() {
        int gridX = getGridX(Gdx.input.getX() * 2);
        int gridY = getGridY((Gdx.graphics.getHeight() - Gdx.input.getY()) * 2);

        if (!mHoldingCog && Gdx.input.isTouched()) {
            if (inputInMaskButton(Gdx.input.getX() * 2,
                    (Gdx.graphics.getHeight() - Gdx.input.getY()) * 2)) {
                toggleMaskMode();
            } else if (mTray.touchInside(Gdx.input.getX() * 2,
                    (Gdx.graphics.getHeight() - Gdx.input.getY()) * 2)
                    && inputInMaskButton(Gdx.input.getX() * 2,
                            (Gdx.graphics.getHeight() - Gdx.input.getY()) * 2)) {

                System.out.println("Selecting cog");

                mHoldingCog = true;
                mHeldCog = mTray.getCog();
                mGraph.addCog(mHeldCog);
                mHeldCog.setMouseTracking(true);
                mCogTime = 0;

                mLogic.playerSelectedCog(mHeldCog, false);
            } else {
                mHeldCog = mGraph.touchOnCog(Gdx.input.getX() * 2,
                        (Gdx.graphics.getHeight() - Gdx.input.getY()) * 2);

                if (mHeldCog != null) {
                    System.out.println("Picking up cog");

                    mHoldingCog = true;
                    mHeldCog.setMouseTracking(true);

                    System.out.println("");
                    System.out.println("");

                    mLogic.playerSelectedCog(mHeldCog, true);
                } else if (inputInGrid(Gdx.input.getX() * 2,
                        (Gdx.graphics.getHeight() - Gdx.input.getY()) * 2)
                        && !inputInMaskButton(
                                Gdx.input.getX() * 2,
                                (Gdx.graphics.getHeight() - Gdx.input.getY()) * 2)
                        && mMaskButtonPressed) {

                    toggleGridSquare(gridX, gridY);
                }
            }
        }
    }

    private Vector2 getCameraOrigin() {
        return mCameraOrigin;
    }

    private int getGridX(int x) {
        return x / (800 / GridManager.SQUARES_PER_ROW);
    }

    private int getGridY(int y) {
        // TODO Auto-generated method stub
        return y / (1280 / GridManager.NUMBER_OF_ROWS);
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
                mGridManager.hideCandidateSquares();
            }
        }
    }

}
