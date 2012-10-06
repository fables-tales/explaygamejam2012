package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CogGraph {

	private HashMap<Cog, List<Cog>> mGraph = new HashMap<Cog, List<Cog>>();
	public Cog mDrive;
	public List<Cog> mCogs = new ArrayList<Cog>();
	private List<Cog> mPossibleConnetions = new ArrayList<Cog>();
	private Cog mScrew;

	public CogGraph() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
		mDrive = new Cog(new Sprite(ResourceManager.get("cogA")), 37);
		mDrive.promoteToDrive();     
		
		mDrive.setCenterX(w * 0.5f);
		mDrive.setCenterY(80);        
		mDrive.fixToGrid(); 

		mCogs.add(mDrive);

		mGraph.put(mDrive, new ArrayList<Cog>());
		
		mScrew = new Cog(new Sprite(ResourceManager.get("cogA")), 37);
		mScrew.promoteToScrew();     
		
		mScrew.setCenterX(w * 0.5f);
		mScrew.setCenterY(h);        
		mScrew.fixToGrid(); 

		mCogs.add(mScrew);
	}

	public Cog touchOnCog(int x, int y) {
		
		for (int i = 0; i < mCogs.size(); i++) {
			Cog other = mCogs.get(i);

			if (other.getIsFixed() == true)
				continue;
			
			if (other.isTouchOn(x, y) == true) {
				
				removeCog(other);
				
				mCogs.add(other);
				
				return other; 
			}
		}
		
		return null;
	}

	public boolean dropCog(Cog cog) {

		// this is possibly not the best way to do this?!
		mPossibleConnetions.clear();

		// ***********  find all connecting cogs *********** 

		for (int i = 0; i < mCogs.size(); i++) {
			Cog other = mCogs.get(i);

			if (other == cog)
				continue;

			if (cog.isPossibleOverlapping(other) == true) {
				mPossibleConnetions.add(other);
			}
		}

		// *********** find a suitable spot *********** 
		
		for (int i = 0; i < mPossibleConnetions.size(); i++) {
			Cog other = mPossibleConnetions.get(i);

			if (cog.isOverlapping(other) == true) {
				
				// THIS NEEDS BETTER(ING) 
				return false;
			}
		}

		// *********** refactor cog graph *********** 
		
		boolean isAttachedToDrive = false;

		// scan for cogs that have been visited
		for (int i = 0; i < mPossibleConnetions.size(); i++) {
			Cog other = mPossibleConnetions.get(i);

			if (other.mVisited == true) {
				isAttachedToDrive = true;
				add(other, cog);
			}
		}

		if (isAttachedToDrive == true) {

			for (int i = 0; i < mCogs.size(); i++) {
				mCogs.get(i).mBindingsReversed = false;
			}

			// scan for cogs that were not visited (not attached to drive) 
			for (int i = 0; i < mPossibleConnetions.size(); i++) {
				Cog other = mPossibleConnetions.get(i);

				if (other.mVisited == false) {
					reverseBindings(other);

					add(cog, other);
				}
			}
		} else {
			
			// we can safely link the cogs in anyway we like? 
			for (int i = 0; i < mPossibleConnetions.size(); i++) {
				Cog other = mPossibleConnetions.get(i);

				add(other, cog);
			}
		}

		return true;
	}

	private void reverseBindings(Cog cog) {

		cog.mBindingsReversed = true;

		// Check any forward bindings
		if (mGraph.containsKey(cog) == true) {

			List<Cog> list = mGraph.get(cog);

			for (int i = list.size() - 1; i >= 0; --i) {

				Cog other = list.get(i);

				if (other.mBindingsReversed == false) {
					
					remove(cog, other);
					add(other, cog);
				
					reverseBindings(other);
				}
			}
		}

		// check any backward bindings
		for (int i = cog.mConnections.size() - 1; i >= 0; --i) {

			Cog other = cog.mConnections.get(i);

			if (other.mBindingsReversed == false) {

				remove(other, cog);
				add(cog, other);

				reverseBindings(other);
			}
		}
	}

	public void addCog(Cog cog) {
		mCogs.add(cog);
	}

	public void removeCog(Cog cog) {

		// Check any forward bindings
		if (mGraph.containsKey(cog) == true) {

			List<Cog> list = mGraph.get(cog);

			for (int i = list.size() - 1; i >= 0; --i) {

				Cog other = list.get(i);

				remove(cog, other);
			}
		}

		// check any backward bindings
		for (int i = cog.mConnections.size() - 1; i >= 0; --i) {

			Cog other = cog.mConnections.get(i);

			if (other.mBindingsReversed == false) {

				remove(other, cog);				
			}
		}

		mCogs.remove(cog);
	}

	public boolean remove(Cog parent, Cog cog) {

		if (mGraph.containsKey(parent) == false) {
			// return false;
			return false;
		}

		List<Cog> list = mGraph.get(parent);

		if (list.contains(cog) == false) {
			return false;
		}

		// remove the back link 
		if (cog.mConnections.contains(parent) == true) {
			cog.mConnections.remove(parent);
		}

		list.remove(cog);

		return true;
	}

	public boolean add(Cog parent, Cog cog) {

		if (mGraph.containsKey(parent) == false) {
			// return false;
			mGraph.put(parent, new ArrayList<Cog>());
		}

		List<Cog> list = mGraph.get(parent);

		if (list.contains(cog) == true) {
			return false;
		}

		// we need a back link so we can refactor the graph 
		if (cog.mConnections.contains(parent) == false) {
			cog.mConnections.add(parent);
		}

		list.add(cog);

		return true;
	}

	public boolean evaluate() {

		for (int i = 0; i < mCogs.size(); i++) {
			mCogs.get(i).mVisited = false;
		}

		mDrive.rotate(false);

		if (propogate(mDrive, true) == true) {

			for (int i = 0; i < mCogs.size(); i++) {
				mCogs.get(i).applyRotation();
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean propogate(Cog node, boolean dir) {

		node.mVisited = true;

		boolean accum = true;

		if (mGraph.containsKey(node)) {
			List<Cog> list = mGraph.get(node);

			for (int i = 0; i < list.size(); i++) {
				Cog child = list.get(i);

				child.rotate(dir);

				accum &= !child.mVisited;

				if (child.mVisited == false) {
					accum &= propogate(child, !dir);
				}
			}
		}

		return accum;
	}

	public void renderDebugLines(ShapeRenderer shape) {

		for (int i = 0; i < mCogs.size(); i++) {
			mCogs.get(i).mVisited = false;
		}

		renderDebugLines_Sub(shape, mDrive, true);

		for (int i = 0; i < mCogs.size(); i++) {
			mCogs.get(i).mBindingsReversed = false;
		}

		for (int i = 0; i < mCogs.size(); i++) {
			if (mCogs.get(i).mVisited == false
					&& mCogs.get(i).mBindingsReversed == false) {

				renderDebugLines_Unconneted(shape, mCogs.get(i));
			}
		}
	}

	private boolean renderDebugLines_Sub(ShapeRenderer shape, Cog node,
			boolean dir) {
		node.mVisited = true;

		boolean accum = true;

		if (mGraph.containsKey(node)) {

			List<Cog> list = mGraph.get(node);

			for (int i = 0; i < list.size(); i++) {

				Cog child = list.get(i);

				if (dir == true)
					shape.setColor(0, 1, 0, 1);
				else
					shape.setColor(1, 0, 0, 1);

				shape.line(node.getCenterX(), node.getCenterY(),
						child.getCenterX(), child.getCenterY());

				accum &= !child.mVisited;

				if (child.mVisited == false) {
					accum &= renderDebugLines_Sub(shape, child, !dir);
				}
			}
		}

		return accum;
	}

	private void renderDebugLines_Unconneted(ShapeRenderer shape, Cog node) {
		node.mBindingsReversed = true;

		if (mGraph.containsKey(node)) {

			List<Cog> list = mGraph.get(node);

			for (int i = 0; i < list.size(); i++) {

				Cog child = list.get(i);

				shape.setColor(0, 0, 1, 1);

				shape.line(node.getCenterX(), node.getCenterY(),
						child.getCenterX(), child.getCenterY());

				if (child.mBindingsReversed == false) {
					renderDebugLines_Unconneted(shape, child);
				}
			}
		}
	}
}
