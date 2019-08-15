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

/**
 * A class to write MarkingMatrix as a style of MATLAB matfile
 *
 */
public class MatlabMatrixWriter {

	/**
	 * A method to write MarkingMatrix to file
	 * @param file A string of file name
	 * @param env An instance of environment
	 * @param mat A instance of marking matrix
	 * @throws ASTException An error when AST is changed to numeric
	 * @throws IOException An error on file
	 */
	public static void write(String file, ASTEnv env, MarkingMatrix mat) throws ASTException, IOException {
		MatlabMatrixWriter writer = new MatlabMatrixWriter();
		MATLABMatFile matfile = writer.createMatlabMatrix(env, mat);
		matfile.writeToFile(Paths.get(file).toFile());
	}

	private final Logger logger;
	
	/**
	 * Constructor
	 */
	private MatlabMatrixWriter() {
        logger = LoggerFactory.getLogger(MatlabMatrixWriter.class);
	}

	/**
	 * A method to create MATLAB matrix file
	 * @param env An instance of environment
	 * @param mat An instance of marking matrix
	 * @return An instance of MATLAB matrix file
	 * @throws ASTException An error when AST is converted to numeric.
	 */
	private MATLABMatFile createMatlabMatrix(ASTEnv env, MarkingMatrix mat) throws ASTException {
		MATLABMatFile matfile = new MATLABMatFile(MATLABEndian.LittleEndian);
		for (Map.Entry<GTuple,ASTMatrix> m : mat.getMatrixSet().entrySet()) {
			String label = mat.getMarkingGraph().getGenVecLabel().get(m.getKey().getSrc())
					+ mat.getMarkingGraph().getGenVecLabel().get(m.getKey().getDest())
					+ mat.getMarkingGraph().getGenTransLabel().get(m.getKey().getGenTrans());
			ASTMatrix am = m.getValue();
			matfile.addDataElement(MATLABDataElement.doubleSparseMatrix(label,
					new int[] {am.getISize(), am.getJSize()},
					am.getNNZ(), am.getI(), am.getJ(), am.getValue(env)));
			if (logger.isTraceEnabled()) {
				logger.trace("Add matrix {} ({},{})", label, am.getISize(), am.getJSize());
				logger.trace(" {} {}", Arrays.toString(am.getI()), Arrays.toString(am.getJ()));
				logger.trace(" {}", Arrays.toString(am.getValue(env)));
			}
		}
		for (Map.Entry<GTuple,ASTVector> v : mat.getSumVectorSet().entrySet()) {
			String label = "sum" + mat.getMarkingGraph().getGenVecLabel().get(v.getKey().getSrc())
					+ mat.getMarkingGraph().getGenTransLabel().get(v.getKey().getGenTrans());
			ASTVector av = v.getValue();
			matfile.addDataElement(MATLABDataElement.doubleMatrix(label,
					new int[] {1,av.getSize()}, av.getValue(env)));			
			if (logger.isTraceEnabled()) {
				logger.trace("Add sum {} ({})", label, av.getSize());
				logger.trace(" {}", Arrays.toString(av.getValue(env)));
			}
		}
		for (Map.Entry<GenVec,ASTVector> v : mat.getInitVectorSet().entrySet()) {
			String label = "init" + mat.getMarkingGraph().getGenVecLabel().get(v.getKey());
			ASTVector av = v.getValue();
			matfile.addDataElement(MATLABDataElement.doubleMatrix(label,
					new int[] {1,av.getSize()}, av.getValue(env)));
			if (logger.isTraceEnabled()) {
				logger.trace("Add init {} ({})", label, av.getSize());
				logger.trace(" {}", Arrays.toString(av.getValue(env)));
			}
		}
		for (Map.Entry<GStringTuple,ASTVector> v : mat.getRewardVectorSet().entrySet()) {
			String label = v.getKey().getGenString()
					+ mat.getMarkingGraph().getGenVecLabel().get(v.getKey().getGenVec());
			ASTVector av = v.getValue();
			matfile.addDataElement(MATLABDataElement.doubleMatrix(label,
					new int[] {1,av.getSize()}, av.getValue(env)));
			if (logger.isTraceEnabled()) {
				logger.trace("Add reward {} ({})", label, av.getSize());
				logger.trace(" {}", Arrays.toString(av.getValue(env)));
			}
		}
		return matfile;
	}
	
}
