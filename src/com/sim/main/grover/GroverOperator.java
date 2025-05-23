package com.sim.main.grover;

import com.sim.exception.MatrixProductException;
import com.sim.gate.Combiner;
import com.sim.gate.Gate;
import com.sim.gate.single.IdentityGate;
import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;
import com.sim.number.QNumber;

public class GroverOperator implements Gate {

	private ComplexMatrix operatorMatrix;

	public GroverOperator(int bitNumber) throws MatrixProductException {
		// Corresponds to H^n (2 |0^n> <0^n| - I^n) H^n
		ComplexMatrix In = Combiner.combineN(new IdentityGate(), bitNumber).matrix();
		ComplexMatrix zeroStateMatrix = new QNumber(bitNumber).getCoefficientVector();
		
		operatorMatrix = zeroStateMatrix
				.product(zeroStateMatrix.conjugateTranspose())
				.timesConstant(Complex.ofCartesian(2, 0))
				.sub(In);
	}

	@Override
	public Complex coefficient() {
		return Complex.ONE;
	}

	@Override
	public ComplexMatrix matrix() {
		return operatorMatrix;
	}

}
