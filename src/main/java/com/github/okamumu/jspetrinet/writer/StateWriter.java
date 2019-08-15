package com.github.okamumu.jspetrinet.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Place;

public class StateWriter {

	public static void write(Net net, MarkingGraph mp, MarkingMatrix mat) {
		PrintWriter bw = new PrintWriter(System.out, true);
		StateWriter writer = new StateWriter();
		writer.writeState(net, mp, mat, bw);
	}

	public static void write(String file, Net net, MarkingGraph mp, MarkingMatrix mat) throws IOException {
		BufferedWriter buf = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8);
		PrintWriter bw = new PrintWriter(buf, false);
		StateWriter writer = new StateWriter();
		writer.writeState(net, mp, mat, bw);
		bw.close();
	}

	private final Logger logger;
	
	private StateWriter() {
        logger = LoggerFactory.getLogger(StateWriter.class);
	}

	private void writeState(Net net, MarkingGraph mp, MarkingMatrix mat, PrintWriter bw) {
		for (GenVec g : mp.getGenVec()) {
			String s = mat.getMarkingGraph().getGenVecLabel().get(g) + " " + makeLabel(net, g);
			bw.println(s);
			if (logger.isTraceEnabled()) logger.trace(s);
			for (Mark m : mp.getMarkSet().get(g)) {
				String str = "" + mat.getMarkingGraph().getMarkIndex().get(m) + " " + makeLabel(net, m);
				bw.println(str);
				if (logger.isTraceEnabled()) logger.trace(str);
			}
		}
	}
	
	private String makeLabel(Net net, Mark m) {
		String result = "";
		for (Place p : net.getPlaceSet()) {
			if (m.get(p.getIndex()) != 0) {
				if (result.equals("")) {
					result = p.getLabel() + ":" + m.get(p.getIndex());						
				} else {
					result = result + "," + p.getLabel() + ":" + m.get(p.getIndex());						
				}
			}
		}
		return result;
	}

	private String makeLabel(Net net, GenVec genv) {
		String result = "(";
		for (GenTrans t: net.getGenTransSet()) {
			switch(genv.get(t.getIndex())) {
			case 0:
				break;
			case 1:
				if (!result.equals("(")) {
					result += " ";
				}
				result += t.getLabel() + "->enable";
				break;
			case 2:
				if (!result.equals("(")) {
					result += " ";
				}
				result += t.getLabel() + "->preemption";
				break;
			default:
				break;
			}
		}
		if (result.equals("(")) {
			result += "EXP";
		}
		result += ")";
		return result;
	}
}
