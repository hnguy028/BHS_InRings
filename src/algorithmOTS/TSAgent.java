package algorithmOTS;

import java.util.ArrayList;

import datastructures.EdgeState;
import mobileAgents.Agent;

public class TSAgent extends Agent {
	
	private int ringSize;
	
	private ExploreMode mode = ExploreMode.BIG;
	
	private AgentState state = AgentState.PREPROCESSING;
	private Direction agentType;
	private Direction travelDirection;
	
	private int distTraveled = 0;
	private int spaceToExplore;
	private int totalExploredSpace = 0;
	private int currentExploreSpace = 0;
	
	private boolean startState = true;
	private boolean finished = false;
	
	boolean hasReturned = false;
	boolean bigDone = false;
	boolean smallDone = false;
	
	public TSAgent(Integer _id, Integer _graphSize, Direction _type, String _name, TSNode _node) {
		super(_id, _name, _node);
		
		ringSize = _graphSize;

		agentType = _type;
		travelDirection = _type;
		
		spaceToExplore = (int) Math.floor((double) (ringSize - 1) / 2.0);
		
	}
	
	public TSAgent(Integer _id, Integer _graphSize, Direction _type, String _name, TSNode _node, int minAsynch, int maxAsynch) {
		super(_id, _name, _node);
		
		ringSize = _graphSize;

		agentType = _type;
		travelDirection = _type;
		
		spaceToExplore = (int) Math.floor((double) (ringSize - 1) / 2.0);
	}
	
	public boolean moveAndCheck() throws Exception {
		if(finished) return false;
		return _move();
	}
	
	private boolean _move() throws Exception {
		if(node == null && edge == null) { throw new NullPointerException("Node and Edge are both null"); }
		boolean atNode = (node != null);
		
		if(atNode && node.isBlackHole()) return false; // we're dead XD
		
		return moveLogic(atNode);
	}
	
	private boolean moveLogic(boolean atNode) throws Exception {
		boolean isLeft = agentType == Direction.LEFT;
		
		switch(state) {
		case PREPROCESSING:
			
			// Check preprocessing termination condition
			if(totalExploredSpace >= spaceToExplore) {
				state = AgentState.EXPLORATION_RETURN;
				currentExploreSpace = 0;
				return false;
			}
			
			return performCautiousWalk(atNode, isLeft);
			
		case EXPLORATION_RETURN:
			// return to homebase
			move(atNode, !isLeft);
			
			if(node != null && node.isHomebase()) {
				ArrayList<Integer> whiteBoard = ((TSNode) node).getWhiteBoard();
				
				// become small
				mode = ExploreMode.SMALL;
				
				/* change state to notify (we will no move to the farthest explored node in the direction of the other agent)
				 * and tell it to become BIG
				 */
				state = AgentState.EXPLORATION_NOTIFY;
				
				return false;
			}			
			break;
		case EXPLORATION_NOTIFY:
			// move in the opposite direction until we find an active edge, then leave a msg on the preceding node for the other agent
			
			// When at a note check if the next edge is active
			if(atNode) {
				// check prev edge state
				TSEdge forthEdge = isLeft ? (TSEdge) node.getRightEdge() : (TSEdge) node.getLeftEdge();
				TSEdge backEdge = isLeft ? (TSEdge) node.getLeftEdge() : (TSEdge) node.getRightEdge();
				
				// found active edge
				if(forthEdge.getState() == EdgeState.ACTIVE) {
					// leave a message at the node, for other agent
					((TSNode) node).setMessage(mode == ExploreMode.SMALL ? ExploreMode.BIG : ExploreMode.SMALL, totalExploredSpace);
					
					// go back to our side an act as small
					state = AgentState.STAGE_C;
					spaceToExplore = 1;
					
					return false;
				}
			}
			move(atNode, !isLeft);
			
			break;
		case STAGE_B:
			if(atNode) {
				// check forth edge to be active, leave a message indicating to other agent to stay as big and update its unexplored space
				TSEdge forthEdge = isLeft ? (TSEdge) node.getRightEdge() : (TSEdge) node.getLeftEdge();
				
				if(forthEdge.getState() == EdgeState.ACTIVE) {
					((TSNode) node).setMessage(ExploreMode.BIG, totalExploredSpace);
					
					spaceToExplore = 1;
					currentExploreSpace = 0;
					distTraveled = 0;
					
					// move to stage C
					state = AgentState.STAGE_C;
					
					return false;
				}
			}
			// move to other agent
			move(atNode, !isLeft);
			break;
		case STAGE_C:
			if(smallDone) {
				if(atNode && node.isHomebase()) {
					finished = true; 
					int i = isLeft ? distTraveled : (ringSize - 1) - distTraveled;
					int j = isLeft ? (ringSize - 1) - distTraveled : distTraveled;
					ArrayList<Integer> wb = ((TSNode) node).getWhiteBoard();
					wb.add(i);
					wb.add(j);
					wb.add((Integer.parseInt(node.getId().replaceAll("[\\D]", "")) + j) % (ringSize));
					
					return true; 
				}
				
				move(atNode, !isLeft);
				return false;
			}
			
			if(atNode) {
				// if explored once, since we are small, go to next stage
				if(currentExploreSpace >= spaceToExplore) {
					state = AgentState.STAGE_E;
					distTraveled = 0;
					hasReturned = false;
					return false;
				}
				
				TSEdge forthEdge = isLeft ? (TSEdge) node.getLeftEdge() : (TSEdge) node.getRightEdge();
				
				if(forthEdge.getState() == EdgeState.UNEXPLORED) {
					hasReturned = true;
					
					// check if all nodes have been explored
					if(distTraveled == (ringSize - 2)) {
						distTraveled = 0;
						smallDone = true;
					}
				}
				
				if(hasReturned) {
					return performCautiousWalk(atNode, isLeft);
				}
			}
			
			if(hasReturned) {
				return performCautiousWalk(atNode, isLeft);
			} else {
				move(atNode, isLeft);
			}
			
			break;
		case STAGE_BIG:
			if(bigDone) {
				if(atNode && node.isHomebase()) { 
					finished = true; 
					int i = isLeft ? distTraveled : (ringSize - 1) - distTraveled;
					int j = isLeft ? (ringSize - 1) - distTraveled : distTraveled;
					ArrayList<Integer> wb = ((TSNode) node).getWhiteBoard();
					wb.add(i);
					wb.add(j);
					wb.add((Integer.parseInt(node.getId().replaceAll("[\\D]", "")) + j) % (ringSize)); 
					
					return true; 
				}
				
				move(atNode, !isLeft);
				return false;
			}
			
			// explore mindlessly
			if(atNode) {
				// if explored once, since we are small, go to next stage
				if(currentExploreSpace >= spaceToExplore) {
					// return to homebase, black hole is at exploreSpace + 1 in the direction of this agent
					bigDone = true;
					distTraveled = 0;
					
					return false;
				}
				
				TSEdge forthEdge = isLeft ? (TSEdge) node.getLeftEdge() : (TSEdge) node.getRightEdge();
				
				if(forthEdge.getState() == EdgeState.UNEXPLORED) {
					hasReturned = true;
				}
				
				if(hasReturned) {
					return performCautiousWalk(atNode, isLeft);
				}

			}
			
			if(hasReturned) {
				return performCautiousWalk(atNode, isLeft);
			} else {
				move(atNode, isLeft);
			}
			
			return false;
			
		case STAGE_E:
			if(atNode) {
				
				// check forth edge to be active, leave a message indicating to other agent to stay as big and update its unexplored space
				TSEdge forthEdge = isLeft ? (TSEdge) node.getRightEdge() : (TSEdge) node.getLeftEdge();
				
				if(forthEdge.getState() == EdgeState.ACTIVE) {
					((TSNode) node).setMessage(ExploreMode.SMALL, totalExploredSpace);
					
					spaceToExplore = (ringSize - 1) - distTraveled - 1;
					currentExploreSpace = 0;
					
					// act as big
					state = AgentState.STAGE_BIG;
					
					return false;
				}
			}
			// move to other agent
			move(atNode, !isLeft);
			break;
		default:
			break;
		}
		return false;
	}
	
	private boolean performCautiousWalk(boolean atNode, boolean isLeft) {
		// explore in the left direction in the amount defined in the explore space
		if(atNode) {
						
			// check prev edge state
			TSEdge forthEdge = isLeft ? (TSEdge) node.getLeftEdge() : (TSEdge) node.getRightEdge();
			TSEdge backEdge = isLeft ? (TSEdge) node.getRightEdge() : (TSEdge) node.getLeftEdge();
						
			if(forthEdge.getState() == EdgeState.UNEXPLORED) {
				if(startState && (backEdge.getState() == EdgeState.UNEXPLORED || backEdge.getState() == EdgeState.ACTIVE)) {
					// intitial state (only occurs at the start of the algo)
					move(atNode, isLeft);
								
					// mark edge as active
					((TSEdge) edge).setActive(name);
							
					travelDirection = isLeft ? Direction.LEFT : Direction.RIGHT;
								
					startState = false;
								
					return false;
				} else if(backEdge.getState() == EdgeState.ACTIVE) {
					// Arrive at the new node by exploring an unexplored edge
								
					// move back 
					move(atNode, !isLeft);
				
					// reverse travel direction
					travelDirection = !isLeft ? Direction.LEFT : Direction.RIGHT;
							
					return false;
				} else {
					// rEdge == SAFE
					move(atNode, isLeft);
								
					// mark edge as active
					((TSEdge) edge).setActive(name);
								
					travelDirection = isLeft ? Direction.LEFT : Direction.RIGHT;
								
					return false;
				}
			} else if(forthEdge.getState() == EdgeState.ACTIVE) {
				// This ONLY occurs when we have safely returned from exploring an unexplored edge
							
				// mark the edge as safe
				forthEdge.markSafe(name);
							
				// increment explore space
				totalExploredSpace++;
				currentExploreSpace++;
							
				// check for messages from other agent
				ExploreMode msg = ((TSNode) node).checkOutMessage();
				if(msg != ExploreMode.NULL) {
					int otherAgentExp = ((TSNode) node).checkOutCount();
					
					int remainingUnexplored = (ringSize - 1) - otherAgentExp - totalExploredSpace;
					
					if(msg == ExploreMode.BIG) {
						// if message is to stay big -> update to explore space (-1 since small will be taking care of it)
						
						spaceToExplore = remainingUnexplored;
						currentExploreSpace = 0;
						
						state = AgentState.STAGE_BIG;
						
					} else {
						// if message is to switch roles -> go to other agent tell them we are going to explore one node (so they -1 from their toExploreSpace)
						spaceToExplore = 1;
						currentExploreSpace = 0;
						
						state = AgentState.STAGE_B;
					}

					// change agent state to phase, and become big/small
					mode = msg;
					
					// now go to other agent and tell him to stay big, let him know we are going to explore one more node
				}
							
				// go back to the newly discovered safe node
				move(atNode, isLeft);
							
				travelDirection = isLeft ? Direction.LEFT : Direction.RIGHT;
			} else if (forthEdge.getState() == EdgeState.SAFE) {
				// move right
				move(atNode, !isLeft);
			}
		} else {				
			move(atNode, travelDirection == Direction.LEFT);
						
//			if(waitCounter == 0) {
//				TSEdge prevEdge = (TSEdge) ((travelDirection == Direction.LEFT) ? node.getRightEdge() : node.getLeftEdge());
//				if(prevEdge.getState() == EdgeState.ACTIVE) {
//					prevEdge.markSafe(name);
//				}
//			}
		}
		return false;
	}
	
	private void move(boolean atNode, boolean left) {
		
		if(atNode) {
			edge = left ? node.getLeftEdge() : node.getRightEdge();
			edge.putAgent(this);
			
			// remove self from node's agent list
			node.removeAgent(this);
			node = null;
			
			getNewWaitTime();
		} else {
			waitCounter--;
			
			if(waitCounter <= 0) {
				node = left ? edge.getLeftNode() : edge.getRightNode();
				node.putAgent(this);
				
				edge.removeAgent(this);
				edge = null;
				
				distTraveled++;
			}
		}
	}
}
