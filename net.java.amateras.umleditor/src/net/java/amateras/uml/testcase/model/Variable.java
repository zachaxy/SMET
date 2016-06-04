package net.java.amateras.uml.testcase.model;

public class Variable {

	private String name;
	private VariableType type;
	private String domain;
	private boolean isInput;
	
	public Variable(String n, String t, String d) {
		name = n;
		
		if (t.equals("double")) {
			type = VariableType.DOUBLE;
		} else if (t.equals("boolean")) {
			type = VariableType.BOOLEAN;
		} else if (t.equals("int8")) {
			type = VariableType.INT8;
		} else if (t.equals("uint8")) {
			type = VariableType.UINT8;
		} else if (t.equals("int16")) {
			type = VariableType.INT16;
		}
		
		String left = "(";
		String right = ")";
		d = d.replaceAll("\\s", "");
        if(d == ""){

        }else{
            for (String s : d.split("&&")) {
                boolean strictlyThan = !s.contains("=");
                boolean greaterThan = s.contains(">");
                String num = s.split("(>=?)|(<=?)")[1];
                if (num.equals(n)) {
                    num = s.split("(>=?)|(<=?)")[0];
                    greaterThan = !greaterThan;
                }
                num = num.substring(6);
                if (greaterThan && strictlyThan) {
                    left = "(" + num;
                } else if (greaterThan && !strictlyThan) {
                    left = "[" + num;
                } else if (!greaterThan && strictlyThan) {
                    right = num + ")";
                } else if (!greaterThan && !strictlyThan) {
                    right = num + "]";
                }
            }
            domain = left + "," + right;
        }

		
		//isInput = false;
	}
	
	public Variable(String n, String t, String d, boolean i) {
		this(n, t, d);
		isInput = i;
	}
	
	public void setIsInput(boolean i) {
		isInput = i;
	}
	
	public boolean isInput() {
		return isInput;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public VariableType getType() {
		return type;
	}
}
