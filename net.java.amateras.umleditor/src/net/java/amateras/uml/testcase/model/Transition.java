package net.java.amateras.uml.testcase.model;

import java.util.Vector;

public class Transition {

	private State startState;
	private State endState;
	private String guard;
	private Vector<String> updates;
	
	public Transition(State sState, State eState) {
		startState = sState;
		endState = eState;
		guard = "true";
		updates = new Vector<String>();
		
		startState.addOutTrans(this);
		endState.addInTrans(this);
	}
	
	public void setGuard(String g) {
		guard = g;
	}
	
	public String getGuard() {
		return guard;
	}
	
	public void addUpdate(String u) {
		updates.add(u);
	}
	
	public Vector<String> getUpdates() {
		return updates;
	}
	
	public State getStartState() {
		return startState;
	}
	
	public State getEndState() {
		return endState;
	}
}
