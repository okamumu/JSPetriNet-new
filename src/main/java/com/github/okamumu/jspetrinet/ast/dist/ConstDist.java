package com.github.okamumu.jspetrinet.ast.dist;

import com.github.okamumu.jmtrandom.Random;
import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;

public class ConstDist extends Dist {
	
	public static final String dname = "det";

	private AST constValue;
	private Object constValueObj;

	public ConstDist(AST constValue) {
		super();
		this.constValue = constValue;
	}

	public final AST getConstValue() {
		return constValue;
	}

	public final void setParam(AST constValue) {
		this.constValue = constValue;
	}

	@Override
	public String toString() {
		return dname + "(" + constValueObj + ")";
	}

	@Override
	public void updateObj(ASTEnv env) throws ASTException {
		constValueObj = constValue.eval(env);
	}

	@Override
	public double nextImpl(Random rnd) throws InvalidOperation {
		return convertObjctToDouble(constValueObj);
	}
}
