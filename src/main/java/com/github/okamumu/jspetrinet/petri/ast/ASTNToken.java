package com.github.okamumu.jspetrinet.petri.ast;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.PetriAnalysis;
import com.github.okamumu.jspetrinet.petri.nodes.Place;

/**
 * A class for the number of tokens
 *
 */

public class ASTNToken implements AST {

	private final String label;
	private final PetriAnalysis analysis;

	/**
	 * Constructor
	 * @param label A name of place to get the number of tokens
	 */

	public ASTNToken(String label) {
		this.label = label;
		this.analysis = PetriAnalysis.getInstance();
	}

	/**
	 * Getter for the label
	 * @return A string
	 */
	public String getLabel() {
		return label;
	}
	
	@Override
	public Object eval(ASTEnv env) throws ASTException {
		try {
			Object p = env.get(label);
			if (p instanceof Place) {
				Place place = (Place) p;
				return analysis.getNToken(env, place);
			} else {
				throw new InvalidOperation("NToken error");
			}
		} catch (ObjectNotFoundInASTEnv e) {
			return new NaN(this);
		}
	}
	
	@Override
	public String toString() {
		return "#"+label;
	}
}
