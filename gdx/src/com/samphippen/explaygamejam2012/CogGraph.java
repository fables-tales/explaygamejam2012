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
	public Cog mScrew;
	
	public List<Cog> mCogs = new ArrayList<Cog>();
	private List<Cog> mPossibleConnetions = new ArrayList<Cog>();	

	public CogGraph() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
		mDrive = Cog.getCog();
		mDrive.promoteToDrive();     
		
		mDrive.setCenterX(w * 0.5f);
		mDrive.setCenterY(80);        
		mDrive.fixToGrid(); 

		mCogs.add(mDrive);

		mGraph.put(mDrive, new ArrayList<Cog>());
		
		mScrew = Cog.getCog();
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
				
				System.out.println("Touch On " + other.CogID);
				
				removeCog(other);
				
				mCogs.add(other);
				
				refactorForward(); 
				
				return other; 
			}
		}
		
		return null;
	}

	public boolean dropCog(Cog cog) {

		System.out.println("Drop Cog " + cog.CogID);
		
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

				if (other.mVisited == false && other.mBindingsReversed == false) {
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
		
		refactorForward(); 

		return true;
	}

	private void reverseBindings(Cog cog) {

		System.out.println("Reverse Bindings " + cog.CogID);
		
		cog.mBindingsReversed = true;

		// Check any forward bindings
		if (mGraph.containsKey(cog) == true) {

			List<Cog> list = mGraph.get(cog);

			for (int i = list.size() - 1; i >= 0; --i) {

				Cog other = list.get(i);

				if (other.mBindingsReversed == false) {
					
					remove(cog, other);
					
					reverseBindings(other);
					
					add(other, cog);			
				}
			}
		}

		// check any backward bindings
		for (int i = cog.mConnections.size() - 1; i >= 0; --i) {

			Cog other = cog.mConnections.get(i);

			if (other.mBindingsReversed == false) {

				remove(other, cog);
				
				reverseBindings(other);
				
				add(cog, other);				
			}
		}
	}

	public boolean refactorForward() {

		for (int i = 0; i < mCogs.size(); i++) {
			mCogs.get(i).mBindingsReversed = false;
		}

		if (refactorForward_Sub(mDrive) == true) {
			
			return true;
		} else {
			return false;
		}
	}

	private boolean refactorForward_Sub(Cog node) {

		node.mBindingsReversed = true;

		if (mGraph.containsKey(node)) {
			List<Cog> list = mGraph.get(node);

			for (int i = 0; i < list.size(); i++) {
				Cog child = list.get(i);

				if (child.mBindingsReversed == false) {
					refactorForward_Sub(child);
				}
			}
		}
		
		// check any backward bindings
		for (int i = node.mConnections.size() - 1; i >= 0; --i) {

			Cog other = node.mConnections.get(i);

			if (other.mBindingsReversed == false) {

				remove(other, node);
				
				reverseBindings(other);
				
				add(node, other);				
			}
		}

		return true;
	}

	public void addCog(Cog cog) {
		
		System.out.println("Add Cog " + cog.CogID);
		
		mCogs.add(cog);
	}

	public void removeCog(Cog cog) {

		System.out.println("Remove Cog " + cog.CogID);
		
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

			remove(other, cog);				
		}

		mCogs.remove(cog);
	}
	
	public void clear() {
		
		for (int i = mCogs.size() - 1; i >= 0; --i) {
			
			Cog cog = mCogs.get(i); 
			
			if (cog.getIsFixed() == false){ 
				removeCog(cog); 
			}
		}

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

		System.out.println("Remove " + cog.CogID + " from " + parent.CogID);
		
		// remove the back link 
		if (cog.mConnections.contains(parent) == true) {
			cog.mConnections.remove(parent);
		}

		list.remove(cog);

		return true;
	}

	public boolean add(Cog parent, Cog cog) {

		if (mGraph.containsKey(parent) == false) {
			
			System.out.println("Add Node " + parent.CogID);
			
			mGraph.put(parent, new ArrayList<Cog>());
		}

		List<Cog> list = mGraph.get(parent);

		if (list.contains(cog) == true) {
			return false;
		}

		System.out.println("Add " + cog.CogID + " to " + parent.CogID);
		
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
