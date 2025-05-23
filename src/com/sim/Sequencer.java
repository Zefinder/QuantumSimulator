package com.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sim.exception.GateNotGoodSizeException;
import com.sim.exception.MatrixProductException;
import com.sim.exception.QNumberNotGoodSizeException;
import com.sim.gate.Combiner;
import com.sim.gate.Gate;
import com.sim.gate.single.IdentityGate;
import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;
import com.sim.number.QNumber;

public class Sequencer {

	private final int bitNumber;
	private final Oracle oracle;
	private final int stateNumber;

	private final List<Gate> gateList;
	private QNumber qresult;

	/**
	 * <p>
	 * Creates a new sequencer that starts with the specified QNumber. The sequencer
	 * is works with steps. It considers that after each step, the oracle will
	 * perform a measure. If the step is not the last step, the oracle will use the
	 * {@link Oracle#quantumPredict(QNumber)} method. If it is the last step, the
	 * oracle is not used
	 * </p>
	 * 
	 * <p>
	 * If you want to measure, you can either do it yourself with
	 * {@link QNumber#check()}, or you can use the
	 * </p>
	 * 
	 * <p>
	 * To create a step, you might want to use the {@link SequencerStep} class,
	 * unless there is only one gate for the step so you can use the
	 * {@link #addStep(Gate)}. Use the {@link Combiner} to easily create gates from
	 * basic single gates.
	 * </p>
	 * 
	 * @see Oracle
	 */
	public Sequencer(int bitNumber, Oracle oracle) {
		this.bitNumber = bitNumber;
		this.oracle = oracle;
		stateNumber = 1 << bitNumber;
		gateList = new ArrayList<Gate>();
	}

	public void addStep(Gate gate) throws GateNotGoodSizeException {
		ComplexMatrix matrix = gate.matrix();

		// If matrix is not stateNumber x stateNumber, then there is a problem...
		if (matrix.getRows() != matrix.getColumns() || matrix.getRows() != stateNumber) {
			throw new GateNotGoodSizeException("Gate matrix must be a %d x %d matrix (currently %d x %d)"
					.formatted(stateNumber, stateNumber, matrix.getRows(), matrix.getColumns()));
		}

		gateList.add(gate);
	}

	public void addStep(SequencerStep step) throws GateNotGoodSizeException, MatrixProductException {
		for (int i = 0; i < step.repeatCounter; i++) {
			addStep(step.build());
		}
	}

	public QNumber run(QNumber start) throws QNumberNotGoodSizeException {
		if (start.getBitNumber() != bitNumber) {
			throw new QNumberNotGoodSizeException(
					"The input QNumber must be %d qbits (currently %d)".formatted(bitNumber, start.getBitNumber()));
		}

		QNumber qresult = start;

		try {
			for (int i = 0; i < gateList.size(); i++) {
				Gate gate = gateList.get(i);

				ComplexMatrix gateMatrix = gate.matrix();
				Complex gateCoefficient = gate.coefficient();
				ComplexMatrix numberCoefficients = qresult.getCoefficientVector();

				ComplexMatrix newCoefficients = gateMatrix.product(numberCoefficients).timesConstant(gateCoefficient);
				qresult = new QNumber(qresult.getBitNumber(), newCoefficients);

				// If not the last index, then quantum prediction
				if (i != gateList.size() - 1) {
					qresult = oracle.quantumPredict(qresult);
				}
			}
		} catch (MatrixProductException e) {
			// This should never happen since gates are checked with the start size and
			// product keeps the same size
			e.printStackTrace();
		}

		return qresult;
	}

	public int measure() {
		if (qresult == null) {
			return -1;
		}

		return qresult.check();
	}

	public boolean evaluate() {
		if (qresult == null) {
			return false;
		}

		int measured = qresult.check();
		return oracle.predict(measured);
	}

	public static class SequencerStep {

		private Complex coefficient;
		private final Stack<ComplexMatrix> matrixStack;

		private int repeatCounter;

		/**
		 * <p>
		 * Merge gates that are serially wired. Put the gates in the builder the way
		 * they appear in the schema. This uses a stack to make the matrix product. If
		 * you have the following schema:
		 * 
		 * <pre>
		 * |s> -- Z -- Y -- X --
		 * </pre>
		 * 
		 * Then the gates X and Y and Z can be merged. To do so, use the following code:
		 * 
		 * <pre>
		 * Gate mergedGate = new Gate.Builder().addGate(Z).addGate(Y).addGate(X).build();
		 * </pre>
		 * 
		 * This will output a gate that is X . Y . Z as expected
		 * </p>
		 * 
		 * <p>
		 * If no gates were added, this will return the identity gate.
		 * </p>
		 * 
		 * <p>
		 * For parallel gates, see {@link Combiner}.
		 * </p>
		 * 
		 * @see Combiner
		 */
		public SequencerStep() {
			this.coefficient = Complex.ONE;
			this.matrixStack = new Stack<ComplexMatrix>();
			this.repeatCounter = 1;
		}

		public SequencerStep addGate(Gate gate) {
			coefficient = coefficient.multiply(gate.coefficient());
			matrixStack.push(gate.matrix());
			return this;
		}

		public SequencerStep addGate(ComplexMatrix matrix, Complex coefficient) {
			coefficient = coefficient.multiply(coefficient);
			matrixStack.push(matrix);
			return this;
		}

		/**
		 * <p>
		 * Set the step to repeat n times. This corresponds to adding n times this step.
		 * <p>
		 * 
		 * <p>
		 * For example:
		 * 
		 * <pre>
		 * Sequencer s = new Sequencer(qnumber);
		 * SequencerStep step = new SequencerStep();
		 * step.addGate(Combiner.combineN(new HadamardGate(), 2)).repeat(3);
		 * s.addStep(step);
		 * </pre>
		 * 
		 * Is the equivalent of:
		 * 
		 * <pre>
		 * Sequencer s = new Sequencer(qnumber);
		 * SequencerStep step = new SequencerStep();
		 * step.addGate(Combiner.combineN(new HadamardGate(), 2));
		 * s.addStep(step);
		 * s.addStep(step);
		 * s.addStep(step);
		 * </pre>
		 * </p>
		 */
		public void repeat(int n) {
			if (n > 0) {
				repeatCounter = n;
			}
		}

		private Gate build() throws MatrixProductException {
			if (matrixStack.isEmpty()) {
				return new IdentityGate();
			}

			ComplexMatrix result = matrixStack.pop();
			while (!matrixStack.isEmpty()) {
				result = result.product(matrixStack.pop());
			}

			Gate gate = new MergedGate(coefficient, result);
			return gate;
		}

		private static class MergedGate implements Gate {

			private final Complex coefficient;
			private final ComplexMatrix matrix;

			public MergedGate(Complex coefficient, ComplexMatrix matrix) {
				this.coefficient = coefficient;
				this.matrix = matrix;
			}

			@Override
			public Complex coefficient() {
				return coefficient;
			}

			@Override
			public ComplexMatrix matrix() {
				return matrix;
			}
		}
	}
}
