package com.sim.gate.single;

import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;
import com.sim.number.QBit;

public class PauliXGate implements SingleGate {

	@Override
	public QBit apply(QBit qbit) {
		Complex oldZeroState = qbit.getZeroStateValue();
		Complex oldOneState = qbit.getOneStateValue();
		QBit newQbit = new QBit(oldOneState, oldZeroState);

		return newQbit;
	}

	@Override
	public Complex coefficient() {
		return Complex.ofRealConstant(1);
	}
	
	@Override
	public ComplexMatrix matrix() {
		return new ComplexMatrix.Builder().putCartesian(0, 0)
										  .putCartesian(1, 0)
										  .putCartesian(1, 0)
										  .putCartesian(0, 0)
										  .build(2);
	}
	
}
