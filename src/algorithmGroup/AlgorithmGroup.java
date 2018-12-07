package algorithmGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import datastructures.Ring;
import engine.GraphManager;
import mobileAgents.Agent;

public class AlgorithmGroup {
	private Ring graph;
	private Random rng = new Random();
	
	private int numAgents;
	private int q;
	
	private int homebaseIndex;
	private int blackHoleIndex;
	
	private ArrayList<String> homebaseWhiteBoard;
	private HashMap<Integer, Character> homebaseTBWhiteBoard;
	
	private ArrayList<AGAgent> agentList; 
	
	private int graphSize;
	
	public AlgorithmGroup(int _n) {
		agentList = new ArrayList<AGAgent>();
		graphSize = _n;
		
		numAgents = _n - 1;
		q = numAgents / 4;
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmGroup", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = rng.nextInt(graphSize);
		blackHoleIndex = rng.nextInt(graphSize);
		
//		homebaseIndex = 2;
//		blackHoleIndex = 4;
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		AGNode homebase = (AGNode) graph.getNodeList().get(homebaseIndex);
		
		// Generate agents
		// LEFT
		int j = 0;
		for(int i = 0; i < q; i++, j++) {
			agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase));
		}
		
		// RIGHT
		for(int i = 0; i < q; i++, j++) {
			agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase));
		}
		
		// MIDDLE
		for(int i = 0; i < q + 1; i++, j++) {
			agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.MIDDLE, "Agent#" + j + ":M:" + i, homebase));
		}
		
		// TIEBREAKER
		for(int i = 0; i < q - 1; i++, j++) {
			agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.TIEBREAKER, "Agent#" + j + ":T:" + i, homebase));
		}
				
		// Let the respective nodes know that they are blackhole and homebase (and set the agents at homebase)
		setHomebase(homebaseIndex);
		setBlackHole(blackHoleIndex);
				
		// Create and set global home base whiteboard
		homebaseWhiteBoard = new ArrayList<String>();
		((AGNode) graph.getNodeList().get(homebaseIndex)).setWhiteBoard(homebaseWhiteBoard);
		
		homebaseTBWhiteBoard = new HashMap<Integer,Character>();
		((AGNode) graph.getNodeList().get(homebaseIndex)).setTBWhiteBoard(homebaseTBWhiteBoard);
				
		// Print initial state
		graph.print(); 
		System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString() + "  -- BH[" + blackHoleIndex + "]");
			
		// Start Algorithm Group
		startAlgorithmGroup();
	}
	
	public void startAlgorithmGroup() {
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 100;
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			for(AGAgent agent : agentList) {
				if(agent.moveAndCheck()) {
					loop = false;
				}
			}
			
			graph.print();
			System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			
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
