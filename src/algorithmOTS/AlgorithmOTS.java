package algorithmOTS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
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
	
	private boolean debug;
	
	private int filenum;
	
	public AlgorithmOTS(int _graphSize) throws Exception {
		init(_graphSize, -1, -1, -1, -1);
	}
	
	public AlgorithmOTS(int _graphSize, int homeBase, int blackHole, int minWait, int maxWait, boolean _debug, int i) throws Exception {
		if(blackHole >= 0 && homeBase >= 0 && blackHole == homeBase) throw new Exception("Black Hole and Home Base cannot be at the same location");
		debug = _debug;
		filenum = i;
		init(_graphSize, homeBase, blackHole, minWait, maxWait);
	}
	
	public void init(int _n, int homeBase, int blackHole, int minWait, int maxWait) throws Exception {
		agentList = new ArrayList<TSAgent>();
		graphSize = _n;
		
		numAgents = 2;
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmOptTeamSize", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = homeBase >= 0 ? homeBase : rng.nextInt(graphSize);
		blackHoleIndex = blackHole >= 0 ? blackHole : rng.nextInt(graphSize);
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		TSNode homebase = (TSNode) graph.getNodeList().get(homebaseIndex);
		
		// Generate agents
		if(minWait > 0 && maxWait > 0) {
			// LEFT
			agentList.add(new TSAgent(0, graphSize, Direction.LEFT, "Agent#0", homebase, minWait, maxWait));
			
			// RIGHT
			agentList.add(new TSAgent(1, graphSize, Direction.RIGHT, "Agent#1", homebase));
		} else {
			// LEFT
			agentList.add(new TSAgent(0, graphSize, Direction.LEFT, "Agent#0", homebase, minWait, maxWait));
			
			// RIGHT
			agentList.add(new TSAgent(1, graphSize, Direction.RIGHT, "Agent#1", homebase));	
		}
				
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
		System.out.println("Start Algorithm : Algorithm Optimal Team Size");
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 1000000000;
		int idealTimeCounter = 0;
		
//		PrintWriter tWriter = new PrintWriter(new BufferedWriter(new FileWriter("OTS_move_time" + filenum + ".csv", true)));
	    
		long startTime = System.nanoTime();
//		long intermediateTime1 = System.nanoTime();
//		long intermediateTime2 = System.nanoTime();
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			for(TSAgent agent : agentList) {
				if(agent.moveAndCheck()) {
					loop = false;
				}
			}
			
//			intermediateTime2 = System.nanoTime();
//			tWriter.write(idealTimeCounter + "," + (intermediateTime2 - intermediateTime1) + "\n");
//			intermediateTime1 = intermediateTime2;
			
			if(debug) {
				graph.print();
				System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			}
			
			loopBound--;
			idealTimeCounter++;
		}
		
		long elapsedTime = System.nanoTime() - startTime;
//	    tWriter.close();
	    
	    // BufferedWriter writer = new BufferedWriter(new FileWriter("OTS.txt"));
	    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("OTS" + graphSize + ".csv", true)));
	    writer.write("" + elapsedTime + "," + (idealTimeCounter/2) + "\n");
	    writer.close();
		
		System.out.println("Black Hole is determined to be: " + homebaseWhiteBoard.get(0) + " node(s) to the left, " + homebaseWhiteBoard.get(1) +  " node(s) to the right, and at node id: " + homebaseWhiteBoard.get(2));
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
