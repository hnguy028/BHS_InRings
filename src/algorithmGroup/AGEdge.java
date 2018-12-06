package algorithmGroup;

import datastructures.Edge;

public class AGEdge extends Edge{
	
	public AGEdge() {
		super(null, null);
	}
	
	public AGEdge(AGNode left, AGNode right) {
		super(left, right);
	}
}
