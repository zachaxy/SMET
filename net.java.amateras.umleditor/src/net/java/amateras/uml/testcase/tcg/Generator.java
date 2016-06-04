package net.java.amateras.uml.testcase.tcg;

import java.util.ArrayList;

public abstract class Generator {

	protected ArrayList<TestCase> testCases;
	
	public Generator() {
		testCases = new ArrayList<TestCase>();
	}
	
	public abstract void start();
	
	public ArrayList<TestCase> getTestCases() {
		return testCases;
	}
}
