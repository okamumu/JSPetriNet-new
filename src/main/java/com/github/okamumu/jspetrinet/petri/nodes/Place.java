package com.github.okamumu.jspetrinet.petri.nodes;

import com.github.okamumu.jspetrinet.graph.Node;

/**
 * A class for place node in Petri nets
 *
 */

public final class Place extends Node {
	
	private final String label;
	private final int index;
	private final int max;

	/**
	 * Constructor
	 * @param label A name of place
	 * @param index An integer indicating the index of mark vector
	 * @param max An integer indicating the number of maximum tokens
	 */

	public Place(String label, int index, int max) {
		this.label = label;
		this.index = index;
		this.max = max;
	}

	/**
	 * Getter for the label
	 * @return A string of label
	 */

	public final String getLabel() {
		return label;
	}
	
	/**
	 * Getter for the index
	 * @return An integer for the index
	 */

	public final int getIndex() {
		return index;
	}

	/**
	 * Getter for the maximum number of tokens
	 * @return An integer for the maximum number of tokens
	 */

	public final int getMax() {
		return max;
	}
	
	@Override
	public String toString() {
		return label + index;
	}
}
