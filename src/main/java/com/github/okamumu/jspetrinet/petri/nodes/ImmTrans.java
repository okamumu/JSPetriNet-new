package com.github.okamumu.jspetrinet.petri.nodes;

import com.github.okamumu.jspetrinet.ast.AST;

public final class ImmTrans extends Trans {
	
	private final AST weight;
	
	/**
	 * Constructor 
	 * @param label A name of transitions
	 * @param index An integer for ID
	 * @param weight An object of AST for the weight function
	 * @param guard An object of AST for the guard function
	 * @param update An object of AST for the (additional) update function
	 * @param priority An integer for the priority
	 * @param vanishable A logical value to represent the transition can be vanished
	 */

	public ImmTrans(String label, int index, AST weight, AST guard, AST update,
			int priority, boolean vanishable) {
		super(label, index, guard, update, priority, vanishable);
		this.weight = weight;
	}
	
	/**
	 * Getter for the weight function
	 * @return An object of AST
	 */

	public final AST getWeight() {
		return weight;
	}

}
