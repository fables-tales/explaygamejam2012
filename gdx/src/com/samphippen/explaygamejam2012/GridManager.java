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
        s.setSize(width/SQUARES_PER_ROW, height/NUMBER_OF_ROWS);
        s.setColor(1, 1, 1, 0);
        return s;
    }

    public void toggle(int gridX, int gridY, int player) {
        
        if (mToggleDelay <= 0) {
            System.out.println("bees");
            mToggleDelay = 10;
            int gridIndex = gridX + gridY * SQUARES_PER_ROW;
            Sprite currentSprite = mGridSprites.get(gridIndex);
            int currentPlayer = mGridPlayer.get(gridIndex);
            if (currentPlayer == 0) {
                currentSprite.setColor(1, 1, 1, 1);
                mGridPlayer.set(gridIndex, player);
            } else if (currentPlayer == player) {
                currentSprite.setColor(1, 1, 1, 0);
                mGridPlayer.set(gridIndex, 0);
            }
        }

    }

    public void draw(SpriteBatch sb) {
        mToggleDelay--;
        for (int i = 0; i < mGridSprites.size(); i++) {
            Sprite gridSprite = mGridSprites.get(i);
            gridSprite.draw(sb);
        }
    }

}
