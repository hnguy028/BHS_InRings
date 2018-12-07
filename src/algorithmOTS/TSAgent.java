package algorithmOTS;

import java.util.ArrayList;

import datastructures.EdgeState;
import mobileAgents.Agent;

public class TSAgent extends Agent {
	
	private int ringSize;
	
	private ExploreMode mode;
	
	private AgentState state;
	private AgentType type;
	
	private int distTraveled;
	
	private boolean finished;
	
	public TSAgent(Integer _id, Integer _graphSize, AgentType _type, String _name, TSNode _node) {
		super(_id, _name, _node);
		
		ringSize = _graphSize;

		type = _type;
		state = AgentState.EXPLORE;
				
		distTraveled = 0;
		finished = false;
	}
	
	public TSAgent(Integer _id, Integer _graphSize, AgentType _type, String _name, TSNode _node, int minAsynch, int maxAsynch) {
		super(_id, _name, _node);
		
		ringSize = _graphSize;

		type = _type;
		state = AgentState.EXPLORE;
		
		distTraveled = 0;
		finished = false;
	}
	
	public boolean moveAndCheck() throws Exception {
		if(finished) return false;
		return _move();
	}
	
	private boolean _move() throws Exception {
		if(node == null && edge == null) { throw new NullPointerException("Node and Edge are both null"); }
		boolean atNode = (node != null);
		
		if(atNode && node.isBlackHole()) return false; // we're dead XD
		
		switch (type) {
		case LEFT:
			return handleLeft(atNode); 
		case RIGHT:
			//return handleRight(atNode);
		}
		
		return false;
	}
	
	private boolean handleLeft(boolean atNode) throws Exception {
		if(atNode) {
			// check prev edge state
			TSEdge lEdge = (TSEdge) node.getLeftEdge();
			TSEdge rEdge = (TSEdge) node.getRightEdge();
			
			if(rEdge.getState() == EdgeState.SAFE) {
				if(lEdge.getState() == EdgeState.ACTIVE) {
					// mark edge as safe? then move left?
				} else if(lEdge.getState() == EdgeState.UNEXPLORED) {
					// move left
				}
			} else if (rEdge.getState() == EdgeState.ACTIVE && lEdge.getState() == EdgeState.UNEXPLORED) {
				// move right
			} else {
				throw new Exception("Major Error, invalid state");
			}

		}
		
//		if(FWD) {
//			// Move left first
//			move(atNode, true);
//			
//			if(node != null) {
//				// If we've reached node j
//				if(distTraveled >= rel_id) {
//					// reset distance traveled
//					distTraveled = 0;
//					
//					// Switch to the next stage
//					FWD = false;
//				}
//			} 
//			return false;
//		} else {
//			// return to homebase
//			move(atNode, false);
//			
//			// if we've reached the homebase
//			if(node != null && node.isHomebase()) {
//				
//				// Check whiteboard to see if we pair with any other nodes that have returned to meet termination condition
//				ArrayList<Integer> whiteBoard = ((ATNode) node).getWhiteBoard();
//				
//				if(whiteBoard.contains(rel_id)) {
//					whiteBoard.add(rel_id);
//					int distLeft = rel_id + 1;
//					int distRight = (ringSize - 1) - rel_id;
//					((ATNode) node).setTerminationData(new ArrayList<Integer>() {{
//						add(distLeft);
//						add(distRight);
//						add((Integer.parseInt(node.getId().replaceAll("[\\D]", "")) + distRight) % (ringSize));
//						}});
//					
//					return true;
//				}
//				
//				// otherwise we write that we have completed to the whiteboard
//				whiteBoard.add(rel_id);
//				finished = true;
//			}
//			
//			return false;
//		}
		return false;
	}
	
//	private boolean handleRight(boolean atNode) {
//		
//		if(FWD) {
//			// Move right
//			move(atNode, false);
//			
//			if(node != null) {
//				// reached goal to begin moving left
//				if(distTraveled >= rel_id) {
//					distTraveled = 0;
//					FWD = false;
//				}
//			}
//			return false;
//		} else {
//			// return to homebase
//			move(atNode, true);
//			
//			if(node != null && node.isHomebase()) {
//				ArrayList<Integer> whiteBoard = ((ATNode) node).getWhiteBoard();
//				
//				// check termination condition
//
//				// There -1 due to indexing, another -1 to exclude homebase 
//				Integer offsetIndex = (ringSize - 2) - rel_id;
//				
//				if(whiteBoard.contains(offsetIndex)) {
//					
//					whiteBoard.add(offsetIndex);
//					int distLeft = (ringSize - 1) - rel_id;
//					int distRight = rel_id + 1;
//
//					((ATNode) node).setTerminationData(new ArrayList<Integer>() {{
//						add(distLeft); 
//						add(distRight);
//						add((Integer.parseInt(node.getId().replaceAll("[\\D]", "")) + distRight) % (ringSize));
//						}});
//					
//					return true;
//				}
//				// write that we've returned onto the whiteboard
//				whiteBoard.add(offsetIndex);
//				finished = true;
//			}
//			return false;
//		}
//	}
	
	private void move(boolean atNode, boolean left) {
		
		if(atNode) {
			edge = left ? node.getLeftEdge() : node.getRightEdge();
			edge.putAgent(this);
			
			// remove self from node's agent list
			node.removeAgent(this);
			node = null;
			
			getNewWaitTime();
		} else {
			waitCounter--;
			
			if(waitCounter <= 0) {
				node = left ? edge.getLeftNode() : edge.getRightNode();
				node.putAgent(this);
				
				edge.removeAgent(this);
				edge = null;
				
				distTraveled++;
			}
		}
	}
}
