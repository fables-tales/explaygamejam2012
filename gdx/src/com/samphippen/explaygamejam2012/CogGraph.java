package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CogGraph {

	private HashMap<Cog, List<Cog>> mGraph = new HashMap<Cog, List<Cog>>();
	private Cog mDrive;
	public List<Cog> mCogs = new ArrayList<Cog>();
	
	public CogGraph(Cog drive) {
		
		mCogs.add(drive); 
		
		mGraph.put(drive, new ArrayList<Cog>());
		mDrive = drive; 
	}

	public boolean add(Cog parent, Cog cog) {
		
		if (mGraph.containsKey(parent) == false) { 
			return false; 
		}
		
		List<Cog> list = mGraph.get(parent); 
		
		if (list.contains(cog) == true) { 
			return false;
		}
				
		list.add(cog);
		
		return true; 
	}	
	
	public boolean evaluate() { 
		
		for (int i = 0; i < mCogs.size(); i++) { 
			mCogs.get(i).mVisited = false; 
		}
		
		return propogate(mDrive, true); 
	}
	
	private boolean propogate(Cog node, boolean dir) { 
		
		node.mVisited = true; 
		
		boolean accum = true; 
		
		List<Cog> list = mGraph.get(node);
		
		for (int i = 0; i < list.size(); i++) { 
			Cog child = list.get(i); 
			
			accum &= !child.mVisited;
			accum &= propogate(child, !dir);
			
			if (accum == false) { 
				return false; 
			}
		}
		
		return accum;
	}
}

