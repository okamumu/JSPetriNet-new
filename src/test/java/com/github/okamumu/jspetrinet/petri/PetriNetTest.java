package com.github.okamumu.jspetrinet.petri;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;

public class PetriNetTest {
	
	Net net;

    @Before
    public void setUp() {
    	ArrayList<Place> places = new ArrayList<Place>();
    	ArrayList<ImmTrans> imm = new ArrayList<ImmTrans>();
    	ArrayList<ExpTrans> exp = new ArrayList<ExpTrans>();
    	ArrayList<GenTrans> gen = new ArrayList<GenTrans>();
    	places.add(new Place("P1", 0, 10));
    	places.add(new Place("P2", 0, 10));
    	places.add(new Place("P3", 0, 10));
    	imm.add(new ImmTrans("T1", 1, ASTValue.getAST(1.0), ASTValue.getAST(true), null, 0, false));
    	imm.add(new ImmTrans("T2", 2, ASTValue.getAST(1.0), ASTValue.getAST(true), null, 0, false));
    	imm.add(new ImmTrans("T3", 3, ASTValue.getAST(1.0), ASTValue.getAST(true), null, 0, false));
    	new InArc(places.get(0), imm.get(0), ASTValue.getAST(1));
    	new InArc(places.get(1), imm.get(0), ASTValue.getAST(1));
    	new InArc(places.get(2), imm.get(1), ASTValue.getAST(1));
    	new InArc(places.get(2), imm.get(2), ASTValue.getAST(1));
    	new OutArc(imm.get(0), places.get(1), ASTValue.getAST(1));
    	new OutArc(imm.get(1), places.get(2), ASTValue.getAST(1));
    	new OutArc(imm.get(2), places.get(0), ASTValue.getAST(1));
    	net = new Net(null, places, imm, exp, gen, null);
    }

    @Test
	public void testPrintNet() {
    	PNWriter.write(new PrintWriter(System.out), net, null);
	}

}
