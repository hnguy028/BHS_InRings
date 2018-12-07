package algorithmOAT;

import java.util.ArrayList;

import datastructures.Node;

public class ATNode extends Node {
	private ArrayList<Integer> whiteBoard;
	
	/*
	 * terminationData[
	 * 	distance to black hole from the Left
	 * 	distance to black hole from the Right
	 * 	calculated Id of black hole
	 * ]
	*/
	private ArrayList<Integer> terminationData;
	
	public ATNode(String _id) {
		super(_id);
		setLeftEdge(null);
		setRightEdge(null);
		terminationData = new ArrayList<>();
	}
	
	public ArrayList<Integer> getTerminationData() { return terminationData; }
	
	public void setTerminationData(ArrayList<Integer> data) { terminationData = data; }
	
	public void setWhiteBoard(ArrayList<Integer> _whiteBoard) { whiteBoard = _whiteBoard; }
	
	@Override
	public ArrayList<Integer> getWhiteBoard() { return whiteBoard; }
}
