package com.github.okamumu.jspetrinet.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.FailToConvertObject;

public class ASTMatrix {

	class Elem implements Comparable<Elem> {
		private final Integer i;
		private final Integer j;
		private final AST value;
		
		public Elem(Integer i, Integer j, AST value) {
			this.i = i;
			this.j = j;
			this.value = value;
		}
		
		public final Integer getI() {
			return i;
		}
		
		public final Integer getJ() {
			return j;
		}
		
		public final AST getValue() {
			return value;
		}

		@Override
		public int compareTo(Elem o) {
			if (this.j < o.j) {
				return -1;
			} else if (this.j > o.j) {
				return 1;
			} else if (this.i < o.i) {
				return -1;
			} else if (this.i > o.i) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private final int isize;
	private final int jsize;
	private final List<Elem> elems;
	
	public ASTMatrix(int isize, int jsize) {
		this.isize = isize;
		this.jsize = jsize;
		elems = new ArrayList<Elem>();
	}
	
	public final List<Elem> getElements() {
		return elems;
	}
	
	public final int getISize() {
		return isize;
	}
	
	public final int getJSize() {
		return jsize;
	}
	
	public final int getNNZ() {
		return elems.size();
	}

	public final void add(Integer i, Integer j, AST value) {
		elems.add(new Elem(i, j, value));
	}

	// for CSR
	//	public final int[] getI() {
	//		int[] result = new int [isize+1];
	//		for (Elem e : elems) {
	//			result[e.getI()+1]++;
	//		}
	//		for (int i=1; i<=isize; i++) {
	//			result[i] += result[i-1];
	//		}
	//		return result;
	//	}
	//
	//	public final int[] getJ() {
	//	int[] result = new int [elems.size()];
	//	Collections.sort(elems);
	//	Iterator<Elem> iter = elems.iterator();
	//	for (int i=0; i<elems.size(); i++) {
	//		result[i] = iter.next().getJ();
	//	}
	//	return result;
	//}

	// for CSC
	public final int[] getI() {
		int[] result = new int [elems.size()];
		Collections.sort(elems);
		Iterator<Elem> iter = elems.iterator();
		for (int i=0; i<elems.size(); i++) {
			result[i] = iter.next().getI();
		}
		return result;
	}

	public final int[] getJ() {
		int[] result = new int [jsize+1];
		for (Elem e : elems) {
			result[e.getJ()+1]++;
		}
		for (int i=1; i<=jsize; i++) {
			result[i] += result[i-1];
		}
		return result;
	}

	public final double[] getValue(ASTEnv env) throws ASTException {
		double[] result = new double [elems.size()];
		Collections.sort(elems);
		Iterator<Elem> iter = elems.iterator();
		for (int i=0; i<elems.size(); i++) {
			Object obj = iter.next().getValue().eval(env);
			if (obj instanceof Integer) {
				result[i] = (Integer) obj;
			} else if (obj instanceof Double) {
				result[i] = (Double) obj;
			} else {
				throw new FailToConvertObject("Fail to convert an object in ASTMatrix");
			}
		}
		return result;
	}
}
