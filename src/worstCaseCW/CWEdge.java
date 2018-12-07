package worstCaseCW;

import datastructures.Edge;

public class CWEdge extends Edge {
	private EdgeState state = EdgeState.UNEXPLORED;
	private String mark;
	
	public CWEdge() {
		super(null, null);
	}
	
	public CWEdge(CWNode _left, CWNode _right) {
		super(_left, _right);
	}
	
	
	public void updateState(EdgeState _state) { state = _state; }
	public EdgeState getState() { return state; }
	
	public void setActive(String _mark) {
		if(state == EdgeState.UNEXPLORED) { 
			mark = _mark;
			state = EdgeState.ACTIVE;
		}
	}
	
	public void markSafe(String _mark) {
		if(_mark.equals(mark) && state == EdgeState.ACTIVE) {
			state = EdgeState.SAFE;
		} 
	}
}
