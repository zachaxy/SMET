package net.java.amateras.uml.testcase.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by zhangxin on 2016/5/28.
 */
public class Automaton {
    private HashMap<String, State> states;
    private ArrayList<Transition> trans;
    private HashMap<String, Variable> vars;
    private State initState;

    public Automaton() {
        states = new HashMap();
        trans = new ArrayList();
        vars = new HashMap();
    }

    public void addState(String name) {
        State s = new State(name);
        states.put(name, s);
    }

    public State getState(String name) {
        return states.get(name);
    }

    public void setInitialState(String name) {
        System.out.println("我执行到这里了,初始状态的值是"+"---"+name+"------"+states.get(name));
        initState = states.get(name);
    }

    public State getInitialState() {
        return initState;
    }

    public void addTransition(String sStateName, String eStateName, String guard, Vector<String> updates) {
        State sState = states.get(sStateName);
        State eState = states.get(eStateName);
        Transition t = new Transition(sState, eState);

        t.setGuard(guard);

        for (String u : updates) {
            t.addUpdate(u);
        }

        trans.add(t);
    }

    public void addVariable(String name, String type, String domain, boolean isInput) {
        Variable v = new Variable(name, type, domain, isInput);
        vars.put(name, v);
    }

    public Variable getVariable(String name) {
        return vars.get(name);
    }

    public HashMap<String, Variable> getVariables() {
        return vars;
    }

    public void dumps() {
        System.out.println("States : " + states.size());
        System.out.println("Transitions : " + trans.size());
        System.out.println("--------------STATES--------------");
        System.out.println(states.keySet());
        System.out.println("-----------INIT  STATES-----------");
        System.out.println(initState.getName());
        System.out.println("---------------VARS---------------");
        for (Variable v : vars.values()) {
            System.out.println(v.getName());
            System.out.println("\t" + v.getDomain());
        }
        System.out.println("--------------TRANS'--------------");
        for (Transition t : trans) {
            System.out.println(t.getStartState().getName() + "->" + t.getEndState().getName());
            System.out.println("\t" + t.getGuard());
            System.out.println("\t" + t.getUpdates());
        }
    }


    public void dumps(String fileName) {
        System.out.println("States : " + states.size());
        System.out.println("Transitions : " + trans.size());
        try {
            PrintWriter pw = new PrintWriter(new File(fileName));
            pw.println("--------------STATES--------------");
            pw.println(states.keySet());
            pw.println("-----------INIT  STATES-----------");
            pw.println(initState.getName());
            pw.println("---------------VARS---------------");
            for (Variable v : vars.values()) {
                pw.println(v.getName());
                pw.println("\t" + v.getDomain());
            }
            pw.println("--------------TRANS'--------------");
            for (Transition t : trans) {
                pw.println(t.getStartState().getName() + "->" + t.getEndState().getName());
                pw.println("\t" + t.getGuard());
                pw.println("\t" + t.getUpdates());
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
