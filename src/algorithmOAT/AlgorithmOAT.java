package algorithmOAT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	private boolean debug = false;
	
	public AlgorithmOAT(int _graphSize) throws IOException {
		init(_graphSize, -1, -1, -1, -1);
	}
	
	public AlgorithmOAT(int _graphSize, int homebase, int blackHole, int minWait, int maxWait, boolean _debug) throws Exception {
		if(blackHole > 0 && homebase > 0 && blackHole == homebase) throw new Exception("Black Hole and Home Base cannot be at the same location");
		debug = _debug;
		init(_graphSize, homebase, blackHole, minWait, maxWait);
	}
	
	private void init(int _n, int homeBase, int blackHole, int minWait, int maxWait) throws IOException {
		agentList = new ArrayList<ATAgent>();
		graphSize = _n;
		
		numAgents = 2 * (_n - 1);
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmOptAvgTime", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = homeBase > 0 ? homeBase : rng.nextInt(graphSize);
		blackHoleIndex = blackHole > 0 ? blackHole : rng.nextInt(graphSize);
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		ATNode homebase = (ATNode) graph.getNodeList().get(homebaseIndex);
		
		// Generate agents
		// LEFT
		int j = 0;
		for(int i = 0; i < numAgents/2; i++, j++) {
			if(minWait > 0 && maxWait > 0) {
				agentList.add(new ATAgent(j, i, graphSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase, minWait, maxWait));
			} else {
				agentList.add(new ATAgent(j, i, graphSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase));
			}
		}
		
		// RIGHT
		for(int i = 0; i < numAgents/2; i++, j++) {
			if(minWait > 0 && maxWait > 0) {
				agentList.add(new ATAgent(j, i, graphSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase, minWait, maxWait));
			} else {
				agentList.add(new ATAgent(j, i, graphSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase));
			}
			
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
	
	public void startAlgorithmOAT() throws IOException {
		System.out.println("Start Algorithm : Algorithm Optimal Average Time");
		
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 1000000000;
		int idealTimeCounter = 0;
		
		long startTime = System.currentTimeMillis();
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			for(ATAgent agent : agentList) {
				if(agent.moveAndCheck()) {
					loop = false;
				}
			}
			
			if(debug) {
				graph.print();
				System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			}
			
			loopBound--;
			idealTimeCounter++;
		}
		
		ATNode homebase = (ATNode) graph.getNodeList().get(homebaseIndex);
		ArrayList<Integer>termData = homebase.getTerminationData();
		System.out.println(termData.toString());
		
		long elapsedTime = System.currentTimeMillis() - startTime;
	    //BufferedWriter writer = new BufferedWriter(new FileWriter("OAT.txt"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("OAT.txt", true)));
	    writer.write("" + elapsedTime + "," + (idealTimeCounter/2) + "\n");
	    writer.close();
	    
		System.out.println("Black Hole is determined to be: " + termData.get(0) + " node(s) to the left, " + termData.get(1) +  " node(s) to the right, and at node id: " + termData.get(2));
		System.out.println("Actual location of black hole is: " + blackHoleIndex);
		System.out.println("Home Base Location: " + homebaseIndex);
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
