package com.github.okamumu.jspetrinet.ast.dist;

import com.github.okamumu.jmtrandom.Random;
import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;

public class UnifDist extends Dist {
	
	public static final String dname = "unif";

	private final AST min;
	private final AST max;
	private Object minObj;
	private Object maxObj;

	public UnifDist(AST min, AST max) {
		super();
		this.min = min;
		this.max = max;
	}
	
	public final AST getMin() {
		return min;
	}

	public final AST getMax() {
		return max;
	}
	
	@Override
	public void updateObj(ASTEnv env) throws ASTException {
		minObj = min.eval(env);
		maxObj = max.eval(env);
	}

	@Override
	public String toString() {
		return dname + "(" + minObj + "," + maxObj + ")";
	}

	@Override
	public double nextImpl(Random rnd) throws InvalidOperation  {
		double min_value = convertObjctToDouble(minObj);
		double max_value = convertObjctToDouble(maxObj);
		return rnd.nextUnif(min_value, max_value);
	}
}
