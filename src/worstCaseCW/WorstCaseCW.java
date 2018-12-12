package worstCaseCW;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
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
	private boolean debug = false;
	private int filenum;
	
	public WorstCaseCW(int _graphSize) throws Exception {
		init(_graphSize, -1, -1, -1, -1);
	}
	
	public WorstCaseCW(int _graphSize, int homebase, int blackHole, int minWait, int maxWait, boolean _debug, int i) throws Exception {
		if(blackHole >= 0 && homebase >= 0 && blackHole == homebase) throw new Exception("Black Hole and Home Base cannot be at the same location");
		debug = _debug;
		filenum = i;
		init(_graphSize, homebase, blackHole, minWait, maxWait);
	}
	
	private void init(int _graphSize, int homebase, int blackHole, int minWait, int maxWait) throws Exception {
		agentList = new ArrayList<CWAgent>();
		graphSize = _graphSize;
		
		// Generate graph
		graph = new GraphManager().generateGraph("CautiousWalk", graphSize);
		
		// Generate blackhole and homebase index
		homebaseIndex = homebase >= 0 ? homebase : rng.nextInt(graphSize);
		blackHoleIndex = blackHole >= 0 ? blackHole : rng.nextInt(graphSize);
		
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
		
		// Generate agents
		if(minWait > 0 && maxWait > 0) {
			agentList.add(new CWAgent(0, "Agent#" + 0, graphSize, (CWNode) graph.getNodeList().get(homebaseIndex), TDirection.LEFT, minWait, maxWait));
			agentList.add(new CWAgent(1, "Agent#" + 1, graphSize, (CWNode) graph.getNodeList().get(homebaseIndex), TDirection.RIGHT, minWait, maxWait));
		} else {
			agentList.add(new CWAgent(0, "Agent#" + 0, graphSize, (CWNode) graph.getNodeList().get(homebaseIndex), TDirection.LEFT));
			agentList.add(new CWAgent(1, "Agent#" + 1, graphSize, (CWNode) graph.getNodeList().get(homebaseIndex), TDirection.RIGHT));
		}
		
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
		System.out.println("Start Algorithm : Worst Case Cautious Walk");
		System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString() + "  -- BH[" + blackHoleIndex + "]");
		
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 1000000000;
		int idealTimeCounter = 0;
		
		int blackHoleOffset = 0;
		
//		PrintWriter tWriter = new PrintWriter(new BufferedWriter(new FileWriter("WCCW_move_time" + filenum + ".csv", true)));
	    
		long startTime = System.nanoTime();
//		long intermediateTime1 = System.nanoTime();
//		long intermediateTime2 = System.nanoTime();
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			for(CWAgent agent : agentList) {
				if(agent._move()) {
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
//		tWriter.close();
		
	    //BufferedWriter writer = new BufferedWriter(new FileWriter("WCCW.txt"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("WCCW" + graphSize + ".csv", true)));
	    writer.write("" + elapsedTime + "," + (idealTimeCounter/2) + "\n");
	    writer.close();
	    
	    System.out.println(homebaseWhiteBoard.toString());
		int blackHoleLocation = (homebaseWhiteBoard.get(1) + homebaseIndex + 1) % graphSize;
		System.out.println("Location of Black Hole -> Cautious Walk Aglorithm: " + blackHoleLocation + " vs Actual: " + blackHoleIndex);
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
