package datastructures;

import java.util.ArrayList;

import mobileAgents.Agent;

public class Node {
	private String id;
	
	private Edge leftEdge;
	private Edge rightEdge;
	
	private boolean isBlackHole;
	private boolean isHomebase;
	
	private ArrayList<Agent> agentList;
	
	public Node(String _id) {
		id = _id;
		leftEdge = null;
		rightEdge = null;
		agentList = new ArrayList<Agent>();
	}
	
	public String getId() { return id; }
	
	public void setLeftEdge(Edge edge) { leftEdge = edge; }
	
	public Edge getLeftEdge() { return leftEdge; }
	
	public void setRightEdge(Edge edge) { rightEdge = edge; }
	
	public Edge getRightEdge() { return rightEdge; }

	public void setAsHomeBase() { isHomebase = true; }
	
	public boolean isHomebase() { return isHomebase; }
	
	public void setAsBlackHole() { isBlackHole = true; }
	
	public boolean isBlackHole() { return isBlackHole; }
	
	public void putAgent(Agent agent) {
		agentList.add(agent);
	}
	
	public String getAgentString() {
		StringBuilder agentString = new StringBuilder("[");
		
		for(Agent agent : agentList) {
			String string = agent.getName() + ", ";
			agentString.append(string);
		}
		
		agentString.append("]");
		
		return agentString.toString();  
	}
	
	public void printAgents() { System.out.print(getAgentString()); }
	
	public String toSring() {
		StringBuilder string = new StringBuilder("(");
		
		string.append(id + ":" + getAgentString());
		
		string.append(")");
		
		return string.toString();
	}
}
