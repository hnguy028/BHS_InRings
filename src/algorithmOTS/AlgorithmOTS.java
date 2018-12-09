package algorithmOTS;

import java.util.ArrayList;
import java.util.Random;

import datastructures.Ring;
import engine.GraphManager;
import mobileAgents.Agent;

public class AlgorithmOTS {
	private Ring graph;
	private Random rng = new Random();
	
	private int numAgents;
	
	private int homebaseIndex;
	private int blackHoleIndex;
	
	private ArrayList<Integer> homebaseWhiteBoard;
	
	private ArrayList<TSAgent> agentList; 
	
	private int graphSize;
	public AlgorithmOTS(int _n) throws Exception {
		agentList = new ArrayList<TSAgent>();
		graphSize = _n;
		
		numAgents = 2;
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmOptTeamSize", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = rng.nextInt(graphSize);
		blackHoleIndex = rng.nextInt(graphSize);
		
		homebaseIndex = 2;
		blackHoleIndex = 4;
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		TSNode homebase = (TSNode) graph.getNodeList().get(homebaseIndex);
		
		// Generate agents
		// LEFT
		agentList.add(new TSAgent(0, graphSize, Direction.LEFT, "Agent#0", homebase));
		
		// RIGHT
		agentList.add(new TSAgent(1, graphSize, Direction.RIGHT, "Agent#1", homebase));
				
		// Let the respective nodes know that they are blackhole and homebase (and set the agents at homebase)
		setHomebase(homebaseIndex);
		setBlackHole(blackHoleIndex);
				
		// Create and set global home base whiteboard
		homebaseWhiteBoard = new ArrayList<Integer>();
		((TSNode) graph.getNodeList().get(homebaseIndex)).setWhiteBoard(homebaseWhiteBoard);
				
		// Print initial state
		graph.print();
		System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString() + "  -- BH[" + blackHoleIndex + "]");
			
		// Start Algorithm Group
		startAlgorithmOTS();
	}
	
	public void startAlgorithmOTS() throws Exception {
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 190;
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			for(TSAgent agent : agentList) {
				if(agent.moveAndCheck()) {
					loop = false;
				}
			}
			
			graph.print();
			System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			//  + ":" + ((TSNode) graph.getNodeList().get(3)).getMessage().toString()
			
			loopBound--;
		}
		
		System.out.println("Black Hole is determined to be: " + homebaseWhiteBoard.get(0) + " node(s) to the left, " + homebaseWhiteBoard.get(1) +  " node(s) to the right, and at node id: " + homebaseWhiteBoard.get(2));
		System.out.println("Actual location of black hole is: " + blackHoleIndex);

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
