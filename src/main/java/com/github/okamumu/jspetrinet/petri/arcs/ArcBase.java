package com.github.okamumu.jspetrinet.petri.arcs;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperator;
import com.github.okamumu.jspetrinet.graph.Arc;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * An abstract class for Arcs
 *
 */

abstract public class ArcBase extends Arc {

	private final AST multi;

	/**
	 * Constructor 
	 * @param src An object of source
	 * @param dest An object of destination
	 * @param multi An object of multiple function
	 */

	public ArcBase(Place src, Trans dest, AST multi) {
		super(src, dest);
		this.multi = multi;
	}

	/**
	 * Constructor 
	 * @param src An object of source
	 * @param dest An object of destination
	 * @param multi An object of multiple function
	 */

	public ArcBase(Trans src, Place dest, AST multi) {
		super(src, dest);
		this.multi = multi;
	}

	/**
	 * Getter for the multiple function
	 * @return An object of AST as the multiple function
	 */

	public final AST getMulti() {
		return multi;
	}
	
	/**
	 * Get the multiplicity
	 * @param env An object of environment
	 * @return An integer as multiplicity
	 * @throws ASTException A fail to get an integer
	 */

	public final int getMulti(ASTEnv env) throws ASTException {
		Object result = multi.eval(env);
		if (result instanceof Integer) {
			return (Integer) result;
		} else if (result instanceof Double) {
			return ((Double) result).intValue();
		} else {
			throw new InvalidOperator("The multiple function should return either integer or double");
		}
	}
}
