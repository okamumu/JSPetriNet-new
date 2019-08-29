package com.github.okamumu.jspetrinet.marking;

import java.util.Objects;

/**
 * A class for state vector of general transitions
 *
 */

public class GenVec extends StateVector {

	/**
	 * A class for an arc between genvec
	 *
	 */
	public class Arc extends com.github.okamumu.jspetrinet.graph.Arc {
		
		/**
		 * Constructor
		 * @param src An object of source mark
		 * @param dest An object of destination mark
		 */

		public Arc(GenVec src, GenVec dest) {
			super(src, dest);
		}
	}

	public enum Type {
		IMM,
		GEN,
		ABS,
	}
	
	private final Type type;
//	private final StateVector vec;

	/**
	 * Constructor
	 * @param vec A state vector to represent conditions of transitions
	 * @param type An enum to represent the group belongs to IMM, GEN and ABS.
	 */
	public GenVec(int[] vec, Type type) {
		super(vec);
		this.type = type;
	}

	/**
	 * Getter for an enum
	 * @return A enum value
	 */
	public Type getType() {
		return type;
	}

//	public final int get(int i) {
//		return vec.get(i);
//	}
//
//	public int[] copy() {
//		return vec.copy();
//	}

	/**
	 * A method to get a string
	 * @param net An instance of Net
	 * @return A string
	 */
//	public String getString(Net net) {
//		String result = "(";
//		for (GenTrans t: net.getGenTransSet()) {
//			switch(this.get(t.getIndex())) {
//			case 0:
//				break;
//			case 1:
//				if (!result.equals("(")) {
//					result += " ";
//				}
//				result += t.getLabel() + "->enable";
//				break;
//			case 2:
//				if (!result.equals("(")) {
//					result += " ";
//				}
//				result += t.getLabel() + "->preemption";
//				break;
//			default:
//				break;
//			}
//		}
//		if (result.equals("(")) {
//			result += "EXP";
//		}
//		result += ")" + type.toString();		
//		return result;
//	}
	
	public boolean isSameClass(GenVec other) {
		if (this == other) {
			return true;
		}
		if (super.equals(other))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenVec other = (GenVec) obj;
		return type == other.type;
	}
}
