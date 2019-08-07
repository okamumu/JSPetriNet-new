package com.github.okamumu.jspetrinet.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * A class of Node in a graph
 *
 */

public class Node {

	private final List<Arc> inarc;
	private final List<Arc> outarc;

	/**
	 * Constructor
	 */

	public Node() {
		inarc = new ArrayList<Arc>();
		outarc = new ArrayList<Arc>();
	}
	
	/**
	 * Getter for a list of inbound arcs
	 * @return A list of arcs
	 */

	public final List<Arc> getInArc() {
		return inarc;
	}

	/**
	 * Getter for a list of outbound arcs
	 * @return A list of arcs
	 */

	public final List<Arc> getOutArc() {
		return outarc;
	}

	/**
	 * Add an inbound arc
	 * @param arc An object of arc
	 */

	public final void addInArc(Arc arc) {
		inarc.add(arc);
	}

	/**
	 * Add an outbound arc
	 * @param arc An object of arc
	 */

	public final void addOutArc(Arc arc) {
		outarc.add(arc);
	}

}
