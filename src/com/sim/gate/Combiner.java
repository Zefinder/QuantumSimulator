package com.sim.gate;

import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;

public final class Combiner {

	private Combiner() {
	}

	public static Gate combine(Gate... gates) {
		CombinedGate gate;

		if (gates.length <= 0) {
			gate = null;

		} else {
			Complex coefficient = gates[0].coefficient();
			ComplexMatrix resultMatrix = gates[0].matrix();

			for (int i = 1; i < gates.length; i++) {
				coefficient = coefficient.multiply(gates[i].coefficient());
				resultMatrix = resultMatrix.kronecker(gates[i].matrix());
			}

			gate = new CombinedGate(coefficient, resultMatrix);
		}

		return gate;
	}

	private static class CombinedGate implements Gate {

		private final Complex coefficient;
		private final ComplexMatrix matrix;

		public CombinedGate(Complex coefficient, ComplexMatrix matrix) {
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
