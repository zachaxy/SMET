package net.java.amateras.uml.testcase.io;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.java.amateras.uml.testcase.model.Automaton;

public class Parser {
	
	private String fileName;
	
	public Parser(String fn) {
		fileName = fn;
	}
	
	public Automaton parseXML() {
		Automaton automaton = new Automaton();
        try {
            String path = "E:\\JavaSrc\\XML\\Testeia.txt";
            File inputFile = new File(path);
            SAXReader reader = new SAXReader();
            org.dom4j.Document document = reader.read(inputFile);

            Element root = document.getRootElement();
            System.out.println("Root element :"
                    + root.attribute("initial").getText());


            Element params = root.element("parameterlist");
            System.out.println(params.getName());
            List<Element> param = params.elements("parameter");
            System.out.println("----------------------------");
            System.out.println("参数的个数"+param.size());

            for (Element node : param) {
                String domain = node.attribute(0).getText();
                String name = node.attribute(3).getText();
                String type = node.attribute(2).getText();
                boolean isInput = node.attribute(1).getText().equals("输入变量");
                automaton.addVariable(name, type, domain, isInput);
                System.out.println(name+":"+type+":"+domain+":"+isInput);

            }

            List<Element> states = root.elements("state");
            System.out.println("状态数："+states.size());
            String initialName = root.attribute("initial").getText();


            for (Element e:states){
                String source = e.attribute(0).getText();
                automaton.addState(source);
            }

            for (Element e:states){
                String source = e.attribute(0).getText();

                List<Element> transitions = e.elements("transition");
                for (Element tran:transitions){
                    String cond = "(true)";
                    if(tran.attribute(0).getText()!=""){
                        cond = tran.attribute(0).getText();
                        cond = regular(cond);
                    }
                    Vector<String> updates = new Vector();
                    String action = tran.attribute(1).getText();
                    String[] as = action.split("=");
                    for (int i = 0; i < as.length; i++) {
                        updates.add(as[i]);
                    }

                    String target = tran.attribute(2).getText();
                    //automaton.addState(target);
                    System.out.println(source+"    "+cond+"    "+action+"  "+target);

                    automaton.addTransition(source, target, cond, updates);
                }
            }
            automaton.setInitialState(initialName);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

		return automaton;
	}
	
	private String regular(String s) {
		boolean found = false;
		Matcher m = Pattern.compile("(&&)|(\\|\\|)").matcher(s);
		if (m.find()) {
			found = true;
		} else {
			m = Pattern.compile("(==)|(>=)|(<=)|(~=)|(>(?!=))|(<(?!=))").matcher(s);
			if (m.find()) {
				found = true;
			} else {
				m = Pattern.compile("(\\+)|(?<!^)(-)|(\\*)").matcher(s);
				if (m.find()) {
					found = true;
				}
			}
		}
		
		if (found) {
			String op = s.substring(m.start(), m.end());
			return "(" + op + regular(s.substring(0, m.start())) + regular(s.substring(m.end())) + ")";
		} else if (s.startsWith("!")) {
			return "(not" + regular(s.substring(1)) + ")";
		} else if (s.startsWith("-")) {
			return "(opp" + regular(s.substring(1)) + ")";
		} else {
			return "(" + s + ")";
		}
	}

    public static void main(String[] args) {
        new Parser("暂时为空吧").parseXML();
    }
}
