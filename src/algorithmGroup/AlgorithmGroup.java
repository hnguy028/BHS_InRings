package algorithmGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import cautiousWalk.CWAgent;
import datastructures.Ring;
import engine.GraphManager;
import mobileAgents.Agent;

public class AlgorithmGroup {
	private Ring graph;
	private Random rng = new Random();
	
	private int numAgents;
	private int groupSize;
	
	private int homebaseIndex;
	private int blackHoleIndex;
	
	private ArrayList<Integer> homebaseWhiteBoard;
	
	private ArrayList<AGAgent> agentList; 
	
	private int graphSize;
	
	public AlgorithmGroup(int _n) {
		agentList = new ArrayList<AGAgent>();
		graphSize = _n;
		
		numAgents = _n - 1;
		groupSize = numAgents / 4;
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmGroup", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = rng.nextInt(graphSize);
		blackHoleIndex = rng.nextInt(graphSize);
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		AGNode homebase = (AGNode) graph.getNodeList().get(homebaseIndex);
		
		// Generate agents
		// LEFT
		int j = 0;
		for(int i = 0; i < groupSize; i++, j++) {
			agentList.add(new AGAgent(j, i, groupSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase));
		}
		
		// RIGHT
		for(int i = 0; i < groupSize; i++, j++) {
			agentList.add(new AGAgent(j, i, groupSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase));
		}
		
		// MIDDLE
		for(int i = 0; i < groupSize + 1; i++, j++) {
			agentList.add(new AGAgent(j, i, groupSize + 1, AgentGroup.MIDDLE, "Agent#" + j + ":M:" + i, homebase));
		}
		
		// TIEBREAKER
		for(int i = 0; i < groupSize - 1; i++, j++) {
			agentList.add(new AGAgent(j, i, groupSize - 1, AgentGroup.LEFT, "Agent#" + j + ":T:" + i, homebase));
		}
				
		// Let the respective nodes know that they are blackhole and homebase (and set the agents at homebase)
		setHomebase(homebaseIndex);
		setBlackHole(blackHoleIndex);
				
		// Create and set global home base whiteboard
		homebaseWhiteBoard = new ArrayList<Integer>(Collections.nCopies(agentList.size(), 0));
		((AGNode) graph.getNodeList().get(homebaseIndex)).setWhiteBoard(homebaseWhiteBoard);
				
		// Print initial state
		graph.print(); 
			
		// Start Algorithm Group
		startAlgorithmGroup();
	}
	
	public void startAlgorithmGroup() {
		System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString() + "  -- BH[" + blackHoleIndex + "]");
		
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 10;
				
		// int blackHoleOffset = 0;
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			
			// check termination condition
			
			loopBound--;
		}
	}
	
	/**
	 * 
	 */
	public void setHomebase(int index) {
		homebaseIndex = index;
		graph.getNodeList().get(index).setAsHomeBase();
		
		for(Agent agent : agentList) {
			graph.getNodeList().get(index).putAgent(agent);
		}
	}
	
	/**
	 * 
	 */
	public void setBlackHole(int index) {
		blackHoleIndex = index;
		graph.getNodeList().get(index).setAsBlackHole();
	}
}
