package com.github.okamumu.jspetrinet.matrix;

import java.util.HashMap;
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

/**
 * A class to store markings as matrices
 *
 */
public class MarkingMatrix {
	
	/**
	 * Create all matrices and vectors (init, reward and sum)
	 * @param net An instance of Net
	 * @param env An instance of environment
	 * @param mp An instance of MarkingGraph (the result of DFS or BFS search)
	 * @param baseIndex An integer for the start index of matrix. baseIndex = 0 means C-style array
	 * @throws ASTException An exception when AST to numeric
	 */
	public static MarkingMatrix create(Net net, ASTEnv env, MarkingGraph mp, int baseIndex) throws ASTException {
		MarkingMatrix mm = new MarkingMatrix(mp);
//		mm.createGenVecLabel(mp);
//		mm.createGenTransLabel(net);
//		mm.createIndexForMarks(mp, baseIndex);
		mm.createTransitionMatrix(env, mp);
		mm.createInitVector(mp);
		mm.createRewardVector(net, env, mp);		
		return mm;
	}

	private final PetriAnalysis analysis;
	private final MarkingGraph mp;
//	private final Map<Mark,Integer> markIndex;
//	private final Map<GenVec,String> genvecLabel;
//	private final Map<GenTrans,String> genTransLabel;
	private final Map<GTuple,ASTMatrix> matrixSet;
	private final Map<GTuple,ASTVector> sumvecSet;
	private final Map<GenVec,ASTVector> initvecSet;
	private final Map<GStringTuple,ASTVector> rewardvecSet;

	/**
	 * Constructor
	 */
	private MarkingMatrix(MarkingGraph mp) {
		this.mp = mp;
//		markIndex = new HashMap<Mark,Integer>();
//		genvecLabel = new HashMap<GenVec,String>();
//		genTransLabel = new HashMap<GenTrans,String>();
		matrixSet = new HashMap<GTuple,ASTMatrix>();
		sumvecSet = new HashMap<GTuple,ASTVector>();
		initvecSet = new HashMap<GenVec,ASTVector>();
		rewardvecSet = new HashMap<GStringTuple,ASTVector>();
		analysis = PetriAnalysis.getInstance();
	}
	
	/**
	 * Getter for a marking graph
	 * @return An instance of marking graph
	 */	
	public MarkingGraph getMarkingGraph() {
		return mp;
	}
	
	/**
	 * Getter for a map from Gtuple (GenVec, GenVec, GenTrans) to ASTMatrix.
	 * This is used to get Map.Entry
	 * @return A map
	 */
	public Map<GTuple,ASTMatrix> getMatrixSet() {
		return matrixSet;
	}
	
	/**
	 * Getter for a map from Gtuple (GenVec, GenVec, GenTrans) to ASTVector for sum of rows.
	 * This is used to get Map.Entry
	 * @return A map
	 */
	public Map<GTuple,ASTVector> getSumVectorSet() {
		return sumvecSet;
	}
	
	/**
	 * Getter for a map from GenVec to ASTVector for initial marking.
	 * This is used to get Map.Entry
	 * @return A map
	 */
	public Map<GenVec,ASTVector> getInitVectorSet() {
		return initvecSet;
	}

	/**
	 * Getter for a map from GStringTuple (String, GenVec) to ASTVector for reward.
	 * This is used to get Map.Entry
	 * @return A map
	 */
	public Map<GStringTuple,ASTVector> getRewardVectorSet() {
		return rewardvecSet;
	}

//	/**
//	 * Create labels for GenVec groups (G0, G1, I0, A0, etc.)
//	 * @param mp An instance of marking graph
//	 */
//	private void createGenVecLabel(MarkingGraph mp) {
//		Map<String,Integer> index = new HashMap<String,Integer>();
//		int next_index = 0;
//		for (GenVec g : mp.getGenVec()) {
//			String key = Arrays.toString(g.copy());
//			if (!index.containsKey(key)) {
//				index.put(key, next_index);
//				next_index++;
//			}
//			Integer i = index.get(key);
//			switch(g.getType()) {
//			case IMM:
//				mp.genvecLabel.put(g, "I" + i);
//				break;
//			case GEN:
//				genvecLabel.put(g, "G" + i);
//				break;
//			case ABS:
//				genvecLabel.put(g, "A" + i);
//				break;
//			default:
//			}
//		}
//	}
//
//	/**
//	 * Create labels for gentrans (E, P0, P1, etc.)
//	 * @param net An instance of Net
//	 */
//	private void createGenTransLabel(Net net) {
//		int index = 0;
//		genTransLabel.put(null, "E");
//		for (GenTrans tr : net.getGenTransSet()) {
//			genTransLabel.put(tr, "P" + index);
//		}
//	}
//
//	/**
//	 * Make indices of markings for each groups.
//	 * The index starts with baseIndex
//	 * @param mp An instance of marking graph
//	 * @param baseIndex An integer to represent the baseIndex
//	 */
//	private void createIndexForMarks(MarkingGraph mp, int baseIndex) {
//		Map<GenVec,List<Mark>> markSet = new HashMap<GenVec,List<Mark>>();
//		for (Mark m : mp.getMark()) {
//			GenVec genv = mp.getGenVec(m);
//			if (!markSet.containsKey(genv)) {
//				List<Mark> list = new ArrayList<Mark>();
//				markSet.put(genv, list);				
//			}
//			List<Mark> list = markSet.get(genv);
//			markIndex.put(m, baseIndex + list.size());
//			markSet.get(genv).add(m);
//		}
//	}
	
	/**
	 * Create transition matrices and their row sums for each group.
	 * This should be called after createIndexForMarks.
	 * This method uses getASTMatrix and getASTVector as inner methods.
	 * @param env An instance of environment
	 * @param mp An instance of marking graph
	 * @throws ASTException An error when AST is converted to numeric
	 */
	private void createTransitionMatrix(ASTEnv env, MarkingGraph mp) throws ASTException {
		for (Map.Entry<Mark,Integer> entry : mp.getMarkIndex().entrySet()) {
			Mark src = entry.getKey();
			Integer i = entry.getValue();
			GenVec gsrc = mp.getGenVec(src);
			for (Arc a : src.getOutArc()) {
				Mark.Arc ma = (Mark.Arc) a;
				Mark dest = (Mark) ma.getDest();
				GenVec gdest = mp.getGenVec(dest);
				if (ma.getTrans() instanceof ImmTrans) {
					ImmTrans tr = (ImmTrans) ma.getTrans();
					GTuple gtuple = new GTuple(gsrc, gdest, null);
					GTuple gtupleSum = new GTuple(gsrc, gsrc, null);
					Integer j = mp.getMarkIndex().get(dest);
					ASTMatrix mat = getASTMatrix(mp, gtuple);
					ASTVector sum = getASTVector(mp, gtupleSum);
					AST value = ASTValue.getAST(analysis.astEval(tr.getWeight(), src, env));
					mat.add(i, j, value);
					sum.set(i, new ASTArithmetic(sum.get(i), value, "+"));
				} else if (ma.getTrans() instanceof ExpTrans) {
					ExpTrans tr = (ExpTrans) ma.getTrans();
					GTuple gtuple = new GTuple(gsrc, gdest, null);
					GTuple gtupleSum = new GTuple(gsrc, gsrc, null);
					Integer j = mp.getMarkIndex().get(dest);
					ASTMatrix mat = getASTMatrix(mp, gtuple);
					ASTVector sum = getASTVector(mp, gtupleSum);
					AST value = ASTValue.getAST(analysis.astEval(tr.getRate(), src, env));
					mat.add(i, j, value);
					sum.set(i, new ASTArithmetic(sum.get(i), value, "+"));
				} else if (ma.getTrans() instanceof GenTrans) {
					GenTrans tr = (GenTrans) ma.getTrans();
					GTuple gtuple = new GTuple(gsrc, gdest, tr);
					GTuple gtupleSum = new GTuple(gsrc, gsrc, tr);
					Integer j = mp.getMarkIndex().get(dest);
					ASTMatrix mat = getASTMatrix(mp, gtuple);
					ASTVector sum = getASTVector(mp, gtupleSum);
					AST value = ASTValue.getAST(1);
					mat.add(i, j, value);
					sum.set(i, new ASTArithmetic(sum.get(i), value, "+"));
				}
			}
		}
	}

	/**
	 * Get an instance of ASTMatrix corresponding to one tuple (GenVec, GenVec, GenTrans)
	 * If it does not exits yet in the map, create an instance of ASTMatrix and put it to map.
	 * An inner method in createTransitionMatrix.
	 * @param mp An instance of marking graph
	 * @param gtuple An instance of tuple (GenVec, GenVec, GenTrans)
	 * @return An instance of ASTMatrix
	 */
	private final ASTMatrix getASTMatrix(MarkingGraph mp, GTuple gtuple) {
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
	
	/**
	 * Get an instance of ASTVector corresponding to one tuple (GenVec, GenVec, GenTrans)
	 * If it does not exits yet in the map, create an instance of ASTVector and put it to map.
	 * An inner method in createTransitionMatrix.
	 * @param mp An instance of marking graph
	 * @param gtuple An instance of tuple (GenVec, GenVec, GenTrans)
	 * @return An instance of ASTVector
	 */
	private final ASTVector getASTVector(MarkingGraph mp, GTuple gtuple) {
		if (!sumvecSet.containsKey(gtuple)) {
			int size = mp.getGenVecSize(gtuple.getSrc());
			sumvecSet.put(gtuple, new ASTVector(size));	
		}
		return sumvecSet.get(gtuple);
	}
	
	/**
	 * Create initial marking vector
	 * @param mp An instance of marking graph
	 */
	private void createInitVector(MarkingGraph mp) {
		Mark init = mp.getInitialMark();
		for (GenVec genv : mp.getGenVec()) {
			ASTVector vec = new ASTVector(mp.getGenVecSize(genv));
			if (mp.getGenVec(init).equals(genv)) {
				vec.set(mp.getMarkIndex().get(init), ASTValue.getAST(1.0));
			}
			initvecSet.put(genv, vec);
		}
	}

	/**
	 * Create reward vectors
	 * @param net An instance of Net
	 * @param env An instance of environment
	 * @param mp An instance of marking graph
	 * @throws ASTException An error when AST is changed to numeric
	 */
	private void createRewardVector(Net net, ASTEnv env, MarkingGraph mp) throws ASTException {
		for (GenVec genv : mp.getGenVec()) {
			ASTVector vec = new ASTVector(mp.getGenVecSize(genv));
			for (String label : net.getRewardSet()) {
				for (Mark m : mp.getMarkSet().get(genv)) {
					AST f = (AST) env.get(label);
					vec.set(mp.getMarkIndex().get(m), ASTValue.getAST(analysis.astEval(f, m, env)));
				}
				GStringTuple tuple = new GStringTuple(label, genv);
				this.rewardvecSet.put(tuple, vec);
			}
		}
	}
}
