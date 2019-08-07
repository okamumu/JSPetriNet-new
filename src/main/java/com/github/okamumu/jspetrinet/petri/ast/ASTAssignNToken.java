package com.github.okamumu.jspetrinet.petri.ast;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.PetriAnalysis;
import com.github.okamumu.jspetrinet.petri.nodes.Place;

/**
 * A class for l-value of the number of tokens
 *
 */

public class ASTAssignNToken implements AST {
	
	private final String label;
	private final AST right;
	private final PetriAnalysis analysis;

	/**
	 * Constructor
	 * 
	 * Note: A right value is evaluated by using the current marking in ASTEnv
	 * 
	 * @param label A name of place to assign a right value
	 * @param right An object of AST to represent the value to be assigned
	 */

	public ASTAssignNToken(String label, AST right) {
		this.label = label;
		this.right = right;
		this.analysis = PetriAnalysis.getInstance();
	}

	@Override
	public Object eval(ASTEnv env) throws ASTException {
		Object res = this.right.eval(env);
		
		if (res instanceof NaN) {
			return new NaN(new ASTAssignNToken(label, ASTValue.getAST(res)));
		}
		
		try {
			Object p = env.get(label);
			if (p instanceof Place && res instanceof Integer) {				
				return analysis.updateNToken(env, (Place) p, (Integer) res);
			} else {
				throw new InvalidOperation("Assign NToken error");
			}
		} catch (ObjectNotFoundInASTEnv e) {
			return new NaN(new ASTAssignNToken(label, ASTValue.getAST(res)));
		}
	}

	@Override
	public String toString() {
		return "#"+label + "=" + right.toString();
	}
}
