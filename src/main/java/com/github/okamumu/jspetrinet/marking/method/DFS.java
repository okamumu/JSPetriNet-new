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

public class DFS implements CreateMarking {
	
	private MarkingGraph markGraph;

	private Map<Mark,Mark> createdMarks;
	private Set<Mark> visited;

	private LinkedList<Mark> novisitedIMM;
	private LinkedList<Mark> novisitedGEN;
	
	private Map<GenVec,GenVec> genvecSet;
	
	private final Logger logger;
	private final PetriAnalysis analysis;
	
	public DFS() {
		logger = LoggerFactory.getLogger(DFS.class);
		analysis = PetriAnalysis.getInstance();
	}
	
	@Override
	public void create(MarkingGraph mg, Mark init, Net net, ASTEnv env) throws JSPNException {
		markGraph = mg;
		createdMarks = new HashMap<Mark,Mark>();
		visited = new HashSet<Mark>();
		novisitedGEN = new LinkedList<Mark>();
		novisitedIMM = new LinkedList<Mark>();
		genvecSet = new HashMap<GenVec,GenVec>();
		createdMarks.put(init, init);
		novisitedGEN.push(init);
		createMarking(net, env);
	}
	
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
	
	private void setGenVecToImm(Mark m, Net net, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.IMM);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markGraph.setGenVec(m, genv);
		logger.trace("Add {} as Imm {}", m.toString(), genv.toString());
	}

	private void setGenVecToGen(Mark m, Net net, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.GEN);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markGraph.setGenVec(m, genv);
		logger.trace("Add {} as Gen {}", m.toString(), genv.toString());
	}

	private void setGenVecToAbs(Mark m, Net net, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.ABS);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markGraph.setGenVec(m, genv);
		logger.trace("Add {} as Abs {}", m.toString(), genv.toString());
	}

	private void visitImmMark(Mark m, Net net, ASTEnv env, List<Trans> enabledIMMList) throws ASTException, MarkingError {
		for (Trans tr : enabledIMMList) {
			Mark dest = analysis.doFiring(m, env, tr);
			if (createdMarks.containsKey(dest)) {
				dest = createdMarks.get(dest);
			} else {
				createdMarks.put(dest, dest);
			}
			novisitedIMM.push(dest);
			connectTo(m, dest, tr);
		}
		visited.add(m);
	}
	
	private boolean visitGenMark(Mark m, Net net, ASTEnv env) throws ASTException, MarkingError {
		boolean noenabled = true;
		for (GenTrans tr : net.getGenTransSet()) {
			switch (analysis.isEnableGenTrans(m, env, tr)) {
			case ENABLE:
				noenabled = false;
				Mark dest = analysis.doFiring(m, env, tr);
				if (createdMarks.containsKey(dest)) {
					dest = createdMarks.get(dest);
				} else {
					createdMarks.put(dest, dest);
				}
				novisitedGEN.push(dest);
				connectTo(m, dest, tr);
				break;
			default:
			}
		}
		for (ExpTrans tr : net.getExpTransSet()) {
			switch (analysis.isEnable(m, env, tr)) {
			case ENABLE:
				noenabled = false;
				Mark dest = analysis.doFiring(m, env, tr);
				if (createdMarks.containsKey(dest)) {
					dest = createdMarks.get(dest);
				} else {
					createdMarks.put(dest, dest);
				}
				novisitedGEN.push(dest);
				connectTo(m, dest, tr);
				break;
			default:
			}
		}
		visited.add(m);
		return noenabled;
	}

	private void connectTo(Mark src, Mark dest, Trans tr) {
		src.new Arc(src, dest, tr);
	}

	private void vanishing(Net net, ASTEnv env) throws ASTException, MarkingError {
		while (!novisitedIMM.isEmpty()) {
			Mark m = novisitedIMM.pop();
			if (visited.contains(m)) {
				continue;
			}
			
			// new visit
			int[] vec = createGenVec(m, net, env);
			logger.trace("New visit {} (GenVec {})", m.toString(), Arrays.toString(vec));

			List<Trans> enabledIMMList = createEnabledIMM(m, net, env);			
			if (enabledIMMList.size() > 0) {
				visitImmMark(m, net, env, enabledIMMList);
				setGenVecToImm(m, net, vec);
			} else {
				if (visitGenMark(m, net, env) == false) {
					setGenVecToGen(m, net, vec);
				} else {
					setGenVecToAbs(m, net, vec);
				}
			}
		}
	}
	
	private void createMarking(Net net, ASTEnv env) throws JSPNException {
		while (!novisitedGEN.isEmpty()) {
			Mark m = novisitedGEN.pop();
			if (visited.contains(m)) {
				continue;
			}

			// new visit
			int[] vec = createGenVec(m, net, env);
			logger.trace("New visit {} (GenVec {})", m.toString(), Arrays.toString(vec));

			List<Trans> enabledIMMList = createEnabledIMM(m, net, env);
			if (enabledIMMList.size() > 0) {
				visitImmMark(m, net, env, enabledIMMList);
				vanishing(net, env);
				setGenVecToImm(m, net, vec);
			} else {
				if (visitGenMark(m, net, env) == false) {
					setGenVecToGen(m, net, vec);
				} else {
					setGenVecToAbs(m, net, vec);
				}
			}
		}
	}
}
