package algorithmOAT;

import datastructures.Node;
import mobileAgents.Agent;

public class ATAgent extends Agent{
	private int distTraveled;
	
	public ATAgent(Integer _id, String _name, Node _node) {
		super(_id, _name, _node);
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
