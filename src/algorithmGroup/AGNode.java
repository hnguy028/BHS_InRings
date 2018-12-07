package algorithmGroup;

import java.util.ArrayList;
import java.util.HashMap;

import datastructures.Node;

public class AGNode extends Node{
	private ArrayList<String> whiteBoard;
	private HashMap<Integer, Character> whiteBoardTBNotes;
	
	public AGNode(String id) {
		super(id);
		setLeftEdge(null);
		setRightEdge(null);
	}
	
	public void setWhiteBoard(ArrayList<String> _whiteBoard) { whiteBoard = _whiteBoard; }
	
	public void setTBWhiteBoard(HashMap<Integer, Character> _whiteBoard) { whiteBoardTBNotes = _whiteBoard; }
	
	@Override
	public ArrayList<String> getWhiteBoard() { return whiteBoard; }
	
	public HashMap<Integer, Character> getTBWhiteBoard() { return whiteBoardTBNotes; }
}
