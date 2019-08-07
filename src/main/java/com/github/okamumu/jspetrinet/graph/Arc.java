package com.github.okamumu.jspetrinet.graph;

import java.util.Objects;

/**
 * A class for arcs
 *
 */
public class Arc {
	
	private final Node src;
	private final Node dest;

	/**
	 * Constructor. This has a side effect on src and dest nodes.
	 * @param src An object of source node
	 * @param dest An object of destination node
	 */
	public Arc(Node src, Node dest) {
		this.src = src;
		this.dest = dest;
		src.addOutArc(this);
		dest.addInArc(this);
	}

	/**
	 * Getter for a source node
	 * @return An object of node
	 */
	public final Node getSrc() {
		return src;
	}
	
	/**
	 * Getter for a destination node
	 * @return An object of node
	 */
	public final Node getDest() {
		return dest;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dest, src);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arc other = (Arc) obj;
		return Objects.equals(dest, other.dest) && Objects.equals(src, other.src);
	}

}
