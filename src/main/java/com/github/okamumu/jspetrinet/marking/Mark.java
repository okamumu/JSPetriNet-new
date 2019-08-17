package com.github.okamumu.jspetrinet.marking;

import com.github.okamumu.jspetrinet.graph.Node;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for a mark
 *
 */

public final class Mark extends StateVector {

	/**
	 * A class for an arc between marks
	 *
	 */
	public class Arc extends com.github.okamumu.jspetrinet.graph.Arc {
		private final Trans trans;

		/**
		 * Constructor
		 * @param src An object of source mark
		 * @param dest An object of destination mark
		 * @param tr An object of transition
		 */
		public Arc(Mark src, Mark dest, Trans tr) {
			super(src, dest);
			this.trans = tr;
		}
		
		/**
		 * Getter for the transition
		 * @return An object of Trans
		 */
		public final Trans getTrans() {
			return trans;
		}
	}

//	private final StateVector vec;

	/**
	 * Constructor
	 * @param vec An object of array of int (state vector)
	 */
	public Mark(int[] vec) {
		super(vec);
	}
	
//	public int get(int i) {
//		return vec.get(i);
//	}
//
//	public int[] copy() {
//		return vec.copy();
//	}
//
//	@Override
//	public int compareTo(Mark o) {
//		return vec.compareTo(o.vec);
//	}
}
