package com.sim.main.grover;

import com.sim.Oracle;
import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;
import com.sim.number.QNumber;

public class GroverOracle implements Oracle {

	private final int trueValue;

	public GroverOracle(int value) {
		this.trueValue = value;
	}

	@Override
	public boolean predict(int value) {
		return trueValue == value;
	}

	@Override
	public QNumber quantumPredict(QNumber qvalue) {
		ComplexMatrix oldCoefficients = qvalue.getCoefficientVector();

		// Grover's algorithm negates the coefficient for the good state value
		ComplexMatrix.Builder builder = new ComplexMatrix.Builder();
		for (int i = 0; i < oldCoefficients.getRows(); i++) {
			Complex c = oldCoefficients.get(i, 0);
			builder.putComplex(i == trueValue ? c.negate() : c);
		}

		QNumber newValue = new QNumber(qvalue.getBitNumber(), builder.build(1));
		return newValue;
	}

}
