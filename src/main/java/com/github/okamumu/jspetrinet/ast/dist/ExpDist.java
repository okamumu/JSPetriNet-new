package com.github.okamumu.jspetrinet.ast.dist;

import com.github.okamumu.jmtrandom.Random;
import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;

public class ExpDist extends Dist {
	
	public static final String dname = "expdist";

	private AST rate;
	private Object rateObj;

	public ExpDist(AST rate) {
		super();
		this.rate = rate;
	}
	
	public final AST getRate() {
		return rate;
	}

	public final void setParam(AST rate) {
		this.rate = rate;
	}

	@Override
	public void updateObj(ASTEnv env) throws ASTException {
		rateObj = rate.eval(env);
	}

	@Override
	public String toString() {
		return dname + "(" + rateObj + ")";
	}

	@Override
	public double nextImpl(Random rnd) throws InvalidOperation {
		double rate_value = convertObjctToDouble(rateObj);
		return rnd.nextExp(rate_value);
	}
}
