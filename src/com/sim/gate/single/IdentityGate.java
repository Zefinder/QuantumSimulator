package com.sim.gate.single;

import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;
import com.sim.number.QBit;

public class IdentityGate implements SingleGate {

	@Override
	public QBit apply(QBit qbit) {
		return qbit;
	}

	@Override
	public Complex coefficient() {
		return Complex.ofRealConstant(1);
	}
	
	@Override
	public ComplexMatrix matrix() {
		return new ComplexMatrix.Builder().putCartesian(1, 0)
										  .putCartesian(0, 0)
										  .putCartesian(0, 0)
										  .putCartesian(1, 0)
										  .build(2);
	}
}
