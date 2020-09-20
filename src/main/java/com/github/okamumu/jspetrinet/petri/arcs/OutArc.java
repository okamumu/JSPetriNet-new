package com.github.okamumu.jspetrinet.petri.arcs;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for outbound arc (connect from transition to place)
 *
 */

public final class OutArc extends ArcBase {

	/**
	 * Constructor 
	 * @param src An object of Trans
	 * @param dest An object of Place
	 * @param multi An object of multiple function
	 */

	public OutArc(Trans src, Place dest, AST multi) {
		super(src, dest, multi);
	}

}
