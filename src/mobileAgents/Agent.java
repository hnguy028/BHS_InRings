package mobileAgents;

import java.util.Random;

import datastructures.Edge;
import datastructures.Node;

public class Agent {
	protected int id;
	protected String name;
	protected Edge edge;
	protected Node node;
	protected int waitCounter;
	
	private int minWait = 1;
	private int maxWait = 3;

	public Agent(Integer _id, String _name, Node _node) {
		id = _id;
		name = _name;
		node = _node;
		edge = null;
		waitCounter = 0;
	}
	
	public Agent(Integer _id, String _name, Node _node, int minWaitTime, int maxWaitTime) {
		id = _id;
		name = _name;
		node = _node;
		edge = null;
		waitCounter = 0;
		minWait = minWaitTime;
		maxWait = maxWaitTime;
	}
	
	public int getId() { return id; }
	
	public void setNode(Node _node) {
		node = _node;
		edge = null;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setEdge(Edge _edge) {
		edge = _edge;
		node = null;
	}
	
	public Edge getEdge() {
		return edge;
	}
	
	public void setCounterTime(int time) { waitCounter = time; }
	
	public int getCounterTime() { return waitCounter; }
	
	public String getName() { return name; }
	
	protected void getNewWaitTime() {
		waitCounter =  new Random().nextInt(maxWait) + minWait;
	}
	
	/**
	 * @throws InstantiationException 
	 * 
	 */
	public void move() throws InstantiationException, Exception { throw new InstantiationException("Needs to be implmented"); }
}
