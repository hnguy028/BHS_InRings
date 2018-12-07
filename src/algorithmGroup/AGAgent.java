package algorithmGroup;

import java.util.ArrayList;
import java.util.HashMap;

import datastructures.Node;
import mobileAgents.Agent;

public class AGAgent extends Agent {
	private AgentGroup group;
	private int ringSize;
	private int rel_id;
	private int q;
	private int stage;
	
	private int distTraveled;
	
	private boolean finished;
	private boolean tbStart;
	private char tbChar;
	
	public AGAgent(int _id, int _rel_id, int groupSize, int _ringSize, AgentGroup _group, String _name, Node _node) {
		super(_id, _name, _node);
		
		rel_id = _rel_id; // i
		q = groupSize; // q
		ringSize = _ringSize;
		group = _group;
		
		stage = 0;
		distTraveled = 0;
		
		finished = false;
		tbStart = false;
	}
	
	public AGAgent(int _id, int _rel_id, int groupSize, int _ringSize, AgentGroup _group, String _name, Node _node, int minAsynch, int maxAsynch) {
		super(_id, _name, _node, minAsynch, maxAsynch);
		
		rel_id = _rel_id; // i
		q = groupSize; // q
		ringSize = _ringSize;
		group = _group;
		
		stage = 0;
		distTraveled = 0;
		
		finished = false;
		tbStart = false;
	}
	
	public boolean moveAndCheck() {
		if(finished) return false;
		return _move();
	}
	
	private boolean _move() {
		if(node == null && edge == null) { throw new NullPointerException("Node and Edge are both null"); }
		boolean atNode = (node != null);
		
		if(atNode && node.isBlackHole()) return false; // we're dead XD
		
		switch (group) {
		case LEFT:
			return handleLeftGroup(atNode); 
		case RIGHT:
			return handleRightGroup(atNode);
		case MIDDLE:
			return handleMiddleGroup(atNode);
		case TIEBREAKER:
			if(!tbStart && atNode) {
				HashMap<Integer,Character> tbWhiteBoard = ((AGNode) node).getTBWhiteBoard();
				
				tbStart = tbWhiteBoard.get(rel_id + 1) != null;
				if(tbStart) { tbChar = tbWhiteBoard.get(rel_id + 1); }
				
				return false;
			}
			return handleTieBreakerGroup(atNode);
		}
		
		return false;
	}
	
	private boolean handleLeftGroup(boolean atNode) {
		// Case i == 1 -> we dont move left
		if(stage == 0 && rel_id == 0) stage++; 
		
		switch (stage) {
		case 0:
			// Move left first
			move(atNode, true);
			
			if(node != null) {
				// If we've reached "i-1", we use i+1 here because of how the indexing is done
				if(distTraveled >= rel_id) {
					// reset distance traveled
					distTraveled = 0;
					
					// Switch to the next stage
					stage++;
				}
			} 
			return false;
		case 1:
			// Move right
			move(atNode, false);
			
			if(node != null) {
				// Note: the extra +1 is due to the indexing
				// An extra rel_id is counted because we need to move back to homebase first, then an additional rel_id + q as per the algorithm
				if(distTraveled >= (2*rel_id) + q) {
					// reset distance counter
					distTraveled = 0;
					
					// switch to the next step
					stage++;
				}
				
				// If we are Left_i+1, mark the whiteboard, for tiebreaker group to know they can start
				if(node.isHomebase()) {
					((AGNode) node).getTBWhiteBoard().put(rel_id, 'L');
				}
			}
			return false;
		default:
			// return to homebase
			move(atNode, true);
			
			// if we've reached the homebase
			if(node != null && node.isHomebase()) {
				
				// Check whiteboard to see if we pair with any other nodes that have returned to meet termination condition
				ArrayList<String> whiteBoard = ((AGNode) node).getWhiteBoard();
				for(String string : whiteBoard) {
					if(string.equals("M" + rel_id) || string.equals("T" + rel_id)) {
						// if such a pair exists return true to terminate
						whiteBoard.add("L" + rel_id);
						return true;
					}
				}
				
				// otherwise we write that we have completed to the whiteboard
				whiteBoard.add("L" + rel_id);
				finished = true;
			}
			
			return false;
		}
	}
	
	private boolean handleRightGroup(boolean atNode) {
		if(stage == 0 && rel_id == 0) stage++;
		switch (stage) {
		case 0:
			// Move right
			move(atNode, false);
			
			if(node != null) {
				// reached goal to begin moving left
				if(distTraveled >= rel_id) {
					distTraveled = 0;
					stage++;
				}
			}
			return false;
		case 1:
			// Move left
			move(atNode, true);
			
			if(node != null) {
				if(distTraveled >= (2*rel_id) + q) {
					distTraveled = 0;
					stage++;
				}
				
				// If we are Right_i+1, mark the whiteboard, for tiebreaker group to know they can start
				if(node.isHomebase()) {
					((AGNode) node).getTBWhiteBoard().put(rel_id, 'R');
				}
			}
			return false;
		default:
			// return to homebase
			move(atNode, false);
			
			if(node != null && node.isHomebase()) {
				ArrayList<String> whiteBoard = ((AGNode) node).getWhiteBoard();
				for(String string : whiteBoard) {
					// check termination condition
					if(string.equals("M" + (q - rel_id - 1)) || string.equals("T" + rel_id)) {
						whiteBoard.add("R" + rel_id);
						return true;
					}
				}
				// write that we've returned onto the whiteboard
				whiteBoard.add("R" + rel_id);
				finished = true;
			}
			return false;
		}
	}
	
	private boolean handleMiddleGroup(boolean atNode) {
		switch (stage) {
		case 0:
			// Move left first
			move(atNode, true);
			
			if(node != null) {
				// If we've reached "i+q-2", again we add 1 due to the indexing
				if(distTraveled >= rel_id + q - 1) {
					// reset distance traveled
					distTraveled = 0;
					
					// Switch to the next stage
					stage++;
				}
			} 
			return false;
		case 1:
			// Move right
			move(atNode, false);
			
			if(node != null) {
				// Note: the extra +1 is due to the indexing
				// An extra rel_id is counted because we need to move back to homebase first, then an additional rel_id + q as per the algorithm
				if(distTraveled >= (2*rel_id) + (3*q)) {
					// reset distance counter
					distTraveled = 0;
					
					// switch to the next step
					stage++;
				}
			}
			return false;
		default:
			// return to homebase
			move(atNode, true);
			
			// if we've reached the homebase
			if(node != null && node.isHomebase()) {
				
				// Check whiteboard to see if we pair with any other nodes that have returned to meet termination condition
				ArrayList<String> whiteBoard = ((AGNode) node).getWhiteBoard();
				for(String string : whiteBoard) {
					if(string.equals("R" + ((q-1) - rel_id - 1)) || string.equals("L" + rel_id)) {
						// if such a pair exists return true to terminate
						whiteBoard.add("M" + rel_id);
						return true;
					}
				}
				
				// otherwise we write that we have completed to the whiteboard
				whiteBoard.add("M" + rel_id);
				finished = true;
			}
			
			return false;
		}
	}
	
	private boolean handleTieBreakerGroup(boolean atNode) {
		// Check if we can start, ie Left_i+1 or Right_i+1 has passed the homebase
		if(!tbStart) return false;
		boolean left = tbChar == 'L';
		
		switch (stage) {
		case 0:
			// Move right first
			move(atNode, left);
			
			if(node != null) {
				// If we've reached "i-1", we use i+1 here because of how the indexing is done
				if(distTraveled >= ringSize - rel_id - 3) {
					// reset distance traveled
					distTraveled = 0;
					
					// Switch to the next stage
					stage++;
				}
			} 
			return false;
		default:
			// return to homebase
			move(atNode, !left);
			
			// if we've reached the homebase
			if(node != null && node.isHomebase()) {
				
				// Check whiteboard to see if we pair with any other nodes that have returned to meet termination condition
				ArrayList<String> whiteBoard = ((AGNode) node).getWhiteBoard();
				for(String string : whiteBoard) {
					if(string.equals("R" + rel_id) || string.equals("L" + rel_id)) {
						// if such a pair exists return true to terminate
						whiteBoard.add("T" + rel_id);
						return true;
					}
				}
				
				// otherwise we write that we have completed to the whiteboard
				whiteBoard.add("T" + rel_id);
				finished = true;
			}
			
			return false;
		}
	}

	
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
