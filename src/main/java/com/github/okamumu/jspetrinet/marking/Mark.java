package com.github.okamumu.jspetrinet.marking;

import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
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

	/**
	 * Constructor
	 * @param vec An object of array of int (state vector)
	 */

	public Mark(int[] vec) {
		super(vec);
	}

	/**
	 * A method to get a string of marking
	 * @param net An instance of Net
	 * @return A string of mark
	 */

	public String getString(Net net) {
		String result = "";
		for (Place p : net.getPlaceSet()) {
			if (this.get(p.getIndex()) != 0) {
				if (result.equals("")) {
					result = p.getLabel() + ":" + this.get(p.getIndex());						
				} else {
					result = result + "," + p.getLabel() + ":" + this.get(p.getIndex());						
				}
			}
		}
		return result;
	}
}
