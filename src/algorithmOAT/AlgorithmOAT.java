package algorithmOAT;

import java.util.ArrayList;
import java.util.Random;

import datastructures.Ring;
import engine.GraphManager;
import mobileAgents.Agent;

public class AlgorithmOAT {
	
	private Ring graph;
	private Random rng = new Random();
	
	private int numAgents;
	
	private int homebaseIndex;
	private int blackHoleIndex;
	
	private ArrayList<Integer> homebaseWhiteBoard;
	
	private ArrayList<ATAgent> agentList; 
	
	private int graphSize;
	
	public AlgorithmOAT(int _n) {
		agentList = new ArrayList<ATAgent>();
		graphSize = _n;
		
		numAgents = 2 * (_n - 1);
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmOptAvgTime", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = rng.nextInt(graphSize);
		blackHoleIndex = rng.nextInt(graphSize);
		
//		homebaseIndex = 7;
//		blackHoleIndex = 0;
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		ATNode homebase = (ATNode) graph.getNodeList().get(homebaseIndex);
		
		// Generate agents
		// LEFT
		int j = 0;
		for(int i = 0; i < numAgents/2; i++, j++) {
			agentList.add(new ATAgent(j, i, graphSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase));
		}
		
		// RIGHT
		for(int i = 0; i < numAgents/2; i++, j++) {
			agentList.add(new ATAgent(j, i, graphSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase));
		}
				
		// Let the respective nodes know that they are blackhole and homebase (and set the agents at homebase)
		setHomebase(homebaseIndex);
		setBlackHole(blackHoleIndex);
				
		// Create and set global home base whiteboard
		homebaseWhiteBoard = new ArrayList<Integer>();
		((ATNode) graph.getNodeList().get(homebaseIndex)).setWhiteBoard(homebaseWhiteBoard);
				
		// Print initial state
		// graph.print();
		System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString() + "  -- BH[" + blackHoleIndex + "]");
			
		// Start Algorithm Group
		startAlgorithmOAT();
	}
	
	public void startAlgorithmOAT() {
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 100000000;
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			for(ATAgent agent : agentList) {
				if(agent.moveAndCheck()) {
					loop = false;
				}
			}
			
			// graph.print();
			// System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			
			loopBound--;
		}
		
		ATNode homebase = (ATNode) graph.getNodeList().get(homebaseIndex);
		ArrayList<Integer>termData = homebase.getTerminationData();
		System.out.println(termData.toString());
		
		
		System.out.println("Black Hole is determined to be: " + termData.get(0) + " node(s) to the left, " + termData.get(1) +  " node(s) to the right, and at node id: " + termData.get(2));
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
