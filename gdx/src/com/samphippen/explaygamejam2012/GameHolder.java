package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class GameHolder implements ApplicationListener {

    private SpriteBatch mSpriteBatch;
    private ShapeRenderer mDebugShapeRenderer;
    
    private Camera mCamera;
    private Vector2 mCameraOrigin = new Vector2(0, 0);    
    private Tray mTray;
    private boolean mHoldingCog = false;
    private Cog mHeldCog;
    private Cog mLastCog;
    private int mCogTime;
    private CogGraph mGraph;
	private boolean mDebugging = true;
    

    @Override
    public void create() {
    	ResourceManager.loadResources();   
       
    	mTray = new Tray();
        mGraph = new CogGraph(); 
        
        mSpriteBatch = new SpriteBatch();
        mSpriteBatch.enableBlending();         
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mCamera = new OrthographicCamera(w, h);
        mCameraOrigin.set(w / 2, h / 2);
                
        mDebugShapeRenderer = new ShapeRenderer(); 
        
        mLastCog = mGraph.mDrive;
        
        //createTestGraph(); 
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
        
        //mGraph.removeCog(mHeldCog); 
	}

	@Override
    public void dispose() {
    	ResourceManager.dispose(); 
    }

    private Vector2 getCameraOrigin() {
        return mCameraOrigin;
    }

    public void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Matrix4 traslate = new Matrix4().translate(-getCameraOrigin().x, -getCameraOrigin().y, 0); 
                
        mSpriteBatch.setProjectionMatrix(mCamera.combined);
        mSpriteBatch.setTransformMatrix(traslate);

        mSpriteBatch.begin();
        mTray.draw(mSpriteBatch);
        for (int i = 0; i < mGraph.mCogs.size(); i++) {
            Cog c = mGraph.mCogs.get(i);
            c.draw(mSpriteBatch);
        }

        mSpriteBatch.end();
        
        if (mDebugging  == true) {         	
        	mDebugShapeRenderer.setProjectionMatrix(mCamera.combined);
        	mDebugShapeRenderer.setTransformMatrix(traslate);
        	
        	mDebugShapeRenderer.begin(ShapeType.Line);
        	mDebugShapeRenderer.setColor(0, 1, 0, 1);
        	
        	mGraph.renderDebugLines(mDebugShapeRenderer); 
        	
        	mDebugShapeRenderer.end();
        }        
    }

    public void update() {
    	
    	mGraph.evaluate(); 
    	
        mCogTime += 1;
        if (!mHoldingCog && Gdx.input.isTouched()) {
            if (mTray.touchInside(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
            	System.out.println("Selecting cog");
            	mHoldingCog = true;
                mHeldCog = mTray.getCog();
                mGraph.addCog(mHeldCog);
                mHeldCog.setMouseTracking(true);
                mCogTime = 0;
            }
            else { 
            	mHeldCog = mGraph.touchOnCog(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()); 
            	
            	if (mHeldCog != null) {
            		System.out.println("Picking up cog");
            		mHoldingCog = true;
            		mHeldCog.setMouseTracking(true);
            	}            	
            }            	
        }

        if (mHoldingCog && !Gdx.input.isTouched()) {
            
        	System.out.println("Dropping cog");
        	
            mHeldCog.setMouseTracking(false);
            mHeldCog.fixToGrid();
            
            if (mGraph.dropCog(mHeldCog) == false) {
            	System.out.println("Dropping failed");
            	mGraph.mCogs.remove(mHeldCog); 
            	mTray.addCog(mHeldCog); 
            }
            
            mLastCog = mHeldCog;  
            mHeldCog = null;
            mHoldingCog = false;
        }

        for (int i = 0; i < mGraph.mCogs.size(); i++) {
            Cog c = mGraph.mCogs.get(i);
            c.update();
        }
    }

    @Override
    public void render() {
        update();
        draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
