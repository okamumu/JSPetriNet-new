package com.github.okamumu.jspetrinet.marking.method;

import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.petri.Net;

public interface CreateMarking {
	
	/**
	 * Create a marking graph from an initial mark, Petrinet (net) and formulas (env)
	 * @param mg An instance of marking graph
	 * @param init An instance of initial mark
	 * @param net An instance of Petri net
	 * @param env An instance of environment
	 * @throws JSPNException An error to create marking graph
	 */
	public void create(MarkingGraph mg, Mark init, Net net, ASTEnv env) throws JSPNException;

}
