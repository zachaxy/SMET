package net.java.amateras.uml.testcase.model;

import choco.Choco;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.Variable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.model.variables.real.RealVariable;

public class VarR {

    private String name;
    private VarRType type;
    private String domain;
    private boolean isInput;
    private Constraint[] constraints;
    private String[] cons;
    //-----------------------
    public Variable variable;
    public Constraint constraint = Choco.TRUE;

    public VarR(String n, String t, String d) {
        name = n;
        domain = d.replaceAll("\\s", "");


        //TODO:添加String类型的判断.
        if (t.equals("double")) {
            type = VarRType.DOUBLE;
            variable = Choco.makeRealVar(n, -21474836.0, 21474836.0);

        } else if (t.equals("boolean")) {
            type = VarRType.BOOLEAN;
            variable = Choco.makeBooleanVar(n);
        } else if (t.equals("int")) {
            type = VarRType.INT;
            //[警告] choco.Choco (checkIntVarBounds) WARNING! Domains over [-21474836, 21474836] are strongly inadvisable !
            variable = Choco.makeIntVar(n, -21474836, 21474836);

        }

        if(domain!=""){
            constraint = getConstraint();
        }

        //提取变量的约束
        /*String left = "(";
        String right = ")";*/

       /* if (d == "") {
            constraint = Choco.TRUE;
        } else {
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
            if (d.contains("||")) {

            } else {

            }
        }*/


        //isInput = false;
    }

    public VarR(String n, String t, String d, boolean i) {
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

    public VarRType getType() {
        return type;
    }

    public Constraint getConstraint() {
        cons = domain.split("\\|\\|");
        constraints = new Constraint[cons.length];
        for (int i = 0; i < cons.length; i++) {
            /*constraint = Choco.or(constraint, parseSingleDomain(cons[i]));
            constraint = Choco.or(constraints);*/
            constraints[i] = parseSingleDomain(cons[i]);
        }
        constraint = Choco.or(constraints);
        return constraint;
    }

    private Constraint parseSingleDomain(String s) {
        String[] bounds = s.substring(1, s.length() - 1).split(",");
        Constraint c;
        Constraint c1 = null;
        Constraint c2 = null;
        if (type == VarRType.INT) {
            int low = Integer.valueOf(bounds[0]);
            int upp = Integer.valueOf(bounds[1]);
            if (s.startsWith("(")) {
                c1 = Choco.gt((IntegerVariable) variable, low);
            } else {
                c1 = Choco.geq((IntegerVariable) variable, low);
            }

            if (s.endsWith(")")) {
                c2 = Choco.lt((IntegerVariable) variable, upp);
            } else {
                c2 = Choco.leq((IntegerVariable) variable, upp);
            }
        } else if (type == VarRType.DOUBLE) {
            //TODO:无法解决实数的边缘问题!!!
            double low = Integer.valueOf(bounds[0]);
            double upp = Integer.valueOf(bounds[1]);
            if (s.startsWith("(")) {
                c1 = (Choco.geq((RealVariable) variable, low));

            } else {
                c1 = Choco.geq((RealVariable) variable, low);
            }

            if (s.endsWith(")")) {
                c2 = Choco.leq((RealVariable) variable, upp);
            } else {
                c2 = Choco.leq((RealVariable) variable, upp);
            }
        }
        c = Choco.and(c1, c2);
       // constraints[0] = c;
        return c;
    }
}
