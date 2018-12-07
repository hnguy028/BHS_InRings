package algorithmOAT;

import java.util.ArrayList;

import mobileAgents.Agent;

public class ATAgent extends Agent{
	private AgentGroup group;
	private int ringSize;
	private int rel_id;
	
	// true if we are moving away from homebase
	private boolean FWD;
	
	private int distTraveled;
	
	private boolean finished;

	public ATAgent(Integer _id, Integer _rel_id, Integer _graphSize, AgentGroup _group, String _name, ATNode _node) {
		super(_id, _name, _node);
		rel_id = _rel_id;
		ringSize = _graphSize;
		group = _group;
		
		FWD = true;
		distTraveled = 0;
		finished = false;
	}
	
	public ATAgent(Integer _id, Integer _rel_id, Integer _graphSize, AgentGroup _group, String _name, ATNode _node, int minAsynch, int maxAsynch) {
		super(_id, _name, _node, minAsynch, maxAsynch);
		rel_id = _rel_id;
		ringSize = _graphSize;
		group = _group;
		
		FWD = true;
		distTraveled = 0;
		finished = false;
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
		}
		
		return false;
	}
	
	private boolean handleLeftGroup(boolean atNode) {
		if(rel_id == 0 && atNode) {
			((ATNode) node).getWhiteBoard().add(rel_id);
			finished = true;
			return false;
		}
		
		if(FWD) {
			// Move left first
			move(atNode, true);
			
			if(node != null) {
				// If we've reached node j
				if(distTraveled >= rel_id) {
					// reset distance traveled
					distTraveled = 0;
					
					// Switch to the next stage
					FWD = false;
				}
			} 
			return false;
		} else {
			// return to homebase
			move(atNode, false);
			
			// if we've reached the homebase
			if(node != null && node.isHomebase()) {
				
				// Check whiteboard to see if we pair with any other nodes that have returned to meet termination condition
				ArrayList<Integer> whiteBoard = ((ATNode) node).getWhiteBoard();
				
				if(whiteBoard.contains(rel_id)) {
					whiteBoard.add(rel_id);
					int distLeft = rel_id + 1;
					int distRight = (ringSize - 1) - rel_id;
					((ATNode) node).setTerminationData(new ArrayList<Integer>() {{
						add(distLeft);
						add(distRight);
						add((Integer.parseInt(node.getId().replaceAll("[\\D]", "")) + distRight) % (ringSize));
						}});
					
					return true;
				}
				
				// otherwise we write that we have completed to the whiteboard
				whiteBoard.add(rel_id);
				finished = true;
			}
			
			return false;
		}
	}
	
	private boolean handleRightGroup(boolean atNode) {
		if(rel_id == 0 && atNode) {
			((ATNode) node).getWhiteBoard().add((ringSize - 2) - rel_id);
			finished = true;
			return false;
		}
		
		if(FWD) {
			// Move right
			move(atNode, false);
			
			if(node != null) {
				// reached goal to begin moving left
				if(distTraveled >= rel_id) {
					distTraveled = 0;
					FWD = false;
				}
			}
			return false;
		} else {
			// return to homebase
			move(atNode, true);
			
			if(node != null && node.isHomebase()) {
				ArrayList<Integer> whiteBoard = ((ATNode) node).getWhiteBoard();
				
				// check termination condition

				// There -1 due to indexing, another -1 to exclude homebase 
				Integer offsetIndex = (ringSize - 2) - rel_id;
				
				if(whiteBoard.contains(offsetIndex)) {
					
					whiteBoard.add(offsetIndex);
					int distLeft = (ringSize - 1) - rel_id;
					int distRight = rel_id + 1;

					((ATNode) node).setTerminationData(new ArrayList<Integer>() {{
						add(distLeft); 
						add(distRight);
						add((Integer.parseInt(node.getId().replaceAll("[\\D]", "")) + distRight) % (ringSize));
						}});
					
					return true;
				}
				// write that we've returned onto the whiteboard
				whiteBoard.add(offsetIndex);
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
