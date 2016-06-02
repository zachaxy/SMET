package net.java.amateras.uml.testcase.tcg;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;


import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.cp.solver.search.integer.branching.AssignVar;
import choco.cp.solver.search.integer.varselector.MinDomain;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.Variable;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.model.variables.real.RealExpressionVariable;
import choco.kernel.model.variables.real.RealVariable;
import net.java.amateras.uml.testcase.model.VariableType;
import net.java.amateras.uml.testcase.util.Utility;

public class Solver {

	private HashMap<String, Variable> variables;
	private Constraint constraint;

	public Solver() {
		variables = new HashMap<String, Variable>();
	}

	public void setVariables(Collection<net.java.amateras.uml.testcase.model.Variable> vars) {
		for (net.java.amateras.uml.testcase.model.Variable v : vars) {
            if(!v.isInput()){
                continue;
            }
			if (v.getType() == VariableType.BOOLEAN) {
				variables.put(v.getName(), Choco.makeBooleanVar(v.getName()));
			} else if (v.getType() == VariableType.DOUBLE) {
				double inf = Double.MIN_VALUE;
				double sup = Double.MAX_VALUE;
				String domain[] = v.getDomain().replaceAll("\\(|\\)|\\[|\\]", "").split(",");
				if (!domain[0].equals("")) {
					inf = Math.max(inf, Double.parseDouble(domain[0]));
				}
				if (!domain[1].equals("")) {
					sup = Math.min(sup, Double.parseDouble(domain[1]));
				}
				variables.put(v.getName(), Choco.makeRealVar(v.getName(), inf, sup));
			} else if (v.getType() == VariableType.INT8 || v.getType() == VariableType.UINT8 || v.getType() == VariableType.INT16) {
				int inf = -32768;
				int sup = 32767;
				if (v.getType() == VariableType.INT8) {
					inf = -128;
					sup = 127;
				} else if (v.getType() == VariableType.UINT8) {
					inf = 0;
					sup = 255;
				}

                if(v.getDomain()!= null){
                    System.out.println("暂时不会到这里");
                    String domain[] = v.getDomain().replaceAll("\\(|\\)|\\[|\\]", "").split(",");
                    if (!domain[0].equals("")) {
                        inf = Math.max(inf, Integer.parseInt(domain[0]));
                    }
                    if (!domain[1].equals("")) {
                        sup = Math.min(sup, Integer.parseInt(domain[1]));
                    }
                }

				variables.put(v.getName(), Choco.makeIntVar(v.getName(), inf, sup));
			}
		}
	}

	public void setConstraint(String s) {
		constraint = parseConstraint(s);
	}

	public HashMap<String, String> solve() {
		CPModel m = new CPModel();
		for (Variable var : variables.values()) {
			m.addVariable(var);
		}
		m.addConstraint(constraint);

		CPSolver solver = new CPSolver();
		solver.read(m);
		solver.addGoal(new AssignVar(new MinDomain(solver), new RandomDomain()));
		solver.solve();

		if (solver.existsSolution()) {
			HashMap<String, String> result = new HashMap<String, String>();
			for (String s : variables.keySet()) {
				Variable var = variables.get(s);
				if (var instanceof IntegerVariable) {
					result.put(s, "" + solver.getVar((IntegerVariable)var).getVal());
                    //System.out.println(s+"->"+solver.getVar((IntegerVariable)var).getVal());
                } else if (var instanceof RealVariable) {
					result.put(s, "" + getRandomValInDomain(solver.getVar((RealVariable)var).getInf(), solver.getVar((RealVariable)var).getSup()));
				}
			}
			return result;
		} else {
			return null;
		}
	}

	private double getRandomValInDomain(double inf, double sup) {
		double range = sup - inf;
		int part = new Random().nextInt(9) + 1;
		return range * 10 / part;
	}

	private Constraint parseConstraint(String s) {
		if (Pattern.compile("^\\((&&)|(\\|\\|)").matcher(s).find()) {
			/*int op1Left = 3;
			int op1Right = Utility.findMatchedBracketPos(s, op1Left) + 1;
			int op2Left = op1Right;
			int op2Right = s.length() - 1;
			Constraint op1 = Choco.TRUE;
			choco.kernel.model.constraints.Constraint op2 = Choco.TRUE;
			if (!s.substring(op1Left, op1Right).equals("(true)")) {
				op1 = parseConstraint(s.substring(op1Left, op1Right));
			}
			if (!s.substring(op2Left, op2Right).equals("(true)")) {
				op2 = parseConstraint(s.substring(op2Left, op2Right));
			}
			if (s.startsWith("(&&")) {
				return Choco.and(op1, op2);
			} else if (s.startsWith("(||")) {
				return Choco.or(op1, op2);
			}*/
            System.out.println("暂时不支持的功能");
            return null;
        } else if (Pattern.compile("^\\(not").matcher(s).find()) {
			/*int opLeft = 4;
			int opRight = s.length() - 1;
			Constraint op = Choco.TRUE;
			if (!s.substring(opLeft, opRight).equals("(true)")) {
				op = parseConstraint(s.substring(opLeft, opRight));
			}
			return Choco.not(op);*/
            System.out.println("暂时不支持的功能");
            return null;
		} else if (Pattern.compile("^\\((==)|(>=)|(<=)|(~=)|(>(?!=))|(<(?!=))").matcher(s).find()) {
			int op1Left = 3;
			if (s.charAt(op1Left) != '(') {
				op1Left = 2;
			}
			int op1Right = Utility.findMatchedBracketPos(s, op1Left) + 1;
			int op2Left = op1Right;
			int op2Right = s.length() - 1;
			Variable op1 = parseExpression(s.substring(op1Left, op1Right));
			Variable op2 = parseExpression(s.substring(op2Left, op2Right));
			if (op1 instanceof IntegerExpressionVariable && op2 instanceof IntegerExpressionVariable) {
				if (s.startsWith("(==")) {
					return Choco.eq((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(>=")) {
					return Choco.geq((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(<=")) {
					return Choco.leq((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(~=")) {
					return Choco.neq((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(>")) {
					return Choco.gt((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(<")) {
					return Choco.lt((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				}
			} else if (op1 instanceof RealExpressionVariable && op2 instanceof RealExpressionVariable) {
				if (s.startsWith("(==")) {
					return Choco.eq((RealExpressionVariable)op1, (RealExpressionVariable)op2);
				} else if (s.startsWith("(>=")) {
					return Choco.geq((RealExpressionVariable)op1, (RealExpressionVariable)op2);
				} else if (s.startsWith("(<=")) {
					return Choco.leq((RealExpressionVariable)op1, (RealExpressionVariable)op2);
				} else if (s.startsWith("(~=")) {
					return Choco.not(Choco.eq((RealExpressionVariable)op1, (RealExpressionVariable)op2));
				} else if (s.startsWith("(>")) {
					return Choco.not(Choco.leq((RealExpressionVariable)op1, (RealExpressionVariable)op2));
				} else if (s.startsWith("(<")) {
					return Choco.not(Choco.geq((RealExpressionVariable)op1, (RealExpressionVariable)op2));
				}
			}
		}
		return null;
	}

	private Variable parseExpression(String s) {
		if (Pattern.compile("^\\((\\+)|(-)|(\\*)").matcher(s).find()) {
			int op1Left = 2;
			int op1Right = Utility.findMatchedBracketPos(s, op1Left) + 1;
			int op2Left = op1Right;
			int op2Right = s.length() - 1;
			Variable op1 = parseExpression(s.substring(op1Left, op1Right));
			Variable op2 = parseExpression(s.substring(op2Left, op2Right));
			if (op1 instanceof IntegerExpressionVariable && op2 instanceof IntegerExpressionVariable) {
				if (s.startsWith("(+")) {
					return Choco.plus((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(-")) {
					return Choco.minus((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				} else if (s.startsWith("(*")) {
					return Choco.mult((IntegerExpressionVariable)op1, (IntegerExpressionVariable)op2);
				}
			} else if (op1 instanceof RealExpressionVariable && op2 instanceof RealExpressionVariable) {
				if (s.startsWith("(+")) {
					return Choco.plus((RealExpressionVariable)op1, (RealExpressionVariable)op2);
				} else if (s.startsWith("(-")) {
					return Choco.minus((RealExpressionVariable)op1, (RealExpressionVariable)op2);
				} else if (s.startsWith("(*")) {
					return Choco.mult((RealExpressionVariable)op1, (RealExpressionVariable)op2);
				}
			}
		} else if (s.startsWith("(opp")) {
			int opLeft = 4;
			int opRight = s.length() - 1;
			Variable op = parseExpression(s.substring(opLeft, opRight));
			if (op instanceof IntegerExpressionVariable) {
				return Choco.minus(0, (IntegerExpressionVariable)op);
			} else if (op instanceof RealExpressionVariable) {
				return Choco.minus(0, (RealExpressionVariable)op);
			}
		} else if (s.startsWith("(ifthenelse")) {
			int op1Left = 11;
			int op1Right = Utility.findMatchedBracketPos(s, op1Left) + 1;
			int op2Left = op1Right;
			int op2Right = Utility.findMatchedBracketPos(s, op2Left) + 1;
			int op3Left = op2Right;
			int op3Right = s.length() - 1;
			Constraint op1 = parseConstraint(s.substring(op1Left, op1Right));
			Variable op2 = parseExpression(s.substring(op2Left, op2Right));
			Variable op3 = parseExpression(s.substring(op3Left, op3Right));
			if (op2 instanceof IntegerExpressionVariable && op3 instanceof IntegerExpressionVariable) {
				return Choco.ifThenElse(op1, (IntegerExpressionVariable)op2, (IntegerExpressionVariable)op3);
			}
		} else {
			String varStr = s.substring(1, s.length() - 1);
			try {
				int i = Integer.parseInt(varStr);
				return Choco.constant(i);
			}  catch (NumberFormatException e) {
			}
			try {
				double d = Double.parseDouble(varStr);
				return Choco.constant(d);
			}  catch (NumberFormatException e) {
			}
			Variable var = variables.get(varStr);
			if (var instanceof IntegerVariable) {
				return (IntegerVariable)var;
			} else if (var instanceof RealVariable) {
				return (RealVariable)var;
			}
		}
		return null;
	}
}
