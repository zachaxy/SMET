package net.java.amateras.uml.testcase.tcg;

import java.util.HashMap;

public class TestCase {

	private String abstractPath;
	private HashMap<String, String> inputData;
	
	public TestCase(String ap) {
		abstractPath = ap;
		inputData = new HashMap<String, String>();
	}
	
	public String getAbstractPath() {
		return abstractPath;
	}
	
	public void addInputData(String var, String value) {
		inputData.put(var, value);
	}
	
	public HashMap<String, String> getInputData() {
		return inputData;
	}
	
	public void dumps() {
		System.out.println("-------------------Path-------------------");
		System.out.println(abstractPath);
		System.out.println("----------------Input Data----------------");
		for (String s : inputData.keySet()) {
			System.out.println(s + " : " + inputData.get(s));
		}
	}
}
