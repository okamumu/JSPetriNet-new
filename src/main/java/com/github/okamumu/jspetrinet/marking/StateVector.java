package com.github.okamumu.jspetrinet.marking;

import java.util.Arrays;

import com.github.okamumu.jspetrinet.graph.Node;

/**
 * A class for a state vector
 *
 */

public class StateVector extends Node implements Comparable<StateVector> {

	private final int[] vec;
	private final int hash;

	/**
	 * Constructor
	 * @param vec An instance of state vector
	 */

	public StateVector(int[] vec) {
		this.vec = vec;
		this.hash = Arrays.hashCode(vec);
	}

	/**
	 * Get a copied state vector
	 * @return An instance of state vector
	 */

	public final int[] copy() {
		return Arrays.copyOf(vec, vec.length);
	}

	/**
	 * Getter for a value of one state
	 * @param i An integer to represent a coordinate
	 * @return An integer to represent a state
	 */

	public final int get(int i) {
		return vec[i];
	}
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateVector other = (StateVector) obj;
		if (!Arrays.equals(vec, other.vec))
			return false;
		return true;
	}

	@Override
	public int compareTo(StateVector other) {
		if (vec.length < other.vec.length) {
			return -1;
		}
		if (vec.length > other.vec.length) {
			return 1;
		}
		for (int i=vec.length-1; i>=0; i--) {
			if (vec[i] < other.vec[i]) {
				return -1;
			}
			if (vec[i] > other.vec[i]) {
				return 1;
			}
		}
		return 0;
	}
}
