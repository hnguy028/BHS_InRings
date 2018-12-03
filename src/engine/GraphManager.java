package engine;

import java.awt.event.MouseWheelEvent;

import cautiousWalk.CWEdge;
import cautiousWalk.CWNode;
import datastructures.Graph;
import datastructures.Ring;

public class GraphManager {
	public Ring generateGraph(String algorithm, int size) {
		switch (algorithm) {
		case "CautiousWalk":
			return generateCautiousWalkGraph(size);
		case "Algorithm1":
			return null;
		default:
			return null;
		}
	}
	
	private Ring generateCautiousWalkGraph(int graphSize) {
		Ring ring = new Ring();
		
		for(int i = 0; i < graphSize; i++) {
			ring.addNewNode(new CWNode("ID#" + i), new CWEdge());
		}
		
		return ring;
	}
}
