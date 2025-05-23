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
	
	public static Gate combineN(Gate gate, int n) {
		if (n < 0) {
			n = 0;
		}
		
		Gate[] gates = new Gate[n];
		for (int i= 0; i < n; i++) {
			gates[i] = gate;
		}
		
		return combine(gates);
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
