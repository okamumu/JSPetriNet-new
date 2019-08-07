package com.github.okamumu.jspetrinet.writer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jmatout.MATLABDataElement;
import com.github.okamumu.jmatout.MATLABEndian;
import com.github.okamumu.jmatout.MATLABMatFile;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.matrix.ASTMatrix;
import com.github.okamumu.jspetrinet.matrix.ASTVector;
import com.github.okamumu.jspetrinet.matrix.GStringTuple;
import com.github.okamumu.jspetrinet.matrix.GTuple;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;

public class MatlabMatrixWriter {

	private ASTEnv env;
	private MarkingMatrix mat;
	private final Logger logger;
	
	public MatlabMatrixWriter(ASTEnv env, MarkingMatrix mat) {
		this.env = env;
		this.mat = mat;
        logger = LoggerFactory.getLogger(MatlabMatrixWriter.class);
	}

	public void writeMatlabMatrix(String fileName) throws ASTException {
		MATLABMatFile matfile = new MATLABMatFile(MATLABEndian.LittleEndian);
		for (Map.Entry<GTuple,ASTMatrix> m : mat.getMatrixSet().entrySet()) {
			String label = mat.getGenVecLabel().get(m.getKey().getSrc())
					+ mat.getGenVecLabel().get(m.getKey().getDest())
					+ mat.getGenTransLabel().get(m.getKey().getGenTrans());
			ASTMatrix am = m.getValue();
			matfile.addDataElement(MATLABDataElement.doubleSparseMatrix(label,
					new int[] {am.getISize(), am.getJSize()},
					am.getNNZ(), am.getI(), am.getJ(), am.getValue(env)));
			logger.trace("Add matrix {} ({},{})", label, am.getISize(), am.getJSize());
			logger.trace(" {} {}", Arrays.toString(am.getI()), Arrays.toString(am.getJ()));
			logger.trace(" {}", Arrays.toString(am.getValue(env)));
		}
		for (Map.Entry<GTuple,ASTVector> v : mat.getSumVectorSet().entrySet()) {
			String label = "sum" + mat.getGenVecLabel().get(v.getKey().getSrc())
					+ mat.getGenTransLabel().get(v.getKey().getGenTrans());
			ASTVector av = v.getValue();
			matfile.addDataElement(MATLABDataElement.doubleMatrix(label,
					new int[] {1,av.getSize()}, av.getValue(env)));			
			logger.trace("Add sum {} ({})", label, av.getSize());
			logger.trace(" {}", Arrays.toString(av.getValue(env)));
		}
		for (Map.Entry<GenVec,ASTVector> v : mat.getInitVectorSet().entrySet()) {
			String label = "init" + mat.getGenVecLabel().get(v.getKey());
			ASTVector av = v.getValue();
			matfile.addDataElement(MATLABDataElement.doubleMatrix(label,
					new int[] {1,av.getSize()}, av.getValue(env)));
			logger.trace("Add init {} ({})", label, av.getSize());
			logger.trace(" {}", Arrays.toString(av.getValue(env)));
		}
		for (Map.Entry<GStringTuple,ASTVector> v : mat.getRewardVectorSet().entrySet()) {
			String label = v.getKey().getGenString()
					+ mat.getGenVecLabel().get(v.getKey().getGenVec());
			ASTVector av = v.getValue();
			matfile.addDataElement(MATLABDataElement.doubleMatrix(label,
					new int[] {1,av.getSize()}, av.getValue(env)));
			logger.trace("Add reward {} ({})", label, av.getSize());
			logger.trace(" {}", Arrays.toString(av.getValue(env)));
		}
		try {
		  matfile.writeToFile(Paths.get(fileName).toFile());
		} catch (IOException e) {
		  e.printStackTrace();
		}
	}
	
}
