package com.github.okamumu.jspetrinet.petri.arcs;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for inbound arc (connect from place to transition)
 *
 */

public class InArc extends ArcBase {

	/**
	 * Constructor 
	 * @param src An object of Place
	 * @param dest An object of Trans
	 * @param multi An object of multiple function
	 */

	public InArc(Place src, Trans dest, AST multi) {
		super(src, dest, multi);
	}

}
