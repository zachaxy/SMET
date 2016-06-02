package net.java.amateras.uml.testcase.tcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import net.java.amateras.uml.testcase.model.Automaton;
import net.java.amateras.uml.testcase.model.State;
import net.java.amateras.uml.testcase.model.Transition;
import net.java.amateras.uml.testcase.model.Variable;

 

public class DFSPCGenerator extends Generator {

	private Automaton automaton;
	private ArrayList<State> visitedStates;
	private Stack<Transition> currentPath;
	
	public DFSPCGenerator(Automaton a) {
		super();
		
		automaton = a;
		visitedStates = new ArrayList<State>();
		currentPath = new Stack<Transition>();
	}
	
	@Override
	public void start() {
		searchPathAndGenerateTestCase(automaton.getInitialState());
	}
	
	private void searchPathAndGenerateTestCase(State s) {
		if (!visitedStates.contains(s) && s.getOuTransitions().size() > 0) {
			visitedStates.add(s);
			for (Transition t : s.getOuTransitions()) {
				currentPath.push(t);
				searchPathAndGenerateTestCase(t.getEndState());
				currentPath.pop();
			}
		} else {
			TestCase tc = generateTestCase();
			if (tc != null) {
				testCases.add(tc);
			}
		}
	}
	
	private TestCase generateTestCase() {
		Stack<Transition> path = new Stack<Transition>();
		path.addAll(currentPath);
		String pc = "(true)";
		String ap = path.lastElement().getEndState().getName();
		while (!path.empty()) {
			Transition t = path.pop();
			for (int i = t.getUpdates().size() - 1; i > 0; i = i - 2) {
				pc = pc.replaceAll("\\(" + t.getUpdates().elementAt(i - 1) + "\\)", t.getUpdates().elementAt(i));
			}
			pc = "(&&" + t.getGuard() + pc + ")";
			ap = t.getStartState().getName() + "---" + ap;
		}
		
		Solver s = new Solver();
		s.setVariables(automaton.getVariables().values());
		s.setConstraint(pc);
		HashMap<String, String> result = s.solve();
		if (result == null) {
			return null;
		}
		TestCase tc = new TestCase(ap);
		for (Variable v : automaton.getVariables().values()) {
			if (v.isInput()) {
				tc.addInputData(v.getName(), result.get(v.getName()));
			}
		}
		return tc;
	}
}
