package com.github.okamumu.jspetrinet.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.operators.ASTArithmetic;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.graph.Arc;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.PetriAnalysis;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.nodes.ExpTrans;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.ImmTrans;

public class MarkingMatrix {
	
	private final PetriAnalysis analysis;

	private final Map<Mark,Integer> markIndex;
	private final Map<GenVec,String> genvecLabel;
	private final Map<GenTrans,String> genTransLabel;
	private final Map<GTuple,ASTMatrix> matrixSet;
	private final Map<GTuple,ASTVector> sumvecSet;
	private final Map<GenVec,ASTVector> initvecSet;
	private final Map<GStringTuple,ASTVector> rewardvecSet;
		
	public MarkingMatrix(Net net, ASTEnv env, MarkingGraph mp, int baseIndex) {
		markIndex = new HashMap<Mark,Integer>();
		genvecLabel = new HashMap<GenVec,String>();
		genTransLabel = new HashMap<GenTrans,String>();
		matrixSet = new HashMap<GTuple,ASTMatrix>();
		sumvecSet = new HashMap<GTuple,ASTVector>();
		initvecSet = new HashMap<GenVec,ASTVector>();
		rewardvecSet = new HashMap<GStringTuple,ASTVector>();
		analysis = PetriAnalysis.getInstance();
		try {
			createGenVecLabel(mp);
			createGenTransLabel(net);
			create(net, env, mp, baseIndex);
			createInitVector(mp);
			createRewardVector(net, env, mp);
		} catch (ASTException e) {
			e.printStackTrace();
		}
	}
	
	public Map<GenVec,String> getGenVecLabel() {
		return genvecLabel;
	}
	
	public Map<GenTrans,String> getGenTransLabel() {
		return genTransLabel;
	}

	public Map<Mark,Integer> getMarkIndex() {
		return markIndex;
	}

	public Map<GTuple,ASTMatrix> getMatrixSet() {
		return matrixSet;
	}
	
	public Map<GTuple,ASTVector> getSumVectorSet() {
		return sumvecSet;
	}
	
	public Map<GenVec,ASTVector> getInitVectorSet() {
		return initvecSet;
	}

	public Map<GStringTuple,ASTVector> getRewardVectorSet() {
		return rewardvecSet;
	}

	private void createGenVecLabel(MarkingGraph mp) {
		Map<String,Integer> index = new HashMap<String,Integer>();
		int next_index = 0;
		for (GenVec g : mp.getGenVec()) {
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

	private void createGenTransLabel(Net net) {
		int index = 0;
		genTransLabel.put(null, "E");
		for (GenTrans tr : net.getGenTransSet()) {
			genTransLabel.put(tr, "P" + index);
		}
	}

	private void create(Net net, ASTEnv env, MarkingGraph mp, int baseIndex) throws ASTException {
		/*
		 * Make indices of markings for each groups
		 * The index starts with baseIndex
		 */
		Map<GenVec,List<Mark>> markSet = new HashMap<GenVec,List<Mark>>();
		for (Mark m : mp.getMark()) {
			GenVec genv = mp.getGenVec(m);
			if (!markSet.containsKey(genv)) {
				List<Mark> list = new ArrayList<Mark>();
				markSet.put(genv, list);				
			}
			List<Mark> list = markSet.get(genv);
			markIndex.put(m, baseIndex + list.size());
			markSet.get(genv).add(m);
		}
		
		/*
		 * Make transition matrices
		 */
		for (Mark src : mp.getMark()) {
			GenVec gsrc = mp.getGenVec(src);
			for (Arc a : src.getOutArc()) {
				Mark.Arc ma = (Mark.Arc) a;
				Mark dest = (Mark) ma.getDest();
				GenVec gdest = mp.getGenVec(dest);
				if (ma.getTrans() instanceof ImmTrans) {
					ImmTrans tr = (ImmTrans) ma.getTrans();
					GTuple gtuple = new GTuple(gsrc, gdest, null);
					GTuple gtupleSum = new GTuple(gsrc, gsrc, null);
					Integer i = markIndex.get(src);
					Integer j = markIndex.get(dest);
					ASTMatrix mat = getASTMatrix(mp, gtuple);
					ASTVector sum = getASTVector(mp, gtupleSum);
					AST value = ASTValue.getAST(analysis.astEval(tr.getWeight(), src, env));
					mat.add(i, j, value);
					sum.set(i, new ASTArithmetic(sum.get(i), value, "+"));
				} else if (ma.getTrans() instanceof ExpTrans) {
					ExpTrans tr = (ExpTrans) ma.getTrans();
					GTuple gtuple = new GTuple(gsrc, gdest, null);
					GTuple gtupleSum = new GTuple(gsrc, gsrc, null);
					Integer i = markIndex.get(src);
					Integer j = markIndex.get(dest);
					ASTMatrix mat = getASTMatrix(mp, gtuple);
					ASTVector sum = getASTVector(mp, gtupleSum);
					AST value = ASTValue.getAST(analysis.astEval(tr.getRate(), src, env));
					mat.add(i, j, value);
					sum.set(i, new ASTArithmetic(sum.get(i), value, "+"));
				} else if (ma.getTrans() instanceof GenTrans) {
					GenTrans tr = (GenTrans) ma.getTrans();
					GTuple gtuple = new GTuple(gsrc, gdest, tr);
					GTuple gtupleSum = new GTuple(gsrc, gsrc, tr);
					Integer i = markIndex.get(src);
					Integer j = markIndex.get(dest);
					ASTMatrix mat = getASTMatrix(mp, gtuple);
					ASTVector sum = getASTVector(mp, gtupleSum);
					AST value = ASTValue.getAST(1);
					mat.add(i, j, value);
					sum.set(i, new ASTArithmetic(sum.get(i), value, "+"));
				}
			}
		}
	}

	private ASTMatrix getASTMatrix(MarkingGraph mp, GTuple gtuple) {
		if (!matrixSet.containsKey(gtuple)) {
			int isize = mp.getGenVecSize(gtuple.getSrc());
			int jsize = mp.getGenVecSize(gtuple.getDest());
			matrixSet.put(gtuple, new ASTMatrix(isize, jsize));
			GTuple gdiag = new GTuple(gtuple.getSrc(), gtuple.getSrc(), gtuple.getGenTrans());
			if (!matrixSet.containsKey(gdiag)) {
				matrixSet.put(gdiag, new ASTMatrix(isize, isize));
//				matrixSet.get(gdiag).add(0, 0, ASTValue.getAST(0));
			}
		}
		return matrixSet.get(gtuple);
	}
	
	private ASTVector getASTVector(MarkingGraph mp, GTuple gtuple) {
		if (!sumvecSet.containsKey(gtuple)) {
			int size = mp.getGenVecSize(gtuple.getSrc());
			sumvecSet.put(gtuple, new ASTVector(size));	
		}
		return sumvecSet.get(gtuple);
	}
	
	private void createInitVector(MarkingGraph mp) {
		Mark init = mp.getInitialMark();
		for (GenVec genv : mp.getGenVec()) {
			ASTVector vec = new ASTVector(mp.getGenVecSize(genv));
			if (mp.getGenVec(init).equals(genv)) {
				vec.set(this.markIndex.get(init), ASTValue.getAST(1.0));
			}
			initvecSet.put(genv, vec);
		}
	}

	private void createRewardVector(Net net, ASTEnv env, MarkingGraph mp) throws ASTException {
		for (GenVec genv : mp.getGenVec()) {
			ASTVector vec = new ASTVector(mp.getGenVecSize(genv));
			for (String label : net.getRewardSet()) {
				for (Mark m : mp.getMarkSet().get(genv)) {
					AST f = (AST) env.get(label);
					vec.set(markIndex.get(m), ASTValue.getAST(analysis.astEval(f, m, env)));
				}
				GStringTuple tuple = new GStringTuple(label, genv);
				this.rewardvecSet.put(tuple, vec);
			}
		}
	}
}
