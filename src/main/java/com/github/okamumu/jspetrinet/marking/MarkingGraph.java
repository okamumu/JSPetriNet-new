package com.github.okamumu.jspetrinet.marking;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;

public class MarkingGraph {

	public static MarkingGraph create(Mark init, Net net, ASTEnv env, CreateMarking method) throws JSPNException {
		Mark init0 = new Mark(init.copy());
		MarkingGraph mg = new MarkingGraph();
		method.create(mg, init0, net, env);
		mg.makingGroupGraph();
		Collections.sort(mg.allgenvec);
		Collections.sort(mg.allmark);
		// count size for each group
		for (Map.Entry<GenVec,List<Mark>> entry : mg.markSet.entrySet()) {
			mg.genvecSize.put(entry.getKey(), entry.getValue().size());
			Collections.sort(entry.getValue());
		}
		mg.countNNZ();
		mg.createGenVecLabel();
		mg.createGenTransLabel(net);
		mg.createIndexForMarks();
		return mg;
	}

	private Mark imark;
	private final Map<Mark,GenVec> markToGenvec;
	private final Map<GenVec,Integer> genvecSize;
	private final Map<GenVec,Integer> genvecNnzSize;
	private final Map<GenVec,List<Mark>> markSet;
	private final List<GenVec> allgenvec;
	private final List<Mark> allmark;

	private final Map<Mark,Integer> markIndex;
	private final Map<GenVec,String> genvecLabel;
	private final Map<GenTrans,String> genTransLabel;

	private MarkingGraph() {
		markToGenvec = new HashMap<Mark,GenVec>();
		genvecSize = new HashMap<GenVec,Integer>();
		genvecNnzSize = new HashMap<GenVec,Integer>();
		markSet = new HashMap<GenVec,List<Mark>>();
		allgenvec = new ArrayList<GenVec>();
		allmark = new ArrayList<Mark>();
		markIndex = new HashMap<Mark,Integer>();
		genvecLabel = new HashMap<GenVec,String>();
		genTransLabel = new HashMap<GenTrans,String>();
	}
	
	private final int getTotal(GenVec.Type type, Map<GenVec,Integer> map) {
		int total = 0;
		for (Map.Entry<GenVec,Integer> entry : map.entrySet()) {
			if (entry.getKey().getType() == type) {
				total += entry.getValue();
			}
		}
		return total;		
	}

	public final int getTotalState(GenVec.Type type) {
		return getTotal(type, genvecSize);
	}
	
	public final int getTotalNNZ(GenVec.Type type) {
		return getTotal(type, genvecNnzSize);
	}

	public final Mark getInitialMark() {
		return imark;
	}

	public final void setInitialMark(Mark imark) {
		this.imark = imark;
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

	/**
	 * Getter for a map from GenVec to String (G0, I0, etc.)
	 * @return A map
	 */
	public Map<GenVec,String> getGenVecLabel() {
		return genvecLabel;
	}
	
	/**
	 * Getter for a string corresponding to general transition (P0, P1, etc.)
	 * @return A map
	 */
	public Map<GenTrans,String> getGenTransLabel() {
		return genTransLabel;
	}

	/**
	 * Getter for a map from a mark to an index
	 * @return A map
	 */
	public Map<Mark,Integer> getMarkIndex() {
		return markIndex;
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
	
	/**
	 * Make connections between GenVec groups
	 */
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

	/**
	 * Count NNZ transitions
	 */
	private void countNNZ() {
		for (Map.Entry<GenVec,List<Mark>> entry : markSet.entrySet()) {
			int c = 0;
			for (Mark m : entry.getValue()) {
				c += m.getOutArc().size();
			}
			genvecNnzSize.put(entry.getKey(),c);
		}
	}

	/**
	 * Create labels for GenVec groups (G0, G1, I0, A0, etc.)
	 */
	private void createGenVecLabel() {
		Map<String,Integer> index = new HashMap<String,Integer>();
		int next_index = 0;
		for (GenVec g : getGenVec()) {
			String key = Arrays.toString(g.copy());
			if (!index.containsKey(key)) {
				index.put(key, next_index);
				next_index++;
			}
			Integer i = index.get(key);
			switch(g.getType()) {
			case IMM:
				genvecLabel.put(g, "I" + i);
				break;
			case GEN:
				genvecLabel.put(g, "G" + i);
				break;
			case ABS:
				genvecLabel.put(g, "A" + i);
				break;
			default:
			}
		}
	}

	/**
	 * Create labels for gentrans (E, P0, P1, etc.)
	 * @param net An instance of Net
	 */
	private void createGenTransLabel(Net net) {
		int index = 0;
		genTransLabel.put(null, "E");
		for (GenTrans tr : net.getGenTransSet()) {
			genTransLabel.put(tr, "P" + index);
		}
	}

	/**
	 * Make indices of markings for each groups.
	 * The index starts with baseIndex
	 */
	private void createIndexForMarks() {
		Map<GenVec,List<Mark>> markSet = new HashMap<GenVec,List<Mark>>();
		for (Mark m : getMark()) {
			GenVec genv = getGenVec(m);
			if (!markSet.containsKey(genv)) {
				List<Mark> list = new ArrayList<Mark>();
				markSet.put(genv, list);				
			}
			List<Mark> list = markSet.get(genv);
			markIndex.put(m, list.size());
			list.add(m);
		}
	}
	
}
