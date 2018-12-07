package algorithmOTS;

import datastructures.Edge;

public class TSEdge extends Edge {
	
	public TSEdge() {
		super(null, null);
	}
	
	public TSEdge(TSNode left, TSNode right) {
		super(left, right);
	}
}
