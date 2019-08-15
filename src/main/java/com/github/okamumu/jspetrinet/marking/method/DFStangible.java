package com.github.okamumu.jspetrinet.marking.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.MarkingError;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.PetriAnalysis;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.nodes.ExpTrans;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.ImmTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

public class DFStangible implements CreateMarking {
	
	class Arc {
		Mark src;
		Mark dest;
		Trans tr;
		
		Arc(Mark src, Mark dest, Trans tr) {
			this.src = src;
			this.dest = dest;
			this.tr = tr;
		}
	}

	private static final Mark endMark = new Mark(null);
	
	private MarkingGraph markGraph;
	private Net net;

	private final Map<Mark,Mark> createdMarks;
	private final Map<GenVec,GenVec> genvecSet;
	private final Set<Mark> visited;
	private final LinkedList<Mark> novisited;
	private final LinkedList<Mark> novisitedIMM;
	private final Map<Mark,ExitMark> exitMarkSet;
	private final LinkedList<Mark> markPath;
	private final Map<Mark,GenVec> markToGenVec;
	private final List<Arc> arcList;

	private final Logger logger;
	private final PetriAnalysis analysis;
	
	public DFStangible() {
		createdMarks = new HashMap<Mark,Mark>();
		visited = new HashSet<Mark>();
		novisited = new LinkedList<Mark>();
		novisitedIMM = new LinkedList<Mark>();
		genvecSet = new HashMap<GenVec,GenVec>();
		markPath = new LinkedList<Mark>();
		exitMarkSet = new HashMap<Mark,ExitMark>();
		markToGenVec = new HashMap<Mark,GenVec>();
		arcList = new ArrayList<Arc>();

		logger = LoggerFactory.getLogger(DFStangible.class);
		analysis = PetriAnalysis.getInstance();
	}
	
	@Override
	public void create(MarkingGraph mg, Mark init, Net net, ASTEnv env) throws JSPNException {
		this.net = net;
		markGraph = mg;
		createdMarks.put(init, init);
		novisited.push(init);
		createMarking(net, env);
		postProcessing();
	}
	
	/**
	 * A method to generate a vector of status of transitions.
	 * 0: disabled, 1: enabled, 2: preempted
	 * @param m An instance of current marking
	 * @param net An instance of Petri net. This is required to get a set of gen trans.
	 * @param env An instance of environment
	 * @return A vector of status of transitions
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 */
	private int[] createGenVec(Mark m, Net net, ASTEnv env) throws ASTException {
		int[] vec = new int [net.getGenTransSet().size()];
		for (GenTrans tr : net.getGenTransSet()) {
			switch (analysis.isEnableGenTrans(m, env, tr)) {
			case ENABLE:
				vec[tr.getIndex()] = 1;
				break;
			case PREEMPTION:
				vec[tr.getIndex()] = 2;
				break;
			default:
			}
		}
		return vec;
	}
	
	/**
	 * A method to generate a list of transitions
	 * @param m An instance of current marking
	 * @param net An instance of Petri net. This is required to get a set of imm trans.
	 * @param env An instance of environment
	 * @return A list of enabled IMM transitions
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 */
	private List<Trans> createEnabledIMM(Mark m, Net net, ASTEnv env) throws ASTException {
		List<Trans> enabledIMMList = new ArrayList<Trans>();
		int highestPriority = 0;
		for (ImmTrans tr : net.getImmTransSet()) {
			if (highestPriority > tr.getPriority()) {
				break;
			}
			switch (analysis.isEnable(m, env, tr)) {
			case ENABLE:
				highestPriority = tr.getPriority();
				enabledIMMList.add(tr);
				break;
			default:
			}
		}
		return enabledIMMList;
	}
	
	/**
	 * A method to set a making as an element of marking group with one or more IMM transitions are enabled
	 * @param m An instance of current marking
	 * @param vec A vector of status of gen transitions
	 */
	private void setGenVecToImm(Mark m, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.IMM);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markToGenVec.put(m, genv);
		if (logger.isTraceEnabled())
			logger.trace("Add {} as Imm {}", m.toString(), genv.toString());
	}

	/**
	 * A method to set a making as an element of marking group with one or more no IMM transition is enabled
	 * @param m An instance of current marking
	 * @param vec A vector of status of gen transitions
	 */
	private void setGenVecToGen(Mark m, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.GEN);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markToGenVec.put(m, genv);
		if (logger.isTraceEnabled())
			logger.trace("Add {} as Gen {}", m.toString(), genv.toString());
	}

	/**
	 * A method to set a making as an element of absorbing marking group
	 * @param m An instance of current marking
	 * @param vec A vector of status of gen transitions
	 */
	private void setGenVecToAbs(Mark m, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.ABS);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markToGenVec.put(m, genv);
		if (logger.isTraceEnabled())
			logger.trace("Add {} as Abs {}", m.toString(), genv.toString());
	}

	/**
	 * A method to create next marking from the current marking by a firing of given transition
	 * @param m An instance of current marking
	 * @param env An instance of environment
	 * @param tr An instance of transition to be fired
	 * @return An instance of marking. The generated mark should keep its uniqueness.
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 * @throws MarkingError An error when the generated marking takes negative or exceeds the maximum
	 */
	private Mark createNextMarking(Mark m, ASTEnv env, Trans tr) throws ASTException, MarkingError {
		Mark dest = analysis.doFiring(m, env, tr);
		if (createdMarks.containsKey(dest)) {
			dest = createdMarks.get(dest);
		} else {
			createdMarks.put(dest, dest);
		}
		return dest;		
	}

	/**
	 * A method to add next markings generated by enabled IMM transitions to the novisited list
	 * @param m An instance of current marking
	 * @param enabledIMMList A list of enabled imm transitions
	 * @param env An instance of environment
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 * @throws MarkingError An error when the generated marking takes negative or exceeds the maximum
	 */
	private void visitImmMark(Mark m, List<Trans> enabledIMMList, ASTEnv env) throws ASTException, MarkingError {
		markPath.push(m);
		novisitedIMM.push(endMark);
		for (Trans tr : enabledIMMList) {
			Mark dest = createNextMarking(m, env, tr);
			novisitedIMM.push(dest);
			connectTo(m, dest, tr);
		}
	}
	
	/**
	 * A method to add next markings generated by enabled EXP and GEN transitions to the novisited list
	 * @param m An instance of current marking
	 * @param net An instance of net to get lists of EXP and GEN transitions
	 * @param env An instance of environment
	 * @return A boolean whether the current mark is absorbing state or not
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 * @throws MarkingError An error when the generated marking takes negative or exceeds the maximum
	 */
	private boolean visitGenMark(Mark m, Net net, ASTEnv env) throws ASTException, MarkingError {
		boolean noenabled = true;
		for (GenTrans tr : net.getGenTransSet()) {
			switch (analysis.isEnableGenTrans(m, env, tr)) {
			case ENABLE:
				noenabled = false;
				Mark dest = createNextMarking(m, env, tr);
				novisited.push(dest);
				connectTo(m, dest, tr);
				break;
			default:
			}
		}
		for (ExpTrans tr : net.getExpTransSet()) {
			switch (analysis.isEnable(m, env, tr)) {
			case ENABLE:
				noenabled = false;
				Mark dest = createNextMarking(m, env, tr);
				novisited.push(dest);
				connectTo(m, dest, tr);
				break;
			default:
			}
		}
		visited.add(m);
		exitMarkSet.put(m, ExitMark.finalize(m));
		return noenabled;
	}

	/**
	 * A method to connect two marks and add the information of transition
	 * @param src A source marking
	 * @param dest A destination marking
	 * @param tr A transition
	 * @throws MarkingError 
	 */
	private void connectTo(Mark src, Mark dest, Trans tr) {
//		src.new Arc(src, dest, tr);
		arcList.add(new Arc(src, dest, tr));
	}
	
	private void margeExitSet(Mark m, Mark other) throws MarkingError {
		ExitMark em = exitMarkSet.getOrDefault(m, ExitMark.init(m));
		ExitMark eo = exitMarkSet.getOrDefault(other, ExitMark.init(other));
		exitMarkSet.put(m, ExitMark.union(markToGenVec, em, eo));
	}

	private void vanishing(Net net, ASTEnv env) throws JSPNException {
		while (!novisitedIMM.isEmpty()) {
			Mark m = novisitedIMM.pop();

			/**
			 * If m is endMark,
			 * it finishes exploring children of the top of stack markPath.
			 * The child exit should be merged to the parent.
			 */
			if (m == endMark) {
				Mark e = markPath.pop();
				Mark r = markPath.peek();
				if (r != null) {
					margeExitSet(r, e);
				}
				visited.add(e);
				continue;
			}

			/**
			 * If m is visited, the exploring of m is also finished.
			 * Therefore, the merge is executed.
			 */
			if (visited.contains(m)) {
				Mark r = markPath.peek();
				margeExitSet(r, m);
				continue;
			}

			// new visit
			int[] vec = createGenVec(m, net, env);
			if (logger.isTraceEnabled())
				logger.trace("New visit {} (GenVec {}) in vanishing", m.toString(), Arrays.toString(vec));

			List<Trans> enabledIMMList = createEnabledIMM(m, net, env);
			if (enabledIMMList.size() > 0) {
				visitImmMark(m, enabledIMMList, env);
				setGenVecToImm(m, vec);
			} else {
				if (visitGenMark(m, net, env) == false) {
					setGenVecToGen(m, vec);
				} else {
					setGenVecToAbs(m, vec);
				}
				Mark r = markPath.peek();
				margeExitSet(r, m);
			}
		}
	}

	private void createMarking(Net net, ASTEnv env) throws JSPNException {
		while (!novisited.isEmpty()) {
			Mark m = novisited.pop();

			if (visited.contains(m)) {
				continue;
			}

			// new visit
			int[] vec = createGenVec(m, net, env);
			if (logger.isTraceEnabled())
				logger.trace("New visit {} (GenVec {})", m.toString(), Arrays.toString(vec));

			List<Trans> enabledIMMList = createEnabledIMM(m, net, env);
			if (enabledIMMList.size() > 0) {
				visitImmMark(m, enabledIMMList, env);
				setGenVecToImm(m, vec);
				vanishing(net, env);
			} else {
				if (visitGenMark(m, net, env) == false) {
					setGenVecToGen(m, vec);
				} else {
					setGenVecToAbs(m, vec);
				}
			}
		}
	}

	private void postProcessing() {
		// post processing 1
		for (Map.Entry<Mark,GenVec> entry : markToGenVec.entrySet()) {
			if (!exitMarkSet.get(entry.getKey()).canVanishing()) {
				this.markGraph.setGenVec(entry.getKey(), entry.getValue());
			}
		}
		for (Arc a : arcList) {
			Mark src = a.src;
			Mark dest = a.dest;
			Trans tr = a.tr;
			if (exitMarkSet.get(src).canVanishing() == false) {
				if (exitMarkSet.get(dest).canVanishing() == true) {
					src.new Arc(src, exitMarkSet.get(dest).get(), tr);
				} else {
					src.new Arc(src, dest, tr);				
				}
			}
		}
	}
}
