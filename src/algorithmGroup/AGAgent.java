package algorithmGroup;

import datastructures.Node;
import mobileAgents.Agent;

public class AGAgent extends Agent {
	private AgentGroup group;
	private int rel_id;
	private int q;
	private int stage;
	
	private int distTraveled;
	
	public AGAgent(int _id, int _rel_id, int groupSize, AgentGroup _group, String _name, Node _node) {
		super(_id, _name, _node);
		
		rel_id = _rel_id; // i
		q = groupSize; // q
		group = _group;
		
		stage = 0;
		distTraveled = 0;
		
		// distToTravel = ;
		// depends on group type
		
	}
	
	@Override
	public void move() {
		switch (group) {
		case LEFT:
			break;
		case RIGHT:
			break;
		case MIDDLE:
			break;
		case TIEBREAKER:
			break;
		}
	}
	
	private void moveLeft() {
		switch (stage) {
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
	}
}
