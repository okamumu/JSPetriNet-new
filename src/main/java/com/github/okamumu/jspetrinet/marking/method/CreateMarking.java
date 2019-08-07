package com.github.okamumu.jspetrinet.marking.method;

import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.petri.Net;

public interface CreateMarking {
	
	public void create(MarkingGraph mg, Mark init, Net net, ASTEnv env) throws JSPNException;

}
