package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GridManager {

    public static final int SQUARES_PER_ROW = 3;
    public static final int NUMBER_OF_ROWS = 6;

    private List<Sprite> mGridSprites = new ArrayList<Sprite>();
    private List<Integer> mGridPlayer = new ArrayList<Integer>();
    private int mToggleDelay = 10;

    private int mTouchedSquares = 0;
    private int mSquareTouchX;
    private int mSquareTouchY;

    public GridManager() {
        for (int y = 0; y < NUMBER_OF_ROWS; y++) {
            for (int x = 0; x < SQUARES_PER_ROW; x++) {
                mGridSprites.add(this.makeSprite(x, y));
                mGridPlayer.add(0);
            }
        }
    }

    private Sprite makeSprite(int x, int y) {
        Texture t = new Texture(Gdx.files.internal("block.png"));
        Sprite s = new Sprite(t);
        int height = 1280 - 64 * 2;
        int yOffset = 64;
        int width = 800;
        s.setPosition(width / SQUARES_PER_ROW * x, height / NUMBER_OF_ROWS * y
                + yOffset);
        s.setSize(width / SQUARES_PER_ROW, height / NUMBER_OF_ROWS);
        s.setColor(1, 1, 1, 0);
        return s;
    }

    public void receiveTouch(int gridX, int gridY, int player) {
        if (mToggleDelay <= 0) {
            mToggleDelay = 10;
            if (mTouchedSquares == 0) {
                mSquareTouchX = gridX;
                mSquareTouchY = gridY;
                mTouchedSquares++;
                this.showCandidateSquares();
            } else if (mTouchedSquares == 1) {
                if (this.isCandidateSquare(gridX, gridY)) {
                    this.block(gridX, gridY, player);
                    
                } else {
                    this.hideCandidateSquares(player);
                }
            }
        }
    }

    public void hideCandidateSquares(int currentPlayerID) {
        int gridIndex;
        mTouchedSquares = 0;
        for (int x = 0; x < SQUARES_PER_ROW; x++) {
            int y = mSquareTouchY;
            gridIndex = x + y * SQUARES_PER_ROW;
            int gridPlayer = mGridPlayer.get(gridIndex);
            if (gridPlayer == currentPlayerID) {
                mGridSprites.get(gridIndex).setColor(1, 1, 1, 0.0f);
            }
        }

        for (int y = 0; y < NUMBER_OF_ROWS; y++) {
            int x = mSquareTouchX;
            gridIndex = x + y * SQUARES_PER_ROW;
            int gridPlayer = mGridPlayer.get(gridIndex);
            if (gridPlayer == currentPlayerID) {
                mGridSprites.get(gridIndex).setColor(1, 1, 1, 0.0f);
            }
            
        }
        
    }

    private void block(int gridX, int gridY, int currentPlayer) {
        assert isCandidateSquare(gridX, gridY);
        this.hideCandidateSquares(currentPlayer);
        if (gridY == mSquareTouchY) {
            this.blockRow(currentPlayer);
        } else if (gridX == mSquareTouchX) {
            this.blockColumn(currentPlayer);
        }
        
        GameLogic.sInstance.placedGrid();
    }

    private void blockColumn(int currentPlayer) {
        // TODO Auto-generated method stub

        int x = mSquareTouchX;
        for (int y = 0; y < NUMBER_OF_ROWS; y++) {
            int gridIndex = x + y * SQUARES_PER_ROW;
            mGridSprites.get(gridIndex).setColor(1, 1, 1, 1f);
            mGridPlayer.set(gridIndex, currentPlayer);
        }
    }

    private void blockRow(int currentPlayer) {
        int y = mSquareTouchY;
        for (int x = 0; x < SQUARES_PER_ROW; x++) {
            int gridIndex = x + y * SQUARES_PER_ROW;
            mGridSprites.get(gridIndex).setColor(1, 1, 1, 1f);
            mGridPlayer.set(gridIndex, currentPlayer);
        }
    }

    private boolean isCandidateSquare(int gridX, int gridY) {
        return gridX == mSquareTouchX || gridY == mSquareTouchY;
    }

    private void showCandidateSquares() {
        int gridIndex = mSquareTouchX + mSquareTouchY * SQUARES_PER_ROW;

        for (int x = 0; x < SQUARES_PER_ROW; x++) {
            int y = mSquareTouchY;
            gridIndex = x + y * SQUARES_PER_ROW;
            mGridSprites.get(gridIndex).setColor(1, 1, 1, 0.5f);
        }

        for (int y = 0; y < NUMBER_OF_ROWS; y++) {
            int x = mSquareTouchX;
            gridIndex = x + y * SQUARES_PER_ROW;
            mGridSprites.get(gridIndex).setColor(1, 1, 1, 0.5f);
        }
        gridIndex = mSquareTouchX + mSquareTouchY * SQUARES_PER_ROW;
        mGridSprites.get(gridIndex).setColor(1, 1, 1, 0.9f);
    }

    public void draw(SpriteBatch sb) {
        mToggleDelay--;
        for (int i = 0; i < mGridSprites.size(); i++) {
            Sprite gridSprite = mGridSprites.get(i);
            gridSprite.draw(sb);
        }
    }

    public void clearPlayer(int mPlayerID) {
        System.out.println(mPlayerID);
        this.hideCandidateSquares(mPlayerID);
        for (int y = 0; y < NUMBER_OF_ROWS; y++) {
            for (int x = 0; x < SQUARES_PER_ROW; x++) {
                int gridIndex = x + y * SQUARES_PER_ROW;
                int currentPlayer = mGridPlayer.get(gridIndex);
                if (currentPlayer == mPlayerID) {
                    mGridPlayer.set(gridIndex, 0);
                    mGridSprites.get(gridIndex).setColor(1, 1, 1, 0);
                }
            }
        }
        
    }

    public void resetCountdown() {
        mToggleDelay = 10;
    }

}
