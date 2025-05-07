package com.sim.gate.single;

import com.sim.number.Complex;
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
	public double coefficient() {
		return INV_SQRT_2;
	}
	
	@Override
	public double[][] matrix() {
		return new double[][] { { 1, 1 }, { 1, -1 } };
	}

}
