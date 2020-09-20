package com.github.okamumu.jspetrinet.petri.nodes;

import com.github.okamumu.jspetrinet.ast.AST;

public class GenTrans extends Trans {

	private final AST dist;
	private final Policy policy;

	/**
	 * Enum for a general transition policies
	 * PRI: Preemptive repeat
	 * PRD: Preemptive different
	 * PRS: Preemptive resume
	 *
	 */

	public enum Policy {
		PRI,
		PRD,
		PRS;
	}

	/**
	 * Constructor 
	 * @param label A name of transitions
	 * @param index An integer for ID
	 * @param dist An object of AST for the distribution
	 * @param policy An object of general transition policy
	 * @param guard An object of AST for the guard function
	 * @param update An object of AST for the (additional) update function
	 * @param priority An integer for the priority
	 * @param vanishable A logical value to represent the transition can be vanished
	 */

	public GenTrans(String label, int index, AST dist, Policy policy,
			AST guard, AST update, int priority, boolean vanishable) {
		super(label, index, guard, update, priority, vanishable);
		this.dist = dist;
		this.policy = policy;
	}

	/**
	 * Getter for the distribution
	 * @return An object of distribution
	 */

	public final AST getDist() {
		return dist;
	}

	/**
	 * Getter for the policy
	 * @return An object of policy
	 */

	public final Policy getPolicy() {
		return policy;
	}
}
