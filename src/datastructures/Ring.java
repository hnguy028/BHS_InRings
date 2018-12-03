package datastructures;

import java.util.ArrayList;

public class Ring extends Graph {
	private int size;
	private ArrayList<Node> nodeList;
	private ArrayList<Edge> edgeList;
	
	public Ring() {
		size = 0;
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
	}
	
	public void addNewNode(Node node, Edge edge) {
		if(size == 0) {
			node.setLeftEdge(edge);
			node.setRightEdge(edge);
			
			edge.setLeftNode(node);
			edge.setRightNode(node);
			
			nodeList.add(node);
			edgeList.add(edge);
			size++;
			
			return;
		}
		// Update the last edge to point to the new node
		Edge lastEdge = edgeList.get(edgeList.size()-1);
		lastEdge.setRightNode(node);
		
		node.setLeftEdge(lastEdge);
		node.setRightEdge(edge);
		
		// update new edge to link the last node and the first node to make it a ring
		edge.setLeftNode(node);
		edge.setRightNode(nodeList.get(0));
		
		nodeList.get(0).setLeftEdge(edge);
		
		// add new edge and list to their respective lists
		nodeList.add(node);
		edgeList.add(edge);
		
		// increment size
		size++;
	}
	
	public ArrayList<Edge> getEdgeList() { return edgeList; }
	
	public ArrayList<Node> getNodeList() { return nodeList; }
	
	public int getSize() { return size; }
	
	public String toString() {
		StringBuilder ring = new StringBuilder();
		for(int i = 0; i < size; i++) {
			String string = nodeList.get(i).toSring() + " - " + edgeList.get(i).getAgentString() + " - ";
			ring.append(string);
		}
		
		ring.append(nodeList.get(0).toSring());
		
		return ring.toString(); 
	}
	
	public void print() { System.out.println(toString()); }
	
	
}
