package com.github.okamumu.jspetrinet.petri.arcs;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for inhibited arc
 *
 */

public final class InhibitArc extends ArcBase {

	/**
	 * Constructor
	 * @param src An object of Place
	 * @param dest An object of Trans
	 * @param multi An object of AST to represent the multiplicity
	 */

	public InhibitArc(Place src, Trans dest, AST multi) {
		super(src, dest, multi);
	}

}
