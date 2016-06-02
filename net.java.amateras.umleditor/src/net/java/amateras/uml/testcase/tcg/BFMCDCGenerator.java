package net.java.amateras.uml.testcase.tcg;

import java.util.*;
import java.util.regex.Pattern;

import net.java.amateras.uml.testcase.model.Automaton;
import net.java.amateras.uml.testcase.model.State;
import net.java.amateras.uml.testcase.model.Transition;
import net.java.amateras.uml.testcase.model.Variable;
import net.java.amateras.uml.testcase.util.Utility;

 

 

public class BFMCDCGenerator extends Generator {

	private Automaton automaton;
	private ArrayList<State> visitedStates;
	private Stack<Transition> currentPath;
	private HashSet<String> conditions;
	
	public BFMCDCGenerator(Automaton a) {
		super();
		
		automaton = a;
		visitedStates = new ArrayList<State>();
		currentPath = new Stack<Transition>();
		conditions = new HashSet<String>();
	}
	
	@Override
	public void start() {
		searchPathAndCollectCondition(automaton.getInitialState());
		//generateTestCase();
	}

    //using DFS to search the path,generate a test case per path;
	private void searchPathAndCollectCondition(State s) {
        //System.out.print(s.getName()+"->");
        if (!visitedStates.contains(s) && s.getOuTransitions().size() > 0) {
			visitedStates.add(s);
			for (Transition t : s.getOuTransitions()) {
				currentPath.push(t);
				searchPathAndCollectCondition(t.getEndState());
				currentPath.pop();
			}
		} else {
			collectCondition(calculatePathCondition());
            generateTestCase();
            conditions.clear();

        }
	}
	
	private String calculatePathCondition(int m) {
		Stack<Transition> path = new Stack<Transition>();
		path.addAll(currentPath);
		String pc = "(true)";
		//int index = 0;
		while (!path.empty()) {
			Transition t = path.pop();
			/*if(index == 0){
				pc = t.getUpdates().elementAt(1);
			}else {*/
				for (int i = t.getUpdates().size() - 1; i > 0; i = i - 2) {
					pc = pc.replaceAll("\\(" + t.getUpdates().elementAt(i - 1) + "\\)", t.getUpdates().elementAt(i));
				}
			//}
			
			pc = "(&&" + t.getGuard() + pc + ")";
		}
		
		return pc;
	}
	
	private String calculatePathCondition() {
		Stack<Transition> path = new Stack<Transition>();
		path.addAll(currentPath);
		String pc = "(true)";
		//int index = 0;
		while (!path.empty()) {
			Transition t = path.pop();

			if(pc=="(true)"&&t.getUpdates().size()>0){
				String s = t.getUpdates().elementAt(1); //= t.getUpdates().elementAt(i - 1).
				s = s.replaceAll("\\\\n", "%");
				s = s.replaceAll("\\n", "%");
				t.getUpdates().set(1, s);
				pc = "(&&" + t.getUpdates().elementAt(1) + pc + ")";
			}else{
				for (int i = t.getUpdates().size() - 1; i > 0; i = i - 2) {
					String s = t.getUpdates().elementAt(i - 1); //= t.getUpdates().elementAt(i - 1).
					//s = s.replaceAll("\\\\n", "%");
					//s = s.replaceAll("\\n", "%");
					//t.getUpdates().set(i-1, s);
					//pc = pc.replaceAll("\\(" + t.getUpdates().elementAt(i - 1) + "\\)", t.getUpdates().elementAt(i));
					pc = pc.replace(t.getUpdates().elementAt(i-1), t.getUpdates().elementAt(i));
					//pc = pc.replaceAll("\\(" + t.getUpdates().elementAt(i - 1), t.getUpdates().elementAt(i));
				}
			}
			if(t.getGuard()=="(true)"){
				
			}else{
				pc = "(&&" + t.getGuard() + pc + ")";
			}
			
		}
		
		return pc;
	}
	
	private void collectCondition(String s) {
		if (Pattern.compile("^\\((&&)|(\\|\\|)").matcher(s).find()) {
			int op1Left = 3;
			int op1Right = Utility.findMatchedBracketPos(s, op1Left) + 1;
			int op2Left = op1Right;
			int op2Right = s.length() - 1;
			collectCondition(s.substring(op1Left, op1Right));
			collectCondition(s.substring(op2Left, op2Right));
		} else {
			if (!s.equals("(true)") && !s.equals("(false)") && Pattern.compile("[a-zA-Z]").matcher(s).find()) {
				s = Utility.addInitConstraint(s);
                if(s!=null){
                    conditions.add(s);
                }
			}
		}
	}
	
	/*private void generateTestCase() {
		ArrayList<String> conditionArray = new ArrayList<String>();
		conditionArray.addAll(conditions);
		for (int i = 0; i < 1 << conditionArray.size(); ++ i) {
			String pc = "(true)";
			for (int j = 0; j < conditionArray.size(); ++ j) {
				if (i << (31 - j) >>> 31 == 1) {
					pc = "(&&" + pc + conditionArray.get(j) + ")";
				} else {
					pc = "(&&" + pc + "(not" + conditionArray.get(j) + "))";
				}
			}
			String ap = "";
			Solver s = new Solver();
			s.setVariables(automaton.getVariables().values());
			s.setConstraint(pc);
			HashMap<String, String> result = s.solve();
			if (result == null) {
				continue;
			}
			TestCase tc = new TestCase(ap);
			for (Variable v : automaton.getVariables().values()) {
				if (v.isInput()) {
					tc.addInputData(v.getName(), result.get(v.getName()));
				}
			}
			testCases.add(tc);
		}
	}*/
    private void generateTestCase() {
        ArrayList<String> conditionArray = new ArrayList<String>();
        conditionArray.addAll(conditions);
        for (int i = 0; i <conditionArray.size(); ++ i) {
            String pc =conditionArray.get(i);
            String ap = "";
            Solver s = new Solver();
            s.setVariables(automaton.getVariables().values());
            s.setConstraint(pc);
            HashMap<String, String> result = s.solve();
            if (result == null) {
                continue;
            }else{
                System.out.println("dump the result");
                for (Map.Entry<String, String> entry : result.entrySet()) {
                    System.out.println(entry.getKey()+":"+entry.getValue());

                }
            }
            TestCase tc = new TestCase(ap);
            for (Variable v : automaton.getVariables().values()) {
                if (v.isInput()) {
                    tc.addInputData(v.getName(), result.get(v.getName()));
                }
            }
            testCases.add(tc);
        }
    }
}
/*||||||| .r0
=======
package tcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Pattern;

import Automaton;
import State;
import Transition;
import Variable;
import util.Utility;

public class BFMCDCGenerator extends Generator {

	private Automaton automaton;
	private ArrayList<State> visitedStates;
	private Stack<Transition> currentPath;
	private HashSet<String> conditions;
	
	public BFMCDCGenerator(Automaton a) {
		super();
		
		automaton = a;
		visitedStates = new ArrayList<State>();
		currentPath = new Stack<Transition>();
		conditions = new HashSet<String>();
	}
	
	@Override
	public void start() {
		searchPathAndCollectCondition(automaton.getInitialState());
		generateTestCase();
	}
	
	private void searchPathAndCollectCondition(State s) {
		if (!visitedStates.contains(s) && s.getOuTransitions().size() > 0) {
			visitedStates.add(s);
			for (Transition t : s.getOuTransitions()) {
				currentPath.push(t);
				searchPathAndCollectCondition(t.getEndState());
				currentPath.pop();
			}
		} else {
			collectCondition(calculatePathCondition());
		}
	}
	
	private String calculatePathCondition() {
		Stack<Transition> path = new Stack<Transition>();
		path.addAll(currentPath);
		String pc = "(true)";
		while (!path.empty()) {
			Transition t = path.pop();
			for (int i = t.getUpdates().size() - 1; i > 0; i = i - 2) {
				pc = pc.replaceAll("\\(" + t.getUpdates().elementAt(i - 1) + "\\)", t.getUpdates().elementAt(i));
			}
			pc = "(&&" + t.getGuard() + pc + ")";
		}
		
		return pc;
	}
	
	private void collectCondition(String s) {
		if (Pattern.compile("^\\((&&)|(\\|\\|)").matcher(s).find()) {
			int op1Left = 3;
			int op1Right = Utility.findMatchedBracketPos(s, op1Left) + 1;
			int op2Left = op1Right;
			int op2Right = s.length() - 1;
			collectCondition(s.substring(op1Left, op1Right));
			collectCondition(s.substring(op2Left, op2Right));
		} else {
			if (!s.equals("(true)") && !s.equals("(false)") && Pattern.compile("[a-zA-Z]").matcher(s).find()) {
				conditions.add(s);
			}
		}
	}
	
	private void generateTestCase() {
		ArrayList<String> conditionArray = new ArrayList<String>();
		conditionArray.addAll(conditions);
		for (int i = 0; i < 1 << conditionArray.size(); ++ i) {
			String pc = "(true)";
			for (int j = 0; j < conditionArray.size(); ++ j) {
				if (i << (31 - j) >>> 31 == 1) {
					pc = "(&&" + pc + conditionArray.get(j) + ")";
				} else {
					pc = "(&&" + pc + "(not" + conditionArray.get(j) + "))";
				}
			}
			String ap = "";
			Solver s = new Solver();
			s.setVariables(automaton.getVariables().values());
			s.setConstraint(pc);
			HashMap<String, String> result = s.solve();
			if (result == null) {
				continue;
			}
			TestCase tc = new TestCase(ap);
			for (Variable v : automaton.getVariables().values()) {
				if (v.isInput()) {
					tc.addInputData(v.getName(), result.get(v.getName()));
				}
			}
			testCases.add(tc);
		}
	}
}
 
*/