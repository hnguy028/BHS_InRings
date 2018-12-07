package engine;

import algorithmGroup.AGEdge;
import algorithmGroup.AGNode;
import algorithmOAT.ATEdge;
import algorithmOAT.ATNode;
import algorithmOTS.TSEdge;
import algorithmOTS.TSNode;
import datastructures.Ring;
import worstCaseCW.CWEdge;
import worstCaseCW.CWNode;

public class GraphManager {
	public Ring generateGraph(String algorithm, int size) {
		switch (algorithm) {
		case "CautiousWalk":
			return generateCautiousWalkGraph(size);
		case "AlgorithmGroup":
			return generateAlgorithmGroupGraph(size);
		case "AlgorithmOptAvgTime":
			return generateAlgorithmOptAvgTime(size);
		case "AlgorithmOptTeamSize":
			return generateAlgorithmOptTeamSize(size);
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
	
	public Ring generateAlgorithmGroupGraph(int graphSize) {
		Ring ring = new Ring();
		
		for(int i = 0; i < graphSize; i++) {
			ring.addNewNode(new AGNode("ID#" + i), new AGEdge());
		}
		
		return ring;
	}
	
	public Ring generateAlgorithmOptAvgTime(int graphSize) {
		Ring ring = new Ring();
		
		for(int i = 0; i < graphSize; i++) {
			ring.addNewNode(new ATNode("ID#" + i), new ATEdge());
		}
		
		return ring;
	}
	
	public Ring generateAlgorithmOptTeamSize(int graphSize) {
		Ring ring = new Ring();
		
		for(int i = 0; i < graphSize; i++) {
			ring.addNewNode(new TSNode("ID#" + i), new TSEdge());
		}
		
		return ring;
	}
}
