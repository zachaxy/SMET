package net.java.amateras.uml.testcase.util;

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
}
