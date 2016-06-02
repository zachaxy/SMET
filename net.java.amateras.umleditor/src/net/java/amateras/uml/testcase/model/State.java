package net.java.amateras.uml.testcase.model;

import java.util.Vector;

public class State {

	private String name;
	private Vector<Transition> inTrans;
	private Vector<Transition> outTrans;
	
	public State(String n) {
		name = n;
		inTrans = new Vector<Transition>();
		outTrans = new Vector<Transition>();
	}
	
	public void addInTrans(Transition t) {
		inTrans.add(t);
	}
	
	public void addOutTrans(Transition t) {
		outTrans.add(t);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector<Transition> getOuTransitions() {
		return outTrans;
	}
}
