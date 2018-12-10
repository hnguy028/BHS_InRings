package algorithmGroup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	private boolean debug = false;
	
	public AlgorithmGroup(int _graphSize) throws IOException {
		init(_graphSize, -1, -1, -1, -1);
	}
	
	public AlgorithmGroup(int _graphSize, int homeBase, int blackHole, int minWait, int maxWait, boolean _debug) throws Exception {
		if(blackHole > 0 && homeBase > 0 && blackHole == homeBase) throw new Exception("Black Hole and Home Base cannot be at the same location");
		debug = _debug;
		init(_graphSize, homeBase, blackHole, minWait, maxWait);
	}
	
	private void init(int _n, int homeBase, int blackHole, int minWait, int maxWait) throws IOException {
		agentList = new ArrayList<AGAgent>();
		graphSize = _n;
		
		numAgents = _n - 1;
		q = numAgents / 4;
		
		// Generate graph
		graph = new GraphManager().generateGraph("AlgorithmGroup", graphSize);
				
		// Generate blackhole and homebase index
		homebaseIndex = homeBase > 0 ? homeBase : rng.nextInt(graphSize);
		blackHoleIndex = blackHole > 0 ? blackHole : rng.nextInt(graphSize);
		
//		homebaseIndex = 2;
//		blackHoleIndex = 4;
				
		// Ensure blackhole and homebase indices are different 
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize);
		}
				
		AGNode homebase = (AGNode) graph.getNodeList().get(homebaseIndex);
		
		boolean boundAsynch = minWait > 0 && maxWait > 0;
		
		// Generate agents
		// LEFT
		int j = 0;
		for(int i = 0; i < q; i++, j++) {
			if(boundAsynch) { 
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase, minWait, maxWait));
			} else {
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.LEFT, "Agent#" + j + ":L:" + i, homebase));
			}
		}
		
		// RIGHT
		for(int i = 0; i < q; i++, j++) {
			if(boundAsynch) { 
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase, minWait, maxWait));
			} else {
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.RIGHT, "Agent#" + j + ":R:" + i, homebase));
			}
		}
		
		// MIDDLE
		for(int i = 0; i < q + 1; i++, j++) {
			if(boundAsynch) { 
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.MIDDLE, "Agent#" + j + ":M:" + i, homebase, minWait, maxWait));
			} else {
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.MIDDLE, "Agent#" + j + ":M:" + i, homebase));
			}
		}
		
		// TIEBREAKER
		for(int i = 0; i < q - 1; i++, j++) {
			if(boundAsynch) { 
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.TIEBREAKER, "Agent#" + j + ":T:" + i, homebase, minWait, maxWait));
			} else {
				agentList.add(new AGAgent(j, i, q, graphSize, AgentGroup.TIEBREAKER, "Agent#" + j + ":T:" + i, homebase));
			}
			
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
	
	public void startAlgorithmGroup() throws IOException {
		System.out.println("Start Algorithm : Algorithm Group");
		
		boolean loop = true;
		
		// max time units - for testing
		int loopBound = 1000000000;
		int counter = 0;
		
		long startTime = System.currentTimeMillis();
		
		while(loop) {
			if(loopBound <= 0) { loop = false; System.out.println("Forced Termination!");}
			
			// move agents
			for(AGAgent agent : agentList) {
				if(agent.moveAndCheck()) {
					loop = false;
				}
			}
			
			if(debug) {
				graph.print();
				System.out.println("HB[" + homebaseIndex + "]:" + homebaseWhiteBoard.toString());
			}
			
			loopBound--;
			counter ++;
		}
		
		long elapsedTime = System.currentTimeMillis() - startTime;
	    //BufferedWriter writer = new BufferedWriter(new FileWriter("Group.txt"));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Group.txt", true)));
	    writer.write("" + elapsedTime + "," + counter + "\n");
	    writer.close();
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
