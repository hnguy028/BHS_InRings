package datastructures;

import java.util.ArrayList;
import java.util.Collection;

import mobileAgents.Agent;

public class Node {
	protected String id;
	
	protected Edge leftEdge;
	protected Edge rightEdge;
	
	protected boolean isBlackHole;
	protected boolean isHomebase;
	
	protected ArrayList<Agent> agentList;
	
	protected Object whiteBoard;
	
	public Node(String _id) {
		id = _id;
		leftEdge = null;
		rightEdge = null;
		agentList = new ArrayList<Agent>();
	}
	
	public String getId() { return id; }
	
	public Object getWhiteBoard() throws InstantiationException { throw new InstantiationException("Needs to be implmented"); }
	
	public void setLeftEdge(Edge edge) { leftEdge = edge; }
	
	public Edge getLeftEdge() { return leftEdge; }
	
	public void setRightEdge(Edge edge) { rightEdge = edge; }
	
	public Edge getRightEdge() { return rightEdge; }

	public void setAsHomeBase() { isHomebase = true; }
	
	public ArrayList<Agent> getAgentList() { return agentList; }
	
	public boolean isHomebase() { return isHomebase; }
	
	public void setAsBlackHole() { isBlackHole = true; }
	
	public boolean isBlackHole() { return isBlackHole; }
	
	public void putAgent(Agent agent) {
		agentList.add(agent);
	}
	
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
	
	public String toSring() {
		StringBuilder string = new StringBuilder("(");
		
		string.append(id + ":" + getAgentString());
		
		string.append(")");
		
		return string.toString();
	}
}
