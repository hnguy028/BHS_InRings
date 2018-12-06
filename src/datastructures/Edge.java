package datastructures;

import java.util.ArrayList;

import mobileAgents.Agent;

public class Edge {
	protected Node left;
	protected Node right;
	protected ArrayList<Agent> agentList;
	
	public Edge() {
		left = null;
		right = null;
		agentList = new ArrayList<Agent>();
	}
	
	public Edge(Node _left, Node _right) {
		this.left = _left;
		this.right = _right;
		this.agentList = new ArrayList<Agent>();
	}
	
	public ArrayList<Agent> getAgent() {
		return agentList;
	}
	
	public void setLeftNode(Node node) { left = node; }
	
	public Node getLeftNode() { return left; }
	
	public Node getRightNode() { return right; }
	
	public void setRightNode(Node node) { right = node; }
	
	public void putAgent(Agent agent) { agentList.add(agent); }
	
	public void removeAgent(Agent agent) { agentList.remove(agent); }
	
	public String getAgentString() {
		StringBuilder agentString = new StringBuilder("[");
		
		String prefix = "";
		for(Agent agent : agentList) {
			String string = prefix + agent.getName();
			agentString.append(string);
			prefix = ", ";
		}
		
		agentString.append("]");
		
		return agentString.toString();
	}
	
	public void printAgents() { System.out.print(getAgentString()); }
}
