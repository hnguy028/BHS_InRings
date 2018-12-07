package algorithmOTS;

import mobileAgents.Agent;

public class TSAgent extends Agent {
	public TSAgent(Integer _id, Integer _rel_id, Integer _graphSize, AgentGroup _group, String _name, TSNode _node) {
		super(_id, _name, _node);
	}
	
	public TSAgent(Integer _id, Integer _rel_id, Integer _graphSize, AgentGroup _group, String _name, TSNode _node, int minAsynch, int maxAsynch) {
		super(_id, _name, _node);
	}
}
