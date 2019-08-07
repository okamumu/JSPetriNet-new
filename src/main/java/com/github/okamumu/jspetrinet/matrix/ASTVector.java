package com.github.okamumu.jspetrinet.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.FailToConvertObject;

public class ASTVector {

	private final int size;
	private final List<AST> elems;
	
	public ASTVector(int size) {
		this.size = size;
		elems = new ArrayList<AST>(size);
		for (int i=0; i<size; i++) {
			elems.add(ASTValue.getAST(0));
		}
	}
	
	public final int getSize() {
		return size;
	}
	
	public final AST get(Integer i) {
		return elems.get(i);
	}

	public final void set(Integer i, AST value) {
		elems.set(i,  value);
	}

	public final double[] getValue(ASTEnv env) throws ASTException {
		double[] result = new double [elems.size()];
		Iterator<AST> iter = elems.iterator();
		for (int i=0; i<elems.size(); i++) {
			Object obj = iter.next().eval(env);
			if (obj instanceof Integer) {
				result[i] = (Integer) obj;
			} else if (obj instanceof Double) {
				result[i] = (Double) obj;
			} else {
				throw new FailToConvertObject("Fail to convert an object in ASTVector");
			}
		}
		return result;
	}
}
