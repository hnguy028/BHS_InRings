package cautiousWalk;

import datastructures.Edge;

public class CWEdge extends Edge {
	private EdgeState state = EdgeState.UNEXPLORED;
	
	public CWEdge() {
		super(null, null);
	}
	
	public CWEdge(CWNode _left, CWNode _right) {
		super(_left, _right);
	}
	
	
	public void updateState(EdgeState _state) { state = _state; }
	public EdgeState getState() { return state; }
}
