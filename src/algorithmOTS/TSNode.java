package algorithmOTS;

import java.util.ArrayList;

import datastructures.Node;

public class TSNode extends Node{
	private ArrayList<Integer> whiteBoard;
	private ExploreMode message;
	private int exploreSpace;
	
	public TSNode(String _id) {
		super(_id);
		setLeftEdge(null);
		setRightEdge(null);
		
		// Initially there is no message
		message = ExploreMode.NULL;
		
		// count of nodes explored by agent who leaves a message
		exploreSpace = 0;
	}
	
	public void setWhiteBoard(ArrayList<Integer> _whiteBoard) { whiteBoard = _whiteBoard; }
	
	@Override
	public ArrayList<Integer> getWhiteBoard() { return whiteBoard; }
	
	public ExploreMode checkOutMessage() { ExploreMode rtnMessage = message; message = ExploreMode.NULL; return rtnMessage; }
	public int checkOutCount() { int count = exploreSpace; exploreSpace = 0; return count; }
	
	public ExploreMode getMessage() { return message; }
	
	public void setMessage(ExploreMode _message, int _space) { message = _message; exploreSpace = _space; }
}
