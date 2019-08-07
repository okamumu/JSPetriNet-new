package com.github.okamumu.jspetrinet.petri.ast;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.PetriAnalysis;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for the boolean function to get the enable condition of transition
 *
 */

public class ASTEnableCond implements AST {

	private final String label;
	private final PetriAnalysis analysis;

	/**
	 * Constructor
	 * @param label A name of transition to get the enable condition
	 */

	public ASTEnableCond(String label) {
		this.label = label;
		this.analysis = PetriAnalysis.getInstance();
	}
	
	@Override
	public Object eval(ASTEnv env) throws ASTException {
		try {
			Object t = env.get(label);
			if (t instanceof Trans) {
				Trans trans = (Trans) t;
				return analysis.isEnable(env, trans) == Trans.Status.ENABLE;
			} else {
				throw new InvalidOperation("EnableCond error");
			}
		} catch (ObjectNotFoundInASTEnv e) {
			return new NaN(this);
		}
	}
	
	@Override
	public String toString() {
		return "?" + label;
	}
}
