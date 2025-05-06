package com.sim.gate.single;

import com.sim.number.Complex;
import com.sim.number.QBit;

public class PauliYGate implements SingleGate {

	private static final Complex I = Complex.I;
	private static final Complex NEG_I = Complex.I.negate();

	@Override
	public QBit apply(QBit qbit) {
		Complex oldZeroState = qbit.getZeroStateValue();
		Complex oldOneState = qbit.getOneStateValue();

		Complex newZeroState = oldOneState.multiply(NEG_I);
		Complex newOneState = oldZeroState.multiply(I);
		QBit newQbit = new QBit(newZeroState, newOneState);

		return newQbit;
	}

}
