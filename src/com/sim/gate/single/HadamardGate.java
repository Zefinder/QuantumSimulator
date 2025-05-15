package com.sim.gate.single;

import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;
import com.sim.number.QBit;

public class HadamardGate implements SingleGate {

	private static final double INV_SQRT_2 = 0.7071067811865476;

	@Override
	public QBit apply(QBit qbit) {
		Complex oldZeroState = qbit.getZeroStateValue();
		Complex oldOneState = qbit.getOneStateValue();

		Complex newZeroState = oldZeroState.add(oldOneState).multiply(INV_SQRT_2);
		Complex newOneState = oldZeroState.subtract(oldOneState).multiply(INV_SQRT_2);
		QBit newQbit = new QBit(newZeroState, newOneState);

		return newQbit;
	}

	@Override
	public Complex coefficient() {
		return Complex.ofRealConstant(INV_SQRT_2);
	}
	
	@Override
	public ComplexMatrix matrix() {
		return new ComplexMatrix.Builder().putCartesian(1, 0)
										  .putCartesian(1, 0)
										  .putCartesian(1, 0)
										  .putCartesian(-1, 0)
										  .build(2);
	}

}
