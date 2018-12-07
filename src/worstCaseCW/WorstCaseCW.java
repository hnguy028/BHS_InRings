package worstCaseCW;

import java.util.ArrayList;
import java.util.Random;

import datastructures.Ring;
import engine.GraphManager;
import mobileAgents.Agent;

public class WorstCaseCW {
	Ring graph;
	Random rng = new Random();
	
	static int numAgents = 2;
	int homebaseIndex;
	int blackHoleIndex;
	
	ArrayList<Integer> homebaseWhiteBoard;
	
	ArrayList<CWAgent> agentList; 
	
	private int graphSize;
	
	public WorstCaseCW(int _graphSize) throws Exception {
		agentList = new ArrayList<CWAgent>();
		graphSize = _graphSize;
		
		// Generate graph
		graph = new GraphManager().generateGraph("CautiousWalk", graphSize);
		
		// Generate blackhole and homebase index
		homebaseIndex = rng.nextInt(graphSize);
		blackHoleIndex = rng.nextInt(graphSize);
		
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
		
		// Generate agents
		agentList.add(new CWAgent(0, "Agent#" + 0,(CWNode) graph.getNodeList().get(homebaseIndex), TDirection.LEFT));
		agentList.add(new CWAgent(1, "Agent#" + 1,(CWNode) graph.getNodeList().get(homebaseIndex), TDirection.RIGHT));
		
		// Let the respective nodes know that they are blackhole and homebase (and set the agents at homebase)
		setHomebase(homebaseIndex);
		setBlackHole(blackHoleIndex);
		
		// Create and set global home base whiteboard
		homebaseWhiteBoard = new ArrayList<Integer>() {{ add(0); add(0); }}; 
		((CWNode) graph.getNodeList().get(homebaseIndex)).setWhiteBoard(homebaseWhiteBoard);
		
		// Print initial state
		graph.print(); 
		
		// Start CautiousWalk algorithm
		startCautiousWalk();
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void startCautiousWalk() throws Exception {
		System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString() + "  -- BH[" + blackHoleIndex + "]");
		
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 100000;
		
		int blackHoleOffset = 0;
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			for(CWAgent agent : agentList) {
				agent.move();
			}
			
			if(homebaseWhiteBoard.get(0) + homebaseWhiteBoard.get(1) == graphSize - 2) {
				loop = false;
			}
			
			graph.print();
			System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			
			
			loopBound--;
		}
		
		int blackHoleLocation = (homebaseWhiteBoard.get(1) + homebaseIndex + 1) % graphSize;
		System.out.println("Location of Black Hole -> Cautious Walk Aglorithm: " + blackHoleLocation + " vs Actual: " + blackHoleIndex);
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
