package cautiousWalk;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Random;

import datastructures.Graph;
import datastructures.Ring;
import engine.GraphManager;
import mobileAgents.Agent;

public class CautiousWalk {
	Ring graph;
	Random rng = new Random();
	
	static int numAgents = 2;
	ArrayList<Agent> agentList; 
	
	private int graphSize;
	
	public CautiousWalk(int _graphSize) {
		agentList = new ArrayList<Agent>();
		graphSize = _graphSize;
		
		// Generate graph
		graph = new GraphManager().generateGraph("CautiousWalk", 10);
		
		// Generate blackhole and homebase index
		int homebaseIndex = rng.nextInt(graphSize) + 1;
		int blackHoleIndex = rng.nextInt(graphSize) + 1;
		
		while(homebaseIndex == blackHoleIndex) {
			blackHoleIndex = rng.nextInt(graphSize) + 1;
		}
		
		// Generate agents
		for(int i = 0; i < numAgents; i++) {
			agentList.add(new Agent("Agent#" + i));
		}
		
		setHomeBase(homebaseIndex);
		setBlackHole(blackHoleIndex);
		
		graph.print();
		
		// put agents at home base
		
		
		// generate agent list
		
		// start algo
		
		// go check
	}
	
	public void setHomeBase(int index) {
		graph.getNodeList().get(index).setAsHomeBase();
		
		for(Agent agent : agentList) {
			graph.getNodeList().get(index).putAgent(agent);
		}
	}
	
	public void setBlackHole(int index) {
		graph.getNodeList().get(index).setAsBlackHole();
	}
}
