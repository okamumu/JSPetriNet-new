package com.github.okamumu.jspetrinet.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTNull;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.graph.Arc;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.arcs.ArcBase;
import com.github.okamumu.jspetrinet.petri.arcs.InArc;
import com.github.okamumu.jspetrinet.petri.arcs.InhibitArc;
import com.github.okamumu.jspetrinet.petri.arcs.OutArc;
import com.github.okamumu.jspetrinet.petri.nodes.ExpTrans;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.ImmTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for write the graph of Petri net
 *
 */
public class PNWriter {

	private final Set<Object> visited;
	private final LinkedList<Object> novisited;
	private final ASTEnv env;
	private final Net net;

	private final PrintWriter bw;
	
	private static String ln = "\n";
	private static String placeFMT = "\"%s\" [shape = circle, label = \"%s\"];" + ln;
	private static String expFMT = "\"%s\" [shape = box, label = \"%s\" width=0.8, height=0.2];" + ln;
	private static String immFMT = "\"%s\" [shape = box, label = \"%s\" width=0.8, height=0.02, style=\"filled,dashed\"];" + ln;
	private static String genFMT = "\"%s\" [shape = box, label = \"%s\" width=0.8, height=0.2, style=filled];" + ln;
	private static String arcFMT = "\"%s\" -> \"%s\" [label = \"%s\"];" + ln;
	private static String harcFMT = "\"%s\" -> \"%s\" [label = \"%s\", arrowhead=odot];" + ln;

	/**
	 * A method to write the Petri net graph to PrintWriter (System.out)
	 * @param net An instance of Petri net
	 * @param env An instance of environment
	 */
	public static void write(Net net, ASTEnv env) {
		PrintWriter bw = new PrintWriter(System.out, true);
		PNWriter pviz = new PNWriter(net, env, bw);
		pviz.doDraw();
	}
	
	/**
	 * A method to write the Petri net graph to PrintWriter (file)
	 * @param file A file name
	 * @param net An instance of Petri net
	 * @param env An instance of environment
	 * @throws IOException 
	 */
	public static void write(String file, Net net, ASTEnv env) throws IOException {
		BufferedWriter buf = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8);
		PrintWriter bw = new PrintWriter(buf, false);
		PNWriter pviz = new PNWriter(net, env, bw);
		pviz.doDraw();
		bw.close();
	}

	private PNWriter(Net net, ASTEnv env, PrintWriter bw) {
		visited = new HashSet<Object>();
		novisited = new LinkedList<Object>();
		this.env = env;
		this.net = net;
		this.bw = bw;
	}
	
	private void doDraw() {
		novisited.addAll(net.getPlaceSet());
		novisited.addAll(net.getImmTransSet());
		novisited.addAll(net.getExpTransSet());
		novisited.addAll(net.getGenTransSet());
		while (!novisited.isEmpty()) {
			Object c = novisited.removeFirst();
			if (!visited.contains(c)) {
				draw(c);
			}
		}
	}

	private void draw(Object start) {
		LinkedList<Object> novisited = new LinkedList<Object>();
		novisited.addLast(start);
		bw.println("digraph { layout=dot; overlap=false; splines=true; node [fontsize=10];");
		while (!novisited.isEmpty()) {
			Object component = novisited.removeFirst();
			if (visited.contains(component)) {
				continue;
			}
			if (component instanceof Place) {
				Place c =  (Place) component;
				bw.printf(placeFMT, c, c.getLabel());
				for (Arc a : c.getInArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getSrc());
				}
				for (Arc a : c.getOutArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getDest());
				}
			} else if (component instanceof ExpTrans) {
				ExpTrans c =  (ExpTrans) component;
				bw.printf(expFMT, c, this.makeTransLabel(c));
				for (Arc a : c.getInArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getSrc());
				}
				for (Arc a : c.getOutArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getDest());
				}
			} else if (component instanceof ImmTrans) {
				ImmTrans c =  (ImmTrans) component;
				bw.printf(immFMT, c, this.makeTransLabel(c));
				for (Arc a : c.getInArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getSrc());
				}
				for (Arc a : c.getOutArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getDest());
				}
			} else if (component instanceof GenTrans) {
				GenTrans c =  (GenTrans) component;
				bw.printf(genFMT, c, this.makeTransLabel(c));
				for (Arc a : c.getInArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getSrc());
				}
				for (Arc a : c.getOutArc()) {
					novisited.addLast(a);
					novisited.addLast(a.getDest());
				}
			} else if (component instanceof InArc) {
				InArc ac = (InArc) component;
				bw.printf(arcFMT, ac.getSrc(), ac.getDest(), this.makeArcLabel(ac));
			} else if (component instanceof OutArc) {
				OutArc ac = (OutArc) component;
				bw.printf(arcFMT, ac.getSrc(), ac.getDest(), this.makeArcLabel(ac));
			} else if (component instanceof InhibitArc) {
				InhibitArc ac = (InhibitArc) component;			
				bw.printf(harcFMT, ac.getSrc(), ac.getDest(), this.makeArcLabel(ac));
			}			
			visited.add(component);
		}
		bw.println("}");		
	}

	private String makeTransLabel(Trans tr) {
		String label = tr.getLabel();

		// guard
		AST guard = tr.getGuard();
		try {
			Object obj = guard.eval(env);
			if (obj instanceof Boolean) {
				if ((Boolean) obj != true) {
					label += ln + "[" + obj.toString() + "]";
				}
			} else {
				label += ln + "[" + obj.toString() + "]";
			}
		} catch (ASTException e) {
			label += ln + "[" + guard.toString() + "]";
		}
		
		// update
		AST update = tr.getUpdate();
		if (!(update instanceof ASTNull)) {
			label += ln + "{" + update.toString() + "}";
		}
		
		return label;
	}

	private String makeArcLabel(ArcBase arc) {
		Object obj;
		try {
			obj = arc.getMulti().eval(env);
		} catch (ASTException e) {
			return arc.getMulti().toString();
		}
		int m;
		if (obj instanceof Integer) {
			m = (Integer) obj;
		} else if (obj instanceof Double) {
			m = ((Double) obj).intValue();
		} else {
			return obj.toString();
		}
		if (m != 1) {
			return Integer.toString(m);
		} else {
			return "";
		}
	}
}
