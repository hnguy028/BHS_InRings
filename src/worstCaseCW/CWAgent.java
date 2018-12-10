package worstCaseCW;

import java.util.ArrayList;

import mobileAgents.Agent;

public class CWAgent extends Agent {
	private int distToTravel;
	private int distTraveled;
	private TDirection initDirection;
	private TDirection currDirection;
	
	private int ringSize;
	
	public CWAgent(Integer id, String name, Integer _graphSize, CWNode node, TDirection _direction) {
		super(id, name, node);
		initDirection = _direction;
		currDirection = _direction;
		distToTravel = 1;
		distTraveled = 0;
		ringSize = _graphSize;
	}
	
	public CWAgent(Integer id, String name, Integer _graphSize, CWNode node, TDirection _direction, int minAsynch, int maxAsynch) {
		super(id, name, node, minAsynch, maxAsynch);
		initDirection = _direction;
		currDirection = _direction;
		distToTravel = 1;
		distTraveled = 0;
		ringSize = _graphSize;
	}
	
	public TDirection getTravelDirection() { return initDirection; }
	
	@Override
	public void getNewWaitTime() {
		switch (((CWEdge) edge).getState()) {
			case ACTIVE:
				((CWEdge) edge).markSafe(name);
			case SAFE:
				waitCounter = 1;
				break;
			case UNEXPLORED:
				super.getNewWaitTime();
				((CWEdge) edge).setActive(name);
				break;
		}
		waitCounter = 1;
	}
	
	public boolean _move() throws Exception {
		if(node == null && edge == null) { throw new NullPointerException("Node and Edge are both null"); }
		
		if(node != null) {
			if(node.isBlackHole()) return false; // Do nothing, we are dead XD
				
			if(currDirection.equals(TDirection.LEFT)) edge = node.getLeftEdge();
			else edge = node.getRightEdge();
			
			// Leave this node
			node.removeAgent(this);
			
			edge.putAgent(this);
			
			node = null;
			
			getNewWaitTime();
			
		} else {
			waitCounter--;
			
			// wait time is up
			if(waitCounter <= 0) {
				
				// remove ourself from the edge's agentList
				edge.removeAgent(this);
				
				if(currDirection.equals(TDirection.LEFT)) node = edge.getLeftNode();
				else node = edge.getRightNode();
				
				node.putAgent(this);
				
				edge = null;
				
				// reached a new node, decrement distance tra
				distTraveled++;
				
				if(node.isHomebase()) {
					ArrayList<Integer> whiteboard = ((CWNode) node).getWhiteBoard();
					// Update whiteboard for distance explored for this agent
					whiteboard.set(id, distToTravel);
					
					currDirection = initDirection;
					
					// increment the distance to explore next
					 distToTravel++;
					 distTraveled = 0;
					 
					 if(whiteboard.size() == 2) {
						 if(whiteboard.get(0) + whiteboard.get(1) == ringSize - 2) {
							 System.out.println(whiteboard.toString());
							 return true;
						 }
					 }
				}
				
				// Reached destination node, turn around
				if(distToTravel == distTraveled) {
					currDirection = currDirection == TDirection.LEFT ? TDirection.RIGHT : TDirection.LEFT;
				}
			}
		}
		return false;
	} 
}
