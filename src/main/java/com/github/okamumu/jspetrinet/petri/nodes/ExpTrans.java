package com.github.okamumu.jspetrinet.petri.nodes;

import com.github.okamumu.jspetrinet.ast.AST;

public class ExpTrans extends Trans {
	
	private final AST rate;
	
	/**
	 * Constructor 
	 * @param label A name of transitions
	 * @param index An integer for ID
	 * @param rate An object of AST for the rate function
	 * @param guard An object of AST for the guard function
	 * @param update An object of AST for the (additional) update function
	 * @param priority An integer for the priority
	 * @param vanishable A logical value to represent the transition can be vanished
	 */

	public ExpTrans(String label, int index, AST rate, AST guard, AST update,
			int priority, boolean vanishable) {
		super(label, index, guard, update, priority, vanishable);
		this.rate = rate;
	}
	
	/**
	 * Getter for the rate function
	 * @return An object of AST
	 */

	public final AST getRate() {
		return rate;
	}

}
