package algorithmOTS;

import datastructures.Edge;
import datastructures.EdgeState;


public class TSEdge extends Edge {
	
	private EdgeState state = EdgeState.UNEXPLORED;
	private String mark;
	
	public TSEdge() {
		super(null, null);
	}
	
	public TSEdge(TSNode left, TSNode right) {
		super(left, right);
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

