package net.java.amateras.uml.testcase.util;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	public static int findMatchedBracketPos(String s, int left) {
		int cnt = 1;
		for (int i = left + 1; i < s.length(); ++ i) {
			if (s.charAt(i) == '(') {
				++ cnt;
			} else if (s.charAt(i) == ')') {
				-- cnt;
			}
			if (cnt == 0) {
				return i;
			}
		}
		return -1;
	}
	
	public static String addInitConstraint(String s){
        //String regex = "(&&)|(\\|\\|)|(==)|(>=)|(<=)|(~=)|(>(?!=))|(<(?!=))|(\\+)|(?<!^)(-)|(\\*)";
        String regex = "(&&)|(\\|\\|)|(==)|(>=)|(<=)|(~=)|(>(?!=))|(<(?!=))";
        Matcher m = Pattern.compile(regex).matcher(s);
        if(m.find()){
            return s;
        }else {
            /*s = "(=="+s+"(1))";
            return s;*/
            return null;
        }
    }
	
	public static String expRegular(String s) {
        Stack<String> vars = new Stack<String>();
        Stack<Character> ops = new Stack<Character>();
        String op = "+-*/";
        String tmp = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                //执行变换
                if (tmp != "") {
                    System.out.println("得到一个变量->" + tmp);
                    tmp = "(" + tmp + ")";
                    vars.push(tmp);
                }
                if (ops.peek() != '(') {
                    System.out.println("遇到了),执行变换");
                    tmp = "(" + ops.pop() + vars.pop() + vars.pop() + ")";
                    System.out.println("变换得到的tmp是" + tmp);
                    vars.push(tmp);
                    ops.pop();
                    tmp = "";
                }
            } else if (op.indexOf(c) >= 0) {
                //这一步得到的是一个操作符.
                if (tmp != "") {
                    System.out.println("得到一个变量->" + tmp);
                    tmp = "(" + tmp + ")";
                    vars.push(tmp);
                    tmp = "";
                }
                if ((c == '+' || c == '-') && (ops.peek() != '(')) {
                    tmp = "(" + ops.pop() + vars.pop() + vars.pop() + ")";
                    System.out.println("变换得到的tmp是" + tmp);
                    vars.push(tmp);
                    tmp = "";
                }
                ops.push(c);
                System.out.println("得到一个操作符->" + c);
            } else {
                //这一步得到的是变量名
                tmp = tmp + c;
            }
        }
        s = vars.pop();
        return s;
    }
	
	
}
