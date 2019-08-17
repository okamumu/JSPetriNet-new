package com.github.okamumu.jspetrinet.petri.nodes;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.graph.Node;

/**
 * An abstract class for a transition node in Petri net
 *
 */

abstract public class Trans extends Node implements Comparable<Trans> {

	/**
	 * Enum class to represent the status of transition
	 *
	 */

	public enum Status {
		/**
		 * The transition is disabled
		 */
		DISABLE(0),
		ENABLE(1),
		PREEMPTION(2);
		
		private final int value;

		/**
		 * Constructor
		 * @param value An integer to indicate the status of transition.
		 * 0 is disabled, 1 is enabled and 2 is preempted
		 */

		Status(int value) {
			this.value = value;
		}
		
		public final int getIntValue() {
			return value;
		}
	}

	private final String label;
	private final int index;
	private final AST guard;
	private final AST update;
	private final int priority;
	private final boolean vanishable;

	/**
	 * Constructor
	 * @param label A name of transitions
	 * @param index An integer for ID
	 * @param guard An object of AST for the guard function
	 * @param update An object of AST for the (additional) update function
	 * @param priority An integer for the priority
	 * @param vanishable A logical value to represent the transition can be vanished
	 */

	public Trans(String label, int index, AST guard, AST update, int priority, boolean vanishable) {
		this.label = label;
		this.index = index;
		this.guard = guard;
		this.update = update;
		this.priority = priority;
		this.vanishable = vanishable;
	}

	/**
	 * Getter for the label
	 * @return A string for the label
	 */

	public final String getLabel() {
		return label;
	}

	/**
	 * Getter for index
	 * @return An integer
	 */

	public final int getIndex() {
		return index;
	}

	/**
	 * Getter for the AST of guard function
	 * @return An object of guard function
	 */

	public final AST getGuard() {
		return guard;
	}

	/**
	 * Getter for the AST of update function
	 * @return An object of update function
	 */

	public final AST getUpdate() {
		return update;
	}

	/**
	 * Getter for the priority
	 * @return An integer
	 */

	public final int getPriority() {
		return priority;
	}

	/**
	 * Check whether the transition is vanishable or not
	 * @return A boolean
	 */

	public final boolean canVanishing() {
		return vanishable;
	}
	
	/**
	 * Evaluate the guard function
	 * @param env An object of environment
	 * @return A boolean
	 * @throws ASTException Fail to get a boolean
	 */

	public final boolean guardEval(ASTEnv env) throws ASTException {
		if (guard == null) {
			return true;
		}
		Object result = guard.eval(env);
		if (result instanceof Boolean) {
			return (Boolean) result;
		} else {
			throw new InvalidOperation("The guard function should return a boolean:" + guard.toString());
		}
	}

	@Override
	public int compareTo(Trans o) {
		int p1 = this.priority;
		int p2 = o.priority;
		if (p1 == p2) {
			boolean v1 = this.vanishable;
			boolean v2 = o.vanishable;
			if (v1 == v2) {
				return 0;
			} else if (v1) {
				return 1;
			} else {
				return -1;
			}
		} else if (p1 < p2) {
			return 1;
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		return label;
	}	
}
