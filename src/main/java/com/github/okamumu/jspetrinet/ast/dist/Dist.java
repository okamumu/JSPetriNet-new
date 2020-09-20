package com.github.okamumu.jspetrinet.ast.dist;

import com.github.okamumu.jmtrandom.Random;
import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;

abstract public class Dist implements AST {
	
	private final ValueChange vc;
	
	public Dist() {
		vc = ValueChange.getInstance();
	}

	@Override
	public final Object eval(ASTEnv env) throws ASTException {
		updateObj(env);
		return this;
	}

	public final double next(ASTEnv env, Random rnd) throws ASTException {
		this.eval(env);
		return nextImpl(rnd);
	}

	public double convertObjctToDouble(Object obj) throws InvalidOperation {
		return vc.convertObjctToDouble(obj);
	}

	abstract public void updateObj(ASTEnv env) throws ASTException;
	abstract public double nextImpl(Random rnd) throws ASTException;

}
