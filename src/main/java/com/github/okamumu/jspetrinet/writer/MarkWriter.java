package com.github.okamumu.jspetrinet.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.okamumu.jspetrinet.graph.Arc;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Place;

public class MarkWriter {
	private final Net net;
	private final MarkingGraph mgraph;

	private final PrintWriter bw;

	private static String ln = "\n";
	private static String genFormat = "\"%s\" [label=\"%s\n %s\"];" + ln;
	private static String immFormat = "\"%s\" [label=\"%s\n %s\", style=filled];" + ln;
	private static String arcFormat = "\"%s\" -> \"%s\" [label=\"%s\"];" + ln;

	private static String genFormatG = "\"%s\" [label=\"%s\n(%d)\"];" + ln;
	private static String immFormatG = "\"%s\" [label=\"%s\n(%d)\", style=filled];" + ln;
	private static String absFormatG = "\"%s\" [label=\"%s\n(%d)\"];" + ln;
	private static String arcFormatG = "\"%s\" -> \"%s\" [];" + ln;

	/**
	 * Write a graph of marking (System.out)
	 * @param net An instance of Net
	 * @param mgraph An instance of MarkingGraph
	 */
	public static void writeMark(Net net, MarkingGraph mgraph) {
		PrintWriter bw = new PrintWriter(System.out, true);
		MarkWriter pviz = new MarkWriter(net, mgraph, bw);
		pviz.dotMark();
	}
	
	/**
	 * Write a graph of marking (file)
	 * @param file A name of file
	 * @param net An instance of Net
	 * @param mgraph An instance of MarkingGraph
	 * @throws IOException An error on file IO
	 */
	public static void writeMark(String file, Net net, MarkingGraph mgraph) throws IOException {
		BufferedWriter buf = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8);
		PrintWriter bw = new PrintWriter(buf, false);
		MarkWriter pviz = new MarkWriter(net, mgraph, bw);
		pviz.dotMark();
		bw.close();
	}

	/**
	 * Write a graph of Group of marking (System.out)
	 * @param net An instance of Net
	 * @param mgraph An instance of MarkingGraph
	 */
	public static void writeMarkGroup(Net net, MarkingGraph mgraph) {
		PrintWriter bw = new PrintWriter(System.out, true);
		MarkWriter pviz = new MarkWriter(net, mgraph, bw);
		pviz.dotMarkGroup();
	}

	/**
	 * Write a graph of Group of marking (file)
	 * @param file A name of file
	 * @param net An instance of Net
	 * @param mgraph An instance of MarkingGraph
	 * @throws IOException An error on file IO
	 */
	public static void writeMarkGroup(String file, Net net, MarkingGraph mgraph) throws IOException {
		BufferedWriter buf = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8);
		PrintWriter bw = new PrintWriter(buf, false);
		MarkWriter pviz = new MarkWriter(net, mgraph, bw);
		pviz.dotMarkGroup();
		bw.close();
	}

	private MarkWriter(Net net, MarkingGraph mgraph, PrintWriter bw) {
		this.net = net;
		this.mgraph = mgraph;
		this.bw = bw;
	}
	
	private void dotMark() {
		bw.println("digraph { layout=dot; overlap=false; splines=true;");
		for (Mark m : mgraph.getMark()) {
			GenVec g = mgraph.getGenVec(m);
			switch (g.getType()) {
			case IMM:
				bw.printf(immFormat, m, makeLabel(m), makeLabel(g));
				break;
			case GEN:
				bw.printf(genFormat, m, makeLabel(m), makeLabel(g));
				break;
			case ABS:
				bw.printf(genFormat, m, makeLabel(m), makeLabel(g));
				break;
			default:
			}
			for (Arc a : m.getOutArc()) {
				Mark.Arc ma = (Mark.Arc) a;
				bw.printf(arcFormat, ma.getSrc(), ma.getDest(), ma.getTrans().getLabel());
			}
		}
		bw.println("}");
	}

	public void dotMarkGroup() {
		bw.println("digraph { layout=dot; overlap=false; splines=true;");
		for (GenVec genv : mgraph.getGenVec()) {
			Integer size = mgraph.getGenVecSize(genv);
			switch (genv.getType()) {
			case IMM:
				bw.printf(immFormatG, genv, makeLabel(genv), size);
				break;
			case GEN:
				bw.printf(genFormatG, genv, makeLabel(genv), size);
				break;
			case ABS:
				bw.printf(absFormatG, genv, makeLabel(genv), size);
				break;
			default:
			}
			for (Arc a : genv.getOutArc()) {
				GenVec.Arc ma = (GenVec.Arc) a;
				bw.printf(arcFormatG, ma.getSrc(), ma.getDest());
			}
		}
		bw.println("}");
	}

	private String makeLabel(Mark m) {
		return mgraph.getMarkIndex().get(m).toString();
		// detal version
//			String result = "";
//			for (Place p : net.getPlaceSet()) {
//				if (m.get(p.getIndex()) != 0) {
//					if (result.equals("")) {
//						result = p.getLabel() + ":" + m.get(p.getIndex());						
//					} else {
//						result = result + "," + p.getLabel() + ":" + m.get(p.getIndex());						
//					}
//				}
//			}
//			return result;
		}

	private String makeLabel(GenVec genv) {
		return mgraph.getGenVecLabel().get(genv);
//		String result = "(";
//		for (GenTrans t: net.getGenTransSet()) {
//			switch(genv.get(t.getIndex())) {
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
//		result += ")";
//		return result;
	}
}
