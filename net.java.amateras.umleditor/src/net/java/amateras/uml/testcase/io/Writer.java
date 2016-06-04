package net.java.amateras.uml.testcase.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.java.amateras.uml.testcase.tcg.TestCase;

 

public class Writer {

	private String srceiaFileName;
	private String fileName;
	
	public Writer(String fn) {
		fileName = fn;
	}
	
	public Writer(String srceia, String fn) {
		srceiaFileName = srceia;
		fileName = fn;
	}
	
	public void writeXML(ArrayList<TestCase> testCases) {
		try {
			PrintWriter pw = new PrintWriter(fileName);
			pw.println("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
			pw.println("<root>");
			pw.println("\t<model>");
			//pw.println("\t\t<name>" + fileName.substring(0, fileName.length() - 4) + "</name>");
			pw.println("\t\t<name>" + fileName.substring(fileName.lastIndexOf("/")+1, fileName.length() - 4) + "</name>");
			pw.println("\t\t<inputs>");
			BufferedReader br = new BufferedReader(new FileReader(srceiaFileName));
			while (true) {
				String s = br.readLine();
				if (s.equals("        <inputs>")) {
					break;
				}
			}
			while (true) {
				String s = br.readLine();
				pw.println(s);
				if (s.equals("        </outputs>")) {
					break;
				}
			}
			br.close();
			pw.println("\t</model>");
			pw.println("\t<tsfinfo>");
			pw.println("\t\t<generatetime>" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.SIMPLIFIED_CHINESE).format(new Date()) + "</generatetime>");
			pw.println("\t</tsfinfo>");
			pw.println("\t<function>");
			int cnt = 0;
			for (TestCase tc : testCases) {
				pw.println("\t\t<testcase seq=\"" + cnt + "\">");
				pw.println("\t\t\t<abstract>" + tc.getAbstractPath() + "</abstract>");
				pw.println("\t\t\t<inputs>");
				for (String s : tc.getInputData().keySet()) {
					pw.println("\t\t\t\t<input name=\"" + s + "\">");
					pw.println("\t\t\t\t<value>" + tc.getInputData().get(s) + "</value>");
					pw.println("\t\t\t\t</input>");
				}
				pw.println("\t\t\t</inputs>");
				pw.println("\t\t\t<outputs/>");
				pw.println("\t\t</testcase>");
				++ cnt;
			}
			pw.println("\t</function>");
			pw.println("</root>");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
