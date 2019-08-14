package com.github.okamumu.jspetrinet.marking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.graph.Arc;
import com.github.okamumu.jspetrinet.marking.method.CreateMarking;
import com.github.okamumu.jspetrinet.petri.Net;

public class MarkingGraph {

	public static MarkingGraph create(Mark init, Net net, ASTEnv env, CreateMarking method) throws JSPNException {
		MarkingGraph mg = new MarkingGraph(init);
		method.create(mg, init, net, env);
		mg.makingGroupGraph();
		Collections.sort(mg.allgenvec);
		Collections.sort(mg.allmark);
		for (Map.Entry<GenVec,List<Mark>> entry : mg.markSet.entrySet()) {
			mg.genvecSize.put(entry.getKey(), entry.getValue().size());
			Collections.sort(entry.getValue());
		}
		return mg;
	}

	private Mark imark;
	private final Map<Mark,GenVec> markToGenvec;
	private final Map<GenVec,Integer> genvecSize;
	private final Map<GenVec,List<Mark>> markSet;
	private final List<GenVec> allgenvec;
	private final List<Mark> allmark;

	private MarkingGraph(Mark imark) {
		markToGenvec = new HashMap<Mark,GenVec>();
		genvecSize = new HashMap<GenVec,Integer>();
		markSet = new HashMap<GenVec,List<Mark>>();
		allgenvec = new ArrayList<GenVec>();
		allmark = new ArrayList<Mark>();
		this.imark = imark;
	}

	public final int immSize() {
		int total = 0;
		for (Map.Entry<GenVec,Integer> entry : genvecSize.entrySet()) {
			if (entry.getKey().getType() == GenVec.Type.IMM) {
				total += entry.getValue();
			}
		}
		return total;
	}
	
	public final int genSize() {
		int total = 0;
		for (Map.Entry<GenVec,Integer> entry : genvecSize.entrySet()) {
			if (entry.getKey().getType() == GenVec.Type.GEN) {
				total += entry.getValue();
			}
		}
		return total;
	}

	public final int absSize() {
		int total = 0;
		for (Map.Entry<GenVec,Integer> entry : genvecSize.entrySet()) {
			if (entry.getKey().getType() == GenVec.Type.ABS) {
				total += entry.getValue();
			}
		}
		return total;
	}

	public final Mark getInitialMark() {
		return imark;
	}

	public final List<GenVec> getGenVec() {
		return allgenvec;
	}

	public final GenVec getGenVec(Mark m) {
		return markToGenvec.get(m);
	}

	public final List<Mark> getMark() {
		return allmark;
	}

	public final Integer getGenVecSize(GenVec g) {
		return genvecSize.get(g);
	}
	
	public final Map<GenVec,List<Mark>> getMarkSet() {
		return markSet;
	}

	public final void setGenVec(Mark m, GenVec g) {
		markToGenvec.put(m, g);
		if (!markSet.containsKey(g)) {
			markSet.put(g, new ArrayList<Mark>());
			allgenvec.add(g);
		}
		markSet.get(g).add(m);
		allmark.add(m);
	}
	
	private void makingGroupGraph() {
		class GenVecTuple {
			final GenVec src;
			final GenVec dest;
			
			GenVecTuple(GenVec src, GenVec dest) {
				this.src = src;
				this.dest = dest;
			}

			@Override
			public int hashCode() {
				return Objects.hash(dest, src);
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				GenVecTuple other = (GenVecTuple) obj;
				return Objects.equals(dest, other.dest) && Objects.equals(src, other.src);
			}
		}
		
		Set<GenVecTuple> created = new HashSet<GenVecTuple>();
		for (Map.Entry<Mark,GenVec> entry : markToGenvec.entrySet()) {
			Mark m = entry.getKey();
			GenVec gsrc = entry.getValue();
			for (Arc a : m.getOutArc()) {
				Mark.Arc ma = (Mark.Arc) a;
				Mark dest = (Mark) ma.getDest();
				GenVec gdest = markToGenvec.get(dest);
				GenVecTuple tuple = new GenVecTuple(gsrc, gdest);
				if (!created.contains(tuple)) {
					gsrc.new Arc(gsrc, gdest);
					created.add(tuple);
				}
			}
		}
	}

}
