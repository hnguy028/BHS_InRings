package worstCaseCW;

import java.util.ArrayList;

import datastructures.Node;

public class CWNode extends Node {
	private ArrayList<Integer> whiteBoard;
	
	public CWNode(String _id) {
		super(_id);
		setLeftEdge(null);
		setRightEdge(null);
	}
	
	public void setWhiteBoard(ArrayList<Integer> _whiteBoard) { whiteBoard = _whiteBoard; }
	
	@Override
	public ArrayList<Integer> getWhiteBoard() { return whiteBoard; }
}
