package algorithmOTS;

import java.util.ArrayList;

import datastructures.Node;

public class TSNode extends Node{
	ArrayList<Integer> whiteBoard;
	
	public TSNode(String _id) {
		super(_id);
		setLeftEdge(null);
		setRightEdge(null);
	}
	
	public void setWhiteBoard(ArrayList<Integer> _whiteBoard) { whiteBoard = _whiteBoard; }
	
	@Override
	public ArrayList<Integer> getWhiteBoard() { return whiteBoard; }
}
