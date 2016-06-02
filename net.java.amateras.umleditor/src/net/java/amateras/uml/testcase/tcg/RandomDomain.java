package net.java.amateras.uml.testcase.tcg;

import choco.kernel.solver.search.ValIterator;
import choco.kernel.solver.variables.integer.IntDomainVar;

public class RandomDomain implements ValIterator<IntDomainVar> {

	int domain[];
	int size;
	
	 
	public int getFirstVal(IntDomainVar x) {
		
		size = x.getSup() - x.getInf() + 1;
		domain = new int[size];
		domain[0] = x.getInf();
		for (int i = 1; i < size; i ++) {
			domain[i] = domain[i - 1] + 1;
		}
		
		int sel = (int) (Math.random() * size);
		size --;
		int tmp = domain[sel];
		domain[sel] = domain[size];
		domain[size] = tmp;
		
		return domain[size];
	}

 
	public int getNextVal(IntDomainVar x, int i) {
		
		int sel = (int) (Math.random() * size);
		size --;
		int tmp = domain[sel];
		domain[sel] = domain[size];
		domain[size] = tmp;
		
		return domain[size];
	}

	 
	public boolean hasNextVal(IntDomainVar x, int i) {
		
		return (size > 0);
	}

}
